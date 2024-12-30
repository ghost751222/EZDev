
package com.consilium.excel.consilium;

import com.consilium.domain.AppCode;
import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.DynaHashValue;
import com.consilium.excel.models.talk.CalendarUtil;
import com.consilium.service.AppCodeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
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
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Service("ConsiliumCallIdentificationExcelImpl")
public class ConsiliumCallIdentification implements ExcelInterface {
    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    @Autowired
    AppCodeService appCodeService;

    private static final Logger logger = Logger.getLogger(ConsiliumCallIdentification.class);
    private String excelName = "來電號碼識別.xls";
    private String reportPath = "/WEB-INF/report/consilium/CallIdentification.xls";


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


        String serviceType = checkStringValue(jsonNode.get("serviceType").asText());
        String serviceItem = checkStringValue(jsonNode.get("serviceItem").asText());
        String serviceCategory = checkStringValue(jsonNode.get("serviceCategory").asText());
        String callCategory = checkStringValue(jsonNode.get("callCategory").asText());


        Map beans = new HashMap();
        try {
            List<AppCode> appCodes = appCodeService.findAllByCategory("R08");
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


            int i = 0;
            String groupSQL = "";
            if ("A".equals(timeType)) {
                groupSQL = " 'total' ";
            } else if ("Y".equals(timeType)) {
                groupSQL = " format([CREATETIME],'yyyy年') ";
            } else if ("M".equals(timeType)) {
                groupSQL = " format([CREATETIME],'yyyy年MM月') ";
            } else if ("D".equals(timeType)) {
                groupSQL = " format([CREATETIME],'yyyy年MM月dd日') ";
            } else if ("W".equals(timeType)) {
                groupSQL = " concat(FORMAT([datetime],'yyyy年MM月'),' 第', (DATEPART(week, [datetime]) - DATEPART(week, DATEADD(day, 1, EOMONTH([datetime], -1)))) + 1  ,'週') ";
            } else if ("H".equals(timeType)) {
                groupSQL = " format([CREATETIME],'HH時') ";
            }

            int years = Integer.parseInt(bDataTime_YYYY);
            int month = Integer.parseInt(bDataTime_MM);
            int days = Integer.parseInt(bDataTime_DD);
            int weeks = CalendarUtil.process(years, month, days);
            int hour = 0;
            DynaHashValue report = new DynaHashValue();
            String fday = "";
            String columnTitle = "";

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
                        report.addRowTitle(columnTitle, columnTitle);
                        years = yyyy;
                    }
                    columnTitle = years + "年";
                    if (i == (diffDay - 1)) {
                        report.addRowTitle(columnTitle, columnTitle);
                    }
                } else if ("M".equals(timeType)) {

                    if (month != mm) {
                        report.addRowTitle(columnTitle, columnTitle);
                        years = yyyy;
                        month = mm;
                    }

                    String setMMStr = mm + "";
                    if (setMMStr.length() == 1) setMMStr = "0" + mm;
                    columnTitle = yyyy + "年" + setMMStr + "月";

                    if (i == (diffDay - 1)) {
                        report.addRowTitle(columnTitle, columnTitle);
                    }
                } else if ("D".equals(timeType)) {


                    if (days != dd) {
                        report.addRowTitle(columnTitle, columnTitle);
                        years = yyyy;
                        month = mm;
                        weeks = ww;
                        days = dd;
                    }
                    String setMMStr = mm + "";
                    String setDDStr = dd + "";
                    if (setMMStr.length() == 1) setMMStr = "0" + mm;
                    if (setDDStr.length() == 1) setDDStr = "0" + dd;
                    columnTitle = yyyy + "年" + setMMStr + "月" + setDDStr + "日";
                    if (i == (diffDay - 1)) {
                        report.addRowTitle(columnTitle, columnTitle);
                    }
                } else if ("H".equals(timeType)) {

                    if (hour != hh) {
                        if (!report.getRowCodeList().contains(columnTitle))
                            report.addRowTitle(columnTitle, columnTitle);
                        years = yyyy;
                        month = mm;
                        weeks = ww;
                        days = dd;
                        hour = hh;
                    }
                    String setHHStr = hh + "";
                    if (setHHStr.length() == 1) setHHStr = "0" + hh;
                    columnTitle = setHHStr + "時";

                    if (i == (diffDay - 1)) {
                        report.addRowTitle(columnTitle, columnTitle);
                    }
                }

                fromDate.add(Calendar.HOUR, 1);
            } //所有時間的 for 迴圈
            String sqlString = "SELECT  " + groupSQL + " as datetime," +
                    " count(*) AS amount , " +
                    " trim(CallIdentification) as CallIdentification  " +
                    " FROM CallLog_form " +
                    " WHERE [createTime] >= ? " +
                    " AND [createTime] < DATEADD (day , 1 , cast(? as date) ) " +
                    " AND (CallType = ? or ? is null)" +
                    " AND (PROCESSTYPE = ? or ? is null)" +
                    " AND (servicetype  in (SELECT value FROM   STRING_SPLIT (?, ',') ) or ? is null) " +
                    " AND (SERVICEITEM  in (SELECT value FROM   STRING_SPLIT (?, ',') ) or ? is null) " +
                    " AND CallIdentification is not null " +
                    " AND createTime is not null " +
                    (groupSQL.equals(" 'total' ") ? "Group BY CallIdentification" : (" Group BY CallIdentification," + groupSQL)) +
                    " ORDER BY [datetime] ASC";


            pstmt = conn.prepareStatement(sqlString);
            logger.info(" 來電號碼識別 sql = " + sqlString);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 00:00:00");
            pstmt.setString(3, callCategory);
            pstmt.setString(4, callCategory);
            pstmt.setString(5, serviceCategory);
            pstmt.setString(6, serviceCategory);
            pstmt.setString(7, serviceType);
            pstmt.setString(8, serviceType);
            pstmt.setString(9, serviceItem);
            pstmt.setString(10, serviceItem);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                String datetime = rs.getString("datetime");
                String callIdentification = rs.getString("CallIdentification");
                int amount = rs.getInt("amount");
                report.addInt(datetime, callIdentification, amount);
            }


            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            beans.put("startDate", startDate);
            beans.put("endDate", endDate);
            beans.put("reportTime", dateFormater1.format(now.getTime()));
            beans.put("header", appCodes);
            beans.put("report", report);
            String path = httpServletRequest.getServletContext().getRealPath(reportPath);
            File file = new File(path);
            InputStream in = new FileInputStream(file);

            XLSTransformer transformer = new XLSTransformer();
            Workbook workbook = transformer.transformXLS(in, beans);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(0, 0, 0, appCodes.size()));
            workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(1, 1, 3, appCodes.size()));
            workbook.write(outputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, format("attachment; filename=%s", new String(excelName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)))
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


    private String checkStringValue(String value) {
        return "All".equals(value) || "".equals(value) ? null : value;
    }
}
