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

@Service("CallAmountTypeExcelImpl")
public class CallAmountTypeExcelImpl implements ExcelInterface {


    private enum sqlChoice {
        sectionOfInform, // 各時段通報數
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
        ArrayList<String> al = new ArrayList<String>();


        String acceptType = jsonNode.get("acceptType").asText();
        String dateType = jsonNode.get("dateType").asText();


        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();

        String bDataTime_YYYY = startDate.substring(0,4);
        String eDataTime_YYYY = endDate.substring(0,4);
        String bDataTime_MM = startDate.substring(5,7);
        String bDataTime_DD = startDate.substring(8,10);
        String eDataTime_MM = endDate.substring(5,7);
        String eDataTime_DD = endDate.substring(8,10);

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
                    " SELECT " +
                            //"SERVICETYPE, " +
                    " case " +
                        " when   SERVICEITEM='A4007'  then " +
                            "case " +
                            "    when CASETYPE ='V' then " +
                            "     'C' " +
                            "    else " +
                            "     '6' " +
                            " end " +
                     " else " +
                        "SERVICETYPE "+
                    "end SERVICETYPE, "+

                            " CALLTYPE,                                         " +
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
                                    : ""  )) +


                            " GROUP BY                                                      " +
                            "   SERVICETYPE,SERVICEITEM,CASETYPE,CALLTYPE,  datepart(yyyy,CREATETIME)                     " +
                            " ORDER BY                                                      " +
                            "   SERVICETYPE,  datepart(yyyy,CREATETIME)                     ";


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
                    " SELECT " +
                            //"SERVICETYPE," +
                            " case " +
                            " when   SERVICEITEM='A4007'  then " +
                            "case " +
                            "    when CASETYPE ='V' then " +
                            "     'C' " +
                            "    else " +
                            "     '6' " +
                            " end " +
                            " else " +
                            "SERVICETYPE "+
                            "end SERVICETYPE, "+


                            " CALLTYPE,                                                 " +
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
                                    : ""  )) +


                            " GROUP BY                                                             " +
                            "   SERVICETYPE,SERVICEITEM,CASETYPE,CALLTYPE,  datepart(yyyy,CREATETIME) , RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) " +
                            " ORDER BY                                                             " +
                            "   SERVICETYPE,  datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2)  ";


        } else if ("W".equals(timeType)) // 邊撈邊塞日期索引
        {

            String ASQL =  "  (select  concat(convert(char(10),?,126),'-',DATEPART(iso_week, ?)) as EVERYDATE,       " +
                    "           datepart(yyyy,?) AS YEAR,                    " +
                    "           format(datepart(mm,?),'00') AS MONTH,                     " +
                    "           (datepart(DD,?) /7 ) + 1 AS WEEK_OF_MONTH,              " +
                    "           DATEPART(iso_week, ?) AS WEEK_OF_YEAR,              " +
                    "           format(datepart(dd,?),'00') AS DAY                        " ;

            StringBuilder A2SQL = new StringBuilder("  ");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate _startDate = LocalDate.parse(startDate, df);

            for (int k = 0; k < cntDay; k++) {
                LocalDate localDateTime = _startDate.plusDays(k);
                A2SQL.append(" union");

                A2SQL.append("  select  concat(convert(char(10),'" + localDateTime.format(df) + "',126),'-',DATEPART(iso_week, '" + localDateTime.format(df) + "')) as EVERYDATE,       ");
                A2SQL.append("           datepart(yyyy,'" + localDateTime.format(df) + "') AS YEAR,                    ");
                A2SQL.append("           format(datepart(mm,'" + localDateTime.format(df) + "'),'00') AS MONTH,                     ");
                A2SQL.append("           (datepart(DD,'" + localDateTime.format(df) + "') /7 ) + 1 AS WEEK_OF_MONTH,              ");
                A2SQL.append("           DATEPART(iso_week, '" + localDateTime.format(df) + "') AS WEEK_OF_YEAR,              ");
                A2SQL.append("           format(datepart(dd,'" + localDateTime.format(df) + "'),'00') AS DAY                        ");


            }

            sqlQuery[sqlChoice.sectionOfInform.ordinal()] =
                    " SELECT                                                        " +
                            "   A1.SERVICETYPE,                                             " +
                            "   A1.CALLTYPE,                                                     " +
                            "   A1.CNT,                                                     " +
                            "   A2.EVERYDATE,                                               " +
                            "   A2.YEAR,                                                    " +
                            "   A2.MONTH,                                                   " +
                            "   A2.WEEK_OF_MONTH,                                           " +
                            "   A2.WEEK_OF_YEAR,                                            " +
                            "   A2.DAY                                                      " +
                            " FROM                                                          " +
                            "  ( SELECT                                                     " +
                            //"       SERVICETYPE, " +
                            " case " +
                            " when   SERVICEITEM='A4007'  then " +
                            "case " +
                            "    when CASETYPE ='V' then " +
                            "     'C' " +
                            "    else " +
                            "     '6' " +
                            " end " +
                            " else " +
                            "SERVICETYPE "+
                            "end SERVICETYPE, "+

                            " CALLTYPE,                                          " +
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
                                    : ""  )) +



                            "    GROUP BY                                                   " +
                            "       SERVICETYPE,SERVICEITEM,CASETYPE,CALLTYPE, concat(convert(char(10),CREATETIME,126),'-',DATEPART(iso_week, CREATETIME))        " +
//                            "    ORDER BY                                                   " +
//                            "       SERVICETYPE, concat(convert(char(10),CREATETIME,126),'-',DATEPART(wk, CREATETIME))  )" +
                            "   )A1 right join   " +
                            ASQL+
                            A2SQL +
                            "       )A2                               " +

                            " on                                                         " +
                            "   A2.EVERYDATE  = A1.EVERYDATE                              " +
                            " ORDER BY                                                      " +
                            "   A2.EVERYDATE,A1.SERVICETYPE                                 ";


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
                    " SELECT " +
                            //"SERVICETYPE,  " +
                            " case " +
                            " when   SERVICEITEM='A4007'  then " +
                            "case " +
                            "    when CASETYPE ='V' then " +
                            "     'C' " +
                            "    else " +
                            "     '6' " +
                            " end " +
                            " else " +
                            "SERVICETYPE "+
                            "end SERVICETYPE, "+

                            " CALLTYPE,                                        " +
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
                                    : ""  )) +


                            " GROUP BY                                                      " +
                            "   SERVICETYPE,SERVICEITEM,CASETYPE,CALLTYPE,  datepart(yyyy,CREATETIME), RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) ,(datepart(DD,CREATETIME) /7 ) + 1,RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2)" +
                            " ORDER BY                                                      " +
                            "   SERVICETYPE,  datepart(yyyy,CREATETIME) , RIGHT(CONCAT('0', ISNULL(datepart(MM,CREATETIME),'')), 2) ,(datepart(DD,CREATETIME) /7 ) + 1,RIGHT(CONCAT('0', ISNULL(datepart(DD,CREATETIME),'')), 2)";

        }else if("H".equals(timeType)){
            String _hour;
            for(i = 0;i<=23;i++){
                _hour = format.format(i) + "~" + format.format(i+1);
                report.addRowTitle(_hour, _hour);
            }

            sqlQuery[sqlChoice.sectionOfInform.ordinal()] =
                    " SELECT " +
                            //"SERVICETYPE, " +
                            " case " +
                            " when   SERVICEITEM='A4007'  then " +
                            "case " +
                            "    when CASETYPE ='V' then " +
                            "     'C' " +
                            "    else " +
                            "     '6' " +
                            " end " +
                            " else " +
                            "SERVICETYPE "+
                            "end SERVICETYPE, "+

                            "CALLTYPE,                                          " +
                            "    concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00')) as HOUR, COUNT(*) AS CNT         " +
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
                                    : ""  )) +


                            " GROUP BY                                                      " +
                            "   SERVICETYPE,SERVICEITEM,CASETYPE,  concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00'))  ,CALLTYPE                    " +
                            " ORDER BY                                                      " +
                            "   SERVICETYPE, concat(FORMAT(DATEPART(HOUR,createTime),'00'),'~',FORMAT(DATEPART(HOUR,createTime)+1,'00'))                     ";
        }
        else if("A".equals(timeType)){
            report.addRowTitle("總數", "總數");
            sqlQuery[sqlChoice.sectionOfInform.ordinal()] =
                    " SELECT" +
                            //" SERVICETYPE, " +
                            " case " +
                            " when   SERVICEITEM='A4007'  then " +
                            "case " +
                            "    when CASETYPE ='V' then " +
                            "     'C' " +
                            "    else " +
                            "     '6' " +
                            " end " +
                            " else " +
                            "SERVICETYPE "+
                            "end SERVICETYPE, "+

                            "     CALLTYPE,                                     " +
                            "   COUNT(*) AS CNT         " +
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
                                    : ""  )) +

                            " GROUP BY                                                      " +
                            "   SERVICETYPE ,SERVICEITEM,CASETYPE  ,CALLTYPE                 " +
                            " ORDER BY                                                      " +
                            "   SERVICETYPE                    ";
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
            int count = 0;
            int rightWeek = 0;
            boolean checkOne = true; // 判斷開始日期週次的例外情況
            while (rs.next()) {
                whichType = rs.getString("SERVICETYPE");
                appCode = "G01";
                if ("Y".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR")), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR")), appCode + "Z", rs.getInt("CNT"));
                    }
                    report.addInt((String) hp.get(rs.getString("YEAR")), "CALLTYPE_" + rs.getString("CALLTYPE"), rs.getInt("CNT"));
                } else if ("M".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")),
                                appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), appCode + "Z",
                                rs.getInt("CNT"));
                    }
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH")), "CALLTYPE_" + rs.getString("CALLTYPE"), rs.getInt("CNT"));
                } else if ("W".equals(timeType)) {
                    String weekOfYear = rs.getString("WEEK_OF_YEAR");
                    String year = rs.getString("YEAR");
                    if (!tmpWeekOfYear.equals(weekOfYear)) // 以WEEK_OF_YEAR為基準當KEY，避免跨年週次問題
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

                    String callType = "CALLTYPE_" + rs.getString("CALLTYPE");
                    if(hp3.containsKey(callType)){
                        count = (Integer) hp3.get(callType);
                        hp3.put(callType, count + rs.getInt("CNT"));
                    }else{
                        hp3.put(callType,  rs.getInt("CNT"));
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
                    report.addInt((String) hp.get(rs.getString("YEAR") + rs.getString("MONTH") +
                            rs.getString("DAY")), "CALLTYPE_" + rs.getString("CALLTYPE"), rs.getInt("CNT"));
                } else if ("H".equals(timeType)) {
                    if (colName.contains(whichType)) {
                        report.addInt(rs.getString("HOUR"), appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt(rs.getString("HOUR"), appCode + "Z", rs.getInt("CNT"));
                    }
                    report.addInt(rs.getString("HOUR"), "CALLTYPE_" + rs.getString("CALLTYPE"), rs.getInt("CNT"));
                } else if ("A".equals(timeType)) {

                    if (colName.contains(whichType)) {
                        report.addInt("總數", appCode + whichType, rs.getInt("CNT"));
                    } else {
                        report.addInt("總數", appCode + "Z", rs.getInt("CNT"));
                    }

                    report.addInt("總數", "CALLTYPE_" + rs.getString("CALLTYPE"), rs.getInt("CNT"));
                }
            }
            // add by aaron 塞週類別資料到report
            HashMap tmpHp = new HashMap();
            if ("W".equals(timeType)) {
                for (String key : al) {
                    report.addRowTitle((String) hp.get(key), (String) hp.get(key)); // 塞日期索引
                    tmpHp = (HashMap) hp2.get(key);
                    for (String type : colName) {
                        if (tmpHp.containsKey(type)) {
                            report.addInt((String) hp.get(key), appCode + type, (Integer) tmpHp.get(type));
                        }
                    }

                    if(tmpHp != null && tmpHp.containsKey("CALLTYPE_C")){
                        report.addInt((String) hp.get(key),  "CALLTYPE_C", (Integer) tmpHp.get("CALLTYPE_C"));
                    }
                    if(tmpHp != null && tmpHp.containsKey("CALLTYPE_H")){
                        report.addInt((String) hp.get(key),  "CALLTYPE_H", (Integer) tmpHp.get("CALLTYPE_H"));
                    }
                }
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;
            conn.close();
            conn = null;

            String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/CallAmountType.xls");
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

