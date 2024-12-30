package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.UnitCode;
import com.consilium.excel.models.UnitCodeObject;
import com.consilium.excel.models.UserInstance;
import com.consilium.excel.models.UserObject;
import com.consilium.excel.models.window.WinDataObject;
import com.consilium.service.BaseService;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.jxls.transformer.XLSTransformer;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service("ConsiliumSectionSuperManagerReportExcelImpl")
public class ConsiliumSectionSuperManagerReportExcelImpl extends BaseService implements ExcelInterface {


    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "總連絡窗口清單.xls";

    private String reportPath = "/WEB-INF/report/consilium/WinListA.xls";


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();
        String nowDate = dateFormater.format(now.getTime());
        UserObject userObj = UserInstance.getInstance().getUserObject(httpServletRequest);
        String userName = userObj.getUserName();
        Map beans = new HashMap();
        String sql = "";
        String mainUnit = httpServletRequest.getParameter("mainUnit");
        String reportKey = "WINLISTA";
        ArrayList<WinDataObject> winList = new ArrayList<WinDataObject>();
        try {

            conn = ds.getConnection();

            if ("WINLISTA".equals(reportKey)) {
                ArrayList superUnitList = UnitCode.getInstance().getMoiSuperUnits();
                WinDataObject wObj = null;
                Hashtable<String, WinDataObject> winTable = new Hashtable<String, WinDataObject>();
                for (int s = 0; s < superUnitList.size(); s++) {
                    UnitCodeObject nowUnitObj = (UnitCodeObject) superUnitList.get(s);
                    String nowNodeCode = nowUnitObj.getUnitCode();
                    String nowNodeName = nowUnitObj.getUnitName();
                    if ("地政機關".equals(nowNodeName)) {
                        nowNodeCode = "E2001";
                        nowNodeName = "土地重劃工程處";
                        wObj = new WinDataObject();
                        wObj.setUnitCode(nowNodeCode);
                        wObj.setUnitName(nowNodeName);
                        winList.add(wObj);
                        winTable.put(wObj.getUnitCode(), wObj);

                        nowNodeCode = "E2014";
                        nowNodeName = "國土測繪中心";
                        wObj = new WinDataObject();
                        wObj.setUnitCode(nowNodeCode);
                        wObj.setUnitName(nowNodeName);
                        winList.add(wObj);
                        winTable.put(wObj.getUnitCode(), wObj);
                    } else {
                        wObj = new WinDataObject();
                        wObj.setUnitCode(nowNodeCode);
                        wObj.setUnitName(nowNodeName);
                        winList.add(wObj);
                        winTable.put(wObj.getUnitCode(), wObj);
                    }

                }

                sql =
                        " SELECT   											" +
                                "    SID, UNITID, SECTIONID,                        " +
                                "    SUPEROCCUPATION, SUPERCONNECTOR, SUPERTEL,     " +
                                "    SUPEREMAIL, CREATOR, CREATETIME,               " +
                                "    LASTMODIFIER, LASTUPDATETIME, SUPEREXTENSION,  " +
                                "    SUPEROCCUPATION2, SUPERCONNECTOR2, SUPERTEL2,  " +
                                "    SUPEREMAIL2, SUPEREXTENSION2                   " +
                                " FROM SECTION_SUPER_MANAGER ORDER BY UNITID        ";

                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    WinDataObject tmpWObj = winTable.get(rs.getString("UNITID"));
                    if (tmpWObj != null) {

                        tmpWObj.setOccupation1((rs.getString("SUPEROCCUPATION") == null ? "" : rs.getString("SUPEROCCUPATION")));
                        tmpWObj.setName1((rs.getString("SUPERCONNECTOR") == null ? "" : rs.getString("SUPERCONNECTOR")));
                        tmpWObj.setTel1(rs.getString("SUPERTEL"));
                        tmpWObj.setExt1(rs.getString("SUPEREXTENSION"));
                        tmpWObj.setEmail1(rs.getString("SUPEREMAIL"));

                        tmpWObj.setOccupation2((rs.getString("SUPEROCCUPATION2") == null ? "" : rs.getString("SUPEROCCUPATION2")));
                        tmpWObj.setName2((rs.getString("SUPERCONNECTOR2") == null ? "" : rs.getString("SUPERCONNECTOR2")));
                        tmpWObj.setTel2(rs.getString("SUPERTEL2"));
                        tmpWObj.setExt2(rs.getString("SUPEREXTENSION2"));
                        tmpWObj.setEmail2(rs.getString("SUPEREMAIL2"));

                    }
                }

                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;
                beans.put("winList", winList);

            } else if ("WINLISTB".equals(reportKey)) {
                if (mainUnit != null && mainUnit.length() > 0) {
                    WinDataObject wObj = null;
                    UnitCodeObject sUobj = UnitCode.getInstance().getUnitCodeObject("M", mainUnit);
                    Hashtable<String, WinDataObject> winTable = new Hashtable<String, WinDataObject>();
                    ArrayList subList = UnitCode.getInstance().getMoiSubUnits(mainUnit);
                    for (int s = 0; s < subList.size(); s++) {
                        UnitCodeObject unitObj = (UnitCodeObject) subList.get(s);
                        wObj = new WinDataObject();
                        wObj.setUnitCode(sUobj.getUnitCode());
                        wObj.setUnitName(sUobj.getUnitName());
                        wObj.setSubUnitCode(unitObj.getUnitCode());
                        wObj.setSubUnitName(unitObj.getUnitName());
                        winList.add(wObj);
                        winTable.put(wObj.getSubUnitCode(), wObj);

                    }

                    sql =
                            " SELECT     									" +
                                    "    SID, UNITID, SECTIONID,                    " +
                                    "    OCCUPATION1, CONNECTOR1, TEL1,             " +
                                    "    EMAIL1, OCCUPATION2, CONNECTOR2,           " +
                                    "    TEL2, EMAIL2, CREATOR,                     " +
                                    "    CREATETIME, LASTMODIFIER, LASTUPDATETIME,  " +
                                    "    EXTENSION1, EXTENSION2, NEEDAUDIT          " +
                                    " FROM SECTION_DATA WHERE UNITID=? ORDER BY SECTIONID             ";

                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, mainUnit);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        WinDataObject tmpWObj = winTable.get(rs.getString("SECTIONID"));
                        if (tmpWObj != null) {
                            tmpWObj.setOccupation1((rs.getString("OCCUPATION1") == null ? "" : rs.getString("OCCUPATION1")));
                            tmpWObj.setName1((rs.getString("CONNECTOR1") == null ? "" : rs.getString("CONNECTOR1")));
                            tmpWObj.setTel1(rs.getString("TEL1"));
                            tmpWObj.setExt1(rs.getString("EXTENSION1"));
                            tmpWObj.setEmail1(rs.getString("EMAIL1"));

                            tmpWObj.setOccupation2((rs.getString("OCCUPATION2") == null ? "" : rs.getString("OCCUPATION2")));
                            tmpWObj.setName2((rs.getString("CONNECTOR2") == null ? "" : rs.getString("CONNECTOR2")));
                            tmpWObj.setTel2(rs.getString("TEL2"));
                            tmpWObj.setExt2(rs.getString("EXTENSION2"));
                            tmpWObj.setEmail2(rs.getString("EMAIL2"));

                        }
                    }

                    rs.close();
                    rs = null;
                    pstmt.close();
                    pstmt = null;
                    beans.put("winList", winList);
                } else {

                    ArrayList superUnitList = UnitCode.getInstance().getMoiSuperUnits();
                    WinDataObject wObj = null;
                    Hashtable<String, WinDataObject> winTable = new Hashtable<String, WinDataObject>();
                    for (int s = 0; s < superUnitList.size(); s++) {
                        UnitCodeObject unitObj = (UnitCodeObject) superUnitList.get(s);
                        String unitCode = unitObj.getUnitCode();
                        String unitName = unitObj.getUnitName();
                        ArrayList subList = UnitCode.getInstance().getMoiSubUnits(unitCode);
                        if (subList != null) {
                            for (int a = 0; a < subList.size(); a++) {
                                UnitCodeObject subUnitObj = (UnitCodeObject) subList.get(a);
                                wObj = new WinDataObject();
                                wObj.setUnitCode(unitCode);
                                wObj.setUnitName(unitName);
                                wObj.setSubUnitCode(subUnitObj.getUnitCode());
                                wObj.setSubUnitName(subUnitObj.getUnitName());
                                winList.add(wObj);
                                winTable.put(wObj.getSubUnitCode(), wObj);
                            }
                        }
                    }

                    sql =
                            " SELECT     									" +
                                    "    SID, UNITID, SECTIONID,                    " +
                                    "    OCCUPATION1, CONNECTOR1, TEL1,             " +
                                    "    EMAIL1, OCCUPATION2, CONNECTOR2,           " +
                                    "    TEL2, EMAIL2, CREATOR,                     " +
                                    "    CREATETIME, LASTMODIFIER, LASTUPDATETIME,  " +
                                    "    EXTENSION1, EXTENSION2, NEEDAUDIT          " +
                                    " FROM SECTION_DATA ORDER BY UNITID,SECTIONID            ";

                    pstmt = conn.prepareStatement(sql);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        WinDataObject tmpWObj = winTable.get(rs.getString("SECTIONID"));
                        if (tmpWObj != null) {

                            tmpWObj.setOccupation1((rs.getString("OCCUPATION1") == null ? "" : rs.getString("OCCUPATION1")));
                            tmpWObj.setName1((rs.getString("CONNECTOR1") == null ? "" : rs.getString("CONNECTOR1")));
                            tmpWObj.setTel1(rs.getString("TEL1"));
                            tmpWObj.setExt1(rs.getString("EXTENSION1"));
                            tmpWObj.setEmail1(rs.getString("EMAIL1"));

                            tmpWObj.setOccupation2((rs.getString("OCCUPATION2") == null ? "" : rs.getString("OCCUPATION2")));
                            tmpWObj.setName2((rs.getString("CONNECTOR2") == null ? "" : rs.getString("CONNECTOR2")));
                            tmpWObj.setTel2(rs.getString("TEL2"));
                            tmpWObj.setExt2(rs.getString("EXTENSION2"));
                            tmpWObj.setEmail2(rs.getString("EMAIL2"));

                        }
                    }

                    rs.close();
                    rs = null;
                    pstmt.close();
                    pstmt = null;
                    beans.put("winList", winList);

                }

            }

            conn.close();
            conn = null;

        } catch (Exception e) {
            logger.error(e);
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception ex) {
                ;
            }

            try {
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
            } catch (Exception ex) {
                ;
            }

            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (Exception ex) {
                ;
            }
        }

        beans.put("userName", userName);
        beans.put("nowDate", nowDate);
        String path = httpServletRequest.getServletContext().getRealPath(reportPath);
        File file = new File(path);
        InputStream in = new FileInputStream(file);

        XLSTransformer transformer = new XLSTransformer();
        Workbook workbook = transformer.transformXLS(in, beans);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", new String(excelName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)))
                .body(outputStream.toByteArray());
    }
}
