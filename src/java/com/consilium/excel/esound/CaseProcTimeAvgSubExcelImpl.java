package com.consilium.excel.esound;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.UnitCode;
import com.consilium.excel.models.UserInstance;
import com.consilium.excel.models.UserObject;
import com.consilium.excel.models.caseproctimeavg.CaseProcTimeAvgSubDataObject;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("CaseProcTimeAvgSubExcelImpl")
public class CaseProcTimeAvgSubExcelImpl implements ExcelInterface {


    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;


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
            spUnitCodeCondition = "AND (SPUNITCODE = ? or  ? is null) ";
            String sql = "SELECT UNITNAME, UNITCODE "
                    + "FROM UNITS "
                    + "WHERE (SPUNITCODE = ? or  ? is null) "
                    + "AND ( (UNITCODE != ? or ? is null) OR UNITCODE = 'E2014' OR UNITCODE = 'E2001' ) ";
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
            sqlQuery =
                    "SELECT " +
                            " T.ACTIONID, T.RECEIVESTATUS, R.REPORTTYPE, T.RECEIVEUNITTYPE, T.RECEIVEUNITCODE, U.SUPERUNIT, U.SPUNITCODE, U.UNITCODE, " +
                            " ISNULL(T.RECEIVETIME, getdate()) BTIME, cast(ISNULL(T.RECEIVETIME, getdate()) as date) BDAY, " +
                            " isnull(R.CREATETIME, getdate()) ETIME, cast(isnull(R.CREATETIME, getdate()) as date) EDAY, " +
                            " dateAdd(day,1,cast(ISNULL(T.RECEIVETIME, getdate()) as date)) B1DAY, " +
                            " CASE WHEN datediff(day, cast(T.RECEIVETIME as date),cast(ISNULL(R.CREATETIME, getdate()) as date)) <= 1 THEN 0 ELSE datediff(day, cast(T.RECEIVETIME as date),cast(ISNULL(R.CREATETIME, getdate()) as date)) - 1  END PERIODDAY " +
                            "FROM " +
                            " (CASE_FLOW T LEFT JOIN " +
                            " (SELECT" +
                            "      ACTIONID, " +
                            "      CREATETIME, " +
                            "      REPORTTYPE, " +
                            "      RESPONSEUNITTYPE, " +
                            "      RESPONSEUNITCODE " +
                            "  FROM " +
                            "  (" +
                            "       SELECT " +
                            "       ACTIONID, " +
                            "       CREATETIME, " +
                            "       REPORTTYPE, " +
                            "       RESPONSEUNITTYPE, " +
                            "       RESPONSEUNITCODE, " +
                            "       row_number() over (partition by ACTIONID, RESPONSEUNITTYPE, RESPONSEUNITCODE order by CREATETIME ASC) rnk " +
                            "       From CASE_RESPONSE_RECORD " +
                            "       WHERE REPORTTYPE IN ('F','G','H') " +
                            "       ) a" +
                            "   WHERE rnk = 1) R " +
                            " ON (T.ACTIONID = R.ACTIONID AND T.RECEIVEUNITTYPE = R.RESPONSEUNITTYPE AND T.RECEIVEUNITCODE = R.RESPONSEUNITCODE)) LEFT JOIN UNITS U ON (T.RECEIVEUNITTYPE = U.UNITTYPE AND T.RECEIVEUNITCODE = U.UNITCODE) " +
                            " WHERE T.RECEIVESTATUS IN ('D', 'E', 'G', 'H', 'I', 'J', 'K') " +
                            " AND " +
                            "     T.APPROVENTIME >= ? " +
                            " AND " +
                            "     T.APPROVENTIME <= ? " +
                            "  AND T.FORMTYPE = 'CG' AND T.RECEIVEUNITCODE<> 'A4007' " + spUnitCodeCondition +
                            " ORDER BY U.UNITCODE ";

            pstmt = conn.prepareStatement(sqlQuery);
            int i = 0;
            pstmt.setString(++i, startDate + " 00:00:00");
            pstmt.setString(++i, endDate + " 23:59:59");
            pstmt.setString(++i, mainUnitCode);
            pstmt.setString(++i, mainUnitCode);

            rs = pstmt.executeQuery();
            long tmpTotalTime = 0;
            String[] unitCodeOrder = (String[]) vUnitCode.toArray(new String[0]);
            //28單位
//            String[] unitCodeOrder = {"A1000",      //民政司
//                    "A2000",      //地政司
//                    "A3000",      //總務司
//                    "B4000",      //營建署
//                    "C1000",      //中央警察大學
//                    "C2000",      //建築研究所
//                    "E1000",      //兒童局
//                    "E2001",      //土地重劃工程處
//                    "E2014",      //國土測繪中心
//                    "E3000",      //社政機關
//                    "F1000",      //秘書室
//                    "F2000",      //資訊中心
//                    "F3000",      //政風處
//                    "F4000",      //國民年金監理會
//                    "A4000",      //戶政司
//                    "A6000",      //社會司
//                    "B1000",      //消防署
//                    "B2000",      //入出國及移民署
//                    "B3000",      //警政署
//                    "B5000",      //役政署
//                    "D1000",      //空勤總隊
//                    "E4000",      //家庭暴力及性侵害防治委員會
//                    "F5000",      //人事處
//                    "F6000",      //會計處
//                    "F7000",      //統計處
//                    "F8000",      //法規委員會
//                    "F9000",      //訴願審議委員會
//                    "G1000"       //部長信箱
//            };
            while (rs.next()) {
                for (int u = 0; u < unitCodeOrder.length; u++) {

                    String tmpSuperUnit = rs.getString("UNITCODE") == null ? "" : rs.getString("UNITCODE");
                    if (tmpSuperUnit.equals(unitCodeOrder[u])) {
                        if (countMap.containsKey(unitCodeOrder[u])) {
                            countMap.put(unitCodeOrder[u], (Integer) countMap.get(unitCodeOrder[u]) + 1);
                        } else {
                            countMap.put(unitCodeOrder[u], 1);
                        }

                        if (rs.getInt("PERIODDAY") == 0) {
                            tmpTotalTime = getTimeSecond(rs.getString("BTIME"), rs.getString("ETIME"));
                        } else {
                            tmpTotalTime = Long.parseLong(workDay(pstmt1, rs.getString("B1DAY").substring(0, 10), Long.parseLong(rs.getString("PERIODDAY")))) * 60 * 60 * 8 * 1000 + getTimeSecond(rs.getString("BTIME"), rs.getString("BTIME").substring(0, 10) + " 17:30:00") + getTimeSecond(rs.getString("ETIME").substring(0, 10) + " 08:30:00", rs.getString("ETIME"));
                        }

                        if (totalTimeMap.containsKey(unitCodeOrder[u])) {
                            totalTimeMap.put(unitCodeOrder[u], (Long) totalTimeMap.get(unitCodeOrder[u]) + tmpTotalTime);
                        } else {
                            totalTimeMap.put(unitCodeOrder[u], tmpTotalTime);
                        }
                        break;
                    }
                }
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            long time;
            long day;
            long hour;
            long min;

            ArrayList<CaseProcTimeAvgSubDataObject> dataList = new ArrayList<CaseProcTimeAvgSubDataObject>();
            for (int u = 0; u < unitCodeOrder.length; u++) {
                if (countMap.containsKey(unitCodeOrder[u])) {
                    CaseProcTimeAvgSubDataObject dataObj = new CaseProcTimeAvgSubDataObject();
                    time = (Long) totalTimeMap.get(unitCodeOrder[u]) / (Integer) countMap.get(unitCodeOrder[u]);
                    day = time / (8 * 60 * 60 * 1000);
                    hour = (time / (60 * 60 * 1000) - day * 8);
                    min = ((time / (60 * 1000)) - day * 8 * 60 - hour * 60);
                    dataObj.setUnitName((String) mappingTable.get(unitCodeOrder[u]));
                    dataObj.setUnitCode(unitCodeOrder[u]);
                    dataObj.setAvgTime(day + "天" + hour + "小時" + min + "分");
                    dataObj.setCounter(countMap.get(unitCodeOrder[u]));

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
                    dataList.add(dataObj);
                } else {
                    CaseProcTimeAvgSubDataObject dataObj = new CaseProcTimeAvgSubDataObject();
                    dataObj.setUnitName((String) mappingTable.get(unitCodeOrder[u]));
                    dataObj.setUnitCode(unitCodeOrder[u]);
                    dataObj.setAvgTime("--");
                    dataObj.setCounter(0);
                    dataObj.setScore(0);
                    dataList.add(dataObj);
                }
            }

            //region 平均接案
            sqlQuery =
                    " SELECT " +
                            "     T.RECEIVESTATUS, T.RECEIVEUNITTYPE, T.RECEIVEUNITCODE, U.SUPERUNIT, U.SPUNITCODE, U.UNITCODE, " +
                            "     T.APPROVENTIME BTIME, ISNULL(T.RECEIVETIME, getdate()) ETIME, " +
                            "     cast(T.APPROVENTIME as date) BDAY, cast(isNULL(T.RECEIVETIME, getdate()) as date ) EDAY, dateadd(day,1,cast( T.APPROVENTIME as date))  B1DAY, " +
                            "     CASE WHEN  datediff(day,T.APPROVENTIME,  ISNULL(T.RECEIVETIME, getdate()) )   <= 1 THEN 0 ELSE   datediff(day, T.APPROVENTIME, ISNULL(T.RECEIVETIME, getdate()) ) - 1  END PERIODDAY" +
                            " FROM " +
                            "     CASE_FLOW T, UNITS U " +
                            " WHERE " +
                            "     T.RECEIVESTATUS IS NOT NULL " +
                            " AND " +
                            "     T.RECEIVEUNITTYPE = U.UNITTYPE " +
                            " AND " +
                            "     T.RECEIVEUNITCODE = U.UNITCODE " +
                            " AND " +
                            "     T.APPROVENTIME >= ? " +
                            " AND " +
                            "     T.APPROVENTIME <= ? " +
                            "  AND T.FORMTYPE = 'CG' AND T.RECEIVEUNITCODE<> 'A4007' AND (SPUNITCODE = ? or ? is null)";

            pstmt = conn.prepareStatement(sqlQuery);
            i = 0;
            pstmt.setString(++i, startDate + " 00:00:00");
            pstmt.setString(++i, endDate + " 23:59:59");
            pstmt.setString(++i, mainUnitCode);
            pstmt.setString(++i, mainUnitCode);
            rs = pstmt.executeQuery();
            tmpTotalTime = 0;
            //SUPERUNIT為E2000
            //以SPUNITCODE判定(特別處理)
            //E2001---土地重劃工程處
            //E2014---國土測繪中心
            while (rs.next()) {
                for (int u = 0; u < unitCodeOrder.length; u++) {
                    if (rs.getString("UNITCODE").equals(unitCodeOrder[u])) {
                        if (countMapCase.containsKey(unitCodeOrder[u])) {
                            countMapCase.put(unitCodeOrder[u], (Integer) countMapCase.get(unitCodeOrder[u]) + 1);
                        } else {
                            countMapCase.put(unitCodeOrder[u], 1);
                        }

                        if (rs.getInt("PERIODDAY") == 0) {
                            tmpTotalTime = getTimeSecond(rs.getString("BTIME"), rs.getString("ETIME"));
                        } else {
                            tmpTotalTime = Long.parseLong(workDay(pstmt1, rs.getString("B1DAY").substring(0, 10), Long.parseLong(rs.getString("PERIODDAY")))) * 60 * 60 * 8 * 1000 + getTimeSecond(rs.getString("BTIME"), rs.getString("BTIME").substring(0, 10) + " 17:30:00") + getTimeSecond(rs.getString("ETIME").substring(0, 10) + " 08:30:00", rs.getString("ETIME"));
                        }

                        if (totalTimeMapCase.containsKey(unitCodeOrder[u])) {
                            totalTimeMapCase.put(unitCodeOrder[u], (Long) totalTimeMapCase.get(unitCodeOrder[u]) + tmpTotalTime);
                        } else {
                            totalTimeMapCase.put(unitCodeOrder[u], tmpTotalTime);
                        }
                        break;
                    }
                }
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;
            conn.close();
            conn = null;

            for (int u = 0; u < unitCodeOrder.length; u++) {
                boolean isExist = false;
                CaseProcTimeAvgSubDataObject tempObj = new CaseProcTimeAvgSubDataObject();

                for (CaseProcTimeAvgSubDataObject caseProcTimeAvgSubDataObject : dataList) {
                    if (caseProcTimeAvgSubDataObject.getUnitCode().equals(unitCodeOrder[u])) {
                        isExist = true;
                        tempObj = caseProcTimeAvgSubDataObject;
                        break;
                    }
                }

                if (countMapCase.containsKey(unitCodeOrder[u])) {
                    tempObj.setCounterCase(countMapCase.get(unitCodeOrder[u]));
                    time = (Long) totalTimeMapCase.get(unitCodeOrder[u]) / (Integer) (countMapCase.getOrDefault(unitCodeOrder[u], 1));
                    day = time / (8 * 60 * 60 * 1000);
                    hour = (time / (60 * 60 * 1000) - day * 8);
                    min = ((time / (60 * 1000)) - day * 8 * 60 - hour * 60);
                    tempObj.setAvgTimeCase(day + "天" + hour + "小時" + min + "分");
                } else {
                    tempObj.setCounter(0);
                    tempObj.setAvgTime("--");
                }

                if (!isExist) dataList.add(tempObj);
            }
            //endregion

            beans.put("dataList", dataList);

            String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/CaseProcTimeAvgSub.xls");
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
