package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.ServiceInstance;
import com.consilium.excel.models.ServiceTypeObject;
import com.consilium.excel.models.UnitCode;
import com.consilium.excel.models.UnitCodeObject;
import com.consilium.excel.models.contentinfoform.ReportObject;
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

@Service("ConsiliumContentInfoFormExcelImpl")
public class ConsiliumContentInfoFormExcelImpl implements ExcelInterface {

    private static final Logger logger = Logger.getLogger(ConsiliumContentInfoFormExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "系統更正/新增/確認通知單報表.xls";

    private String reportPath = "/WEB-INF/report/consilium/ContentInfoForm.xls";


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        ResultSet rs = null;
        String sql = "";
        PreparedStatement pstmt = null;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = dateFormater.format(now.getTime());
        Map<String, Object> beans = new HashMap<String, Object>();

        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumIntegerDigits(2);

        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();

        ArrayList<ReportObject> list = new ArrayList<ReportObject>();
        Map<String, String> caseTypeMap = new HashMap<>();
        caseTypeMap.put("A", "科室資料管理");
        caseTypeMap.put("B", "業務職掌管理");
        caseTypeMap.put("C", "FAQ管理");
        caseTypeMap.put("Z", "其他");
        try {
            ArrayList<ServiceTypeObject> typeList = ServiceInstance.getInstance().getTypeEnableList();
            Vector units = UnitCode.getInstance().getUnitList("M");
            conn = ds.getConnection();
//            sql = "   select distinct dbo.Content_latest_ResponseTime(a.ACTIONID) as '日期', a.ACTIONID as '列管編號', a.SUPERUNIT as '單位', a.UNITCODE as '科室', a.CASETYPE AS '類別', a.OPERTYPE as '建請修正或新增資料'," +
//                    "        a.DESCRIPTION as '說明', a.NOTE as '備註',  dbo.Content_Latest_ProcessOther(a.ACTIONID) as '其他辦理情形', dbo.content_latest_processnote(a.ACTIONID) as '辦理情形'," +
//                    " case when a.CONFIRMDATE <= a.DEADLINE then '不減分' else '減1分' end as '複查確認結果', a.CONFIRMNOTE as '複查確認結果'," +
//                    "    convert(varchar(20), d.responseTime, 120) as '受理時間'," +
//                    "    convert(varchar(20), e.responseTime, 120) as '結報時間'," +
//                    "    convert(varchar(20), a.CONFIRMTIME, 120) as '結案時間'," +
//                    "    dbo.Content_IsExpire (a.ACTIONID, 72) as '是否逾期'" +
//                    " from Content_Info_Form as a with(nolock) " +
//
//                    " left join (Select * from Case_Response_Record with(nolock) where reportType='E' and responseUnitType='M' and SUBSTRING(actionId, 1,2)='CI') as d on a.ACTIONID=d.actionId " +
//                    " left join (Select * from Case_Response_Record with(nolock) where reportType='H' and responseUnitType='M' and SUBSTRING(actionId, 1,2)='CI') as e on a.ACTIONID=e.actionId " +
//                    " where dbo.Content_latest_ResponseTime(a.ACTIONID) is not null and dbo.Content_latest_ResponseTime(a.ACTIONID) > ? and dbo.Content_latest_ResponseTime(a.ACTIONID) < ? ";


            sql = " select distinct dbo.Content_latest_ResponseTime(a.ACTIONID) as '日期', a.ACTIONID as '列管編號', a.SUPERUNIT as '單位', h.UNITNAME as '單位中文', a.UNITCODE as '科室',           "
                    + " g.UNITNAME as '科室中文', a.CASETYPE AS '類別', a.suggestion as '建請修正或新增資料',                                                                                           "
                    + " a.DESCRIPTION as '說明', a.NOTE as '備註',  dbo.Content_Latest_ProcessOther(a.ACTIONID) as '其他辦理情形', dbo.content_latest_processnote(a.ACTIONID) as '辦理情形',          "
                    + " case when a.CONFIRMDATE <= a.DEADLINE and a.CONFIRMDATE is not null then '不減分' when a.CONFIRMDATE is null then '' else '減1分' end as '複查確認結果Score', a.CONFIRMNOTE as '複查確認結果',                                                      "
                    + " convert(varchar(20), d.responseTime, 120) as '受理時間',                                                                                                                      "
                    + " convert(varchar(20), e.responseTime, 120) as '結報時間',                                                                                                                      "
                    + " convert(varchar(20), f.RESPONSETIME, 120) as '結案時間',                                                                                                                      "
                    + " dbo.Content_IsExpire (a.ACTIONID, 72) as '是否逾期'                                                                                                                           "
                    + " from Content_Info_Form as a with(nolock)                                                                                                                                    "

                    + " left join (Select * from Case_Response_Record with(nolock) where reportType='E' and responseUnitType='M' and SUBSTRING(actionId, 1,2)='CI') as d  on a.ACTIONID=d.actionId    "
                    + " left join (Select * from Case_Response_Record with(nolock) where reportType='H' and responseUnitType='M' and SUBSTRING(actionId, 1,2)='CI') as e  on a.ACTIONID=e.actionId    "
                    + " left join (Select * from Case_Response_Record with(nolock) where reportType='I' and SUBSTRING(actionId, 1,2)='CI') as f  on a.ACTIONID=f.actionId                             "
                    + " left join UNITS as g on g.UNITCODE=a.UNITCODE                                                                                                                                 "
                    + " left join (select * from UNITS where SUPERUNIT is null) as h on a.SUPERUNIT=h.SPSERVICETYPE                                                                                   "
                    + " where dbo.Content_latest_ResponseTime(a.ACTIONID) is not null and                                                                                                             "
                    + " dbo.Content_latest_ResponseTime(a.ACTIONID) > ? and dbo.Content_latest_ResponseTime(a.ACTIONID) < ?  ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");

            rs = pstmt.executeQuery();
            ReportObject obj = null;
            boolean isHidden = false;
            while (rs.next()) {
                isHidden = false;
                for (Object u: units) {
                    UnitCodeObject unitCodeObject = (UnitCodeObject) u;

                    if( unitCodeObject.getUnitCode().equals(rs.getString("科室"))  && unitCodeObject.getSuperUnit().contains("_D")   ){
                        isHidden = true;
                        break;
                    }
                }
                if(isHidden) continue;
                obj = new ReportObject();

                obj.setCreateTime(rs.getString("日期"));
                obj.setActionId(rs.getString("列管編號"));

                obj.setUnitName(ServiceInstance.getInstance().getTypeName(rs.getString("單位")));

                obj.setSectionName(ServiceInstance.getInstance().getItemName(rs.getString("科室")));

                obj.setCaseType(caseTypeMap.get(rs.getString("類別")));

                obj.setSuggestion(rs.getString("建請修正或新增資料"));

                obj.setDescription(rs.getString("說明"));

                obj.setNote(rs.getString("備註"));

                obj.setOtherSituation(rs.getString("其他辦理情形"));

                obj.setSituation(rs.getString("辦理情形"));

                obj.setConfirmResultScore(rs.getString("複查確認結果Score"));
                obj.setConfirmResult(rs.getString("複查確認結果"));


                obj.setIsOverTime(rs.getString("是否逾期"));

                obj.setReceiveTime(rs.getString("受理時間"));

                obj.setLastReplyTime(rs.getString("結報時間"));

                obj.setConfirmTime(rs.getString("結案時間"));
                list.add(obj);
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;
            conn.close();
            conn = null;
            beans.put("list", list);
            beans.put("startDate", startDate);
            beans.put("endDate", endDate);
            beans.put("printDate", nowDate);

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
