/****************************************************************************
 *
 * Copyright (c) 2014 ESound Tech. All Rights Reserved.
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
 *     File name:       SectionForecastSampleNumberExcelImpl.java
 *
 *     History:
 *     Date             Author              Comments
 *     -----------------------------------------------------------------------
 *     Sep 26, 2014     Vim                 Initial Release
 ****************************************************************************/

package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.UnitCode;
import com.consilium.excel.models.UnitCodeObject;
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
import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service("ConsiliumSectionForecastSampleNumberExcelImpl")
public class ConsiliumSectionForecastSampleNumberExcelImpl implements ExcelInterface {

    private static final Logger logger = Logger.getLogger(ConsiliumSectionForecastSampleNumberExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "業務職掌預估抽測通數表.xls";

    private String reportPath = "/WEB-INF/report/consilium/SectionForecastSampleNumber.xls";

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

            UserObject usrObj = UserInstance.getInstance().getUserObject(httpServletRequest);
            if (usrObj == null) {
                throw new Exception("您尚未登入或已登入逾時!");
            }

            Map beans = new HashMap();

            Connection conn = null;
            PreparedStatement pstmt = null;
            PreparedStatement pstmt1 = null;
            Statement stmt = null;
            ResultSet rs = null;

            NumberFormat format = NumberFormat.getInstance(); //為了跑兩位數
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //格式化日期
            Calendar now = Calendar.getInstance(); //抓現在時間

            format.setMinimumIntegerDigits(2);  //跑兩位數

            beans.put("printDate", dateFormater.format(now.getTime()));
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //28單位
            String[] unitCodeOrder = {
                    "A1000",      //民政司
                    "A2000",      //地政司
                    "A3000",      //總務司
                    //"B4000",      //營建署
                    "C1000",      //中央警察大學
                    "C2000",      //建築研究所
                    "E1000",      //兒童局
//                    "E2001",      //土地重劃工程處
//                    "E2014",      //國土測繪中心
                    "H3000",      //土地重劃工程處
                    "E2000",      //國土測繪中心
//                                  //"E3000",      //社政機關
                    "F1000",      //秘書室
                    "F2000",      //資訊中心
                    "F3000",      //政風處
                    "F4000",      //國民年金監理會
                    "A4000",      //戶政司
                    "A6000",      //社會司
                    "B1000",      //消防署
                    "B2000",      //入出國及移民署
                    "B3000",      //警政署
                    "B5000",      //役政署
                    "D1000",      //空勤總隊
                    //"E4000",      //家庭暴力及性侵害防治委員會
                    "F5000",      //人事處
                    "F6000",      //會計處
                    "F7000",      //統計處
                    "F8000",      //法規委員會
                   // "F9000",      //訴願審議委員會
                    "G1000",       //部長信箱
                    "H1000",         //宗教及禮制司
                    "H2000",         //替代役訓練及管理中心

                    "H4000",         //國土管理署
                    "H5000",         //國家公園署
            };

            Map countMap = new HashMap();
            int nowCount = 0;

            try {

                conn = ds.getConnection();

                String sqlSD =
                        " SELECT STRING_AGG(SD.CONNECTOR1 + ',' + SD.CONNECTOR2, ',') WITHIN GROUP (ORDER BY SD.CONNECTOR1 + ',' + SD.CONNECTOR2) NAMES FROM SECTION_DATA SD " +
                                " WHERE SD.UNITID = ? ";

                String sqlSD_SP_E2001 =
                        " SELECT STRING_AGG(SD.CONNECTOR1 + ',' + SD.CONNECTOR2, ',') WITHIN GROUP (ORDER BY SD.CONNECTOR1 + ',' + SD.CONNECTOR2) NAMES FROM SECTION_DATA SD " +
                                " WHERE SD.SECTIONID IN ( " + getUnitList("E2000", "E2001") + " ) ";

                String sqlSD_SP_E2014 =
                        " SELECT STRING_AGG(SD.CONNECTOR1 + ',' + SD.CONNECTOR2, ',') WITHIN GROUP (ORDER BY SD.CONNECTOR1 + ',' + SD.CONNECTOR2) NAMES FROM SECTION_DATA SD " +
                                " WHERE SD.SECTIONID IN ( " + getUnitList("E2000", "E2014") + " ) ";

                String sqlSB =
                        " SELECT STRING_AGG(SB.CONNECTOR, ',') WITHIN GROUP (ORDER BY SB.CONNECTOR) NAMES FROM SECTION_BUSINESS SB LEFT JOIN SECTION_DATA SD ON SB.PID = SD.SID " +
                                " WHERE SD.SECTIONID = ? ";

                pstmt = conn.prepareStatement(sqlSD);

                pstmt1 = conn.prepareStatement(sqlSB);

                stmt = conn.createStatement();

                for (int u = 0; u < unitCodeOrder.length; u++) {
                    if ("E2001".equals(unitCodeOrder[u])) {
                        rs = stmt.executeQuery(sqlSD_SP_E2001);
                    } else if ("E2014".equals(unitCodeOrder[u])) {
                        rs = stmt.executeQuery(sqlSD_SP_E2014);
                    } else {
                        pstmt = conn.prepareStatement(sqlSD);
                        pstmt.setString(1, unitCodeOrder[u]);
                        rs = pstmt.executeQuery();
                    }

                    String names = "";
                    int num = 0;

                    while (rs.next()) {
                        names = rs.getString("NAMES");
                    }

                    Map nameMap = new HashMap();
                    String[] nameList = names == null ? new String[0]: names.split(",");

                    for (int n = 0; n < nameList.length; n++) {
                        if (!nameMap.containsKey(nameList[n])) {
                            if (!"".equals(nameList[n].trim())) nameMap.put(nameList[n], nameList[n]);
                        }
                    }

                    num = nameMap.size();

                    rs.close();
                    rs = null;

                    String units = "";

                    if ("E2001".equals(unitCodeOrder[u])) {
                        units = getUnitList("E2000", "E2001");
                        String[] unitList = units.split(",");

                        for (int ul = 0; ul < unitList.length; ul++) {
                            names = "";
                            num = 0;

                            pstmt1 = conn.prepareStatement(sqlSB);
                            pstmt1.setString(1, unitList[ul].replace("'", ""));
                            rs = pstmt1.executeQuery();

                            while (rs.next()) {
                                names = rs.getString("NAMES");
                            }

                            if (names != null && !"".equals(names)) {
                                nameList = names.split(",");

                                for (int n = 0; n < nameList.length; n++) {
                                    if (!nameMap.containsKey(nameList[n])) {
                                        if (!"".equals(nameList[n].trim())) nameMap.put(nameList[n], nameList[n]);
                                    }
                                }
                            }

                            rs.close();
                            rs = null;
                            pstmt1.close();
                            pstmt1 = null;
                        }

                        num = nameMap.size();
                        if (countMap.containsKey(unitCodeOrder[u])) {
                            nowCount = num + (Integer) countMap.get(unitCodeOrder[u]);

                            countMap.put(unitCodeOrder[u], nowCount);

                            beans.put(unitCodeOrder[u] + "_COUNT", nowCount);
                            if (nowCount >= 10) beans.put(unitCodeOrder[u] + "_SAMPLE", 10);
                            else beans.put(unitCodeOrder[u] + "_SAMPLE", nowCount);
                        } else {
                            nowCount = num;

                            countMap.put(unitCodeOrder[u], nowCount);

                            beans.put(unitCodeOrder[u] + "_COUNT", nowCount);
                            if (nowCount >= 10) beans.put(unitCodeOrder[u] + "_SAMPLE", 10);
                            else beans.put(unitCodeOrder[u] + "_SAMPLE", nowCount);
                        }
                    } else if ("E2014".equals(unitCodeOrder[u])) {
                        units = getUnitList("E2000", "E2014");
                        String[] unitList = units.split(",");

                        for (int ul = 0; ul < unitList.length; ul++) {
                            names = "";
                            num = 0;

                            pstmt1 = conn.prepareStatement(sqlSB);
                            pstmt1.setString(1, unitList[ul].replace("'", ""));
                            rs = pstmt1.executeQuery();

                            while (rs.next()) {
                                names = rs.getString("NAMES");
                            }

                            if (names != null && !"".equals(names)) {
                                nameList = names.split(",");

                                for (int n = 0; n < nameList.length; n++) {
                                    if (!nameMap.containsKey(nameList[n])) {
                                        if (!"".equals(nameList[n].trim())) nameMap.put(nameList[n], nameList[n]);
                                    }
                                }
                            }

                            rs.close();
                            rs = null;
                            pstmt1.close();
                            pstmt1 = null;
                        }

                        num = nameMap.size();
                        if (countMap.containsKey(unitCodeOrder[u])) {
                            nowCount = num + (Integer) countMap.get(unitCodeOrder[u]);

                            countMap.put(unitCodeOrder[u], nowCount);

                            beans.put(unitCodeOrder[u] + "_COUNT", nowCount);
                            if (nowCount >= 10) beans.put(unitCodeOrder[u] + "_SAMPLE", 10);
                            else beans.put(unitCodeOrder[u] + "_SAMPLE", nowCount);
                        } else {
                            nowCount = num;

                            countMap.put(unitCodeOrder[u], nowCount);

                            beans.put(unitCodeOrder[u] + "_COUNT", nowCount);
                            if (nowCount >= 10) beans.put(unitCodeOrder[u] + "_SAMPLE", 10);
                            else beans.put(unitCodeOrder[u] + "_SAMPLE", nowCount);
                        }
                    } else {
                        units = getUnitList(unitCodeOrder[u]);
                        String[] unitList = units.split(",");

                        for (int ul = 0; ul < unitList.length; ul++) {
                            names = "";
                            num = 0;

                            pstmt1 = conn.prepareStatement(sqlSB);
                            pstmt1.setString(1, unitList[ul].replace("'", ""));
                            rs = pstmt1.executeQuery();

                            while (rs.next()) {
                                names = rs.getString("NAMES");
                            }

                            if (names != null && !"".equals(names)) {
                                nameList = names.split(",");

                                for (int n = 0; n < nameList.length; n++) {
                                    if (!nameMap.containsKey(nameList[n])) {
                                        if (!"".equals(nameList[n].trim())) nameMap.put(nameList[n], nameList[n]);
                                    }
                                }
                            }

                            rs.close();
                            rs = null;
                            pstmt1.close();
                            pstmt1 = null;
                        }

                        num = nameMap.size();
                        if (countMap.containsKey(unitCodeOrder[u])) {
                            nowCount = num + (Integer) countMap.get(unitCodeOrder[u]);

                            countMap.put(unitCodeOrder[u], nowCount);

                            beans.put(unitCodeOrder[u] + "_COUNT", nowCount);
                            if (nowCount >= 10) beans.put(unitCodeOrder[u] + "_SAMPLE", 10);
                            else beans.put(unitCodeOrder[u] + "_SAMPLE", nowCount);
                        } else {
                            nowCount = num;

                            countMap.put(unitCodeOrder[u], nowCount);

                            beans.put(unitCodeOrder[u] + "_COUNT", nowCount);
                            if (nowCount >= 10) beans.put(unitCodeOrder[u] + "_SAMPLE", 10);
                            else beans.put(unitCodeOrder[u] + "_SAMPLE", nowCount);
                        }
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
                    if (pstmt1 != null) {
                        pstmt1.close();
                        pstmt1 = null;
                    }
                    if (stmt != null) {
                        stmt.close();
                        stmt = null;
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

    private String getUnitList(String superUnit)
    {
        String result = "";

        ArrayList tmpList = UnitCode.getInstance().getMoiSubUnits(superUnit);
        UnitCodeObject unitCodeObj;

        String unitCode = "";

        if (tmpList != null) {
            for (int i = 0; i < tmpList.size(); i++) {
                unitCode = "";

                unitCodeObj = (UnitCodeObject) tmpList.get(i);

                unitCode = "'" + unitCodeObj.getUnitCode() + "'";

                if (i == 0) result += unitCode;
                else result += "," + unitCode;
            }
        }

        return result;
    }

    private String getUnitList (String superUnit, String spUnitCode)
    {
        String result = "";

        ArrayList tmpList = UnitCode.getInstance().getMoiSubUnitsBySpUnitCode(superUnit, spUnitCode);
        UnitCodeObject unitCodeObj;

        String unitCode = "";

        if (tmpList != null) {
            for (int i = 0; i < tmpList.size(); i++) {
                unitCode = "";

                unitCodeObj = (UnitCodeObject) tmpList.get(i);

                unitCode = "'" + unitCodeObj.getUnitCode() + "'";

                if (i == 0) result += unitCode;
                else result += "," + unitCode;
            }
        }

        return result;
    }

}
