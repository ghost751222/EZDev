/****************************************************************************
*
* Copyright (c) 2018 ESound Tech. All Rights Reserved.
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
*     File name:       LineServiceExcelImpl.java
*
*     History:
*     Date               Author                  Comments
*     -----------------------------------------------------------------------
*     Nov 06, 2018       Jack                    Initial Release
*     Jan 23, 2019       Eric                    調整資料格式
*****************************************************************************/
package com.consilium.excel.esound;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.LineServiceObj;
import com.consilium.excel.models.UserInstance;
import com.consilium.excel.models.UserObject;

import com.fasterxml.jackson.databind.JsonNode;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Service("LineServiceExcelImpl")
public class LineServiceExcelImpl implements ExcelInterface
{

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;


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
       String startDate =jsonNode.get("startTime").asText();
       String endDate =jsonNode.get("endTime").asText();
       beans.put("startDate", startDate);
       beans.put("endDate", endDate);
       String printDate = dateFormater.format(now.getTime());
       beans.put("printDate", printDate);
       UserObject userObj = UserInstance.getInstance().getUserObject(httpServletRequest);
       beans.put("userName", userObj.getUserName());

       ArrayList<LineServiceObj> vo = new ArrayList<LineServiceObj>();
       
       try 
       {

           conn = ds.getConnection();

           sqlQuery =
               "SELECT LM.ID, LM.REPORTNAME, LM.ODR, COUNT(1) CNT "+
			   "FROM LINE_MENU LM, LINE_MENU_RANK LMR "+
               "WHERE LM.ID = LMR.MENUID " + 
               "AND LMR.CREATETIME >= ? "+
               "AND LMR.CREATETIME <= ? "+
               "GROUP BY LM.ID, LM.REPORTNAME, LM.ODR ORDER BY ODR";

           pstmt = conn.prepareStatement(sqlQuery);
           int i = 0;
           pstmt.setString(++i, startDate + " 00:00:00");
           pstmt.setString(++i, endDate + " 23:59:59");
           rs = pstmt.executeQuery();
           while (rs.next()) 
           {
        	   LineServiceObj obj = new LineServiceObj();
       		   obj.setKeyWord(rs.getString("REPORTNAME"));
        	   
        	   obj.setCount(rs.getLong("CNT"));
        	   vo.add(obj);
           }
           rs.close();
           rs = null;
           pstmt.close();
           pstmt = null;

           sqlQuery = "SELECT COUNT(1) CNT FROM LINE_ROOM_ONLINE "+
			   "WHERE CREATETIME >= ? "+
               "AND CREATETIME <= ?";

           pstmt = conn.prepareStatement(sqlQuery);
           i = 0;
           pstmt.setString(++i, startDate + " 00:00:00");
           pstmt.setString(++i, endDate + " 23:59:59");
           rs = pstmt.executeQuery();
           if(rs.next()) 
           {
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

           String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/LineService.xls");
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

