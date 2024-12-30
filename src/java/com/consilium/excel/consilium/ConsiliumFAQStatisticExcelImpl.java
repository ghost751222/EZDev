
package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.FAQStatisticObject;
import com.consilium.excel.models.UnitCode;
import com.consilium.excel.models.UnitCodeObject;
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

@Service("ConsiliumFAQStatisticExcelImpl")
public class ConsiliumFAQStatisticExcelImpl implements ExcelInterface {
    private static final Logger logger = Logger.getLogger(ConsiliumFAQStatisticExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "FAQ統計表.xls";

    private String reportPath = "/WEB-INF/report/consilium/FAQReport.xls";

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {
        {

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
            String endDate = jsonNode.get("time").get("endTime").asText();
            FAQStatisticObject obj;
            ArrayList list = new ArrayList();
            Hashtable dataTable = new Hashtable();
            UnitCodeObject uObj;

            try {

                conn = ds.getConnection();
                sql =
                        " SELECT                                                        " +
                                "       UNITID,                                                 " +
                                "       SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))<0 AND  (DATEDIFF(day,EFFICIENTTIME, getdate()))<-30 THEN 1 ELSE 0 END) STATUSE," +
                                "       SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))>0 THEN 1 ELSE 0 END) STATUSO, " +
                                "       COUNT(SID) AMOUNT                                       " +
                                " FROM                                                          " +
                                "       QUESTION_ANSWER                                         " +
                                " WHERE                                                         " +
                                "       CREATETIME>=?         " +
                                " AND                                                           " +
                                "       CREATETIME<=?         " +
                                " AND                                                           " +
                                "       UNITID<>'E2000'                                         " +
                                " GROUP BY                                                      " +
                                "       UNITID                                                  ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, startDate + " 00:00:00");
                pstmt.setString(2, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    obj = new FAQStatisticObject();
                    obj.setUnitCode(rs.getString("UNITID"));
                    obj.setUnitName(UnitCode.getInstance().getUnitName("M", rs.getString("UNITID")));
                    obj.setLeftAmount(rs.getInt("AMOUNT"));
                    obj.setLeftEAmount(rs.getInt("STATUSE"));
                    obj.setLeftOAmount(rs.getInt("STATUSO"));
                    dataTable.put(obj.getUnitCode(), obj);
                }
                rs.close();
                pstmt.close();

                sql =
                        " SELECT                                                        " +
                                "       UNITID,                                                 " +
                                "       SUM(CASE WHEN STATUS='D' THEN 1 ELSE 0 END) STATUSD,    " +
                                "       COUNT(SID) AMOUNT                                       " +
                                " FROM                                                          " +
                                "       QUESTION_ANSWER                                         " +
                                " WHERE                                                         " +
                                "       CLOSEDATATIME>=?      " +
                                " AND                                                           " +
                                "       CLOSEDATATIME<=?      " +
                                " AND                                                           " +
                                "       UNITID<>'E2000'                                         " +
                                " GROUP BY                                                      " +
                                "       UNITID                                                  ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, startDate + " 00:00:00");
                pstmt.setString(2, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (dataTable.containsKey(rs.getString("UNITID"))) {
                        obj = (FAQStatisticObject) dataTable.get(rs.getString("UNITID"));
                        obj.setLeftDAmount(rs.getInt("STATUSD"));
                    } else {
                        obj = new FAQStatisticObject();
                        obj.setUnitCode(rs.getString("UNITID"));
                        obj.setLeftDAmount(rs.getInt("STATUSD"));
                        dataTable.put(obj.getUnitCode(), obj);
                    }
                }
                rs.close();
                pstmt.close();

                sql =
                        " SELECT                                                                                                                                   " +
                                "       SECTIONID,                                                             " +
                                "       SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))<0 AND  (DATEDIFF(day,EFFICIENTTIME, getdate()))>-30 THEN 1 ELSE 0 END) STATUSE," +
                                "       SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))>0 THEN 1 ELSE 0 END) STATUSO, " +
                                "       COUNT(*)  AS AMOUNT                                                    " +
                                " FROM                                                                         " +
                                "       QUESTION_ANSWER                                                        " +
                                " WHERE                                                                        " +
                                "       UNITID='E2000'                                                         " +
                                " AND                                                                          " +
                                "       CREATETIME>=?                        " +
                                " AND                                                                          " +
                                "       CREATETIME<=?                        " +
                                " GROUP BY                                                                     " +
                                "       SECTIONID                                                              ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, startDate + " 00:00:00");
                pstmt.setString(2, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    uObj = UnitCode.getInstance().getUnitCodeObject("M", rs.getString("SECTIONID"));
                    if (dataTable.containsKey(uObj.getSpUnitCode())) {
                        obj = (FAQStatisticObject) dataTable.get(uObj.getSpUnitCode());
                        obj.setLeftAmount(obj.getLeftAmount() + rs.getInt("AMOUNT"));
                        obj.setLeftEAmount(obj.getLeftEAmount() + rs.getInt("STATUSE"));
                        obj.setLeftOAmount(obj.getLeftOAmount() + rs.getInt("STATUSO"));
                    } else {
                        obj = new FAQStatisticObject();
                        obj.setUnitCode(uObj.getSpUnitCode());
                        obj.setUnitName(UnitCode.getInstance().getUnitName("M", uObj.getSpUnitCode()));
                        obj.setLeftAmount(rs.getInt("AMOUNT"));
                        obj.setLeftEAmount(rs.getInt("STATUSE"));
                        obj.setLeftOAmount(rs.getInt("STATUSO"));
                        dataTable.put(obj.getUnitCode(), obj);
                    }
                }
                rs.close();
                pstmt.close();

                sql =
                        " SELECT                                                                       " +
                                "       SECTIONID,                                                             " +
                                "       SUM(CASE WHEN STATUS='D' THEN 1 ELSE 0 END) STATUSD,                   " +
                                "       COUNT(*)  AS AMOUNT                                                    " +
                                " FROM                                                                         " +
                                "       QUESTION_ANSWER                                                        " +
                                " WHERE                                                                        " +
                                "       UNITID='E2000'                                                         " +
                                " AND                                                                          " +
                                "       CLOSEDATATIME>=?                     " +
                                " AND                                                                          " +
                                "       CLOSEDATATIME<=?                     " +
                                " GROUP BY                                                                     " +
                                "       SECTIONID                                                              ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, startDate + " 00:00:00");
                pstmt.setString(2, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    uObj = UnitCode.getInstance().getUnitCodeObject("M", rs.getString("SECTIONID"));
                    if (dataTable.containsKey(uObj.getSpUnitCode())) {
                        obj = (FAQStatisticObject) dataTable.get(uObj.getSpUnitCode());
                        obj.setLeftDAmount(obj.getLeftDAmount() + rs.getInt("STATUSD"));
                    } else {
                        obj = new FAQStatisticObject();
                        obj.setUnitCode(uObj.getSpUnitCode());
                        obj.setUnitName(UnitCode.getInstance().getUnitName("M", uObj.getSpUnitCode()));
                        obj.setLeftDAmount(rs.getInt("STATUSD"));
                        dataTable.put(obj.getUnitCode(), obj);
                    }
                }
                rs.close();
                pstmt.close();

                //===================

                sql =
                        " SELECT                                                                                                    " +
                                "     UNITID,                                               " +
                                "     SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))<0 AND  (DATEDIFF(day,EFFICIENTTIME, getdate()))>-30 THEN 1 ELSE 0 END) STATUSE," +
                                "     SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))>0 THEN 1 ELSE 0 END) STATUSO, " +
                                "     COUNT(SID) AMOUNT                                     " +
                                " FROM                                                      " +
                                "     QUESTIONANSWER_RELATION                               " +
                                " WHERE                                                     " +
                                "     CREATETIME>=?       " +
                                " AND                                                       " +
                                "     CREATETIME<=?       " +
                                " AND                                                       " +
                                "     UNITID<>'E2000'                                       " +
                                " GROUP BY                                                  " +
                                "     UNITID                                                ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, startDate + " 00:00:00");
                pstmt.setString(2, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (dataTable.containsKey(rs.getString("UNITID"))) {
                        obj = (FAQStatisticObject) dataTable.get(rs.getString("UNITID"));
                        obj.setRightAmount(rs.getInt("AMOUNT"));
                        obj.setRightEAmount(rs.getInt("STATUSE"));
                        obj.setRightOAmount(rs.getInt("STATUSO"));
                    } else {
                        obj = new FAQStatisticObject();
                        obj.setUnitCode(rs.getString("UNITID"));
                        obj.setRightAmount(rs.getInt("AMOUNT"));
                        obj.setRightEAmount(rs.getInt("STATUSE"));
                        obj.setRightOAmount(rs.getInt("STATUSO"));
                        dataTable.put(obj.getUnitCode(), obj);
                    }

                }
                rs.close();
                pstmt.close();

                sql =
                        " SELECT                                                    " +
                                "     UNITID,                                               " +
                                "     SUM(CASE WHEN STATUS='D' THEN 1 ELSE 0 END) STATUSD,  " +
                                "     COUNT(SID) AMOUNT                                     " +
                                " FROM                                                      " +
                                "     QUESTIONANSWER_RELATION                               " +
                                " WHERE                                                     " +
                                "     CLOSEDATATIME>=?    " +
                                " AND                                                       " +
                                "     CLOSEDATATIME<=?    " +
                                " AND                                                       " +
                                "     UNITID<>'E2000'                                       " +
                                " GROUP BY                                                  " +
                                "     UNITID                                                ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, startDate + " 00:00:00");
                pstmt.setString(2, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (dataTable.containsKey(rs.getString("UNITID"))) {
                        obj = (FAQStatisticObject) dataTable.get(rs.getString("UNITID"));
                        obj.setRightDAmount(rs.getInt("STATUSD"));
                    } else {
                        obj = new FAQStatisticObject();
                        obj.setUnitCode(rs.getString("UNITID"));
                        obj.setRightDAmount(rs.getInt("STATUSD"));
                        dataTable.put(obj.getUnitCode(), obj);
                    }

                }
                rs.close();
                pstmt.close();

                sql =
                        " SELECT                                                                            " +
                                "       QR.UNITID,COUNT(QO.SID) AMOUNT                                              " +
                                " FROM                                                                              " +
                                "       QUESTIONANSWER_RELATION QR, QUESTIONANSWER_OPINION QO                       " +
                                " WHERE                                                                             " +
                                "       QR.SID=QO.PID                                                               " +
                                " AND                                                                               " +
                                "       QO.VISIBLE='Y'                                                              " +
                                " AND                                                                               " +
                                "       QR.UNITID<>'E2000'                                                          " +
                                " AND                                                                               " +
                                "       QO.CREATETIME>=?                          " +
                                " AND                                                                               " +
                                "       QO.CREATETIME<=?                          " +
                                " GROUP BY QR.UNITID                                                                ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, startDate + " 00:00:00");
                pstmt.setString(2, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (dataTable.containsKey(rs.getString("UNITID"))) {
                        obj = (FAQStatisticObject) dataTable.get(rs.getString("UNITID"));
                        obj.setOpinionAmount(rs.getInt("AMOUNT"));
                    } else {
                        obj = new FAQStatisticObject();
                        obj.setUnitCode(rs.getString("UNITID"));
                        obj.setOpinionAmount(rs.getInt("AMOUNT"));
                        dataTable.put(obj.getUnitCode(), obj);
                    }
                }

                rs.close();
                pstmt.close();

                sql =
                        " SELECT                                                                                                                                   " +
                                "       SECTIONID,                                                             " +
                                "       SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))<0 AND  (DATEDIFF(day,EFFICIENTTIME, getdate()))>-30 THEN 1 ELSE 0 END) STATUSE," +
                                "       SUM(CASE WHEN EFFICIENTTIME IS NOT NULL AND (DATEDIFF(day,EFFICIENTTIME, getdate()))>0 THEN 1 ELSE 0 END) STATUSO, " +
                                "       COUNT(*)  AS AMOUNT                                                    " +
                                " FROM                                                                         " +
                                "       QUESTIONANSWER_RELATION                                                " +
                                " WHERE                                                                        " +
                                "       UNITID='E2000'                                                         " +
                                " AND                                                                          " +
                                "       CREATETIME>=?                        " +
                                " AND                                                                          " +
                                "       CREATETIME<=?                        " +
                                " GROUP BY                                                                     " +
                                "       SECTIONID                                                              ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, startDate + " 00:00:00");
                pstmt.setString(2, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    uObj = UnitCode.getInstance().getUnitCodeObject("M", rs.getString("SECTIONID"));
                    if (dataTable.containsKey(uObj.getSpUnitCode())) {

                        obj = (FAQStatisticObject) dataTable.get(uObj.getSpUnitCode());
                        obj.setRightAmount(obj.getRightAmount() + rs.getInt("AMOUNT"));
                        obj.setRightEAmount(obj.getRightEAmount() + rs.getInt("STATUSE"));
                        obj.setRightOAmount(obj.getRightOAmount() + rs.getInt("STATUSO"));
                    } else {
                        obj = new FAQStatisticObject();
                        obj.setUnitCode(uObj.getSpUnitCode());
                        obj.setUnitName(UnitCode.getInstance().getUnitName("M", uObj.getSpUnitCode()));
                        obj.setRightAmount(rs.getInt("AMOUNT"));
                        obj.setRightEAmount(rs.getInt("STATUSE"));
                        obj.setRightOAmount(rs.getInt("STATUSO"));
                        dataTable.put(obj.getUnitCode(), obj);
                    }
                }

                rs.close();
                pstmt.close();

                sql =
                        " SELECT                                                                                                                                   " +
                                "       SECTIONID,                                                             " +
                                "       SUM(CASE WHEN STATUS='D' THEN 1 ELSE 0 END) STATUSD,                   " +
                                "       COUNT(*)  AS AMOUNT                                                    " +
                                " FROM                                                                         " +
                                "       QUESTIONANSWER_RELATION                                                " +
                                " WHERE                                                                        " +
                                "       UNITID='E2000'                                                         " +
                                " AND                                                                          " +
                                "       CLOSEDATATIME>=?                     " +
                                " AND                                                                          " +
                                "       CLOSEDATATIME<=?                     " +
                                " GROUP BY                                                                     " +
                                "       SECTIONID                                                              ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, startDate + " 00:00:00");
                pstmt.setString(2, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    uObj = UnitCode.getInstance().getUnitCodeObject("M", rs.getString("SECTIONID"));
                    if (dataTable.containsKey(uObj.getSpUnitCode())) {
                        obj = (FAQStatisticObject) dataTable.get(uObj.getSpUnitCode());
                        obj.setRightDAmount(obj.getRightDAmount() + rs.getInt("STATUSD"));
                    } else {
                        obj = new FAQStatisticObject();
                        obj.setUnitCode(uObj.getSpUnitCode());
                        obj.setUnitName(UnitCode.getInstance().getUnitName("M", uObj.getSpUnitCode()));
                        obj.setRightDAmount(rs.getInt("STATUSD"));
                        dataTable.put(obj.getUnitCode(), obj);
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
                                "       QR.UNITID='E2000'                                                           " +
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
                    uObj = UnitCode.getInstance().getUnitCodeObject("M", rs.getString("SECTIONID"));
                    if (dataTable.containsKey(uObj.getSpUnitCode())) {
                        obj = (FAQStatisticObject) dataTable.get(uObj.getSpUnitCode());
                        obj.setOpinionAmount(obj.getOpinionAmount() + rs.getInt("AMOUNT"));
                    } else {
                        obj = new FAQStatisticObject();
                        obj.setUnitCode(uObj.getSpUnitCode());
                        obj.setUnitName(UnitCode.getInstance().getUnitName("M", uObj.getSpUnitCode()));
                        obj.setOpinionAmount(rs.getInt("AMOUNT"));
                        dataTable.put(obj.getUnitCode(), obj);
                    }

                }

                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;
                conn.close();
                conn = null;

                //28單位
                String[] unitCodeOrder = {
                        "ZZZ",        //1996
                        "A1000",      //民政司
                        "A2000",      //地政司
                        "A3000",      //總務司
                        //"B4000",      //營建署
                        "C1000",      //中央警察大學
                        "C2000",      //建築研究所
                        //"E1000",      //兒童局
                        //"E2001",      //土地重劃工程處
                        //"E2010",      //國土測繪中心
                        //"E2000A",      //土地重劃工程處
                        //"E2000B",      //國土測繪中心
                        "H3000",      //土地重劃工程處
                        "E2000",      //國土測繪中心
                        //"E3000",      //社政機關
                        "F1000",      //秘書室
                        "F2000",      //資訊中心
                        "F3000",      //政風處
                        //"F4000",      //國民年金監理會
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
                        //"F9000",      //訴願審議委員會
                        "G1000" ,      //部長信箱
                        "H1000",         //宗教及禮制司
                        "H2000",         //替代役訓練及管理中心

                        "H4000",         //國土管理署
                        "H5000",         //國家公園署

                };

                String[] unitArr1 = {
//                        "E2015",    //土地重劃工程處政風室
//                        "E2001",    //土地重劃工程處測量工程課
//                        "E2002",    //土地重劃工程處市地重劃工程課
//                        "E2004",    //土地重劃工程處鄉村更新建設課
//                        "E2003",    //土地重劃工程處農地重劃工程課
                        "H3001",	//	土地重劃工程處主計室
                        "H3002",	//	土地重劃工程處人事室
                        "H3003",	//	土地重劃工程處秘書室
                        "H3004",	//	土地重劃工程處測量工程課
                        "H3005",	//	土地重劃工程處市地重劃工程課
                        "H3006",	//	土地重劃工程處農地重劃工程課
                        "H3007",	//	土地重劃工程處鄉村更新建設課
                        "H3008",	//	土地重劃工程處政風室
                };

                String[] unitArr2 = {
                        "E2012",    //國土測繪中心會計室
                        "E2013",    //國土測繪中心人事室
                        "E2014",    //國土測繪中心秘書室
                        "E2005",    //國土測繪中心地籍測量課
                        "E2011",    //國土測繪中心測繪資訊課
                        "E2006",    //國土測繪中心地形及海洋測量課
                        "E2007",    //國土測繪中心地籍圖重測課
                        "E2008",    //國土測繪中心政風室
                        "E2009",    //國土測繪中心控制測量課
                        "E2010",    //國土測繪中心企劃課
                };

//                FAQStatisticObject dataobj1 = new FAQStatisticObject();
//                FAQStatisticObject tmpObj1;
//                dataobj1.setUnitName("土地重劃工程處");
//                dataobj1.setUnitCode("H3000");
//                for (int i = 0; i < unitArr1.length; i++) {
//                    tmpObj1 = (FAQStatisticObject) dataTable.get(unitArr1[i]);
//                    if (tmpObj1 != null) {
//                        dataobj1.setLeftAmount(dataobj1.getLeftAmount() + tmpObj1.getLeftAmount());
//                        dataobj1.setLeftDAmount(dataobj1.getLeftDAmount() + tmpObj1.getLeftDAmount());
//                        dataobj1.setLeftEAmount(dataobj1.getLeftEAmount() + tmpObj1.getLeftEAmount());
//                        dataobj1.setLeftOAmount(dataobj1.getLeftOAmount() + tmpObj1.getLeftOAmount());
//                        dataobj1.setLeftSAmount(dataobj1.getLeftSAmount() + tmpObj1.getLeftSAmount());
//
//                        dataobj1.setRightAmount(dataobj1.getRightAmount() + tmpObj1.getRightAmount());
//                        dataobj1.setRightDAmount(dataobj1.getRightDAmount() + tmpObj1.getRightDAmount());
//                        dataobj1.setRightEAmount(dataobj1.getRightEAmount() + tmpObj1.getRightEAmount());
//                        dataobj1.setRightOAmount(dataobj1.getRightOAmount() + tmpObj1.getRightOAmount());
//                        dataobj1.setRightSAmount(dataobj1.getRightSAmount() + tmpObj1.getRightSAmount());
//                        dataobj1.setOpinionAmount(dataobj1.getOpinionAmount() + tmpObj1.getOpinionAmount());
//                    }
//
//                }
//
//                dataTable.put("H3000", dataobj1);

//                FAQStatisticObject dataobj2 = new FAQStatisticObject();
//                FAQStatisticObject tmpObj2;
//                dataobj2.setUnitName("國土測繪中心");
//                dataobj2.setUnitCode("E2000");
//                for (int i = 0; i < unitArr2.length; i++) {
//                    tmpObj2 = (FAQStatisticObject) dataTable.get(unitArr2[i]);
//                    if (tmpObj2 != null) {
//                        dataobj2.setLeftAmount(dataobj2.getLeftAmount() + tmpObj2.getLeftAmount());
//                        dataobj2.setLeftDAmount(dataobj2.getLeftDAmount() + tmpObj2.getLeftDAmount());
//                        dataobj2.setLeftEAmount(dataobj2.getLeftEAmount() + tmpObj2.getLeftEAmount());
//                        dataobj2.setLeftOAmount(dataobj2.getLeftOAmount() + tmpObj2.getLeftOAmount());
//                        dataobj2.setLeftSAmount(dataobj2.getLeftSAmount() + tmpObj2.getLeftSAmount());
//
//                        dataobj2.setRightAmount(dataobj2.getRightAmount() + tmpObj2.getRightAmount());
//                        dataobj2.setRightDAmount(dataobj2.getRightDAmount() + tmpObj2.getRightDAmount());
//                        dataobj2.setRightEAmount(dataobj2.getRightEAmount() + tmpObj2.getRightEAmount());
//                        dataobj2.setRightOAmount(dataobj2.getRightOAmount() + tmpObj2.getRightOAmount());
//                        dataobj2.setRightSAmount(dataobj2.getRightSAmount() + tmpObj2.getRightSAmount());
//                        dataobj2.setOpinionAmount(dataobj2.getOpinionAmount() + tmpObj2.getOpinionAmount());
//
//                    }
//
//                }
//
//                dataTable.put("E2000", dataobj2);
                for (int i = 0; i < unitCodeOrder.length; i++) {
                    if (dataTable.containsKey(unitCodeOrder[i])) {
                        obj = (FAQStatisticObject) dataTable.get(unitCodeOrder[i]);
                        if ("ZZZ".equals(obj.getUnitCode())) {
                            obj.setUnitName("1996");
                        } else if ("H3000".equals(obj.getUnitCode())) {
                            obj.setUnitName("土地重劃工程處");
                        } else if ("E2000".equals(obj.getUnitCode())) {
                            obj.setUnitName("國土測繪中心");
                        } else {
                            obj.setUnitName(UnitCode.getInstance().getUnitName("M", unitCodeOrder[i]));
                        }
                        list.add(obj);
                    } else {
                        obj = new FAQStatisticObject();
                        obj.setUnitName(UnitCode.getInstance().getUnitName("M", unitCodeOrder[i]));
                        obj.setUnitCode(unitCodeOrder[i]);
                        if ("ZZZ".equals(obj.getUnitCode())) {
                            obj.setUnitName("1996");
                        } else if ("H3000".equals(obj.getUnitCode())) {
                            obj.setUnitName("土地重劃工程處");
                        } else if ("E2000".equals(obj.getUnitCode())) {
                            obj.setUnitName("國土測繪中心");
                        }
                        obj.setLeftAmount(0);
                        obj.setRightAmount(0);
                        list.add(obj);
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
}
