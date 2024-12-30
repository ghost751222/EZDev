
package com.consilium.excel.consilium;

import com.consilium.excel.componet.ApplicationCode;
import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.DynaHashValue;
import com.consilium.excel.models.UserInstance;
import com.consilium.excel.models.UserObject;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("ConsiliumCallAmountCompareAllExcelImpl")
public class ConsiliumCallAmountCompareAll implements ExcelInterface {


    private enum sqlChoice {
        KPI,//KPI
        connectType, // 接通類型統計
        connectCall, // 話務流量分析-進線量

        typeOfDisposal, // 服務類型分析
        callWaitingCount // 處理時間
    }

    private enum callWaitingCountType {
        MAXWAITCALLNBR, // 最多等候人數
        AVERAGEWAIT, // 平均等候時間
        AVERAGECOMMUNICATION // 平均通話時間
    }

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private static final Logger logger = Logger.getLogger(ConsiliumAgentEfficientExcelImpl.class);

    private String excelName = "服務概況表.xls";
    private String reportPath = "/WEB-INF/report/consilium/CallAmountCompareAll.xls";

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String[] sqlQuery = new String[sqlChoice.values().length];

        Map beans = new HashMap();

        NumberFormat format = NumberFormat.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();
        format.setMinimumIntegerDigits(2);
        ArrayList<String> al = new ArrayList<String>();

        String acceptType = jsonNode.get("acceptType").asText();
        String dateType = jsonNode.get("dateType").asText();
        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();

        String bDataTime_YYYY = startDate.substring(0, 4);
        String eDataTime_YYYY = endDate.substring(0, 4);
        String bDataTime_MM = startDate.substring(5, 7);
        String bDataTime_DD = startDate.substring(8, 10);
        String eDataTime_MM = endDate.substring(5, 7);
        String eDataTime_DD = endDate.substring(8, 10);

        String printDate = dateFormater.format(now.getTime());
        UserObject userObj = UserInstance.getInstance().getUserObject(httpServletRequest);
        String userName = userObj.getUserName();
        beans.put("startDate", startDate);
        beans.put("endDate", endDate);
        beans.put("printDate", printDate);
        beans.put("userName", userName);
        //取得 展現方式 依年Y 依月M 依週W 依日D 依時H 依總數A
        //String timeType = request.getParameter("timeType");
        String timeType = jsonNode.get("timeType").asText();

        //計算時間
        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        Calendar tempDate = Calendar.getInstance();
        fromDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1,
                Integer.parseInt(bDataTime_DD), 0, 0, 0);
        toDate.set(Integer.parseInt(eDataTime_YYYY), Integer.parseInt(eDataTime_MM) - 1, Integer.parseInt(eDataTime_DD),
                0, 0, 0);
        tempDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1,
                Integer.parseInt(bDataTime_DD), 0, 0, 0);

        //  取得String-年
        String tempYear = Integer.toString(tempDate.get(Calendar.YEAR));

        //  取得String-月，缺0補0
        String tempMonth = "";
        int tempIntMonth = tempDate.get(Calendar.MONTH) + 1;
        if (tempIntMonth < 10)
            tempMonth = "0" + Integer.toString(tempIntMonth);
        else
            tempMonth = Integer.toString(tempIntMonth);

        //  WeekOfYear
        String tempWeekOfYear = "";
        int tempIntWeekOfYear = tempDate.get(Calendar.WEEK_OF_YEAR) + 1;
        if (tempIntWeekOfYear < 10)
            tempWeekOfYear = "0" + Integer.toString(tempIntWeekOfYear);
        else
            tempWeekOfYear = Integer.toString(tempIntWeekOfYear);

        //  WeekOfMonth
        String tempWeekOfMonth = "";
        int tempIntWeekOfMonth = tempDate.get(Calendar.WEEK_OF_MONTH);
        if (tempIntWeekOfMonth < 10)
            tempWeekOfMonth = "0" + Integer.toString(tempIntWeekOfMonth);
        else
            tempWeekOfMonth = Integer.toString(tempIntWeekOfMonth);

        //  Week
        // int tempWeek = 0;

        //  取得String-日，缺0補0
        String tempDay = "";
        int tempIntDay = tempDate.get(Calendar.DAY_OF_MONTH);
        if (tempIntDay < 10)
            tempDay = "0" + Integer.toString(tempIntDay);
        else
            tempDay = Integer.toString(tempIntDay);

        //計算年數、月份數、天數
        int cntDay = (int) ((toDate.getTimeInMillis() - fromDate.getTimeInMillis()) / (1000 * 60 * 60) + 24) / 24;
        int cntYear = (int) (toDate.get((Calendar.YEAR)) - fromDate.get((Calendar.YEAR))) + 1;

        int cntMonth = 0;
        if (cntYear == 1)
            cntMonth = (int) (toDate.get(Calendar.MONTH) - fromDate.get(Calendar.MONTH)) + 1;
        else
            cntMonth = ((12 - fromDate.get(Calendar.MONTH)) + (toDate.get(Calendar.MONTH) + 1) + ((cntYear - 2) * 12));

        int i = 0;

        HashMap hp = new HashMap();
        HashMap hp2 = new HashMap(); // 週分類時，key為日期索引，object為hp3
        HashMap hp3 = new HashMap(); // 週分類時，key為"G01"類別，object為次數加總
        DynaHashValue report = new DynaHashValue();
        beans.put("report", report);

        String acceptTypeSql = "";
        String kpiCntSql = " abandoned XCNT, chattingCalls + voiceCalls AS CCNT ";

        if ("T".equals(acceptType)) {
            kpiCntSql = " abandoned XCNT, voiceCalls AS CCNT ";
        }else if ("L".equals(acceptType)){
            kpiCntSql = " 0 XCNT, chattingCalls AS CCNT ";
        }else  if("I".equals(acceptType)){
            kpiCntSql = " 0 XCNT, 0 AS CCNT ";
        } else if("TL".equals(acceptType)){
            kpiCntSql = " abandoned XCNT, chattingCalls + voiceCalls AS CCNT ";
        }

        if ("L".equals(acceptType) || "I".equals(acceptType)) {
            acceptTypeSql = " AND CASETYPE = ? ";
        } else if ("T".equals(acceptType)) {
            acceptTypeSql = " AND CASETYPE NOT IN(?, ?) ";
        } else if ("TL".equals(acceptType)) {
            acceptTypeSql = " AND CASETYPE NOT IN(?) ";
        }

        String dateColumn = "";
        //依年Y 依月M 依週W 依日D產生Obj和Sql
        if ("Y".equals(timeType)) {
            for (i = 0; i < cntYear; i++) {
                tempYear = Integer.toString(tempDate.get(Calendar.YEAR));
                dateColumn = tempYear + "年";
                hp.put(tempYear, dateColumn);
                tempDate.add(Calendar.YEAR, 1);
                report.addRowTitle(dateColumn, dateColumn);
            }

            sqlQuery[sqlChoice.connectType.ordinal()] =
                    " SELECT  datepart(yyyy,[dateTime]) AS YEAR, " +
                            " sum(voiceCalls) voiceCalls , sum(chattingCalls) chattingCalls ,sum(ivr) ivr ,sum(answered) answered" +
                            " FROM                                                          " +
                            "   CallLog1                                                " +
                            " WHERE                                                         " +
                            "   [dateTime]>=?            " +
                            " AND                                                           " +
                            "   [dateTime]<=?                     " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : "")) +
                            " GROUP BY  datepart(yyyy,[dateTime])                                                    " +
                            " ORDER BY  datepart(yyyy,[dateTime])                                                    ";

            sqlQuery[sqlChoice.connectCall.ordinal()] =
                    " SELECT datepart(yyyy,CREATETIME) AS YEAR,                  " +
                            "   count(*)  as cnt" +
                            " FROM                                                        " +
                            "   CALLLOG_FORM with(nolock)                                          " +
                            " WHERE                                                       " +
                            "   CREATETIME >= ?          " +
                            " AND                                                         " +
                            "   CREATETIME <= ? " + acceptTypeSql +

//                            " AND                                                           " +
//                            "   caseType is not null                                        " +
//                            " AND                                                           " +
//                            "   trim(caseType) <>'V'                                         " +

                            //" And callType in ('I','J') or (M7480CALLID = NULL AND SERVICETYPE=NULL AND SERVICEITEM=NULL AND PROCESSTYPE=NULL AND CALLTYPE=NULL AND CASETYPE=NULL)" +
                            " And callType in ('I','J')" +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY datepart(yyyy,CREATETIME)                         ";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                       " +
                            "   PROCESSTYPE, COUNT(*) AS CNT,                              " +
                            "    datepart(yyyy,CREATETIME) AS YEAR                         " +
                            " FROM                                                         " +
                            "   CALLLOG_FORM  with(nolock)                                             " +
                            " WHERE                                                        " +
                            "   CREATETIME>=?           " +
                            " AND                                                          " +
                            "   CREATETIME<=?           " + acceptTypeSql +
                            " AND                                                          " +
                            "   PROCESSTYPE IS NOT NULL                                    " +

                            //  " and ringTime is not null  " +
                            " and serviceType is not null  " +
                            //  " and serviceItem is not null and serviceItem <> '' " +
                            //" and caseType is not null and caseType <> 'V'" +
                            " and callType is not null  " +
                            " And callType not in ('I','J') " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME)                    " +
                            " ORDER BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME)                    ";

            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                            " +
                            "   DATEPART(YYYY, CallLog1.[datetime])               AS YEAR,                             " +

                            "   SUM(AVG_WAIT_TIME) AVG_WAIT_TIME,       " + //平均等候
                            "   SUM(AVG_TALK_TIME) AVG_TALK_TIME,        " + //平均通話長度
                            "   MAX(AVG_ABANDONED) MAX_ABANDONED ,       " + //平均放棄數
                            "   TYPE " +
                            " FROM                                                              " +
                            "   CallLog1  inner join AvgWaitingAndTalking  on CallLog1.datetime =AvgWaitingAndTalking.datetime             " +
                            " WHERE                                                             " +
                            "   CallLog1.[datetime] >= ?                                  " +
                            " AND                                                               " +
                            "   CallLog1.[datetime] <= ?                                  " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CallLog1.[datetime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,[datetime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : "")) +

                            " GROUP BY                                                          " +
                            "   DATEPART(YYYY, CallLog1.[datetime])    ,TYPE                                         " +
                            " ORDER BY                                                          " +
                            "   DATEPART(YYYY, CallLog1.[datetime])                                                    ";

            sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT LEFT(DataDate,4) AS 'YEAR',   " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    "SELECT " +
                    " cast([datetime] as date) AS SDAY, " +
                    " format([datetime],'yyyy年') AS  DataDate, " +
                    " format([datetime],'HH') AS HOUR, " +
                    //" abandoned XCNT, answered AS CCNT " +
                    kpiCntSql +
                    " FROM CallLog1 " +
                    " WHERE [datetime] >= ? " +
                    " AND [datetime] <= ?" +

                    ("H".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                    " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : ("W".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                    " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : "")) +

                    " GROUP BY cast([datetime] as date),format([datetime],'yyyy年'),format([datetime],'HH') ,answered ,abandoned ,chattingCalls,voiceCalls" +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B " +
                    " GROUP BY  LEFT(DataDate,4)  " +
                    " ORDER BY  LEFT(DataDate,4)  ";

        } else if ("M".equals(timeType)) {
            for (i = 0; i < cntMonth; i++) {
                tempYear = Integer.toString(tempDate.get(Calendar.YEAR));
                tempIntMonth = tempDate.get(Calendar.MONTH) + 1;
                if (tempIntMonth < 10)
                    tempMonth = "0" + Integer.toString(tempIntMonth);
                else
                    tempMonth = Integer.toString(tempIntMonth);

                dateColumn = tempYear + "年" + tempMonth + "月";
                hp.put(tempYear + tempMonth, dateColumn);
                tempDate.add(Calendar.MONTH, 1);
                report.addRowTitle(dateColumn, dateColumn);
            }

            sqlQuery[sqlChoice.connectType.ordinal()] =
                    " SELECT      datepart(yyyy,[dateTime]) AS YEAR,                  " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,[dateTime]),'')), 2) AS MONTH,                         " +
                            " sum(voiceCalls) voiceCalls , sum(chattingCalls) chattingCalls ,sum(ivr) ivr ,sum(answered) answered" +
                            " FROM                                                          " +
                            "   CallLog1                                                " +
                            " WHERE                                                         " +
                            "   [dateTime]>=?            " +
                            " AND                                                           " +
                            "   [dateTime]<=?                     " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : "")) +
                            " GROUP BY  datepart(yyyy,[dateTime]),RIGHT(CONCAT('0', ISNULL(datepart(MM,[dateTime]),'')), 2)                                                    " +
                            " ORDER BY  datepart(yyyy,[dateTime]),RIGHT(CONCAT('0', ISNULL(datepart(MM,[dateTime]),'')), 2)                                                    ";

            sqlQuery[sqlChoice.connectCall.ordinal()] =
                    " SELECT datepart(yyyy,CREATETIME) AS YEAR,                  " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) AS MONTH,                         " +
                            "   count(*)  as cnt" +
                            " FROM                                                        " +
                            "   CALLLOG_FORM  with(nolock)                                         " +
                            " WHERE                                                       " +
                            "   CREATETIME >= ?          " +
                            " AND                                                         " +
                            "   CREATETIME <= ? " + acceptTypeSql +

//                            " AND                                                           " +
//                            "   caseType is not null                                        " +
//                            " AND                                                           " +
//                            "   trim(caseType) <>'V'                                         " +

                            //" And callType in ('I','J') or (M7480CALLID = NULL AND SERVICETYPE=NULL AND SERVICEITEM=NULL AND PROCESSTYPE=NULL AND CALLTYPE=NULL AND CASETYPE=NULL)" +
                            " And callType in ('I','J')" +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2)";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                       " +
                            "   PROCESSTYPE, COUNT(*) AS CNT,                              " +
                            "    datepart(yyyy,CREATETIME) AS YEAR,                        " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) AS MONTH                          " +
                            " FROM                                                         " +
                            "   CALLLOG_FORM    with(nolock)                                           " +
                            " WHERE                                                        " +
                            "   CREATETIME>=?            " +
                            " AND                                                          " +
                            "   CREATETIME<=?            " + acceptTypeSql +
                            " AND                                                          " +
                            "   PROCESSTYPE IS NOT NULL                                    " +
                            //" and ringTime is not null  " +
                            " and serviceType is not null  " +
                            //  " and serviceItem is not null and serviceItem <> '' " +
                            //" and caseType is not null and caseType <> 'V'" +
                            " and callType is not null  " +
                            " And callType not in ('I','J') " +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) " +
                            " ORDER BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME),RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2)  ";

            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                            " +
                            "   DATEPART(YYYY, CallLog1.[datetime])               AS YEAR,                             " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CallLog1.[datetime]),'')), 2) AS MONTH,                               " +

                            "   SUM(AVG_WAIT_TIME) AVG_WAIT_TIME,       " + //平均等候
                            "   SUM(AVG_TALK_TIME) AVG_TALK_TIME,        " + //平均通話長度
                            "   MAX(AVG_ABANDONED) MAX_ABANDONED ,       " + //平均放棄數
                            "   TYPE " +
                            " FROM                                                              " +
                            "   CallLog1  inner join AvgWaitingAndTalking  on CallLog1.datetime =AvgWaitingAndTalking.datetime             " +
                            " WHERE                                                             " +
                            "   CallLog1.[datetime] >= ?                                  " +
                            " AND                                                               " +
                            "   CallLog1.[datetime] <= ?                                  " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CallLog1.[datetime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,[datetime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : "")) +

                            "   group by DATEPART(YYYY, CallLog1.[datetime]) ,TYPE             , RIGHT(CONCAT('0', ISNULL(datepart(MM,CallLog1.[datetime]),'')), 2)             " +
                            " ORDER BY                                                          " +
                            "   DATEPART(YYYY, CallLog1.[datetime])              , RIGHT(CONCAT('0', ISNULL(datepart(MM,CallLog1.[datetime]),'')), 2)             ";

            sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT LEFT(DataDate,4) AS 'YEAR', SUBSTRING(DATADATE,6,2) AS 'MONTH',   " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    " select " +
                    " cast([datetime] as date) AS SDAY, " +
                    " format([datetime],'yyyy年MM月') AS  DataDate, " +
                    " format([datetime],'HH') AS HOUR, " +
                    //" abandoned XCNT, answered AS CCNT " +
                    kpiCntSql +
                    " FROM CallLog1 " +
                    " WHERE [datetime] >= ? " +
                    " AND [datetime] <= ?" +

                    ("H".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                    " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : ("W".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                    " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : "")) +

                    " GROUP BY cast([datetime] as date),format([datetime],'yyyy年MM月'),format([datetime],'HH') ,answered ,abandoned,chattingCalls,voiceCalls" +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B " +
                    " GROUP BY  LEFT(DataDate,4) , SUBSTRING(DATADATE,6,2) " +
                    " ORDER BY  LEFT(DataDate,4) , SUBSTRING(DATADATE,6,2) ";

        } else if ("W".equals(timeType)) // 邊撈邊塞日期索引
        {

            String ASQL = "  (select  concat(convert(char(10),?,126),'-',DATEPART(iso_week, ?)) as EVERYDATE,       " +
                    "           datepart(yyyy,?) AS YEAR,                    " +
                    "           datepart(mm,?) AS MONTH,                     " +
                    "           (datepart(DD,?) /7 ) + 1 AS WEEK_OF_MONTH,              " +
                    "           DATEPART(iso_week, ?) AS WEEK_OF_YEAR,              " +
                    "           datepart(dd,?) AS DAY                        ";

            StringBuilder A2SQL = new StringBuilder("  ");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate _startDate = LocalDate.parse(startDate, df);

            for (int k = 0; k < cntDay; k++) {
                LocalDate localDateTime = _startDate.plusDays(k);
                A2SQL.append(" union");

                A2SQL.append("  select  concat(convert(char(10),'" + localDateTime.format(df) + "',126),'-',DATEPART(iso_week, '" + localDateTime.format(df) + "')) as EVERYDATE,       ");
                A2SQL.append("           datepart(yyyy,'" + localDateTime.format(df) + "') AS YEAR,                    ");
                A2SQL.append("           datepart(mm,'" + localDateTime.format(df) + "') AS MONTH,                     ");
                A2SQL.append("           (datepart(DD,'" + localDateTime.format(df) + "') /7 ) + 1 AS WEEK_OF_MONTH,              ");
                A2SQL.append("           DATEPART(iso_week, '" + localDateTime.format(df) + "') AS WEEK_OF_YEAR,              ");
                A2SQL.append("           datepart(dd,'" + localDateTime.format(df) + "') AS DAY                        ");


            }

            sqlQuery[sqlChoice.connectType.ordinal()] =
                    " SELECT                                                        " +
                            "   A1.VOICECALLS,                                             " +
                            "   A1.CHATTINGCALLS,                                             " +
                            "   A1.IVR,                                             " +
                            "   A1.ANSWERED,                                                     " +
                            "   A2.EVERYDATE,                                               " +
                            "   A2.YEAR,                                                    " +
                            "   A2.MONTH,                                                   " +
                            "   A2.WEEK_OF_MONTH,                                           " +
                            "   A2.WEEK_OF_YEAR,                                            " +
                            "   A2.DAY                                                      " +
                            " FROM                                                          " +
                            "  ( " +

                            " SELECT    " +
                            "  concat(convert(char(10),[dateTime],126),'-',DATEPART(iso_week, [dateTime])) AS EVERYDATE,                  " +
                            " sum(voiceCalls) voiceCalls , sum(chattingCalls) chattingCalls ,sum(ivr) ivr ,sum(answered) answered" +
                            " FROM                                                          " +
                            "   CallLog1                                                " +
                            " WHERE                                                         " +
                            "   [dateTime]>=?            " +
                            " AND                                                           " +
                            "   [dateTime]<=?                     " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : "")) +
                            " GROUP BY  concat(convert(char(10),[dateTime],126),'-',DATEPART(iso_week, [dateTime]))   " +

                            "   )A1 right join   " +

                            ASQL +

                            A2SQL +

                            "       )A2                               " +

                            " on                                                         " +
                            "   A2.EVERYDATE  = A1.EVERYDATE                              " +
                            " ORDER BY                                                      " +
                            "   A2.EVERYDATE                                 ";

            sqlQuery[sqlChoice.connectCall.ordinal()] =
                    " SELECT                                                        " +
                            "   A1.CNT,                                   " +
                            "   A2.EVERYDATE,                                               " +
                            "   A2.YEAR,                                                    " +
                            "   A2.MONTH,                                                   " +
                            "   A2.WEEK_OF_MONTH,                                           " +
                            "   A2.WEEK_OF_YEAR,                                            " +
                            "   A2.DAY                                                      " +
                            " FROM                                                          " +
                            "  ( SELECT                                                     " +
                            "       concat(convert(char(10),createTime,126),'-',DATEPART(iso_week, createTime)) AS EVERYDATE,        " +
                            "   count(*) as cnt" +

                            "    FROM                                                       " +
                            "       calllog_form   with(nolock)                                      " +
                            "    WHERE                                                      " +
                            "       createTime>=?          " +
                            "    AND                                                        " +
                            "       createTime<=?          " + acceptTypeSql +

//                            " AND                                                           " +
//                            "   caseType is not null                                        " +
//                            " AND                                                           " +
//                            "   trim(caseType) <>'V'                                         " +

                            //" And callType in ('I','J') or (M7480CALLID = NULL AND SERVICETYPE=NULL AND SERVICEITEM=NULL AND PROCESSTYPE=NULL AND CALLTYPE=NULL AND CASETYPE=NULL)" +
                            " And callType in ('I','J')" +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,createTime) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(calllog_form.createTime as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(calllog_form.createTime as date))) "
                                    : "")) +

                            "    GROUP BY                                                   " +
                            "       concat(convert(char(10),createTime,126),'-',DATEPART(iso_week, createTime))  ) " +
                            "  A1 right join                 " +

                            ASQL +

                            A2SQL +
                            "       )A2                               " +

                            " on                                                         " +
                            "   A2.EVERYDATE  = A1.EVERYDATE                              " +
                            " ORDER BY                                                      " +
                            "   A2.EVERYDATE                                                ";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                        " +
                            "   A1.PROCESSTYPE,                                             " +
                            "   A1.CNT,                                                     " +
                            "   A2.EVERYDATE,                                               " +
                            "   A2.YEAR,                                                    " +
                            "   A2.MONTH,                                                   " +
                            "   A2.WEEK_OF_MONTH,                                           " +
                            "   A2.WEEK_OF_YEAR,                                            " +
                            "   A2.DAY                                                      " +
                            " FROM                                                          " +
                            "  ( SELECT                                                     " +
                            "       PROCESSTYPE,                                            " +
                            "       concat(convert(char(10),CREATETIME,126),'-',DATEPART(iso_week, CREATETIME)) AS EVERYDATE,       " +
                            "       COUNT(*) AS CNT                                         " +
                            "    FROM                                                       " +
                            "       CALLLOG_FORM with(nolock)                                           " +
                            "    WHERE                                                      " +
                            "       CREATETIME>=?         " +
                            "    AND                                                        " +
                            "       CREATETIME<=?         " + acceptTypeSql +
                            //  " and ringTime is not null  " +
                            " and serviceType is not null  " +
                            //  " and serviceItem is not null and serviceItem <> '' " +
                            //" and caseType is not null and caseType <> 'V'" +
                            " and callType is not null  " +
                            " And callType not in ('I','J') " +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            "    GROUP BY                                                   " +
                            "       PROCESSTYPE, concat(convert(char(10),CREATETIME,126),'-',DATEPART(iso_week, CREATETIME))        " +

                            " )A1 right join  " +

                            ASQL +

                            A2SQL +

                            "       )A2                               " +

                            " on                                                         " +
                            "   A2.EVERYDATE  = A1.EVERYDATE                              " +
                            " ORDER BY                                                      " +
                            "   A2.EVERYDATE,A1.PROCESSTYPE                                 ";
            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                        " +
                            "   A1.TYPE,                                          " +
                            "   A1.AVG_WAIT_TIME,                               " +
                            "   A1.AVG_TALK_TIME,                            " +
                            "   A1.MAX_ABANDONED,                            " +
                            "   A2.EVERYDATE,                                               " +
                            "   A2.YEAR,                                                    " +
                            "   A2.MONTH,                                                   " +
                            "   A2.WEEK_OF_MONTH,                                           " +
                            "   A2.WEEK_OF_YEAR,                                            " +
                            "   A2.DAY                                                      " +
                            " FROM                                                          " +
                            "  ( SELECT  " +
                            " concat(convert(char(10),CallLog1.[datetime],126),'-',DATEPART(iso_week, CallLog1.[datetime])) as EVERYDATE,  " +

                            "   SUM(AVG_WAIT_TIME) AVG_WAIT_TIME,       " + //平均等候
                            "   SUM(AVG_TALK_TIME) AVG_TALK_TIME,        " + //平均通話長度
                            "   MAX(AVG_ABANDONED) MAX_ABANDONED ,       " + //平均放棄數
                            "   TYPE " +
                            " FROM                                                              " +
                            "   CallLog1  inner join AvgWaitingAndTalking  on CallLog1.datetime =AvgWaitingAndTalking.datetime             " +
                            " WHERE                                                             " +
                            "   CallLog1.[datetime] >= ?                                  " +
                            " AND                                                               " +
                            "   CallLog1.[datetime] <= ?                                  " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CallLog1.[datetime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CallLog1.[datetime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : "")) +

                            "    GROUP BY  TYPE,concat(convert(char(10),CallLog1.[datetime],126),'-',DATEPART(iso_week, CallLog1.[datetime])) " +

                            " )A1 right join" +

                            ASQL +

                            A2SQL +

                            "       )A2                               " +

                            " on                                                         " +
                            "   A2.EVERYDATE  = A1.EVERYDATE                              " +
                            " ORDER BY                                                      " +
                            "   A2.EVERYDATE                                                ";

            sqlQuery[sqlChoice.KPI.ordinal()] = "set datefirst 1; SELECT concat(FORMAT(SDAY,'yyyy年M月'),' 第', (DATEPART(week, SDAY) - DATEPART(week, DATEADD(day, 1, EOMONTH(SDAY, -1))))+  1  ,'週') as DataDate,   " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    "SELECT " +
                    " cast([datetime] as date) AS SDAY, " +
                    " format([datetime],'yyyy年MM月dd日') AS  DataDate, " +
                    " format([datetime],'HH') AS HOUR, " +
                    //" abandoned XCNT, answered AS CCNT " +
                    kpiCntSql +
                    "FROM CallLog1 " +
                    "WHERE [datetime] >= ? " +
                    "AND [datetime] <= ?" +

                    ("H".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                    " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : ("W".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                    " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : "")) +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B " +
                    " GROUP BY  concat(FORMAT(SDAY,'yyyy年M月'),' 第', (DATEPART(week, SDAY) - DATEPART(week, DATEADD(day, 1, EOMONTH(SDAY, -1))))+  1  ,'週') " +
                    " ORDER BY  concat(FORMAT(SDAY,'yyyy年M月'),' 第', (DATEPART(week, SDAY) - DATEPART(week, DATEADD(day, 1, EOMONTH(SDAY, -1))))+  1  ,'週') ";

        } else if ("D".equals(timeType)) {
            for (i = 0; i < cntDay; i++) {
                tempYear = Integer.toString(tempDate.get(Calendar.YEAR));

                tempIntMonth = tempDate.get(Calendar.MONTH) + 1;
                if (tempIntMonth < 10)
                    tempMonth = "0" + Integer.toString(tempIntMonth);
                else
                    tempMonth = Integer.toString(tempIntMonth);

                tempIntDay = tempDate.get(Calendar.DAY_OF_MONTH);
                if (tempIntDay < 10)
                    tempDay = "0" + Integer.toString(tempIntDay);
                else
                    tempDay = Integer.toString(tempIntDay);

                dateColumn = tempYear + "年" + tempMonth + "月" + tempDay + "日";
                hp.put(tempYear + tempMonth + tempDay, dateColumn);
                tempDate.add(Calendar.DATE, 1);
                report.addRowTitle(dateColumn, dateColumn);
            }

            sqlQuery[sqlChoice.connectType.ordinal()] =
                    " SELECT " +
                            "    datepart(yyyy,[dateTime]) AS YEAR,                         " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,[dateTime]),'')), 2) AS MONTH,                          " +
                            "   (datepart(DD,[dateTime]) /7 ) + 1 AS WEEK,                            " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(DD,[dateTime]),'')), 2) AS DAY,                            " +
                            " sum(voiceCalls) voiceCalls , sum(chattingCalls) chattingCalls ,sum(ivr) ivr ,sum(answered) answered" +
                            " FROM                                                          " +
                            "   CallLog1                                                " +
                            " WHERE                                                         " +
                            "   [dateTime]>=?            " +
                            " AND                                                           " +
                            "   [dateTime]<=?                     " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : "")) +

                            " GROUP BY                                                      " +
                            "   datepart(yyyy,[dateTime]), RIGHT(CONCAT('0', ISNULL(datepart(MM,[dateTime]),'')), 2) ,(datepart(DD,[dateTime]) /7 ) + 1,RIGHT(CONCAT('0', ISNULL(datepart(DD,[dateTime]),'')), 2)" +
                            " ORDER BY                                                      " +
                            "   datepart(yyyy,[dateTime]), RIGHT(CONCAT('0', ISNULL(datepart(MM,[dateTime]),'')), 2) ,(datepart(DD,[dateTime]) /7 ) + 1,RIGHT(CONCAT('0', ISNULL(datepart(DD,[dateTime]),'')), 2)";

            sqlQuery[sqlChoice.connectCall.ordinal()] =
                    "      SELECT datepart(yyyy,CREATETIME) AS YEAR,                  " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) AS MONTH,                         " +
                            "   (datepart(DD,CREATETIME) /7 ) + 1 AS WEEK,                           " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2) AS DAY,                           " +
                            "   count(*)  as cnt" +
                            " FROM                                                        " +
                            "   CALLLOG_FORM with(nolock)                                          " +
                            " WHERE                                                       " +
                            "   CREATETIME >= ?          " +
                            " AND                                                         " +
                            "   CREATETIME <= ? " + acceptTypeSql +

//                            " AND                                                           " +
//                            "   caseType is not null                                        " +
//                            " AND                                                           " +
//                            "   trim(caseType) <>'V'                                         " +

                            //" And callType in ('I','J') or (M7480CALLID = NULL AND SERVICETYPE=NULL AND SERVICEITEM=NULL AND PROCESSTYPE=NULL AND CALLTYPE=NULL AND CASETYPE=NULL)" +
                            " And callType in ('I','J')" +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2), (datepart(DD,CREATETIME) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2) ";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                       " +
                            "   PROCESSTYPE, COUNT(*) AS CNT,                              " +
                            "    datepart(yyyy,CREATETIME) AS YEAR,                        " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) AS MONTH,                         " +
                            "   (datepart(DD,CREATETIME) /7 ) + 1 AS WEEK,                           " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2) AS DAY                            " +
                            " FROM                                                         " +
                            "   CALLLOG_FORM with(nolock)                                             " +
                            " WHERE                                                        " +
                            "   CREATETIME>=?            " +
                            " AND                                                          " +
                            "   CREATETIME<=?            " + acceptTypeSql +
                            " AND                                                          " +
                            "   PROCESSTYPE IS NOT NULL                                    " +
                            // " and ringTime is not null  " +
                            " and serviceType is not null  " +
                            //  " and serviceItem is not null and serviceItem <> '' " +
                            //" and caseType is not null and caseType <> 'V'" +
                            " and callType is not null  " +
                            " And callType not in ('I','J') " +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2), (datepart(DD,CREATETIME) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2) " +
                            " ORDER BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2), (datepart(DD,CREATETIME) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2) ";

//            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
//                    " SELECT                                                            " +
//                            "   DATEPART(YYYY, [datetime])               AS YEAR,                             " +
//                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,[datetime]),'')), 2) AS MONTH,                               " +
//                            "   (datepart(DD,[datetime]) /7 ) + 1 AS WEEK,                                 " +
//                            "   RIGHT(CONCAT('0', ISNULL(datepart(DD,[datetime]),'')), 2) AS DAY,                                 " +
//
//                            "   SUM(AVG_ABANDONED) AVG_ABANDONED,       " + //平均放棄數
//                            "   SUM(AVG_WAIT_TIME) AVG_WAIT_TIME,       " + //平均等候
//                            "   SUM(AVG_TALK_TIME) AVG_TALK_TIME,        " + //平均通話長度
//                            "   MAX(AVG_ABANDONED) MAX_ABANDONED        " +
//                            " FROM                                                              " +
//                            "   CallLog1                                                 " +
//                            " WHERE                                                             " +
//                            "   [datetime] >= ?                                  " +
//                            " AND                                                               " +
//                            "   [datetime] <= ?                                  " +
//
//                            ("H".equals(dateType) ?
//                                    " AND ( ((datepart(dd,[datetime]) /7) +1) IN ('0', '6')  " +
//                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
//                                    : ("W".equals(dateType) ?
//                                    " AND ( ((datepart(dd,[datetime]) /7) +1) NOT IN ('0', '6')  " +
//                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
//                                    : "")) +
//
//                            " GROUP BY                                                          " +
//                            "   DATEPART(YYYY, [datetime])              , RIGHT(CONCAT('0', ISNULL(datepart(MM,[datetime]),'')), 2), (datepart(DD,[datetime]) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,[datetime]),'')), 2) " +
//                            " ORDER BY                                                          " +
//                            "   DATEPART(YYYY, [datetime])              , RIGHT(CONCAT('0', ISNULL(datepart(MM,[datetime]),'')), 2), (datepart(DD,[datetime]) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,[datetime]),'')), 2) ";


            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                            " +
                            "   DATEPART(YYYY, CallLog1.[datetime])               AS YEAR,                             " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CallLog1.[datetime]),'')), 2) AS MONTH,                               " +
                            "   (datepart(DD,CallLog1.[datetime]) /7 ) + 1 AS WEEK,                                 " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(DD,CallLog1.[datetime]),'')), 2) AS DAY,                                 " +

                            "   SUM(AVGWAITING) AVG_WAIT_TIME,       " + //平均等候
                            "   SUM(AVGDURATION) AVG_TALK_TIME,        " + //平均通話長度
                            "   MAX(AVG_ABANDONED) MAX_ABANDONED ," + //平均放棄數
                            "   TYPE " +
                            " FROM                                                              " +
                            "   CallLog1  inner join AvgWaitingAndTalking  on CallLog1.datetime =AvgWaitingAndTalking.datetime             " +
                            " WHERE                                                             " +
                            "   CallLog1.[datetime] >= ?                                  " +
                            " AND                                                               " +
                            "   CallLog1.[datetime] <= ?                                  " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CallLog1.[datetime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CallLog1.[datetime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : "")) +

                            " GROUP BY                                                          " +
                            "   DATEPART(YYYY, CallLog1.[datetime]),   type           , RIGHT(CONCAT('0', ISNULL(datepart(MM,CallLog1.[datetime]),'')), 2), (datepart(DD,CallLog1.[datetime]) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,CallLog1.[datetime]),'')), 2) " +
                            " ORDER BY                                                          " +
                            "   DATEPART(YYYY, CallLog1.[datetime])           , RIGHT(CONCAT('0', ISNULL(datepart(MM,CallLog1.[datetime]),'')), 2), (datepart(DD,CallLog1.[datetime]) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,CallLog1.[datetime]),'')), 2) ";


                    sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT LEFT(DataDate,4) AS 'YEAR', SUBSTRING(DATADATE,6,2) AS 'MONTH', DATEPART(WEEK,LEFT(DataDate,4)+'-'+SUBSTRING(DATADATE,6,2)+'-'+SUBSTRING(DATADATE,9,2)) AS 'WEEK', SUBSTRING(DATADATE,9,2) AS 'day',   " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    " select" +
                    " cast([datetime] as date) AS SDAY, " +
                    " format([datetime],'yyyy年MM月dd日') AS  DataDate, " +
                    " format([datetime],'HH') AS HOUR, " +
                    //" abandoned XCNT, answered AS CCNT " +
                    kpiCntSql +
                    " FROM CallLog1 " +
                    " WHERE [datetime] >= ? " +
                    " AND [datetime] <= ?" +

                    ("H".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                    " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : ("W".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                    " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : "")) +

                    " GROUP BY cast([datetime] as date),format([datetime],'yyyy年MM月dd日'),format([datetime],'HH') ,answered ,abandoned,chattingCalls,voiceCalls" +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B " +
                    " GROUP BY  LEFT(DataDate,4) , SUBSTRING(DATADATE,6,2) , SUBSTRING(DATADATE,9,2) " +
                    " ORDER BY  LEFT(DataDate,4) , SUBSTRING(DATADATE,6,2) , SUBSTRING(DATADATE,9,2) ";

        } else if ("H".equals(timeType)) {
            String _hour;
            for (i = 0; i <= 23; i++) {
                _hour = format.format(i) + "~" + format.format(i + 1);
                report.addRowTitle(_hour, _hour);
            }

            sqlQuery[sqlChoice.connectType.ordinal()] =
                    " SELECT " +
                            "    concat(FORMAT(DATEPART(HOUR,[dateTime]),'00'),'~',FORMAT(DATEPART(HOUR,[dateTime])+1,'00')) as HOUR,                                " +

                            " sum(voiceCalls) voiceCalls , sum(chattingCalls) chattingCalls ,sum(ivr) ivr ,sum(answered) answered" +
                            " FROM                                                          " +
                            "   CallLog1                                                " +
                            " WHERE                                                         " +
                            "   [dateTime]>=?            " +
                            " AND                                                           " +
                            "   [dateTime]<=?                     " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : "")) +

                            " GROUP BY                                                             " +
                            "    concat(FORMAT(DATEPART(HOUR,[dateTime]),'00'),'~',FORMAT(DATEPART(HOUR,[dateTime])+1,'00')) " +
                            " ORDER BY                                                             " +
                            "   concat(FORMAT(DATEPART(HOUR,[dateTime]),'00'),'~',FORMAT(DATEPART(HOUR,[dateTime])+1,'00'))  ";

            sqlQuery[sqlChoice.connectCall.ordinal()] =
                    "      SELECT concat(FORMAT(DATEPART(HOUR,CREATETIME),'00'),'~',FORMAT(DATEPART(HOUR,CREATETIME)+1,'00')) as HOUR   ,               " +
                            "   count(*)  as cnt" +
                            " FROM                                                        " +
                            "   CALLLOG_FORM  with(nolock)                                         " +
                            " WHERE                                                       " +
                            "   CREATETIME >= ?          " +
                            " AND                                                         " +
                            "   CREATETIME <= ? " + acceptTypeSql +

//                            " AND                                                           " +
//                            "   caseType is not null                                        " +
//                            " AND                                                           " +
//                            "   trim(caseType) <>'V'                                         " +

                            //" And callType in ('I','J') or (M7480CALLID = NULL AND SERVICETYPE=NULL AND SERVICEITEM=NULL AND PROCESSTYPE=NULL AND CALLTYPE=NULL AND CASETYPE=NULL)" +
                            " And callType in ('I','J')" +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY concat(FORMAT(DATEPART(HOUR,CREATETIME),'00'),'~',FORMAT(DATEPART(HOUR,CREATETIME)+1,'00')) ";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                       " +
                            "   PROCESSTYPE, COUNT(*) AS CNT,                              " +
                            "    concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00')) as HOUR                        " +

                            " FROM                                                         " +
                            "   CALLLOG_FORM  with(nolock)                                              " +
                            " WHERE                                                        " +
                            "   CREATETIME>=?            " +
                            " AND                                                          " +
                            "   CREATETIME<=?            " + acceptTypeSql +
                            " AND                                                          " +
                            "   PROCESSTYPE IS NOT NULL                                    " +
                            // " and ringTime is not null  " +
                            " and serviceType is not null  " +
                            //  " and serviceItem is not null and serviceItem <> '' " +
                            //" and caseType is not null and caseType <> 'V'" +
                            " and callType is not null  " +
                            " And callType not in ('I','J') " +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                     " +
                            "   PROCESSTYPE,  concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00')) " +
                            " ORDER BY                                                     " +
                            "   PROCESSTYPE,  concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00'))  ";

            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                            " +
                            "    concat(FORMAT(DATEPART(HOUR,CallLog1.[datetime]),'00'),'~',FORMAT(DATEPART(HOUR,CallLog1.[datetime])+1,'00'))  AS HOUR,  " +


                            "   SUM(AVG_WAIT_TIME) AVG_WAIT_TIME,       " + //平均等候
                            "   SUM(AVG_TALK_TIME) AVG_TALK_TIME,        " + //平均通話長度
                            "   MAX(AVG_ABANDONED) MAX_ABANDONED ,       " + //平均放棄數
                            "   TYPE " +
                            " FROM                                                              " +
                            "   CallLog1  inner join AvgWaitingAndTalking  on CallLog1.datetime =AvgWaitingAndTalking.datetime             " +
                            " WHERE                                                             " +
                            "   CallLog1.[datetime] >= ?                                  " +
                            " AND                                                               " +
                            "   CallLog1.[datetime] <= ?                                  " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CallLog1.[datetime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.CallLog1.[datetime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CallLog1.[datetime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : "")) +

                            "  GROUP BY  TYPE,concat(FORMAT(DATEPART(HOUR,CallLog1.[datetime]),'00'),'~',FORMAT(DATEPART(HOUR,CallLog1.[datetime])+1,'00'))      " +
                            " ORDER BY                                                          " +
                            "  concat(FORMAT(DATEPART(HOUR,CallLog1.[datetime]),'00'),'~',FORMAT(DATEPART(HOUR,CallLog1.[datetime])+1,'00'))            ";

            sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT hour,   " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    "SELECT " +

                    " cast([datetime] as date) AS SDAY, " +
                    " format([datetime],'yyyy年MM月') AS  DataDate, " +
                    " format([datetime],'HH') AS HOUR, " +
                    //" abandoned XCNT, answered AS CCNT " +
                    kpiCntSql +
                    " FROM CallLog1 " +
                    " WHERE [datetime] >= ? " +
                    " AND [datetime] <= ?" +

                    ("H".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                    " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : ("W".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                    " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : "")) +

                    " GROUP BY cast([datetime] as date),format([datetime],'yyyy年MM月'),format([datetime],'HH') ,answered ,abandoned,chattingCalls,voiceCalls" +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B " +
                    " GROUP BY  hour " +
                    " ORDER BY  hour ";


        } else if ("A".equals(timeType)) {
            report.addRowTitle("總數", "總數");

            sqlQuery[sqlChoice.connectType.ordinal()] =
                    " SELECT   '總數' as '總數' ,                                          " +
                            " sum(voiceCalls) voiceCalls , sum(chattingCalls) chattingCalls ,sum(ivr) ivr ,sum(answered) answered" +
                            " FROM                                                          " +
                            "   CallLog1                                                " +
                            " WHERE                                                         " +
                            "   [dateTime]>=?            " +
                            " AND                                                           " +
                            "   [dateTime]<=?                     " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                                    : ""));

            sqlQuery[sqlChoice.connectCall.ordinal()] =
                    "      SELECT '總數' as '總數',  " +
                            "   count(*)  as cnt" +
                            " FROM                                                        " +
                            "   CALLLOG_FORM  with(nolock)                                         " +
                            " WHERE                                                       " +
                            "   CREATETIME >= ?          " +
                            " AND                                                         " +
                            "   CREATETIME <= ? " + acceptTypeSql +

//                            " AND                                                           " +
//                            "   caseType is not null                                        " +
//                            " AND                                                           " +
//                            "   trim(caseType) <>'V'                                         " +

                            //" And callType in ('I','J') or (M7480CALLID = NULL AND SERVICETYPE=NULL AND SERVICEITEM=NULL AND PROCESSTYPE=NULL AND CALLTYPE=NULL AND CASETYPE=NULL)" +
                            " And callType in ('I','J')" +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ""));

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                       " +
                            "   PROCESSTYPE,   COUNT(*) AS CNT ,'總數' as '總數'" +
                            " FROM                                                         " +
                            "   CALLLOG_FORM   with(nolock)                                            " +
                            " WHERE                                                        " +
                            "   CREATETIME>=?            " +
                            " AND                                                          " +
                            "   CREATETIME<=?            " + acceptTypeSql +
                            " AND                                                          " +
                            "   PROCESSTYPE IS NOT NULL                                    " +
                            //   " and ringTime is not null  " +
                            " and serviceType is not null  " +
                            //  " and serviceItem is not null and serviceItem <> '' " +
                            //" and caseType is not null and caseType <> 'V'" +
                            " and callType is not null  " +
                            " And callType not in ('I','J') " +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                     " +
                            "   PROCESSTYPE" +
                            " ORDER BY                                                     " +
                            "   PROCESSTYPE";

            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                            " +
                            "      COUNT(*) AS CNT      ,  '總數' as '總數'," +

                            "   SUM(AVG_WAIT_TIME) AVG_WAIT_TIME,       " + //平均等候
                            "   SUM(AVG_TALK_TIME) AVG_TALK_TIME,        " + //平均通話長度
                            "   MAX(AVG_ABANDONED) MAX_ABANDONED ,       " + //平均放棄數
                            "  TYPE " +
                            " FROM                                                              " +
                            "   CallLog1  inner join AvgWaitingAndTalking  on CallLog1.datetime =AvgWaitingAndTalking.datetime             " +
                            " WHERE                                                             " +
                            "   CallLog1.[datetime] >= ?                                  " +
                            " AND                                                               " +
                            "   CallLog1.[datetime] <= ?                                  " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CallLog1[datetime]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CallLog1[datetime]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[datetime] as date))) "
                                    : "")) +
                            " GROUP BY  TYPE ";



            sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    " select " +
                    " cast([datetime] as date) AS SDAY, " +
                    " format([datetime],'yyyy年MM月') AS  DataDate, " +
                    " format([datetime],'HH') AS HOUR, " +
                    //" abandoned XCNT, answered AS CCNT " +
                    kpiCntSql +
                    " FROM CallLog1 " +
                    " WHERE [datetime] >= ? " +
                    " AND [datetime] <= ?" +

                    ("H".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                    " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : ("W".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                    " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : "")) +

                    " GROUP BY cast([datetime] as date),format([datetime],'yyyy年MM月'),format([datetime],'HH') ,answered ,abandoned,chattingCalls,voiceCalls" +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B ";


        }

        try {
            // 各時段通報數

            conn = ds.getConnection();
            pstmt = conn.prepareStatement(sqlQuery[sqlChoice.connectType.ordinal()]);
            logger.info(" connectType sql = " + sqlQuery[sqlChoice.connectType.ordinal()]);
            int j = 0;
            pstmt.setString(++j, startDate + " 00:00:00");
            pstmt.setString(++j, endDate + " 23:59:59");

            if ("W".equals(timeType)) {
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
            }
            rs = pstmt.executeQuery();

            ApplicationCode app = ApplicationCode.getInstance();
            beans.put("app", app);
            Vector<String> colName = app.getCodeCodeList("G01");
            String weekStart = "";
            String weekEnd = "";
            String dayKey = ""; // 日期索引
            String whichType = ""; // 紀錄"G01"類別
            String appCode = ""; // appCode分類
            String tmpMonth = "";
            String tmpWeekOfYear = "";
            String tmpYear = "";
            String caseType = "";
            String casyTypeCode = "CASETYPE_";
            int voiceCalls = 0, chattingCalls = 0, ivr = 0, answered = 0;

            String voiceCallsKey = "VOICECALLS";
            String chattingCallsKey = "CHATTINGCALLS";
            String ivrKey = "IVRCALLS";
            String answeredKey = "ANSWERED";

            int count = 0;
            int rightWeek = 0;
            boolean checkOne = true; // 判斷開始日期週次的例外情況
            while (rs.next()) {
                voiceCalls = rs.getInt("VOICECALLS");
                chattingCalls = rs.getInt("CHATTINGCALLS");
                ivr = rs.getInt("ivr");
                answered = rs.getInt("ANSWERED");

                if ("L".equals(acceptType)) {
                    voiceCalls = 0;
                    ivr = 0;
                } else if ("I".equals(acceptType)) {
                    voiceCalls = 0;
                    chattingCalls = 0;
                } else if ("T".equals(acceptType)) {
                    chattingCalls = 0;
                    ivr = 0;
                } else if ("TL".equals(acceptType)) {
                    ivr = 0;
                }

                if ("Y".equals(timeType)) {
                    report.addInt((String) hp.get(rs.getString("YEAR")), voiceCallsKey, voiceCalls);
                    report.addInt((String) hp.get(rs.getString("YEAR")), chattingCallsKey, chattingCalls);
                    report.addInt((String) hp.get(rs.getString("YEAR")), ivrKey, ivr);
                    report.addInt((String) hp.get(rs.getString("YEAR")), answeredKey, answered);

                } else if ("M".equals(timeType)) {

                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), voiceCallsKey, voiceCalls);
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), chattingCallsKey, chattingCalls);
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), ivrKey, ivr);
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), answeredKey, answered);


                } else if ("W".equals(timeType)) {
                    String weekOfYear = rs.getString("WEEK_OF_YEAR");
                    String year = rs.getString("YEAR");
                    if (tmpWeekOfYear != null && !tmpWeekOfYear.equals(weekOfYear)) // 以WEEK_OF_YEAR為基準當KEY，避免跨年週次問題
                    {
                        tmpYear = year; // (避免跨年問題)
                        dayKey = year + weekOfYear; // 暫存日期索引(避免跨年問題)
                        tmpWeekOfYear = weekOfYear;
                    } else {
                        dayKey = tmpYear + weekOfYear; // 暫存日期索引 (避免跨年問題)
                    }

                    if (hp.get(dayKey) == null) // 新創日期 hp(dayKey,dateColumn)
                    {
                        weekStart = rs.getString("DAY");
                        weekEnd = rs.getString("DAY");
                        hp3 = new HashMap(); // 一個dayKey對應一個hp3，代表一個日期對應多個"G01"類別(key)及次數加總(object)

                        if (checkOne) // 開始日期的週次參照WEEK_OF_MONTH
                        {
                            rightWeek = rs.getInt("WEEK_OF_MONTH");
                            dateColumn =
                                    rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                            weekStart + "~" + weekEnd + "日)";

                            checkOne = false;
                        } else // 非開始日期
                        {
                            if (tmpMonth.equals(rs.getString("MONTH"))) // 同月時
                            {
                                rightWeek++;
                                dateColumn =
                                        rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                weekStart + "~" + weekEnd + "日)";
                            } else // 不同月時
                            {
                                if ("01".equals(rs.getString("DAY"))) // 剛好為當月1號時，第一周開始算
                                {
                                    rightWeek = 1;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                } else // 否則第二周開始算
                                {
                                    rightWeek = 2;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                }
                            }
                        }
                        tmpMonth = rs.getString("MONTH");
                        //hp.put(dayKey, dateColumn);
                    } else // 有該物件時，更正結束日期
                    {
                        weekEnd = rs.getString("DAY");
                        dateColumn =
                                tmpYear + "年" + tmpMonth + "月 第" + rightWeek + "週 (" + weekStart + "~" + weekEnd + "日)";
                    }
                    hp.put(dayKey, dateColumn); // 目的為了過濾獲得正確的日期區間
                    hp2.put(dayKey, hp3); // 目的為了獲取每個日期的"G01"類別的次數總和

                    if (hp3.get(voiceCallsKey) == null) {
                        hp3.put(voiceCallsKey, voiceCalls);
                    } else // 兩次以上存取此"G01"類別，將次數加總
                    {
                        count = (Integer) hp3.get(voiceCallsKey);
                        hp3.put(voiceCallsKey, count + voiceCalls);
                    }

                    if (hp3.get(chattingCallsKey) == null) {
                        hp3.put(chattingCallsKey, chattingCalls);
                    } else // 兩次以上存取此"G01"類別，將次數加總
                    {
                        count = (Integer) hp3.get(chattingCallsKey);
                        hp3.put(chattingCallsKey, count + chattingCalls);
                    }

                    if (hp3.get(ivrKey) == null) {
                        hp3.put(ivrKey, ivr);
                    } else // 兩次以上存取此"G01"類別，將次數加總
                    {
                        count = (Integer) hp3.get(ivrKey);
                        hp3.put(ivrKey, count + ivr);
                    }

                    if (hp3.get(answeredKey) == null) {
                        hp3.put(answeredKey, answered);
                    } else // 兩次以上存取此"G01"類別，將次數加總
                    {
                        count = (Integer) hp3.get(answeredKey);
                        hp3.put(answeredKey, count + answered);
                    }

                    // }
                    if (!al.contains(dayKey)) // 暫存日期索引
                    {
                        al.add(dayKey);
                    }
                } else if ("D".equals(timeType)) {

                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")), voiceCallsKey, voiceCalls);
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")), chattingCallsKey, chattingCalls);
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")), ivrKey, ivr);
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")), answeredKey, answered);


                } else if ("H".equals(timeType)) {

                    report.addInt(rs.getString("HOUR"), voiceCallsKey, voiceCalls);
                    report.addInt(rs.getString("HOUR"), chattingCallsKey, chattingCalls);
                    report.addInt(rs.getString("HOUR"), ivrKey, ivr);
                    report.addInt(rs.getString("HOUR"), answeredKey, answered);

                } else if ("A".equals(timeType)) {

                    report.addInt(rs.getString("總數"), voiceCallsKey, voiceCalls);
                    report.addInt(rs.getString("總數"), chattingCallsKey, chattingCalls);
                    report.addInt(rs.getString("總數"), ivrKey, ivr);
                    report.addInt(rs.getString("總數"), answeredKey, answered);

                }

            }

            // add by aaron 塞週類別資料到report
            HashMap tmpHp = new HashMap();
            Vector<String> caseTypeColName = app.getCodeCodeList("G02");
            caseTypeColName.add("V");
            if ("W".equals(timeType)) {
                for (String key : al) {
                    report.addRowTitle((String) hp.get(key), (String) hp.get(key)); // 塞日期索引
                    tmpHp = (HashMap) hp2.get(key);

                    if (tmpHp.containsKey(voiceCallsKey)) {
                        report.addInt((String) hp.get(key), voiceCallsKey, (Integer) tmpHp.get(voiceCallsKey));
                    }

                    if (tmpHp.containsKey(chattingCallsKey)) {
                        report.addInt((String) hp.get(key), chattingCallsKey, (Integer) tmpHp.get(chattingCallsKey));
                    }

                    if (tmpHp.containsKey(ivrKey)) {
                        report.addInt((String) hp.get(key), ivrKey, (Integer) tmpHp.get(ivrKey));
                    }

                    if (tmpHp.containsKey(answeredKey)) {
                        report.addInt((String) hp.get(key), answeredKey, (Integer) tmpHp.get(answeredKey));
                    }

                }
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            // 話務分時等候數
            tmpWeekOfYear = "";
            tmpMonth = "";
            tmpYear = "";
            dayKey = ""; // 日期索引
            weekStart = "";
            weekEnd = "";
            dateColumn = "";
            rightWeek = 0;
            count = 0;
            checkOne = true; // 判斷開始日期週次的例外情況
            hp2 = new HashMap(); // 週分類時，key為日期索引，object為hp3
            hp3 = new HashMap(); // 週分類時，key為"G01"類別，object為次數加總

            pstmt = conn.prepareStatement(sqlQuery[sqlChoice.connectCall.ordinal()]);
            logger.info(" connectCall sql =" + sqlQuery[sqlChoice.connectCall.ordinal()]);
            j = 0;
            pstmt.setString(++j, startDate + " 00:00:00");
            pstmt.setString(++j, endDate + " 23:59:59");

            if ("L".equals(acceptType))
                pstmt.setString(++j, acceptType);
            else if ("I".equals(acceptType)) {
                pstmt.setString(++j, "V");
            } else if ("T".equals(acceptType)) {
                pstmt.setString(++j, "V");
                pstmt.setString(++j, "L");
            } else if ("TL".equals(acceptType)) {
                pstmt.setString(++j, "V");
            }

            if ("W".equals(timeType)) {
                hp = new HashMap();
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                whichType = "inValid";
                if ("Y".equals(timeType)) {
                   // if (!"L".equals(acceptType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR")), whichType, rs.getInt("cnt"));
                    //}
                } else if ("M".equals(timeType)) {
                    //if (!"L".equals(acceptType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), whichType, rs.getInt("cnt"));
                    //}
                } else if ("W".equals(timeType)) {
                    String weekOfYear = rs.getString("WEEK_OF_YEAR");
                    String year = rs.getString("YEAR");
                    if (tmpWeekOfYear != null && !tmpWeekOfYear.equals(weekOfYear)) // 以WEEK_OF_YEAR為基準當KEY，避免跨年週次問題
                    {
                        tmpYear = year; // (避免跨年問題)
                        dayKey = year + weekOfYear; // 暫存日期索引(避免跨年問題)
                        tmpWeekOfYear = weekOfYear;
                    } else {
                        dayKey = tmpYear + weekOfYear; // 暫存日期索引 (避免跨年問題)
                    }

                    if (hp.get(dayKey) == null) // 新創日期 hp(dayKey,dateColumn)
                    {
                        weekStart = rs.getString("DAY");
                        weekEnd = rs.getString("DAY");
                        hp3 = new HashMap(); // 一個dayKey對應一個hp3，代表一個日期對應多個"G01"類別(key)及次數加總(object)

                        if (checkOne) // 開始日期的週次參照WEEK_OF_MONTH
                        {
                            rightWeek = rs.getInt("WEEK_OF_MONTH");
                            dateColumn =
                                    rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                            weekStart + "~" + weekEnd + "日)";

                            checkOne = false;
                        } else // 非開始日期
                        {
                            if (tmpMonth.equals(rs.getString("MONTH"))) // 同月時
                            {
                                rightWeek++;
                                dateColumn =
                                        rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                weekStart + "~" + weekEnd + "日)";
                            } else // 不同月時
                            {
                                if ("01".equals(rs.getString("DAY"))) // 剛好為當月1號時，第一周開始算
                                {
                                    rightWeek = 1;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                } else // 否則第二周開始算
                                {
                                    rightWeek = 2;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                }
                            }
                        }
                        tmpMonth = rs.getString("MONTH");
                        //hp.put(dayKey, dateColumn);
                    } else // 有該物件時，更正結束日期
                    {
                        weekEnd = rs.getString("DAY");
                        dateColumn =
                                tmpYear + "年" + tmpMonth + "月 第" + rightWeek + "週 (" + weekStart + "~" + weekEnd + "日)";
                    }
                    hp.put(dayKey, dateColumn); // 目的為了過濾獲得正確的日期區間
                    hp2.put(dayKey, hp3); // 目的為了獲取每個日期的"G01"類別的次數總和
                    if (hp3.get(whichType) == null) // 第一次存取此"G01"類別
                    {
                        //if (!"L".equals(acceptType)) {
                            hp3.put(whichType, rs.getInt("cnt"));
                        //}
                    } else // 兩次以上存取此"G01"類別，將次數加總
                    {
                        count = (Integer) hp3.get(whichType);
                        //if (!"L".equals(acceptType)) {
                            hp3.put(whichType, count + rs.getInt("cnt"));
                        //}
                    }
                    if (!al.contains(dayKey)) // 暫存日期索引
                    {
                        al.add(dayKey);
                    }
                } else if ("D".equals(timeType)) {
                    //if (!"L".equals(acceptType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") +
                                        rs.getString("DAY")), whichType,
                                rs.getInt("cnt"));
                    //}
                } else if ("H".equals(timeType)) {
                    //if (!"L".equals(acceptType)) {
                        report.addInt(rs.getString("HOUR"), whichType,
                                rs.getInt("cnt"));
                    //}
                } else if ("A".equals(timeType)) {
                    //if (!"L".equals(acceptType)) {
                        report.addInt(rs.getString("總數"), whichType,
                                rs.getInt("cnt"));
                    //}
                }
            }
            // add by aaron 塞週類別資料到report
            tmpHp = new HashMap();
            if ("W".equals(timeType)) {
                for (String key : al) {
                    //report.addRowTitle( ( String )hp.get( key ), ( String )hp.get( key ) ) ;
                    tmpHp = (HashMap) hp2.get(key);
                    if (tmpHp != null && tmpHp.containsKey("inValid")) {
                       // if (!"L".equals(acceptType)) {
                            report.addInt((String) hp.get(key), "inValid", (Integer) tmpHp.get("inValid"));
                        //}
                    }
                }
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            // 處置類別總數
            tmpWeekOfYear = "";
            tmpMonth = "";
            tmpYear = "";
            dayKey = ""; // 日期索引
            weekStart = "";
            weekEnd = "";
            dateColumn = "";
            appCode = "G03";
            rightWeek = 0;
            count = 0;
            checkOne = true; // 判斷開始日期週次的例外情況
            colName = app.getCodeCodeList("G03");
            hp2 = new HashMap(); // 週分類時，key為日期索引，object為hp3
            hp3 = new HashMap(); // 週分類時，key為"G03"類別，object為次數加總

            pstmt = conn.prepareStatement(sqlQuery[sqlChoice.typeOfDisposal.ordinal()]);
            logger.info("typeOfDisposal sql=" + sqlQuery[sqlChoice.typeOfDisposal.ordinal()]);
            j = 0;
            pstmt.setString(++j, startDate + " 00:00:00");
            pstmt.setString(++j, endDate + " 23:59:59");
            if ("L".equals(acceptType))
                pstmt.setString(++j, acceptType);
            else if ("I".equals(acceptType)) {
                pstmt.setString(++j, "V");
            } else if ("T".equals(acceptType)) {
                pstmt.setString(++j, "V");
                pstmt.setString(++j, "L");
            } else if ("TL".equals(acceptType)) {
                pstmt.setString(++j, "V");
            }
            if ("W".equals(timeType)) {
                hp = new HashMap();
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                whichType = rs.getString("PROCESSTYPE");
                if ("Y".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR")), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR")), appCode + "Z", rs.getInt("CNT"));
                    }
                } else if ("M".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")),
                                appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), appCode + "Z",
                                rs.getInt("CNT"));
                    }
                } else if ("W".equals(timeType)) {
                    String weekOfYear = rs.getString("WEEK_OF_YEAR");
                    String year = rs.getString("YEAR");
                    if (tmpWeekOfYear != null && !tmpWeekOfYear.equals(weekOfYear)) // 以WEEK_OF_YEAR為基準當KEY，避免跨年週次問題
                    {
                        tmpYear = year; // (避免跨年問題)
                        dayKey = year + weekOfYear; // 暫存日期索引(避免跨年問題)
                        tmpWeekOfYear = weekOfYear;
                    } else {
                        dayKey = tmpYear + weekOfYear; // 暫存日期索引 (避免跨年問題)
                    }

                    if (hp.get(dayKey) == null) // 新創日期 hp(dayKey,dateColumn)
                    {
                        weekStart = rs.getString("DAY");
                        weekEnd = rs.getString("DAY");
                        hp3 = new HashMap(); // 一個dayKey對應一個hp3，代表一個日期對應多個"G03"類別(key)及次數加總(object)

                        if (checkOne) // 開始日期的週次參照WEEK_OF_MONTH
                        {
                            rightWeek = rs.getInt("WEEK_OF_MONTH");
                            dateColumn =
                                    rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                            weekStart + "~" + weekEnd + "日)";

                            checkOne = false;
                        } else // 非開始日期
                        {
                            if (tmpMonth.equals(rs.getString("MONTH"))) // 同月時
                            {
                                rightWeek++;
                                dateColumn =
                                        rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                weekStart + "~" + weekEnd + "日)";
                            } else // 不同月時
                            {
                                if ("01".equals(rs.getString("DAY"))) // 剛好為當月1號時，第一周開始算
                                {
                                    rightWeek = 1;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                } else // 否則第二周開始算
                                {
                                    rightWeek = 2;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                }
                            }
                        }
                        tmpMonth = rs.getString("MONTH");
                        //hp.put(dayKey, dateColumn);
                    } else // 有該物件時，更正結束日期
                    {
                        weekEnd = rs.getString("DAY");
                        dateColumn =
                                tmpYear + "年" + tmpMonth + "月 第" + rightWeek + "週 (" + weekStart + "~" + weekEnd + "日)";
                    }
                    hp.put(dayKey, dateColumn); // 目的為了過濾獲得正確的日期區間
                    hp2.put(dayKey, hp3); // 目的為了獲取每個日期的"G03"類別的次數總和
                    if (colName.contains(whichType)) {
                        if (hp3.get(whichType) == null) // 第一次存取此"G03"類別
                        {
                            hp3.put(whichType, rs.getInt("CNT"));
                        } else // 兩次以上存取此"G03"類別，將次數加總
                        {
                            count = (Integer) hp3.get(whichType);
                            hp3.put(whichType, count + rs.getInt("CNT"));
                        }
                    } else {
                        if (hp3.get("Z") == null) // 第一次存取此"G03"類別
                        {
                            hp3.put("Z", rs.getInt("CNT"));
                        } else // 兩次以上存取此"G03"類別，將次數加總
                        {
                            count = (Integer) hp3.get("Z");
                            hp3.put("Z", count + rs.getInt("CNT"));
                        }
                    }
                    if (!al.contains(dayKey)) // 暫存日期索引
                    {
                        al.add(dayKey);
                    }
                } else if ("D".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") +
                                rs.getString("DAY")), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") +
                                rs.getString("DAY")), appCode + "Z", rs.getInt("CNT"));
                    }
                } else if ("H".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt(rs.getString("HOUR"), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt(rs.getString("HOUR"), appCode + "Z", rs.getInt("CNT"));
                    }
                } else if ("A".equals(timeType)) {

                    if (colName.contains(whichType)) {
                        report.addInt(rs.getString("總數"), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt(rs.getString("總數"), appCode + "Z", rs.getInt("CNT"));
                    }
                }
            }
            // add by aaron 塞週類別資料到report
            tmpHp = new HashMap();
            if ("W".equals(timeType)) {
                for (String key : al) {
                    //report.addRowTitle( ( String )hp.get( key ), ( String )hp.get( key ) ) ;
                    tmpHp = (HashMap) hp2.get(key);
                    for (String type : colName) {
                        if (tmpHp != null && tmpHp.containsKey(type)) {
                            report.addInt((String) hp.get(key), appCode + type, (Integer) tmpHp.get(type));
                        }
                    }
                }
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            // 話務分析，最多等候人數
            tmpWeekOfYear = "";
            tmpMonth = "";
            tmpYear = "";
            dayKey = ""; // 日期索引
            weekStart = "";
            weekEnd = "";
            dateColumn = "";
            rightWeek = 0;
            count = 0;
            int waitTime = 0;
            int communicationTime = 0;
            int callCount = 0;
            int tmpWaitTime = 0;
            int tmpCommunicationTime = 0;
            int tmpCallCount = 0;
            int reportCount = 0;
            String dateTimeColumn = null;
            String avgWaitingAndTalkingType = null;
            checkOne = true; // 判斷開始日期週次的例外情況
            hp2 = new HashMap(); // 週分類時，key為日期索引，object為hp3
            hp3 = new HashMap(); // 週分類時，key為"G01"類別，object為次數加總

            pstmt = conn.prepareStatement(sqlQuery[sqlChoice.callWaitingCount.ordinal()]);
            logger.info("callWaitingCount sql = " + sqlQuery[sqlChoice.callWaitingCount.ordinal()]);
            j = 0;
            pstmt.setString(++j, startDate + " 00:00:00");
            pstmt.setString(++j, endDate + " 23:59:59");
            if ("W".equals(timeType)) {
                hp = new HashMap();
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
            }
            rs = pstmt.executeQuery();
            int avg_abandoned = 0, avg_wait_time = 0, avg_talk_time = 0;
            while (rs.next()) {


                avg_abandoned = rs.getInt("MAX_ABANDONED");
                avg_wait_time =  rs.getInt("AVG_WAIT_TIME");
                avg_talk_time = rs.getInt("AVG_TALK_TIME");
                avgWaitingAndTalkingType = rs.getString("TYPE");

                if ("L".equals(acceptType)) {
                    if(!avgWaitingAndTalkingType.equals("textchat")) continue;
                    avg_abandoned = 0;

                } else if ("I".equals(acceptType)) {
                    avg_abandoned = 0;
                    avg_wait_time = 0;
                    avg_talk_time = 0;
                } else if ("T".equals(acceptType)) {
                    if(!avgWaitingAndTalkingType.equals("voicecall")) continue;

                } else if ("TL".equals(acceptType) || "A".equals(acceptType)) {
                    if(!avgWaitingAndTalkingType.equals("textchat"))  avg_abandoned= 0; //避免重複計算 因為 平均放棄數 此數值 只需要抓一次

                }
                if ("Y".equals(timeType)) {

                    avg_wait_time = Math.round((float) avg_wait_time / 15);
                    avg_talk_time = Math.round((float) avg_talk_time / 15);
                    report.addInt((String) hp.get(rs.getString("YEAR")), callWaitingCountType.MAXWAITCALLNBR.toString(), avg_abandoned);
                    report.addInt((String) hp.get(rs.getString("YEAR")), callWaitingCountType.AVERAGEWAIT.toString(), avg_wait_time);
                    report.addInt((String) hp.get(rs.getString("YEAR")), callWaitingCountType.AVERAGECOMMUNICATION.toString(), avg_talk_time);

                } else if ("M".equals(timeType)) {
                    dateTimeColumn = (String) hp.get(rs.getString("YEAR") + rs.getString("MONTH"));

                    avg_wait_time = Math.round((float) avg_wait_time / 15);
                    avg_talk_time = Math.round((float) avg_talk_time / 15);


                    report.addInt(dateTimeColumn, callWaitingCountType.MAXWAITCALLNBR.toString(), avg_abandoned);
                    report.addInt(dateTimeColumn, callWaitingCountType.AVERAGEWAIT.toString(), avg_wait_time);
                    report.addInt(dateTimeColumn, callWaitingCountType.AVERAGECOMMUNICATION.toString(), avg_talk_time);
                    //}
                } else if ("W".equals(timeType)) {
                    String weekOfYear = rs.getString("WEEK_OF_YEAR");
                    String year = rs.getString("YEAR");
                    if (tmpWeekOfYear != null && !tmpWeekOfYear.equals(weekOfYear)) // 以WEEK_OF_YEAR為基準當KEY，避免跨年週次問題
                    {
                        tmpYear = year; // (避免跨年問題)
                        dayKey = year + weekOfYear; // 暫存日期索引(避免跨年問題)
                        tmpWeekOfYear = weekOfYear;
                    } else {
                        dayKey = tmpYear + weekOfYear; // 暫存日期索引 (避免跨年問題)
                    }

                    if (hp.get(dayKey) == null) // 新創日期 hp(dayKey,dateColumn)
                    {
                        tmpCallCount = 0;
                        tmpWaitTime = 0;
                        tmpCommunicationTime = 0;
                        weekStart = rs.getString("DAY");
                        weekEnd = rs.getString("DAY");
                        hp3 = new HashMap(); // 一個dayKey對應一個hp3，代表一個日期對應多個"G01"類別(key)及次數加總(object)

                        if (checkOne) // 開始日期的週次參照WEEK_OF_MONTH
                        {
                            rightWeek = rs.getInt("WEEK_OF_MONTH");
                            dateColumn =
                                    rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                            weekStart + "~" + weekEnd + "日)";

                            checkOne = false;
                        } else // 非開始日期
                        {
                            if (tmpMonth.equals(rs.getString("MONTH"))) // 同月時
                            {
                                rightWeek++;
                                dateColumn =
                                        rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                weekStart + "~" + weekEnd + "日)";
                            } else // 不同月時
                            {
                                if ("01".equals(rs.getString("DAY"))) // 剛好為當月1號時，第一周開始算
                                {
                                    rightWeek = 1;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                } else // 否則第二周開始算
                                {
                                    rightWeek = 2;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                }
                            }
                        }
                        tmpMonth = rs.getString("MONTH");
                        //hp.put(dayKey, dateColumn);
                    } else // 有該物件時，更正結束日期
                    {
                        weekEnd = rs.getString("DAY");
                        dateColumn =
                                tmpYear + "年" + tmpMonth + "月 第" + rightWeek + "週 (" + weekStart + "~" + weekEnd + "日)";

                    }

                    hp.put(dayKey, dateColumn); // 目的為了過濾獲得正確的日期區間
                    hp2.put(dayKey, hp3); // 目的為了獲取每個日期的"G01"類別的次數總和
                    //avg_abandoned = rs.getInt("AVG_ABANDONED") / 15;
                    avg_abandoned = rs.getInt("MAX_ABANDONED");
                    avg_wait_time = rs.getInt("AVG_WAIT_TIME");
                    avg_talk_time = rs.getInt("AVG_TALK_TIME");

                    for (callWaitingCountType type : callWaitingCountType.values()) {
                        switch (type) {
                            case MAXWAITCALLNBR:
                                if (hp3.get(type.MAXWAITCALLNBR) == null) // 第一次存取此"G01"類別
                                {
                                    //if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                                    hp3.put(type.MAXWAITCALLNBR, avg_abandoned);
                                    //}
                                } else // 兩次以上存取此"G01"類別，將次數加總
                                {
                                    //if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                                    if (avg_abandoned > (int) hp3.get(type.MAXWAITCALLNBR))
                                        hp3.put(type.MAXWAITCALLNBR, avg_abandoned);
                                    //}
                                }
                                break;
                            case AVERAGEWAIT:

                                //if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                                if (hp3.get(type.AVERAGEWAIT) == null) {
                                    hp3.put(type.AVERAGEWAIT, avg_wait_time);
                                } else {
                                    hp3.put(type.AVERAGEWAIT, (int) hp3.get(type.AVERAGEWAIT) + avg_wait_time);

                                }

                                //}
                                break;
                            case AVERAGECOMMUNICATION:
                                tmpCommunicationTime = tmpCommunicationTime + avg_talk_time;
                                //if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                                //hp3.put(type.AVERAGECOMMUNICATION, avg_talk_time);

                                if (hp3.get(type.AVERAGECOMMUNICATION) == null) {
                                    hp3.put(type.AVERAGECOMMUNICATION, avg_talk_time);
                                } else {
                                    hp3.put(type.AVERAGECOMMUNICATION, (int) hp3.get(type.AVERAGECOMMUNICATION) + avg_talk_time);

                                }
                                //}
                                break;
                        }
                    }
                    if (!al.contains(dayKey)) // 暫存日期索引
                    {
                        al.add(dayKey);

                    }
                } else if ("D".equals(timeType)) {


                    avg_wait_time = Math.round((float) avg_wait_time / 15);
                    avg_talk_time = Math.round((float) avg_talk_time / 15);

                    //if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")), callWaitingCountType.MAXWAITCALLNBR.toString(), avg_abandoned);
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")), callWaitingCountType.AVERAGEWAIT.toString(), avg_wait_time);
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")), callWaitingCountType.AVERAGECOMMUNICATION.toString(), avg_talk_time);
                    //}
                } else if ("H".equals(timeType)) {
                    //avg_abandoned = rs.getInt("AVG_ABANDONED") / cntDay;
                    avg_abandoned = avg_abandoned / cntDay;
                    avg_wait_time = Math.round((float) avg_wait_time / cntDay);
                    avg_talk_time = Math.round((float) avg_talk_time / cntDay);


                    report.addInt(rs.getString("HOUR"), callWaitingCountType.MAXWAITCALLNBR.toString(), avg_abandoned);
                    report.addInt(rs.getString("HOUR"), callWaitingCountType.AVERAGEWAIT.toString(), avg_wait_time);
                    report.addInt(rs.getString("HOUR"), callWaitingCountType.AVERAGECOMMUNICATION.toString(), avg_talk_time);
                    //}
                } else if ("A".equals(timeType)) {


                    avg_wait_time = Math.round((float) avg_wait_time / 15);
                    avg_talk_time = Math.round((float) avg_talk_time / 15);

                    report.addInt(rs.getString("總數"), callWaitingCountType.MAXWAITCALLNBR.toString(), avg_abandoned);
                    report.addInt(rs.getString("總數"), callWaitingCountType.AVERAGEWAIT.toString(), avg_wait_time);
                    report.addInt(rs.getString("總數"), callWaitingCountType.AVERAGECOMMUNICATION.toString(), avg_talk_time);

                }
                reportCount++;

                waitTime += avg_wait_time;
                communicationTime += +avg_talk_time;

            }

            beans.put("waitTime", reportCount == 0 ? 0 : waitTime / reportCount);
            beans.put("communicationTime", reportCount == 0 ? 0 : communicationTime / reportCount);
//            if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
//                beans.put("waitTime", waitTime);
//                beans.put("communicationTime", (callCount != 0) ? communicationTime / callCount : 0);
//            } else {
//                beans.put("waitTime", 0); // 總平均等候時間
//                beans.put("communicationTime", 0); // 總平均通話時間
//            }

            // add by aaron 塞週類別資料到report
            tmpHp = new HashMap();
            if ("W".equals(timeType)) {
                for (String key : al) {
                    //report.addRowTitle( ( String )hp.get( key ), ( String )hp.get( key ) ) ;
                    tmpHp = (HashMap) hp2.get(key);
                    if (tmpHp != null) {
                        for (callWaitingCountType type : callWaitingCountType.values()) {
                            if (tmpHp.containsKey(type)) {
                                if (type.equals(callWaitingCountType.AVERAGECOMMUNICATION) || type.equals(callWaitingCountType.AVERAGEWAIT)) {
                                    int temp = (int) tmpHp.get(type);
                                    report.addInt((String) hp.get(key), type.toString(), Math.round((float) temp / 15));
                                } else
                                    report.addInt((String) hp.get(key), type.toString(), (Integer) tmpHp.get(type));
                            }
                        }
                    }

                }
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            //KPI
            if (sqlQuery[sqlChoice.KPI.ordinal()] != null) {

                pstmt = conn.prepareStatement(sqlQuery[sqlChoice.KPI.ordinal()]);
                logger.info("KPI SQL = " + sqlQuery[sqlChoice.KPI.ordinal()]);
                j = 0;
                pstmt.setString(++j, startDate + " 00:00:00");
                pstmt.setString(++j, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                String time = "";

                while (rs.next()) {
                    if ("Y".equals(timeType)) {
                        time = rs.getString("YEAR") + "年";
                        report.addInt(time, "KX", rs.getInt("KXCNT"));
                        report.addInt(time, "KC", rs.getInt("KCCNT"));
                        report.addInt(time, "DX", rs.getInt("DXCNT"));
                        report.addInt(time, "DC", rs.getInt("DCCNT"));
                    } else if ("M".equals(timeType)) {
                        time = rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月";
                        report.addInt(time, "KX", rs.getInt("KXCNT"));
                        report.addInt(time, "KC", rs.getInt("KCCNT"));
                        report.addInt(time, "DX", rs.getInt("DXCNT"));
                        report.addInt(time, "DC", rs.getInt("DCCNT"));
                    } else if ("W".equals(timeType)) {
                        time = rs.getString("DataDate");
                        for (Object o : report.getRowCodeList()) {
                            String columnName = o.toString();
                            if (columnName.substring(0, 11).equals(time) || columnName.substring(0, 12).equals(time)) {
                                report.addInt(columnName, "KX", rs.getInt("KXCNT"));
                                report.addInt(columnName, "KC", rs.getInt("KCCNT"));
                                report.addInt(columnName, "DX", rs.getInt("DXCNT"));
                                report.addInt(columnName, "DC", rs.getInt("DCCNT"));
                            }
                        }


                    } else if ("D".equals(timeType)) {
                        time = rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月" + rs.getString("DAY") + "日";
                        report.addInt(time, "KX", rs.getInt("KXCNT"));
                        report.addInt(time, "KC", rs.getInt("KCCNT"));
                        report.addInt(time, "DX", rs.getInt("DXCNT"));
                        report.addInt(time, "DC", rs.getInt("DCCNT"));


                    } else if ("H".equals(timeType)) {
                        time = rs.getString("HOUR") + "~" + format.format(Integer.parseInt(rs.getString("HOUR")) + 1);
                        report.addInt(time, "KX", rs.getInt("KXCNT"));
                        report.addInt(time, "KC", rs.getInt("KCCNT"));
                        report.addInt(time, "DX", rs.getInt("DXCNT"));
                        report.addInt(time, "DC", rs.getInt("DCCNT"));
                    } else if ("A".equals(timeType)) {
                        time = "總數";
                        report.addInt(time, "KX", rs.getInt("KXCNT"));
                        report.addInt(time, "KC", rs.getInt("KCCNT"));
                        report.addInt(time, "DX", rs.getInt("DXCNT"));
                        report.addInt(time, "DC", rs.getInt("DCCNT"));
                    }

					
					/*
                    time = rs.getString("DataDate");

                    report.addInt(time, "KX", rs.getInt("KXCNT"));
                    report.addInt(time, "KC", rs.getInt("KCCNT"));
                    report.addInt(time, "DX", rs.getInt("DXCNT"));
                    report.addInt(time, "DC", rs.getInt("DCCNT"));
					*/
                }
                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;

            }
            conn.close();
            conn = null;

            String path = httpServletRequest.getServletContext().getRealPath(reportPath);
            File file = new File(path);
            InputStream in = new FileInputStream(file);

            XLSTransformer transformer = new XLSTransformer();
            Workbook workbook = transformer.transformXLS(in, beans);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            IOUtils.closeQuietly(in);
            //  IOUtils.closeQuietly(outputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", new String(excelName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)))
                    .body(outputStream.toByteArray());

        } catch (
                Exception e) {
            logger.error(e);
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }


}

