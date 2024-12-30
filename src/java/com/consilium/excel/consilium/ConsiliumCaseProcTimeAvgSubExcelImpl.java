package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.UnitCode;
import com.consilium.excel.models.UserInstance;
import com.consilium.excel.models.UserObject;
import com.consilium.excel.models.caseproctimeavg.CaseProcTimeAvgSubDataObject;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("ConsiliumCaseProcTimeAvgSubExcelImpl")
public class ConsiliumCaseProcTimeAvgSubExcelImpl implements ExcelInterface {


    private static final Logger logger = Logger.getLogger(ConsiliumCaseProcTimeAvgSubExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "案件處理時間統計.xls";

    private String reportPath = "/WEB-INF/report/consilium/CaseProcTimeAvgSub.xls";


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {
        UserObject usrObj = UserInstance.getInstance().getUserObject(httpServletRequest);
        if (usrObj == null) {
            throw new Exception("您尚未登入或已登入逾時!");
        }

        //scoring
        int score1D3D = 2;
        int score3D7D = 0;
        int score7D15D = -1;
        int score15D = -2;

        //Initializations
        Map<String, Object> beans = new HashMap<String, Object>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        String sqlQuery = ""; //暫存sql語法

        NumberFormat format = NumberFormat.getInstance(); //為了跑兩位數
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //格式化日期
        Calendar now = Calendar.getInstance(); //抓現在時間

        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();

        //parameters delivering
        String bDataTime_YYYY = startDate.substring(0, 4);
        String eDataTime_YYYY = endDate.substring(0, 4);
        String mainUnitCode = jsonNode.get("unitCode").asText(null); //unit code
        if ("".equals(mainUnitCode)) mainUnitCode = null;
        String unitName = UnitCode.getInstance().getUnitName("M", mainUnitCode);

        //formations
        format.setMinimumIntegerDigits(2);  //跑兩位數
        String bDataTime_MM = startDate.substring(5, 7);
        String bDataTime_DD = startDate.substring(8, 10);
        String eDataTime_MM = endDate.substring(5, 7);
        String eDataTime_DD = endDate.substring(8, 10);

        //validation
//        if (mainUnitCode == null || mainUnitCode.length() == 0) {
//            throw new Exception("無效的參數!");
//        }

        //store data to the hash table in order to output as excel file
        beans.put("startDate", startDate);
        beans.put("endDate", endDate);
        beans.put("printDate", dateFormater.format(now.getTime()));
        beans.put("unitName", unitName);

        //maps for storing data form each section
        Map<String, Integer> countMap = new HashMap<String, Integer>();//平均處理使用
        Map<String, Integer> countMapCase = new HashMap<String, Integer>(); //平均接案使用
        Map<String, Long> totalTimeMap = new HashMap<String, Long>();//平均處理使用
        Map<String, Long> totalTimeMapCase = new HashMap<String, Long>(); //平均接案使用
        try {

            conn = ds.getConnection();

            Hashtable<String, String> mappingTable = new Hashtable<String, String>();
            Vector<String> vUnitCode = new Vector<String>();

            String spUnitCodeCondition = "";
            //if (mainUnitCode != null && mainUnitCode.length() != 0) {
            //spUnitCodeCondition = "AND (a.SUPERUNIT in (SELECT value FROM   STRING_SPLIT (?, ',')) or  ? is null) ";
            spUnitCodeCondition = "AND (t1.SUPERUNIT in (SELECT value FROM   STRING_SPLIT (?, ',')) or  ? is null) ";
            String sql = "SELECT UNITNAME, UNITCODE "
                    + "FROM UNITS "
                    + "WHERE (SPUNITCODE in (SELECT value FROM   STRING_SPLIT (?, ',')) or  ? is null OR SPUNITCODE = 'E2014') "
                    + "AND unitCode <>'A4007' and (SUPERUNIT  not like '%_D' or SUPERUNIT is null )"
                    + "AND ( (UNITCODE not in (SELECT value FROM   STRING_SPLIT (?, ',')) or ? is null) OR UNITCODE = 'E2014' OR UNITCODE = 'E2001' ) ";
            pstmt2 = conn.prepareStatement(sql);
            pstmt2.setString(1, mainUnitCode);
            pstmt2.setString(2, mainUnitCode);
            pstmt2.setString(3, mainUnitCode);
            pstmt2.setString(4, mainUnitCode);
            rs = pstmt2.executeQuery();
            //}
            while (rs.next()) {
                if (!vUnitCode.contains(rs.getString("UNITCODE"))) {
                    vUnitCode.addElement(rs.getString("UNITCODE"));
                }
                mappingTable.put(rs.getString("UNITCODE"), rs.getString("UNITNAME"));
            }

            String sqlWorkday =
                    "SELECT COUNT(*) WORKDAY "
                            + "FROM "
                            + " (select dateadd(day,ROWNUM-1,cast(? as date)) MYDATE from (SELECT ROW_NUMBER() over( order by userId) as ROWNUM FROM AGENT_TABLE) a where ROWNUM <=?) WEEKDAY  "
                            + " left join HOLIDAY "
                            + "  on WEEKDAY.MYDATE = HOLIDAY.DATEOFF AND HOLIDAY.DATEOFF IS NULL where DATEPART(weekday,MYDATE) NOT IN('1', '7') ";//暫存sql語法
            pstmt1 = conn.prepareStatement(sqlWorkday);

            //New Code
            sqlQuery = "SELECT T1.SUPERUNIT, T1.UNITCODE, T1.UNITNAME, T1.平均接案次數, T1.平均接案時間, T2.平均處理次數, T2.平均處理時間 from ( " +
                    " select a.SUPERUNIT, a.UNITCODE, a.UNITNAME, COUNT(a.cnt) as '平均接案次數', sum(b.interval)/COUNT(a.cnt) as '平均接案時間'  " +
                    " from view_3 as a left join View_4 as b on a.actionid=b.actionid where a.CREATETIME > ? AND a.CREATETIME < ? " +
                    " group by a.SUPERUNIT, a.UNITCODE, a.UNITNAME) as T1 left join ( " +
                    " select a.SUPERUNIT, a.UNITCODE, a.UNITNAME, COUNT(a.cnt) as '平均處理次數', sum(b.interval)/COUNT(a.cnt) as '平均處理時間'  " +
                    " from view_5 as a left join View_6 as b on a.actionid=b.actionid where a.RESPONSETIME > ? AND a.RESPONSETIME < ? " +
                    " group by a.SUPERUNIT, a.UNITCODE, a.UNITNAME) as T2 on t1.unitcode=t2.unitcode where 1=1" +
                    spUnitCodeCondition ;

            pstmt = conn.prepareStatement(sqlQuery);
            logger.info("案件處理時間統計  sqlQuery =" + sqlQuery);
            CaseProcTimeAvgSubDataObject dataObj = null;
            int i = 0;
            long time;
            long day;
            long hour;
            long min;
            int cnt = 0;
            String[] unitCodeOrder = (String[]) vUnitCode.toArray(new String[0]);
            pstmt.setString(++i, startDate + " 00:00:00");
            pstmt.setString(++i, endDate + " 23:59:59");
            pstmt.setString(++i, startDate + " 00:00:00");
            pstmt.setString(++i, endDate + " 23:59:59");
            pstmt.setString(++i, mainUnitCode);
            pstmt.setString(++i, mainUnitCode);
            rs = pstmt.executeQuery();
            ArrayList<CaseProcTimeAvgSubDataObject> dataList = new ArrayList<CaseProcTimeAvgSubDataObject>();
            for (int u = 0; u < unitCodeOrder.length; u++) {
                dataObj = new CaseProcTimeAvgSubDataObject();
                dataObj.setUnitName((String) mappingTable.get(unitCodeOrder[u]));
                dataObj.setUnitCode(unitCodeOrder[u]);
                dataObj.setCounterCase(0);
                dataObj.setAvgTimeCase("--");
                dataObj.setScoreCase(0);

                dataObj.setCounter(0);
                dataObj.setAvgTime("--");
                dataObj.setScore(0);

                dataList.add(dataObj);
            }
            while (rs.next()) {
                String unitCode =  rs.getString("UNITCODE");
                dataObj = null;
                List<CaseProcTimeAvgSubDataObject> caseProcTimeAvgSubDataObjects = dataList.stream().filter(d -> d.getUnitCode().equals(unitCode)).collect(Collectors.toList());

                if (caseProcTimeAvgSubDataObjects.size() >0){
                    dataObj = caseProcTimeAvgSubDataObjects.get(0);
                    cnt = rs.getInt("平均接案次數");
                    time = rs.getInt("平均接案時間");
                    day = (int) TimeUnit.SECONDS.toDays(time);
                    hour = TimeUnit.SECONDS.toHours(time) - (day * 24);
                    min = TimeUnit.SECONDS.toMinutes(time) - (TimeUnit.SECONDS.toHours(time) * 60);

                    dataObj.setCounterCase(cnt);
                    dataObj.setAvgTimeCase(day + "天" + hour + "小時" + min + "分");
                    dataObj.setScoreCase(0);
                    long workHour = day * 8 + hour;

                    if (workHour < 4) {
                        dataObj.setScoreCase(3);
                    } else if (workHour >= 4 && day < 8) {
                        dataObj.setScoreCase(0);
                    } else if (workHour >= 8 && day < 16) {
                        dataObj.setScoreCase(-1);
                    } else if (workHour >= 16 && day < 24) {
                        dataObj.setScoreCase(-2);
                    } else if (workHour >= 24) {
                        dataObj.setScoreCase(-3);
                    }

                    cnt = rs.getInt("平均處理次數");
                    time = rs.getInt("平均處理時間");
                    day = (int) TimeUnit.SECONDS.toDays(time);
                    hour = TimeUnit.SECONDS.toHours(time) - (day * 24);
                    min = TimeUnit.SECONDS.toMinutes(time) - (TimeUnit.SECONDS.toHours(time) * 60);
                    dataObj.setCounter(cnt);
                    dataObj.setAvgTime(day + "天" + hour + "小時" + min + "分");
                    dataObj.setScore(0);

                    //平均案件初報時間3日內加2分；3日以上未達7日者加0分；7日以上未達15日者減1分；15日以上者減2分。無派案者0分。
                    if (day < 3) {
                        dataObj.setScore(score1D3D);
                    } else if (day >= 3 && day < 7) {
                        dataObj.setScore(score3D7D);
                    } else if (day >= 7 && day < 15) {
                        dataObj.setScore(score7D15D);
                    } else if (day >= 15) {
                        dataObj.setScore(score15D);
                    }


                }


            }

            //平均處理
//            sqlQuery = " Select a.superunit, a.unitcode, b.unitname, a.cnt, isnull(b.interval/a.cnt,0) as AVG_TIME " +
//                    " From ( " +
//                    " select SUPERUNIT, UNITCODE,sum(cnt) AS CNT " +
//                    " from view_5 " +
//                    " where createTime >=? and createTime <=? " +
//                    " group by superunit, unitcode) " +
//                    " as a inner join view_6 as b on a.unitcode=b.unitcode " +
//                    " where  1=1 and a.UNITCODE <>'A4007' "
//                    + spUnitCodeCondition;
//
//            pstmt = conn.prepareStatement(sqlQuery);
//            logger.info("案件處理時間統計 平均處理 sqlQuery =" + sqlQuery);
//            int i = 0;
//            pstmt.setString(++i, startDate + " 00:00:00");
//            pstmt.setString(++i, endDate + " 23:59:59");
//            pstmt.setString(++i, mainUnitCode);
//            pstmt.setString(++i, mainUnitCode);
//
//            rs = pstmt.executeQuery();
//            long tmpTotalTime = 0;
//            int cnt = 0;
//            String[] unitCodeOrder = (String[]) vUnitCode.toArray(new String[0]);
//
//            while (rs.next()) {
//                for (int u = 0; u < unitCodeOrder.length; u++) {
//
//                    String tmpSuperUnit = rs.getString("UNITCODE") == null ? "" : rs.getString("UNITCODE");
//                    tmpTotalTime = rs.getInt("AVG_TIME");
//                    cnt = rs.getInt("cnt");
//                    if (tmpSuperUnit.equals(unitCodeOrder[u])) {
//                        if (countMap.containsKey(unitCodeOrder[u])) {
//                            countMap.put(unitCodeOrder[u], (Integer) countMap.get(unitCodeOrder[u]) + cnt);
//                        } else {
//                            countMap.put(unitCodeOrder[u], cnt);
//                        }
//
//                        if (totalTimeMap.containsKey(unitCodeOrder[u])) {
//                            totalTimeMap.put(unitCodeOrder[u], (Long) totalTimeMap.get(unitCodeOrder[u]) + tmpTotalTime);
//                        } else {
//                            totalTimeMap.put(unitCodeOrder[u], tmpTotalTime);
//                        }
//                        break;
//                    }
//                }
//            }
//
//            rs.close();
//            rs = null;
//            pstmt.close();
//            pstmt = null;
//
//            long time;
//            long day;
//            long hour;
//            long min;
//
//            ArrayList<CaseProcTimeAvgSubDataObject> dataList = new ArrayList<CaseProcTimeAvgSubDataObject>();
//            for (int u = 0; u < unitCodeOrder.length; u++) {
//                if (countMap.containsKey(unitCodeOrder[u])) {
//                    CaseProcTimeAvgSubDataObject dataObj = new CaseProcTimeAvgSubDataObject();
//                    //time = (Long) (totalTimeMap.get(unitCodeOrder[u]) * 1000) / (Integer) countMap.get(unitCodeOrder[u]);
//                    //time = (Long) (totalTimeMap.get(unitCodeOrder[u]) * 1000);
//                    time = (Long) (totalTimeMap.get(unitCodeOrder[u]));
////                    day = time / (8 * 60 * 60 * 1000);
////                    hour = (time / (60 * 60 * 1000) - day * 8);
////                    min = ((time / (60 * 1000)) - day * 8 * 60 - hour * 60);
//
//                     day = (int) TimeUnit.SECONDS.toDays(time);
//                    hour = TimeUnit.SECONDS.toHours(time) - (day *24);
//                    min = TimeUnit.SECONDS.toMinutes(time) - (TimeUnit.SECONDS.toHours(time)* 60);
//
//
//                    dataObj.setUnitName((String) mappingTable.get(unitCodeOrder[u]));
//                    dataObj.setUnitCode(unitCodeOrder[u]);
//                    dataObj.setAvgTime(day + "天" + hour + "小時" + min + "分");
//                    dataObj.setCounter(countMap.get(unitCodeOrder[u]));
//
//                    //平均案件初報時間3日內加2分；3日以上未達7日者加0分；7日以上未達15日者減1分；15日以上者減2分。無派案者0分。
//                    if (day < 3) {
//                        dataObj.setScore(score1D3D);
//                    } else if (day >= 3 && day < 7) {
//                        dataObj.setScore(score3D7D);
//                    } else if (day >= 7 && day < 15) {
//                        dataObj.setScore(score7D15D);
//                    } else if (day >= 15) {
//                        dataObj.setScore(score15D);
//                    }
//                    dataList.add(dataObj);
//                } else {
//                    CaseProcTimeAvgSubDataObject dataObj = new CaseProcTimeAvgSubDataObject();
//                    dataObj.setUnitName((String) mappingTable.get(unitCodeOrder[u]));
//                    dataObj.setUnitCode(unitCodeOrder[u]);
//                    dataObj.setAvgTime("--");
//                    dataObj.setCounter(0);
//                    dataObj.setScore(0);
//                    dataList.add(dataObj);
//                }
//            }
//
//            //region 平均接案
//
//            sqlQuery = " Select a.superunit, a.unitcode, b.unitname, a.cnt, isnull(b.interval/a.cnt,0) as AVG_TIME " +
//                    " From ( " +
//                    " select SUPERUNIT, UNITCODE,sum(cnt) AS CNT " +
//                    " from view_3 " +
//                    " where createTime >=? and createTime <=? " +
//                    "group by superunit, unitcode) " +
//                    " as a inner join view_4 as b on a.unitcode=b.unitcode " +
//                    " where  1=1 and a.UNITCODE <>'A4007'"
//                    + spUnitCodeCondition;
//
//            pstmt = conn.prepareStatement(sqlQuery);
//            logger.info("案件處理時間統計 平均接案 sqlQuery =" + sqlQuery);
//            i = 0;
//            pstmt.setString(++i, startDate + " 00:00:00");
//            pstmt.setString(++i, endDate + " 23:59:59");
//            pstmt.setString(++i, mainUnitCode);
//            pstmt.setString(++i, mainUnitCode);
//            rs = pstmt.executeQuery();
//            tmpTotalTime = 0;
//            //SUPERUNIT為E2000
//            //以SPUNITCODE判定(特別處理)
//            //E2001---土地重劃工程處
//            //E2014---國土測繪中心
//            while (rs.next()) {
//                tmpTotalTime = rs.getInt("AVG_TIME");
//                cnt = rs.getInt("cnt");
//                for (int u = 0; u < unitCodeOrder.length; u++) {
//                    if (rs.getString("UNITCODE").equals(unitCodeOrder[u])) {
//                        if (countMapCase.containsKey(unitCodeOrder[u])) {
//                            countMapCase.put(unitCodeOrder[u], (Integer) countMapCase.get(unitCodeOrder[u]) + cnt);
//                        } else {
//                            countMapCase.put(unitCodeOrder[u], cnt);
//                        }
//
//                        if (totalTimeMapCase.containsKey(unitCodeOrder[u])) {
//                            totalTimeMapCase.put(unitCodeOrder[u], (Long) totalTimeMapCase.get(unitCodeOrder[u]) + tmpTotalTime);
//                        } else {
//                            totalTimeMapCase.put(unitCodeOrder[u], tmpTotalTime);
//                        }
//                        break;
//                    }
//                }
//            }
//
//            rs.close();
//            rs = null;
//            pstmt.close();
//            pstmt = null;
//            conn.close();
//            conn = null;
//
//            for (int u = 0; u < unitCodeOrder.length; u++) {
//                boolean isExist = false;
//                CaseProcTimeAvgSubDataObject tempObj = new CaseProcTimeAvgSubDataObject();
//
//                for (CaseProcTimeAvgSubDataObject caseProcTimeAvgSubDataObject : dataList) {
//                    if (caseProcTimeAvgSubDataObject.getUnitCode().equals(unitCodeOrder[u])) {
//                        isExist = true;
//                        tempObj = caseProcTimeAvgSubDataObject;
//                        break;
//                    }
//                }
//
//                if (countMapCase.containsKey(unitCodeOrder[u])) {
//                    tempObj.setCounterCase(countMapCase.get(unitCodeOrder[u]));
//                    //time = (Long) (totalTimeMapCase.get(unitCodeOrder[u]) * 1000) / (Integer) (countMapCase.getOrDefault(unitCodeOrder[u], 1));
//                   // time = (Long) (totalTimeMapCase.get(unitCodeOrder[u]) * 1000);
//                    time = (Long) (totalTimeMapCase.get(unitCodeOrder[u]));
////                    day = time / (8 * 60 * 60 * 1000);
////                    hour = (time / (60 * 60 * 1000) - day * 8);
////                    min = ((time / (60 * 1000)) - day * 8 * 60 - hour * 60);
//                    day = (int) TimeUnit.SECONDS.toDays(time);
//                    hour = TimeUnit.SECONDS.toHours(time) - (day *24);
//                    min = TimeUnit.SECONDS.toMinutes(time) - (TimeUnit.SECONDS.toHours(time)* 60);
//                    tempObj.setAvgTimeCase(day + "天" + hour + "小時" + min + "分");
//
////                    1日以工作時數8小時計算：
////                    (1)平均案件接收時間為4小時內接收者加3分
////                    (2)4小時以上達8小時者加0分
////                    (3)1日以上未達2日者減1分
////                    (4)2日以上未達3日者減2分
////                    (5)3日以上者減3分
////                    (6)無派案者0分。
//
//                    long workHour = day * 8 + hour;
//
//                    if (workHour < 4) {
//                        tempObj.setScoreCase(3);
//                    } else if (workHour >= 4 && day < 8) {
//                        tempObj.setScoreCase(0);
//                    } else if (workHour >= 8 && day < 16) {
//                        tempObj.setScoreCase(-1);
//                    } else if (workHour >= 16 && day < 24) {
//                        tempObj.setScoreCase(-2);
//                    } else if (workHour >= 24) {
//                        tempObj.setScoreCase(-3);
//                    }
//                } else {
//                    tempObj.setCounterCase(0);
//                    tempObj.setAvgTimeCase("--");
//                    tempObj.setScoreCase(0);
//                }
//
//                if (!isExist) dataList.add(tempObj);
//            }
//            //endregion

            beans.put("dataList", dataList);

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

    private long getTimeSecond(String beginTime, String endTime) // 計算時間差(秒)
    {
        long totalSecond = 0;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date bTime = df.parse(beginTime);
            Date eTime = df.parse(endTime);

            //設定特定時間點
            Date bWork = df.parse(beginTime.substring(0, 10) + " 08:30:00");
            Date eWork = df.parse(beginTime.substring(0, 10) + " 17:30:00");
            Date bRest = df.parse(beginTime.substring(0, 10) + " 12:30:00");
            Date eRest = df.parse(beginTime.substring(0, 10) + " 13:30:00");
            //
            long l = 0;
            String bTimeFlag = "";
            String eTimeFlag = "";

            if (bTime.compareTo(bWork) <= 0) {
                //上班時間前
                bTimeFlag = "A";
            } else if (bTime.compareTo(bWork) > 0 && bTime.compareTo(bRest) <= 0) {
                //上班時間到午休前
                bTimeFlag = "B";
            } else if (bTime.compareTo(bRest) > 0 && bTime.compareTo(eRest) <= 0) {
                //午休時間
                bTimeFlag = "C";
            } else if (bTime.compareTo(eRest) > 0 && bTime.compareTo(eWork) <= 0) {
                //午休後到下班時間
                bTimeFlag = "D";
            } else if (bTime.compareTo(eWork) > 0) {
                //下班時間後
                bTimeFlag = "E";
            }

            if (eTime.compareTo(bWork) <= 0) {
                //上班時間前
                eTimeFlag = "A";
            } else if (eTime.compareTo(bWork) > 0 && eTime.compareTo(bRest) <= 0) {
                //上班時間到午休前
                eTimeFlag = "B";
            } else if (eTime.compareTo(bRest) > 0 && eTime.compareTo(eRest) <= 0) {
                //午休時間
                eTimeFlag = "C";
            } else if (eTime.compareTo(eRest) > 0 && eTime.compareTo(eWork) <= 0) {
                //午休後到下班時間
                eTimeFlag = "D";
            } else if (eTime.compareTo(eWork) > 0) {
                //下班時間後
                eTimeFlag = "E";
            }

            if (bTimeFlag.equals("A") && eTimeFlag.equals("A")) {
                l = 0;
            } else if (bTimeFlag.equals("A") && eTimeFlag.equals("B")) {
                bTime = bWork;
                l = eTime.getTime() - bTime.getTime();
            } else if (bTimeFlag.equals("A") && eTimeFlag.equals("C")) {
                bTime = bWork;
                eTime = bRest;
                l = eTime.getTime() - bTime.getTime();
            } else if (bTimeFlag.equals("A") && eTimeFlag.equals("D")) {
                bTime = bWork;
                l = (eTime.getTime() - bTime.getTime()) - 60 * 60 * 1000;
            } else if (bTimeFlag.equals("A") && eTimeFlag.equals("E")) {
                bTime = bWork;
                eTime = eWork;
                l = (eTime.getTime() - bTime.getTime()) - 60 * 60 * 1000;
            } else if (bTimeFlag.equals("B") && eTimeFlag.equals("B")) {
                l = eTime.getTime() - bTime.getTime();
            } else if (bTimeFlag.equals("B") && eTimeFlag.equals("C")) {
                eTime = bRest;
                l = eTime.getTime() - bTime.getTime();
            } else if (bTimeFlag.equals("B") && eTimeFlag.equals("D")) {
                l = (eTime.getTime() - bTime.getTime()) - 60 * 60 * 1000;
            } else if (bTimeFlag.equals("B") && eTimeFlag.equals("E")) {
                eTime = eWork;
                l = (eTime.getTime() - bTime.getTime()) - 60 * 60 * 1000;
            } else if (bTimeFlag.equals("C") && eTimeFlag.equals("C")) {
                l = 0;
            } else if (bTimeFlag.equals("C") && eTimeFlag.equals("D")) {
                bTime = eRest;
                l = eTime.getTime() - bTime.getTime();
            } else if (bTimeFlag.equals("C") && eTimeFlag.equals("E")) {
                bTime = eRest;
                eTime = eWork;
                l = eTime.getTime() - bTime.getTime();
            } else if (bTimeFlag.equals("D") && eTimeFlag.equals("D")) {
                l = eTime.getTime() - bTime.getTime();
            } else if (bTimeFlag.equals("D") && eTimeFlag.equals("E")) {
                eTime = eWork;
                l = eTime.getTime() - bTime.getTime();
            } else if (bTimeFlag.equals("E") && eTimeFlag.equals("E")) {
                l = 0;
            }
            totalSecond = l;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return totalSecond;
    }

    private String workDay(PreparedStatement pstmt, String beginTime, long diffDay) throws Exception // 計算工作天
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
