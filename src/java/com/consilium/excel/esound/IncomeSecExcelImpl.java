package com.consilium.excel.esound;

/****************************************************************************
 *
 * Copyright (c) 2013 ESound Tech. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by ESound as part
 * of a ESound product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of ESound.
 *
 * USE OF THIS SOFTWARE IS GOVERNED BY THE TERMS AND CONDITIONS
 * OF THE LICENSE STATEMENT AND LIMITED WARRANTY FURNISHED WITH
 * THE PRODUCT.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD ESOUND, ITS RELATED
 * COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST ANY CLAIMS
 * OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR DIStrIBUTION
 * OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES ARISING OUT OF
 * OR RESULTING FROM THE USE, MODIFICATION, OR DIStrIBUTION OF PROGRAMS
 * OR FILES CREATED FROM, BASED ON, AND/OR DERIVED FROM THIS SOURCE
 * CODE FILE.
 *
 *
 *     File name:       ReportModel.java
 *
 *     History:
 *     Date             Author              Comments
 *     -----------------------------------------------------------------------
 *     Nov 12, 2013     Kevin              Initial Release
 ****************************************************************************/


import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.incomesec.ReportData;
import com.consilium.excel.models.incomesec.ReportObj;
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


@Service("IncomeSecExcelImpl")
public class IncomeSecExcelImpl implements ExcelInterface {




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

//        String bDataTime_YYYY = String.valueOf((Integer.parseInt(request.getParameter("bDataTime_YYYY"))));
//        String bDataTime_MM = String.valueOf((format.format(Long.valueOf(request.getParameter("bDataTime_MM")))));
//        String bDataTime_DD = String.valueOf((format.format(Long.valueOf(request.getParameter("bDataTime_DD")))));
//
//        String bDataTime = bDataTime_YYYY+bDataTime_MM+bDataTime_DD;
//
//        String eDataTime_YYYY = String.valueOf((Integer.parseInt(request.getParameter("eDataTime_YYYY"))));
//        String eDataTime_MM = String.valueOf((format.format(Long.valueOf(request.getParameter("eDataTime_MM")))));
//        String eDataTime_DD = String.valueOf((format.format(Long.valueOf(request.getParameter("eDataTime_DD")))));
//
//        String eDataTime = eDataTime_YYYY+eDataTime_MM+eDataTime_DD;
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
                sqlQuery = "select datepart(yyyy,calltime) ystr, " +
                        "case when callclid > '9' then '手機' else '市話' end type, " +
                        "count(1) cnt, " +
                        "sum(callwaitingduration + calltalkingduration) ctime " +
                        "from M7480_CALL t " +
                        "where calltime >= ? " +
                        "and calltime <= ? " +
                        "group by  datepart(yyyy,calltime), case when callclid > '9' then '手機' else '市話' end " +
                        "order by  datepart(yyyy,calltime), case when callclid > '9' then '手機' else '市話' end ";
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

                String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/IncomeSec.xls");
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

