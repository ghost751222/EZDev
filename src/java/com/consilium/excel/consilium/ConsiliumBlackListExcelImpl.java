
package com.consilium.excel.consilium;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.blacklist.BLackListObj;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service("ConsiliumBlackListExcelImpl")
public class ConsiliumBlackListExcelImpl implements ExcelInterface {


    private static final Logger logger = Logger.getLogger(ConsiliumBlackListExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "來電號碼比對過濾統計表.xls";

    private String reportPath = "/WEB-INF/report/consilium/BlackList.xls";


    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sqlQuery = "";
        Map beans = new HashMap();

        NumberFormat format = NumberFormat.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();

        format.setMinimumIntegerDigits(2);
        String startDate = jsonNode.get("time").get("startTime").asText();
        String endDate = jsonNode.get("time").get("endTime").asText();
        String dateType = jsonNode.get("dateType").asText();
        beans.put("startDate", startDate);
        beans.put("endDate", endDate);
        String printDate = dateFormater.format(now.getTime());
        beans.put("printDate", printDate);
        String timeType = jsonNode.get("timeType").asText();

        String groupSql = "";
        ArrayList<BLackListObj> reportList = new ArrayList<>();

        if ("Y".equals(timeType))//年
        {
            groupSql = "  format([datetime], 'yyyy') ";
        } else if ("M".equals(timeType))//月
        {
            groupSql = "  format([datetime], 'yyyy-MM') ";

        } else if ("D".equals(timeType))//日
        {
            groupSql = "  format([datetime], 'yyyy-MM-dd') ";
        } else if ("H".equals(timeType))//時
        {
            groupSql = "  format( [datetime], 'yyyy-MM-dd HH')";

        } else if ("W".equals(timeType)) {
            groupSql = " concat(format([datetime],'yyyy年'),datepart(wk,[datetime]),'週') ";
        }


        try {

            conn = ds.getConnection();

//            sqlQuery = "SELECT" +  (!groupSql.equals("") ? groupSql  + " as dataDate,": "" ) +
//                    " sum(case  when BLOCKRULE = 1 then 1 else 0 end  ) rule_1," +
//                    "  sum(case  when BLOCKRULE = 2 then 1 else 0 end  ) rule_2," +
//                    "  sum(case  when BLOCKRULE = 3 then 1 else 0 end  ) rule_3," +
//                    "  sum(case  when BLOCKRULE = 4 then 1 else 0 end  ) rule_4," +
//                    "  sum(case  when BLOCKRULE = 5 then 1 else 0 end  ) rule_5  " +
//                    " FROM BLACK_LIST_REPORT_1996 " +
//                    " WHERE 1=1" +
//                    " AND CREATETIME >= ? " +
//                    " AND CREATETIME <= ? " +
//                    (!groupSql.equals("") ? " group by"+ groupSql : "" );

            sqlQuery = "SELECT" +  (!groupSql.equals("") ? groupSql  + " as dataDate,": "" ) +
                    " sum(case  when BLOCKRULE = 1 then 1 else 0 end  ) rule_1," +
                    "  sum(case  when BLOCKRULE = 2 then 1 else 0 end  ) rule_2," +
                    "  sum(case  when BLOCKRULE = 3 then 1 else 0 end  ) rule_3," +
                    "  sum(case  when BLOCKRULE = 4 then 1 else 0 end  ) rule_4," +
                    "  sum(case  when BLOCKRULE = 5 then 1 else 0 end  ) rule_5  " +
                    " FROM Black_List_History " +
                    " WHERE 1=1" +
                    " AND [datetime] >= ? " +
                    " AND [datetime] <= ? " +

                    ("H".equals(dateType) ?
                            " AND ( ((datepart(dd,[datetime]) /7) +1) IN ('0', '6')  " +
                                    " OR EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(Black_List_History.[datetime] as date))) "
                            : ("W".equals(dateType) ?
                            " AND ( ((datepart(dd,[datetime]) /7) +1) NOT IN ('0', '6')  " +
                                    " AND NOT EXISTS(SELECT 1 FROM HOLIDAY HD WHERE HD.DATEOFF = cast(Black_List_History.[datetime] as date))) "
                            : "")) +
                    (!groupSql.equals("") ? " group by"+ groupSql : "" );



            pstmt = conn.prepareStatement(sqlQuery);
            int i = 0;
            pstmt.setString(++i, startDate + " 00:00:00");
            pstmt.setString(++i, endDate + " 23:59:59");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BLackListObj obj = new BLackListObj();
                try{
                    obj.setDataDate(rs.getString("dataDate"));
                }catch (Exception e){
                    obj.setDataDate("All");

                }
                obj.setRule_1(rs.getInt("rule_1"));
                obj.setRule_2(rs.getInt("rule_2"));
                obj.setRule_3(rs.getInt("rule_3"));
                obj.setRule_4(rs.getInt("rule_4"));
                obj.setRule_5(rs.getInt("rule_5"));
                reportList.add(obj);
            }
            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;

            if(reportList.size() == 0) reportList.add(new BLackListObj());
            beans.put("reportList", reportList);

            conn.close();
            conn = null;

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

