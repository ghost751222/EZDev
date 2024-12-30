
package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.LineServiceObj;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service("ConsiliumLineServiceExcelImpl")
public class ConsiliumLineServiceExcelImpl implements ExcelInterface {

    private static final Logger logger = Logger.getLogger(ConsiliumLineServiceExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "Line互動客服統計表.xls";

    private String reportPath = "/WEB-INF/report/consilium/LineService.xls";


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sqlQuery = "";
        Map beans = new HashMap();

        NumberFormat format = NumberFormat.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();
//       String bDataTime_YYYY = request.getParameter("bDataTime_YYYY");
//       String eDataTime_YYYY = request.getParameter("eDataTime_YYYY");
        format.setMinimumIntegerDigits(2);
//       String bDataTime_MM = format.format(Long.valueOf(request.getParameter("bDataTime_MM")));
//       String bDataTime_DD = format.format(Long.valueOf(request.getParameter("bDataTime_DD")));
//       String eDataTime_MM = format.format(Long.valueOf(request.getParameter("eDataTime_MM")));
//       String eDataTime_DD = format.format(Long.valueOf(request.getParameter("eDataTime_DD")));
//       String startDate = bDataTime_YYYY + "-" + bDataTime_MM + '-' + bDataTime_DD;
//       String endDate = eDataTime_YYYY + "-" + eDataTime_MM + '-' + eDataTime_DD;
        String startDate = jsonNode.get("startTime").asText();
        String endDate = jsonNode.get("endTime").asText();
        beans.put("startDate", startDate);
        beans.put("endDate", endDate);
        String printDate = dateFormater.format(now.getTime());
        beans.put("printDate", printDate);
        UserObject userObj = UserInstance.getInstance().getUserObject(httpServletRequest);
        beans.put("userName", userObj.getUserName());

        ArrayList<LineServiceObj> vo = new ArrayList<LineServiceObj>();

        try {

            conn = ds.getConnection();

            sqlQuery =
                    "SELECT LM.ID, LM.REPORTNAME, LM.ODR, COUNT(1) CNT " +
                            "FROM LINE_MENU LM, LINE_MENU_RANK LMR " +
                            "WHERE LM.ID = LMR.MENUID " +
                            "AND LMR.CREATETIME >= ? " +
                            "AND LMR.CREATETIME <= ? " +
                            "GROUP BY LM.ID, LM.REPORTNAME, LM.ODR ORDER BY ODR";

            pstmt = conn.prepareStatement(sqlQuery);
            int i = 0;
            pstmt.setString(++i, startDate + " 00:00:00");
            pstmt.setString(++i, endDate + " 23:59:59");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LineServiceObj obj = new LineServiceObj();
                obj.setKeyWord(rs.getString("REPORTNAME"));

                obj.setCount(rs.getLong("CNT"));
                vo.add(obj);
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

//            sqlQuery = "SELECT COUNT(1) CNT FROM LINE_ROOM_ONLINE " +
//                    "WHERE CREATETIME >= ? " +
//                    "AND CREATETIME <= ?";

            sqlQuery = "SELECT sum(chattingCalls) CNT FROM CallLog1 " +
                    "WHERE [dateTime] >= ? " +
                    "AND [dateTime] <= ?";


            pstmt = conn.prepareStatement(sqlQuery);
            i = 0;
            pstmt.setString(++i, startDate + " 00:00:00");
            pstmt.setString(++i, endDate + " 23:59:59");
            rs = pstmt.executeQuery();
            if (rs.next()) {
                LineServiceObj obj = new LineServiceObj();
                obj.setKeyWord("其他(轉服務人員)");

                obj.setCount(rs.getLong("CNT"));
                vo.add(obj);
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            beans.put("vo", vo);

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

