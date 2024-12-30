/****************************************************************************
 *
 * Copyright (c) 2008 ESound Tech. All Rights Reserved.
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
 *     File name:       CallAmountAll.java
 *
 *     History:
 *     Date               Author                  Comments
 *     -----------------------------------------------------------------------
 *     Aug 08, 2011       Aaron                Initial Release
 *     Dec 08, 2017       Vim                  增加LINE話務
 *****************************************************************************/
package com.consilium.excel.esound;

import com.consilium.excel.componet.ApplicationCode;
import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.DynaHashValue;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("CallAmountCompareAllExcelImpl")
public class CallAmountCompareAll implements ExcelInterface {


    private enum sqlChoice {
        KPI,//KPI
        sectionOfInform, // 各時段通報數
        callWaiting, // 話務分時等候數
        typeOfDisposal, // 處置類別總數
        callWaitingCount // 話務分析，最多等候人數
    }

    private enum callWaitingCountType {
        MAXWAITCALLNBR, // 最多等候人數
        AVERAGEWAIT, // 平均等候時間
        AVERAGECOMMUNICATION // 平均通話時間
    }

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String[] sqlQuery = new String[sqlChoice.values().length];

        Map beans = new HashMap();

        NumberFormat format = NumberFormat.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();
        //String bDataTime_YYYY = request.getParameter("bDataTime_YYYY");
        //String eDataTime_YYYY = request.getParameter("eDataTime_YYYY");

        format.setMinimumIntegerDigits(2);
        //String bDataTime_MM = format.format(Long.valueOf(request.getParameter("bDataTime_MM")));
        //String bDataTime_DD = format.format(Long.valueOf(request.getParameter("bDataTime_DD")));
        //String eDataTime_MM = format.format(Long.valueOf(request.getParameter("eDataTime_MM")));
        //String eDataTime_DD = format.format(Long.valueOf(request.getParameter("eDataTime_DD")));
        ArrayList<String> al = new ArrayList<String>();

        //String acceptType = request.getParameter("acceptType");
        String acceptType = jsonNode.get("acceptType").asText();
        String dateType = jsonNode.get("dateType").asText();
        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();

        String bDataTime_YYYY = startDate.substring(0, 4);
        String eDataTime_YYYY = endDate.substring(0, 4);
        String bDataTime_MM = startDate.substring(5, 7);
        String bDataTime_DD = startDate.substring(8, 10);
        String eDataTime_MM = endDate.substring(5, 7);
        String eDataTime_DD = endDate.substring(8, 10);

        String printDate = dateFormater.format(now.getTime());
        UserObject userObj = UserInstance.getInstance().getUserObject(httpServletRequest);
        String userName = userObj.getUserName();
        beans.put("startDate", startDate);
        beans.put("endDate", endDate);
        beans.put("printDate", printDate);
        beans.put("userName", userName);
        //取得 展現方式 依年Y 依月M 依週W 依日D 依時H 依總數A
        //String timeType = request.getParameter("timeType");
        String timeType = jsonNode.get("timeType").asText();

        //計算時間
        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        Calendar tempDate = Calendar.getInstance();
        fromDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1,
                Integer.parseInt(bDataTime_DD), 0, 0, 0);
        toDate.set(Integer.parseInt(eDataTime_YYYY), Integer.parseInt(eDataTime_MM) - 1, Integer.parseInt(eDataTime_DD),
                0, 0, 0);
        tempDate.set(Integer.parseInt(bDataTime_YYYY), Integer.parseInt(bDataTime_MM) - 1,
                Integer.parseInt(bDataTime_DD), 0, 0, 0);

        //  取得String-年
        String tempYear = Integer.toString(tempDate.get(Calendar.YEAR));

        //  取得String-月，缺0補0
        String tempMonth = "";
        int tempIntMonth = tempDate.get(Calendar.MONTH) + 1;
        if (tempIntMonth < 10)
            tempMonth = "0" + Integer.toString(tempIntMonth);
        else
            tempMonth = Integer.toString(tempIntMonth);

        //  WeekOfYear
        String tempWeekOfYear = "";
        int tempIntWeekOfYear = tempDate.get(Calendar.WEEK_OF_YEAR) + 1;
        if (tempIntWeekOfYear < 10)
            tempWeekOfYear = "0" + Integer.toString(tempIntWeekOfYear);
        else
            tempWeekOfYear = Integer.toString(tempIntWeekOfYear);

        //  WeekOfMonth
        String tempWeekOfMonth = "";
        int tempIntWeekOfMonth = tempDate.get(Calendar.WEEK_OF_MONTH);
        if (tempIntWeekOfMonth < 10)
            tempWeekOfMonth = "0" + Integer.toString(tempIntWeekOfMonth);
        else
            tempWeekOfMonth = Integer.toString(tempIntWeekOfMonth);

        //  Week
        // int tempWeek = 0;

        //  取得String-日，缺0補0
        String tempDay = "";
        int tempIntDay = tempDate.get(Calendar.DAY_OF_MONTH);
        if (tempIntDay < 10)
            tempDay = "0" + Integer.toString(tempIntDay);
        else
            tempDay = Integer.toString(tempIntDay);

        //計算年數、月份數、天數
        int cntDay = (int) ((toDate.getTimeInMillis() - fromDate.getTimeInMillis()) / (1000 * 60 * 60) + 24) / 24;
        int cntYear = (int) (toDate.get((Calendar.YEAR)) - fromDate.get((Calendar.YEAR))) + 1;

        int cntMonth = 0;
        if (cntYear == 1)
            cntMonth = (int) (toDate.get(Calendar.MONTH) - fromDate.get(Calendar.MONTH)) + 1;
        else
            cntMonth = ((12 - fromDate.get(Calendar.MONTH)) + (toDate.get(Calendar.MONTH) + 1) + ((cntYear - 2) * 12));

        int i = 0;

        HashMap hp = new HashMap();
        HashMap hp2 = new HashMap(); // 週分類時，key為日期索引，object為hp3
        HashMap hp3 = new HashMap(); // 週分類時，key為"G01"類別，object為次數加總
        DynaHashValue report = new DynaHashValue();
        beans.put("report", report);

        String acceptTypeSql = "";

        if ("L".equals(acceptType) || "I".equals(acceptType)) {
            acceptTypeSql = " AND CASETYPE = ? ";
        } else if ("T".equals(acceptType)) {
            acceptTypeSql = " AND CASETYPE NOT IN(?, ?) ";
        } else if ("TL".equals(acceptType)) {
            acceptTypeSql = " AND CASETYPE NOT IN(?) ";
        }

        String dateColumn = "";
        //依年Y 依月M 依週W 依日D產生Obj和Sql
        if ("Y".equals(timeType)) {
            for (i = 0; i < cntYear; i++) {
                tempYear = Integer.toString(tempDate.get(Calendar.YEAR));
                dateColumn = tempYear + "年";
                hp.put(tempYear, dateColumn);
                tempDate.add(Calendar.YEAR, 1);
                report.addRowTitle(dateColumn, dateColumn);
            }
            sqlQuery[sqlChoice.sectionOfInform.ordinal()] =
                    " SELECT SERVICETYPE,                                           " +
                            " CASETYPE," +
                            "    datepart(yyyy,CREATETIME) AS YEAR, COUNT(*) AS CNT         " +
                            " FROM                                                          " +
                            "   CALLLOG_FORM                                                " +
                            " WHERE                                                         " +
                            "   CREATETIME>=?            " +
                            " AND                                                           " +
                            "   CREATETIME<=?          " + acceptTypeSql +
                            " AND                                                           " +
                            "   NOT EXISTS                                                  " +
                            "      (SELECT 1 FROM M7480_CALL M                              " +
                            "       WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid          " +
                            "       AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                               " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                      " +
                            "   SERVICETYPE, CASETYPE, datepart(yyyy,CREATETIME)                     " +
                            " ORDER BY                                                      " +
                            "   SERVICETYPE,  datepart(yyyy,CREATETIME)                     ";

            sqlQuery[sqlChoice.callWaiting.ordinal()] =
                    " SELECT datepart(yyyy,CALLTIME) AS YEAR,                  " +
                            "   SUM(CASE WHEN (CALLTIME >=cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                            "   (CALLTIME <cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) NBRCALLSABANDONEDWAIT           " +
                            " FROM                                                        " +
                            "   M7480_CALL                                           " +
                            " WHERE                                                       " +
                            "   CALLTIME >= ?          " +
                            " AND                                                         " +
                            "   CALLTIME <= ? " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CALLTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : "")) +
                            ("TL".equals(acceptType) || "T".equals(acceptType) || "L".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL AND RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22' " +
                                            " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    : ("I".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL " +
                                            " AND EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    :
                                    " AND ANSWERTIME IS NULL AND ((RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22') " +
                                            " OR EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID))")) +
                            " AND NOT EXISTS(SELECT 1 FROM CTI_ID_LOSS CL WHERE CL.CALLID = M7480_CALL.M7480CALLID AND CL.STATUS <> 0) " +
                            " GROUP BY datepart(yyyy,CALLTIME)                         ";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                       " +
                            "   PROCESSTYPE, COUNT(*) AS CNT,                              " +
                            "    datepart(yyyy,CREATETIME) AS YEAR                         " +
                            " FROM                                                         " +
                            "   CALLLOG_FORM                                               " +
                            " WHERE                                                        " +
                            "   CREATETIME>=?           " +
                            " AND                                                          " +
                            "   CREATETIME<=?           " + acceptTypeSql +
                            " AND                                                          " +
                            "   PROCESSTYPE IS NOT NULL                                    " +
                            " AND                                                          " +
                            "   NOT EXISTS                                                 " +
                            "      (SELECT 1 FROM M7480_CALL M                             " +
                            "       WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid         " +
                            "       AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                              " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME)                    " +
                            " ORDER BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME)                    ";

            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                            " +
                            "   DATEPART(YYYY, BEGINTIME)               AS YEAR,                             " +
                            "   MAX(MAXWAITCALLNBR) MAXWAITCALLNBR,                             " + //最多等待通數
                            "   SUM(SUMCALLSAGENTCOMMDURATION) SUMCALLSAGENTCOMMDURATION,       " + //總通話時間
                            //"   SUM(SUMCALLSDURATIONBEFOREANSWER) SUMCALLSDURATIONBEFOREANSWER, " + //總等待時間
                            "sum ( case when  CAST( BEGINTIME as date) >='2022-12-03' then     SUMCALLSDURATIONBEFOREANSWER +   10* NBRCALLSANSWERED else   SUMCALLSDURATIONBEFOREANSWER   end  ) SUMCALLSDURATIONBEFOREANSWER,"+ //總等待時間
                            "   SUM(NBRCALLSANSWERED) NBRCALLSANSWERED                          " + //總接通數
                            " FROM                                                              " +
                            "   M7480_SITE_INFO                                                 " +
                            " WHERE                                                             " +
                            "   BEGINTIME >= ?                                  " +
                            " AND                                                               " +
                            "   BEGINTIME <= ?                                  " +
                            " AND                                                               " +
                            "   BEGINTIME IS NOT NULL                                           " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,BEGINTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : "")) +

                            " GROUP BY                                                          " +
                            "   DATEPART(YYYY, BEGINTIME)                                                    " +
                            " ORDER BY                                                          " +
                            "   DATEPART(YYYY, BEGINTIME)                                                    ";

            sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT LEFT(DataDate,4) AS 'YEAR',   " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    "SELECT " +
                    " cast(CREATETIME as date) AS SDAY, " +
                    " format(CREATETIME,'yyyy年') AS DataDate, " +
                    " format(CREATETIME,'HH') AS HOUR, " +
                    " 0 XCNT, COUNT(1) AS CCNT " +
                    "FROM CALLLOG_FORM " +
                    "WHERE CREATETIME >= ? " +
                    "AND CREATETIME <= ? AND CASETYPE NOT IN ('I') " +
                    "AND NOT EXISTS " +
                    " (SELECT 1 FROM M7480_CALL M " +
                    " WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid " +
                    " AND m.answertime IS NULL) " +
                    "GROUP BY cast(CREATETIME as date),format(CREATETIME,'yyyy年'),format(CREATETIME,'HH') " +
                    "UNION ALL " +
                    "SELECT " +
                    " cast(CALLTIME as date) AS SDAY, " +
                    " format(CALLTIME,'yyyy年')  DataDate,   " +
                    " format(CALLTIME,'HH') AS HOUR, " +
                    " SUM(CASE WHEN (CALLTIME >= cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                    " (CALLTIME < cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) XCNT, " +
                    " 0 CCNT " +
                    "FROM M7480_CALL " +
                    " WHERE CALLTIME >= ? " +
                    " AND CALLTIME <= ? " +
                    " AND ANSWERTIME IS NULL " +
                    " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) " +
                    " GROUP BY cast(CALLTIME as date), format(CALLTIME,'yyyy年'),format(CALLTIME,'HH')  " +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B " +
                    " GROUP BY  LEFT(DataDate,4)  " +
                    " ORDER BY  LEFT(DataDate,4)  ";

        } else if ("M".equals(timeType)) {
            for (i = 0; i < cntMonth; i++) {
                tempYear = Integer.toString(tempDate.get(Calendar.YEAR));
                tempIntMonth = tempDate.get(Calendar.MONTH) + 1;
                if (tempIntMonth < 10)
                    tempMonth = "0" + Integer.toString(tempIntMonth);
                else
                    tempMonth = Integer.toString(tempIntMonth);

                dateColumn = tempYear + "年" + tempMonth + "月";
                hp.put(tempYear + tempMonth, dateColumn);
                tempDate.add(Calendar.MONTH, 1);
                report.addRowTitle(dateColumn, dateColumn);
            }
            sqlQuery[sqlChoice.sectionOfInform.ordinal()] =
                    " SELECT SERVICETYPE,                                                  " +
                            " CASETYPE," +
                            "    datepart(yyyy,CREATETIME) AS YEAR,                                " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) AS MONTH,                                 " +
                            "   COUNT(*) AS CNT                                                    " +
                            " FROM                                                                 " +
                            "   CALLLOG_FORM                                                       " +
                            " WHERE                                                                " +
                            "   CREATETIME>=?                                      " +
                            " AND                                                                  " +
                            "   CREATETIME<=?                                      " + acceptTypeSql +
                            " AND                                                                  " +
                            "   NOT EXISTS                                                         " +
                            "      (SELECT 1 FROM M7480_CALL M                                     " +
                            "       WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid                 " +
                            "       AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                                      " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                             " +
                            "   SERVICETYPE, CASETYPE, datepart(yyyy,CREATETIME) , RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) " +
                            " ORDER BY                                                             " +
                            "   SERVICETYPE,  datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2)  ";

            sqlQuery[sqlChoice.callWaiting.ordinal()] =
                    " SELECT datepart(yyyy,CALLTIME) AS YEAR,                  " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CALLTIME),'')), 2) AS MONTH,                         " +
                            "   SUM(CASE WHEN (CALLTIME >=cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                            "   (CALLTIME <cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) NBRCALLSABANDONEDWAIT           " +
                            " FROM                                                        " +
                            "   M7480_CALL                                           " +
                            " WHERE                                                       " +
                            "   CALLTIME >= ?          " +
                            " AND                                                         " +
                            "   CALLTIME <= ?          " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CALLTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : "")) +
                            ("TL".equals(acceptType) || "T".equals(acceptType) || "L".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL AND RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22' " +
                                            " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    : ("I".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL " +
                                            " AND EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    :
                                    " AND ANSWERTIME IS NULL AND ((RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22') " +
                                            " OR EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID))")) +
                            " AND NOT EXISTS(SELECT 1 FROM CTI_ID_LOSS CL WHERE CL.CALLID = M7480_CALL.M7480CALLID AND CL.STATUS <> 0) " +
                            " GROUP BY datepart(yyyy,CALLTIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CALLTIME),'')), 2)";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                      1                                 " +
                            "   PROCESSTYPE, COUNT(*) AS CNT,                              " +
                            "    datepart(yyyy,CREATETIME) AS YEAR,                        " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) AS MONTH                          " +
                            " FROM                                                         " +
                            "   CALLLOG_FORM                                               " +
                            " WHERE                                                        " +
                            "   CREATETIME>=?            " +
                            " AND                                                          " +
                            "   CREATETIME<=?            " + acceptTypeSql +
                            " AND                                                          " +
                            "   PROCESSTYPE IS NOT NULL                                    " +
                            " AND                                                          " +
                            "   NOT EXISTS                                                 " +
                            "      (SELECT 1 FROM M7480_CALL M                             " +
                            "       WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid         " +
                            "       AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                              " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) " +
                            " ORDER BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME),RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2)  ";

            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                            " +
                            "   DATEPART(YYYY, BEGINTIME)               AS YEAR,                             " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,BEGINTIME),'')), 2) AS MONTH,                               " +
                            "   MAX(MAXWAITCALLNBR) MAXWAITCALLNBR,                             " + //最多等待通數
                            "   SUM(SUMCALLSAGENTCOMMDURATION) SUMCALLSAGENTCOMMDURATION,       " + //總通話時間
                            //"   SUM(SUMCALLSDURATIONBEFOREANSWER) SUMCALLSDURATIONBEFOREANSWER, " + //總等待時間
                            "sum ( case when  CAST( BEGINTIME as date) >='2022-12-03' then     SUMCALLSDURATIONBEFOREANSWER +   10* NBRCALLSANSWERED else   SUMCALLSDURATIONBEFOREANSWER   end  ) SUMCALLSDURATIONBEFOREANSWER,"+ //總等待時間
                            "   SUM(NBRCALLSANSWERED) NBRCALLSANSWERED                          " + //總接通數
                            " FROM                                                              " +
                            "   M7480_SITE_INFO                                                 " +
                            " WHERE                                                             " +
                            "   BEGINTIME >= ?                                  " +
                            " AND                                                               " +
                            "   BEGINTIME <= ?                                  " +
                            " AND                                                               " +
                            "   BEGINTIME IS NOT NULL                                           " +
                            " GROUP BY                                                          " +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,BEGINTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : "")) +

                            "   DATEPART(YYYY, BEGINTIME)              , RIGHT(CONCAT('0', ISNULL(datepart(MM,BEGINTIME),'')), 2)             " +
                            " ORDER BY                                                          " +
                            "   DATEPART(YYYY, BEGINTIME)              , RIGHT(CONCAT('0', ISNULL(datepart(MM,BEGINTIME),'')), 2)             ";

            sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT LEFT(DataDate,4) AS 'YEAR', SUBSTRING(DATADATE,6,2) AS 'MONTH',   " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    "SELECT " +
                    " cast(CREATETIME as date) AS SDAY, " +
                    " format(CREATETIME,'yyyy年MM月') AS DataDate, " +
                    " format(CREATETIME,'HH') AS HOUR, " +
                    " 0 XCNT, COUNT(1) AS CCNT " +
                    "FROM CALLLOG_FORM " +
                    "WHERE CREATETIME >= ? " +
                    "AND CREATETIME <= ? AND CASETYPE NOT IN ('I') " +
                    "AND NOT EXISTS " +
                    " (SELECT 1 FROM M7480_CALL M " +
                    " WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid " +
                    " AND m.answertime IS NULL) " +
                    "GROUP BY cast(CREATETIME as date),format(CREATETIME,'yyyy年MM月'),format(CREATETIME,'HH') " +
                    "UNION ALL " +
                    "SELECT " +
                    " cast(CALLTIME as date) AS SDAY, " +
                    " format(CALLTIME,'yyyy年MM月')  DataDate,   " +
                    " format(CALLTIME,'HH') AS HOUR, " +
                    " SUM(CASE WHEN (CALLTIME >= cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                    " (CALLTIME < cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) XCNT, " +
                    " 0 CCNT " +
                    "FROM M7480_CALL " +
                    " WHERE CALLTIME >= ? " +
                    " AND CALLTIME <= ? " +
                    " AND ANSWERTIME IS NULL " +
                    " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) " +
                    " GROUP BY cast(CALLTIME as date), format(CALLTIME,'yyyy年MM月'),format(CALLTIME,'HH')  " +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B " +
                    " GROUP BY  LEFT(DataDate,4) , SUBSTRING(DATADATE,6,2) " +
                    " ORDER BY  LEFT(DataDate,4) , SUBSTRING(DATADATE,6,2) ";

        } else if ("W".equals(timeType)) // 邊撈邊塞日期索引
        {

            String ASQL = "  (select  concat(convert(char(10),?,126),'-',DATEPART(iso_week, ?)) as EVERYDATE,       " +
                    "           datepart(yyyy,?) AS YEAR,                    " +
                    "           datepart(mm,?) AS MONTH,                     " +
                    "           (datepart(DD,?) /7 ) + 1 AS WEEK_OF_MONTH,              " +
                    "           DATEPART(iso_week, ?) AS WEEK_OF_YEAR,              " +
                    "           datepart(dd,?) AS DAY                        ";

            StringBuilder A2SQL = new StringBuilder("  ");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate _startDate = LocalDate.parse(startDate, df);

            for (int k = 0; k < cntDay; k++) {
                LocalDate localDateTime = _startDate.plusDays(k);
                A2SQL.append(" union");

                A2SQL.append("  select  concat(convert(char(10),'" + localDateTime.format(df) + "',126),'-',DATEPART(iso_week, '" + localDateTime.format(df) + "')) as EVERYDATE,       ");
                A2SQL.append("           datepart(yyyy,'" + localDateTime.format(df) + "') AS YEAR,                    ");
                A2SQL.append("           datepart(mm,'" + localDateTime.format(df) + "') AS MONTH,                     ");
                A2SQL.append("           (datepart(DD,'" + localDateTime.format(df) + "') /7 ) + 1 AS WEEK_OF_MONTH,              ");
                A2SQL.append("           DATEPART(iso_week, '" + localDateTime.format(df) + "') AS WEEK_OF_YEAR,              ");
                A2SQL.append("           datepart(dd,'" + localDateTime.format(df) + "') AS DAY                        ");


            }

            sqlQuery[sqlChoice.sectionOfInform.ordinal()] =
                    " SELECT                                                        " +
                            "   A1.SERVICETYPE,                                             " +
                            "   A1.CASETYPE,                                             " +
                            "   A1.CNT,                                                     " +
                            "   A2.EVERYDATE,                                               " +
                            "   A2.YEAR,                                                    " +
                            "   A2.MONTH,                                                   " +
                            "   A2.WEEK_OF_MONTH,                                           " +
                            "   A2.WEEK_OF_YEAR,                                            " +
                            "   A2.DAY                                                      " +
                            " FROM                                                          " +
                            "  ( SELECT                                                     " +
                            "       SERVICETYPE,    CASETYPE,                                        " +
                            "       concat(convert(char(10),CREATETIME,126),'-',DATEPART(iso_week, CREATETIME)) AS EVERYDATE,       " +
                            "       COUNT(*) AS CNT                                         " +
                            "    FROM                                                       " +
                            "       CALLLOG_FORM                                            " +
                            "    WHERE                                                      " +
                            "       CREATETIME>=?         " +
                            "    AND                                                        " +
                            "       CREATETIME<=?         " + acceptTypeSql +
                            "    AND                                                        " +
                            "       NOT EXISTS                                              " +
                            "          (SELECT 1 FROM M7480_CALL M                          " +
                            "          WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid       " +
                            "          AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                            " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            "    GROUP BY                                                   " +
                            "       SERVICETYPE, CASETYPE,concat(convert(char(10),CREATETIME,126),'-',DATEPART(iso_week, CREATETIME))        " +
//                            "    ORDER BY                                                   " +
//                            "       SERVICETYPE, concat(convert(char(10),CREATETIME,126),'-',DATEPART(wk, CREATETIME))  )" +
                            "   )A1 right join   " +

                            ASQL +

                            A2SQL +

                            "       )A2                               " +

                            " on                                                         " +
                            "   A2.EVERYDATE  = A1.EVERYDATE                              " +
                            " ORDER BY                                                      " +
                            "   A2.EVERYDATE,A1.SERVICETYPE                                 ";

            sqlQuery[sqlChoice.callWaiting.ordinal()] =
                    " SELECT                                                        " +
                            "   A1.NBRCALLSABANDONEDWAIT,                                   " +
                            "   A2.EVERYDATE,                                               " +
                            "   A2.YEAR,                                                    " +
                            "   A2.MONTH,                                                   " +
                            "   A2.WEEK_OF_MONTH,                                           " +
                            "   A2.WEEK_OF_YEAR,                                            " +
                            "   A2.DAY                                                      " +
                            " FROM                                                          " +
                            "  ( SELECT                                                     " +
                            "       concat(convert(char(10),CALLTIME,126),'-',DATEPART(iso_week, CALLTIME)) AS EVERYDATE,        " +
                            "   SUM(CASE WHEN (CALLTIME >=cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                            "   (CALLTIME <cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) NBRCALLSABANDONEDWAIT           " +
                            "    FROM                                                       " +
                            "       M7480_CALL                                         " +
                            "    WHERE                                                      " +
                            "       CALLTIME>=?          " +
                            "    AND                                                        " +
                            "       CALLTIME<=?          " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CALLTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : "")) +

                            ("TL".equals(acceptType) || "T".equals(acceptType) || "L".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL AND RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22' " +
                                            " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    : ("I".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL " +
                                            " AND EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    :
                                    " AND ANSWERTIME IS NULL AND ((RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22') " +
                                            " OR EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID))")) +
                            " AND NOT EXISTS(SELECT 1 FROM CTI_ID_LOSS CL WHERE CL.CALLID = M7480_CALL.M7480CALLID AND CL.STATUS <> 0) " +
                            "    GROUP BY                                                   " +
                            "       concat(convert(char(10),CALLTIME,126),'-',DATEPART(iso_week, CALLTIME))  ) " +
                            "  A1 right join                 " +

                            ASQL +

                            A2SQL +
                            "       )A2                               " +

                            " on                                                         " +
                            "   A2.EVERYDATE  = A1.EVERYDATE                              " +
                            " ORDER BY                                                      " +
                            "   A2.EVERYDATE                                                ";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                        " +
                            "   A1.PROCESSTYPE,                                             " +
                            "   A1.CNT,                                                     " +
                            "   A2.EVERYDATE,                                               " +
                            "   A2.YEAR,                                                    " +
                            "   A2.MONTH,                                                   " +
                            "   A2.WEEK_OF_MONTH,                                           " +
                            "   A2.WEEK_OF_YEAR,                                            " +
                            "   A2.DAY                                                      " +
                            " FROM                                                          " +
                            "  ( SELECT                                                     " +
                            "       PROCESSTYPE,                                            " +
                            "       concat(convert(char(10),CREATETIME,126),'-',DATEPART(iso_week, CREATETIME)) AS EVERYDATE,       " +
                            "       COUNT(*) AS CNT                                         " +
                            "    FROM                                                       " +
                            "       CALLLOG_FORM                                            " +
                            "    WHERE                                                      " +
                            "       CREATETIME>=?         " +
                            "    AND                                                        " +
                            "       CREATETIME<=?         " + acceptTypeSql +
                            "    AND                                                        " +
                            "       NOT EXISTS                                              " +
                            "          (SELECT 1 FROM M7480_CALL M                          " +
                            "          WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid       " +
                            "          AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                            " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            "    GROUP BY                                                   " +
                            "       PROCESSTYPE, concat(convert(char(10),CREATETIME,126),'-',DATEPART(iso_week, CREATETIME))        " +
//                            "    ORDER BY                                                   " +
//                            "       PROCESSTYPE, concat(convert(char(10),CREATETIME,126),'-',DATEPART(wk, CREATETIME))  ) " +
                            " )A1 right join  " +

                            ASQL +

                            A2SQL +

                            "       )A2                               " +

                            " on                                                         " +
                            "   A2.EVERYDATE  = A1.EVERYDATE                              " +
                            " ORDER BY                                                      " +
                            "   A2.EVERYDATE,A1.PROCESSTYPE                                 ";
            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                        " +
                            "   A1.MAXWAITCALLNBR,                                          " +
                            "   A1.SUMCALLSAGENTCOMMDURATION,                               " +
                            "   A1.SUMCALLSDURATIONBEFOREANSWER,                            " +
                            "   A1.NBRCALLSANSWERED,                                        " +
                            "   A2.EVERYDATE,                                               " +
                            "   A2.YEAR,                                                    " +
                            "   A2.MONTH,                                                   " +
                            "   A2.WEEK_OF_MONTH,                                           " +
                            "   A2.WEEK_OF_YEAR,                                            " +
                            "   A2.DAY                                                      " +
                            " FROM                                                          " +
                            "  ( SELECT                                                     " +
                            "       MAX(MAXWAITCALLNBR) MAXWAITCALLNBR,                     " +
                            "       SUM(SUMCALLSAGENTCOMMDURATION) SUMCALLSAGENTCOMMDURATION, " +
                            //"       SUM(SUMCALLSDURATIONBEFOREANSWER) SUMCALLSDURATIONBEFOREANSWER, " +
                            "sum ( case when  CAST( BEGINTIME as date) >='2022-12-03' then     SUMCALLSDURATIONBEFOREANSWER +   10* NBRCALLSANSWERED else   SUMCALLSDURATIONBEFOREANSWER   end  ) SUMCALLSDURATIONBEFOREANSWER,"+ //總等待時間
                            "       SUM(NBRCALLSANSWERED) NBRCALLSANSWERED,                 " +
                            "       concat(convert(char(10),BEGINTIME,126),'-',DATEPART(iso_week, BEGINTIME)) AS EVERYDATE         " +
                            "    FROM                                                       " +
                            "       M7480_SITE_INFO                                         " +
                            "    WHERE                                                      " +
                            "       BEGINTIME>=?          " +
                            "    AND                                                        " +
                            "       BEGINTIME<=?          " +
                            "    AND                                                        " +
                            "       BEGINTIME IS NOT NULL                                   " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,BEGINTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : "")) +

                            "    GROUP BY                                                   " +

                            "       concat(convert(char(10),BEGINTIME,126),'-',DATEPART(iso_week, BEGINTIME))        " +
//                            "    ORDER BY                                                   " +
//                            "       concat(convert(char(10),BEGINTIME,126),'-',DATEPART(wk, BEGINTIME))  )  " +
                            " )A1 right join" +

                            ASQL +

                            A2SQL +

                            "       )A2                               " +

                            " on                                                         " +
                            "   A2.EVERYDATE  = A1.EVERYDATE                              " +
                            " ORDER BY                                                      " +
                            "   A2.EVERYDATE                                                ";

            sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT concat(FORMAT(SDAY,'yyyy年M月'),' 第', (DATEPART(week, SDAY) - DATEPART(week, DATEADD(day, 1, EOMONTH(SDAY, -1))))+  1  ,'週') as DataDate,   " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    "SELECT " +
                    " cast(CREATETIME as date) AS SDAY, " +
                    " format(CREATETIME,'yyyy年MM月dd日') AS  DataDate, " +
                    " format(CREATETIME,'HH') AS HOUR, " +
                    " 0 XCNT, COUNT(1) AS CCNT " +
                    "FROM CALLLOG_FORM " +
                    "WHERE CREATETIME >= ? " +
                    "AND CREATETIME <= ? AND CASETYPE NOT IN ('I') " +
                    "AND NOT EXISTS " +
                    " (SELECT 1 FROM M7480_CALL M " +
                    " WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid " +
                    " AND m.answertime IS NULL) " +
                    "GROUP BY cast(CREATETIME as date),format(CREATETIME,'yyyy年MM月dd日')  ,format(CREATETIME,'HH')" +
                    "UNION ALL " +
                    "SELECT " +
                    " cast(CALLTIME as date) AS SDAY, " +
                    " format(CALLTIME,'yyyy年MM月dd日') AS  DataDate,   " +
                    " format(CALLTIME,'HH') AS HOUR, " +
                    " SUM(CASE WHEN (CALLTIME >= cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                    " (CALLTIME < cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) XCNT, " +
                    " 0 CCNT " +
                    "FROM M7480_CALL " +
                    " WHERE CALLTIME >= ? " +
                    " AND CALLTIME <= ? " +
                    " AND ANSWERTIME IS NULL " +
                    " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) " +
                    " GROUP BY cast(CALLTIME as date), format(CALLTIME,'yyyy年MM月dd日') ,format(CALLTIME,'HH')" +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B " +
                    " GROUP BY  concat(FORMAT(SDAY,'yyyy年M月'),' 第', (DATEPART(week, SDAY) - DATEPART(week, DATEADD(day, 1, EOMONTH(SDAY, -1))))+  1  ,'週') " +
                    " ORDER BY  concat(FORMAT(SDAY,'yyyy年M月'),' 第', (DATEPART(week, SDAY) - DATEPART(week, DATEADD(day, 1, EOMONTH(SDAY, -1))))+  1  ,'週') ";

        } else if ("D".equals(timeType)) {
            for (i = 0; i < cntDay; i++) {
                tempYear = Integer.toString(tempDate.get(Calendar.YEAR));

                tempIntMonth = tempDate.get(Calendar.MONTH) + 1;
                if (tempIntMonth < 10)
                    tempMonth = "0" + Integer.toString(tempIntMonth);
                else
                    tempMonth = Integer.toString(tempIntMonth);

                tempIntDay = tempDate.get(Calendar.DAY_OF_MONTH);
                if (tempIntDay < 10)
                    tempDay = "0" + Integer.toString(tempIntDay);
                else
                    tempDay = Integer.toString(tempIntDay);

                dateColumn = tempYear + "年" + tempMonth + "月" + tempDay + "日";
                hp.put(tempYear + tempMonth + tempDay, dateColumn);
                tempDate.add(Calendar.DATE, 1);
                report.addRowTitle(dateColumn, dateColumn);
            }
            sqlQuery[sqlChoice.sectionOfInform.ordinal()] =
                    " SELECT SERVICETYPE,                                           " +
                            " CASETYPE," +
                            "    datepart(yyyy,CREATETIME) AS YEAR,                         " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) AS MONTH,                          " +
                            "   (datepart(DD,CREATETIME) /7 ) + 1 AS WEEK,                            " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2) AS DAY,                            " +
                            "   COUNT(*) AS CNT                                             " +
                            " FROM                                                          " +
                            "   CALLLOG_FORM                                                " +
                            " WHERE                                                         " +
                            "   CREATETIME>=?             " +
                            " AND                                                           " +
                            "   CREATETIME<=?             " + acceptTypeSql +
                            " AND                                                           " +
                            "   NOT EXISTS                                                  " +
                            "      (SELECT 1 FROM M7480_CALL M                              " +
                            "       WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid          " +
                            "       AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                               " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                      " +
                            "   SERVICETYPE, CASETYPE, datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) ,(datepart(DD,CREATETIME) /7 ) + 1,RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2)" +
                            " ORDER BY                                                      " +
                            "   SERVICETYPE,  datepart(yyyy,CREATETIME) , RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) ,(datepart(DD,CREATETIME) /7 ) + 1,RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2)";

            sqlQuery[sqlChoice.callWaiting.ordinal()] =
                    " SELECT datepart(yyyy,CALLTIME) AS YEAR,                  " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CALLTIME),'')), 2) AS MONTH,                         " +
                            "   (datepart(DD,CALLTIME) /7 ) + 1 AS WEEK,                           " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(DD,CALLTIME),'')), 2) AS DAY,                           " +
                            "   SUM(CASE WHEN (CALLTIME >=cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                            "   (CALLTIME <cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) NBRCALLSABANDONEDWAIT           " +
                            " FROM                                                        " +
                            "   M7480_CALL                                           " +
                            " WHERE                                                       " +
                            "   CALLTIME >= ?          " +
                            " AND                                                         " +
                            "   CALLTIME <= ?          " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CALLTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : "")) +

                            ("TL".equals(acceptType) || "T".equals(acceptType) || "L".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL AND RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22' " +
                                            " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    : ("I".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL " +
                                            " AND EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    :
                                    " AND ANSWERTIME IS NULL AND ((RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22') " +
                                            " OR EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID))")) +
                            " AND NOT EXISTS(SELECT 1 FROM CTI_ID_LOSS CL WHERE CL.CALLID = M7480_CALL.M7480CALLID AND CL.STATUS <> 0) " +
                            " GROUP BY datepart(yyyy,CALLTIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CALLTIME),'')), 2), (datepart(DD,CALLTIME) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,CALLTIME),'')), 2) ";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                       " +
                            "   PROCESSTYPE, COUNT(*) AS CNT,                              " +
                            "    datepart(yyyy,CREATETIME) AS YEAR,                        " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) AS MONTH,                         " +
                            "   (datepart(DD,CREATETIME) /7 ) + 1 AS WEEK,                           " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2) AS DAY                            " +
                            " FROM                                                         " +
                            "   CALLLOG_FORM                                               " +
                            " WHERE                                                        " +
                            "   CREATETIME>=?            " +
                            " AND                                                          " +
                            "   CREATETIME<=?            " + acceptTypeSql +
                            " AND                                                          " +
                            "   PROCESSTYPE IS NOT NULL                                    " +
                            " AND                                                          " +
                            "   NOT EXISTS                                                 " +
                            "      (SELECT 1 FROM M7480_CALL M                             " +
                            "       WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid         " +
                            "       AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                              " +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2), (datepart(DD,CREATETIME) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2) " +
                            " ORDER BY                                                     " +
                            "   PROCESSTYPE,  datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2), (datepart(DD,CREATETIME) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2) ";

            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                            " +
                            "   DATEPART(YYYY, BEGINTIME)               AS YEAR,                             " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(MM,BEGINTIME),'')), 2) AS MONTH,                               " +
                            "   (datepart(DD,BEGINTIME) /7 ) + 1 AS WEEK,                                 " +
                            "   RIGHT(CONCAT('0', ISNULL(datepart(DD,BEGINTIME),'')), 2) AS DAY,                                 " +
                            "   MAX(MAXWAITCALLNBR) MAXWAITCALLNBR,                             " + //最多等待通數
                            "   SUM(SUMCALLSAGENTCOMMDURATION) SUMCALLSAGENTCOMMDURATION,       " + //總通話時間
                            //"   SUM(SUMCALLSDURATIONBEFOREANSWER) SUMCALLSDURATIONBEFOREANSWER, " + //總等待時間
                            "sum ( case when  CAST( BEGINTIME as date) >='2022-12-03' then     SUMCALLSDURATIONBEFOREANSWER +   10* NBRCALLSANSWERED else   SUMCALLSDURATIONBEFOREANSWER   end  ) SUMCALLSDURATIONBEFOREANSWER,"+ //總等待時間
                            "   SUM(NBRCALLSANSWERED) NBRCALLSANSWERED                          " + //總接通數
                            " FROM                                                              " +
                            "   M7480_SITE_INFO                                                 " +
                            " WHERE                                                             " +
                            "   BEGINTIME >= ?                                  " +
                            " AND                                                               " +
                            "   BEGINTIME <= ?                                  " +
                            " AND                                                               " +
                            "   BEGINTIME IS NOT NULL                                           " +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,BEGINTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : "")) +

                            " GROUP BY                                                          " +
                            "   DATEPART(YYYY, BEGINTIME)              , RIGHT(CONCAT('0', ISNULL(datepart(MM,BEGINTIME),'')), 2), (datepart(DD,BEGINTIME) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,BEGINTIME),'')), 2) " +
                            " ORDER BY                                                          " +
                            "   DATEPART(YYYY, BEGINTIME)              , RIGHT(CONCAT('0', ISNULL(datepart(MM,BEGINTIME),'')), 2), (datepart(DD,BEGINTIME) /7 ) + 1, RIGHT(CONCAT('0', ISNULL(datepart(DD,BEGINTIME),'')), 2) ";

            sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT LEFT(DataDate,4) AS 'YEAR', SUBSTRING(DATADATE,6,2) AS 'MONTH', DATEPART(WEEK,LEFT(DataDate,4)+'-'+SUBSTRING(DATADATE,6,2)+'-'+SUBSTRING(DATADATE,9,2)) AS 'WEEK', SUBSTRING(DATADATE,9,2) AS 'day',   " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    "SELECT " +
                    " cast(CREATETIME as date) AS SDAY, " +
                    " format(CREATETIME,'yyyy年MM月dd日') AS DataDate, " +
                    " format(CREATETIME,'HH') AS HOUR, " +
                    " 0 XCNT, COUNT(1) AS CCNT " +
                    "FROM CALLLOG_FORM " +
                    "WHERE CREATETIME >= ? " +
                    "AND CREATETIME <= ? AND CASETYPE NOT IN ('I') " +
                    "AND NOT EXISTS " +
                    " (SELECT 1 FROM M7480_CALL M " +
                    " WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid " +
                    " AND m.answertime IS NULL) " +
                    "GROUP BY cast(CREATETIME as date),format(CREATETIME,'yyyy年MM月dd日'),format(CREATETIME,'HH') " +
                    "UNION ALL " +
                    "SELECT " +
                    " cast(CALLTIME as date) AS SDAY, " +
                    " format(CALLTIME,'yyyy年MM月dd日')  DataDate,   " +
                    " format(CALLTIME,'HH') AS HOUR, " +
                    " SUM(CASE WHEN (CALLTIME >= cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                    " (CALLTIME < cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) XCNT, " +
                    " 0 CCNT " +
                    "FROM M7480_CALL " +
                    " WHERE CALLTIME >= ? " +
                    " AND CALLTIME <= ? " +
                    " AND ANSWERTIME IS NULL " +
                    " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) " +
                    " GROUP BY cast(CALLTIME as date), format(CALLTIME,'yyyy年MM月dd日'),format(CALLTIME,'HH')  " +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B " +
                    " GROUP BY  LEFT(DataDate,4) , SUBSTRING(DATADATE,6,2) , SUBSTRING(DATADATE,9,2) " +
                    " ORDER BY  LEFT(DataDate,4) , SUBSTRING(DATADATE,6,2) , SUBSTRING(DATADATE,9,2) ";

        } else if ("H".equals(timeType)) {
            String _hour;
            for (i = 0; i <= 23; i++) {
                _hour = format.format(i) + "~" + format.format(i + 1);
                report.addRowTitle(_hour, _hour);
            }

            sqlQuery[sqlChoice.sectionOfInform.ordinal()] =
                    " SELECT SERVICETYPE,                                                  " +
                            " CASETYPE," +
                            "    concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00')) as HOUR,                                " +
                            //" FORMAT(DATEPART(HOUR,createTime),'00') as HOUR," +
                            "   COUNT(*) AS CNT                                                    " +
                            " FROM                                                                 " +
                            "   CALLLOG_FORM                                                       " +
                            " WHERE                                                                " +
                            "   CREATETIME>=?                                      " +
                            " AND                                                                  " +
                            "   CREATETIME<=?                                      " + acceptTypeSql +
                            " AND                                                                  " +
                            "   NOT EXISTS                                                         " +
                            "      (SELECT 1 FROM M7480_CALL M                                     " +
                            "       WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid                 " +
                            "       AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                                      " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                             " +
                            "   SERVICETYPE, CASETYPE, concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00')) " +
                            " ORDER BY                                                             " +
                            "   SERVICETYPE, concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00'))  ";

            sqlQuery[sqlChoice.callWaiting.ordinal()] =
                    " SELECT  concat(FORMAT(DATEPART(HOUR,CALLTIME),'00'),'~',FORMAT(DATEPART(HOUR,CALLTIME)+1,'00')) as HOUR,                  " +

                            "   SUM(CASE WHEN (CALLTIME >=cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                            "   (CALLTIME <cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) NBRCALLSABANDONEDWAIT           " +
                            " FROM                                                        " +
                            "   M7480_CALL                                           " +
                            " WHERE                                                       " +
                            "   CALLTIME >= ?          " +
                            " AND                                                         " +
                            "   CALLTIME <= ?          " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CALLTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : "")) +

                            ("TL".equals(acceptType) || "T".equals(acceptType) || "L".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL AND RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22' " +
                                            " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    : ("I".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL " +
                                            " AND EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    :
                                    " AND ANSWERTIME IS NULL AND ((RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22') " +
                                            " OR EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID))")) +
                            " AND NOT EXISTS(SELECT 1 FROM CTI_ID_LOSS CL WHERE CL.CALLID = M7480_CALL.M7480CALLID AND CL.STATUS <> 0) " +
                            " GROUP BY  concat(FORMAT(DATEPART(HOUR,CALLTIME),'00'),'~',FORMAT(DATEPART(HOUR,CALLTIME)+1,'00')) ";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                       " +
                            "   PROCESSTYPE, COUNT(*) AS CNT,                              " +
                            "    concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00')) as HOUR                        " +

                            " FROM                                                         " +
                            "   CALLLOG_FORM                                               " +
                            " WHERE                                                        " +
                            "   CREATETIME>=?            " +
                            " AND                                                          " +
                            "   CREATETIME<=?            " + acceptTypeSql +
                            " AND                                                          " +
                            "   PROCESSTYPE IS NOT NULL                                    " +
                            " AND                                                          " +
                            "   NOT EXISTS                                                 " +
                            "      (SELECT 1 FROM M7480_CALL M                             " +
                            "       WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid         " +
                            "       AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                              " +
                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                     " +
                            "   PROCESSTYPE,  concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00')) " +
                            " ORDER BY                                                     " +
                            "   PROCESSTYPE,  concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00'))  ";

            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                            " +
                            "    concat(FORMAT(DATEPART(HOUR,BEGINTIME),'00'),'~',FORMAT(DATEPART(HOUR,BEGINTIME)+1,'00'))  AS HOUR,  " +
                            "   MAX(MAXWAITCALLNBR) MAXWAITCALLNBR,                             " + //最多等待通數
                            "   SUM(SUMCALLSAGENTCOMMDURATION) SUMCALLSAGENTCOMMDURATION,       " + //總通話時間
                            //"   SUM(SUMCALLSDURATIONBEFOREANSWER) SUMCALLSDURATIONBEFOREANSWER, " + //總等待時間
                            "sum ( case when  CAST( BEGINTIME as date) >='2022-12-03' then     SUMCALLSDURATIONBEFOREANSWER +   10* NBRCALLSANSWERED else   SUMCALLSDURATIONBEFOREANSWER   end  ) SUMCALLSDURATIONBEFOREANSWER,"+ //總等待時間
                            "   SUM(NBRCALLSANSWERED) NBRCALLSANSWERED                          " + //總接通數
                            " FROM                                                              " +
                            "   M7480_SITE_INFO                                                 " +
                            " WHERE                                                             " +
                            "   BEGINTIME >= ?                                  " +
                            " AND                                                               " +
                            "   BEGINTIME <= ?                                  " +
                            " AND                                                               " +
                            "   BEGINTIME IS NOT NULL                                           " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,BEGINTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : "")) +

                            " GROUP BY                                                          " +
                            "  concat(FORMAT(DATEPART(HOUR,BEGINTIME),'00'),'~',FORMAT(DATEPART(HOUR,BEGINTIME)+1,'00'))      " +
                            " ORDER BY                                                          " +
                            "  concat(FORMAT(DATEPART(HOUR,BEGINTIME),'00'),'~',FORMAT(DATEPART(HOUR,BEGINTIME)+1,'00'))            ";

            sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT hour,   " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    "SELECT " +
                    " cast(CREATETIME as date) AS SDAY, " +
                    " format(CREATETIME,'yyyy年MM月') AS DataDate, " +
                    " format(CREATETIME,'HH') AS HOUR, " +
                    " 0 XCNT, COUNT(1) AS CCNT " +
                    "FROM CALLLOG_FORM " +
                    "WHERE CREATETIME >= ? " +
                    "AND CREATETIME <= ? AND CASETYPE NOT IN ('I') " +
                    "AND NOT EXISTS " +
                    " (SELECT 1 FROM M7480_CALL M " +
                    " WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid " +
                    " AND m.answertime IS NULL) " +
                    "GROUP BY cast(CREATETIME as date),format(CREATETIME,'yyyy年MM月'),format(CREATETIME,'HH') " +
                    "UNION ALL " +
                    "SELECT " +
                    " cast(CALLTIME as date) AS SDAY, " +
                    " format(CALLTIME,'yyyy年MM月')  DataDate,   " +
                    " format(CALLTIME,'HH') AS HOUR, " +
                    " SUM(CASE WHEN (CALLTIME >= cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                    " (CALLTIME < cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) XCNT, " +
                    " 0 CCNT " +
                    "FROM M7480_CALL " +
                    " WHERE CALLTIME >= ? " +
                    " AND CALLTIME <= ? " +
                    " AND ANSWERTIME IS NULL " +
                    " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) " +
                    " GROUP BY cast(CALLTIME as date), format(CALLTIME,'yyyy年MM月'),format(CALLTIME,'HH')  " +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B " +
                    " GROUP BY  hour " +
                    " ORDER BY  hour ";


        } else if ("A".equals(timeType)) {
            report.addRowTitle("總數", "總數");

            sqlQuery[sqlChoice.sectionOfInform.ordinal()] =
                    " SELECT SERVICETYPE,    CASETYPE,   '總數' as '總數' ,                                          " +
                            "   COUNT(*) AS CNT                                                    " +
                            " FROM                                                                 " +
                            "   CALLLOG_FORM                                                       " +
                            " WHERE                                                                " +
                            "   CREATETIME>=?                                      " +
                            " AND                                                                  " +
                            "   CREATETIME<=?                                      " + acceptTypeSql +
                            " AND                                                                  " +
                            "   NOT EXISTS                                                         " +
                            "      (SELECT 1 FROM M7480_CALL M                                     " +
                            "       WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid                 " +
                            "       AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                                      " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                             " +
                            "   SERVICETYPE,CASETYPE" +
                            " ORDER BY                                                             " +
                            "   SERVICETYPE";

            sqlQuery[sqlChoice.callWaiting.ordinal()] =
                    " SELECT   COUNT(*) AS CNT      ,     '總數' as '總數',             " +

                            "   SUM(CASE WHEN (CALLTIME >=cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                            "   (CALLTIME <cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) NBRCALLSABANDONEDWAIT           " +
                            " FROM                                                        " +
                            "   M7480_CALL                                           " +
                            " WHERE                                                       " +
                            "   CALLTIME >= ?          " +
                            " AND                                                         " +
                            "   CALLTIME <= ?          " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CALLTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_CALL.CALLTIME as date))) "
                                    : "")) +

                            ("TL".equals(acceptType) || "T".equals(acceptType) || "L".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL AND RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22' " +
                                            " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    : ("I".equals(acceptType) ?
                                    " AND ANSWERTIME IS NULL " +
                                            " AND EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) "
                                    :
                                    " AND ANSWERTIME IS NULL AND ((RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) >= '08' and RIGHT(CONCAT('0', ISNULL(datepart(HH,CALLTIME),'')), 2) < '22') " +
                                            " OR EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID))")) +
                            " AND NOT EXISTS(SELECT 1 FROM CTI_ID_LOSS CL WHERE CL.CALLID = M7480_CALL.M7480CALLID AND CL.STATUS <> 0) " +
                            " GROUP BY  concat(FORMAT(DATEPART(HOUR,CALLTIME),'00'),'-',FORMAT(DATEPART(HOUR,CALLTIME)+1,'00')) ";

            sqlQuery[sqlChoice.typeOfDisposal.ordinal()] =
                    " SELECT                                                       " +
                            "   PROCESSTYPE,   COUNT(*) AS CNT ,'總數' as '總數'" +
                            " FROM                                                         " +
                            "   CALLLOG_FORM                                               " +
                            " WHERE                                                        " +
                            "   CREATETIME>=?            " +
                            " AND                                                          " +
                            "   CREATETIME<=?            " + acceptTypeSql +
                            " AND                                                          " +
                            "   PROCESSTYPE IS NOT NULL                                    " +
                            " AND                                                          " +
                            "   NOT EXISTS                                                 " +
                            "      (SELECT 1 FROM M7480_CALL M                             " +
                            "       WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid         " +
                            "       AND m.answertime IS NULL AND CALLLOG_FORM.CASETYPE != 'I')                              " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(CALLLOG_FORM.CREATETIME as date))) "
                                    : "")) +

                            " GROUP BY                                                     " +
                            "   PROCESSTYPE" +
                            " ORDER BY                                                     " +
                            "   PROCESSTYPE";

            sqlQuery[sqlChoice.callWaitingCount.ordinal()] =
                    " SELECT                                                            " +
                            "      COUNT(*) AS CNT      ,  '總數' as '總數'," +
                            "   MAX(MAXWAITCALLNBR) MAXWAITCALLNBR,                             " + //最多等待通數
                            "   SUM(SUMCALLSAGENTCOMMDURATION) SUMCALLSAGENTCOMMDURATION,       " + //總通話時間
                          //  "   SUM(SUMCALLSDURATIONBEFOREANSWER) SUMCALLSDURATIONBEFOREANSWER, " + //總等待時間
                            "sum ( case when  CAST( BEGINTIME as date) >='2022-12-03' then     SUMCALLSDURATIONBEFOREANSWER +   10* NBRCALLSANSWERED else   SUMCALLSDURATIONBEFOREANSWER   end  ) SUMCALLSDURATIONBEFOREANSWER,"+ //總等待時間
                            "   SUM(NBRCALLSANSWERED) NBRCALLSANSWERED                          " + //總接通數
                            " FROM                                                              " +
                            "   M7480_SITE_INFO                                                 " +
                            " WHERE                                                             " +
                            "   BEGINTIME >= ?                                  " +
                            " AND                                                               " +
                            "   BEGINTIME <= ?                                  " +
                            " AND                                                               " +
                            "   BEGINTIME IS NOT NULL                                           " +

                            ("H".equals(dateType) ?
                                    " AND ( ((datepart(dd,BEGINTIME) /7) +1) IN ('0', '6')  " +
                                            " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : ("W".equals(dateType) ?
                                    " AND ( ((datepart(dd,CREATETIME) /7) +1) NOT IN ('0', '6')  " +
                                            " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(M7480_SITE_INFO.BEGINTIME as date))) "
                                    : ""));

            sqlQuery[sqlChoice.KPI.ordinal()] = " SELECT " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN XCNT ELSE 0 END) KXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN CCNT ELSE 0 END) KCCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE XCNT END) DXCNT, " +
                    "SUM(CASE WHEN CNT < (CASE WHEN DAYOFF IS NULL AND HOUR >= 9 AND HOUR < 18 THEN (CASE WHEN SDAY >= cast('20220401' as date) THEN 70 ELSE 60 END) ELSE 40 END) THEN 0 ELSE CCNT END) DCCNT " +
                    "FROM ( " +
                    "SELECT SDAY, DataDate,HOUR, SUM(XCNT) XCNT, SUM(CCNT) CCNT, SUM(XCNT+CCNT) CNT,  " +
                    "(SELECT DISTINCT 1 FROM HOLIDAY WHERE DATEOFF = SDAY) AS DAYOFF " +
                    "FROM ( " +
                    "SELECT " +
                    " cast(CREATETIME as date) AS SDAY, " +
                    " format(CREATETIME,'yyyy年MM月') AS DataDate, " +
                    " format(CREATETIME,'HH') AS HOUR, " +
                    " 0 XCNT, COUNT(1) AS CCNT " +
                    "FROM CALLLOG_FORM " +
                    "WHERE CREATETIME >= ? " +
                    "AND CREATETIME <= ? AND CASETYPE NOT IN ('I') " +
                    "AND NOT EXISTS " +
                    " (SELECT 1 FROM M7480_CALL M " +
                    " WHERE M.M7480CALLID = CALLLOG_FORM.m7480callid " +
                    " AND m.answertime IS NULL) " +
                    "GROUP BY cast(CREATETIME as date),format(CREATETIME,'yyyy年MM月'),format(CREATETIME,'HH') " +
                    "UNION ALL " +
                    "SELECT " +
                    " cast(CALLTIME as date) AS SDAY, " +
                    " format(CALLTIME,'yyyy年MM月')  DataDate,   " +
                    " format(CALLTIME,'HH') AS HOUR, " +
                    " SUM(CASE WHEN (CALLTIME >= cast('2016-12-22 15:00:00' as datetime) AND CALLIVRDURATION > 0) OR " +
                    " (CALLTIME < cast('2016-12-22 15:00:00' as datetime) AND CALLDISCONNECTIONSTATUS = 0) THEN 1 ELSE 0 END) XCNT, " +
                    " 0 CCNT " +
                    "FROM M7480_CALL " +
                    " WHERE CALLTIME >= ? " +
                    " AND CALLTIME <= ? " +
                    " AND ANSWERTIME IS NULL " +
                    " AND NOT EXISTS(SELECT 1 FROM M7480_CALLS_TO_EXTERNAL S WHERE S.M7480CALLID = M7480_CALL.M7480CALLID) " +
                    " GROUP BY cast(CALLTIME as date), format(CALLTIME,'yyyy年MM月'),format(CALLTIME,'HH')  " +

                    " ) A GROUP BY SDAY, DataDate,HOUR) B ";


        }

        try {
            // 各時段通報數

            conn = ds.getConnection();
            pstmt = conn.prepareStatement(sqlQuery[sqlChoice.sectionOfInform.ordinal()]);
            int j = 0;
            pstmt.setString(++j, startDate + " 00:00:00");
            pstmt.setString(++j, endDate + " 23:59:59");
            if ("L".equals(acceptType) || "I".equals(acceptType))
                pstmt.setString(++j, acceptType);
            else if ("T".equals(acceptType)) {
                pstmt.setString(++j, "I");
                pstmt.setString(++j, "L");
            } else if ("TL".equals(acceptType)) {
                pstmt.setString(++j, "I");
            }
            if ("W".equals(timeType)) {
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
            }
            rs = pstmt.executeQuery();

            ApplicationCode app = ApplicationCode.getInstance();
            beans.put("app", app);
            Vector<String> colName = app.getCodeCodeList("G01");
            String weekStart = "";
            String weekEnd = "";
            String dayKey = ""; // 日期索引
            String whichType = ""; // 紀錄"G01"類別
            String appCode = ""; // appCode分類
            String tmpMonth = "";
            String tmpWeekOfYear = "";
            String tmpYear = "";
            String caseType = "";
            String casyTypeCode = "CASETYPE_";
            int count = 0;
            int rightWeek = 0;
            boolean checkOne = true; // 判斷開始日期週次的例外情況
            while (rs.next()) {
                whichType = rs.getString("SERVICETYPE");
                caseType = rs.getString("CASETYPE");
               // if ("V".equalsIgnoreCase(caseType)) caseType = "T";

                appCode = "G01";
                if ("Y".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR")), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR")), appCode + "Z", rs.getInt("CNT"));
                    }

                    report.addInt((String) hp.get(rs.getString("YEAR")), casyTypeCode + caseType, rs.getInt("CNT"));
                } else if ("M".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")),
                                appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), appCode + "Z",
                                rs.getInt("CNT"));
                    }

                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), casyTypeCode + caseType, rs.getInt("CNT"));
                } else if ("W".equals(timeType)) {
                    String weekOfYear = rs.getString("WEEK_OF_YEAR");
                    String year = rs.getString("YEAR");
                    if (tmpWeekOfYear != null && !tmpWeekOfYear.equals(weekOfYear)) // 以WEEK_OF_YEAR為基準當KEY，避免跨年週次問題
                    {
                        tmpYear = year; // (避免跨年問題)
                        dayKey = year + weekOfYear; // 暫存日期索引(避免跨年問題)
                        tmpWeekOfYear = weekOfYear;
                    } else {
                        dayKey = tmpYear + weekOfYear; // 暫存日期索引 (避免跨年問題)
                    }

                    if (hp.get(dayKey) == null) // 新創日期 hp(dayKey,dateColumn)
                    {
                        weekStart = rs.getString("DAY");
                        weekEnd = rs.getString("DAY");
                        hp3 = new HashMap(); // 一個dayKey對應一個hp3，代表一個日期對應多個"G01"類別(key)及次數加總(object)

                        if (checkOne) // 開始日期的週次參照WEEK_OF_MONTH
                        {
                            rightWeek = rs.getInt("WEEK_OF_MONTH");
                            dateColumn =
                                    rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                            weekStart + "~" + weekEnd + "日)";

                            checkOne = false;
                        } else // 非開始日期
                        {
                            if (tmpMonth.equals(rs.getString("MONTH"))) // 同月時
                            {
                                rightWeek++;
                                dateColumn =
                                        rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                weekStart + "~" + weekEnd + "日)";
                            } else // 不同月時
                            {
                                if ("01".equals(rs.getString("DAY"))) // 剛好為當月1號時，第一周開始算
                                {
                                    rightWeek = 1;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                } else // 否則第二周開始算
                                {
                                    rightWeek = 2;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                }
                            }
                        }
                        tmpMonth = rs.getString("MONTH");
                        //hp.put(dayKey, dateColumn);
                    } else // 有該物件時，更正結束日期
                    {
                        weekEnd = rs.getString("DAY");
                        dateColumn =
                                tmpYear + "年" + tmpMonth + "月 第" + rightWeek + "週 (" + weekStart + "~" + weekEnd + "日)";
                    }
                    hp.put(dayKey, dateColumn); // 目的為了過濾獲得正確的日期區間
                    hp2.put(dayKey, hp3); // 目的為了獲取每個日期的"G01"類別的次數總和
                    if (colName.contains(whichType)) {
                        if (hp3.get(whichType) == null) // 第一次存取此"G01"類別
                        {
                            hp3.put(whichType, rs.getInt("CNT"));
                        } else // 兩次以上存取此"G01"類別，將次數加總
                        {
                            count = (Integer) hp3.get(whichType);
                            hp3.put(whichType, count + rs.getInt("CNT"));
                        }
                    } else {
                        if (hp3.get("Z") == null) // 第一次存取此"G01"類別
                        {
                            hp3.put("Z", rs.getInt("CNT"));
                        } else // 兩次以上存取此"G01"類別，將次數加總
                        {
                            count = (Integer) hp3.get("Z");
                            hp3.put("Z", count + rs.getInt("CNT"));
                        }
                    }

                    //if ("T".equalsIgnoreCase(caseType) || "L".equalsIgnoreCase(caseType)) {
                    if (hp3.containsKey(casyTypeCode + caseType)) {
                        count = (Integer) hp3.get(casyTypeCode + caseType);
                        hp3.put(casyTypeCode + caseType, count + rs.getInt("CNT"));
                    } else {
                        hp3.put(casyTypeCode + caseType, rs.getInt("CNT"));
                    }
                    // }
                    if (!al.contains(dayKey)) // 暫存日期索引
                    {
                        al.add(dayKey);
                    }
                } else if ("D".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") +
                                rs.getString("DAY")), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") +
                                rs.getString("DAY")), appCode + "Z", rs.getInt("CNT"));
                    }

                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")), casyTypeCode + caseType, rs.getInt("CNT"));
                } else if ("H".equals(timeType)) {

                    if (colName.contains(whichType)) {
                        report.addInt(rs.getString("HOUR"), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt(rs.getString("HOUR"), appCode + "Z", rs.getInt("CNT"));
                    }

                    report.addInt(rs.getString("HOUR"), casyTypeCode + caseType, rs.getInt("CNT"));
                } else if ("A".equals(timeType)) {

                    if (colName.contains(whichType)) {
                        report.addInt(rs.getString("總數"), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt(rs.getString("總數"), appCode + "Z", rs.getInt("CNT"));
                    }

                    report.addInt(rs.getString("總數"), casyTypeCode + caseType, rs.getInt("CNT"));
                }


            }
            // add by aaron 塞週類別資料到report
            HashMap tmpHp = new HashMap();
            Vector<String> caseTypeColName = app.getCodeCodeList("G02");
            caseTypeColName.add("V");
            if ("W".equals(timeType)) {
                for (String key : al) {
                    report.addRowTitle((String) hp.get(key), (String) hp.get(key)); // 塞日期索引
                    tmpHp = (HashMap) hp2.get(key);
                    for (String type : colName) {
                        if (tmpHp.containsKey(type)) {
                            report.addInt((String) hp.get(key), appCode + type, (Integer) tmpHp.get(type));
                        }
                    }


                    for (String type : caseTypeColName) {
                        if (tmpHp.containsKey(casyTypeCode+ type)) {
                            report.addInt((String) hp.get(key), casyTypeCode + type, (Integer) tmpHp.get(casyTypeCode + type));
                        }
                    }



                }
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            // 話務分時等候數
            tmpWeekOfYear = "";
            tmpMonth = "";
            tmpYear = "";
            dayKey = ""; // 日期索引
            weekStart = "";
            weekEnd = "";
            dateColumn = "";
            rightWeek = 0;
            count = 0;
            checkOne = true; // 判斷開始日期週次的例外情況
            hp2 = new HashMap(); // 週分類時，key為日期索引，object為hp3
            hp3 = new HashMap(); // 週分類時，key為"G01"類別，object為次數加總

            pstmt = conn.prepareStatement(sqlQuery[sqlChoice.callWaiting.ordinal()]);
            j = 0;
            pstmt.setString(++j, startDate + " 00:00:00");
            pstmt.setString(++j, endDate + " 23:59:59");
            if ("W".equals(timeType)) {
                hp = new HashMap();
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                whichType = "wait";
                if ("Y".equals(timeType)) {
                    if (!"L".equals(acceptType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR")), whichType,
                                rs.getInt("NBRCALLSABANDONEDWAIT"));
                    }
                } else if ("M".equals(timeType)) {
                    if (!"L".equals(acceptType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), whichType,
                                rs.getInt("NBRCALLSABANDONEDWAIT"));
                    }
                } else if ("W".equals(timeType)) {
                    String weekOfYear = rs.getString("WEEK_OF_YEAR");
                    String year = rs.getString("YEAR");
                    if (tmpWeekOfYear != null && !tmpWeekOfYear.equals(weekOfYear)) // 以WEEK_OF_YEAR為基準當KEY，避免跨年週次問題
                    {
                        tmpYear = year; // (避免跨年問題)
                        dayKey = year + weekOfYear; // 暫存日期索引(避免跨年問題)
                        tmpWeekOfYear = weekOfYear;
                    } else {
                        dayKey = tmpYear + weekOfYear; // 暫存日期索引 (避免跨年問題)
                    }

                    if (hp.get(dayKey) == null) // 新創日期 hp(dayKey,dateColumn)
                    {
                        weekStart = rs.getString("DAY");
                        weekEnd = rs.getString("DAY");
                        hp3 = new HashMap(); // 一個dayKey對應一個hp3，代表一個日期對應多個"G01"類別(key)及次數加總(object)

                        if (checkOne) // 開始日期的週次參照WEEK_OF_MONTH
                        {
                            rightWeek = rs.getInt("WEEK_OF_MONTH");
                            dateColumn =
                                    rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                            weekStart + "~" + weekEnd + "日)";

                            checkOne = false;
                        } else // 非開始日期
                        {
                            if (tmpMonth.equals(rs.getString("MONTH"))) // 同月時
                            {
                                rightWeek++;
                                dateColumn =
                                        rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                weekStart + "~" + weekEnd + "日)";
                            } else // 不同月時
                            {
                                if ("01".equals(rs.getString("DAY"))) // 剛好為當月1號時，第一周開始算
                                {
                                    rightWeek = 1;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                } else // 否則第二周開始算
                                {
                                    rightWeek = 2;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                }
                            }
                        }
                        tmpMonth = rs.getString("MONTH");
                        //hp.put(dayKey, dateColumn);
                    } else // 有該物件時，更正結束日期
                    {
                        weekEnd = rs.getString("DAY");
                        dateColumn =
                                tmpYear + "年" + tmpMonth + "月 第" + rightWeek + "週 (" + weekStart + "~" + weekEnd + "日)";
                    }
                    hp.put(dayKey, dateColumn); // 目的為了過濾獲得正確的日期區間
                    hp2.put(dayKey, hp3); // 目的為了獲取每個日期的"G01"類別的次數總和
                    if (hp3.get(whichType) == null) // 第一次存取此"G01"類別
                    {
                        if (!"L".equals(acceptType)) {
                            hp3.put(whichType, rs.getInt("NBRCALLSABANDONEDWAIT"));
                        }
                    } else // 兩次以上存取此"G01"類別，將次數加總
                    {
                        count = (Integer) hp3.get(whichType);
                        if (!"L".equals(acceptType)) {
                            hp3.put(whichType, count + rs.getInt("NBRCALLSABANDONEDWAIT"));
                        }
                    }
                    if (!al.contains(dayKey)) // 暫存日期索引
                    {
                        al.add(dayKey);
                    }
                } else if ("D".equals(timeType)) {
                    if (!"L".equals(acceptType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") +
                                        rs.getString("DAY")), whichType,
                                rs.getInt("NBRCALLSABANDONEDWAIT"));
                    }
                } else if ("H".equals(timeType)) {
                    if (!"L".equals(acceptType)) {
                        report.addInt(rs.getString("HOUR"), whichType,
                                rs.getInt("NBRCALLSABANDONEDWAIT"));
                    }
                } else if ("A".equals(timeType)) {
                    if (!"L".equals(acceptType)) {
                        report.addInt(rs.getString("總數"), whichType,
                                rs.getInt("NBRCALLSABANDONEDWAIT"));
                    }
                }
            }
            // add by aaron 塞週類別資料到report
            tmpHp = new HashMap();
            if ("W".equals(timeType)) {
                for (String key : al) {
                    //report.addRowTitle( ( String )hp.get( key ), ( String )hp.get( key ) ) ;
                    tmpHp = (HashMap) hp2.get(key);
                    if (tmpHp != null && tmpHp.containsKey("wait")) {
                        if (!"L".equals(acceptType)) {
                            report.addInt((String) hp.get(key), "wait", (Integer) tmpHp.get("wait"));
                        }
                    }
                }
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            // 處置類別總數
            tmpWeekOfYear = "";
            tmpMonth = "";
            tmpYear = "";
            dayKey = ""; // 日期索引
            weekStart = "";
            weekEnd = "";
            dateColumn = "";
            appCode = "G03";
            rightWeek = 0;
            count = 0;
            checkOne = true; // 判斷開始日期週次的例外情況
            colName = app.getCodeCodeList("G03");
            hp2 = new HashMap(); // 週分類時，key為日期索引，object為hp3
            hp3 = new HashMap(); // 週分類時，key為"G03"類別，object為次數加總

            pstmt = conn.prepareStatement(sqlQuery[sqlChoice.typeOfDisposal.ordinal()]);
            j = 0;
            pstmt.setString(++j, startDate + " 00:00:00");
            pstmt.setString(++j, endDate + " 23:59:59");
            if ("L".equals(acceptType) || "I".equals(acceptType))
                pstmt.setString(++j, acceptType);
            else if ("T".equals(acceptType)) {
                pstmt.setString(++j, "L");
                pstmt.setString(++j, "I");
            } else if ("TL".equals(acceptType)) {
                pstmt.setString(++j, "I");
            }
            if ("W".equals(timeType)) {
                hp = new HashMap();
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                whichType = rs.getString("PROCESSTYPE");
                if ("Y".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR")), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR")), appCode + "Z", rs.getInt("CNT"));
                    }
                } else if ("M".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")),
                                appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), appCode + "Z",
                                rs.getInt("CNT"));
                    }
                } else if ("W".equals(timeType)) {
                    String weekOfYear = rs.getString("WEEK_OF_YEAR");
                    String year = rs.getString("YEAR");
                    if (tmpWeekOfYear != null && !tmpWeekOfYear.equals(weekOfYear)) // 以WEEK_OF_YEAR為基準當KEY，避免跨年週次問題
                    {
                        tmpYear = year; // (避免跨年問題)
                        dayKey = year + weekOfYear; // 暫存日期索引(避免跨年問題)
                        tmpWeekOfYear = weekOfYear;
                    } else {
                        dayKey = tmpYear + weekOfYear; // 暫存日期索引 (避免跨年問題)
                    }

                    if (hp.get(dayKey) == null) // 新創日期 hp(dayKey,dateColumn)
                    {
                        weekStart = rs.getString("DAY");
                        weekEnd = rs.getString("DAY");
                        hp3 = new HashMap(); // 一個dayKey對應一個hp3，代表一個日期對應多個"G03"類別(key)及次數加總(object)

                        if (checkOne) // 開始日期的週次參照WEEK_OF_MONTH
                        {
                            rightWeek = rs.getInt("WEEK_OF_MONTH");
                            dateColumn =
                                    rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                            weekStart + "~" + weekEnd + "日)";

                            checkOne = false;
                        } else // 非開始日期
                        {
                            if (tmpMonth.equals(rs.getString("MONTH"))) // 同月時
                            {
                                rightWeek++;
                                dateColumn =
                                        rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                weekStart + "~" + weekEnd + "日)";
                            } else // 不同月時
                            {
                                if ("01".equals(rs.getString("DAY"))) // 剛好為當月1號時，第一周開始算
                                {
                                    rightWeek = 1;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                } else // 否則第二周開始算
                                {
                                    rightWeek = 2;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                }
                            }
                        }
                        tmpMonth = rs.getString("MONTH");
                        //hp.put(dayKey, dateColumn);
                    } else // 有該物件時，更正結束日期
                    {
                        weekEnd = rs.getString("DAY");
                        dateColumn =
                                tmpYear + "年" + tmpMonth + "月 第" + rightWeek + "週 (" + weekStart + "~" + weekEnd + "日)";
                    }
                    hp.put(dayKey, dateColumn); // 目的為了過濾獲得正確的日期區間
                    hp2.put(dayKey, hp3); // 目的為了獲取每個日期的"G03"類別的次數總和
                    if (colName.contains(whichType)) {
                        if (hp3.get(whichType) == null) // 第一次存取此"G03"類別
                        {
                            hp3.put(whichType, rs.getInt("CNT"));
                        } else // 兩次以上存取此"G03"類別，將次數加總
                        {
                            count = (Integer) hp3.get(whichType);
                            hp3.put(whichType, count + rs.getInt("CNT"));
                        }
                    } else {
                        if (hp3.get("Z") == null) // 第一次存取此"G03"類別
                        {
                            hp3.put("Z", rs.getInt("CNT"));
                        } else // 兩次以上存取此"G03"類別，將次數加總
                        {
                            count = (Integer) hp3.get("Z");
                            hp3.put("Z", count + rs.getInt("CNT"));
                        }
                    }
                    if (!al.contains(dayKey)) // 暫存日期索引
                    {
                        al.add(dayKey);
                    }
                } else if ("D".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") +
                                rs.getString("DAY")), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") +
                                rs.getString("DAY")), appCode + "Z", rs.getInt("CNT"));
                    }
                } else if ("H".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt(rs.getString("HOUR"), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt(rs.getString("HOUR"), appCode + "Z", rs.getInt("CNT"));
                    }
                } else if ("A".equals(timeType)) {

                    if (colName.contains(whichType)) {
                        report.addInt(rs.getString("總數"), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt(rs.getString("總數"), appCode + "Z", rs.getInt("CNT"));
                    }
                }
            }
            // add by aaron 塞週類別資料到report
            tmpHp = new HashMap();
            if ("W".equals(timeType)) {
                for (String key : al) {
                    //report.addRowTitle( ( String )hp.get( key ), ( String )hp.get( key ) ) ;
                    tmpHp = (HashMap) hp2.get(key);
                    for (String type : colName) {
                        if (tmpHp != null && tmpHp.containsKey(type)) {
                            report.addInt((String) hp.get(key), appCode + type, (Integer) tmpHp.get(type));
                        }
                    }
                }
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            // 話務分析，最多等候人數
            tmpWeekOfYear = "";
            tmpMonth = "";
            tmpYear = "";
            dayKey = ""; // 日期索引
            weekStart = "";
            weekEnd = "";
            dateColumn = "";
            rightWeek = 0;
            count = 0;
            int waitTime = 0;
            int communicationTime = 0;
            int callCount = 0;
            int tmpWaitTime = 0;
            int tmpCommunicationTime = 0;
            int tmpCallCount = 0;
            checkOne = true; // 判斷開始日期週次的例外情況
            hp2 = new HashMap(); // 週分類時，key為日期索引，object為hp3
            hp3 = new HashMap(); // 週分類時，key為"G01"類別，object為次數加總

            pstmt = conn.prepareStatement(sqlQuery[sqlChoice.callWaitingCount.ordinal()]);
            j = 0;
            pstmt.setString(++j, startDate + " 00:00:00");
            pstmt.setString(++j, endDate + " 23:59:59");
            if ("W".equals(timeType)) {
                hp = new HashMap();
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
                pstmt.setString(++j, startDate);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if ("Y".equals(timeType)) {
                    int sumCallsDurationBeforeAnswer =
                            rs.getInt("SUMCALLSDURATIONBEFOREANSWER") - 10 * rs.getInt("NBRCALLSANSWERED");
                    if (sumCallsDurationBeforeAnswer < 0)
                        sumCallsDurationBeforeAnswer = 0;

                    if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR")), callWaitingCountType.MAXWAITCALLNBR.toString(),
                                rs.getInt("MAXWAITCALLNBR"));
                        report.addInt((String) hp.get(rs.getString("YEAR")), callWaitingCountType.AVERAGEWAIT.toString(),
                                (rs.getInt("NBRCALLSANSWERED") > 0) ?
                                        sumCallsDurationBeforeAnswer / rs.getInt("NBRCALLSANSWERED") : 0);
                        report.addInt((String) hp.get(rs.getString("YEAR")),
                                callWaitingCountType.AVERAGECOMMUNICATION.toString(),
                                (rs.getInt("NBRCALLSANSWERED") > 0) ?
                                        rs.getInt("SUMCALLSAGENTCOMMDURATION") / rs.getInt("NBRCALLSANSWERED") : 0);
                    }
                } else if ("M".equals(timeType)) {
                    int sumCallsDurationBeforeAnswer =
                            rs.getInt("SUMCALLSDURATIONBEFOREANSWER") - 10 * rs.getInt("NBRCALLSANSWERED");
                    if (sumCallsDurationBeforeAnswer < 0)
                        sumCallsDurationBeforeAnswer = 0;

                    if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")),
                                callWaitingCountType.MAXWAITCALLNBR.toString(), rs.getInt("MAXWAITCALLNBR"));
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")),
                                callWaitingCountType.AVERAGEWAIT.toString(),
                                (rs.getInt("NBRCALLSANSWERED") > 0) ?
                                        sumCallsDurationBeforeAnswer / rs.getInt("NBRCALLSANSWERED") : 0);
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")),
                                callWaitingCountType.AVERAGECOMMUNICATION.toString(),
                                (rs.getInt("NBRCALLSANSWERED") > 0) ?
                                        rs.getInt("SUMCALLSAGENTCOMMDURATION") / rs.getInt("NBRCALLSANSWERED") : 0);
                    }
                } else if ("W".equals(timeType)) {
                    String weekOfYear = rs.getString("WEEK_OF_YEAR");
                    String year = rs.getString("YEAR");
                    if (tmpWeekOfYear != null && !tmpWeekOfYear.equals(weekOfYear)) // 以WEEK_OF_YEAR為基準當KEY，避免跨年週次問題
                    {
                        tmpYear = year; // (避免跨年問題)
                        dayKey = year + weekOfYear; // 暫存日期索引(避免跨年問題)
                        tmpWeekOfYear = weekOfYear;
                    } else {
                        dayKey = tmpYear + weekOfYear; // 暫存日期索引 (避免跨年問題)
                    }

                    if (hp.get(dayKey) == null) // 新創日期 hp(dayKey,dateColumn)
                    {
                        tmpCallCount = 0;
                        tmpWaitTime = 0;
                        tmpCommunicationTime = 0;
                        weekStart = rs.getString("DAY");
                        weekEnd = rs.getString("DAY");
                        hp3 = new HashMap(); // 一個dayKey對應一個hp3，代表一個日期對應多個"G01"類別(key)及次數加總(object)

                        if (checkOne) // 開始日期的週次參照WEEK_OF_MONTH
                        {
                            rightWeek = rs.getInt("WEEK_OF_MONTH");
                            dateColumn =
                                    rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                            weekStart + "~" + weekEnd + "日)";

                            checkOne = false;
                        } else // 非開始日期
                        {
                            if (tmpMonth.equals(rs.getString("MONTH"))) // 同月時
                            {
                                rightWeek++;
                                dateColumn =
                                        rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                weekStart + "~" + weekEnd + "日)";
                            } else // 不同月時
                            {
                                if ("01".equals(rs.getString("DAY"))) // 剛好為當月1號時，第一周開始算
                                {
                                    rightWeek = 1;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                } else // 否則第二周開始算
                                {
                                    rightWeek = 2;
                                    dateColumn =
                                            rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月 第" + rightWeek + "週 (" +
                                                    weekStart + "~" + weekEnd + "日)";
                                }
                            }
                        }
                        tmpMonth = rs.getString("MONTH");
                        //hp.put(dayKey, dateColumn);
                    } else // 有該物件時，更正結束日期
                    {
                        weekEnd = rs.getString("DAY");
                        dateColumn =
                                tmpYear + "年" + tmpMonth + "月 第" + rightWeek + "週 (" + weekStart + "~" + weekEnd + "日)";

                    }

                    tmpCallCount = tmpCallCount + rs.getInt("NBRCALLSANSWERED");

                    hp.put(dayKey, dateColumn); // 目的為了過濾獲得正確的日期區間
                    hp2.put(dayKey, hp3); // 目的為了獲取每個日期的"G01"類別的次數總和
                    for (callWaitingCountType type : callWaitingCountType.values()) {
                        switch (type) {
                            case MAXWAITCALLNBR:
                                if (hp3.get(type.MAXWAITCALLNBR) == null) // 第一次存取此"G01"類別
                                {
                                    if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                                        hp3.put(type.MAXWAITCALLNBR, rs.getInt("MAXWAITCALLNBR"));
                                    }
                                } else // 兩次以上存取此"G01"類別，將次數加總
                                {
                                    if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                                        count = rs.getInt("MAXWAITCALLNBR");
                                        if (count > (Integer) hp3.get(type.MAXWAITCALLNBR)) {
                                            hp3.put(type.MAXWAITCALLNBR, count);
                                        }
                                    }
                                }
                                break;
                            case AVERAGEWAIT:
                                int sumCallsDurationBeforeAnswer =
                                        rs.getInt("SUMCALLSDURATIONBEFOREANSWER") - 10 * rs.getInt("NBRCALLSANSWERED");
                                if (sumCallsDurationBeforeAnswer < 0)
                                    sumCallsDurationBeforeAnswer = 0;

                                tmpWaitTime = tmpWaitTime + sumCallsDurationBeforeAnswer;
                                if (!"L".equals(acceptType) && !"I".equals(acceptType)) {

                                    hp3.put(type.AVERAGEWAIT, (tmpCallCount > 0) ? tmpWaitTime / tmpCallCount : 0);
                                }
                                break;
                            case AVERAGECOMMUNICATION:
                                tmpCommunicationTime = tmpCommunicationTime + rs.getInt("SUMCALLSAGENTCOMMDURATION");
                                if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                                    hp3.put(type.AVERAGECOMMUNICATION,
                                            (tmpCallCount > 0) ? tmpCommunicationTime / tmpCallCount : 0);
                                }
                                break;
                        }
                    }
                    if (!al.contains(dayKey)) // 暫存日期索引
                    {
                        al.add(dayKey);
                    }
                } else if ("D".equals(timeType)) {
                    int sumCallsDurationBeforeAnswer =
                            rs.getInt("SUMCALLSDURATIONBEFOREANSWER") - 10 * rs.getInt("NBRCALLSANSWERED");
                    if (sumCallsDurationBeforeAnswer < 0)
                        sumCallsDurationBeforeAnswer = 0;

                    if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")),
                                callWaitingCountType.MAXWAITCALLNBR.toString(), rs.getInt("MAXWAITCALLNBR"));
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")),
                                callWaitingCountType.AVERAGEWAIT.toString(),
                                (rs.getInt("NBRCALLSANSWERED") > 0) ?
                                        sumCallsDurationBeforeAnswer / rs.getInt("NBRCALLSANSWERED") : 0);
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") + rs.getString("DAY")),
                                callWaitingCountType.AVERAGECOMMUNICATION.toString(),
                                (rs.getInt("NBRCALLSANSWERED") > 0) ?
                                        rs.getInt("SUMCALLSAGENTCOMMDURATION") / rs.getInt("NBRCALLSANSWERED") : 0);
                    }
                } else if ("H".equals(timeType)) {
                    int sumCallsDurationBeforeAnswer =
                            rs.getInt("SUMCALLSDURATIONBEFOREANSWER") - 10 * rs.getInt("NBRCALLSANSWERED");
                    if (sumCallsDurationBeforeAnswer < 0)
                        sumCallsDurationBeforeAnswer = 0;

                    if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                        report.addInt(rs.getString("HOUR"), callWaitingCountType.MAXWAITCALLNBR.toString(),
                                rs.getInt("MAXWAITCALLNBR"));
                        report.addInt(rs.getString("HOUR"), callWaitingCountType.AVERAGEWAIT.toString(),
                                (rs.getInt("NBRCALLSANSWERED") > 0) ?
                                        sumCallsDurationBeforeAnswer / rs.getInt("NBRCALLSANSWERED") : 0);
                        report.addInt(rs.getString("HOUR"),
                                callWaitingCountType.AVERAGECOMMUNICATION.toString(),
                                (rs.getInt("NBRCALLSANSWERED") > 0) ?
                                        rs.getInt("SUMCALLSAGENTCOMMDURATION") / rs.getInt("NBRCALLSANSWERED") : 0);
                    }
                } else if ("A".equals(timeType)) {

                    int sumCallsDurationBeforeAnswer =
                            rs.getInt("SUMCALLSDURATIONBEFOREANSWER") - 10 * rs.getInt("NBRCALLSANSWERED");
                    if (sumCallsDurationBeforeAnswer < 0)
                        sumCallsDurationBeforeAnswer = 0;

                    if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                        report.addInt(rs.getString("總數"), callWaitingCountType.MAXWAITCALLNBR.toString(),
                                rs.getInt("MAXWAITCALLNBR"));
                        report.addInt(rs.getString("總數"), callWaitingCountType.AVERAGEWAIT.toString(),
                                (rs.getInt("NBRCALLSANSWERED") > 0) ?
                                        sumCallsDurationBeforeAnswer / rs.getInt("NBRCALLSANSWERED") : 0);
                        report.addInt(rs.getString("總數"),
                                callWaitingCountType.AVERAGECOMMUNICATION.toString(),
                                (rs.getInt("NBRCALLSANSWERED") > 0) ?
                                        rs.getInt("SUMCALLSAGENTCOMMDURATION") / rs.getInt("NBRCALLSANSWERED") : 0);
                    }
                }

                int sumCallsDurationBeforeAnswer =
                        rs.getInt("SUMCALLSDURATIONBEFOREANSWER") - 10 * rs.getInt("NBRCALLSANSWERED");
                if (sumCallsDurationBeforeAnswer < 0)
                    sumCallsDurationBeforeAnswer = 0;

                waitTime = waitTime + sumCallsDurationBeforeAnswer;
                communicationTime = communicationTime + rs.getInt("SUMCALLSAGENTCOMMDURATION");
                callCount = callCount + rs.getInt("NBRCALLSANSWERED");
            }

            if (!"L".equals(acceptType) && !"I".equals(acceptType)) {
                beans.put("waitTime", (callCount != 0) ? waitTime / callCount : 0); // 總平均等候時間
                beans.put("communicationTime", (callCount != 0) ? communicationTime / callCount : 0); // 總平均通話時間
            } else {
                beans.put("waitTime", 0); // 總平均等候時間
                beans.put("communicationTime", 0); // 總平均通話時間
            }

            // add by aaron 塞週類別資料到report
            tmpHp = new HashMap();
            if ("W".equals(timeType)) {
                for (String key : al) {
                    //report.addRowTitle( ( String )hp.get( key ), ( String )hp.get( key ) ) ;
                    tmpHp = (HashMap) hp2.get(key);
                    if (tmpHp != null) {
                        for (callWaitingCountType type : callWaitingCountType.values()) {
                            if (tmpHp.containsKey(type)) {
                                report.addInt((String) hp.get(key), type.toString(), (Integer) tmpHp.get(type));
                            }
                        }
                    }

                }
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            //KPI
            if (sqlQuery[sqlChoice.KPI.ordinal()] != null) {

                pstmt = conn.prepareStatement(sqlQuery[sqlChoice.KPI.ordinal()]);
                j = 0;
                pstmt.setString(++j, startDate + " 00:00:00");
                pstmt.setString(++j, endDate + " 23:59:59");
                pstmt.setString(++j, startDate + " 00:00:00");
                pstmt.setString(++j, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                String time = "";

                while (rs.next()) {
                    if ("Y".equals(timeType)) {
                        time = rs.getString("YEAR") + "年";
                        report.addInt(time, "KX", rs.getInt("KXCNT"));
                        report.addInt(time, "KC", rs.getInt("KCCNT"));
                        report.addInt(time, "DX", rs.getInt("DXCNT"));
                        report.addInt(time, "DC", rs.getInt("DCCNT"));
                    } else if ("M".equals(timeType)) {
                        time = rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月";
                        report.addInt(time, "KX", rs.getInt("KXCNT"));
                        report.addInt(time, "KC", rs.getInt("KCCNT"));
                        report.addInt(time, "DX", rs.getInt("DXCNT"));
                        report.addInt(time, "DC", rs.getInt("DCCNT"));
                    } else if ("W".equals(timeType)) {
                        time = rs.getString("DataDate");
                        for (Object o : report.getRowCodeList()) {
                            String columnName = o.toString();
                            if (columnName.substring(0, 12).equals(time)) {
                                report.addInt(columnName, "KX", rs.getInt("KXCNT"));
                                report.addInt(columnName, "KC", rs.getInt("KCCNT"));
                                report.addInt(columnName, "DX", rs.getInt("DXCNT"));
                                report.addInt(columnName, "DC", rs.getInt("DCCNT"));
                            }
                        }


                    } else if ("D".equals(timeType)) {
                        time = rs.getString("YEAR") + "年" + rs.getString("MONTH") + "月" + rs.getString("DAY") + "日";
                        report.addInt(time, "KX", rs.getInt("KXCNT"));
                        report.addInt(time, "KC", rs.getInt("KCCNT"));
                        report.addInt(time, "DX", rs.getInt("DXCNT"));
                        report.addInt(time, "DC", rs.getInt("DCCNT"));


                    } else if ("H".equals(timeType)) {
                        time = rs.getString("HOUR") + "~" + format.format(Integer.parseInt(rs.getString("HOUR")) + 1);
                        report.addInt(time, "KX", rs.getInt("KXCNT"));
                        report.addInt(time, "KC", rs.getInt("KCCNT"));
                        report.addInt(time, "DX", rs.getInt("DXCNT"));
                        report.addInt(time, "DC", rs.getInt("DCCNT"));
                    } else if ("A".equals(timeType)) {
                        time = "總數";
                        report.addInt(time, "KX", rs.getInt("KXCNT"));
                        report.addInt(time, "KC", rs.getInt("KCCNT"));
                        report.addInt(time, "DX", rs.getInt("DXCNT"));
                        report.addInt(time, "DC", rs.getInt("DCCNT"));
                    }

					
					/*
                    time = rs.getString("DataDate");

                    report.addInt(time, "KX", rs.getInt("KXCNT"));
                    report.addInt(time, "KC", rs.getInt("KCCNT"));
                    report.addInt(time, "DX", rs.getInt("DXCNT"));
                    report.addInt(time, "DC", rs.getInt("DCCNT"));
					*/
                }
                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;

            }
            conn.close();
            conn = null;

            String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/CallAmountCompareAll.xls");
            File file = new File(path);
            InputStream in = new FileInputStream(file);

            XLSTransformer transformer = new XLSTransformer();
            Workbook workbook = transformer.transformXLS(in, beans);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            IOUtils.closeQuietly(in);
            //  IOUtils.closeQuietly(outputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)))
                    .body(outputStream.toByteArray());

        } catch (
                Exception e) {
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

