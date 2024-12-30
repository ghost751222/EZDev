
package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.talk.CalendarUtil;
import com.consilium.excel.models.talk.DefaultObject;
import com.consilium.excel.models.talk.ReportData;
import com.consilium.excel.models.talk.ServiceLevelObject;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.jxls.transformer.XLSTransformer;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Service("ConsiliumTalkFlowExcelImpl")
public class ConsiliumTalkFlowExcelImpl implements ExcelInterface {
    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private static final Logger logger = Logger.getLogger(ConsiliumCallFlowDetailReportExcelImpl.class);
    private String excelName = "分時話務服務水準表.xls";
    private String reportPath = "/WEB-INF/report/consilium/TalkFlow.xls";

    private ServiceLevelObject createServiceLevelObject(String dateColumn) {
        ServiceLevelObject serviceObj = new ServiceLevelObject();
        serviceObj.setDataTime(dateColumn);
        serviceObj.setDataAllNbr(0);//進線數
        serviceObj.setDataNoNbr(0);//未等候即放棄數
        serviceObj.setDataNoNbrWait(0);//等候放棄數
        serviceObj.setDataNbr(0);//接通數
        serviceObj.setMaxCommunication(0);//平均放棄數
        serviceObj.setMaxWait(CalendarUtil.formatDate(0)); //最長等候時間
        serviceObj.setAverageRang(CalendarUtil.formatDate(0)); //平均響鈴時間
        serviceObj.setAverageWait(CalendarUtil.formatDate(0));//平均等候時間
        serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));//平均通話長度
        serviceObj.setAverageCallNotAnswer(0); //座席響鈴未接通總數
        return serviceObj;
    }

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        NumberFormat format = NumberFormat.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();
        String theDate;
        format.setMinimumIntegerDigits(2);
        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();
        String dateType = jsonNode.get("dateType").asText();
        String bDataTime_YYYY = startDate.substring(0, 4);
        String eDataTime_YYYY = endDate.substring(0, 4);
        String bDataTime_MM = startDate.substring(5, 7);
        String bDataTime_DD = startDate.substring(8, 10);
        String eDataTime_MM = endDate.substring(5, 7);
        String eDataTime_DD = endDate.substring(8, 10);

        Calendar fromDate = Calendar.getInstance();
        fromDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1, Integer.parseInt(bDataTime_DD), 0, 0, 0);

        //取得 展現方式 依年Y 依月M 依週W 依日D 依時H 依總數A
        String timeType = jsonNode.get("timeType").asText();
        int cnt = 1;
        Map beans = new HashMap();
        try {

            conn = ds.getConnection();
            SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat getDateHous = new SimpleDateFormat("yyyyMMddHH");

            Calendar toDate = Calendar.getInstance();
            fromDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1, Integer.parseInt(bDataTime_DD), 0, 0, 0);
            toDate.set(Integer.parseInt(eDataTime_YYYY), Integer.parseInt(eDataTime_MM) - 1, Integer.parseInt(eDataTime_DD), 0, 0, 0);
            int diffDay = (int) ((toDate.getTimeInMillis() - fromDate.getTimeInMillis()) / (1000 * 60 * 60) + 24);

            //計算時間

            Calendar tempDate = Calendar.getInstance();

            toDate.set(Integer.parseInt(eDataTime_YYYY), Integer.parseInt(eDataTime_MM) - 1, Integer.parseInt(eDataTime_DD),
                    0, 0, 0);
            tempDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1,
                    Integer.parseInt(bDataTime_DD), 0, 0, 0);

            ReportData reportData = new ReportData();
            reportData.setStartDate(bDataTime_YYYY + "-" + bDataTime_MM + '-' + bDataTime_DD);
            reportData.setEndDate(eDataTime_YYYY + "-" + eDataTime_MM + '-' + eDataTime_DD);
            reportData.setReportTime(dateFormater1.format(now.getTime()));
            reportData.setTimeType(timeType);

            DefaultObject defObj = new DefaultObject();
            ServiceLevelObject serviceObj = new ServiceLevelObject();
            DefaultObject defTotalObj = new DefaultObject();
            Map<String, ServiceLevelObject> hp = new HashMap<>();
            int i = 0;
            int maxWait = 0;
            int totalCnt = 0;
            int max_avg_abandoned = 0;
            int totalAllNbr = 0;
            int totalNoNbrWait = 0;
            int totalNbr = 0;
            int averageCallNotAnswer = 0;
            int total_avg_ring_time = 0;
            int total_avg_wait_time = 0;
            int total_avg_talk_time = 0;
            int recordCount = 0;
            String groupSQL = "";
            if ("A".equals(timeType)) {
                cnt = 15;
                groupSQL = " 'total' ";
            } else if ("Y".equals(timeType)) {
                cnt = 15;
                groupSQL = " format([datetime],'yyyy年') ";
            } else if ("M".equals(timeType)) {
                cnt = 15;
                groupSQL = " format([datetime],'yyyy年MM月') ";
            } else if ("D".equals(timeType)) {
                cnt = 15;
                groupSQL = " format([datetime],'yyyy年MM月dd日') ";
            } else if ("W".equals(timeType)) {
                cnt = 15;
                groupSQL = " concat(FORMAT([datetime],'yyyy年MM月'),' 第', (DATEPART(week, [datetime]) - DATEPART(week, DATEADD(day, 1, EOMONTH([datetime], -1)))) + 1  ,'週') ";
            } else if ("H".equals(timeType)) {
                //cnt = (int) ((toDate.getTimeInMillis() - fromDate.getTimeInMillis()) / (1000 * 60 * 60) + 24) / 24;
                cnt = 1;
                groupSQL = " format([datetime],'yyyy年MM月dd日 HH時') ";

            }

            int years = Integer.parseInt(bDataTime_YYYY);
            int month = Integer.parseInt(bDataTime_MM);
            int days = Integer.parseInt(bDataTime_DD);
            int weeks = CalendarUtil.process(years, month, days);
            int hour = 0;
            String fday = "";
            if (bDataTime_DD.length() == 1)
                fday = "0" + bDataTime_DD;
            else
                fday = bDataTime_DD;
            for (i = 0; i < diffDay; i++) {
                //組出所有時間
                theDate = getDateHous.format(fromDate.getTime());
                //分解時間 年 月 日 週 時
                int yyyy = Integer.parseInt((String) theDate.substring(0, 4));
                int mm = Integer.parseInt((String) theDate.substring(4, 6));
                int dd = Integer.parseInt((String) theDate.substring(6, 8));
                int ww = CalendarUtil.process(yyyy, mm, dd);
                int hh = Integer.parseInt((String) theDate.substring(8, 10));

                if ("Y".equals(timeType)) {
                    if (years != yyyy) {
                        serviceObj = createServiceLevelObject(defObj.getDataTime());
                        hp.put(defObj.getDataTime(), serviceObj);
                        reportData.addServiceLevelObject(serviceObj);
                        defObj = new DefaultObject();
                        years = yyyy;
                    }
                    defObj.setDataTime(years + "年");
                    if (i == (diffDay - 1)) {
                        serviceObj = createServiceLevelObject(defObj.getDataTime());
                        hp.put(defObj.getDataTime(), serviceObj);
                        reportData.addServiceLevelObject(serviceObj);
                    }
                } else if ("M".equals(timeType)) {
                    if (month != mm) {
                        serviceObj = createServiceLevelObject(defObj.getDataTime());
                        hp.put(defObj.getDataTime(), serviceObj);
                        reportData.addServiceLevelObject(serviceObj);
                        defObj = new DefaultObject();
                        years = yyyy;
                        month = mm;
                    }

                    String setMMStr = mm + "";
                    if (setMMStr.length() == 1) setMMStr = "0" + mm;
                    defObj.setDataTime(yyyy + "年" + setMMStr + "月");

                    if (i == (diffDay - 1)) {
                        serviceObj = createServiceLevelObject(defObj.getDataTime());
                        hp.put(defObj.getDataTime(), serviceObj);
                        reportData.addServiceLevelObject(serviceObj);
                    }
                } else if ("W".equals(timeType)) {
                    if (weeks != ww) {
                        serviceObj = createServiceLevelObject(defObj.getDataTime());
                        hp.put(defObj.getDataTime(), serviceObj);
                        reportData.addServiceLevelObject(serviceObj);
                        defObj = new DefaultObject();
                        years = yyyy;
                        month = mm;
                        weeks = ww;
                        fday = dd + "";
                        if (fday.length() == 1) fday = "0" + fday;
                    }

                    String lday = dd + "";
                    if (lday.length() == 1) lday = "0" + lday;
                    String setMMStr = mm + "";
                    if (setMMStr.length() == 1) setMMStr = "0" + mm;
                    defObj.setDataTime(yyyy + "年" + setMMStr + "月 第" + ww + "週 " + fday + "~" + lday + "日");

                    if (i == (diffDay - 1)) {
                        serviceObj = createServiceLevelObject(defObj.getDataTime());
                        hp.put(defObj.getDataTime(), serviceObj);
                        reportData.addServiceLevelObject(serviceObj);
                    }
                } else if ("D".equals(timeType)) {
                    if (days != dd) {
                        serviceObj = createServiceLevelObject(defObj.getDataTime());
                        hp.put(defObj.getDataTime(), serviceObj);
                        reportData.addServiceLevelObject(serviceObj);
                        defObj = new DefaultObject();
                        years = yyyy;
                        month = mm;
                        weeks = ww;
                        days = dd;
                    }

                    String setMMStr = mm + "";
                    String setDDStr = dd + "";
                    if (setMMStr.length() == 1) setMMStr = "0" + mm;
                    if (setDDStr.length() == 1) setDDStr = "0" + dd;
                    defObj.setDataTime(yyyy + "年" + setMMStr + "月" + setDDStr + "日");

                    if (i == (diffDay - 1)) {
                        serviceObj = createServiceLevelObject(defObj.getDataTime());
                        hp.put(defObj.getDataTime(), serviceObj);
                        reportData.addServiceLevelObject(serviceObj);
                    }
                } else if ("H".equals(timeType)) {
                    if (hour != hh) {
                        serviceObj = createServiceLevelObject(defObj.getDataTime());
                        hp.put(defObj.getDataTime(), serviceObj);
                        reportData.addServiceLevelObject(serviceObj);
                        defObj = new DefaultObject();
                        years = yyyy;
                        month = mm;
                        weeks = ww;
                        days = dd;
                        hour = hh;
                    }

                    String setMMStr = mm + "";
                    String setDDStr = dd + "";
                    String setHHStr = hh + "";
                    if (setMMStr.length() == 1) setMMStr = "0" + mm;
                    if (setDDStr.length() == 1) setDDStr = "0" + dd;
                    if (setHHStr.length() == 1) setHHStr = "0" + hh;
                    defObj.setDataTime(yyyy + "年" + setMMStr + "月" + setDDStr + "日 " + setHHStr + "時");

                    if (i == (diffDay - 1)) {
                        serviceObj = createServiceLevelObject(defObj.getDataTime());
                        hp.put(defObj.getDataTime(), serviceObj);
                        reportData.addServiceLevelObject(serviceObj);
                    }
                }

                fromDate.add(Calendar.HOUR, 1);
            } //所有時間的 for 迴圈

            String sqlString = "set datefirst 1;SELECT  " + groupSQL + " as datetime," +
                    " SUM(ANSWERED) AS ANSWERED , " +    //接通數
                    " SUM(ABANDONED) AS ABANDONED, " +   //等候放棄數
                    " SUM(INBOUND_CALLS) AS INBOUND_CALLS, " +  //進線數
                    " SUM(AVG_ABANDONED) AS AVG_ABANDONED, " +  //平均放棄數
                    " SUM(MAX_WAIT_TIME) AS MAX_WAIT_TIME, " +  //最長等候時間
                    " SUM(AVG_RING_TIME) AS AVG_RING_TIME, " +  //平均響鈴時間
                    " SUM(AVG_WAIT_TIME) AS AVG_WAIT_TIME, " +  //平均等候時間
                    " SUM(AVG_TALK_TIME) AS AVG_TALK_TIME , " +  //平均通話長度
                    " SUM(RNA) AS RNA ," +  //座席響鈴未接通總數
                    " sum(case when INBOUND_CALLS >0 then 1 else 0 end) as cnt," +
                    "   MAX(AVG_ABANDONED) MAX_ABANDONED        " +
                    " FROM CallLog1 " +
                    " WHERE [datetime] >= ? " +
                    " AND [datetime] < DATEADD (day , 1 , cast(? as date) ) " +
                    " AND [datetime] IS NOT NULL " +

                    ("H".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) IN ('0', '6')  " +
                                    " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : ("W".equals(dateType) ?
                            " AND ( ((datepart(dd,[dateTime]) /7) +1) NOT IN ('0', '6')  " +
                                    " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CallLog1.[dateTime] as date))) "
                            : "")) +

                    (groupSQL.equals(" 'total' ") ? "" : (" Group BY " + groupSQL)) +
                    " ORDER BY [datetime] ASC";

            pstmt = conn.prepareStatement(sqlString);
            logger.info(" 分時話務服務水準報表 sql = " + sqlString);
            pstmt.setString(1, (String) reportData.getStartDate() + " 00:00:00");
            pstmt.setString(2, (String) reportData.getEndDate() + " 00:00:00");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                serviceObj = null;
                String dataTime = rs.getString("datetime");


                int answered = rs.getInt("ANSWERED");
                int abandoned = rs.getInt("ABANDONED");
                int inbound_calls = rs.getInt("INBOUND_CALLS");
                int avg_abandoned = rs.getInt("AVG_ABANDONED");
                int max_abandoned = rs.getInt("MAX_ABANDONED");
                int max_wait_time = rs.getInt("MAX_WAIT_TIME");
                int avg_ring_time = rs.getInt("AVG_RING_TIME") / cnt;
                int avg_wait_time = rs.getInt("AVG_WAIT_TIME") / cnt;
                int avg_talk_time = rs.getInt("AVG_TALK_TIME") / cnt;
                int rna = rs.getInt("RNA");

                if (max_wait_time > maxWait) maxWait = max_wait_time;
                if (max_abandoned > max_avg_abandoned) max_avg_abandoned = max_abandoned;

                totalAllNbr += inbound_calls;
                totalNoNbrWait += abandoned;
                totalNbr += answered;
                averageCallNotAnswer += rna;
                totalCnt += cnt;
                total_avg_ring_time += avg_ring_time;
                total_avg_wait_time += avg_wait_time;
                total_avg_talk_time += avg_talk_time;

                if("W".equals(timeType)){
                    for (Map.Entry<String, ServiceLevelObject> entry : hp.entrySet()) {
                        if(entry.getKey().indexOf(dataTime) > -1){
                            dataTime = entry.getKey();
                            break;
                        }
                    }
                }

                if (hp.containsKey(dataTime)) {
                    serviceObj = hp.get(dataTime);
                } else {
                    serviceObj = new ServiceLevelObject();
                    reportData.addServiceLevelObject(serviceObj);
                }

                serviceObj.setDataTime(dataTime);
                serviceObj.setDataAllNbr(inbound_calls);//進線數
                serviceObj.setDataNoNbr(0);//未等候即放棄數
                serviceObj.setDataNoNbrWait(abandoned);//等候放棄數
                serviceObj.setDataNbr(answered);//接通數

                serviceObj.setMaxCommunication(max_abandoned);//平均放棄數
                serviceObj.setMaxWait(CalendarUtil.formatDate(max_wait_time)); //最長等候時間
                serviceObj.setAverageRang(CalendarUtil.formatDate(avg_ring_time)); //平均響鈴時間
                serviceObj.setAverageWait(CalendarUtil.formatDate(avg_wait_time));//平均等候時間
                serviceObj.setAverageCommunication(CalendarUtil.formatDate(avg_talk_time));//平均通話長度
                serviceObj.setAverageCallNotAnswer(rna); //座席響鈴未接通總數

                if (inbound_calls > 0) recordCount++;
            }

            reportData.setTotalAllNbr(totalAllNbr);
            reportData.setTotalNoNbr(0);
            reportData.setTotalNoNbrWait(totalNoNbrWait);
            reportData.setTotalNbr(totalNbr);
            reportData.setPeriodMaxCommunication(max_avg_abandoned);
            reportData.setPeriodMaxWait(CalendarUtil.formatDate(maxWait));

            if (recordCount > 0) {
                reportData.setPeriodAverageRang(CalendarUtil.formatDate(total_avg_ring_time / recordCount));
                reportData.setPeriodAverageWait(CalendarUtil.formatDate(total_avg_wait_time / recordCount));
                reportData.setPeriodAverageCommunication(CalendarUtil.formatDate(total_avg_talk_time / recordCount));
            } else {
                reportData.setPeriodAverageRang(CalendarUtil.formatDate(0));
                reportData.setPeriodAverageWait(CalendarUtil.formatDate(0));
                reportData.setPeriodAverageCommunication(CalendarUtil.formatDate(0));
            }

            reportData.setAverageCallNotAnswer(averageCallNotAnswer);

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            beans.put("reportData", reportData);
            String path = httpServletRequest.getServletContext().getRealPath(reportPath);
            File file = new File(path);
            InputStream in = new FileInputStream(file);

            XLSTransformer transformer = new XLSTransformer();
            Workbook workbook = transformer.transformXLS(in, beans);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, format("attachment; filename=%s", new String(excelName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)))
                    .body(outputStream.toByteArray());
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
                ;
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
            } catch (SQLException ex) {
                ;
            }
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException ex) {
                ;
            }
        } // finally
    }
}
