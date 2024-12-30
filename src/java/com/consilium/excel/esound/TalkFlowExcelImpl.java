/****************************************************************************
 *
 * Copyright (c) 2009 ESound Tech. All Rights Reserved.
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
 *     File name:       TalkFlowExcelImpl.java
 *
 *     History:
 *     Date               Author                  Comments
 *     -----------------------------------------------------------------------
 *     Apr 19, 2010       David                 Initial Release
 *     Apr 14, 2015       eRic                  等候放棄數誤植修正
 *     Apr 17, 2015       eRic                  過濾非上班時間
 *****************************************************************************/
package com.consilium.excel.esound;


import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.talk.*;
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
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service("TalkFlowExcelImpl")
public class TalkFlowExcelImpl implements ExcelInterface {
    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        NumberFormat format = NumberFormat.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();

        format.setMinimumIntegerDigits(2);
        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();

        String bDataTime_YYYY = startDate.substring(0, 4);
        String eDataTime_YYYY = endDate.substring(0, 4);
        String bDataTime_MM = startDate.substring(5, 7);
        String bDataTime_DD = startDate.substring(8, 10);
        String eDataTime_MM = endDate.substring(5, 7);
        String eDataTime_DD = endDate.substring(8, 10);

        Calendar fromDate = Calendar.getInstance();
        fromDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1, Integer.parseInt(bDataTime_DD), 0, 0, 0);

        //取得 展現方式 依年Y 依月M 依週W 依日D 依時H 依總數A
        String timeType = jsonNode.get("timeType").asText();

        Map beans = new HashMap();
        try {

            conn = ds.getConnection();
            SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat getDateHous = new SimpleDateFormat("yyyyMMddHH");

            Calendar toDate = Calendar.getInstance();
            fromDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1, Integer.parseInt(bDataTime_DD), 0, 0, 0);
            toDate.set(Integer.parseInt(eDataTime_YYYY), Integer.parseInt(eDataTime_MM) - 1, Integer.parseInt(eDataTime_DD), 0, 0, 0);
            int diffDay = (int) ((toDate.getTimeInMillis() - fromDate.getTimeInMillis()) / (1000 * 60 * 60) + 24);
            ReportData reportData = new ReportData();
            reportData.setStartDate(bDataTime_YYYY + "-" + bDataTime_MM + '-' + bDataTime_DD);
            reportData.setEndDate(eDataTime_YYYY + "-" + eDataTime_MM + '-' + eDataTime_DD);
            reportData.setReportTime(dateFormater1.format(now.getTime()));
            reportData.setTimeType(timeType);
            HashMap hm = new HashMap();
            DefaultObject defObj = new DefaultObject();
            ServiceLevelObject serviceObj = new ServiceLevelObject();
            DefaultObject defTotalObj = new DefaultObject();
            int maxWait = 0;
            int agentNO = 0;
            String getAgentNumber = "SELECT VALUE " +
                    "FROM SYSTEM_TABLE " +
                    "WHERE PARAMETER = 'CTI_SERVICE_AGENT'";

            pstmt = conn.prepareStatement(getAgentNumber);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                agentNO = rs.getInt("VALUE");
            }

            if ("A".equals(timeType)) {
                String getMaxWait = "SELECT " +
                        "MAXCALLDURATIONBEFOREANSWER " +
                        "FROM M7480_SITE_INFO " +
                        "WHERE BEGINTIME >= ? " +
                        "AND BEGINTIME <  DATEADD (day , 1 , cast(? as date) ) " +
                        "AND BEGINTIME IS NOT NULL ";

                String sqlString = "SELECT " +
                        "SUM(MAXWAITCALLNBR)MAXWAITCALLNBR, " +
                        "SUM(SUMRINGDURANSWERCALL)SUMRINGDURANSWERCALL, SUM(NBRCALLSANSWERED)NBRCALLSANSWERED, " +
                        "SUM(WAITCALLNBR)WAITCALLNBR, SUM(REPORTCOUNT)REPORTCOUNT, " +

                        //"SUM(SUMCALLSDURATIONBEFOREANSWER)SUMCALLSDURATIONBEFOREANSWER, " +
                        "sum ( case when  CAST( BEGINTIME as date) >='2022-12-03' then     SUMCALLSDURATIONBEFOREANSWER +   10* NBRCALLSANSWERED else   SUMCALLSDURATIONBEFOREANSWER   end  ) SUMCALLSDURATIONBEFOREANSWER,"+

                        "SUM(SUMCALLSAGENTCOMMDURATION)SUMCALLSAGENTCOMMDURATION, " +
                        "SUM(TOTALLOGINREADYIDLEDURATION)TOTALLOGINREADYIDLEDURATION, " +
                        "SUM(TOTALLOGINNOTREADYIDLEDURATION)TOTALLOGINNOTREADYIDLEDURATION, " +
                        "SUM(NBRINBOUNDCALLNOTANSWER)NBRINBOUNDCALLNOTANSWER " +
                        "FROM M7480_SITE_INFO " +
                        "WHERE BEGINTIME >= ? " +
                        "AND BEGINTIME <  DATEADD (day , 1 , cast(? as date) ) " +
                        "AND BEGINTIME IS NOT NULL ";// +
                //"group BY BEGINTIME ";

                pstmt = conn.prepareStatement(getMaxWait);
                pstmt.setString(1, (String) reportData.getStartDate() + " 00:00:00");
                pstmt.setString(2, (String) reportData.getEndDate() + " 00:00:00");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    maxWait = rs.getInt("MAXCALLDURATIONBEFOREANSWER");
                    if (maxWait > defTotalObj.getMaxWait()) defTotalObj.setMaxWait(maxWait);
                }

                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;

                pstmt = conn.prepareStatement(sqlString);
                pstmt.setString(1, (String) reportData.getStartDate() + " 00:00:00");
                pstmt.setString(2, (String) reportData.getEndDate() + " 00:00:00");
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    defTotalObj.setAverageCommunication(rs.getInt("SUMCALLSAGENTCOMMDURATION"));
                    defTotalObj.setAverageRang(rs.getInt("SUMRINGDURANSWERCALL"));
                    defTotalObj.setAverageWait(rs.getInt("SUMCALLSDURATIONBEFOREANSWER"));
                    defTotalObj.setMaxCommunication(rs.getInt("MAXWAITCALLNBR"));
                    defTotalObj.setTotalConnected(rs.getInt("NBRCALLSANSWERED"));
                    defTotalObj.setWaitCallnbr(rs.getInt("WAITCALLNBR"));
                    defTotalObj.setReportCount(rs.getInt("REPORTCOUNT"));
                    defTotalObj.setTotalLoginReady(rs.getInt("TOTALLOGINREADYIDLEDURATION"));
                    defTotalObj.setTotalLoginNotReady(rs.getInt("TOTALLOGINNOTREADYIDLEDURATION"));
                    defTotalObj.setCallNotAnswer(rs.getInt("NBRINBOUNDCALLNOTANSWER"));
                }
            } else {
                String sqlString = "SELECT format(beginTime,'yyyyMMddHHmm') as BEGINTIME, " +
                        "MAXWAITCALLNBR, " +
                        "MAXCALLDURATIONBEFOREANSWER, " +
                        "SUMRINGDURANSWERCALL, NBRCALLSANSWERED, " +
                        "WAITCALLNBR, REPORTCOUNT, " +

                       // "SUMCALLSDURATIONBEFOREANSWER, " +
                  "case when  CAST( BEGINTIME as date) >='2022-12-03' then     SUMCALLSDURATIONBEFOREANSWER +   10* NBRCALLSANSWERED else   SUMCALLSDURATIONBEFOREANSWER   end SUMCALLSDURATIONBEFOREANSWER,"+

                        "SUMCALLSAGENTCOMMDURATION, " +
                        "TOTALLOGINREADYIDLEDURATION, " +
                        "TOTALLOGINNOTREADYIDLEDURATION, " +
                        "NBRINBOUNDCALLNOTANSWER " +
                        "FROM M7480_SITE_INFO " +
                        "WHERE BEGINTIME >= ? " +
                        "AND BEGINTIME < DATEADD (day , 1 , cast(? as date) ) " +
                        "AND BEGINTIME IS NOT NULL " +
                        "ORDER BY BEGINTIME ASC";
                pstmt = conn.prepareStatement(sqlString);
                pstmt.setString(1, (String) reportData.getStartDate() + " 00:00:00");
                pstmt.setString(2, (String) reportData.getEndDate() + " 00:00:00");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    hm.put("AC-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("SUMCALLSAGENTCOMMDURATION")));//總通話時間
                    hm.put("AR-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("SUMRINGDURANSWERCALL")));//總響鈴時間
                    hm.put("AW-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("SUMCALLSDURATIONBEFOREANSWER")));//總等待時間
                    hm.put("MC-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("MAXWAITCALLNBR")));//最多等待通數
                    hm.put("MW-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("MAXCALLDURATIONBEFOREANSWER")));//最長等待時間
                    hm.put("TC-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("NBRCALLSANSWERED")));//總接通數

                    hm.put("WC-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("WAITCALLNBR")));//等待通數
                    hm.put("RC-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("REPORTCOUNT")));//REPORTCOUNT

                    hm.put("TLR-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("TOTALLOGINREADYIDLEDURATION")));//座席總等待時間
                    hm.put("TLNR-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("TOTALLOGINNOTREADYIDLEDURATION")));//座席總離席時間
                    hm.put("CNA-" + rs.getString("BEGINTIME").substring(0, 10), new Integer(rs.getInt("NBRINBOUNDCALLNOTANSWER")));//座席總響鈴未接通數
                }

                String theDate = "";
                String fday = "";

                if (bDataTime_DD.length() == 1)
                    fday = "0" + bDataTime_DD;
                else
                    fday = bDataTime_DD;

                int years = Integer.parseInt(bDataTime_YYYY);
                int month = Integer.parseInt(bDataTime_MM);
                int days = Integer.parseInt(bDataTime_DD);
                int weeks = CalendarUtil.process(years, month, days);
                int hour = 0;

                for (int i = 0; i < diffDay; i++) {
                    //組出所有時間
                    theDate = getDateHous.format(fromDate.getTime());
                    //分解時間 年 月 日 週 時
                    int yyyy = Integer.parseInt((String) theDate.substring(0, 4));
                    int mm = Integer.parseInt((String) theDate.substring(4, 6));
                    int dd = Integer.parseInt((String) theDate.substring(6, 8));
                    int ww = CalendarUtil.process(yyyy, mm, dd);
                    int hh = Integer.parseInt((String) theDate.substring(8, 10));

                    if ("Y".equals(timeType)) {
                        if (years != yyyy) {
                            serviceObj = new ServiceLevelObject();
                            serviceObj.setDataTime(defObj.getDataTime());
                            serviceObj.setMaxWait(CalendarUtil.formatDate(defObj.getMaxWait()));
                            serviceObj.setMaxCommunication(defObj.getMaxCommunication());

                            if (defObj.getTotalConnected() > 0) {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate((defObj.getAverageCommunication() / defObj.getTotalConnected())));
                                serviceObj.setAverageRang(CalendarUtil.formatDate((defObj.getAverageRang() / defObj.getTotalConnected())));
                                serviceObj.setAverageWait(CalendarUtil.formatDate((defObj.getAverageWait() / defObj.getTotalConnected() - 10) > 0 ? (defObj.getAverageWait() / defObj.getTotalConnected() - 10) : 0));
                            } else {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));
                                serviceObj.setAverageRang(CalendarUtil.formatDate(0));
                                serviceObj.setAverageWait(CalendarUtil.formatDate(0));
                            }
                            if (defObj.getReportCount() > 0) {
                                serviceObj.setAverageWaitCount(defObj.getWaitCallnbr() / defObj.getReportCount());
                            } else {
                                serviceObj.setAverageWaitCount(0);
                            }
                            if (agentNO > 0) {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(defObj.getTotalLoginReady() / agentNO));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate((defObj.getTotalLoginNotReady() / agentNO)));
                                serviceObj.setAverageCallNotAnswer(defObj.getCallNotAnswer());
                            } else {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageCallNotAnswer(0);
                            }

                            reportData.addServiceLevelObject(serviceObj);
                            defObj = new DefaultObject();
                            years = yyyy;
                        }
                        defObj.setDataTime(years + "年");
                        if (hm.get("AC-" + theDate) != null)
                            defObj.setAverageCommunication(defObj.getAverageCommunication() + (Integer) hm.get("AC-" + theDate));
                        if (hm.get("AR-" + theDate) != null)
                            defObj.setAverageRang(defObj.getAverageRang() + (Integer) hm.get("AR-" + theDate));
                        if (hm.get("AW-" + theDate) != null)
                            defObj.setAverageWait(defObj.getAverageWait() + (Integer) hm.get("AW-" + theDate));
                        //存放最大等候通數
                        if (hm.get("MC-" + theDate) != null) {
                            if ((Integer) hm.get("MC-" + theDate) > defObj.getMaxCommunication()) {
                                defObj.setMaxCommunication((Integer) hm.get("MC-" + theDate));
                            }
                        }
                        //存放最長等待時間
                        if (hm.get("MW-" + theDate) != null) {
                            if ((Integer) hm.get("MW-" + theDate) > defObj.getMaxWait()) {
                                defObj.setMaxWait((Integer) hm.get("MW-" + theDate));
                            }
                        }
                        if (hm.get("TC-" + theDate) != null)
                            defObj.setTotalConnected(defObj.getTotalConnected() + (Integer) hm.get("TC-" + theDate));
                        if (hm.get("WC-" + theDate) != null)
                            defObj.setWaitCallnbr(defObj.getWaitCallnbr() + (Integer) hm.get("WC-" + theDate));
                        if (hm.get("RC-" + theDate) != null)
                            defObj.setReportCount(defObj.getReportCount() + (Integer) hm.get("RC-" + theDate));

                        if (hm.get("TLR-" + theDate) != null)
                            defObj.setTotalLoginReady(defObj.getTotalLoginReady() + (Integer) hm.get("TLR-" + theDate));
                        if (hm.get("TLNR-" + theDate) != null)
                            defObj.setTotalLoginNotReady(defObj.getTotalLoginNotReady() + (Integer) hm.get("TLNR-" + theDate));
                        if (hm.get("CNA-" + theDate) != null)
                            defObj.setCallNotAnswer(defObj.getCallNotAnswer() + (Integer) hm.get("CNA-" + theDate));

                        if (i == (diffDay - 1)) {
                            serviceObj = new ServiceLevelObject();
                            serviceObj.setDataTime(defObj.getDataTime());
                            serviceObj.setMaxWait(CalendarUtil.formatDate(defObj.getMaxWait()));
                            serviceObj.setMaxCommunication(defObj.getMaxCommunication());

                            if (defObj.getTotalConnected() > 0) {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate((defObj.getAverageCommunication() / defObj.getTotalConnected())));
                                serviceObj.setAverageRang(CalendarUtil.formatDate((defObj.getAverageRang() / defObj.getTotalConnected())));
                                serviceObj.setAverageWait(CalendarUtil.formatDate((defObj.getAverageWait() / defObj.getTotalConnected() - 10) > 0 ? (defObj.getAverageWait() / defObj.getTotalConnected() - 10) : 0));
                            } else {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));
                                serviceObj.setAverageRang(CalendarUtil.formatDate(0));
                                serviceObj.setAverageWait(CalendarUtil.formatDate(0));
                            }
                            if (defObj.getReportCount() > 0) {
                                serviceObj.setAverageWaitCount(defObj.getWaitCallnbr() / defObj.getReportCount());
                            } else {
                                serviceObj.setAverageWaitCount(0);
                            }
                            if (agentNO > 0) {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(defObj.getTotalLoginReady() / agentNO));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate((defObj.getTotalLoginNotReady() / agentNO)));
                                serviceObj.setAverageCallNotAnswer(defObj.getCallNotAnswer());
                            } else {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageCallNotAnswer(0);
                            }
                            reportData.addServiceLevelObject(serviceObj);
                        }
                    } else if ("M".equals(timeType)) {
                        if (month != mm) {
                            serviceObj = new ServiceLevelObject();
                            serviceObj.setDataTime(defObj.getDataTime());
                            serviceObj.setMaxWait(CalendarUtil.formatDate(defObj.getMaxWait()));
                            serviceObj.setMaxCommunication(defObj.getMaxCommunication());

                            if (defObj.getTotalConnected() > 0) {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate((defObj.getAverageCommunication() / defObj.getTotalConnected())));
                                serviceObj.setAverageRang(CalendarUtil.formatDate((defObj.getAverageRang() / defObj.getTotalConnected())));
                                serviceObj.setAverageWait(CalendarUtil.formatDate((defObj.getAverageWait() / defObj.getTotalConnected() - 10) > 0 ? (defObj.getAverageWait() / defObj.getTotalConnected() - 10) : 0));
                            } else {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));
                                serviceObj.setAverageRang(CalendarUtil.formatDate(0));
                                serviceObj.setAverageWait(CalendarUtil.formatDate(0));
                            }
                            if (defObj.getReportCount() > 0) {
                                serviceObj.setAverageWaitCount(defObj.getWaitCallnbr() / defObj.getReportCount());
                            } else {
                                serviceObj.setAverageWaitCount(0);
                            }
                            if (agentNO > 0) {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(defObj.getTotalLoginReady() / agentNO));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate((defObj.getTotalLoginNotReady() / agentNO)));
                                serviceObj.setAverageCallNotAnswer(defObj.getCallNotAnswer());
                            } else {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageCallNotAnswer(0);
                            }
                            reportData.addServiceLevelObject(serviceObj);
                            defObj = new DefaultObject();
                            years = yyyy;
                            month = mm;
                        }

                        String setMMStr = mm + "";
                        if (setMMStr.length() == 1) setMMStr = "0" + mm;
                        defObj.setDataTime(yyyy + "年" + setMMStr + "月");
                        if (hm.get("AC-" + theDate) != null)
                            defObj.setAverageCommunication(defObj.getAverageCommunication() + (Integer) hm.get("AC-" + theDate));
                        if (hm.get("AR-" + theDate) != null)
                            defObj.setAverageRang(defObj.getAverageRang() + (Integer) hm.get("AR-" + theDate));
                        if (hm.get("AW-" + theDate) != null)
                            defObj.setAverageWait(defObj.getAverageWait() + (Integer) hm.get("AW-" + theDate));
                        //存放最大等候通數
                        if (hm.get("MC-" + theDate) != null) {
                            if ((Integer) hm.get("MC-" + theDate) > defObj.getMaxCommunication()) {
                                defObj.setMaxCommunication((Integer) hm.get("MC-" + theDate));
                            }
                        }
                        //存放最長等待時間
                        if (hm.get("MW-" + theDate) != null) {
                            if ((Integer) hm.get("MW-" + theDate) > defObj.getMaxWait()) {
                                defObj.setMaxWait((Integer) hm.get("MW-" + theDate));
                            }
                        }
                        if (hm.get("TC-" + theDate) != null)
                            defObj.setTotalConnected(defObj.getTotalConnected() + (Integer) hm.get("TC-" + theDate));
                        if (hm.get("WC-" + theDate) != null)
                            defObj.setWaitCallnbr(defObj.getWaitCallnbr() + (Integer) hm.get("WC-" + theDate));
                        if (hm.get("RC-" + theDate) != null)
                            defObj.setReportCount(defObj.getReportCount() + (Integer) hm.get("RC-" + theDate));

                        if (hm.get("TLR-" + theDate) != null)
                            defObj.setTotalLoginReady(defObj.getTotalLoginReady() + (Integer) hm.get("TLR-" + theDate));
                        if (hm.get("TLNR-" + theDate) != null)
                            defObj.setTotalLoginNotReady(defObj.getTotalLoginNotReady() + (Integer) hm.get("TLNR-" + theDate));
                        if (hm.get("CNA-" + theDate) != null)
                            defObj.setCallNotAnswer(defObj.getCallNotAnswer() + (Integer) hm.get("CNA-" + theDate));

                        if (i == (diffDay - 1)) {
                            serviceObj = new ServiceLevelObject();
                            serviceObj.setDataTime(defObj.getDataTime());
                            serviceObj.setMaxWait(CalendarUtil.formatDate(defObj.getMaxWait()));
                            serviceObj.setMaxCommunication(defObj.getMaxCommunication());

                            if (defObj.getTotalConnected() > 0) {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate((defObj.getAverageCommunication() / defObj.getTotalConnected())));
                                serviceObj.setAverageRang(CalendarUtil.formatDate((defObj.getAverageRang() / defObj.getTotalConnected())));
                                serviceObj.setAverageWait(CalendarUtil.formatDate((defObj.getAverageWait() / defObj.getTotalConnected() - 10) > 0 ? (defObj.getAverageWait() / defObj.getTotalConnected() - 10) : 0));
                            } else {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));
                                serviceObj.setAverageRang(CalendarUtil.formatDate(0));
                                serviceObj.setAverageWait(CalendarUtil.formatDate(0));
                            }
                            if (defObj.getReportCount() > 0) {
                                serviceObj.setAverageWaitCount(defObj.getWaitCallnbr() / defObj.getReportCount());
                            } else {
                                serviceObj.setAverageWaitCount(0);
                            }
                            if (agentNO > 0) {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(defObj.getTotalLoginReady() / agentNO));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate((defObj.getTotalLoginNotReady() / agentNO)));
                                serviceObj.setAverageCallNotAnswer(defObj.getCallNotAnswer());
                            } else {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageCallNotAnswer(0);
                            }
                            reportData.addServiceLevelObject(serviceObj);
                        }
                    } else if ("W".equals(timeType)) {
                        if (weeks != ww) {
                            serviceObj = new ServiceLevelObject();
                            serviceObj.setDataTime(defObj.getDataTime());
                            serviceObj.setMaxWait(CalendarUtil.formatDate(defObj.getMaxWait()));
                            serviceObj.setMaxCommunication(defObj.getMaxCommunication());

                            if (defObj.getTotalConnected() > 0) {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate((defObj.getAverageCommunication() / defObj.getTotalConnected())));
                                serviceObj.setAverageRang(CalendarUtil.formatDate((defObj.getAverageRang() / defObj.getTotalConnected())));
                                serviceObj.setAverageWait(CalendarUtil.formatDate((defObj.getAverageWait() / defObj.getTotalConnected() - 10) > 0 ? (defObj.getAverageWait() / defObj.getTotalConnected() - 10) : 0));
                            } else {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));
                                serviceObj.setAverageRang(CalendarUtil.formatDate(0));
                                serviceObj.setAverageWait(CalendarUtil.formatDate(0));
                            }
                            if (defObj.getReportCount() > 0) {
                                serviceObj.setAverageWaitCount(defObj.getWaitCallnbr() / defObj.getReportCount());
                            } else {
                                serviceObj.setAverageWaitCount(0);
                            }
                            if (agentNO > 0) {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(defObj.getTotalLoginReady() / agentNO));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate((defObj.getTotalLoginNotReady() / agentNO)));
                                serviceObj.setAverageCallNotAnswer(defObj.getCallNotAnswer());
                            } else {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageCallNotAnswer(0);
                            }
                            reportData.addServiceLevelObject(serviceObj);
                            defObj = new DefaultObject();
                            years = yyyy;
                            month = mm;
                            weeks = ww;
                            fday = dd + "";
                            if (fday.length() == 1) fday = "0" + fday;
                        }

                        String lday = dd + "";
                        if (lday.length() == 1) lday = "0" + lday;
                        String setMMStr = mm + "";
                        if (setMMStr.length() == 1) setMMStr = "0" + mm;
                        defObj.setDataTime(yyyy + "年" + setMMStr + "月 第" + ww + "週 " + fday + "~" + lday + "日");
                        if (hm.get("AC-" + theDate) != null)
                            defObj.setAverageCommunication(defObj.getAverageCommunication() + (Integer) hm.get("AC-" + theDate));
                        if (hm.get("AR-" + theDate) != null)
                            defObj.setAverageRang(defObj.getAverageRang() + (Integer) hm.get("AR-" + theDate));
                        if (hm.get("AW-" + theDate) != null)
                            defObj.setAverageWait(defObj.getAverageWait() + (Integer) hm.get("AW-" + theDate));
                        //存放最大等候通數
                        if (hm.get("MC-" + theDate) != null) {
                            if ((Integer) hm.get("MC-" + theDate) > defObj.getMaxCommunication()) {
                                defObj.setMaxCommunication((Integer) hm.get("MC-" + theDate));
                            }
                        }
                        //存放最長等待時間
                        if (hm.get("MW-" + theDate) != null) {
                            if ((Integer) hm.get("MW-" + theDate) > defObj.getMaxWait()) {
                                defObj.setMaxWait((Integer) hm.get("MW-" + theDate));
                            }
                        }
                        if (hm.get("TC-" + theDate) != null)
                            defObj.setTotalConnected(defObj.getTotalConnected() + (Integer) hm.get("TC-" + theDate));
                        if (hm.get("WC-" + theDate) != null)
                            defObj.setWaitCallnbr(defObj.getWaitCallnbr() + (Integer) hm.get("WC-" + theDate));
                        if (hm.get("RC-" + theDate) != null)
                            defObj.setReportCount(defObj.getReportCount() + (Integer) hm.get("RC-" + theDate));

                        if (hm.get("TLR-" + theDate) != null)
                            defObj.setTotalLoginReady(defObj.getTotalLoginReady() + (Integer) hm.get("TLR-" + theDate));
                        if (hm.get("TLNR-" + theDate) != null)
                            defObj.setTotalLoginNotReady(defObj.getTotalLoginNotReady() + (Integer) hm.get("TLNR-" + theDate));
                        if (hm.get("CNA-" + theDate) != null)
                            defObj.setCallNotAnswer(defObj.getCallNotAnswer() + (Integer) hm.get("CNA-" + theDate));

                        if (i == (diffDay - 1)) {
                            serviceObj = new ServiceLevelObject();
                            serviceObj.setDataTime(defObj.getDataTime());
                            serviceObj.setMaxWait(CalendarUtil.formatDate(defObj.getMaxWait()));
                            serviceObj.setMaxCommunication(defObj.getMaxCommunication());

                            if (defObj.getTotalConnected() > 0) {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate((defObj.getAverageCommunication() / defObj.getTotalConnected())));
                                serviceObj.setAverageRang(CalendarUtil.formatDate((defObj.getAverageRang() / defObj.getTotalConnected())));
                                serviceObj.setAverageWait(CalendarUtil.formatDate((defObj.getAverageWait() / defObj.getTotalConnected() - 10) > 0 ? (defObj.getAverageWait() / defObj.getTotalConnected() - 10) : 0));
                            } else {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));
                                serviceObj.setAverageRang(CalendarUtil.formatDate(0));
                                serviceObj.setAverageWait(CalendarUtil.formatDate(0));
                            }
                            if (defObj.getReportCount() > 0) {
                                serviceObj.setAverageWaitCount(defObj.getWaitCallnbr() / defObj.getReportCount());
                            } else {
                                serviceObj.setAverageWaitCount(0);
                            }
                            if (agentNO > 0) {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(defObj.getTotalLoginReady() / agentNO));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate((defObj.getTotalLoginNotReady() / agentNO)));
                                serviceObj.setAverageCallNotAnswer(defObj.getCallNotAnswer());
                            } else {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageCallNotAnswer(0);
                            }
                            reportData.addServiceLevelObject(serviceObj);
                        }
                    } else if ("D".equals(timeType)) {
                        if (days != dd) {
                            serviceObj = new ServiceLevelObject();
                            serviceObj.setDataTime(defObj.getDataTime());
                            serviceObj.setMaxWait(CalendarUtil.formatDate(defObj.getMaxWait()));
                            serviceObj.setMaxCommunication(defObj.getMaxCommunication());

                            if (defObj.getTotalConnected() > 0) {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate((defObj.getAverageCommunication() / defObj.getTotalConnected())));
                                serviceObj.setAverageRang(CalendarUtil.formatDate((defObj.getAverageRang() / defObj.getTotalConnected())));
                                serviceObj.setAverageWait(CalendarUtil.formatDate((defObj.getAverageWait() / defObj.getTotalConnected() - 10) > 0 ? (defObj.getAverageWait() / defObj.getTotalConnected() - 10) : 0));
                            } else {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));
                                serviceObj.setAverageRang(CalendarUtil.formatDate(0));
                                serviceObj.setAverageWait(CalendarUtil.formatDate(0));
                            }
                            if (defObj.getReportCount() > 0) {
                                serviceObj.setAverageWaitCount(defObj.getWaitCallnbr() / defObj.getReportCount());
                            } else {
                                serviceObj.setAverageWaitCount(0);
                            }
                            if (agentNO > 0) {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(defObj.getTotalLoginReady() / agentNO));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate((defObj.getTotalLoginNotReady() / agentNO)));
                                serviceObj.setAverageCallNotAnswer(defObj.getCallNotAnswer());
                            } else {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageCallNotAnswer(0);
                            }
                            reportData.addServiceLevelObject(serviceObj);
                            defObj = new DefaultObject();
                            years = yyyy;
                            month = mm;
                            weeks = ww;
                            days = dd;
                        }

                        String setMMStr = mm + "";
                        String setDDStr = dd + "";
                        if (setMMStr.length() == 1) setMMStr = "0" + mm;
                        if (setDDStr.length() == 1) setDDStr = "0" + dd;
                        defObj.setDataTime(yyyy + "年" + setMMStr + "月" + setDDStr + "日");
                        if (hm.get("AC-" + theDate) != null)
                            defObj.setAverageCommunication(defObj.getAverageCommunication() + (Integer) hm.get("AC-" + theDate));
                        if (hm.get("AR-" + theDate) != null)
                            defObj.setAverageRang(defObj.getAverageRang() + (Integer) hm.get("AR-" + theDate));
                        if (hm.get("AW-" + theDate) != null)
                            defObj.setAverageWait(defObj.getAverageWait() + (Integer) hm.get("AW-" + theDate));

                        //存放最大等候通數
                        if (hm.get("MC-" + theDate) != null) {
                            if ((Integer) hm.get("MC-" + theDate) > defObj.getMaxCommunication()) {
                                defObj.setMaxCommunication((Integer) hm.get("MC-" + theDate));
                            }
                        }

                        //存放最長等待時間
                        if (hm.get("MW-" + theDate) != null) {
                            if ((Integer) hm.get("MW-" + theDate) > defObj.getMaxWait()) {
                                defObj.setMaxWait((Integer) hm.get("MW-" + theDate));
                            }
                        }

                        if (hm.get("TC-" + theDate) != null)
                            defObj.setTotalConnected(defObj.getTotalConnected() + (Integer) hm.get("TC-" + theDate));
                        if (hm.get("WC-" + theDate) != null)
                            defObj.setWaitCallnbr(defObj.getWaitCallnbr() + (Integer) hm.get("WC-" + theDate));
                        if (hm.get("RC-" + theDate) != null)
                            defObj.setReportCount(defObj.getReportCount() + (Integer) hm.get("RC-" + theDate));

                        if (hm.get("TLR-" + theDate) != null)
                            defObj.setTotalLoginReady(defObj.getTotalLoginReady() + (Integer) hm.get("TLR-" + theDate));
                        if (hm.get("TLNR-" + theDate) != null)
                            defObj.setTotalLoginNotReady(defObj.getTotalLoginNotReady() + (Integer) hm.get("TLNR-" + theDate));
                        if (hm.get("CNA-" + theDate) != null)
                            defObj.setCallNotAnswer(defObj.getCallNotAnswer() + (Integer) hm.get("CNA-" + theDate));

                        if (i == (diffDay - 1)) {
                            serviceObj = new ServiceLevelObject();
                            serviceObj.setDataTime(defObj.getDataTime());
                            serviceObj.setMaxWait(CalendarUtil.formatDate(defObj.getMaxWait()));
                            serviceObj.setMaxCommunication(defObj.getMaxCommunication());
                            if (defObj.getTotalConnected() > 0) {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate((defObj.getAverageCommunication() / defObj.getTotalConnected())));
                                serviceObj.setAverageRang(CalendarUtil.formatDate((defObj.getAverageRang() / defObj.getTotalConnected())));
                                serviceObj.setAverageWait(CalendarUtil.formatDate((defObj.getAverageWait() / defObj.getTotalConnected() - 10) > 0 ? (defObj.getAverageWait() / defObj.getTotalConnected() - 10) : 0));
                            } else {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));
                                serviceObj.setAverageRang(CalendarUtil.formatDate(0));
                                serviceObj.setAverageWait(CalendarUtil.formatDate(0));
                            }
                            if (defObj.getReportCount() > 0) {
                                serviceObj.setAverageWaitCount(defObj.getWaitCallnbr() / defObj.getReportCount());
                            } else {
                                serviceObj.setAverageWaitCount(0);
                            }
                            if (agentNO > 0) {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(defObj.getTotalLoginReady() / agentNO));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate((defObj.getTotalLoginNotReady() / agentNO)));
                                serviceObj.setAverageCallNotAnswer(defObj.getCallNotAnswer());
                            } else {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageCallNotAnswer(0);
                            }
                            reportData.addServiceLevelObject(serviceObj);
                        }
                    } else if ("H".equals(timeType)) {
                        if (hour != hh) {
                            serviceObj = new ServiceLevelObject();
                            serviceObj.setDataTime(defObj.getDataTime());
                            serviceObj.setMaxWait(CalendarUtil.formatDate(defObj.getMaxWait()));
                            serviceObj.setMaxCommunication(defObj.getMaxCommunication());
                            if (defObj.getTotalConnected() > 0) {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate((defObj.getAverageCommunication() / defObj.getTotalConnected())));
                                serviceObj.setAverageRang(CalendarUtil.formatDate((defObj.getAverageRang() / defObj.getTotalConnected())));
                                serviceObj.setAverageWait(CalendarUtil.formatDate((defObj.getAverageWait() / defObj.getTotalConnected() - 10) > 0 ? (defObj.getAverageWait() / defObj.getTotalConnected() - 10) : 0));
                            } else {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));
                                serviceObj.setAverageRang(CalendarUtil.formatDate(0));
                                serviceObj.setAverageWait(CalendarUtil.formatDate(0));
                            }
                            if (defObj.getReportCount() > 0) {
                                serviceObj.setAverageWaitCount(defObj.getWaitCallnbr() / defObj.getReportCount());
                            } else {
                                serviceObj.setAverageWaitCount(0);
                            }
                            if (agentNO > 0) {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(defObj.getTotalLoginReady() / agentNO));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate((defObj.getTotalLoginNotReady() / agentNO)));
                                serviceObj.setAverageCallNotAnswer(defObj.getCallNotAnswer());
                            } else {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageCallNotAnswer(0);
                            }
                            reportData.addServiceLevelObject(serviceObj);
                            defObj = new DefaultObject();
                            years = yyyy;
                            month = mm;
                            weeks = ww;
                            days = dd;
                            hour = hh;
                        }

                        String setMMStr = mm + "";
                        String setDDStr = dd + "";
                        String setHHStr = hh + "";
                        if (setMMStr.length() == 1) setMMStr = "0" + mm;
                        if (setDDStr.length() == 1) setDDStr = "0" + dd;
                        if (setHHStr.length() == 1) setHHStr = "0" + hh;
                        defObj.setDataTime(yyyy + "年" + setMMStr + "月" + setDDStr + "日 " + setHHStr + "時");

                        if (hm.get("AC-" + theDate) != null)
                            defObj.setAverageCommunication(defObj.getAverageCommunication() + (Integer) hm.get("AC-" + theDate));
                        if (hm.get("AR-" + theDate) != null)
                            defObj.setAverageRang(defObj.getAverageRang() + (Integer) hm.get("AR-" + theDate));
                        if (hm.get("AW-" + theDate) != null)
                            defObj.setAverageWait(defObj.getAverageWait() + (Integer) hm.get("AW-" + theDate));

                        if (hm.get("MC-" + theDate) != null)
                            defObj.setMaxCommunication((Integer) hm.get("MC-" + theDate));
                        if (hm.get("MW-" + theDate) != null) defObj.setMaxWait((Integer) hm.get("MW-" + theDate));

                        if (hm.get("TC-" + theDate) != null)
                            defObj.setTotalConnected(defObj.getTotalConnected() + (Integer) hm.get("TC-" + theDate));
                        if (hm.get("WC-" + theDate) != null)
                            defObj.setWaitCallnbr(defObj.getWaitCallnbr() + (Integer) hm.get("WC-" + theDate));
                        if (hm.get("RC-" + theDate) != null)
                            defObj.setReportCount(defObj.getReportCount() + (Integer) hm.get("RC-" + theDate));

                        if (hm.get("TLR-" + theDate) != null)
                            defObj.setTotalLoginReady(defObj.getTotalLoginReady() + (Integer) hm.get("TLR-" + theDate));
                        if (hm.get("TLNR-" + theDate) != null)
                            defObj.setTotalLoginNotReady(defObj.getTotalLoginNotReady() + (Integer) hm.get("TLNR-" + theDate));
                        if (hm.get("CNA-" + theDate) != null)
                            defObj.setCallNotAnswer(defObj.getCallNotAnswer() + (Integer) hm.get("CNA-" + theDate));

                        if (i == (diffDay - 1)) {
                            serviceObj = new ServiceLevelObject();
                            serviceObj.setDataTime(defObj.getDataTime());
                            serviceObj.setMaxWait(CalendarUtil.formatDate(defObj.getMaxWait()));
                            serviceObj.setMaxCommunication(defObj.getMaxCommunication());
                            if (defObj.getTotalConnected() > 0) {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate((defObj.getAverageCommunication() / defObj.getTotalConnected())));
                                serviceObj.setAverageRang(CalendarUtil.formatDate((defObj.getAverageRang() / defObj.getTotalConnected())));
                                serviceObj.setAverageWait(CalendarUtil.formatDate((defObj.getAverageWait() / defObj.getTotalConnected() - 10) > 0 ? (defObj.getAverageWait() / defObj.getTotalConnected() - 10) : 0));
                            } else {
                                serviceObj.setAverageCommunication(CalendarUtil.formatDate(0));
                                serviceObj.setAverageRang(CalendarUtil.formatDate(0));
                                serviceObj.setAverageWait(CalendarUtil.formatDate(0));
                            }
                            if (defObj.getReportCount() > 0) {
                                serviceObj.setAverageWaitCount(defObj.getWaitCallnbr() / defObj.getReportCount());
                            } else {
                                serviceObj.setAverageWaitCount(0);
                            }
                            if (agentNO > 0) {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(defObj.getTotalLoginReady() / agentNO));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate((defObj.getTotalLoginNotReady() / agentNO)));
                                serviceObj.setAverageCallNotAnswer(defObj.getCallNotAnswer());
                            } else {
                                serviceObj.setAverageLoginReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                                serviceObj.setAverageCallNotAnswer(0);
                            }
                            reportData.addServiceLevelObject(serviceObj);
                        }
                    }

                    //存放最大等候通數
                    if (hm.get("MC-" + theDate) != null) {
                        if ((Integer) hm.get("MC-" + theDate) > defTotalObj.getMaxCommunication()) {
                            defTotalObj.setMaxCommunication((Integer) hm.get("MC-" + theDate));
                        }
                    }

                    //存放最長等待時間
                    if (hm.get("MW-" + theDate) != null) {
                        if ((Integer) hm.get("MW-" + theDate) > defTotalObj.getMaxWait()) {
                            defTotalObj.setMaxWait((Integer) hm.get("MW-" + theDate));
                        }
                    }

                    //加總
                    if (hm.get("AC-" + theDate) != null)
                        defTotalObj.setAverageCommunication(defTotalObj.getAverageCommunication() + (Integer) hm.get("AC-" + theDate));
                    if (hm.get("AR-" + theDate) != null)
                        defTotalObj.setAverageRang(defTotalObj.getAverageRang() + (Integer) hm.get("AR-" + theDate));
                    if (hm.get("AW-" + theDate) != null)
                        defTotalObj.setAverageWait(defTotalObj.getAverageWait() + (Integer) hm.get("AW-" + theDate));
                    if (hm.get("WC-" + theDate) != null)
                        defTotalObj.setWaitCallnbr(defTotalObj.getWaitCallnbr() + (Integer) hm.get("WC-" + theDate));
                    if (hm.get("RC-" + theDate) != null)
                        defTotalObj.setReportCount(defTotalObj.getReportCount() + (Integer) hm.get("RC-" + theDate));

                    if (hm.get("TLR-" + theDate) != null)
                        defTotalObj.setTotalLoginReady(defTotalObj.getTotalLoginReady() + (Integer) hm.get("TLR-" + theDate));
                    if (hm.get("TLNR-" + theDate) != null)
                        defTotalObj.setTotalLoginNotReady(defTotalObj.getTotalLoginNotReady() + (Integer) hm.get("TLNR-" + theDate));
                    if (hm.get("CNA-" + theDate) != null)
                        defTotalObj.setCallNotAnswer(defTotalObj.getCallNotAnswer() + (Integer) hm.get("CNA-" + theDate));
                    if (hm.get("TC-" + theDate) != null)
                        defTotalObj.setTotalConnected(defTotalObj.getTotalConnected() + (Integer) hm.get("TC-" + theDate));
                    fromDate.add(Calendar.HOUR, 1);
                } //所有時間的 for 迴圈
            }
            reportData.setPeriodMaxWait(CalendarUtil.formatDate((defTotalObj.getMaxWait())));
            reportData.setPeriodMaxCommunication(defTotalObj.getMaxCommunication());
            if (defTotalObj.getTotalConnected() > 0) {
                reportData.setPeriodAverageCommunication(CalendarUtil.formatDate((defTotalObj.getAverageCommunication() / defTotalObj.getTotalConnected())));
                reportData.setPeriodAverageRang(CalendarUtil.formatDate((defTotalObj.getAverageRang() / defTotalObj.getTotalConnected())));
                reportData.setPeriodAverageWait(CalendarUtil.formatDate((defTotalObj.getAverageWait() / defTotalObj.getTotalConnected() - 10 > 0) ? defTotalObj.getAverageWait() / defTotalObj.getTotalConnected() - 10 : 0));
            } else {
                reportData.setPeriodAverageCommunication(CalendarUtil.formatDate(0));
                reportData.setPeriodAverageRang(CalendarUtil.formatDate(0));
                reportData.setPeriodAverageWait(CalendarUtil.formatDate(0));
            }
            if (defTotalObj.getReportCount() > 0) {
                reportData.setAverageWaitCount(defTotalObj.getWaitCallnbr() / defTotalObj.getReportCount());
            } else {
                reportData.setAverageWaitCount(0);
            }
            if (agentNO > 0) {
                reportData.setAverageLoginReady(CalendarUtil.formatDate(defTotalObj.getTotalLoginReady() / agentNO));
                reportData.setAverageLoginNotReady(CalendarUtil.formatDate(defTotalObj.getTotalLoginNotReady() / agentNO));
                reportData.setAverageCallNotAnswer(defTotalObj.getCallNotAnswer());
            } else {
                reportData.setAverageLoginReady(CalendarUtil.formatDate(0));
                reportData.setAverageLoginNotReady(CalendarUtil.formatDate(0));
                reportData.setAverageCallNotAnswer(0);
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            String groupStrSQL = "";
            if ("Y".equals(timeType))
                groupStrSQL = " FORMAT(callTime,'yyyy年') ";
            else if ("M".equals(timeType))
                groupStrSQL = " FORMAT(callTime,'yyyy年MM月') ";
            else if ("W".equals(timeType))
                groupStrSQL = " concat(FORMAT(callTime,'yyyy年MM月'),' 第', (DATEPART(week, callTime) - DATEPART(week, DATEADD(day, 1, EOMONTH(callTime, -1)))) + 1  ,'週')";
            else if ("D".equals(timeType))
                groupStrSQL = " FORMAT(callTime,'yyyy年MM月dd日') ";
            else if ("H".equals(timeType))
                groupStrSQL = " FORMAT(callTime,'yyyy年MM月dd日 HH時')";
            else if ("A".equals(timeType))
                groupStrSQL = "callTime";

            //region talk_flow

            String sql = "SELECT BEGINTIME, SUM(CALLCOUNT) CALLCOUNT, SUM(NBRCALLSABANDONED) NBRCALLSABANDONED, SUM(NBRCALLSABANDONEDWAIT) NBRCALLSABANDONEDWAIT, " +
                    "SUM(NBRCALLSANSWERED) NBRCALLSANSWERED FROM (" +
                    "SELECT " +
                    "FORMAT(BEGINTIME,'HH:mm')  BEGINTIME, " +
                    "SUM(CALLCOUNT)CALLCOUNT, SUM(NBRCALLSABANDONED)NBRCALLSABANDONED, " +
                    "SUM(NBRCALLSABANDONEDWAIT)NBRCALLSABANDONEDWAIT, " +
                    "SUM(NBRCALLSANSWERED)NBRCALLSANSWERED " +
                    "FROM M7480_SITE_INFO " +
                    "WHERE BEGINTIME >= ? " +
                    "AND BEGINTIME < DATEADD (day , 1 , cast(? as date) )   AND BEGINTIME < cast('2016-12-22 15:00:00' as datetime) " +
                    "AND datepart(HOUR,BEGINTIME)  >= '08' and  datepart(HOUR,BEGINTIME) < '22' " +
                    "GROUP BY FORMAT(BEGINTIME,'HH:mm')" +
                    "  UNION ALL " +
                    "SELECT " + groupStrSQL + " as   BEGINTIME, " +
                    "SUM(CASE WHEN ANSWERTIME IS NOT NULL OR CALLDISCONNECTINGPARTY > 0 OR CALLIVRDURATION > 0 THEN 1 ELSE 0 END) CALLCOUNT, " +
                    "SUM(CASE WHEN ANSWERTIME IS NULL AND (CALLDISCONNECTINGPARTY > 0 OR CALLIVRDURATION > 0) THEN 1 ELSE 0 END) NBRCALLSABANDONED, " +
                    "SUM(CASE WHEN ANSWERTIME IS NULL AND CALLDISCONNECTINGPARTY > 0 AND CALLIVRDURATION = 0 THEN 1 ELSE 0 END) NBRCALLSABANDONEDWAIT, " +
                    "SUM(CASE WHEN ANSWERTIME IS NOT NULL THEN 1 ELSE 0 END) NBRCALLSANSWERED " +
                    "FROM M7480_CALL " +
                    "WHERE CALLTIME >= ? " +
                    "AND  CALLTIME <= DATEADD (day , 1 , cast(? as date) ) " +
                    "AND datepart(HOUR,CALLTIME)  >= '08' and datepart(HOUR,CALLTIME)  < '22' " +
                    "GROUP BY  " + groupStrSQL + " ) A GROUP BY BEGINTIME";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reportData.getStartDate() + " 00:00:00");
            pstmt.setString(2, reportData.getEndDate() + " 00:00:00");
            pstmt.setString(3, reportData.getStartDate() + " 00:00:00"); // 2017/1/4
            pstmt.setString(4, reportData.getEndDate() + " 00:00:00"); // 2017/1/4
            rs = pstmt.executeQuery();
            int nbrCallSabandOned = 0;
            int nbrCallSaBandOnedWait = 0;
            TalkFlowReportTemp reportTemp = new TalkFlowReportTemp();
            int figure = 0;
            while (rs.next()) {
                reportTemp.setCallByHourValue(rs.getString("BEGINTIME"), reportTemp.ALL_CALL_ANSWERED, rs.getInt("CALLCOUNT"));//進線數
                reportTemp.setCallByHourValue(rs.getString("BEGINTIME"), reportTemp.CALL_ANSWERED, rs.getInt("NBRCALLSANSWERED"));//接通數
                //reportTemp.setCallByHourValue(rs.getString("BEGINTIME"),reportTemp.NO_CALL_ANSWERED,rs.getInt("NBRCALLSABANDONED"));//未等候即放棄數
                //reportTemp.setCallByHourValue(rs.getString("BEGINTIME"),reportTemp.NO_CALL_ANSWERED_WAIT,rs.getInt("NBRCALLSABANDONEDWAIT"));//等候放棄數
                nbrCallSabandOned = rs.getInt("NBRCALLSABANDONED");
                nbrCallSaBandOnedWait = rs.getInt("NBRCALLSABANDONEDWAIT");
                figure = nbrCallSabandOned - nbrCallSaBandOnedWait;
                if (figure < 0) figure = 0;
                reportTemp.setCallByHourValue(rs.getString("BEGINTIME"), reportTemp.NO_CALL_ANSWERED, nbrCallSaBandOnedWait);//未等候即放棄數
                reportTemp.setCallByHourValue(rs.getString("BEGINTIME"), reportTemp.NO_CALL_ANSWERED_WAIT, figure);//等候放棄數
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;
            conn.close();
            conn = null;

            for (int i = 0; i < reportData.getServiceLevelObjectList().size(); i++) {
                ServiceLevelObject serviceLevelObject = (ServiceLevelObject) reportData.getServiceLevelObjectList().get(i);
                String theDate = serviceLevelObject.getDataTime();

                if ("W".equals(timeType)) {
                    theDate = theDate.substring(0, 12);

                }
                serviceLevelObject.setDataAllNbr(reportTemp.getCallByHourValue(theDate, reportTemp.ALL_CALL_ANSWERED));//進線數
                serviceLevelObject.setDataNbr(reportTemp.getCallByHourValue(theDate, reportTemp.CALL_ANSWERED));//接通數
                serviceLevelObject.setDataNoNbr(reportTemp.getCallByHourValue(theDate, reportTemp.NO_CALL_ANSWERED));//未等候即放棄數
                serviceLevelObject.setDataNoNbrWait(reportTemp.getCallByHourValue(theDate, reportTemp.NO_CALL_ANSWERED_WAIT));//等候放棄數


            }

            //算最後加總值
            reportData.setTotalAllNbr(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.ALL_CALL_ANSWERED));//總進線數
            reportData.setTotalNbr(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.CALL_ANSWERED));//總接通數
            reportData.setTotalNoNbr(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.NO_CALL_ANSWERED));//未等候即放棄數
            reportData.setTotalNoNbrWait(reportTemp.getCallByHourValue(reportTemp.THE_TOTAL, reportTemp.NO_CALL_ANSWERED_WAIT));//等候放棄數

            //endregion

            beans.put("reportData", reportData);
            String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/TalkFlow.xls");
            File file = new File(path);
            InputStream in = new FileInputStream(file);

            XLSTransformer transformer = new XLSTransformer();
            Workbook workbook = transformer.transformXLS(in, beans);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

//            IOUtils.closeQuietly(in);
//            IOUtils.closeQuietly(outputStream);
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
            } catch (SQLException ex) {
                ;
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
            } catch (SQLException ex) {
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
        } // finally
    }
}
