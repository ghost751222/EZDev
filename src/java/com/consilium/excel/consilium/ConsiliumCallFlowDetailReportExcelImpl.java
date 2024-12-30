
package com.consilium.excel.consilium;
import com.consilium.excel.componet.ApplicationCode;
import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.CallFlowDetailObj;
import com.consilium.excel.models.UnitCode;
import com.consilium.excel.models.UserInstance;
import com.consilium.excel.models.UserObject;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.jxls.transformer.XLSTransformer;
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

@Service("ConsiliumCallFlowDetailReportExcelImpl")
public class ConsiliumCallFlowDetailReportExcelImpl implements ExcelInterface {

    private static final Logger logger = Logger.getLogger(ConsiliumCallFlowDetailReportExcelImpl.class);
    private String excelName = "派案明細表.xls";
    private String reportPath = "/WEB-INF/report/consilium/CallFlowDetail.xls";
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

            String caseType = jsonNode.get("caseStatus").asText().toUpperCase();

            NumberFormat format = NumberFormat.getInstance(); //為了跑兩位數
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //格式化日期
            Calendar now = Calendar.getInstance(); //抓現在時間

            format.setMinimumIntegerDigits(2);  //跑兩位數
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

//                sqlQuery =
//                        " select * from ( " +
//                        "  select distinct ROW_NUMBER() over (partition by a.ACTIONID order by a.ACTIONID) as rownum ," +
//
//                        "  a.ACTIONID as '案號', a.serviceType as '類別', a.ringTime as '進線時間', c.responseTime as '通報時間', si.ITEMNAME as '受理單位', " +
//                                "b.approvenStatus as '案件狀態', d.RESPONSETIME as '接收/退回時間', e.responseTime as '初報時間', f.RESPONSETIME as '結報時間' ,h.RESPONSETIME as '結案時間'" +
//
//                                "from CallLog_Form as a inner join Case_Flow (nolock) as b on a.ACTIONID=b.actionId and  b.CREATETIME >= ? AND b.CREATETIME <=  ? " +
//                                " left join SERVICE_ITEM si on  a.serviceItem=  ITEMID " +
//                                " left join (select * from CASE_RESPONSE_RECORD where REPORTTYPE='D'  and DESCRIPTION='待接收' ) as c on a.ACTIONID=c.actionId " +
//                                " left join (select * from CASE_RESPONSE_RECORD where RESPONSEUNITTYPE='M' and DESCRIPTION = '單位已接收案件') as d on b.ACTIONID=d.actionId " +
//                                " left join (select * from CASE_RESPONSE_RECORD where REPORTTYPE='E') as e on b.ACTIONID=e.ACTIONID" +
//                                " left join (Select * from CASE_RESPONSE_RECORD where REPORTTYPE='G') as f on b.ACTIONID=f.ACTIONID" +
//                                " left join (Select * from CASE_RESPONSE_RECORD where REPORTTYPE in ('I','S') ) as h on b.ACTIONID=h.ACTIONID" +
//
//                                " ) A where rownum = 1";

                sqlQuery =  "  select " +

                                "  a.ACTIONID as '案號', Case when a.callType = 'K' then '8' else a.serviceType end as '類別', a.ringTime as '進線時間', c.responseTime as '通報時間', si.ITEMNAME as '受理單位', " +
                                "  b.approvenStatus as '案件狀態', d.RESPONSETIME as '接收/退回時間', e.responseTime as '初報時間', f.RESPONSETIME as '結報時間' ,h.RESPONSETIME as '結案時間'" +
                                "from CallLog_Form as a inner join Case_Flow (nolock) as b on a.ACTIONID=b.actionId and  b.CREATETIME >= ? AND b.CREATETIME <=  ? " +
                                " left join SERVICE_ITEM si on  a.serviceItem=  ITEMID " +
                                " left join (select distinct ACTIONID,convert(varchar(20), responseTime, 120) as responseTime from CASE_RESPONSE_RECORD where REPORTTYPE='D'  and DESCRIPTION='待接收' ) as c on a.ACTIONID=c.actionId " +
                                " left join (select * from CASE_RESPONSE_RECORD where RESPONSEUNITTYPE='M' and DESCRIPTION = '單位已接收案件') as d on b.ACTIONID=d.actionId " +
                                " left join (select * from CASE_RESPONSE_RECORD where REPORTTYPE='E') as e on b.ACTIONID=e.ACTIONID" +
                                " left join (Select * from CASE_RESPONSE_RECORD where REPORTTYPE='G') as f on b.ACTIONID=f.ACTIONID" +
                                " left join (Select * from CASE_RESPONSE_RECORD where REPORTTYPE in ('I','S') ) as h on b.ACTIONID=h.ACTIONID" +
                                " where a.serviceType not in('O','3') and a.serviceItem not in ('10000','10001','10002','10003','10004','10020') " ;
                pstmt = conn.prepareStatement(sqlQuery);
                logger.info("派案明細表 sqlQuery = " + sqlQuery);
                int i = 0;
                pstmt.setString(++i, startDate + " 00:00:00");
                pstmt.setString(++i, endDate + " 23:59:59");
                rs = pstmt.executeQuery();
                while (rs.next()) {//
                    specialType = rs.getString("案件狀態");
                    if (caseType.equals(specialType) || "ALL".equals(caseType)) {
                        actionId = rs.getString("案號");
                        caseStatus = rs.getString("案件狀態");
                        obj = new CallFlowDetailObj();
                        obj.setCaseId(actionId);
                        obj.setType(rs.getString("類別"));
                        obj.setCreateTime(rs.getTimestamp("進線時間") == null ? "" : formater.format(rs.getTimestamp("進線時間")));
                        obj.setUnitType(rs.getString("受理單位"));
                        obj.setCaseStatus(caseStatus);

                        obj.setApproveTime(rs.getTimestamp("通報時間") == null ? "" : formater.format(rs.getTimestamp("通報時間")));
                        obj.setReceiveTime(rs.getTimestamp("接收/退回時間") == null ? "" : formater.format(rs.getTimestamp("接收/退回時間")));
                        obj.setFirstReportTime(rs.getTimestamp("初報時間") == null ? "" : formater.format(rs.getTimestamp("初報時間")));
                        obj.setEndReportTime(rs.getTimestamp("結報時間") == null ? "" : formater.format(rs.getTimestamp("結報時間")));
                        obj.setEndCaseTime(rs.getTimestamp("結案時間") == null ? "" : formater.format(rs.getTimestamp("結案時間")));

                        callFlowList.add(obj);
                    }
                }

                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;

                conn.close();
                conn = null;
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
            } catch (SQLException e) {
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

    }

    private long diffDay(String beginTime, String endTime) throws Exception {
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