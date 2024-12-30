
package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.FAQStatisticObject;
import com.consilium.excel.models.UnitCode;
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
import java.util.*;

@Service("ConsiliumFAQSectionStatisticExcelImpl")
public class ConsiliumFAQSectionStatisticExcelImpl implements ExcelInterface {

    private static final Logger logger = Logger.getLogger(ConsiliumFAQSectionStatisticExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "FAQ科室統計表.xls";

    private String reportPath = "/WEB-INF/report/consilium/FAQSectionReport.xls";

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        ResultSet rs = null;
        String sql = "";
        PreparedStatement pstmt = null;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowDate = dateFormater.format(now.getTime());
        Map beans = new HashMap();

        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumIntegerDigits(2);

        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate =jsonNode.get("time").get("endTime").asText();
        FAQStatisticObject obj;
        ArrayList list = new ArrayList();
        Hashtable dataTable = new Hashtable();

        try {

            conn = ds.getConnection();
            sql =
                    " SELECT                                                                                                                                                                                                                                                                " +
                            "     SECTIONID,                                                                                                                            " +
                            "     SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))<0 AND  (DATEDIFF(day,EFFICIENTTIME, getdate()))>-30 THEN 1 ELSE 0 END) STATUSE,     " +
                            "     SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))>0 THEN 1 ELSE 0 END) STATUSO,                                     " +
                            "     COUNT(SID) AMOUNT                                                                                                                     " +
                            " FROM                                                                                                                                      " +
                            "     QUESTION_ANSWER                                                                                                                       " +
                            " WHERE                                                                                                                                     " +
                            "     CREATETIME>=?                                                                                " +
                            " AND                                                                                                                                       " +
                            "     CREATETIME<=?                                                                                       " +
                            " GROUP BY                                                                                                                                  " +
                            "     SECTIONID                                                                                                                             ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new FAQStatisticObject();
                obj.setSectionCode(rs.getString("SECTIONID"));
                obj.setSectionName(UnitCode.getInstance().getUnitName("M", rs.getString("SECTIONID")));
                obj.setLeftAmount(rs.getInt("AMOUNT"));
                obj.setLeftEAmount(rs.getInt("STATUSE"));
                obj.setLeftOAmount(rs.getInt("STATUSO"));
                dataTable.put(obj.getSectionCode(), obj);
            }
            rs.close();
            pstmt.close();

            sql =
                    " SELECT                                                                       " +
                            "       SECTIONID,                                                             " +
                            "       SUM(CASE WHEN STATUS='D' THEN 1 ELSE 0 END) STATUSD                    " +
                            " FROM                                                                         " +
                            "       QUESTION_ANSWER                                                        " +
                            " WHERE                                                                        " +
                            "       CLOSEDATATIME>=?                 " +
                            " AND                                                                          " +
                            "       CLOSEDATATIME<=?                     " +
                            " GROUP BY                                                                     " +
                            "       SECTIONID                                                              ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (dataTable.containsKey(rs.getString("SECTIONID") == null ? "" : rs.getString("SECTIONID"))) {
                    obj = (FAQStatisticObject) dataTable.get(rs.getString("SECTIONID"));
                    obj.setLeftDAmount(rs.getInt("STATUSD"));
                } else {
                    obj = new FAQStatisticObject();
                    obj.setSectionCode(rs.getString("SECTIONID"));
                    obj.setLeftDAmount(rs.getInt("STATUSD"));
                    dataTable.put(obj.getSectionCode(), obj);
                }
            }
            rs.close();
            pstmt.close();

            //========================
            //各科室刪除總計
            sql =
                    " SELECT                                                      " +
                            "       SECTIONID,                                            " +
                            "       COUNT(*) AS AMOUNT                                    " +
                            " FROM                                                        " +
                            "       QUESTIONANSWER_BAK                                    " +
                            " WHERE                                                       " +
                            "       LASTUPDATETIME>=?   " +
                            " AND                                                         " +
                            "       LASTUPDATETIME<=?   " +
                            " AND                                                         " +
                            "        FLAG='A'                                             " +
                            " GROUP BY                                                    " +
                            "       SECTIONID                                             ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (dataTable.containsKey(rs.getString("SECTIONID") == null ? "" : rs.getString("SECTIONID"))) {
                    obj = (FAQStatisticObject) dataTable.get(rs.getString("SECTIONID"));
                    obj.setLeftDeleteAmount(rs.getInt("AMOUNT"));
                } else {
                    obj = new FAQStatisticObject();
                    obj.setSectionCode(rs.getString("SECTIONID"));
                    obj.setLeftDeleteAmount(rs.getInt("AMOUNT"));
                    dataTable.put(obj.getSectionCode(), obj);
                }
            }
            rs.close();
            pstmt.close();

            //==========================


            //1996刪除總計
            sql =
                    " SELECT                                                      " +
                            "       SECTIONID,                                            " +
                            "       COUNT(*) AS AMOUNT                                    " +
                            " FROM                                                        " +
                            "       QUESTIONANSWER_BAK                                    " +
                            " WHERE                                                       " +
                            "       LASTUPDATETIME>=?   " +
                            " AND                                                         " +
                            "       LASTUPDATETIME<=?   " +
                            " AND                                                         " +
                            "        FLAG='B'                                             " +
                            " GROUP BY                                                    " +
                            "       SECTIONID                                             ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (dataTable.containsKey(rs.getString("SECTIONID") == null ? "" : rs.getString("SECTIONID"))) {
                    obj = (FAQStatisticObject) dataTable.get(rs.getString("SECTIONID"));
                    obj.setRightDeleteAmount(rs.getInt("AMOUNT"));
                } else {
                    obj = new FAQStatisticObject();
                    obj.setSectionCode(rs.getString("SECTIONID"));
                    obj.setRightDeleteAmount(rs.getInt("AMOUNT"));
                    dataTable.put(obj.getSectionCode(), obj);
                }
            }
            rs.close();
            pstmt.close();

            //==========================


            sql =
                    " SELECT                                                                                                                                                                                                                                                                " +
                            "     SECTIONID,                                                                                                                            " +
                            "     SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))<0 AND  (DATEDIFF(day,EFFICIENTTIME, getdate()))>-30 THEN 1 ELSE 0 END) STATUSE,     " +
                            "     SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))>0 THEN 1 ELSE 0 END) STATUSO,                                     " +
                            "     COUNT(SID) AMOUNT                                                                                                                     " +
                            " FROM                                                                                                                                      " +
                            "     QUESTIONANSWER_RELATION                                                                                                               " +
                            " WHERE                                                                                                                                     " +
                            "     CREATETIME>=?                                                                               " +
                            " AND                                                                                                                                       " +
                            "     CREATETIME<=?                                                                                     " +
                            " GROUP BY                                                                                                                                  " +
                            "     SECTIONID                                                                                                                             ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");
            rs = pstmt.executeQuery();
            while (rs.next()) {

                if (dataTable.containsKey(rs.getString("SECTIONID") == null ? "" : rs.getString("SECTIONID"))) {
                    obj = (FAQStatisticObject) dataTable.get(rs.getString("SECTIONID"));
                    obj.setRightAmount(rs.getInt("AMOUNT"));
                    obj.setRightEAmount(rs.getInt("STATUSE"));
                    obj.setRightOAmount(rs.getInt("STATUSO"));
                } else {
                    obj = new FAQStatisticObject();
                    obj.setSectionCode(rs.getString("SECTIONID"));
                    obj.setRightAmount(rs.getInt("AMOUNT"));
                    obj.setRightEAmount(rs.getInt("STATUSE"));
                    obj.setRightOAmount(rs.getInt("STATUSO"));
                    dataTable.put(obj.getSectionCode(), obj);
                }
            }
            rs.close();
            pstmt.close();

            sql =
                    " SELECT                                                                       " +
                            "       SECTIONID,                                                             " +
                            "       SUM(CASE WHEN STATUS='D' THEN 1 ELSE 0 END) STATUSD                    " +
                            " FROM                                                                         " +
                            "       QUESTIONANSWER_RELATION                                                " +
                            " WHERE                                                                        " +
                            "       CLOSEDATATIME>?                    " +
                            " AND                                                                          " +
                            "       CLOSEDATATIME<=?                     " +
                            " GROUP BY                                                                     " +
                            "       SECTIONID                                                              ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (dataTable.containsKey(rs.getString("SECTIONID") == null ? "" : rs.getString("SECTIONID"))) {
                    obj = (FAQStatisticObject) dataTable.get(rs.getString("SECTIONID"));
                    obj.setRightDAmount(rs.getInt("STATUSD"));
                } else {
                    obj = new FAQStatisticObject();
                    obj.setSectionCode(rs.getString("SECTIONID"));
                    obj.setRightDAmount(rs.getInt("STATUSD"));
                    dataTable.put(obj.getSectionCode(), obj);
                }
            }
            rs.close();
            pstmt.close();

            sql =
                    " SELECT                                                                            " +
                            "       QR.SECTIONID,COUNT(QO.SID) AMOUNT                                           " +
                            " FROM                                                                              " +
                            "       QUESTIONANSWER_RELATION QR, QUESTIONANSWER_OPINION QO                       " +
                            " WHERE                                                                             " +
                            "       QR.SID=QO.PID                                                               " +
                            " AND                                                                               " +
                            "       QO.VISIBLE='Y'                                                              " +
                            " AND                                                                               " +
                            "       QO.CREATETIME>=?                          " +
                            " AND                                                                               " +
                            "       QO.CREATETIME<=?                          " +
                            " GROUP BY QR.SECTIONID                                                             ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (dataTable.containsKey(rs.getString("SECTIONID") == null ? "" : rs.getString("SECTIONID"))) {
                    obj = (FAQStatisticObject) dataTable.get(rs.getString("SECTIONID"));
                    obj.setOpinionAmount(rs.getInt("AMOUNT"));
                } else {
                    obj = new FAQStatisticObject();
                    obj.setSectionCode(rs.getString("SECTIONID"));
                    obj.setOpinionAmount(rs.getInt("AMOUNT"));
                    dataTable.put(obj.getSectionCode(), obj);
                }
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            sql = " SELECT * FROM EXCEL_UNITS WHERE UNITLEVEL='2' ORDER BY ORDERNUM ASC ";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ArrayList<String> sectionList = new ArrayList<String>();
            while (rs.next()) {
                sectionList.add(rs.getString("UNITCODE"));
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;
            conn.close();
            conn = null;


            for (int i = 0; i < sectionList.size(); i++) {

                if (dataTable.containsKey(sectionList.get(i))) {
                    obj = (FAQStatisticObject) dataTable.get(sectionList.get(i));
                    if ("ZZZ".equals(obj.getSectionCode())) {
                        obj.setSectionName("1996");
                    } else {
                        obj.setSectionName(UnitCode.getInstance().getUnitName("M", sectionList.get(i)));

                    }
                    if (obj.getSectionName() != null && obj.getSectionName().length() > 0) list.add(obj);

                } else {
                    obj = new FAQStatisticObject();
                    obj.setSectionName(UnitCode.getInstance().getUnitName("M", sectionList.get(i)));
                    obj.setSectionCode(sectionList.get(i));
                    if ("ZZZ".equals(obj.getSectionCode())) {
                        obj.setSectionName("1996");
                    }
                    obj.setLeftAmount(0);
                    obj.setRightAmount(0);
                    if (obj.getSectionName() != null && obj.getSectionName().length() > 0) list.add(obj);
                }

            }


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
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        beans.put("list", list);
        beans.put("startDate", startDate);
        beans.put("endDate", endDate);
        beans.put("nowDate", nowDate);

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



    }


}
