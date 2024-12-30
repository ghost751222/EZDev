
package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.ExternalReportData;
import com.consilium.excel.models.ExternalReportObject;
import com.consilium.excel.models.ExternalReportTemp;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Service("ConsiliumRISReportExcelImpl")
public class ConsiliumRISReportExcelImpl implements ExcelInterface {
    private static final Logger logger = Logger.getLogger(ConsiliumRISReportExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "轉接統計表.xls";

    private String reportPath = "/WEB-INF/report/consilium/RISStatistic.xls";


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        NumberFormat format = NumberFormat.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();

        format.setMinimumIntegerDigits(2);

        String startTime = jsonNode.get("time").get("startTime").asText();
        String endTime = jsonNode.get("time").get("endTime").asText();
        String dateType = jsonNode.get("dateType").asText();
        String bDataTime_YYYY = startTime.substring(0, 4);
        String eDataTime_YYYY = endTime.substring(0, 4);
        String bDataTime_MM = startTime.substring(5, 7);
        String bDataTime_DD = startTime.substring(8, 10);
        String eDataTime_MM = endTime.substring(5, 7);
        String eDataTime_DD = endTime.substring(8, 10);

        Calendar fromDate = Calendar.getInstance();
        fromDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1, Integer.parseInt(bDataTime_DD), 0, 0, 0);

        int diffHour = 24;
        ExternalReportData reportData = new ExternalReportData();
        ExternalReportObject reportObj = null;
        ExternalReportTemp reportTemp = new ExternalReportTemp();

        reportData.setStartDate(bDataTime_YYYY + "-" + bDataTime_MM + '-' + bDataTime_DD);
        reportData.setEndDate(eDataTime_YYYY + "-" + eDataTime_MM + '-' + eDataTime_DD);
        reportData.setReportTime(dateFormater.format(now.getTime()));

        try {

            conn = ds.getConnection();

            String sql =
                    " SELECT                                                         " +
                            " 	format(datepart(hh,alert_time),'00') CALLTIME,                          " +
                            " 	SUM(CASE WHEN success=1 THEN 1 ELSE 0 END) SAMOUNT,   " +
                            " 	SUM(CASE WHEN success =0 THEN 1 ELSE 0 END) FAMOUNT   " +
                            " FROM                                                           " +
                            " 	calllog5                                      " +
                            " where 1=1 " +
                            " AND alert_time >= ?         " +
                            " AND alert_time < cast(? as datetime)  +1           " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,[alert_time]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(calllog5.[alert_time] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,[alert_time]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(calllog5.[alert_time] as date))) "
                                    : "")) +

                            " GROUP BY datepart(hh,alert_time) ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reportData.getStartDate() + " 00:00:00");
            pstmt.setString(2, reportData.getEndDate() + " 00:00:00");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String timeKey = rs.getString("CALLTIME") + ":00";

                reportTemp.setCallByHourValue(timeKey, reportTemp.TYPEHS, rs.getInt("SAMOUNT"));//直轉戶所-成功
                reportTemp.setCallByHourValue(timeKey, reportTemp.TYPEHF, rs.getInt("FAMOUNT"));//直轉戶所-失敗

            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            sql =
                    "SELECT format(datepart(hh,CREATETIME),'00') CALLTIME, COUNT(1) CNT " +
                            "FROM CALLLOG_FORM " +
                            "WHERE CREATETIME >= ? " +
                            "AND CREATETIME < cast(? as datetime)+1 " +
                            "AND CASETYPE = 'I' " +
                            "GROUP BY datepart(hh,CREATETIME)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reportData.getStartDate() + " 00:00:00");
            pstmt.setString(2, reportData.getEndDate() + " 00:00:00");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String timeKey = rs.getString("CALLTIME") + ":00";
                //reportTemp.setCallByHourValue(timeKey,reportTemp.ALL_CALL_ANSWERED,rs.getInt("CNT"));//進線數
                reportTemp.setCallByHourValue(timeKey, reportTemp.TYPESS, rs.getInt("CNT"));//STT-成功
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            sql =
                    "SELECT format(datepart(hh,CALLTIME),'00') CALLTIME, COUNT(1) CNT " +
                            "FROM M7480_CALL " +
                            "WHERE CALLTIME >= ? " +
                            "AND CALLTIME < cast(? as datetime)+1 " +
                            "AND ANSWERTIME IS NULL AND CALLIVRDURATION > 0 " +
                            "AND EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID AND SERVICEDTMF = 1) " +
                            "AND NOT EXISTS(SELECT 1 FROM CTI_ID_LOSS CL WHERE CL.CALLID = M7480_CALL.M7480CALLID AND CL.STATUS <> 0) " +
                            "GROUP BY datepart(hh,CALLTIME)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reportData.getStartDate() + " 00:00:00");
            pstmt.setString(2, reportData.getEndDate() + " 00:00:00");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String timeKey = rs.getString("CALLTIME") + ":00";
                //reportTemp.setCallByHourValue(timeKey,reportTemp.ALL_CALL_ANSWERED,rs.getInt("CNT"));//進線數
                reportTemp.setCallByHourValue(timeKey, reportTemp.TYPESF, rs.getInt("CNT"));//STT-失敗
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            Hashtable<String, String> noPrintTimeTable = new Hashtable<String, String>();
            //因為早上8:00到晚上10:00上班，所以不秀出來
            noPrintTimeTable.put("08:00", "NO");
            noPrintTimeTable.put("09:00", "NO");
            noPrintTimeTable.put("10:00", "NO");
            noPrintTimeTable.put("11:00", "NO");
            noPrintTimeTable.put("12:00", "NO");
            noPrintTimeTable.put("13:00", "NO");
            noPrintTimeTable.put("14:00", "NO");
            noPrintTimeTable.put("15:00", "NO");
            noPrintTimeTable.put("16:00", "NO");
            noPrintTimeTable.put("17:00", "NO");
            noPrintTimeTable.put("18:00", "NO");
            noPrintTimeTable.put("19:00", "NO");
            noPrintTimeTable.put("20:00", "NO");
            noPrintTimeTable.put("21:00", "NO");
            //noPrintTimeTable.put("22:00", "NO");
//            sql = "SELECT format(BEGINTIME,'HH:mm') BEGINTIME, SUM(CALLCOUNT)CALLCOUNT, SUM(NBRCALLSABANDONED)NBRCALLSABANDONED, " +
//                    "SUM(NBRCALLSABANDONEDWAIT)NBRCALLSABANDONEDWAIT, SUM(NBRCALLSANSWERED)NBRCALLSANSWERED " +
//                    "FROM M7480_SITE_INFO " +
//                    "WHERE BEGINTIME >= ? " +
//                    "AND BEGINTIME < cast(? as datetime)+1 " +
//                    "GROUP BY format(BEGINTIME,'HH:mm')";
//
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, reportData.getStartDate() + " 00:00:00");
//            pstmt.setString(2, reportData.getEndDate() + " 00:00:00");
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                if (noPrintTimeTable.get(rs.getString("BEGINTIME")) == null) {
//                    reportTemp.setCallByHourValue(rs.getString("BEGINTIME"), reportTemp.ALL_CALL_ANSWERED, rs.getInt("CALLCOUNT"));//進線數
//                }
//            }
//
//            rs.close();
//            rs = null;
//            pstmt.close();
//            pstmt = null;

            sql =
                    " SELECT                                                         " +
                            " 	format(datepart(hh,CREATETIME),'00') CALLTIME,                       " +

                            " 	SUM(CASE WHEN STATUS=1 THEN 1 ELSE 0 END) SAMOUNT,   " +
                            " 	SUM(CASE WHEN isnull(STATUS,0) = 0 THEN 1 ELSE 0 END) FAMOUNT   " +
                            " FROM                                                           " +
                            " 	calllog8                                      " +
                            " WHERE                                                          " +
                            " 	CREATETIME >= ?          " +
                            " AND                                                            " +
                            " 	CREATETIME < cast(? as datetime)+1           " +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,[CREATETIME]) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(calllog8.[CREATETIME] as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,[CREATETIME]) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(calllog8.[CREATETIME] as date))) "
                                    : "")) +
                            " GROUP BY                                                       " +
                            " 	datepart(hh,CREATETIME)                      ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reportData.getStartDate() + " 00:00:00");
            pstmt.setString(2, reportData.getEndDate() + " 00:00:00");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String timeKey = rs.getString("CALLTIME") + ":00";
                reportTemp.setCallByHourValue(timeKey, reportTemp.ALL_CALL_ANSWERED, rs.getInt("SAMOUNT") + rs.getInt("FAMOUNT"));//進線數
                reportTemp.setCallByHourValue(timeKey, reportTemp.TYPE1S, rs.getInt("SAMOUNT"));//身分證掛失-成功
                reportTemp.setCallByHourValue(timeKey, reportTemp.TYPE1F, rs.getInt("FAMOUNT"));//身分證掛失-失敗
            }

            rs.close();
            rs = null;

            pstmt.close();
            pstmt = null;

            conn.close();
            conn = null;

            SimpleDateFormat tmpDateFormater = new SimpleDateFormat("HH:mm");
            Calendar tmpFromDate = (Calendar) fromDate.clone();
            for (int i = 0; i < diffHour; i++) {
                reportObj = new ExternalReportObject();
                String theDate = tmpDateFormater.format(fromDate.getTime());
                tmpFromDate.add(Calendar.HOUR_OF_DAY, 1);
                String endStr = " ~ " + tmpDateFormater.format(tmpFromDate.getTime());
                reportObj.setDataTime(theDate + endStr);

//                reportObj.setDataAllNbr(reportTemp.getCallByHourValue(theDate,reportTemp.TYPE1S) +
//                                        reportTemp.getCallByHourValue(theDate,reportTemp.TYPE1F) +
//                                        reportTemp.getCallByHourValue(theDate,reportTemp.TYPE2S) +
//                                        reportTemp.getCallByHourValue(theDate,reportTemp.TYPE2F) +
//                                        reportTemp.getCallByHourValue(theDate,reportTemp.TYPE9S) +
//                                        reportTemp.getCallByHourValue(theDate,reportTemp.TYPE9F) +
//                                        reportTemp.getCallByHourValue(theDate,reportTemp.TYPESS) +
//                                        reportTemp.getCallByHourValue(theDate,reportTemp.TYPESF) +
//                                        reportTemp.getCallByHourValue(theDate,reportTemp.TYPEHS) +
//                                        reportTemp.getCallByHourValue(theDate,reportTemp.TYPEHF)
//                );//進線數

                reportObj.setDataAllNbr2(reportTemp.getCallByHourValue(theDate, reportTemp.ALL_CALL_ANSWERED));
                reportObj.setDataAllNbr1(reportTemp.getCallByHourValue(theDate, reportTemp.TYPESS) +
                        reportTemp.getCallByHourValue(theDate, reportTemp.TYPESF) +
                        reportTemp.getCallByHourValue(theDate, reportTemp.TYPEHS) +
                        reportTemp.getCallByHourValue(theDate, reportTemp.TYPEHF)
                );//進線數

                reportObj.setType1S(reportTemp.getCallByHourValue(theDate, reportTemp.TYPE1S));//身分證掛失-成功
                reportObj.setType1F(reportTemp.getCallByHourValue(theDate, reportTemp.TYPE1F));//身分證掛失-失敗
                reportObj.setType2S(reportTemp.getCallByHourValue(theDate, reportTemp.TYPE2S));//警政治安事項-成功
                reportObj.setType2F(reportTemp.getCallByHourValue(theDate, reportTemp.TYPE2F));//警政治安事項-失敗
                reportObj.setType9S(reportTemp.getCallByHourValue(theDate, reportTemp.TYPE9S));//緊急情況通報-成功
                reportObj.setType9F(reportTemp.getCallByHourValue(theDate, reportTemp.TYPE9F));//緊急情況通報-失敗

                reportObj.setTypeSS(reportTemp.getCallByHourValue(theDate, reportTemp.TYPESS));//STT-成功
                reportObj.setTypeSF(reportTemp.getCallByHourValue(theDate, reportTemp.TYPESF));//STT-失敗
                reportObj.setTypeHS(reportTemp.getCallByHourValue(theDate, reportTemp.TYPEHS));//直轉戶所-成功
                reportObj.setTypeHF(reportTemp.getCallByHourValue(theDate, reportTemp.TYPEHF));//直轉戶所-失敗

                reportData.addDataList(reportObj);
                fromDate.add(Calendar.HOUR_OF_DAY, 1);
            }

            reportData.setTotalAllNbr1(
                    reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPESS) +
                            reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPESF) +
                            reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPEHS) +
                            reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPEHF)
            );
            reportData.setTotalAllNbr2(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.ALL_CALL_ANSWERED));//總進線數
            reportData.setTotalType1S(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPE1S));//身分證掛失-成功(加總)
            reportData.setTotalType1F(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPE1F));//身分證掛失-失敗(加總)
            reportData.setTotalType2S(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPE2S));//警政治安事項-成功(加總)
            reportData.setTotalType2F(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPE2F));//警政治安事項-失敗(加總)
            reportData.setTotalType9S(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPE9S));//緊急情況通報-成功(加總)
            reportData.setTotalType9F(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPE9F));//緊急情況通報-失敗(加總)

            reportData.setTotalTypeSS(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPESS));//STT-成功(加總)
            reportData.setTotalTypeSF(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPESF));//STT-失敗(加總)
            reportData.setTotalTypeHS(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPEHS));//直轉戶所-成功(加總)
            reportData.setTotalTypeHF(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.TYPEHF));//直轉戶所-失敗(加總)

            Map beans = new HashMap();
            beans.put("reportData", reportData);

            String path = httpServletRequest.getServletContext().getRealPath(reportPath);
            File file = new File(path);
            InputStream in = new FileInputStream(file);

            XLSTransformer transformer = new XLSTransformer();
            Workbook workbook = transformer.transformXLS(in, beans);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(outputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", new String(excelName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)))
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
