
package com.consilium.excel.esound;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.QAExcelObject;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("FAQVerifyListExcelImpl")
public class FAQVerifyListExcelImpl implements ExcelInterface {


    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;



    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {


        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        NumberFormat format = NumberFormat.getInstance();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();
        String nowDate = dateFormater.format(now.getTime());
        //String bDataTime_YYYY = request.getParameter("bDataTime_YYYY");
        //String eDataTime_YYYY = request.getParameter("eDataTime_YYYY");
        UserObject userObj = UserInstance.getInstance().getUserObject(httpServletRequest);
        String userName = userObj.getUserName();

        Map beans = new HashMap();
        String sql = "";
        format.setMinimumIntegerDigits(2);
        //String bDataTime_MM = format.format(Long.valueOf(request.getParameter("bDataTime_MM").trim()));
        //String bDataTime_DD = format.format(Long.valueOf(request.getParameter("bDataTime_DD").trim()));
        //String eDataTime_MM = format.format(Long.valueOf(request.getParameter("eDataTime_MM").trim()));
        //String eDataTime_DD = format.format(Long.valueOf(request.getParameter("eDataTime_DD").trim()));
        //String startTime = bDataTime_YYYY + "-" + bDataTime_MM + '-' + bDataTime_DD;
        //String endTime = eDataTime_YYYY + "-" + eDataTime_MM + '-' + eDataTime_DD;

        String unitId = jsonNode.get("unitId").asText(null);
        String sectionId = jsonNode.get("sectionId").asText(null);


        String startTime = "";
        String endTime = "";
        ArrayList QAList = new ArrayList();
        ArrayList QRList = new ArrayList();
        try {

            conn = ds.getConnection();
            sql =
                    " SELECT                                                                                                                                                                                                                  " +
                            "       *                                                                                                                                                                                                                 " +
                            " FROM                                                                                                                                                                                                                    " +
                            " (                                                                                                                                                                                                                       " +
                            "       SELECT                                                                                                                                                                                                            " +
                            "               SID,                                                                                                                                                                                                      " +
                            "               QUESTION,                                                                                                                                                                                                 " +
                            "               ANSWER,                                                                                                                                                                                                   " +
                            "               KEYWORD,                                                                                                                                                                                                  " +
                            "               EFFICIENTTIME,                                                                                                                                                                                            " +
                            "               CLOSEDATATIME,                                                                                                                                                                                            " +
                            "               LASTUPDATETIME,                                                                                                                                                                                           " +
                            "               ORDERTIME,                                                                                                                                                                                                " +
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= A.SID)) a where a.RowNum = 1 ) as CONNECTOR1,                              " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= A.SID)) a where a.RowNum = 1 ) as TEL1,                                                " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= A.SID)) a where a.RowNum = 1 ) as EXTENSION1,                               " +
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= A.SID)) a where a.RowNum = 2 ) as CONNECTOR2,                              " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= A.SID)) a where a.RowNum = 2 ) as TEL2,                                                " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= A.SID)) a where a.RowNum = 2 ) as EXTENSION2                               " +
                            "       FROM                                                                                                                                                                                                              " +
                            "               QUESTION_ANSWER  A                                                                                                                                                                                        " +
                            "       WHERE                                                                                                                                                                                                             " +
                            "               (   (STATUS='K')   And  (unitId = ? or ? is null)  and (sectionId =? or ? is null))                                                                                                        " +

                           // "       OR  EXISTS(SELECT SID FROM QuestionAnswer_Relation B WHERE  STATUS IN('S','D') AND A.SID=B.PID))                                                                                                                  " +

                            " ) QA LEFT OUTER JOIN                                                                                                                                                                                                    " +
                            " (                                                                                                                                                                                                                       " +
                            "       SELECT                                                                                                                                                                                                            " +
                            "               SID AS RSID,                                                                                                                                                                                              " +
                            "               PID AS RPID,                                                                                                                                                                                              " +
                            "               QUESTION AS RQUESTION,                                                                                                                                                                                    " +
                            "               ANSWER AS RANSWER,                                                                                                                                                                                        " +
                            "               KEYWORD AS RKEYWORD,                                                                                                                                                                                      " +
                            "               EFFICIENTTIME AS REFFICIENTTIME,                                                                                                                                                                          " +
                            "               CLOSEDATATIME AS RCLOSEDATATIME,                                                                                                                                                                          " +
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= b.SID)) a where a.RowNum = 1 ) as RCONNECTOR1,                                                                                                                 " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= b.SID)) a where a.RowNum = 1 ) as  RTEL1,                                                                                                                            " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= b.SID)) a where a.RowNum = 1 ) as  REXTENSION1,                                                                                                               " +
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= b.SID)) a where a.RowNum = 1 ) as RCONNECTOR2,                               " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= b.SID)) a where a.RowNum = 1 ) as RTEL2,                                                " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= b.SID)) a where a.RowNum = 1 ) as REXTENSION2                              " +
                            "       FROM                                                                                                                                                                                                              " +
                            "               QuestionAnswer_Relation  B                                                                                                                                                                                " +
                            " ) QR                                                                                                                                                                                                                    " +
                            " ON                                                                                                                                                                                                                   " +
                            "       QA.SID=QR.RPID                                                                                                                                                                                                    " +
                            " ORDER BY                                                                                                                                                                                                                " +
                            "       QA.ORDERTIME DESC,QR.RSID DESC                                                                                                                                                                               ";

            pstmt = conn.prepareStatement(sql);
            // pstmt.setString(1, startTime + " 00:00:00");
            //pstmt.setString(2, endTime + " 23:59:59");
            //pstmt.setString(3, startTime + " 00:00:00");
            //pstmt.setString(4, endTime + " 23:59:59");

            pstmt.setString(1, unitId);
            pstmt.setString(2, unitId);
            pstmt.setString(3, sectionId);
            pstmt.setString(4, sectionId);
            rs = pstmt.executeQuery();
            QAExcelObject obj;
            QAExcelObject rObj;
            Hashtable tmpTable = new Hashtable();
            while (rs.next()) {
                obj = new QAExcelObject();
                obj.setSId(rs.getString("SID"));
                obj.setQuestion(rs.getString("QUESTION"));
                obj.setAnswer(rs.getString("ANSWER"));
                obj.setKeyword(rs.getString("KEYWORD"));
                obj.setConnectName1(rs.getString("CONNECTOR1"));
                obj.setTel1(rs.getString("TEL1"));
                obj.setExten1(rs.getString("EXTENSION1"));
                obj.setConnectName2(rs.getString("CONNECTOR2"));
                obj.setTel2(rs.getString("TEL2"));
                obj.setExten2(rs.getString("EXTENSION2"));
                obj.setEfficientTime(rs.getString("EFFICIENTTIME"));

                rObj = new QAExcelObject();
                rObj.setSId(rs.getString("RSID"));
                rObj.setQuestion(rs.getString("RQUESTION"));
                rObj.setAnswer(rs.getString("RANSWER"));
                rObj.setKeyword(rs.getString("RKEYWORD"));
                rObj.setConnectName1(rs.getString("RCONNECTOR1"));
                rObj.setTel1(rs.getString("RTEL1"));
                rObj.setExten1(rs.getString("REXTENSION1"));
                rObj.setConnectName2(rs.getString("RCONNECTOR2"));
                rObj.setTel2(rs.getString("RTEL2"));
                rObj.setExten2(rs.getString("REXTENSION2"));
                rObj.setEfficientTime(rs.getString("REFFICIENTTIME"));
                if (rObj.getQuestion().trim().length() > 0) QRList.add(rObj);


                if (!"Y".equals(tmpTable.get(obj.getSId()))) {
                    obj.setRSId(rs.getString("RSID"));
                    obj.setRQuestion(rs.getString("RQUESTION"));
                    obj.setRAnswer(rs.getString("RANSWER"));
                    obj.setRKeyWord(rs.getString("RKEYWORD"));
                    obj.setRConnectName1(rs.getString("RCONNECTOR1"));
                    obj.setRTel1(rs.getString("RTEL1"));
                    obj.setRExten1(rs.getString("REXTENSION1"));
                    obj.setRConnectName2(rs.getString("RCONNECTOR2"));
                    obj.setRTel2(rs.getString("RTEL2"));
                    obj.setRExten2(rs.getString("REXTENSION2"));
                    obj.setREfficientTime(rs.getString("REFFICIENTTIME"));
                    QAList.add(obj);
                    tmpTable.put(obj.getSId(), "Y");
                }
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;
            conn.close();
            conn = null;


            beans.put("section", "");
            beans.put("QAList", QAList);
            beans.put("QRList", QRList);
            beans.put("userName", userName);
            beans.put("nowDate", nowDate);
            beans.put("startTime", startTime);
            beans.put("endTime", endTime);


            String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/QAVerifyList.xls");
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
        }


    }


}
