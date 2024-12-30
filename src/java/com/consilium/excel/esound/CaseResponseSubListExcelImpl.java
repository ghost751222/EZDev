package com.consilium.excel.esound;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.UserInstance;
import com.consilium.excel.models.UserObject;
import com.consilium.excel.models.caseresponse.CaseResponseDataObject;
import com.consilium.excel.models.caseresponse.CaseResponseSectionObject;
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
import java.util.*;

@Service("CaseResponseSubListExcelImpl")
public class CaseResponseSubListExcelImpl implements ExcelInterface {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        UserObject usrObj = UserInstance.getInstance().getUserObject(httpServletRequest);
        if (usrObj == null) {
            throw new Exception("您尚未登入或已登入逾時!");
        }

        //Initializations
        Map<String, Object> beans = new HashMap<String, Object>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs = null;
        String sqlQuery = ""; //暫存sql語法

        NumberFormat format = NumberFormat.getInstance(); //為了跑兩位數
        format.setMinimumIntegerDigits(2); //跑兩位數
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //格式化日期
        Calendar now = Calendar.getInstance(); //抓現在時間

        //parameters delivering
//        String bDataTime_YYYY = request.getParameter("bDataTime_YYYY");
//        String eDataTime_YYYY = request.getParameter("eDataTime_YYYY");
//        String bDataTime_MM = format.format(Long.valueOf(request.getParameter("bDataTime_MM")));
//        String bDataTime_DD = format.format(Long.valueOf(request.getParameter("bDataTime_DD")));
//        String eDataTime_MM = format.format(Long.valueOf(request.getParameter("eDataTime_MM")));
//        String eDataTime_DD = format.format(Long.valueOf(request.getParameter("eDataTime_DD")));
//        String unitCode = request.getParameter("mainUnitCode");

        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();

        String bDataTime_YYYY = startDate.substring(0, 4);
        String eDataTime_YYYY = endDate.substring(0, 4);
        String bDataTime_MM = startDate.substring(5, 7);
        String bDataTime_DD = startDate.substring(8, 10);
        String eDataTime_MM = endDate.substring(5, 7);
        String eDataTime_DD = endDate.substring(8, 10);
        String unitCode = jsonNode.get("unitCode").asText(null);
        if ("".equals(unitCode)) unitCode = null;
        //validation
        if (bDataTime_YYYY == null || bDataTime_MM == null || bDataTime_DD == null || eDataTime_YYYY == null ||
                eDataTime_MM == null || eDataTime_DD == null) {
            throw new Exception("無效的參數!");
        }
//        if (unitCode == null || unitCode.length() == 0) {
//            throw new Exception("無效的參數!");
//        }

        beans.put("startDate", startDate);
        beans.put("endDate", endDate);
        beans.put("printDate", dateFormater.format(now.getTime()));
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            conn = ds.getConnection();

            Hashtable<String, String> nameMappingTable = new Hashtable<String, String>();
            LinkedHashMap<String, CaseResponseSectionObject> UnitCodeMap = new LinkedHashMap<String, CaseResponseSectionObject>();

            //if (unitCode != null && unitCode.length() != 0) {
            String sql =
                    "SELECT UNITNAME, UNITCODE "
                            + "FROM UNITS "
                            + "WHERE  (SPUNITCODE = ? or  ? is null) "
                            + "AND ( (UNITCODE != ? or ? is null)  OR UNITCODE = 'E2001' OR UNITCODE = 'E2014') ";
            pstmt1 = conn.prepareStatement(sql);
            pstmt1.setString(1, unitCode);
            pstmt1.setString(2, unitCode);
            pstmt1.setString(3, unitCode);
            pstmt1.setString(4, unitCode);
            rs = pstmt1.executeQuery();
            // }
            while (rs.next()) {
                if (!UnitCodeMap.containsKey(rs.getString("UNITCODE"))) {
                    CaseResponseSectionObject tempObj = new CaseResponseSectionObject();
                    tempObj.setSectionName(rs.getString("UNITNAME"));
                    tempObj.setDataList(new ArrayList<CaseResponseDataObject>());
                    UnitCodeMap.put(rs.getString("UNITCODE"), tempObj);
                    nameMappingTable.put(rs.getString("UNITCODE"), rs.getString("UNITNAME"));
                }
            }
            sqlQuery =
                    "SELECT " +
                            " T.ACTIONID, T.RECEIVESTATUS, R.REPORTTYPE, T.RECEIVEUNITTYPE, T.RECEIVEUNITCODE, U.SUPERUNIT, U.SPUNITCODE, U.UNITCODE,  R.CREATETIME," +
                            " ISNULL (T.RECEIVETIME, getdate()) RECEIVETIME, " + " ISNULL (R.CREATETIME, getdate()) RESPONSETIME " + "FROM " +
                            " (CASE_FLOW T LEFT JOIN " + " (SELECT" + "      ACTIONID, " + "      CREATETIME, " +
                            "      REPORTTYPE, " + "      RESPONSEUNITTYPE, " + "      RESPONSEUNITCODE " + "  FROM " + "  (" +
                            "       SELECT " + "       ACTIONID, " + "       CREATETIME, " + "       REPORTTYPE, " +
                            "       RESPONSEUNITTYPE, " + "       RESPONSEUNITCODE, " +
                            "      row_number() over (partition by ACTIONID, RESPONSEUNITTYPE, RESPONSEUNITCODE order by CREATETIME ASC) rnk " +
                            "       From CASE_RESPONSE_RECORD " + "       WHERE REPORTTYPE IN ('F','G','H') " + "       ) a" +
                            "   WHERE rnk = 1) R " +
                            " ON (T.ACTIONID = R.ACTIONID AND T.RECEIVEUNITTYPE = R.RESPONSEUNITTYPE AND T.RECEIVEUNITCODE = R.RESPONSEUNITCODE)) LEFT JOIN UNITS U ON (T.RECEIVEUNITTYPE = U.UNITTYPE AND T.RECEIVEUNITCODE = U.UNITCODE) " +
                            "WHERE T.RECEIVESTATUS IN ('D', 'E', 'G', 'H', 'I', 'J', 'K') " + "AND " +
                            "    T.APPROVENTIME >= ? " + "AND " +
                            "    T.APPROVENTIME <= ? " + //"AND " + " R.CREATETIME IS NOT NULL " +
                            "  AND T.FORMTYPE = 'CG' " +
                            "  AND (U.SPUNITCODE = ? or ? is null)" +
                            "ORDER BY U.SUPERUNIT,T.RECEIVETIME ";

            pstmt = conn.prepareStatement(sqlQuery);
            int i = 0;
            pstmt.setString(++i, startDate + " 00:00:00");
            pstmt.setString(++i, endDate + " 23:59:59");
            pstmt.setString(++i, unitCode);
            pstmt.setString(++i, unitCode);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String wUnitCode = rs.getString("UNITCODE");
                String wActionId = rs.getString("ACTIONID");
                String wReceiveTime = formater.format(rs.getTimestamp("RECEIVETIME"));
                String wResponseTime = formater.format(rs.getTimestamp("RESPONSETIME"));
                String wSectionName = (String) nameMappingTable.get(wUnitCode);
                String wCreateTime = rs.getString("CREATETIME");
                CaseResponseSectionObject sectionObj = UnitCodeMap.get(wUnitCode);
                if (sectionObj == null) {
                    sectionObj = new CaseResponseSectionObject();
                }

                ArrayList<CaseResponseDataObject> dataList = sectionObj.getDataList();
                if (dataList == null) {
                    dataList = new ArrayList<CaseResponseDataObject>();
                }

                CaseResponseDataObject dataObj = new CaseResponseDataObject();
                if (wCreateTime != null) {
                    dataObj.setActionId(wActionId);
                    dataObj.setReceiveTime(wReceiveTime);
                    dataObj.setResponseTime(wResponseTime);
                } else {
                    dataObj.setActionId1(wActionId);
                    dataObj.setReceiveTime1(wReceiveTime);
                }

                dataList.add(dataObj);
                sectionObj.setDataList(dataList);
                sectionObj.setSectionName(wSectionName);
                UnitCodeMap.put(wUnitCode, sectionObj);
            }

            ArrayList<CaseResponseSectionObject> sectionList = new ArrayList<CaseResponseSectionObject>();
//            Enumeration<String> e = UnitCodeMap.entrySet();
//            while (e.hasMoreElements()) {
//                String key = (String) e.nextElement();
//                if (UnitCodeMap.get(key) == null) {
//                    CaseResponseSectionObject tempObj = UnitCodeMap.get(key);
//                    tempObj.setSectionName(nameMappingTable.get(key));
//                    tempObj.setDataList(new ArrayList<CaseResponseDataObject>());
//                    UnitCodeMap.put(key, tempObj);
//                }
//                sectionList.add(UnitCodeMap.get(key));
//            }

            Set<String> keys = UnitCodeMap.keySet();

            for (String key : keys) {
                if (UnitCodeMap.get(key) == null) {
                    CaseResponseSectionObject tempObj = UnitCodeMap.get(key);
                    tempObj.setSectionName(nameMappingTable.get(key));
                    tempObj.setDataList(new ArrayList<CaseResponseDataObject>());
                    UnitCodeMap.put(key, tempObj);
                }
                sectionList.add(UnitCodeMap.get(key));

            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;
            pstmt1.close();
            pstmt1 = null;

            conn.close();
            conn = null;

            beans.put("sectionList", sectionList);
            String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/CaseResponseSubList.xls");
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
        } catch (SQLException e) {
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
                if (pstmt1 != null) {
                    pstmt1.close();
                    pstmt1 = null;
                }
            } catch (SQLException ex) {
                throw new SQLException("資料庫錯誤:" + ex.getMessage());
            }
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException ex) {
                throw new SQLException("資料庫錯誤:" + ex.getMessage());
            }
        }

    }
}
