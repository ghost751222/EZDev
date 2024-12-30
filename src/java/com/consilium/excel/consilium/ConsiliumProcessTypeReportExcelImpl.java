package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.*;
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

@Service("ConsiliumProcessTypeReportExcelImpl")
public class ConsiliumProcessTypeReportExcelImpl implements ExcelInterface {
    private static final Logger logger = Logger.getLogger(ConsiliumProcessTypeReportExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "業務服務類別統計表.xls";

    private String reportPath = "/WEB-INF/report/consilium/ProcessType.xls";

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sqlQuery = "";
        ArrayList<ProcessTypeReportObject> list = new ArrayList<ProcessTypeReportObject>();
        NumberFormat format = NumberFormat.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();
        String dateType = jsonNode.get("dateType").asText();

        format.setMinimumIntegerDigits(2);

        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();
        String printDate = dateFormater.format(now.getTime());

        Map beans = new HashMap();
        try {
//				sqlQuery =
//						" SELECT      												" +
//								" 	S.ITEMID, F.PROCESSTYPE, COUNT(1) AS AMOUNT             " +
//								" FROM                                                      " +
//								" 	CALLLOG_FORM F, CALLLOG_SERVICEITEM S                   " +
//								" WHERE                                                     " +
//								" 	F.ACTIONID = S.ACTIONID                                 " +
//								" AND                                                       " +
//								" 	F.CREATETIME > ?      " +
//								" AND                                                       " +
//								" 	F.CREATETIME <  ?     " +
//								" GROUP BY                                                  " +
//								" 	S.ITEMID, F.PROCESSTYPE                                 ";

            sqlQuery =
                    " SELECT      												" +
                            " 	F.SERVICEITEM ITEMID, F.PROCESSTYPE, COUNT(1) AS AMOUNT             " +
                            " FROM                                                      " +
                            " 	CALLLOG_FORM F    with(nolock)               " +
                            " WHERE                                                     " +
                            "   1=1                                " +
                            " AND                                                       " +
                            " 	F.CREATETIME > ?      " +
                            " AND                                                       " +
                            " 	F.CREATETIME <  ?     " +
                            " AND  F.PROCESSTYPE IS NOT NULL                                    " +

                            " and serviceType is not null  " +

                            //" and caseType is not null and caseType <> 'V'" +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(f.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(f.CREATETIME as date))) "
                                    : "")) +

                            " and callType is not null  " +
                            " And callType not in ('I','J') " +
                            " GROUP BY                                                  " +
                            " 	F.SERVICEITEM, F.PROCESSTYPE                                 ";

            conn = ds.getConnection();
            pstmt = conn.prepareStatement(sqlQuery);
            logger.info(" 業務服務類別統計表 sqlQuery =" + sqlQuery);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");
            rs = pstmt.executeQuery();
            Hashtable<String, Integer> valueTable = new Hashtable<String, Integer>();
            String key = "";
            while (rs.next()) {
                key = rs.getString("ITEMID") + "-" + rs.getString("PROCESSTYPE");
                valueTable.put(key, new Integer(rs.getInt("AMOUNT")));
            }
            rs.close();
            pstmt.close();

            ArrayList<ServiceTypeObject> typeList = ServiceInstance.getInstance().getTypeEnableList();
            Vector units = UnitCode.getInstance().getUnitList("M");
            boolean isHidden = false;
            for (ServiceTypeObject typeObj : typeList) {
                ArrayList<ServiceItemObject> itemList = ServiceInstance.getInstance().getTypeObject(typeObj.getTypeId()).getItemEnableList();
                for (ServiceItemObject iObj : itemList) {
                    if( iObj.getItemId().equals("10000") || iObj.getItemId().equals("10001") || iObj.getItemId().equals("10002") || iObj.getItemId().equals("10003") || iObj.getItemId().equals("10004") || iObj.getItemId().equals("10020")){
                        continue;
                    }
                    isHidden = false;
                    for (Object u: units) {
                        UnitCodeObject unitCodeObject = (UnitCodeObject) u;

                        if( unitCodeObject.getUnitCode().equals(iObj.getItemId())  && unitCodeObject.getSuperUnit().contains("_D")   ){
                            isHidden = true;
                            break;
                        }
                    }
                    if(isHidden) continue;
                    ProcessTypeReportObject obj = new ProcessTypeReportObject();
                    String typeId = typeObj.getTypeId();
                    if (typeId.equalsIgnoreCase("01") || typeId.equalsIgnoreCase("10")) {
                        obj.setTypeId(typeObj.getTypeId());
                        obj.setTypeName("地政地關");
                        obj.setItemId(iObj.getItemId());
                        obj.setItemName(iObj.getItemName());
                    } else {
                        obj.setTypeId(typeObj.getTypeId());
                        obj.setTypeName(typeObj.getTypeName());
                        obj.setItemId(iObj.getItemId());
                        obj.setItemName(iObj.getItemName());
                    }

                    int valueA = valueTable.get(obj.getItemId() + "-A") == null ? 0 : valueTable.get(obj.getItemId() + "-A");
                    int valueB = valueTable.get(obj.getItemId() + "-B") == null ? 0 : valueTable.get(obj.getItemId() + "-B");
                    int valueC = valueTable.get(obj.getItemId() + "-C") == null ? 0 : valueTable.get(obj.getItemId() + "-C");
                    int valueD = valueTable.get(obj.getItemId() + "-D") == null ? 0 : valueTable.get(obj.getItemId() + "-D");

                    obj.setValueA(valueA);
                    obj.setValueB(valueB);
                    obj.setValueC(valueC);
                    obj.setValueD(valueD);
                    list.add(obj);
                }
            }

            beans.put("list", list);
            beans.put("startDate", startDate);
            beans.put("endDate", endDate);
            beans.put("printDate", printDate);

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
