package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.UserInstance;
import com.consilium.excel.models.UserObject;
import com.consilium.excel.models.caseresponse.CaseResponseDataObject;
import com.consilium.excel.models.caseresponse.CaseResponseSectionObject;
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

@Service("ConsiliumCaseResponseSubListExcelImpl")
public class ConsiliumCaseResponseSubListExcelImpl implements ExcelInterface {

    private static final Logger logger = Logger.getLogger(ConsiliumCaseResponseSubListExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "案件處理狀態清單.xls";

    private String reportPath = "/WEB-INF/report/consilium/CaseResponseSubList.xls";

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
                            + " FROM UNITS "
//                            + "WHERE  (SPUNITCODE = ? or  ? is null) "
//                            + "AND ( (UNITCODE != ? or ? is null)  OR UNITCODE = 'E2001' OR UNITCODE = 'E2014') ";
                            + " WHERE  (SPUNITCODE in (SELECT value FROM   STRING_SPLIT (?, ',') ) or  ? is null OR SPUNITCODE = 'E2014') "
                            + " AND ( (UNITCODE not in (SELECT value FROM   STRING_SPLIT (?, ',') ) or ? is null)  OR UNITCODE = 'E2001' OR UNITCODE = 'E2014') "

                            + " AND (SUPERUNIT  not like '%_D' or SUPERUNIT is null) " ;
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
//            sqlQuery =
//                    "SELECT " +
//                            " T.ACTIONID, T.RECEIVESTATUS, R.REPORTTYPE, T.RECEIVEUNITTYPE, T.RECEIVEUNITCODE, U.SUPERUNIT, U.SPUNITCODE, U.UNITCODE,  R.CREATETIME," +
//                            " ISNULL (T.RECEIVETIME, getdate()) RECEIVETIME, " + " ISNULL (R.CREATETIME, getdate()) RESPONSETIME " + "FROM " +
//                            " (CASE_FLOW T LEFT JOIN " + " (SELECT" + "      ACTIONID, " + "      CREATETIME, " +
//                            "      REPORTTYPE, " + "      RESPONSEUNITTYPE, " + "      RESPONSEUNITCODE " + "  FROM " + "  (" +
//                            "       SELECT " + "       ACTIONID, " + "       CREATETIME, " + "       REPORTTYPE, " +
//                            "       RESPONSEUNITTYPE, " + "       RESPONSEUNITCODE, " +
//                            "      row_number() over (partition by ACTIONID, RESPONSEUNITTYPE, RESPONSEUNITCODE order by CREATETIME ASC) rnk " +
//                            "       From CASE_RESPONSE_RECORD " + "       WHERE REPORTTYPE IN ('F','G','H') " + "       ) a" +
//                            "   WHERE rnk = 1) R " +
//                            " ON (T.ACTIONID = R.ACTIONID AND T.RECEIVEUNITTYPE = R.RESPONSEUNITTYPE AND T.RECEIVEUNITCODE = R.RESPONSEUNITCODE)) LEFT JOIN UNITS U ON (T.RECEIVEUNITTYPE = U.UNITTYPE AND T.RECEIVEUNITCODE = U.UNITCODE) " +
//                            "WHERE T.RECEIVESTATUS IN ('D', 'E', 'G', 'H', 'I', 'J', 'K') " + "AND " +
//                            "    T.APPROVENTIME >= ? " + "AND " +
//                            "    T.APPROVENTIME <= ? " + //"AND " + " R.CREATETIME IS NOT NULL " +
//                            "  AND T.FORMTYPE = 'CG' " +
//                            //"  AND (U.SPUNITCODE = ? or ? is null)" +
//                            "  AND (U.SPUNITCODE in (SELECT value FROM   STRING_SPLIT (?, ',') ) or ? is null)" +
//                            "ORDER BY U.SUPERUNIT,T.RECEIVETIME ";

//            sqlQuery = " select c.SUPERUNIT ,  c.unitCode  as UNITCODE " +
//                    "  ,a.actionId as ACTIONID1, convert (varchar(20),a.responseTime,120) as RECEIVETIME1 " +
//                    "  ,b.actionId as ACTIONID, convert (varchar(20),b.recvTime,120)     as RECEIVETIME, convert(varchar(20),b.backTime,120) as RESPONSETIME " +
//
//                    " from UNITS as c left join view_1 as a on c.unitcode=a.serviceItem " +
//                    "                 left join view_2 as b on c.UNITCODE=b.serviceItem" +
//                    " where (a.responseTime > ? or b.recvTime > ?) and (a.responseTime < ? or b.recvTime < ?) ";

            sqlQuery = "  select c.SUPERUNIT , c.unitCode as '受理單位', a.ACTIONID as '案號1', convert (varchar(20), " +
                       " a.responseTime,120) as '接收時間1' , b.ACTIONID as '案號', convert (varchar(20), b.recvTime,120) as '接收時間', " +
                       " convert(varchar(20),b.backTime,120) as '回報時間' " +
                      "  from View_1 as a inner join View_2 as b on a.ACTIONID=b.ACTIONID " +
                       " left join UNITS as c on a.SERVICEITEM=c.UNITCODE " +
                    "   where a.responseTime > ? and a.responseTime  < ? ";



            pstmt = conn.prepareStatement(sqlQuery);
            logger.info("案件處理狀態清單 sqlQuery =" + sqlQuery);
            int i = 0;
            pstmt.setString(++i, startDate + " 00:00:00");
            pstmt.setString(++i, endDate + " 23:59:59");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String wSuperUnit = rs.getString("SUPERUNIT");
                if(wSuperUnit == null) continue;
                if(!unitCode.contains(wSuperUnit)) continue;
                String wUnitCode = rs.getString("受理單位");
                String wActionId = rs.getString("案號");
                String wReceiveTime = rs.getString("接收時間");
                String wResponseTime = rs.getString("回報時間");
                String wSectionName = (String) nameMappingTable.get(wUnitCode);

                String wActionId1 = rs.getString("案號1");
                String wReceiveTime1 = rs.getString("接收時間1");

                //String wCreateTime = rs.getString("CREATETIME");
                CaseResponseSectionObject sectionObj = UnitCodeMap.get(wUnitCode);
                if (sectionObj == null) {
                    sectionObj = new CaseResponseSectionObject();
                }

                ArrayList<CaseResponseDataObject> dataList = sectionObj.getDataList();
                if (dataList == null) {
                    dataList = new ArrayList<CaseResponseDataObject>();
                }

                CaseResponseDataObject dataObj = new CaseResponseDataObject();

                dataObj.setActionId(wActionId);
                dataObj.setReceiveTime(wReceiveTime);
                dataObj.setResponseTime(wResponseTime);

                dataObj.setActionId1(wActionId1);
                dataObj.setReceiveTime1(wReceiveTime1);

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
