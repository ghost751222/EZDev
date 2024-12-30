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
 * OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR DISTRIBUTION
 * OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES ARISING OUT OF
 * OR RESULTING FROM THE USE, MODIFICATION, OR DISTRIBUTION OF PROGRAMS
 * OR FILES CREATED FROM, BASED ON, AND/OR DERIVED FROM THIS SOURCE
 * CODE FILE.
 *
 *
 *     File name:       ConsiliumSatisfactionListExcelImpl.java
 *
 *     History:
 *     Date               Author                  Comments
 *     -----------------------------------------------------------------------
 *     Mar 18, 2013       Berton                  Initial Release
 *****************************************************************************/
package com.consilium.excel.esound;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.SatisfactionListObject;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * <code>SatisfactionListModel</code>實作ExcelReportInterface, 產生滿意度調查清單所需物件
 * 在此僅列出STATUS為2(未完成),3(已完成)的資料,
 * QUESTION1, QUESTION2, QUESTION3, QUESTION4欄位資料若為-1則顯示空白
 **/
@Service("SatisfactionListExcelImpl")
public class SatisfactionListExcelImpl implements ExcelInterface {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {
        {
            Map beans = null;
            String bDataTime_YYYY = null;
            String bDataTime_MM = null;
            String bDataTime_DD = null;
            String eDataTime_YYYY = null;
            String eDataTime_MM = null;
            String eDataTime_DD = null;


            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            String sql = null;

            ArrayList<SatisfactionListObject> objList = new ArrayList<SatisfactionListObject>();
            SatisfactionListObject obj = null;

            String bDataDate = jsonNode.get("time").get("startTime").asText();
            String eDataDate = jsonNode.get("time").get("endTime").asText();


            String bDataTime = bDataDate + " 00:00:00";
            String eDataTime = eDataDate + " 23:59:59";

            Calendar now = Calendar.getInstance(); //抓現在時間
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //格式化日期

            try {

                conn = ds.getConnection();
                sql = "SELECT IVRTIME, AGENTNAME, CASEID, QUESTION1, QUESTION2, QUESTION3, QUESTION4 FROM M7480_IVRCALLLOGS " +
                        "WHERE STATUS IN ('2','3') AND ? <= IVRTIME " +
                        "AND ?>= IVRTIME";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, bDataTime);
                pstmt.setString(2, eDataTime);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    obj = new SatisfactionListObject();
                    obj.setIvrTime(rs.getString("IVRTIME"));
                    obj.setAgentName(rs.getString("AGENTNAME"));
                    obj.setCaseId(rs.getString("CASEID"));
                    obj.setQuestion1("-1".equals(rs.getString("QUESTION1")) ? "" : rs.getString("QUESTION1"));
                    obj.setQuestion2("-1".equals(rs.getString("QUESTION2")) ? "" : rs.getString("QUESTION2"));
                    obj.setQuestion3("-1".equals(rs.getString("QUESTION3")) ? "" : rs.getString("QUESTION3"));
                    obj.setQuestion4("-1".equals(rs.getString("QUESTION4")) ? "" : rs.getString("QUESTION4"));
                    objList.add(obj);
                }
                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;
                conn.close();
                conn = null;

                beans = new HashMap();
                beans.put("startTime", bDataDate);
                beans.put("endTime", eDataDate);
                beans.put("printTime", dateFormater.format(now.getTime()));
                beans.put("objList", objList);
                beans.put("objListSize", objList.size());

                String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/SatisfactionList.xls");
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
