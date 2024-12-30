/****************************************************************************
 *
 * Copyright (c) 2011 ESound Tech. All Rights Reserved.
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
 *     File name:       CallFlowDetailReportExcelImpl.java
 *
 *     History:
 *     Date               Author            Comments
 *     -----------------------------------------------------------------------
 *     AUG 3, 2011       Aaron           Initial Release
 *     AUG 15, 2011      Aaron           新增接收退回/初報/結報/結案欄位，預先加入計算工作日功能
 *     DEC 04, 2014      Vim             過濾(CASE_FLOW)FOMETYPE 等於 CG
 *****************************************************************************/
package com.consilium.excel.esound;
/**
 * <code>CallFlowDetailReport</code>
 * 派案明細表
 **/

import com.consilium.excel.componet.ApplicationCode;
import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.*;

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

@Service("CallFlowDetailReportExcelImpl")
public class CallFlowDetailReportExcelImpl implements ExcelInterface {


    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {
        {
            UserObject usrObj = UserInstance.getInstance().getUserObject(httpServletRequest);
            if (usrObj == null) {
                throw new Exception("您尚未登入或已登入逾時!");
            }

            Map beans = new HashMap();

            Connection conn = null;
            PreparedStatement pstmt = null;
            PreparedStatement pstmt1 = null;
            ResultSet rs = null;
            String sqlQuery = "";  //暫存sql語法

            //String caseType = request.getParameter("caseType");
            String caseType = jsonNode.get("caseStatus").asText().toUpperCase();

            NumberFormat format = NumberFormat.getInstance(); //為了跑兩位數
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //格式化日期
            Calendar now = Calendar.getInstance(); //抓現在時間
            //String bDataTime_YYYY = request.getParameter("bDataTime_YYYY");
            //String eDataTime_YYYY = request.getParameter("eDataTime_YYYY");

            format.setMinimumIntegerDigits(2);  //跑兩位數
//            String bDataTime_MM = format.format(Long.valueOf(request.getParameter("bDataTime_MM")));
//            String bDataTime_DD = format.format(Long.valueOf(request.getParameter("bDataTime_DD")));
//            String eDataTime_MM = format.format(Long.valueOf(request.getParameter("eDataTime_MM")));
//            String eDataTime_DD = format.format(Long.valueOf(request.getParameter("eDataTime_DD")));

//            if (bDataTime_YYYY == null || bDataTime_MM == null || bDataTime_DD == null ||
//                    eDataTime_YYYY == null || eDataTime_MM == null || eDataTime_DD == null) {
//                throw new Exception("無效的參數!");
//            }

            //String startDate = bDataTime_YYYY + "-" + bDataTime_MM + '-' + bDataTime_DD;
            //String endDate = eDataTime_YYYY + "-" + eDataTime_MM + '-' + eDataTime_DD;
            String startDate = jsonNode.get("time").get("startTime").asText();
            String endDate = jsonNode.get("time").get("endTime").asText();
            beans.put("startDate", startDate);
            beans.put("endDate", endDate);
            beans.put("printDate", dateFormater.format(now.getTime()));

            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            UnitCode unitCode = UnitCode.getInstance();
            beans.put("unitCode", unitCode);

            ApplicationCode app = ApplicationCode.getInstance();
            beans.put("app", app);

            List callFlowList = new ArrayList();
            beans.put("callFlowList", callFlowList);

            CallFlowDetailObj obj = null;
            HashMap hp = new HashMap();
            String actionId = "";
            String receiveUnitcode = "";
            String caseStatus = "";
            String approveTime = "";
            String receiveTime = "";
            String specialType = "";
            long diffDay = 0L;

            try {

                conn = ds.getConnection();

                String sqlWorkday =
//                        " SELECT COUNT(*) WORKDAY  FROM                                                                                           " +
//                                " (SELECT TRUNC (TO_DATE(?, 'YYYY-MM-DD') + ROWNUM)  MYDATE FROM AGENT_TABLE WHERE ROWNUM <= ?) WEEKDAY, HOLIDAY  " +
//                                " WHERE WEEKDAY.MYDATE = HOLIDAY.DATEOFF(+) AND HOLIDAY.DATEOFF IS NULL AND TO_CHAR(MYDATE, 'D') NOT IN('1', '7')         "; //暫存sql語法

                        "SELECT COUNT(*) WORKDAY  FROM " +
                "  (select dateadd(day,rownum,cast(? as date))  as  MYDATE from (   SELECT row_number() OVER(ORDER BY agentid)  as rownum  FROM AGENT_TABLE) a where rownum <=?) WEEKDAY RIGHT JOIN HOLIDAY"+
                " on WEEKDAY.MYDATE = HOLIDAY.DATEOFF AND HOLIDAY.DATEOFF IS NULL AND datepart(w,MYDATE) NOT IN('1', '7') ";
                pstmt1 = conn.prepareStatement(sqlWorkday);

                sqlQuery =
                        " SELECT " +
                                "     O.ACTIONID, O.SERVICETYPE, O.CREATETIME, F.RECEIVEUNITCODE, F.APPROVENTIME, F.RECEIVEUNITTYPE, F.RECEIVESTATUS, F.RECEIVETIME " +
                                " FROM " +
                                "     CALLLOG_FORM O , CASE_FLOW F " +
                                " WHERE " +
                                "     O.ACTIONID = F.ACTIONID " +
                                " AND " +
                                "     F.RECEIVESTATUS IS NOT NULL " +
                                " AND " +
                                "     F.APPROVENTIME >= ? " +
                                " AND " +
                                "     F.APPROVENTIME <= ?  " +
                                "  AND F.FORMTYPE = 'CG' " +
                                " ORDER BY " +
                                "     O.ACTIONID ";
                pstmt = conn.prepareStatement(sqlQuery);
                int i = 0;
                pstmt.setString(++i, startDate + " 00:00:00");
                pstmt.setString(++i, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    specialType = rs.getString("RECEIVESTATUS"); // 暫存案件狀態
                    if ("J".equals(specialType)) specialType = "H"; // 快速結報等同於已結報
                    else if ("K".equals(specialType)) specialType = "I"; // 快速結案等同於已結案
                    if (caseType.equals(specialType) || "ALL".equals(caseType)) {
                        actionId = rs.getString("ACTIONID");
                        receiveUnitcode = rs.getString("RECEIVEUNITCODE");
                        caseStatus = rs.getString("RECEIVESTATUS");
                        approveTime = formater.format(rs.getTimestamp("APPROVENTIME"));
                        receiveTime = (rs.getTimestamp("RECEIVETIME") != null) ? formater.format(rs.getTimestamp("RECEIVETIME")) : "";
                        obj = new CallFlowDetailObj();
                        obj.setCaseId(actionId);
                        obj.setType(rs.getString("SERVICETYPE"));
                        obj.setCreateTime(formater.format(rs.getTimestamp("CREATETIME")));
                        obj.setApproveTime(approveTime); // 通報時間
                        obj.setUnitType(rs.getString("RECEIVEUNITTYPE"));
                        obj.setCaseStatus(caseStatus);
                        obj.setUnitCode(receiveUnitcode);
                        obj.setReceiveTime(receiveTime); // 接收時間
                        diffDay = diffDay(approveTime, receiveTime);
                        if (diffDay >= 0L)
                            obj.setReceiveDay(workDay(pstmt1, obj.getApproveTime().substring(0, 10), diffDay));
                        if (!"F".equals(caseStatus)) hp.put(actionId + "@" + receiveUnitcode, obj); // F 表示退回了
                        callFlowList.add(obj);
                    }
                }
                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;

                String reportType = "";
                String reportTime = "";
                String hpKey = "";
                obj = new CallFlowDetailObj();
                sqlQuery =
                        " SELECT " +
                                "     R.ACTIONID, R.RESPONSEUNITCODE, R.REPORTTYPE, R.CREATETIME " +
                                " FROM " +
                                "     CASE_FLOW F, CASE_RESPONSE_RECORD R " +
                                " WHERE " +
                                "     F.ACTIONID = R.ACTIONID" +
                                " AND " +
                                "     R.REPORTTYPE IN ('F', 'G', 'H') " +
                                " AND" +
                                "     F.APPROVENTIME >= ? " +
                                " AND " +
                                "     F.APPROVENTIME <= ? " +
                                "  AND F.FORMTYPE = 'CG' " +
                                " ORDER BY " +
                                "     R.ACTIONID, R.REPORTTYPE  ,R.CREATETIME desc";
                pstmt = conn.prepareStatement(sqlQuery);
                i = 0;
                pstmt.setString(++i, startDate + " 00:00:00");
                pstmt.setString(++i, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    actionId = rs.getString("ACTIONID");
                    receiveUnitcode = rs.getString("RESPONSEUNITCODE");
                    hpKey = actionId + "@" + receiveUnitcode;
                    if (hp.containsKey(hpKey)) {
                        obj = (CallFlowDetailObj) hp.get(hpKey);
                        reportType = rs.getString("REPORTTYPE");
                        reportTime = formater.format(rs.getTimestamp("CREATETIME"));
                        if ("F".equals(reportType)) // 初報日期
                        {
                            obj.setFirstReportTime(reportTime);
                            diffDay = diffDay(obj.getApproveTime(), obj.getFirstReportTime());
                            if (diffDay >= 0L)
                                obj.setFirstReportDay(workDay(pstmt1, obj.getApproveTime().substring(0, 10), diffDay));
                        } else if ("G".equals(reportType)) // 結報日期
                        {
                            obj.setEndReportTime(reportTime);
                            diffDay = diffDay(obj.getApproveTime(), obj.getEndReportTime());
                            if (diffDay >= 0L)
                                obj.setEndReportDay(workDay(pstmt1, obj.getApproveTime().substring(0, 10), diffDay));
                        } else if ("H".equals(reportType)) // 結案日期
                        {
                            obj.setEndCaseTime(reportTime);
                            diffDay = diffDay(obj.getApproveTime(), obj.getEndCaseTime());
                            if (diffDay >= 0L)
                                obj.setEndCaseDay(workDay(pstmt1, obj.getApproveTime().substring(0, 10), diffDay));
                        }
                    }
                }
                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;
                pstmt1.close();
                pstmt1 = null;
                conn.close();
                conn = null;
                String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/CallFlowDetail.xls");
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

    private long diffDay (String beginTime, String endTime ) throws Exception
    {
        long diffDay = 0L;
        if (beginTime != null && !"".equals(beginTime) && endTime != null && !"".equals(endTime)) {
            Date begin = new Date();
            Date end = new Date();
            try {
                SimpleDateFormat difFormater = new SimpleDateFormat("yyyy-MM-dd");
                begin = difFormater.parse(beginTime);
                end = difFormater.parse(endTime);
                diffDay = (end.getTime() - begin.getTime()) / (24 * 3600 * 1000);
            } catch (Exception e) {
                throw e;
            }
        } else {
            diffDay = -1;
        }
        return diffDay;
    }

    private String workDay (PreparedStatement pstmt, String beginTime,long diffDay ) throws Exception // 計算工作天
    {
        ResultSet rs = null;
        String workDay = "";
        try {
            int i = 0;
            pstmt.setString(++i, beginTime);
            pstmt.setLong(++i, diffDay);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                workDay = rs.getString("WORKDAY");
            }
            rs.close();
            rs = null;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {

            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return workDay;
    }
}