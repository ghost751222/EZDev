package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.waitsec.ReportData;
import com.consilium.excel.models.waitsec.ReportObj;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@Service("ConsiliumWaitSecExcelImpl")
public class ConsiliumWaitSecExcelImpl implements ExcelInterface {


    private static final Logger logger = Logger.getLogger(ConsiliumWaitSecExcelImpl.class);
    private String excelName = "1996等待秒數列表.xls";
    private String reportPath = "/WEB-INF/report/consilium/WaitSec.xls";

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {
        {
            Map beans = new HashMap();
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            ArrayList resultList = new ArrayList();
            String sqlQuery = "";

            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar now = Calendar.getInstance();

            NumberFormat format = NumberFormat.getInstance();
            format.setMinimumIntegerDigits(2);
            String bDataTime = jsonNode.get("time").get("startTime").asText();
            String eDataTime = jsonNode.get("time").get("endTime").asText();

            int idx = 0;
            ReportObj obj = new ReportObj();
            ReportData reportData = new ReportData();

            reportData.setStartDate(bDataTime);
            reportData.setEndDate(eDataTime);
            reportData.setReportTime(dateFormater.format(now.getTime()));

            try {

                conn = ds.getConnection();

                idx = 0;
                sqlQuery = " select  convert(varchar(20),f_start_time,120)  ct, " +
                        " caller_number, actionid, wait_duration " +
                        " from CallLog3 " +
                        " where f_start_time >= ? " +
                        " and f_start_time <= ? " +
                        " order by f_start_time ";
                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setString(++idx, bDataTime + " 00:00:00");
                pstmt.setString(++idx, eDataTime + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    obj = new ReportObj();
                    obj.setCalltime(rs.getString("ct"));
                    obj.setCallclid(rs.getString("caller_number"));
                    obj.setActionid(rs.getString("actionid"));
                    obj.setCallwaitingduration(rs.getString("wait_duration"));
                    resultList.add(obj);
                }
                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;

                conn.close();
                conn = null;
                beans.put("reportData", reportData);
                beans.put("data", resultList);

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

            } catch (Exception e) {
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
                } catch (SQLException ex) {
                    System.out.println(System.currentTimeMillis() + " 資料庫錯誤:" + ex.getMessage());
                }

                try {
                    if (conn != null) {
                        conn.close();
                        conn = null;
                    }
                } catch (SQLException ex) {
                    System.out.println(System.currentTimeMillis() + " 資料庫錯誤:" + ex.getMessage());
                }
            }

        }
    }
}

