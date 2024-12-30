package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.incomesec.ReportData;
import com.consilium.excel.models.incomesec.ReportObj;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@Service("ConsiliumIncomeSecExcelImpl")
public class ConsiliumIncomeSecExcelImpl implements ExcelInterface {




    private static final Logger logger = Logger.getLogger(ConsiliumIncomeSecExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "1996進線量及秒數.xls";

    private String reportPath = "/WEB-INF/report/consilium/IncomeSec.xls";

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
//                sqlQuery = "select datepart(yyyy,calltime) ystr, " +
//                        "case when callclid > '9' then '手機' else '市話' end type, " +
//                        "count(1) cnt, " +
//                        "sum(callwaitingduration + calltalkingduration) ctime " +
//                        "from M7480_CALL t " +
//                        "where calltime >= ? " +
//                        "and calltime <= ? " +
//                        "group by  datepart(yyyy,calltime), case when callclid > '9' then '手機' else '市話' end " +
//                        "order by  datepart(yyyy,calltime), case when callclid > '9' then '手機' else '市話' end ";

                sqlQuery = "select datepart(yyyy,hour_time) ystr, " +
                        " call_type type, " +
                        "sum(total_call) cnt, " +
                        "sum(total_talktime) ctime " +
                        "from calllog4 t " +
                        "where hour_time >= ? " +
                        "and hour_time <= ? " +
                        "group by  datepart(yyyy,hour_time), call_type " +
                        "order by  datepart(yyyy,hour_time), call_type ";

                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setString(++idx, bDataTime + " 00:00:00");
                pstmt.setString(++idx, eDataTime + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    obj = new ReportObj();
                    obj.setystr(rs.getString("ystr"));
                    obj.setType(rs.getString("type"));
                    obj.setCnt(rs.getString("cnt"));
                    obj.setCtime(rs.getString("ctime"));
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

