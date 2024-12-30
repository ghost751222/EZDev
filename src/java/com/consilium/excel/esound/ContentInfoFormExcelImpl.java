package com.consilium.excel.esound;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.UnitCode;
import com.consilium.excel.models.contentinfoform.ReportObject;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.io.IOUtils;
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

@Service("ContentInfoFormExcelImpl")
public class ContentInfoFormExcelImpl implements ExcelInterface {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {


        Connection conn = null;
        ResultSet rs = null;
        String sql = "";
        PreparedStatement pstmt = null;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = dateFormater.format(now.getTime());
        Map<String, Object> beans = new HashMap<String, Object>();
//        String bDataTime_YYYY = request.getParameter("bDataTime_YYYY");
//        String eDataTime_YYYY = request.getParameter("eDataTime_YYYY");
//        String unitCode = request.getParameter("mainUnitCode");
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumIntegerDigits(2);
//        String bDataTime_MM = format.format(Long.valueOf(request.getParameter("bDataTime_MM")));
//        String bDataTime_DD = format.format(Long.valueOf(request.getParameter("bDataTime_DD")));
//        String eDataTime_MM = format.format(Long.valueOf(request.getParameter("eDataTime_MM")));
//        String eDataTime_DD = format.format(Long.valueOf(request.getParameter("eDataTime_DD")));
//        String startDate=bDataTime_YYYY + "-" + bDataTime_MM + '-' + bDataTime_DD;
//        String endDate=eDataTime_YYYY + "-" + eDataTime_MM + '-' + eDataTime_DD;
        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();

        ArrayList<ReportObject> list = new ArrayList<ReportObject>();

        try {


            conn = ds.getConnection();

            sql =
                    " SELECT   																										" +
                            " 	CF.*,                                                                                                       " +
                            " 	(SELECT MAX(RECEIVETIME) FROM CASE_FLOW C WHERE C.ACTIONID = CF.ACTIONID AND C.INPROCESS = 'Y') RECEIVETIME " +
                            " FROM                                                                                                          " +
                            " 	CONTENT_INFO_FORM CF                                                                                        " +
                            " WHERE                                                                                                         " +
                            " 	CREATETIME>=? AND CREATETIME<=?   ";


            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");

            rs = pstmt.executeQuery();
            ReportObject obj = null;
            while (rs.next()) {
                obj = new ReportObject();
                obj.setActionId(rs.getString("ACTIONID"));
                if (rs.getTimestamp("CREATETIME") != null)
                    obj.setCreateTime(formater.format(rs.getTimestamp("CREATETIME")));
                obj.setUnitName(UnitCode.getInstance().getUnitName("M", rs.getString("SUPERUNIT")));
                obj.setSectionName(UnitCode.getInstance().getUnitName("M", rs.getString("UNITCODE")));
                if ("B".equals(rs.getString("CONFIRMTYPE"))) {
                    obj.setIsOverTime("是");
                } else if ("A".equals(rs.getString("CONFIRMTYPE"))) {
                    obj.setIsOverTime("否");
                } else {
                    obj.setIsOverTime("處理中");
                }
                if (rs.getTimestamp("CONFIRMTIME") != null)
                    obj.setConfirmTime(formater.format(rs.getTimestamp("CONFIRMTIME")));
                if (rs.getTimestamp("LASTREPLYTIME") != null)
                    obj.setLastReplyTime(formater.format(rs.getTimestamp("LASTREPLYTIME")));
                if (rs.getTimestamp("RECEIVETIME") != null)
                    obj.setReceiveTime(formater.format(rs.getTimestamp("RECEIVETIME")));
                list.add(obj);
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;
            conn.close();
            conn = null;
            beans.put("list", list);
            beans.put("startDate", startDate);
            beans.put("endDate", endDate);
            beans.put("printDate", nowDate);

            String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/ContentInfoForm.xls");
            File file = new File(path);
            InputStream in = new FileInputStream(file);


            XLSTransformer transformer = new XLSTransformer();
            Workbook workbook = transformer.transformXLS(in, beans);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(outputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)))
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
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
