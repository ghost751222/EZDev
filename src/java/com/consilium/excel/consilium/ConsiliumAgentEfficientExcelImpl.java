package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.UserInstance;
import com.consilium.excel.models.agentefficient.AgentEfficientDataObject;
import com.consilium.excel.models.agentefficient.AgentEfficientReportObject;
import com.consilium.excel.models.talk.CalendarUtil;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("ConsiliumAgentEfficientExcelImpl")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConsiliumAgentEfficientExcelImpl implements ExcelInterface {

    private static final Logger logger = Logger.getLogger(ConsiliumAgentEfficientExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "話務績效統計表.xls";

    private String reportPath = "/WEB-INF/report/consilium/AgentefficiencyV2D.xls";

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sqlQuery = "";
        String calllogsql = "";
        NumberFormat format = NumberFormat.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DecimalFormat dformat = new DecimalFormat("#.##");
        Calendar now = Calendar.getInstance();

        String timeType = jsonNode.get("timeType").asText();
        String apendSQL = "";

        String userID = jsonNode.get("user").asText();
        Hashtable<String, AgentEfficientDataObject> valueTable = new Hashtable<String, AgentEfficientDataObject>();

        format.setMinimumIntegerDigits(2);

        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();

        String bDataTime_YYYY = startDate.substring(0, 4);
        String eDataTime_YYYY = endDate.substring(0, 4);
        String bDataTime_MM = startDate.substring(5, 7);
        String bDataTime_DD = startDate.substring(8, 10);
        String eDataTime_MM = endDate.substring(5, 7);
        String eDataTime_DD = endDate.substring(8, 10);

        AgentEfficientReportObject prao = new AgentEfficientReportObject();
        ArrayList al = new ArrayList();
        int monArray[] = new int[13];
        monArray[0] = 0;
        monArray[1] = 31;
        monArray[2] = monArray[1] + 29;
        monArray[3] = monArray[2] + 31;
        monArray[4] = monArray[3] + 30;
        monArray[5] = monArray[4] + 31;
        monArray[6] = monArray[5] + 30;
        monArray[7] = monArray[6] + 31;
        monArray[8] = monArray[7] + 31;
        monArray[9] = monArray[8] + 30;
        monArray[10] = monArray[9] + 31;
        monArray[11] = monArray[10] + 30;
        monArray[12] = monArray[11] + 31;
        prao.setStartDate(bDataTime_YYYY + "-" + bDataTime_MM + '-' + bDataTime_DD);
        prao.setEndDate(eDataTime_YYYY + "-" + eDataTime_MM + '-' + eDataTime_DD);
        prao.setPrintDate(dateFormater.format(now.getTime()));
        Map beans = new HashMap();

        String dateSql = "";
        String dateFiled = "";
        String cDateSql = "";
        String cDateFiled = "";
        String appendSQL = "";

        if (!"A".equals(timeType)) {
            appendSQL = " AND U.USERID in (SELECT value FROM   STRING_SPLIT (?, ',') ) ";
            if ("Y".equals(timeType))//年
            {
                dateSql = " , format(A.F_START_TIME, 'yyyy') ";
                dateFiled = " BEGINTIME ";

                cDateSql = " , format(CallLog_Form.CREATETIME, 'yyyy') ";
                cDateFiled = "CREATETIME ";

            } else if ("M".equals(timeType))//月
            {
                dateSql = " , format(A.F_START_TIME, 'yyyy-MM') ";
                dateFiled = " BEGINTIME ";

                cDateSql = " , format(CallLog_Form.CREATETIME, 'yyyy-MM') ";
                cDateFiled = "CREATETIME ";

            } else if ("D".equals(timeType))//日
            {
                dateSql = " , format(A.F_START_TIME, 'yyyy-MM-dd') ";
                dateFiled = " BEGINTIME ";

                cDateSql = " , format(CallLog_Form.CREATETIME, 'yyyy-MM-dd') ";
                cDateFiled = "CREATETIME ";


            } else if ("H".equals(timeType))//時
            {
                dateSql = " , format(A.F_START_TIME, 'yyyy-MM-dd HH') ";
                dateFiled = " BEGINTIME ";

                cDateSql = " , format(CallLog_Form.CREATETIME, 'yyyy-MM-dd HH') ";
                cDateFiled = "CREATETIME ";
            }
        }
        String sql = "";

        sql = "SELECT U.USERID , U.USERNAME "
                + ",sum(outbound_talktime)  outbound_talktime  "
                + ",sum(inbound_talktime)     inbound_talktime    "
                + ",sum(hold_time)   hold_time         "
                + ",sum(acw_duration)   acw_duration       "
                + ",sum(ready_time)     ready_time      "
                + ",sum(not_ready_time)  not_ready_time     "
                + ",sum(login_time)   login_time    "
                + ",sum(workTime)    workTime     "
                + ",sum(cast(workPercent as decimal(38,2)))     workPercent     "
                + ",sum(cast(userPercent as decimal(38,2)))  userPercent"
                + ",sum(try_cast(aht as decimal(38,2)))     aht     "
                + ",sum(cast(acw_avg as int))   acw_avg       "
                + ",format(min(firstLoginTime),'yyyy-MM-dd HH:mm:ss')      firstLoginTime    "
                + ",format(max(lastLogoutTime),'yyyy-MM-dd HH:mm:ss')      lastLogoutTime    "
                + ",sum(inbound_calls)  inbound_calls  "
                + ",sum(rest1) rest1       "
                + ",sum(aux0) aux0        "
                + ",sum(aux1) aux1        "
                + ",sum(aux2) aux2"
                + ",sum(aux3)  aux3"
                + ",sum(misc1)  misc1    "
                + ",sum(  case when  cast(workPercent as decimal(38,2)) > 0 then 1 else 0 end) workCount    "
                + ",sum(  case when  cast(userPercent as decimal(38,2)) > 0 then 1 else 0 end) userCount    "

                + dateSql + dateFiled
                + " from CallLog2 A left join  users U on  A.f_agent_id = U.ACCOUNT "

                + "WHERE (A.F_START_TIME >= ?) "
                + "AND A.F_START_TIME < dateadd(day,1,cast(? as date)) "
                + appendSQL

                + "GROUP BY U.USERID, U.USERNAME  " + dateSql
                + " ORDER BY " + dateFiled + (!dateFiled.equals("") ? " , " : "") + " U.USERID ";

        calllogsql =
                " SELECT           " +
                        " 	COUNT(*) AMOUMT," +
                        " 	ISNULL(U.USERID,'') CREATORID      " +
                        cDateSql + cDateFiled +
                        " FROM   CallLog_Form  with(nolock)                                                         " +
                        " 	left join Users u on CallLog_Form.creatorId = u.USERID or CallLog_Form.creatorId = u.ACCOUNT       " +
                        " WHERE                                                             " +
                        " 	CallLog_Form.CREATETIME >=?             " +
                        " AND                                                               " +
                        "	CallLog_Form.CREATETIME < dateadd(day,1,cast(? as date))  " +
                        " ANd " +
                        "   trim(caseType) <>'V'  " +
                        " And callType in ('I','J') or (M7480CALLID = NULL AND SERVICETYPE=NULL AND SERVICEITEM=NULL AND PROCESSTYPE=NULL AND CALLTYPE=NULL AND CASETYPE=NULL)" +
                        appendSQL +
                        " GROUP BY  U.USERID " + cDateSql +
                        " 	ORDER BY " + cDateFiled + (!cDateFiled.equals("") ? " , " : "") + " U.USERID ";

        if (!"W".equals(timeType)) {
            try {

                conn = ds.getConnection();

                int i = 0;
                pstmt = conn.prepareStatement(sql);
                logger.info("話務績效統計表 sql=" + sql);
                pstmt.setString(++i, prao.getStartDate() + " 00:00:00");
                pstmt.setString(++i, prao.getEndDate() + " 00:00:00");
                if (!"A".equals(timeType))
                    pstmt.setString(++i, userID);
                rs = pstmt.executeQuery();
                int nbrDcpInboundCallsAnswered = 0;
                int totalLoginReadyIdleDuration = 0;
                int totalLoginNotReadyIdleDuration = 0;
                int totalLoginPcpIdleDuration = 0;
                int totalDcpInboundCallsDuration = 0;
                int totalLoginDuration = 0;
                int totalPrivateOutBoundDuration = 0;
                int totalHoldDuration = 0;
                int validWorkTime = 0;
                int totalAHT = 0;
                int totalACW = 0;
                int totalInboundCalls = 0;
                double totalWorkPercent = 0.0;
                double totalUsePercent = 0.0;
                int totalWorkCount = 0;
                int totalUserCount = 0;
                int row = 0;
                Integer sortNum = 0;
                String dataYearTmp = "";
                String dataMonTmp = "";
                String dataDayTmp = "";
                String dataHHTmp = "";
                String[] dataArr;
                String[] dateHHArr;
                String tmpDataData = "";
                String usrName = "";
                int totalDuration_0 = 0;
                int totalDuration_1 = 0;
                int totalDuration_2 = 0;
                int totalDuration_3 = 0;
                int totalDuration_4 = 0;
                int totalDuration_5 = 0;
                int totalDuration_6 = 0;
                int totalDuration_7 = 0;
                int totalDuration_8 = 0;
                int totalDuration_9 = 0;
                int totalDuration_10 = 0;

                while (rs.next()) {
                    AgentEfficientDataObject rsDataObj = new AgentEfficientDataObject();

                    if (!"A".equals(timeType)) {
                        rsDataObj.setDataDate(rs.getString("BEGINTIME"));
                        rsDataObj.setUserId(rs.getString("USERID"));
                        valueTable.put(rs.getString("BEGINTIME") + "-" + rs.getString("USERID"), rsDataObj);

                        if ("Y".equals(timeType))//年
                        {
                            if (rs.getString("BEGINTIME") != null && !"".equals(rs.getString("BEGINTIME"))) {
                                sortNum = (Integer) (Integer.parseInt(rs.getString("BEGINTIME")));
                            }
                            rsDataObj.setSortNum(sortNum);
                            rsDataObj.setBeginTime(rs.getString("BEGINTIME"));
                            rsDataObj.setDataDate(rs.getString("BEGINTIME") + "年");
                        } else if ("M".equals(timeType))//月
                        {
                            dataArr = rs.getString("BEGINTIME").split("-");
                            if (dataArr.length > 0) {
                                dataYearTmp = dataArr[0];
                                dataMonTmp = dataArr[1];
                            }

                            sortNum = (Integer) ((Integer.parseInt(dataYearTmp) * 12) + Integer.parseInt(dataMonTmp));
                            rsDataObj.setSortNum(sortNum);

                            rsDataObj.setBeginTime(rs.getString("BEGINTIME"));
                            rsDataObj.setDataDate(dataYearTmp + "年" + dataMonTmp + "月");
                        } else if ("D".equals(timeType))//日
                        {
                            dataArr = rs.getString("BEGINTIME").split("-");
                            if (dataArr.length > 0) {
                                dataYearTmp = dataArr[0];
                                dataMonTmp = dataArr[1];
                                dataDayTmp = dataArr[2];
                            }
                            sortNum = (Integer) ((Integer.parseInt(dataYearTmp) * 365) + monArray[Integer.parseInt(dataMonTmp) - 1] + Integer.parseInt(dataDayTmp));

                            rsDataObj.setSortNum(sortNum);
                            rsDataObj.setBeginTime(rs.getString("BEGINTIME"));
                            rsDataObj.setDataDate(dataYearTmp + "年" + dataMonTmp + "月" + dataDayTmp + "日");
                            rsDataObj.setFirstLoginTime(rs.getString("firstLoginTime"));
                            rsDataObj.setLastLogoutTime(rs.getString("lastLogoutTime"));
                        } else if ("H".equals(timeType))//時
                        {
                            dataArr = rs.getString("BEGINTIME").split("-");
                            if (dataArr.length > 0) {
                                dataYearTmp = dataArr[0];
                                dataMonTmp = dataArr[1];
                                dataDayTmp = dataArr[2];
                                dateHHArr = dataDayTmp.split(" ");
                                dataDayTmp = dateHHArr[0];
                                dataHHTmp = dateHHArr[1];
                            }

                            sortNum = (Integer) ((((Integer.parseInt(dataYearTmp) * 365) + monArray[Integer.parseInt(dataMonTmp) - 1] + Integer.parseInt(dataDayTmp)) * 24) + Integer.parseInt(dataHHTmp));

                            rsDataObj.setSortNum(sortNum);
                            rsDataObj.setBeginTime(rs.getString("BEGINTIME"));
                            //tmpDataData=rs.getString("BEGINTIME")+" 時";
                            tmpDataData = dataYearTmp + "年" + dataMonTmp + "月" + dataDayTmp + "日 " + dataHHTmp + "時";
                            rsDataObj.setDataDate(tmpDataData);
                        }
                    }

                    if (!"".equals(rs.getString("USERID")) && rs.getString("USERID") != null) {
                        usrName = UserInstance.getInstance().getUserObject(rs.getString("USERID")).getUserName();
                    } else {
                        usrName = "";
                    }
                    if (!usrName.equals("遠距測試") && !usrName.equals("客服測試")) {
                        rsDataObj.setAgentName(usrName);
                        int thisTotalLoginNotReadyIdleDuration = rs.getInt("not_ready_time"); //未就緒時間

                        int thisTotalLoginReadyIdleDuration = rs.getInt("ready_time"); //總空閒時間
                        int thisTotalLoginPcpIdleDuration = rs.getInt("acw_duration"); //總文書時間
                        int thisTotalDcpInboundCallsDuration = rs.getInt("inbound_talktime"); //總通話時間
                        int thisTotalLoginDuration = rs.getInt("login_time"); //總登入時間
                        int thisTotalPrivateOutBoundDuration = rs.getInt("outbound_talktime"); //總外撥時間
                        int thisTotalHoldDuration = rs.getInt("hold_time"); //總保留時間
//                        int thisAHT = rs.getInt("aht"); //平均處理時間(AHT)
//                        int thisACW = rs.getInt("acw_avg"); //平均文書處理時間(ACW)
                        int thisInboundCalls = rs.getInt("inbound_calls"); //進縣樹

                        int thisValidWorkTime = rs.getInt("workTime"); //有效值機時間

                        int workCount = rs.getInt("workCount");
                        int userCount = rs.getInt("userCount");

//                        double thisWorkPercent = rs.getDouble("workPercent") / (workCount == 0 ? 1 : workCount); //值機率
//                        String tmpStrthisWorkPercent = thisWorkPercent == 0.0 ? "0.00" : dformat.format(thisWorkPercent);

//                        double thisUsePercent = rs.getDouble("userPercent") / (userCount == 0 ? 1 : userCount); //利用率
//                        String tmpStrthisUsePercent = thisUsePercent == 0.0 ? "0.00" : dformat.format(thisUsePercent);

                        int thisTotalDuration_0 = rs.getInt("misc1"); //其它
                        int thisTotalDuration_1 = rs.getInt("rest1"); //用餐時間
                        int thisTotalDuration_2 = rs.getInt("aux3"); //教育訓練
                        int thisTotalDuration_3 = rs.getInt("aux1"); //開會
                        int thisTotalDuration_4 = rs.getInt("aux0"); //文書

                        int thisTotalDuration_10 = rs.getInt("aux2"); //line

                        double workPercentDenominator = thisTotalDcpInboundCallsDuration + thisTotalPrivateOutBoundDuration +thisTotalHoldDuration +thisTotalLoginPcpIdleDuration + thisTotalLoginReadyIdleDuration;
                        double thisWorkPercent = ((double) (  (thisTotalDcpInboundCallsDuration + thisTotalHoldDuration +thisTotalLoginReadyIdleDuration) / (workPercentDenominator == 0 ? 1 : workPercentDenominator) )*100); //值機率
                        String tmpStrthisWorkPercent = thisWorkPercent == 0.0 ? "0.00" : dformat.format(thisWorkPercent);



                        double userPercentDenominator =  thisTotalLoginDuration == 0 ? 1: thisTotalLoginDuration;
                        double thisUsePercent =  ((double) (  (thisTotalDcpInboundCallsDuration + thisTotalHoldDuration + thisTotalLoginPcpIdleDuration) / userPercentDenominator) * 100)  ; //利用率
                        String tmpStrthisUsePercent = thisUsePercent == 0.0 ? "0.00" : dformat.format(thisUsePercent);


                        int thisAHT = thisTotalDcpInboundCallsDuration / (thisInboundCalls ==0 ? 1 : thisInboundCalls) ; //平均處理時間(AHT)
                        int thisACW = thisTotalLoginPcpIdleDuration  / (thisInboundCalls ==0 ? 1 : thisInboundCalls) ; //平均文書處理時間(ACW)

                        totalLoginReadyIdleDuration += thisTotalLoginReadyIdleDuration;
                        totalLoginNotReadyIdleDuration += thisTotalLoginNotReadyIdleDuration;
                        totalLoginPcpIdleDuration += thisTotalLoginPcpIdleDuration;
                        totalDcpInboundCallsDuration += thisTotalDcpInboundCallsDuration;
                        totalLoginDuration += thisTotalLoginDuration;
                        totalPrivateOutBoundDuration += thisTotalPrivateOutBoundDuration;
                        totalHoldDuration += thisTotalHoldDuration;
                        validWorkTime += thisValidWorkTime;
                        totalAHT += thisAHT;
                        totalACW += thisACW;
                        totalInboundCalls += thisInboundCalls;
                        totalWorkPercent += thisWorkPercent;
                        totalUsePercent += thisUsePercent;

                        // int type

                        rsDataObj.setIntTotalLoginReadyIdleDuration(thisTotalLoginReadyIdleDuration);
                        rsDataObj.setIntTotalLoginNotReadyIdleDuration(thisTotalLoginNotReadyIdleDuration);
                        rsDataObj.setIntTotalLoginPcpIdleDuration(thisTotalLoginPcpIdleDuration);
                        rsDataObj.setIntTotalDcpInboundCallsDuration(thisTotalDcpInboundCallsDuration);
                        rsDataObj.setIntTotalLoginDuration(thisTotalLoginDuration);
                        rsDataObj.setIntTotalPrivateOutBoundDuration(thisTotalPrivateOutBoundDuration);
                        rsDataObj.setIntTotalHoldDuration(thisTotalHoldDuration);
                        rsDataObj.setIntValidWorkTime(thisValidWorkTime);
                        rsDataObj.setDoubleWorkPercent(thisWorkPercent);
                        rsDataObj.setDoubleUsePercent(thisUsePercent);

                        //把秒數換成 00:00:00 (string type)
                        rsDataObj.setTotalLoginReadyIdleDuration(CalendarUtil.formatDate(thisTotalLoginReadyIdleDuration));
                        rsDataObj.setTotalLoginNotReadyIdleDuration(CalendarUtil.formatDate(thisTotalLoginNotReadyIdleDuration));
                        rsDataObj.setTotalLoginPcpIdleDuration(CalendarUtil.formatDate(thisTotalLoginPcpIdleDuration));
                        rsDataObj.setTotalDcpInboundCallsDuration(CalendarUtil.formatDate(thisTotalDcpInboundCallsDuration));
                        rsDataObj.setTotalLoginDuration(CalendarUtil.formatDate(thisTotalLoginDuration));
                        rsDataObj.setTotalPrivateOutBoundDuration(CalendarUtil.formatDate(thisTotalPrivateOutBoundDuration));
                        rsDataObj.setTotalHoldDuration(CalendarUtil.formatDate(thisTotalHoldDuration));
                        rsDataObj.setValidWorkTime(CalendarUtil.formatDate(thisValidWorkTime));

                        rsDataObj.setAHT(CalendarUtil.formatDate(thisAHT));
                        rsDataObj.setACW(CalendarUtil.formatDate(thisACW));

                        tmpStrthisWorkPercent = tmpStrthisWorkPercent + "%";
                        tmpStrthisUsePercent = tmpStrthisUsePercent + "%";
                        rsDataObj.setWorkPercent(tmpStrthisWorkPercent);
                        rsDataObj.setUsePercent(tmpStrthisUsePercent);

                        rsDataObj.setCallLogAmount(String.valueOf(thisInboundCalls));



                        totalDuration_0 += thisTotalDuration_0;
                        totalDuration_1 += thisTotalDuration_1;
                        totalDuration_2 += thisTotalDuration_2;
                        totalDuration_3 += thisTotalDuration_3;
                        totalDuration_4 += thisTotalDuration_4;

                        totalDuration_10 += thisTotalDuration_10;

                        // int type
                        rsDataObj.setIntTotalNotReadyDuration_0(thisTotalDuration_0);
                        rsDataObj.setIntTotalNotReadyDuration_1(thisTotalDuration_1);
                        rsDataObj.setIntTotalNotReadyDuration_2(thisTotalDuration_2);
                        rsDataObj.setIntTotalNotReadyDuration_3(thisTotalDuration_3);
                        rsDataObj.setIntTotalNotReadyDuration_4(thisTotalDuration_4);

                        rsDataObj.setIntTotalNotReadyDuration_10(thisTotalDuration_10);

                        //把秒數換成 00:00:00 (string type)
                        rsDataObj.setTotalNotReadyDuration_0(CalendarUtil.formatDate(thisTotalDuration_0));
                        rsDataObj.setTotalNotReadyDuration_1(CalendarUtil.formatDate(thisTotalDuration_1));
                        rsDataObj.setTotalNotReadyDuration_2(CalendarUtil.formatDate(thisTotalDuration_2));
                        rsDataObj.setTotalNotReadyDuration_3(CalendarUtil.formatDate(thisTotalDuration_3));
                        rsDataObj.setTotalNotReadyDuration_4(CalendarUtil.formatDate(thisTotalDuration_4));
                        rsDataObj.setTotalNotReadyDuration_10(CalendarUtil.formatDate(thisTotalDuration_10));

                        al.add(rsDataObj);
                        if ("A".equals(timeType)) {
                            prao.addDataList(rsDataObj);
                            if (rs.getString("USERID") != null) {
                                valueTable.put(rs.getString("USERID"), rsDataObj);
                            }
                        }

                        row++;
                    }
                }//end while

                ArrayList resultAl = new ArrayList();

                if (!"A".equals(timeType)) {
                    resultAl = this.getAllTime(bDataTime_YYYY, bDataTime_MM, bDataTime_DD, eDataTime_YYYY, eDataTime_MM, eDataTime_DD, timeType, al);
                    Collections.sort(resultAl);
                    //resultAl = al;

                    for (int j = 0; j < resultAl.size(); j++) {
                        AgentEfficientDataObject obj = (AgentEfficientDataObject) resultAl.get(j);
                        usrName = UserInstance.getInstance().getUserObject(obj.getUserId()).getUserName();
                        obj.setAgentName(usrName);
                        prao.addDataList(obj);
                    }
                }

                prao.setDataListnum(row);//設定總筆數
                int totalTime = totalLoginReadyIdleDuration + totalLoginNotReadyIdleDuration + totalLoginPcpIdleDuration + totalDcpInboundCallsDuration;
                prao.setIntAllTnbrDcpInboundCallsAnswered(nbrDcpInboundCallsAnswered);
                prao.setIntAllTotalDcpInboundCallsDuration(totalDcpInboundCallsDuration);
                prao.setIntAllTotalLoginDuration(totalLoginDuration);
                prao.setIntAllTotalLoginNotReadyIdleDuration(totalLoginNotReadyIdleDuration);
                prao.setIntAllTotalLoginPcpIdleDuration(totalLoginPcpIdleDuration);
                prao.setIntALLTotalLoginReadyIdleDuration(totalLoginReadyIdleDuration);
                prao.setIntAlltotalPrivateOutBoundDuration(totalPrivateOutBoundDuration);
                prao.setIntAllTotalHoldDuration(totalHoldDuration);
                prao.setIntAllValidWorkTime(validWorkTime);

//                double workPercentAmount = totalWorkPercent;
//                String tmpStrworkPercentAmount = totalWorkPercent == 0.0 ? "0.00" : dformat.format(workPercentAmount);
//                prao.setDoubleAllWorkPercent(workPercentAmount);


                double workDenominator = totalDcpInboundCallsDuration + totalPrivateOutBoundDuration +totalHoldDuration +totalLoginPcpIdleDuration + totalLoginReadyIdleDuration;
                double workPercentAmount = ((double) (  (totalDcpInboundCallsDuration + totalHoldDuration +totalLoginReadyIdleDuration) / (workDenominator == 0 ? 1 : workDenominator) )*100); //值機率
                String tmpStrworkPercentAmount = totalWorkPercent == 0.0 ? "0.00" : dformat.format(workPercentAmount);
                prao.setDoubleAllWorkPercent(workPercentAmount);

//                double usePercentAmount = totalUsePercent;
//                String tmpStrUsePercentAmount = usePercentAmount == 0.0 ? "0.00" : dformat.format(usePercentAmount);
//                prao.setDoubleAllUsePercent(usePercentAmount);

                int userDenominator = (totalLoginDuration == 0 ? 1 : totalLoginDuration);
                double usePercentAmount =   ((double)  (totalDcpInboundCallsDuration + totalHoldDuration + totalLoginPcpIdleDuration) / userDenominator)*100;
                String tmpStrUsePercentAmount = usePercentAmount == 0.0 ? "0.00" : dformat.format(usePercentAmount);
                prao.setDoubleAllUsePercent(usePercentAmount);

                //將秒數變成 00:00:00
                prao.setStrAllTotalDcpInboundCallsDuration(CalendarUtil.formatDate(totalDcpInboundCallsDuration));
                prao.setStrAllTotalLoginDuration(CalendarUtil.formatDate(totalLoginDuration));
                prao.setStrAllTotalLoginNotReadyIdleDuration(CalendarUtil.formatDate(totalLoginNotReadyIdleDuration));
                prao.setStrAllTotalLoginPcpIdleDuration(CalendarUtil.formatDate(totalLoginPcpIdleDuration));
                prao.setStrALLTotalLoginReadyIdleDuration(CalendarUtil.formatDate(totalLoginReadyIdleDuration));
                prao.setStrAllTotalPrivateOutBoundDuration(CalendarUtil.formatDate(totalPrivateOutBoundDuration));
                prao.setStrAllTotalHoldDuration(CalendarUtil.formatDate(totalHoldDuration));
                prao.setStrALLValidWorkTime(CalendarUtil.formatDate(validWorkTime));
                tmpStrworkPercentAmount = tmpStrworkPercentAmount + "%";
                tmpStrUsePercentAmount = tmpStrUsePercentAmount + "%";
                prao.setStrALLWorkPercent(tmpStrworkPercentAmount);
                prao.setStrALLUsePercent(tmpStrUsePercentAmount);
                prao.setStrALLAHT(CalendarUtil.formatDate(totalDcpInboundCallsDuration / (totalInboundCalls ==0 ? 1:totalInboundCalls)));
                prao.setStrALLACW(CalendarUtil.formatDate(totalLoginPcpIdleDuration / (totalInboundCalls==0 ?  1:totalInboundCalls)));
                prao.setStrALLCallLogAmount("" + totalInboundCalls);

                if (totalTime > 0) {
                    prao.setIntAllTotalDcpInboundCallsDurationPercent((double) totalDcpInboundCallsDuration / totalTime);
                    prao.setIntAllTotalLoginNotReadyIdleDurationPercent((double) totalLoginNotReadyIdleDuration / totalTime);
                    prao.setIntAllTotalLoginPcpIdleDurationPercent((double) totalLoginPcpIdleDuration / totalTime);
                    prao.setIntALLTotalLoginReadyIdleDurationPercent((double) totalLoginReadyIdleDuration / totalTime);

                }

                totalTime = totalDuration_0 + totalDuration_1 + totalDuration_2 + totalDuration_3 + totalDuration_4;
                //+ totalDuration_5 + totalDuration_6 + totalDuration_7 + totalDuration_8 + totalDuration_9 + totalDuration_10;

                //將秒數變成 00:00:00
                prao.setAllTotalNotReadyDuration_0(CalendarUtil.formatDate(totalDuration_0));
                prao.setAllTotalNotReadyDuration_1(CalendarUtil.formatDate(totalDuration_1));
                prao.setAllTotalNotReadyDuration_2(CalendarUtil.formatDate(totalDuration_2));
                prao.setAllTotalNotReadyDuration_3(CalendarUtil.formatDate(totalDuration_3));
                prao.setAllTotalNotReadyDuration_4(CalendarUtil.formatDate(totalDuration_4));
                prao.setAllTotalNotReadyDuration_5(CalendarUtil.formatDate(totalDuration_5));
                prao.setAllTotalNotReadyDuration_6(CalendarUtil.formatDate(totalDuration_6));
                prao.setAllTotalNotReadyDuration_7(CalendarUtil.formatDate(totalDuration_7));
                prao.setAllTotalNotReadyDuration_8(CalendarUtil.formatDate(totalDuration_8));
                prao.setAllTotalNotReadyDuration_9(CalendarUtil.formatDate(totalDuration_9));
                prao.setAllTotalNotReadyDuration_10(CalendarUtil.formatDate(totalDuration_10));

                if (totalTime > 0) {
                    prao.setIntAllTotalNotReadyDurationPercent_0((double) totalDuration_0 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_1((double) totalDuration_1 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_2((double) totalDuration_2 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_3((double) totalDuration_3 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_4((double) totalDuration_4 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_5((double) totalDuration_5 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_6((double) totalDuration_6 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_7((double) totalDuration_7 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_8((double) totalDuration_8 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_9((double) totalDuration_9 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_10((double) totalDuration_10 / totalTime);
                }

                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;

                i = 0;
                pstmt = conn.prepareStatement(calllogsql);
                pstmt.setString(++i, prao.getStartDate() + " 00:00:00");
                pstmt.setString(++i, prao.getEndDate() + " 00:00:00");
                logger.info("calllogsql  =" + calllogsql);
                if (!"A".equals(timeType))
                    pstmt.setString(++i, userID);
                rs = pstmt.executeQuery();
                int intAllCallLogAmount = 0;
                int intAllNotValidPhone = 0;
//
//                for(Map.Entry<String, AgentEfficientDataObject> e : valueTable.entrySet()) {
//                    String key = e.getKey();
//                    AgentEfficientDataObject value = e.getValue();
//                    logger.info(key);
//                }

                while (rs.next()) {
                    AgentEfficientDataObject tmpObj = null;
                    if ("A".equals(timeType)) {
                        tmpObj = valueTable.get(rs.getString("CREATORID"));
                    } else {
                        tmpObj = valueTable.get(rs.getString("CREATETIME") + "-" + rs.getString("CREATORID"));
                    }
                    //logger.info("key=" + rs.getString("CREATETIME") + "-" + rs.getString("CREATORID"));
                    if (tmpObj != null) {
                        tmpObj.setIntNotValidPhone(rs.getInt("AMOUMT"));
                        tmpObj.setNotValidPhone(rs.getString("AMOUMT"));
                        intAllNotValidPhone = intAllNotValidPhone + rs.getInt("AMOUMT");
                    }
                }

                prao.setIntAllNotValidPhone(intAllNotValidPhone);

                prao.setStrALLNotValidPhone("" + intAllNotValidPhone);

                conn.close();
                conn = null;

                beans.put("reportData", prao);


            } catch (Exception e) {
                logger.error(e);
                throw e;
            } finally {

                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    if (pstmt != null) {
                        pstmt.close();
                        pstmt = null;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                try {
                    if (conn != null) {
                        conn.close();
                        conn = null;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else if ("W".equals(timeType)) {
            try {

                int totalDuration_0 = 0;
                int totalDuration_1 = 0;
                int totalDuration_2 = 0;
                int totalDuration_3 = 0;
                int totalDuration_4 = 0;
                int totalDuration_5 = 0;
                int totalDuration_6 = 0;
                int totalDuration_7 = 0;
                int totalDuration_8 = 0;
                int totalDuration_9 = 0;
                int totalDuration_10 = 0;
                conn = ds.getConnection();

                sqlQuery = " SELECT U.USERID, format(A.F_START_TIME, 'yyyy') BEGINTIME, DATEPART(wk,A.F_START_TIME) WEEKFIELD,"
                        + "sum(outbound_talktime)  outbound_talktime  "
                        + ",sum(inbound_talktime)     inbound_talktime    "
                        + ",sum(hold_time)   hold_time         "
                        + ",sum(acw_duration)   acw_duration       "
                        + ",sum(ready_time)     ready_time      "
                        + ",sum(not_ready_time)  not_ready_time     "
                        + ",sum(login_time)   login_time    "
                        + ",sum(workTime)    workTime     "
                        + ",sum(cast(workPercent as decimal(38,2)))     workPercent     "
                        + ",sum(cast(userPercent as decimal(38,2)))  userPercent"
                        + ",sum(try_cast(aht as decimal(38,2)))     aht     "
                        + ",sum(cast(acw_avg as int))   acw_avg       "
                        + ",min(firstLoginTime)      firstLoginTime    "
                        + ",max(lastLogoutTime)      lastLogoutTime     "
                        + ",sum(inbound_calls)  inbound_calls  "
                        + ",sum(rest1) rest1       "
                        + ",sum(aux0) aux0        "
                        + ",sum(aux1) aux1        "
                        + ",sum(aux2) aux2"
                        + ",sum(aux3)  aux3"
                        + ",sum(misc1)  misc1    "
                        + ",sum(  case when  cast(workPercent as decimal(38,2)) > 0 then 1 else 0 end) workCount    "
                        + ",sum(  case when  cast(userPercent as decimal(38,2)) > 0 then 1 else 0 end) userCount    "

                        + " from CallLog2 A left join  users U on  A.f_agent_id = U.ACCOUNT "

                        + "WHERE (A.F_START_TIME >= ?) "
                        + "AND A.F_START_TIME < dateadd(day,1,cast(? as date)) "
                        + appendSQL

                        + "GROUP BY U.USERID, format(A.F_START_TIME, 'yyyy'), DATEPART(wk,A.F_START_TIME)"
                        + "ORDER BY BEGINTIME,USERID,WEEKFIELD ";

                calllogsql =
                        " SELECT           " +
                                " 	COUNT(*) AMOUMT," +
                                " 	ISNULL(U.USERID,'') CREATORID ,     " +
                                " format(CallLog_Form.CREATETIME, 'yyyy') CREATETIME, DATEPART(wk,CallLog_Form.CREATETIME) WEEKFIELD " +
                                " FROM   CallLog_Form                                                           " +
                                " 	left join Users u on CallLog_Form.creatorId = u.USERID or CallLog_Form.creatorId = u.ACCOUNT       " +
                                " WHERE                                                             " +
                                " 	CallLog_Form.CREATETIME >=?             " +
                                " AND                                                               " +
                                "	CallLog_Form.CREATETIME < dateadd(day,1,cast(? as date))  " +
                                " ANd " +
                                "   trim(caseType) <>'V'  " +
                                " And callType in ('I','J') or (M7480CALLID = NULL AND SERVICETYPE=NULL AND SERVICEITEM=NULL AND PROCESSTYPE=NULL AND CALLTYPE=NULL AND CASETYPE=NULL)" +
                                appendSQL +
                                " GROUP BY  U.USERID ,format(CallLog_Form.CREATETIME, 'yyyy') , DATEPART(wk,CallLog_Form.CREATETIME) " +
                                " 	ORDER BY CREATETIME,CREATORID,WEEKFIELD ";

                int i = 0;
                pstmt = conn.prepareStatement(sqlQuery);
                logger.info("話務績效統計表 sql=" + sqlQuery);
                pstmt.setString(++i, prao.getStartDate() + " 00:00:00");
                pstmt.setString(++i, prao.getEndDate() + " 00:00:00");
                if (!"A".equals(timeType))
                    pstmt.setString(++i, userID);
                rs = pstmt.executeQuery();
                String yearStr = "";
                String weekStr = "";
                Calendar tmpStartDate = Calendar.getInstance();
                Calendar tmpEndDate = Calendar.getInstance();

                tmpStartDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1, Integer.parseInt(bDataTime_DD), 0, 0, 0);

                tmpEndDate.set(Integer.parseInt(eDataTime_YYYY), Integer.parseInt(eDataTime_MM) - 1, Integer.parseInt(eDataTime_DD), 0, 0, 0);

                DecimalFormat df = new DecimalFormat("########.00");
                String usrName = "";
                int row = 0;
                while (rs.next()) {
                    AgentEfficientDataObject rsDataObj = new AgentEfficientDataObject();
                    yearStr = rs.getString("BEGINTIME");
                    weekStr = rs.getString("WEEKFIELD");
                    //rsDataObj.setWeekNum(Integer.parseInt(weekStr)+1);
                    rsDataObj.setWeekNum(Integer.parseInt(weekStr));
                    rsDataObj.setBeginTime(rs.getString("BEGINTIME"));
                    rsDataObj.setUserId(rs.getString("USERID"));
                    valueTable.put(yearStr + weekStr + "-" + rs.getString("USERID"), rsDataObj);

                    int thisTotalLoginNotReadyIdleDuration = rs.getInt("not_ready_time"); //未就緒時間

                    int thisTotalLoginReadyIdleDuration = rs.getInt("ready_time"); //總空閒時間
                    int thisTotalLoginPcpIdleDuration = rs.getInt("acw_duration"); //總文書時間
                    int thisTotalDcpInboundCallsDuration = rs.getInt("inbound_talktime"); //總通話時間
                    int thisTotalLoginDuration = rs.getInt("login_time"); //總登入時間
                    int thisTotalPrivateOutBoundDuration = rs.getInt("outbound_talktime"); //總外撥時間
                    int thisTotalHoldDuration = rs.getInt("hold_time"); //總保留時間
//                    int thisAHT = rs.getInt("aht"); //平均處理時間(AHT)
//                    int thisACW = rs.getInt("acw_avg"); //平均文書處理時間(ACW)
                    int thisInboundCalls = rs.getInt("inbound_calls"); //進縣樹

                    int thisValidWorkTime = rs.getInt("workTime"); //有效值機時間

                    int workCount = rs.getInt("workCount");
                    int userCount = rs.getInt("userCount");

                    int thisTotalDuration_0 = rs.getInt("misc1"); //其它
                    int thisTotalDuration_1 = rs.getInt("rest1"); //用餐時間
                    int thisTotalDuration_2 = rs.getInt("aux3"); //教育訓練
                    int thisTotalDuration_3 = rs.getInt("aux1"); //開會
                    int thisTotalDuration_4 = rs.getInt("aux0"); //文書

                    int thisTotalDuration_10 = rs.getInt("aux2"); //line


//                    double thisWorkPercent = rs.getDouble("workPercent") / (workCount == 0 ? 1 : workCount); //值機率
//                    String tmpStrthisWorkPercent = thisWorkPercent == 0.0 ? "0.00" : dformat.format(thisWorkPercent);

//                    double thisUsePercent = rs.getDouble("userPercent") / (userCount == 0 ? 1 : userCount); //利用率
//                    String tmpStrthisUsePercent = thisUsePercent == 0.0 ? "0.00" : dformat.format(thisUsePercent);


                    double workPercentDenominator = thisTotalDcpInboundCallsDuration + thisTotalPrivateOutBoundDuration +thisTotalHoldDuration +thisTotalLoginPcpIdleDuration + thisTotalLoginReadyIdleDuration;
                    double thisWorkPercent = ((double) (  (thisTotalDcpInboundCallsDuration + thisTotalHoldDuration +thisTotalLoginReadyIdleDuration) / (workPercentDenominator == 0 ? 1 : workPercentDenominator) )*100); //值機率
                    String tmpStrthisWorkPercent = thisWorkPercent == 0.0 ? "0.00" : dformat.format(thisWorkPercent);


                    double userPercentDenominator =  thisTotalLoginDuration == 0 ? 1: thisTotalLoginDuration;
                    double thisUsePercent =  ((double) (  (thisTotalDcpInboundCallsDuration + thisTotalHoldDuration + thisTotalLoginPcpIdleDuration) / userPercentDenominator) * 100)  ; //利用率
                    String tmpStrthisUsePercent = thisUsePercent == 0.0 ? "0.00" : dformat.format(thisUsePercent);


                    int thisAHT = thisTotalDcpInboundCallsDuration / (thisInboundCalls ==0 ? 1 : thisInboundCalls) ; //平均處理時間(AHT)
                    int thisACW = thisTotalLoginPcpIdleDuration  / (thisInboundCalls ==0 ? 1 : thisInboundCalls) ; //平均文書處理時間(ACW)


                    rsDataObj.setNbrDcpInboundCallsAnswered("0");

                    rsDataObj.setIntTotalLoginReadyIdleDuration(thisTotalLoginReadyIdleDuration);
                    rsDataObj.setIntTotalLoginNotReadyIdleDuration(thisTotalLoginNotReadyIdleDuration);
                    rsDataObj.setIntTotalLoginPcpIdleDuration(thisTotalLoginPcpIdleDuration);
                    rsDataObj.setIntTotalDcpInboundCallsDuration(thisTotalDcpInboundCallsDuration);
                    rsDataObj.setIntTotalLoginDuration(thisTotalLoginDuration);
                    rsDataObj.setIntTotalPrivateOutBoundDuration(thisTotalPrivateOutBoundDuration);
                    rsDataObj.setIntTotalHoldDuration(thisTotalHoldDuration);
                    rsDataObj.setIntValidWorkTime(thisValidWorkTime);
                    rsDataObj.setDoubleWorkPercent(thisWorkPercent);
                    rsDataObj.setDoubleUsePercent(thisUsePercent);

                    rsDataObj.setTotalLoginReadyIdleDuration(String.valueOf(thisTotalLoginReadyIdleDuration));
                    rsDataObj.setTotalLoginNotReadyIdleDuration(String.valueOf(thisTotalLoginNotReadyIdleDuration));
                    rsDataObj.setTotalLoginPcpIdleDuration(String.valueOf(thisTotalLoginPcpIdleDuration));
                    rsDataObj.setTotalDcpInboundCallsDuration(String.valueOf(thisTotalDcpInboundCallsDuration));
                    rsDataObj.setTotalLoginDuration(String.valueOf(thisTotalLoginDuration));
                    rsDataObj.setTotalPrivateOutBoundDuration(String.valueOf(thisTotalPrivateOutBoundDuration));
                    rsDataObj.setTotalHoldDuration(String.valueOf(thisTotalHoldDuration));
                    rsDataObj.setValidWorkTime(String.valueOf(thisValidWorkTime));
                    rsDataObj.setAHT(String.valueOf(thisAHT));
                    rsDataObj.setACW(String.valueOf(thisACW));
                    rsDataObj.setWorkPercent(tmpStrthisWorkPercent);
                    rsDataObj.setUsePercent(tmpStrthisUsePercent);

                    rsDataObj.setCallLogAmount(String.valueOf(thisInboundCalls));



                    totalDuration_0 += thisTotalDuration_0;
                    totalDuration_1 += thisTotalDuration_1;
                    totalDuration_2 += thisTotalDuration_2;
                    totalDuration_3 += thisTotalDuration_3;
                    totalDuration_4 += thisTotalDuration_4;

                    totalDuration_10 += thisTotalDuration_10;

                    // int type
                    rsDataObj.setIntTotalNotReadyDuration_0(thisTotalDuration_0);
                    rsDataObj.setIntTotalNotReadyDuration_1(thisTotalDuration_1);
                    rsDataObj.setIntTotalNotReadyDuration_2(thisTotalDuration_2);
                    rsDataObj.setIntTotalNotReadyDuration_3(thisTotalDuration_3);
                    rsDataObj.setIntTotalNotReadyDuration_4(thisTotalDuration_4);
                    rsDataObj.setIntTotalNotReadyDuration_10(thisTotalDuration_10);

                    //把秒數換成 00:00:00 (string type)
                    rsDataObj.setTotalNotReadyDuration_0(CalendarUtil.formatDate(thisTotalDuration_0));
                    rsDataObj.setTotalNotReadyDuration_1(CalendarUtil.formatDate(thisTotalDuration_1));
                    rsDataObj.setTotalNotReadyDuration_2(CalendarUtil.formatDate(thisTotalDuration_2));
                    rsDataObj.setTotalNotReadyDuration_3(CalendarUtil.formatDate(thisTotalDuration_3));
                    rsDataObj.setTotalNotReadyDuration_4(CalendarUtil.formatDate(thisTotalDuration_4));
                    rsDataObj.setTotalNotReadyDuration_10(CalendarUtil.formatDate(thisTotalDuration_10));

                    rsDataObj.setWeekDay();
                    rsDataObj.setBeetWeekDays();
                    al.add(rsDataObj);
                    row++;
                }//end while

                ArrayList resultAl = new ArrayList();
                resultAl = this.getAllTime(bDataTime_YYYY, bDataTime_MM, bDataTime_DD, eDataTime_YYYY, eDataTime_MM, eDataTime_DD, "W", al);

                Collections.sort(resultAl);

                Calendar tmpWeekFirstDay = Calendar.getInstance();
                Calendar tmpWeekLastDay = Calendar.getInstance();
                int startFlag = 0;
                int endFlag = 0;
                String dateDate = "";
                AgentEfficientDataObject tmpAobj = new AgentEfficientDataObject();
                AgentEfficientDataObject tmpBobj = new AgentEfficientDataObject();
                AgentEfficientDataObject tmpCobj = new AgentEfficientDataObject();

                for (int p = 0; p < resultAl.size(); p++) {

                    tmpAobj = (AgentEfficientDataObject) resultAl.get(p);

                    tmpWeekFirstDay.set(tmpAobj.getStartYear(), tmpAobj.getStartMon() - 1, tmpAobj.getStartDay(), 0, 0, 0);
                    tmpWeekLastDay.set(tmpAobj.getEndYear(), tmpAobj.getEndMon() - 1, tmpAobj.getEndDay(), 0, 0, 0);

                    //這是判斷查詢開始日期和一週第一天誰比較晚，例如 2009 12/28~2009 1/3 這是第一週，但如果查詢開始日期是1/1日，顯示的日期就要換成 2009 1/1 ~2009 1/3
                    // 下面-100 是因為tmpStartDate和tmpWeekFirstDay如果是表示同一個日期時，有時候getTimeInMillis會不一樣，會有些許差額，所以減100
                    if (tmpStartDate.getTimeInMillis() >= tmpWeekFirstDay.getTimeInMillis() - 100) {

                        String strmon = "";
                        String strStartDay = "";
                        String strEndDay = "";
                        int changeWeekNum = 0;

                        if ((tmpStartDate.get(Calendar.MONTH) + 1) == (tmpWeekLastDay.get(Calendar.MONTH) + 1)) {
                            //這是要把例如 2009 12/28~2009 1/3 這是12月第五週，但如果查詢開始日期是1/1日，則被換成 2009 1/1~2009 1/3 這樣週數還是第五週，因此要換成第一週
                            //變成2009 1/1~2009 1/3 一月第一週   但如果換完查詢開始日期 變成是 2009 12/30~2009 1/3 那依然要留著第五週  所以<20表示這樣的日期不會跨年，就可
                            //換成第一週了。
                            if (tmpAobj.getWeekOfMon() > 4 && tmpWeekLastDay.get(Calendar.DATE) < 20) {
                                changeWeekNum = 1;
                            } else {
                                changeWeekNum = tmpAobj.getWeekOfMon();
                            }

                        } else {
                            changeWeekNum = tmpAobj.getWeekOfMon();
                        }

                        if ((tmpStartDate.get(Calendar.MONTH) + 1) < 10) {
                            strmon = "0" + (tmpStartDate.get(Calendar.MONTH) + 1);
                        } else {
                            strmon = "" + (tmpStartDate.get(Calendar.MONTH) + 1);
                        }

                        if (tmpStartDate.get(Calendar.DATE) < 10) {
                            strStartDay = "0" + tmpStartDate.get(Calendar.DATE);
                        } else {
                            strStartDay = "" + tmpStartDate.get(Calendar.DATE);
                        }

                        if (tmpAobj.getEndDay() < 10) {
                            strEndDay = "0" + tmpAobj.getEndDay();
                        } else {
                            strEndDay = "" + tmpAobj.getEndDay();
                        }

                        // dateDate=tmpStartDate.get(Calendar.YEAR)+"年"+strmon+"月 第"+tmpAobj.getWeekOfMon()+"週 "+strStartDay+"~"+strEndDay+"日";
                        dateDate = tmpStartDate.get(Calendar.YEAR) + "年" + strmon + "月 第" + changeWeekNum + "週 " + strStartDay + "~" + strEndDay + "日";

                        tmpAobj.setDataDate(dateDate);
                        startFlag = 1;

                    }

                    if (tmpEndDate.getTimeInMillis() <= tmpWeekLastDay.getTimeInMillis()) {

                        String strmon = "";
                        String strStartDay = "";
                        String strEndDay = "";
                        if (tmpAobj.getStartMon() < 10) {
                            strmon = "0" + tmpAobj.getStartMon();
                        } else {
                            strmon = "" + tmpAobj.getStartMon();
                        }

                        if (tmpAobj.getStartDay() < 10) {
                            strStartDay = "0" + tmpAobj.getStartDay();
                        } else {
                            strStartDay = "" + tmpAobj.getStartDay();
                        }

                        if (tmpEndDate.get(Calendar.DATE) < 10) {
                            strEndDay = "0" + tmpEndDate.get(Calendar.DATE);
                        } else {
                            strEndDay = "" + tmpEndDate.get(Calendar.DATE);
                        }

                        dateDate = tmpAobj.getStartYear() + "年" + strmon + "月 第" + tmpAobj.getWeekOfMon() + "週 " + strStartDay + "~" + strEndDay + "日";

                        endFlag = 1;
                        tmpAobj.setDataDate(dateDate);

                    }

                    if ((tmpStartDate.getTimeInMillis() > tmpWeekFirstDay.getTimeInMillis()) && (tmpEndDate.getTimeInMillis() <= tmpWeekLastDay.getTimeInMillis())) {

                        String strmon = "";
                        String strStartDay = "";
                        String strEndDay = "";
                        if ((tmpStartDate.get(Calendar.MONTH) + 1) < 10) {
                            strmon = "0" + (tmpStartDate.get(Calendar.MONTH) + 1);
                        } else {
                            strmon = "" + (tmpStartDate.get(Calendar.MONTH) + 1);
                        }

                        if (tmpStartDate.get(Calendar.DATE) < 10) {
                            strStartDay = "0" + tmpStartDate.get(Calendar.DATE);
                        } else {
                            strStartDay = "" + tmpStartDate.get(Calendar.DATE);
                        }

                        if (tmpEndDate.get(Calendar.DATE) < 10) {
                            strEndDay = "0" + tmpEndDate.get(Calendar.DATE);
                        } else {
                            strEndDay = "" + tmpEndDate.get(Calendar.DATE);
                        }

                        dateDate = tmpAobj.getStartYear() + "年" + strmon + "月 第" + tmpAobj.getWeekOfMon() + "週 " + strStartDay + "~" + strEndDay + "日";
                        startFlag = 1;
                        endFlag = 1;
                        tmpAobj.setDataDate(dateDate);
                    }

                }

                if (startFlag == 0) {
                    if (resultAl.size() > 0) {
                        tmpAobj = (AgentEfficientDataObject) resultAl.get(0);

                        tmpBobj.setWeekNum(tmpStartDate.get(Calendar.WEEK_OF_YEAR));
                        tmpBobj.setAgentName("");
                        tmpBobj.setBeginTime("" + tmpStartDate.get(Calendar.YEAR));

                        tmpBobj.setDBflag(1);
                        tmpBobj.setTotalLoginReadyIdleDuration("0");
                        tmpBobj.setTotalLoginNotReadyIdleDuration("0");
                        tmpBobj.setTotalLoginPcpIdleDuration("0");
                        tmpBobj.setTotalDcpInboundCallsDuration("0");
                        tmpBobj.setTotalLoginDuration("0");
                        tmpBobj.setNbrDcpInboundCallsAnswered("0");
                        tmpBobj.setTotalPrivateOutBoundDuration("0");
                        tmpBobj.setTotalHoldDuration("0");
                        tmpBobj.setValidWorkTime("0");
                        tmpBobj.setWorkPercent("0.00%");
                        tmpBobj.setUsePercent("0.00%");
                        tmpBobj.setWeekDay();
                        tmpBobj.setBeetWeekDays();

                        tmpBobj.setIntTnbrDcpInboundCallsAnswered(0);
                        tmpBobj.setIntTotalLoginReadyIdleDuration(0);
                        tmpBobj.setIntTotalLoginNotReadyIdleDuration(0);
                        tmpBobj.setIntTotalLoginPcpIdleDuration(0);
                        tmpBobj.setIntTotalDcpInboundCallsDuration(0);
                        tmpBobj.setIntTotalLoginDuration(0);
                        tmpBobj.setIntTotalPrivateOutBoundDuration(0);
                        tmpBobj.setIntTotalHoldDuration(0);
                        tmpBobj.setIntValidWorkTime(0);
                        tmpBobj.setDoubleWorkPercent(0);
                        tmpBobj.setDoubleUsePercent(0);

                        Calendar tmpDay = Calendar.getInstance();
                        Calendar tmpDay2 = Calendar.getInstance();
                        tmpDay = tmpStartDate;
                        tmpDay2.set(tmpAobj.getStartYear(), tmpAobj.getStartMon() - 1, tmpAobj.getStartDay(), 0, 0, 0);

                        tmpDay2.add(Calendar.DAY_OF_WEEK, -1);

                        String strmon = "";
                        String strStartDay = "";
                        String strEndDay = "";
                        int startYear = tmpDay.get(Calendar.YEAR);
                        int startMon = (tmpDay.get(Calendar.MONTH) + 1);
                        Boolean firstdayFlag = false;

                        int changeWeekNum = 0;
                        if ((tmpDay.get(Calendar.MONTH) + 1) == (tmpDay2.get(Calendar.MONTH) + 1)) {

                            if (tmpDay.get(Calendar.DATE) == tmpDay2.get(Calendar.DATE)) {
                                changeWeekNum = tmpBobj.getWeekOfMon();
                                firstdayFlag = true;

                            } else if (tmpBobj.getWeekOfMon() > 4) {
                                changeWeekNum = 1;

                            } else {
                                changeWeekNum = tmpBobj.getWeekOfMon();

                            }

                        } else {
                            changeWeekNum = tmpBobj.getWeekOfMon();

                        }

                        if (firstdayFlag) {
                            if (changeWeekNum == 1) {
                                if (startMon == 1) {
                                    startYear = startYear - 1;
                                }
                                startMon = startMon - 1;
                                changeWeekNum = 5;
                            } else {
                                changeWeekNum = changeWeekNum - 1;
                            }

                        }

                        if (startMon < 10) {
                            strmon = "0" + startMon;
                        } else {
                            strmon = "" + startMon;
                        }

                        if (tmpDay.get(Calendar.DATE) < 10) {
                            strStartDay = "0" + tmpDay.get(Calendar.DATE);
                        } else {
                            strStartDay = "" + tmpDay.get(Calendar.DATE);
                        }

                        if (tmpDay2.get(Calendar.DATE) < 10) {
                            strEndDay = "0" + tmpDay2.get(Calendar.DATE);
                        } else {
                            strEndDay = "" + tmpDay2.get(Calendar.DATE);
                        }
                        dateDate = startYear + "年" + strmon + "月 第" + changeWeekNum + "週 " + strStartDay + "~" + strEndDay + "日";

                        tmpBobj.setDataDate(dateDate);
                    }
                }

                if (endFlag == 0) {
                    if (resultAl.size() > 0) {
                        tmpAobj = (AgentEfficientDataObject) resultAl.get(resultAl.size() - 1);
                        tmpCobj.setWeekNum(tmpEndDate.get(Calendar.WEEK_OF_YEAR));
                        tmpCobj.setAgentName("");
                        tmpCobj.setBeginTime("" + tmpEndDate.get(Calendar.YEAR));
                        tmpCobj.setDBflag(1);
                        tmpCobj.setTotalLoginReadyIdleDuration("0");
                        tmpCobj.setTotalLoginNotReadyIdleDuration("0");
                        tmpCobj.setTotalLoginPcpIdleDuration("0");
                        tmpCobj.setTotalDcpInboundCallsDuration("0");
                        tmpCobj.setTotalLoginDuration("0");
                        tmpCobj.setNbrDcpInboundCallsAnswered("0");
                        tmpCobj.setTotalPrivateOutBoundDuration("0");
                        tmpCobj.setTotalHoldDuration("0");
                        tmpCobj.setValidWorkTime("0");
                        tmpCobj.setWorkPercent("0.00%");
                        tmpCobj.setUsePercent("0.00%");

                        tmpCobj.setWeekDay();
                        tmpCobj.setBeetWeekDays();

                        tmpCobj.setIntTnbrDcpInboundCallsAnswered(0);
                        tmpCobj.setIntTotalLoginReadyIdleDuration(0);
                        tmpCobj.setIntTotalLoginNotReadyIdleDuration(0);
                        tmpCobj.setIntTotalLoginPcpIdleDuration(0);
                        tmpCobj.setIntTotalDcpInboundCallsDuration(0);
                        tmpCobj.setIntTotalLoginDuration(0);
                        tmpCobj.setIntTotalPrivateOutBoundDuration(0);
                        tmpCobj.setIntTotalHoldDuration(0);
                        tmpCobj.setIntValidWorkTime(0);
                        tmpCobj.setDoubleWorkPercent(0);
                        tmpCobj.setDoubleUsePercent(0);

                        Calendar tmpendDay = Calendar.getInstance();
                        Calendar tmpendDay2 = Calendar.getInstance();
                        tmpendDay = tmpEndDate;

                        tmpendDay2.set(tmpAobj.getEndYear(), tmpAobj.getEndMon() - 1, tmpAobj.getEndDay(), 0, 0, 0);
                        tmpendDay2.add(Calendar.DAY_OF_WEEK, 1);

                        String strmon = "";
                        String strStartDay = "";
                        String strEndDay = "";
                        if ((tmpendDay2.get(Calendar.MONTH) + 1) < 10) {
                            strmon = "0" + (tmpendDay2.get(Calendar.MONTH) + 1);
                        } else {
                            strmon = "" + (tmpendDay2.get(Calendar.MONTH) + 1);
                        }

                        if (tmpendDay2.get(Calendar.DATE) < 10) {
                            strStartDay = "0" + tmpendDay2.get(Calendar.DATE);
                        } else {
                            strStartDay = "" + tmpendDay2.get(Calendar.DATE);
                        }

                        if (tmpendDay.get(Calendar.DATE) < 10) {
                            strEndDay = "0" + tmpendDay.get(Calendar.DATE);
                        } else {
                            strEndDay = "" + tmpendDay.get(Calendar.DATE);
                        }
                        dateDate = tmpendDay2.get(Calendar.YEAR) + "年" + strmon + "月 第" + tmpendDay2.get(Calendar.WEEK_OF_MONTH) + "週 " + strStartDay + "~" + strEndDay + "日";
                        tmpCobj.setDataDate(dateDate);

                    }
                }

                ArrayList allDate = new ArrayList();
                if (startFlag == 0) {
                    allDate.add(tmpBobj);
                }

                for (int q = 0; q < resultAl.size(); q++) {
                    allDate.add(resultAl.get(q));
                }

                if (endFlag == 0) {
                    allDate.add(tmpCobj);
                }

                int intNbrDcpInboundCallsAnswered = 0;
                int intTotalLoginReadyIdleDuration = 0;
                int intTotalLoginNotReadyIdleDuration = 0;
                int intTotalLoginPcpIdleDuration = 0;
                int intTotalDcpInboundCallsDuration = 0;
                int intTotalLoginDuration = 0;
                int intTotalPrivateOutBoundDuration = 0;
                int intTotalHoldDuration = 0;

                usrName = UserInstance.getInstance().getUserObject(userID).getUserName();

                AgentEfficientDataObject tmpDataObj = new AgentEfficientDataObject();
                for (int k = 0; k < allDate.size(); k++) {
                    tmpDataObj = (AgentEfficientDataObject) allDate.get(k);
                    usrName = UserInstance.getInstance().getUserObject(tmpDataObj.getUserId()).getUserName();
                    intNbrDcpInboundCallsAnswered = intNbrDcpInboundCallsAnswered + tmpDataObj.getIntTnbrDcpInboundCallsAnswered();
                    intTotalLoginReadyIdleDuration = intTotalLoginReadyIdleDuration + tmpDataObj.getIntTotalLoginReadyIdleDuration();
                    intTotalLoginNotReadyIdleDuration = intTotalLoginNotReadyIdleDuration + tmpDataObj.getIntTotalLoginNotReadyIdleDuration();
                    intTotalLoginPcpIdleDuration = intTotalLoginPcpIdleDuration + tmpDataObj.getIntTotalLoginPcpIdleDuration();
                    intTotalDcpInboundCallsDuration = intTotalDcpInboundCallsDuration + tmpDataObj.getIntTotalDcpInboundCallsDuration();
                    intTotalLoginDuration = intTotalLoginDuration + tmpDataObj.getIntTotalLoginDuration();
                    intTotalPrivateOutBoundDuration = intTotalPrivateOutBoundDuration + tmpDataObj.getIntTotalPrivateOutBoundDuration();
                    intTotalHoldDuration = intTotalHoldDuration + tmpDataObj.getIntTotalHoldDuration();

                    //把秒數換成 00:00:00
                    tmpDataObj.setTotalLoginReadyIdleDuration(CalendarUtil.formatDate(tmpDataObj.getIntTotalLoginReadyIdleDuration()));
                    tmpDataObj.setTotalLoginNotReadyIdleDuration(CalendarUtil.formatDate(tmpDataObj.getIntTotalLoginNotReadyIdleDuration()));
                    tmpDataObj.setTotalLoginPcpIdleDuration(CalendarUtil.formatDate(tmpDataObj.getIntTotalLoginPcpIdleDuration()));
                    tmpDataObj.setTotalDcpInboundCallsDuration(CalendarUtil.formatDate(tmpDataObj.getIntTotalDcpInboundCallsDuration()));
                    tmpDataObj.setTotalLoginDuration(CalendarUtil.formatDate(tmpDataObj.getIntTotalLoginDuration()));
                    tmpDataObj.setTotalPrivateOutBoundDuration(CalendarUtil.formatDate(tmpDataObj.getIntTotalPrivateOutBoundDuration()));
                    tmpDataObj.setTotalHoldDuration(CalendarUtil.formatDate(tmpDataObj.getIntTotalHoldDuration()));

                    int tmpIntValidWorkTime = tmpDataObj.getIntTotalLoginDuration() - tmpDataObj.getIntTotalLoginNotReadyIdleDuration();
                    tmpDataObj.setValidWorkTime(CalendarUtil.formatDate(tmpIntValidWorkTime));

                    double tmpDoubleWorkPercent = 0;
                    String tmpStrWorkPercent = "0.00";
                    if (tmpDataObj.getIntTotalLoginDuration() > 0) {
                        tmpDoubleWorkPercent = (double) (tmpDataObj.getIntTotalDcpInboundCallsDuration() + tmpDataObj.getIntTotalHoldDuration() + tmpDataObj.getIntTotalLoginPcpIdleDuration() + tmpDataObj.getIntTotalLoginReadyIdleDuration()) / tmpDataObj.getIntTotalLoginDuration();
                        if (tmpDoubleWorkPercent > 0) {
                            tmpDoubleWorkPercent = tmpDoubleWorkPercent * 100;
                            tmpStrWorkPercent = dformat.format(tmpDoubleWorkPercent);
                        }

                    }
                    tmpStrWorkPercent = tmpStrWorkPercent + "%";
                    tmpDataObj.setWorkPercent(tmpStrWorkPercent);

                    double tmpDoubleUsePercent = 0;
                    String tmpStrUsePercent = "0.00";
                    if (tmpDataObj.getIntTotalLoginDuration() > 0) {
                        tmpDoubleUsePercent = (double) (tmpDataObj.getIntTotalDcpInboundCallsDuration() + tmpDataObj.getIntTotalHoldDuration() + tmpDataObj.getIntTotalLoginPcpIdleDuration()) / tmpDataObj.getIntTotalLoginDuration();

                        if (tmpDoubleUsePercent > 0) {
                            tmpDoubleUsePercent = tmpDoubleUsePercent * 100;
                            tmpStrUsePercent = dformat.format(tmpDoubleUsePercent);
                        }

                    }
                    tmpStrUsePercent = tmpStrUsePercent + "%";
                    tmpDataObj.setUsePercent(tmpStrUsePercent);

                    tmpDataObj.setAgentName(usrName);
                    prao.addDataList(tmpDataObj);
                }

                prao.setDataListnum(allDate.size());
                int totalTime = intTotalLoginReadyIdleDuration + intTotalLoginNotReadyIdleDuration + intTotalLoginPcpIdleDuration + intTotalDcpInboundCallsDuration;

                prao.setIntAllTnbrDcpInboundCallsAnswered(intNbrDcpInboundCallsAnswered);
                prao.setIntAllTotalDcpInboundCallsDuration(intTotalDcpInboundCallsDuration);
                prao.setIntAllTotalLoginDuration(intTotalLoginDuration);
                prao.setIntAllTotalLoginNotReadyIdleDuration(intTotalLoginNotReadyIdleDuration);
                prao.setIntAllTotalLoginPcpIdleDuration(intTotalLoginPcpIdleDuration);
                prao.setIntALLTotalLoginReadyIdleDuration(intTotalLoginReadyIdleDuration);
                prao.setIntAlltotalPrivateOutBoundDuration(intTotalPrivateOutBoundDuration);
                prao.setIntAllTotalHoldDuration(intTotalHoldDuration);

                int tmpAllIntValidWorkTime = intTotalLoginDuration - intTotalLoginNotReadyIdleDuration;

                double tmpAllDoubleWorkPercent = 0;
                String tmpAllStrWorkPercent = "0.00";
                if (intTotalLoginDuration > 0) {
                    tmpAllDoubleWorkPercent = (double) (intTotalDcpInboundCallsDuration + intTotalHoldDuration + intTotalLoginPcpIdleDuration + intTotalLoginReadyIdleDuration) / intTotalLoginDuration;
                    tmpAllDoubleWorkPercent = tmpAllDoubleWorkPercent * 100;
                    tmpAllStrWorkPercent = dformat.format(tmpAllDoubleWorkPercent);
                }
                tmpAllStrWorkPercent = tmpAllStrWorkPercent + "%";

                double tmpAllDoubleUsePercent = 0;
                String tmpAllStrUsePercent = "0.00";
                if (intTotalLoginDuration > 0) {
                    tmpAllDoubleUsePercent = (double) (intTotalDcpInboundCallsDuration + intTotalHoldDuration + intTotalLoginPcpIdleDuration) / intTotalLoginDuration;
                    tmpAllDoubleUsePercent = tmpAllDoubleUsePercent * 100;
                    tmpAllStrUsePercent = dformat.format(tmpAllDoubleUsePercent);
                }
                tmpAllStrUsePercent = tmpAllStrUsePercent + "%";





                //將秒數變成 00:00:00
                prao.setStrAllTotalDcpInboundCallsDuration(CalendarUtil.formatDate(intTotalDcpInboundCallsDuration));
                prao.setStrAllTotalLoginDuration(CalendarUtil.formatDate(intTotalLoginDuration));
                prao.setStrAllTotalLoginNotReadyIdleDuration(CalendarUtil.formatDate(intTotalLoginNotReadyIdleDuration));
                prao.setStrAllTotalLoginPcpIdleDuration(CalendarUtil.formatDate(intTotalLoginPcpIdleDuration));
                prao.setStrALLTotalLoginReadyIdleDuration(CalendarUtil.formatDate(intTotalLoginReadyIdleDuration));
                prao.setStrAllTotalPrivateOutBoundDuration(CalendarUtil.formatDate(intTotalPrivateOutBoundDuration));
                prao.setStrAllTotalHoldDuration(CalendarUtil.formatDate(intTotalHoldDuration));
                prao.setStrALLValidWorkTime(CalendarUtil.formatDate(tmpAllIntValidWorkTime));
                prao.setStrALLWorkPercent(tmpAllStrWorkPercent);
                prao.setStrALLUsePercent(tmpAllStrUsePercent);

                if (totalTime > 0) {
                    prao.setIntAllTotalDcpInboundCallsDurationPercent((double) intTotalDcpInboundCallsDuration / totalTime);
                    prao.setIntAllTotalLoginNotReadyIdleDurationPercent((double) intTotalLoginNotReadyIdleDuration / totalTime);
                    prao.setIntAllTotalLoginPcpIdleDurationPercent((double) intTotalLoginPcpIdleDuration / totalTime);
                    prao.setIntALLTotalLoginReadyIdleDurationPercent((double) intTotalLoginReadyIdleDuration / totalTime);

                } else {
                    prao.setIntAllTotalDcpInboundCallsDurationPercent(0);
                    prao.setIntAllTotalLoginNotReadyIdleDurationPercent(0);
                    prao.setIntAllTotalLoginPcpIdleDurationPercent(0);
                    prao.setIntALLTotalLoginReadyIdleDurationPercent(0);
                }

                totalTime = totalDuration_0 + totalDuration_1 + totalDuration_2 + totalDuration_3 + totalDuration_4;
                //將秒數變成 00:00:00
                prao.setAllTotalNotReadyDuration_0(CalendarUtil.formatDate(totalDuration_0));
                prao.setAllTotalNotReadyDuration_1(CalendarUtil.formatDate(totalDuration_1));
                prao.setAllTotalNotReadyDuration_2(CalendarUtil.formatDate(totalDuration_2));
                prao.setAllTotalNotReadyDuration_3(CalendarUtil.formatDate(totalDuration_3));
                prao.setAllTotalNotReadyDuration_4(CalendarUtil.formatDate(totalDuration_4));
                prao.setAllTotalNotReadyDuration_5(CalendarUtil.formatDate(totalDuration_5));
                prao.setAllTotalNotReadyDuration_6(CalendarUtil.formatDate(totalDuration_6));
                prao.setAllTotalNotReadyDuration_7(CalendarUtil.formatDate(totalDuration_7));
                prao.setAllTotalNotReadyDuration_8(CalendarUtil.formatDate(totalDuration_8));
                prao.setAllTotalNotReadyDuration_9(CalendarUtil.formatDate(totalDuration_9));
                prao.setAllTotalNotReadyDuration_10(CalendarUtil.formatDate(totalDuration_10));

                if (totalTime > 0) {
                    prao.setIntAllTotalNotReadyDurationPercent_0((double) totalDuration_0 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_1((double) totalDuration_1 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_2((double) totalDuration_2 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_3((double) totalDuration_3 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_4((double) totalDuration_4 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_5((double) totalDuration_5 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_6((double) totalDuration_6 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_7((double) totalDuration_7 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_8((double) totalDuration_8 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_9((double) totalDuration_9 / totalTime);
                    prao.setIntAllTotalNotReadyDurationPercent_10((double) totalDuration_10 / totalTime);
                }

                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;

                i = 0;
                pstmt = conn.prepareStatement(calllogsql);
                pstmt.setString(++i, prao.getStartDate() + " 00:00:00");
                pstmt.setString(++i, prao.getEndDate() + " 00:00:00");
                logger.info("calllogsql  =" + calllogsql);
                if (!"A".equals(timeType))
                    pstmt.setString(++i, userID);
                rs = pstmt.executeQuery();
                int intAllCallLogAmount = 0;
                int intAllNotValidPhone = 0;
                while (rs.next()) {
                    AgentEfficientDataObject tmpObj = valueTable.get(rs.getString("CREATETIME") + rs.getString("WEEKFIELD") + "-" + rs.getString("CREATORID"));

                    if (tmpObj != null) {
                        tmpObj.setIntNotValidPhone(rs.getInt("AMOUMT"));
                        tmpObj.setNotValidPhone(rs.getString("AMOUMT"));
                        intAllNotValidPhone = intAllNotValidPhone + rs.getInt("AMOUMT");
                    }
                }

                prao.setIntAllNotValidPhone(intAllNotValidPhone);

                prao.setStrALLNotValidPhone("" + intAllNotValidPhone);

                conn.close();
                conn = null;

                beans.put("reportData", prao);
            } catch (Exception e) {
                logger.error(e);
                throw e;
            } finally {

                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    if (pstmt != null) {
                        pstmt.close();
                        pstmt = null;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                try {
                    if (conn != null) {
                        conn.close();
                        conn = null;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        String path = httpServletRequest.getServletContext().getRealPath(reportPath);
        File file = new File(path);
        InputStream in = new FileInputStream(file);

        XLSTransformer transformer = new XLSTransformer();
        Workbook workbook = transformer.transformXLS(in, beans);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", new String(excelName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)))
                .body(outputStream.toByteArray());
    }

    public ArrayList getAllTime(String startYear, String startMon, String startDate, String endYear, String endMon, String endDate, String timeType, ArrayList tmpal) {
        ArrayList returnAl = new ArrayList();
        int monArray[] = new int[13];
        monArray[0] = 0;
        monArray[1] = 31;
        monArray[2] = monArray[1] + 29;
        monArray[3] = monArray[2] + 31;
        monArray[4] = monArray[3] + 30;
        monArray[5] = monArray[4] + 31;
        monArray[6] = monArray[5] + 30;
        monArray[7] = monArray[6] + 31;
        monArray[8] = monArray[7] + 31;
        monArray[9] = monArray[8] + 30;
        monArray[10] = monArray[9] + 31;
        monArray[11] = monArray[10] + 30;
        monArray[12] = monArray[11] + 31;
        DecimalFormat df = new DecimalFormat("########.00");

        Map<String, String> map = new HashMap<>();

        for (AgentEfficientDataObject a : (List<AgentEfficientDataObject>) tmpal) {
            if (!map.containsKey(a.getUserId())) {
                map.put(a.getUserId(), a.getUserId());
            }
        }

        if ("Y".equals(timeType)) {
            int intStartYear = Integer.parseInt(startYear);
            int intStartMon = Integer.parseInt(startMon);
            int intStartDate = Integer.parseInt(startDate);
            int intEndYear = Integer.parseInt(endYear);
            int intEndMon = Integer.parseInt(endMon);
            int intEndDate = Integer.parseInt(endDate);

            String BeginTime = "";
            Calendar fromDate = Calendar.getInstance();
            Calendar toDate = Calendar.getInstance();

            fromDate.set(intStartYear, intStartMon - 1, intStartDate, 0, 0, 0);
            toDate.set(intEndYear, intEndMon - 1, intEndDate, 0, 0, 0);

            ArrayList tmpAL = new ArrayList();
            //toDate.add(Calendar.YEAR,1);
            while (toDate.getTimeInMillis() > fromDate.getTimeInMillis()) {

                BeginTime = "" + fromDate.get(Calendar.YEAR);
                Integer sortNum = (Integer) (fromDate.get(Calendar.YEAR));
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    AgentEfficientDataObject Obj = new AgentEfficientDataObject();
                    Obj.setSortNum(sortNum);
                    Obj.setBeginTime(BeginTime);
                    Obj.setDataDate(BeginTime + "年");
                    Obj.setUserId(entry.getValue());
                    Obj.setAgentName("");
                    Obj.setTotalLoginReadyIdleDuration("00:00:00");
                    Obj.setTotalLoginNotReadyIdleDuration("00:00:00");
                    Obj.setTotalLoginPcpIdleDuration("00:00:00");
                    Obj.setTotalLoginDuration("00:00:00");
                    Obj.setTotalDcpInboundCallsDuration("00:00:00");
                    Obj.setTotalPrivateOutBoundDuration("00:00:00");
                    Obj.setTotalHoldDuration("00:00:00");
                    Obj.setValidWorkTime("00:00:00");
                    Obj.setWorkPercent("0.00%");
                    Obj.setUsePercent("0.00%");
                    Obj.setNbrDcpInboundCallsAnswered("0");

                    Obj.setIntTnbrDcpInboundCallsAnswered(0);
                    Obj.setIntTotalLoginReadyIdleDuration(0);
                    Obj.setIntTotalLoginNotReadyIdleDuration(0);
                    Obj.setIntTotalLoginPcpIdleDuration(0);
                    Obj.setIntTotalDcpInboundCallsDuration(0);
                    Obj.setIntTotalLoginDuration(0);
                    Obj.setIntTotalPrivateOutBoundDuration(0);
                    Obj.setIntTotalHoldDuration(0);
                    Obj.setIntValidWorkTime(0);
                    Obj.setDoubleWorkPercent(0);
                    Obj.setDoubleUsePercent(0);

                    tmpAL.add(Obj);
                }

                fromDate.add(Calendar.YEAR, 1);
            }

            ArrayList allList = new ArrayList();
            for (int n = 0; n < tmpal.size(); n++) {
                allList.add(tmpal.get(n));
            }

            AgentEfficientDataObject aobj = new AgentEfficientDataObject();
            AgentEfficientDataObject aobj2 = new AgentEfficientDataObject();
            int dataFlag = 0;
            for (int m = 0; m < tmpAL.size(); m++) {
                dataFlag = 0;
                aobj = (AgentEfficientDataObject) tmpAL.get(m);
                for (int u = 0; u < tmpal.size(); u++) {
                    aobj2 = (AgentEfficientDataObject) tmpal.get(u);
                    int a = aobj.getSortNum();
                    int b = aobj2.getSortNum();

                    String userIdA = aobj.getUserId();
                    String userIdB = aobj2.getUserId();

                    if (a == b && userIdA.equals(userIdB)) {
                        dataFlag = 1;
                    }


                }

                if (dataFlag == 0) {
                    allList.add(aobj);
                }

            }

            returnAl = allList;
        } else if ("M".equals(timeType)) {
            int intStartYear = Integer.parseInt(startYear);
            int intStartMon = Integer.parseInt(startMon);
            int intEndYear = Integer.parseInt(endYear);
            int intEndMon = Integer.parseInt(endMon);

            String BeginTime = "";
            Calendar fromDate = Calendar.getInstance();
            Calendar toDate = Calendar.getInstance();

            fromDate.set(intStartYear, intStartMon - 1, 1, 0, 0, 0);
            toDate.set(intEndYear, intEndMon - 1, 1, 0, 0, 0);

            ArrayList tmpAL = new ArrayList();
            String tempMon = "";
            toDate.add(Calendar.MONTH, 1);
            while (toDate.getTimeInMillis() > fromDate.getTimeInMillis()) {

                if ((fromDate.get(Calendar.MONTH) + 1) < 10) {
                    tempMon = "0" + (fromDate.get(Calendar.MONTH) + 1);
                } else {
                    tempMon = "" + (fromDate.get(Calendar.MONTH) + 1);
                }
                BeginTime = fromDate.get(Calendar.YEAR) + "年" + tempMon + "月";
                Integer sortNum = (Integer) ((fromDate.get(Calendar.YEAR) * 12) + (fromDate.get(Calendar.MONTH) + 1));
                for (Map.Entry<String, String> entry : map.entrySet()) {

                    AgentEfficientDataObject Obj = new AgentEfficientDataObject();
                    Obj.setSortNum(sortNum);
                    Obj.setBeginTime(BeginTime);
                    Obj.setDataDate(BeginTime);
                    Obj.setUserId(entry.getValue());
                    Obj.setAgentName("");
                    Obj.setTotalLoginReadyIdleDuration("00:00:00");
                    Obj.setTotalLoginNotReadyIdleDuration("00:00:00");
                    Obj.setTotalLoginPcpIdleDuration("00:00:00");
                    Obj.setTotalLoginDuration("00:00:00");
                    Obj.setTotalDcpInboundCallsDuration("00:00:00");
                    Obj.setTotalPrivateOutBoundDuration("00:00:00");
                    Obj.setTotalHoldDuration("00:00:00");
                    Obj.setValidWorkTime("00:00:00");
                    Obj.setWorkPercent("0.00%");
                    Obj.setUsePercent("0.00%");
                    Obj.setNbrDcpInboundCallsAnswered("0");

                    Obj.setIntTnbrDcpInboundCallsAnswered(0);
                    Obj.setIntTotalLoginReadyIdleDuration(0);
                    Obj.setIntTotalLoginNotReadyIdleDuration(0);
                    Obj.setIntTotalLoginPcpIdleDuration(0);
                    Obj.setIntTotalDcpInboundCallsDuration(0);
                    Obj.setIntTotalLoginDuration(0);
                    Obj.setIntTotalPrivateOutBoundDuration(0);
                    Obj.setIntTotalHoldDuration(0);
                    Obj.setIntValidWorkTime(0);
                    Obj.setDoubleWorkPercent(0);
                    Obj.setDoubleUsePercent(0);

                    tmpAL.add(Obj);
                }
                fromDate.add(Calendar.MONTH, 1);
            }
            ArrayList allList = new ArrayList();
            for (int n = 0; n < tmpal.size(); n++) {
                allList.add(tmpal.get(n));
            }

            AgentEfficientDataObject aobj = new AgentEfficientDataObject();
            AgentEfficientDataObject aobj2 = new AgentEfficientDataObject();
            int dataFlag = 0;
            for (int m = 0; m < tmpAL.size(); m++) {
                dataFlag = 0;
                aobj = (AgentEfficientDataObject) tmpAL.get(m);
                for (int u = 0; u < tmpal.size(); u++) {
                    aobj2 = (AgentEfficientDataObject) tmpal.get(u);
                    int a = aobj.getSortNum();
                    int b = aobj2.getSortNum();
                    String userIdA = aobj.getUserId();
                    String userIdB = aobj2.getUserId();

                    if (a == b && userIdA.equals(userIdB)) {
                        dataFlag = 1;
                    }

                }

                if (dataFlag == 0) {
                    allList.add(aobj);
                }

            }
            returnAl = allList;

        } else if ("W".equals(timeType)) {
            int intStartYear = Integer.parseInt(startYear);
            int intStartMon = Integer.parseInt(startMon);
            int intStartDay = Integer.parseInt(startDate);
            int intEndYear = Integer.parseInt(endYear);
            int intEndMon = Integer.parseInt(endMon);
            int intEndDay = Integer.parseInt(endDate);

            HashMap tmpHashMap = new HashMap();

            Calendar fromDate = Calendar.getInstance();
            Calendar toDate = Calendar.getInstance();
            fromDate.set(intStartYear, intStartMon - 1, intStartDay, 0, 0, 0);

            fromDate.getTime();  //這行要留著，不然會少一週
            toDate.set(intEndYear, intEndMon - 1, intEndDay, 0, 0, 0);
            fromDate.set(Calendar.DAY_OF_WEEK, fromDate.getFirstDayOfWeek() + 1);
            int startDaySortNum = intStartYear * 365 + monArray[intStartMon - 1] + intStartDay;
            int endDaySortNum = intEndYear * 365 + monArray[intEndMon - 1] + intEndDay;
            while (fromDate.getTimeInMillis() <= toDate.getTimeInMillis()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    AgentEfficientDataObject wobj = new AgentEfficientDataObject();
                    wobj.setWeekNum(fromDate.get(Calendar.WEEK_OF_YEAR));
                    wobj.setUserId(entry.getValue());
                    wobj.setAgentName("");
                    wobj.setBeginTime("" + fromDate.get(Calendar.YEAR));
                    wobj.setDBflag(1);
                    wobj.setTotalLoginReadyIdleDuration("0");
                    wobj.setTotalLoginNotReadyIdleDuration("0");
                    wobj.setTotalLoginPcpIdleDuration("0");
                    wobj.setTotalDcpInboundCallsDuration("0");
                    wobj.setTotalLoginDuration("0");
                    wobj.setTotalPrivateOutBoundDuration("0");
                    wobj.setTotalHoldDuration("0");
                    wobj.setValidWorkTime("0");
                    wobj.setWorkPercent("0.00%");
                    wobj.setUsePercent("0.00%");
                    wobj.setNbrDcpInboundCallsAnswered("0");
                    wobj.setWeekDay();
                    wobj.setBeetWeekDays();
                    wobj.setIntTnbrDcpInboundCallsAnswered(0);
                    wobj.setIntTotalLoginReadyIdleDuration(0);
                    wobj.setIntTotalLoginNotReadyIdleDuration(0);
                    wobj.setIntTotalLoginPcpIdleDuration(0);
                    wobj.setIntTotalDcpInboundCallsDuration(0);
                    wobj.setIntTotalLoginDuration(0);
                    wobj.setIntTotalPrivateOutBoundDuration(0);
                    wobj.setIntTotalHoldDuration(0);
                    wobj.setIntValidWorkTime(0);
                    wobj.setDoubleWorkPercent(0);
                    wobj.setDoubleUsePercent(0);

                    tmpHashMap.put(wobj.getSortNum() + "-" + wobj.getUserId(), wobj);
                }
                fromDate.add(Calendar.DAY_OF_WEEK, 7);
                fromDate.getTime();
            }

            Map<String, AgentEfficientDataObject> tmpMap = new HashMap<String, AgentEfficientDataObject>();
            HashMap dbDataHmap = new HashMap();
            AgentEfficientDataObject objTmp1 = new AgentEfficientDataObject();
            AgentEfficientDataObject objTmp2 = new AgentEfficientDataObject();
            if (tmpal.size() > 0) {
                for (AgentEfficientDataObject objTmp333 : (List<AgentEfficientDataObject>) tmpal) {

                    String key = objTmp333.getSortNum() + "-" + objTmp333.getUserId();
                    if (!tmpMap.containsKey(key)) tmpMap.put(key, objTmp333);
                    if (!dbDataHmap.containsKey(key)) dbDataHmap.put(key, objTmp333);

                }

//                objTmp1 = (AgentEfficientDataObject) tmpal.get(0);
//                tmpMap.put(objTmp1.getSortNum() + "-" + objTmp1.getAgentName(), objTmp1);
//                dbDataHmap.put(objTmp1.getSortNum(), objTmp1);
            }

            for (int y = 1; y < tmpal.size(); y++) {
                objTmp1 = (AgentEfficientDataObject) tmpal.get(y);
                objTmp2 = tmpMap.get(objTmp1.getSortNum() + "-" + objTmp1.getUserId());
                if (objTmp2 != null) {
                    objTmp2.setTotalLoginReadyIdleDuration("" + (Integer.parseInt(objTmp2.getTotalLoginReadyIdleDuration()) + Integer.parseInt(objTmp1.getTotalLoginReadyIdleDuration())));
                    objTmp2.setTotalLoginNotReadyIdleDuration("" + (Integer.parseInt(objTmp2.getTotalLoginNotReadyIdleDuration()) + Integer.parseInt(objTmp1.getTotalLoginNotReadyIdleDuration())));
                    objTmp2.setTotalLoginPcpIdleDuration("" + (Integer.parseInt(objTmp2.getTotalLoginPcpIdleDuration()) + Integer.parseInt(objTmp1.getTotalLoginPcpIdleDuration())));
                    objTmp2.setNbrDcpInboundCallsAnswered("" + (Integer.parseInt(objTmp2.getNbrDcpInboundCallsAnswered()) + Integer.parseInt(objTmp1.getNbrDcpInboundCallsAnswered())));
                    objTmp2.setTotalLoginDuration("" + (Integer.parseInt(objTmp2.getTotalLoginDuration()) + Integer.parseInt(objTmp1.getTotalLoginDuration())));

                    objTmp2.setTotalDcpInboundCallsDuration("" + (Integer.parseInt(objTmp2.getTotalDcpInboundCallsDuration()) + Integer.parseInt(objTmp1.getTotalDcpInboundCallsDuration())));
                    objTmp2.setTotalPrivateOutBoundDuration("" + (Integer.parseInt(objTmp2.getTotalPrivateOutBoundDuration()) + Integer.parseInt(objTmp1.getTotalPrivateOutBoundDuration())));
                    objTmp2.setTotalHoldDuration("" + (Integer.parseInt(objTmp2.getTotalHoldDuration()) + Integer.parseInt(objTmp1.getTotalHoldDuration())));

                    int tmpIntTotalLoginDuration = Integer.parseInt(objTmp2.getTotalLoginDuration());
                    int tmpIntTotalLoginNotReadyIdleDuration = Integer.parseInt(objTmp2.getTotalLoginNotReadyIdleDuration());
                    int tmpIntTotalDcpInboundCallsDuration = Integer.parseInt(objTmp2.getNbrDcpInboundCallsAnswered());
                    int tmpIntTotalHoldDuration = Integer.parseInt(objTmp2.getTotalHoldDuration());
                    int tmpIntTotalLoginPcpIdleDuration = Integer.parseInt(objTmp2.getTotalLoginPcpIdleDuration());
                    int tmpTotalLoginReadyIdleDuration = Integer.parseInt(objTmp2.getTotalLoginReadyIdleDuration());

                    int tmpIntValidWorkTime = tmpIntTotalLoginDuration - tmpIntTotalLoginNotReadyIdleDuration;
                    objTmp2.setValidWorkTime("" + tmpIntValidWorkTime);

                    int tmpIntWorkPercent = 0;
                    if (tmpIntTotalLoginDuration > 0)
                        tmpIntWorkPercent = (tmpIntTotalDcpInboundCallsDuration + tmpIntTotalHoldDuration + tmpIntTotalLoginPcpIdleDuration + tmpTotalLoginReadyIdleDuration) / tmpIntTotalLoginDuration;
                    objTmp2.setWorkPercent("" + tmpIntWorkPercent);
                    //利用率=(總通話時間+總保留時間+總文書時間)/總登入時間
                    int tmpIntUsePercent = 0;
                    if (tmpIntTotalLoginDuration > 0)
                        tmpIntUsePercent = (tmpIntTotalDcpInboundCallsDuration + tmpIntTotalHoldDuration + tmpIntTotalLoginPcpIdleDuration + tmpTotalLoginReadyIdleDuration) / tmpIntTotalLoginDuration;
                    objTmp2.setUsePercent("" + tmpIntUsePercent);

                    tmpMap.put(objTmp2.getSortNum() + "-" + objTmp2.getUserId(), objTmp2);

                } else {
                    tmpMap.put(objTmp1.getSortNum() + "-" + objTmp1.getUserId(), objTmp1);
                }

            }

            Collection collection = tmpMap.values();
            Iterator iterator = collection.iterator();
            ArrayList DBDataAL = new ArrayList();
            while (iterator.hasNext()) {
                DBDataAL.add(iterator.next());
            }

            Collection collection2 = tmpHashMap.values();
            Iterator iterator2 = collection2.iterator();
            ArrayList DBDataAL2 = new ArrayList();
            while (iterator2.hasNext()) {
                DBDataAL2.add(iterator2.next());
            }

            AgentEfficientDataObject aobjaaaa = new AgentEfficientDataObject();
            for (int ttt = 0; ttt < DBDataAL.size(); ttt++) {
                aobjaaaa = (AgentEfficientDataObject) DBDataAL.get(ttt);

            }

            AgentEfficientDataObject aobjaa = new AgentEfficientDataObject();
            for (int tt = 0; tt < DBDataAL2.size(); tt++) {
                aobjaa = (AgentEfficientDataObject) DBDataAL2.get(tt);

            }

            ArrayList tmpal2 = new ArrayList();
            for (int z = 0; z < DBDataAL.size(); z++) {
                tmpal2.add(DBDataAL.get(z));
            }

            AgentEfficientDataObject aobj = new AgentEfficientDataObject();
            AgentEfficientDataObject aobj2 = new AgentEfficientDataObject();
            int dataFlag = 0;
            for (int m = 0; m < DBDataAL2.size(); m++) {
                dataFlag = 0;
                aobj = (AgentEfficientDataObject) DBDataAL2.get(m);
                for (int u = 0; u < DBDataAL.size(); u++) {
                    aobj2 = (AgentEfficientDataObject) DBDataAL.get(u);
                    int a = aobj.getSortNum();
                    int b = aobj2.getSortNum();
                    String userIdA = aobj.getUserId();
                    String userIdB = aobj2.getUserId();

                    if (a == b && userIdA.equals(userIdB)) {
                        dataFlag = 1;
                    }

                }

                if (dataFlag == 0) {
                    tmpal2.add(aobj);
                }

            }

            returnAl = tmpal2;
            AgentEfficientDataObject ccc = new AgentEfficientDataObject();
            for (int y = 0; y < returnAl.size(); y++) {
                ccc = (AgentEfficientDataObject) returnAl.get(y);
            }
        } else if ("D".equals(timeType)) {
            int intStartYear = Integer.parseInt(startYear);
            int intStartMon = Integer.parseInt(startMon);
            int intStartDate = Integer.parseInt(startDate);
            int intEndYear = Integer.parseInt(endYear);
            int intEndMon = Integer.parseInt(endMon);
            int intEndDate = Integer.parseInt(endDate);

            String BeginTime = "";
            Calendar fromDate = Calendar.getInstance();
            Calendar toDate = Calendar.getInstance();

            fromDate.set(intStartYear, intStartMon - 1, intStartDate, 0, 0, 0);
            toDate.set(intEndYear, intEndMon - 1, intEndDate, 0, 0, 0);
            toDate.add(Calendar.DATE, 1);
            ArrayList tmpAL = new ArrayList();
            String tempMon = "";
            String tempDay = "";
            while (toDate.getTimeInMillis() > fromDate.getTimeInMillis()) {

                if ((fromDate.get(Calendar.MONTH) + 1) < 10) {
                    tempMon = "0" + (fromDate.get(Calendar.MONTH) + 1);
                } else {
                    tempMon = "" + (fromDate.get(Calendar.MONTH) + 1);
                }

                if ((fromDate.get(Calendar.DATE)) < 10) {
                    tempDay = "0" + fromDate.get(Calendar.DATE);
                } else {
                    tempDay = "" + fromDate.get(Calendar.DATE);
                }
                BeginTime = fromDate.get(Calendar.YEAR) + "年" + tempMon + "月" + tempDay + "日";
                Integer sortNum = (Integer) ((fromDate.get(Calendar.YEAR) * 365) + monArray[fromDate.get(Calendar.MONTH)] + (fromDate.get(Calendar.DATE)));
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    AgentEfficientDataObject Obj = new AgentEfficientDataObject();
                    Obj.setSortNum(sortNum);
                    Obj.setBeginTime(BeginTime);
                    Obj.setDataDate(BeginTime);
                    Obj.setUserId(entry.getValue());
                    Obj.setAgentName("");
                    Obj.setTotalLoginReadyIdleDuration("00:00:00");
                    Obj.setTotalLoginNotReadyIdleDuration("00:00:00");
                    Obj.setTotalLoginPcpIdleDuration("00:00:00");
                    Obj.setTotalLoginDuration("00:00:00");
                    Obj.setTotalDcpInboundCallsDuration("00:00:00");
                    Obj.setTotalPrivateOutBoundDuration("00:00:00");
                    Obj.setTotalHoldDuration("00:00:00");
                    Obj.setValidWorkTime("00:00:00");
                    Obj.setWorkPercent("0.00%");
                    Obj.setUsePercent("0.00%");

                    Obj.setNbrDcpInboundCallsAnswered("0");
                    Obj.setIntTnbrDcpInboundCallsAnswered(0);
                    Obj.setIntTotalLoginReadyIdleDuration(0);
                    Obj.setIntTotalLoginNotReadyIdleDuration(0);
                    Obj.setIntTotalLoginPcpIdleDuration(0);
                    Obj.setIntTotalDcpInboundCallsDuration(0);
                    Obj.setIntTotalLoginDuration(0);
                    Obj.setIntTotalPrivateOutBoundDuration(0);
                    Obj.setIntTotalHoldDuration(0);
                    Obj.setIntValidWorkTime(0);
                    Obj.setDoubleWorkPercent(0);
                    Obj.setDoubleUsePercent(0);

                    Obj.setIntTotalNotReadyDuration_0(0);
                    Obj.setIntTotalNotReadyDuration_1(0);
                    Obj.setIntTotalNotReadyDuration_2(0);
                    Obj.setIntTotalNotReadyDuration_3(0);
                    Obj.setIntTotalNotReadyDuration_4(0);
                    Obj.setIntTotalNotReadyDuration_5(0);
                    Obj.setIntTotalNotReadyDuration_6(0);
                    Obj.setIntTotalNotReadyDuration_7(0);
                    Obj.setIntTotalNotReadyDuration_8(0);
                    Obj.setIntTotalNotReadyDuration_9(0);
                    Obj.setIntTotalNotReadyDuration_10(0);

                    Obj.setTotalNotReadyDuration_0(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_1(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_2(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_3(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_4(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_5(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_6(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_7(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_8(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_9(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_10(CalendarUtil.formatDate(0));

                    tmpAL.add(Obj);
                }
                fromDate.add(Calendar.DATE, 1);
            }

            ArrayList allList = new ArrayList();
            for (int n = 0; n < tmpal.size(); n++) {
                allList.add(tmpal.get(n));
            }

            AgentEfficientDataObject aobj = new AgentEfficientDataObject();
            AgentEfficientDataObject aobj2 = new AgentEfficientDataObject();
            int dataFlag = 0;
            for (int m = 0; m < tmpAL.size(); m++) {
                dataFlag = 0;
                aobj = (AgentEfficientDataObject) tmpAL.get(m);
                for (int u = 0; u < tmpal.size(); u++) {
                    aobj2 = (AgentEfficientDataObject) tmpal.get(u);
                    int a = aobj.getSortNum();
                    int b = aobj2.getSortNum();
                    String userIdA = aobj.getUserId();
                    String userIdB = aobj2.getUserId();

                    if (a == b && userIdA.equals(userIdB)) {
                        dataFlag = 1;
                    }
                }

                if (dataFlag == 0) {
                    allList.add(aobj);
                }

            }

            returnAl = allList;

        } else if ("H".equals(timeType)) {
            int intStartYear = Integer.parseInt(startYear);
            int intStartMon = Integer.parseInt(startMon);
            int intStartDate = Integer.parseInt(startDate);
            int intEndYear = Integer.parseInt(endYear);
            int intEndMon = Integer.parseInt(endMon);
            int intEndDate = Integer.parseInt(endDate);

            String BeginTime = "";
            Calendar fromDate = Calendar.getInstance();
            Calendar toDate = Calendar.getInstance();

            fromDate.set(intStartYear, intStartMon - 1, intStartDate, 0, 0, 0);
            toDate.set(intEndYear, intEndMon - 1, intEndDate, 23, 59, 59);

            ArrayList tmpAL = new ArrayList();
            String tempMon = "";
            String tempDay = "";
            String tempHH = "";
            while (toDate.getTimeInMillis() > fromDate.getTimeInMillis()) {

                if ((fromDate.get(Calendar.MONTH) + 1) < 10) {
                    tempMon = "0" + (fromDate.get(Calendar.MONTH) + 1);
                } else {
                    tempMon = "" + (fromDate.get(Calendar.MONTH) + 1);
                }

                if ((fromDate.get(Calendar.DATE)) < 10) {
                    tempDay = "0" + fromDate.get(Calendar.DATE);
                } else {
                    tempDay = "" + fromDate.get(Calendar.DATE);
                }

                if ((fromDate.get(Calendar.HOUR_OF_DAY)) < 10) {
                    tempHH = "0" + fromDate.get(Calendar.HOUR_OF_DAY);
                } else {
                    tempHH = "" + fromDate.get(Calendar.HOUR_OF_DAY);
                }

                BeginTime = fromDate.get(Calendar.YEAR) + "年" + tempMon + "月" + tempDay + "日 " + tempHH + "時";

                Integer sortNum = (Integer) ((((fromDate.get(Calendar.YEAR) * 365) + monArray[fromDate.get(Calendar.MONTH)] + (fromDate.get(Calendar.DATE)))) * 24 + fromDate.get(Calendar.HOUR_OF_DAY));
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    AgentEfficientDataObject Obj = new AgentEfficientDataObject();
                    Obj.setSortNum(sortNum);
                    Obj.setBeginTime(BeginTime);
                    Obj.setDataDate(BeginTime);
                    Obj.setUserId(entry.getValue());
                    Obj.setAgentName("");
                    Obj.setTotalLoginReadyIdleDuration("00:00:00");
                    Obj.setTotalLoginNotReadyIdleDuration("00:00:00");
                    Obj.setTotalLoginPcpIdleDuration("00:00:00");
                    Obj.setTotalLoginDuration("00:00:00");
                    Obj.setTotalDcpInboundCallsDuration("00:00:00");
                    Obj.setTotalPrivateOutBoundDuration("00:00:00");
                    Obj.setTotalHoldDuration("00:00:00");
                    Obj.setValidWorkTime("00:00:00");
                    Obj.setWorkPercent("0.00%");
                    Obj.setUsePercent("0.00%");

                    Obj.setNbrDcpInboundCallsAnswered("0");
                    Obj.setIntTnbrDcpInboundCallsAnswered(0);
                    Obj.setIntTotalLoginReadyIdleDuration(0);
                    Obj.setIntTotalLoginNotReadyIdleDuration(0);
                    Obj.setIntTotalLoginPcpIdleDuration(0);
                    Obj.setIntTotalDcpInboundCallsDuration(0);
                    Obj.setIntTotalLoginDuration(0);
                    Obj.setIntTotalPrivateOutBoundDuration(0);
                    Obj.setIntTotalHoldDuration(0);
                    Obj.setIntValidWorkTime(0);
                    Obj.setDoubleWorkPercent(0);
                    Obj.setDoubleUsePercent(0);

                    Obj.setIntTotalNotReadyDuration_0(0);
                    Obj.setIntTotalNotReadyDuration_1(0);
                    Obj.setIntTotalNotReadyDuration_2(0);
                    Obj.setIntTotalNotReadyDuration_3(0);
                    Obj.setIntTotalNotReadyDuration_4(0);
                    Obj.setIntTotalNotReadyDuration_5(0);
                    Obj.setIntTotalNotReadyDuration_6(0);
                    Obj.setIntTotalNotReadyDuration_7(0);
                    Obj.setIntTotalNotReadyDuration_8(0);
                    Obj.setIntTotalNotReadyDuration_9(0);
                    Obj.setIntTotalNotReadyDuration_10(0);

                    Obj.setTotalNotReadyDuration_0(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_1(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_2(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_3(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_4(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_5(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_6(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_7(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_8(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_9(CalendarUtil.formatDate(0));
                    Obj.setTotalNotReadyDuration_10(CalendarUtil.formatDate(0));

                    tmpAL.add(Obj);
                }
                fromDate.add(Calendar.HOUR, 1);
            }

            ArrayList allList = new ArrayList();
            for (int n = 0; n < tmpal.size(); n++) {
                allList.add(tmpal.get(n));
            }

            AgentEfficientDataObject aobj = new AgentEfficientDataObject();
            AgentEfficientDataObject aobj2 = new AgentEfficientDataObject();
            int dataFlag = 0;
            for (int m = 0; m < tmpAL.size(); m++) {
                dataFlag = 0;
                aobj = (AgentEfficientDataObject) tmpAL.get(m);
                for (int u = 0; u < tmpal.size(); u++) {
                    aobj2 = (AgentEfficientDataObject) tmpal.get(u);
                    int a = aobj.getSortNum();
                    int b = aobj2.getSortNum();
                    String userIdA = aobj.getUserId();
                    String userIdB = aobj2.getUserId();

                    if (a == b && userIdA.equals(userIdB)) {
                        dataFlag = 1;
                    }

                }

                if (dataFlag == 0) {
                    allList.add(aobj);
                }

            }
            returnAl = allList;
        }

        return returnAl;
    }

}




