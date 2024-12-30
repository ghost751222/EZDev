
package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.SatisfactionStatisticsObject;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Service("ConsiliumSatisfactionStatisticsExcelImpl")
public class ConsiliumSatisfactionStatisticsExcelImpl implements ExcelInterface {
    private Map beans = null;
    private String bDataTime_YYYY = null;
    private String bDataTime_MM = null;
    private String bDataTime_DD = null;
    private String eDataTime_YYYY = null;
    private String eDataTime_MM = null;
    private String eDataTime_DD = null;


    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    private String sql = null;


    private static final Logger logger = Logger.getLogger(ConsiliumSatisfactionStatisticsExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "滿意度調查統計表.xls";

    private String reportPath = "/WEB-INF/report/consilium/SatisfactionStatistics.xls";


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {
        {

            String bDataDate = jsonNode.get("time").get("startTime").asText();
            String eDataDate = jsonNode.get("time").get("endTime").asText();

            String bDataTime = bDataDate + " 00:00:00";
            String eDataTime = eDataDate + " 23:59:59";

            Calendar now = Calendar.getInstance(); //抓現在時間
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //格式化日期
            Hashtable table = new Hashtable();
            SatisfactionStatisticsObject obj = new SatisfactionStatisticsObject();

            try {

                conn = ds.getConnection();
                sql = "SELECT " +
                        "SUM(CASE WHEN QUESTION1='-1' or QUESTION1 is null THEN 1 ELSE 0 END) COUNT_Q1_0, " +
                        "SUM(CASE WHEN QUESTION1='1'  THEN 1 ELSE 0 END) COUNT_Q1_1, " +
                        "SUM(CASE WHEN QUESTION1='2'  THEN 1 ELSE 0 END) COUNT_Q1_2, " +
                        "SUM(CASE WHEN QUESTION1='3'  THEN 1 ELSE 0 END) COUNT_Q1_3, " +
                        "SUM(CASE WHEN QUESTION1='4'  THEN 1 ELSE 0 END) COUNT_Q1_4, " +
                        "SUM(CASE WHEN QUESTION1='5'  THEN 1 ELSE 0 END) COUNT_Q1_5, " +
                        "SUM(CASE WHEN QUESTION1='6'  THEN 1 ELSE 0 END) COUNT_Q1_6, " +
                        "SUM(CASE WHEN QUESTION1='7'  THEN 1 ELSE 0 END) COUNT_Q1_7, " +
                        "SUM(CASE WHEN QUESTION1='8'  THEN 1 ELSE 0 END) COUNT_Q1_8, " +
                        "SUM(CASE WHEN QUESTION1='9'  THEN 1 ELSE 0 END) COUNT_Q1_9, " +
                        "SUM(CASE WHEN QUESTION2='-1' or QUESTION2 is null THEN 1 ELSE 0 END) COUNT_Q2_0, " +
                        "SUM(CASE WHEN QUESTION2='1'  THEN 1 ELSE 0 END) COUNT_Q2_1, " +
                        "SUM(CASE WHEN QUESTION2='2'  THEN 1 ELSE 0 END) COUNT_Q2_2, " +
                        "SUM(CASE WHEN QUESTION2='3'  THEN 1 ELSE 0 END) COUNT_Q2_3, " +
                        "SUM(CASE WHEN QUESTION2='4'  THEN 1 ELSE 0 END) COUNT_Q2_4, " +
                        "SUM(CASE WHEN QUESTION2='5'  THEN 1 ELSE 0 END) COUNT_Q2_5, " +
                        "SUM(CASE WHEN QUESTION2='6'  THEN 1 ELSE 0 END) COUNT_Q2_6, " +
                        "SUM(CASE WHEN QUESTION2='7'  THEN 1 ELSE 0 END) COUNT_Q2_7, " +
                        "SUM(CASE WHEN QUESTION2='8'  THEN 1 ELSE 0 END) COUNT_Q2_8, " +
                        "SUM(CASE WHEN QUESTION2='9'  THEN 1 ELSE 0 END) COUNT_Q2_9, " +
                        "SUM(CASE WHEN QUESTION3='-1' or QUESTION3 is null THEN 1 ELSE 0 END) COUNT_Q3_0, " +
                        "SUM(CASE WHEN QUESTION3='1'  THEN 1 ELSE 0 END) COUNT_Q3_1, " +
                        "SUM(CASE WHEN QUESTION3='2'  THEN 1 ELSE 0 END) COUNT_Q3_2, " +
                        "SUM(CASE WHEN QUESTION3='3'  THEN 1 ELSE 0 END) COUNT_Q3_3, " +
                        "SUM(CASE WHEN QUESTION3='4'  THEN 1 ELSE 0 END) COUNT_Q3_4, " +
                        "SUM(CASE WHEN QUESTION3='5'  THEN 1 ELSE 0 END) COUNT_Q3_5, " +
                        "SUM(CASE WHEN QUESTION3='6'  THEN 1 ELSE 0 END) COUNT_Q3_6, " +
                        "SUM(CASE WHEN QUESTION3='7'  THEN 1 ELSE 0 END) COUNT_Q3_7, " +
                        "SUM(CASE WHEN QUESTION3='8'  THEN 1 ELSE 0 END) COUNT_Q3_8, " +
                        "SUM(CASE WHEN QUESTION3='9'  THEN 1 ELSE 0 END) COUNT_Q3_9, " +
                        "SUM(CASE WHEN QUESTION4='-1' or QUESTION4 is null THEN 1 ELSE 0 END) COUNT_Q4_0, " +
                        "SUM(CASE WHEN QUESTION4='1'  THEN 1 ELSE 0 END) COUNT_Q4_1, " +
                        "SUM(CASE WHEN QUESTION4='2'  THEN 1 ELSE 0 END) COUNT_Q4_2, " +
                        "SUM(CASE WHEN QUESTION4='3'  THEN 1 ELSE 0 END) COUNT_Q4_3, " +
                        "SUM(CASE WHEN QUESTION4='4'  THEN 1 ELSE 0 END) COUNT_Q4_4, " +
                        "SUM(CASE WHEN QUESTION4='5'  THEN 1 ELSE 0 END) COUNT_Q4_5, " +
                        "SUM(CASE WHEN QUESTION4='6'  THEN 1 ELSE 0 END) COUNT_Q4_6, " +
                        "SUM(CASE WHEN QUESTION4='7'  THEN 1 ELSE 0 END) COUNT_Q4_7, " +
                        "SUM(CASE WHEN QUESTION4='8'  THEN 1 ELSE 0 END) COUNT_Q4_8, " +
                        "SUM(CASE WHEN QUESTION4='9'  THEN 1 ELSE 0 END) COUNT_Q4_9  " +
                        "FROM M7480_IVRCALLLOGS WHERE STATUS IN ('2','3') " +
                        "AND ? <= IVRTIME " +
                        "AND ? >= IVRTIME ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, bDataTime);
                pstmt.setString(2, eDataTime);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    table.put("COUNT_Q1_0", rs.getInt("COUNT_Q1_0"));
                    table.put("COUNT_Q1_1", rs.getInt("COUNT_Q1_1"));
                    table.put("COUNT_Q1_2", rs.getInt("COUNT_Q1_2"));
                    table.put("COUNT_Q1_3", rs.getInt("COUNT_Q1_3"));
                    table.put("COUNT_Q1_4", rs.getInt("COUNT_Q1_4"));
                    table.put("COUNT_Q1_5", rs.getInt("COUNT_Q1_5"));
                    table.put("COUNT_Q1_6", rs.getInt("COUNT_Q1_6"));
                    table.put("COUNT_Q1_7", rs.getInt("COUNT_Q1_7"));
                    table.put("COUNT_Q1_8", rs.getInt("COUNT_Q1_8"));
                    table.put("COUNT_Q1_9", rs.getInt("COUNT_Q1_9"));
                    table.put("COUNT_Q2_0", rs.getInt("COUNT_Q2_0"));
                    table.put("COUNT_Q2_1", rs.getInt("COUNT_Q2_1"));
                    table.put("COUNT_Q2_2", rs.getInt("COUNT_Q2_2"));
                    table.put("COUNT_Q2_3", rs.getInt("COUNT_Q2_3"));
                    table.put("COUNT_Q2_4", rs.getInt("COUNT_Q2_4"));
                    table.put("COUNT_Q2_5", rs.getInt("COUNT_Q2_5"));
                    table.put("COUNT_Q2_6", rs.getInt("COUNT_Q2_6"));
                    table.put("COUNT_Q2_7", rs.getInt("COUNT_Q2_7"));
                    table.put("COUNT_Q2_8", rs.getInt("COUNT_Q2_8"));
                    table.put("COUNT_Q2_9", rs.getInt("COUNT_Q2_9"));
                    table.put("COUNT_Q3_0", rs.getInt("COUNT_Q3_0"));
                    table.put("COUNT_Q3_1", rs.getInt("COUNT_Q3_1"));
                    table.put("COUNT_Q3_2", rs.getInt("COUNT_Q3_2"));
                    table.put("COUNT_Q3_3", rs.getInt("COUNT_Q3_3"));
                    table.put("COUNT_Q3_4", rs.getInt("COUNT_Q3_4"));
                    table.put("COUNT_Q3_5", rs.getInt("COUNT_Q3_5"));
                    table.put("COUNT_Q3_6", rs.getInt("COUNT_Q3_6"));
                    table.put("COUNT_Q3_7", rs.getInt("COUNT_Q3_7"));
                    table.put("COUNT_Q3_8", rs.getInt("COUNT_Q3_8"));
                    table.put("COUNT_Q3_9", rs.getInt("COUNT_Q3_9"));
                    table.put("COUNT_Q4_0", rs.getInt("COUNT_Q4_0"));
                    table.put("COUNT_Q4_1", rs.getInt("COUNT_Q4_1"));
                    table.put("COUNT_Q4_2", rs.getInt("COUNT_Q4_2"));
                    table.put("COUNT_Q4_3", rs.getInt("COUNT_Q4_3"));
                    table.put("COUNT_Q4_4", rs.getInt("COUNT_Q4_4"));
                    table.put("COUNT_Q4_5", rs.getInt("COUNT_Q4_5"));
                    table.put("COUNT_Q4_6", rs.getInt("COUNT_Q4_6"));
                    table.put("COUNT_Q4_7", rs.getInt("COUNT_Q4_7"));
                    table.put("COUNT_Q4_8", rs.getInt("COUNT_Q4_8"));
                    table.put("COUNT_Q4_9", rs.getInt("COUNT_Q4_9"));
                }
                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;
                conn.close();
                conn = null;

                obj.setTable(table);
                beans = new HashMap();
                beans.put("startTime", bDataDate);
                beans.put("endTime", eDataDate);
                beans.put("printTime", dateFormater.format(now.getTime()));
                beans.put("obj", obj);

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

            } catch (SQLException e) {
                throw e;
            } catch (Exception e) {
                logger.error(e);
                throw e;
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                        pstmt = null;
                    }
                } catch (SQLException ex) {
                    ;
                }

                try {
                    rs.close();
                    rs = null;
                } catch (Exception e) {
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
            }
        }
    }
}
