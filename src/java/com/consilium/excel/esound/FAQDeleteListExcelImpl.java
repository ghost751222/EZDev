package com.consilium.excel.esound;



import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.QAExcelObject;
import com.consilium.excel.models.UnitCode;
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
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;


@Service("FAQDeleteListExcelImpl")
public class FAQDeleteListExcelImpl implements ExcelInterface
{
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
        String nowDate=dateFormater.format(now.getTime());
//        String bDataTime_YYYY = request.getParameter("bDataTime_YYYY");
//        String eDataTime_YYYY = request.getParameter("eDataTime_YYYY");

        UserObject userObj = UserInstance.getInstance().getUserObject(httpServletRequest);
        String userName=userObj.getUserName();
        Map beans = new HashMap();
        String sql="";
        format.setMinimumIntegerDigits(2);
//        String bDataTime_MM = format.format(Long.valueOf(request.getParameter("bDataTime_MM").trim()));
//        String bDataTime_DD = format.format(Long.valueOf(request.getParameter("bDataTime_DD").trim()));
//        String eDataTime_MM = format.format(Long.valueOf(request.getParameter("eDataTime_MM").trim()));
//        String eDataTime_DD = format.format(Long.valueOf(request.getParameter("eDataTime_DD").trim()));

        //String sectionId = request.getParameter("sectionId");
        //String sectionName= UnitCode.getInstance().getUnitName("M", sectionId);

        String startTime = jsonNode.get("time").get("startTime").asText();
        String endTime =jsonNode.get("time").get("endTime").asText();
        String sectionId = jsonNode.get("sectionId").asText();
        String sectionName= UnitCode.getInstance().getUnitName("M", sectionId);

        ArrayList QAList=new ArrayList();
        ArrayList QRList=new ArrayList();
        try
        {
            String sCondition="";
            if(sectionId==null||"".equals(sectionId))
            {
                sCondition=" ";
            }
            else
            {
                sCondition=" AND SECTIONID='"+sectionId+"' ";
            }


            conn = ds.getConnection();
            sql=
                    " SELECT  																																																"+
                            " 	*                                                                                                                                                                                                   "+
                            " FROM                                                                                                                                                                                                  "+
                            " (                                                                                                                                                                                                     "+
                            "     SELECT                                                                                                                                                                                            "+
                            "         SID,                                                                                                                                                                                          "+
                            "         QUESTION,                                                                                                                                                                                     "+
                            "         ANSWER,                                                                                                                                                                                       "+
                            "         KEYWORD,                                                                                                                                                                                      "+
                            "         CLOSEDATATIME,                                                                                                                                                                                "+
                            "         EFFICIENTTIME,                                                                                                                                                                                "+
                            "         LASTUPDATETIME,                                                                                                                                                                               "+
                            "         ORDERTIME,                                                                                                                                                                               "+
                            "         STATUS,                                                                                                                                                                               "+
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= qa.SID)) a where a.RowNum = 1 ) as CONNECTOR1,                              " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= qa.SID)) a where a.RowNum = 1 ) as TEL1,                                                " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= qa.SID)) a where a.RowNum = 1 ) as EXTENSION1,                               " +
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= qa.SID)) a where a.RowNum = 2 ) as CONNECTOR2,                              " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= qa.SID)) a where a.RowNum = 2 ) as TEL2,                                                " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= qa.SID)) a where a.RowNum = 2 ) as EXTENSION2                               " +
                            "     FROM                                                                                                                                                                                              "+
                            "         QUESTION_ANSWER QA                                                                                                                                                                            "+
                            "     WHERE                                                                                                                                                                                             "+
                            "         CLOSEDATATIME>=?                                                                                                                "+
                            "     AND                                                                                                                                                                                               "+
                            "         CLOSEDATATIME<=?                                                                                                                  "+
                            sCondition+
                            "     AND                                                                                                                                                                                               "+
                            "     (                                                                                                                                                                                                 "+
                            "         STATUS='D'                                                                                                                                                                                    "+
                            "         OR                                                                                                                                                                                            "+
                            "         SID IN                                                                                                                                                                                        "+
                            "         (                                                                                                                                                                                             "+
                            "             SELECT PID FROM QUESTIONANSWER_BAK WHERE PID<>0 "+sCondition+
                            "             UNION                                                                                                                                                                                     "+
                            "             SELECT PID FROM QUESTIONANSWER_RELATION WHERE STATUS='D' "+sCondition+
                            "         )                                                                                                                                                                                             "+
                            "     )                                                                                                                                                                                                 "+
                            " UNION ALL                                                                                                                                                                                             "+
                            "     SELECT                                                                                                                                                                                            "+
                            "         SID,                                                                                                                                                                                          "+
                            "         QUESTION,                                                                                                                                                                                     "+
                            "         ANSWER,                                                                                                                                                                                       "+
                            "         KEYWORD,                                                                                                                                                                                      "+
                            "         CLOSEDATATIME,                                                                                                                                                                                "+
                            "         EFFICIENTTIME,                                                                                                                                                                                "+
                            "         LASTUPDATETIME,                                                                                                                                                                               "+
                            "         LASTUPDATETIME AS ORDERTIME,                                                                                                                                                                  "+
                            "         'DD' AS STATUS,                                                                                                                                                                                "+
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as CONNECTOR1,                                                                                                                 " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as  TEL1,                                                                                                                            " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as  EXTENSION1,                                                                                                               " +
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as CONNECTOR2,                               " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as TEL2,                                                " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as EXTENSION2       "+
                            "     FROM                                                                                                                                                                                              "+
                            "         QUESTIONANSWER_BAK  QB                                                                                                                                                                        "+
                            "     WHERE                                                                                                                                                                                             "+
                            "         FLAG='A'                                                                                                                                                                                      "+
                            "     AND                                                                                                                                                                                               "+
                            "         LASTUPDATETIME>=?                                                                                                                   "+
                            "     AND                                                                                                                                                                                               "+
                            "         LASTUPDATETIME<=?                                                                                                               "+
                            sCondition+
                            " )   A                                                                                                                                                                                                  "+
                            "     ORDER BY ORDERTIME DESC                                                                                                                                                     ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,startTime+" 00:00:00");
            pstmt.setString(2,endTime+" 23:59:59");
            pstmt.setString(3,startTime+" 00:00:00");
            pstmt.setString(4,endTime+" 23:59:59");

            rs = pstmt.executeQuery();
            QAExcelObject obj;
            StringBuffer sidBuf= new StringBuffer();
            Hashtable<String, QAExcelObject> tmpTable=new Hashtable<String,QAExcelObject>();
            while(rs.next())
            {
                obj=new QAExcelObject();
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
                String status=rs.getString("STATUS");
                if("K".equals(status))
                {
                    obj.setStatus("上架");
                }
                else if("S".equals(status))
                {
                    obj.setStatus("建議下架");
                }
                else if("D".equals(status))
                {
                    obj.setStatus("下架");
                }
                else if("DD".equals(status))
                {
                    obj.setStatus("已刪除");
                }
                else
                {
                    obj.setStatus("未審核");
                }


                QAList.add(obj);
                tmpTable.put(obj.getSId(),obj);
                sidBuf.append(rs.getString("SID"));
                sidBuf.append(",");
            }

            rs.close();
            pstmt.close();

            String tmpSid=sidBuf.toString();
            if(tmpSid!=null&&tmpSid.length()>0)
            {
                tmpSid=tmpSid.substring(0,tmpSid.length()-1);
                tmpSid=" AND PID IN("+tmpSid+")";
            }
            else
            {
                tmpSid="";
            }

            sql=
                    " SELECT * FROM ( "+
                            " SELECT  																																													"+
                            "     SID,                                                                                                                                                                                  "+
                            "     PID,                                                                                                                                                                                  "+
                            "     QUESTION,                                                                                                                                                                             "+
                            "     ANSWER,                                                                                                                                                                               "+
                            "     KEYWORD,                                                                                                                                                                              "+
                            "     CLOSEDATATIME,                                                                                                                                                                        "+
                            "     EFFICIENTTIME,                                                                                                                                                                        "+
                            "     LASTUPDATETIME,                                                                                                                                                                       "+
                            "     'DD' AS STATUS,                                                                                                                                                                                "+
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as CONNECTOR1,                                                                                                                 " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as  TEL1,                                                                                                                            " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as  EXTENSION1,                                                                                                               " +
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as CONNECTOR2,                               " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as TEL2,                                                " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= QB.SID)) a where a.RowNum = 1 ) as EXTENSION2       "+
                            " FROM                                                                                                                                                                                      "+
                            "     QUESTIONANSWER_BAK QB WHERE 1=1"+sCondition+" "+tmpSid+
                            " UNION ALL                                                                                                                                                                                 "+
                            " SELECT                                                                                                                                                                                    "+
                            "     SID,                                                                                                                                                                                  "+
                            "     PID,                                                                                                                                                                                  "+
                            "     QUESTION,                                                                                                                                                                             "+
                            "     ANSWER,                                                                                                                                                                               "+
                            "     KEYWORD,                                                                                                                                                                              "+
                            "     CLOSEDATATIME,                                                                                                                                                                        "+
                            "     EFFICIENTTIME,                                                                                                                                                                        "+
                            "     LASTUPDATETIME,                                                                                                                                                                       "+
                            "     STATUS,                                                                                                                                                                   "+
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= QR.SID)) a where a.RowNum = 1 ) as CONNECTOR1,                                                                                                                 " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= QR.SID)) a where a.RowNum = 1 ) as  TEL1,                                                                                                                            " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= QR.SID)) a where a.RowNum = 1 ) as  EXTENSION1,                                                                                                               " +
                            "               (select connector from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  connector FROM QuestionAnswer_Window WHERE PID= QR.SID)) a where a.RowNum = 1 ) as CONNECTOR2,                               " +
                            "               (select tel from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  tel FROM QuestionAnswer_Window WHERE PID= QR.SID)) a where a.RowNum = 1 ) as TEL2,                                                " +
                            "               (select extension from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  extension FROM QuestionAnswer_Window WHERE PID= QR.SID)) a where a.RowNum = 1 ) as EXTENSION2       "+
                            " FROM                                                                                                                                                                                      "+
                            "     QUESTIONANSWER_RELATION QR  WHERE 1=1 "+sCondition+" "+tmpSid+
                            " ) A ORDER BY LASTUPDATETIME DESC ";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            QAExcelObject rObj;
            Hashtable<String,String> markTable = new Hashtable<String,String>();
            while(rs.next())
            {
                rObj=new QAExcelObject();
                rObj.setSId(rs.getString("SID"));
                rObj.setPId(rs.getString("PID"));
                rObj.setQuestion(rs.getString("QUESTION"));
                rObj.setAnswer(rs.getString("ANSWER"));
                rObj.setKeyword(rs.getString("KEYWORD"));
                rObj.setConnectName1(rs.getString("CONNECTOR1"));
                rObj.setTel1(rs.getString("TEL1"));
                rObj.setExten1(rs.getString("EXTENSION1"));
                rObj.setConnectName2(rs.getString("CONNECTOR2"));
                rObj.setTel2(rs.getString("TEL2"));
                rObj.setExten2(rs.getString("EXTENSION2"));
                rObj.setEfficientTime(rs.getString("EFFICIENTTIME"));
                String status = rs.getString("STATUS");

                if("K".equals(status))
                {
                    rObj.setStatus("上架");
                }
                else if("S".equals(status))
                {
                    rObj.setStatus("建議下架");
                }
                else if("D".equals(status))
                {
                    rObj.setStatus("下架");
                }
                else if("DD".equals(status))
                {
                    rObj.setStatus("已刪除");
                }
                else
                {
                    rObj.setStatus("未審核");
                }


                if(rObj.getQuestion().trim().length()>0)QRList.add(rObj);

                obj=tmpTable.get(rObj.getPId());
                if(obj!=null)
                {
                    if(markTable.get(obj.getSId())==null)//因為只要取第一筆
                    {
                        obj.setRSId(rObj.getSId());
                        obj.setRQuestion(rObj.getQuestion());
                        obj.setRAnswer(rObj.getAnswer());
                        obj.setRKeyWord(rObj.getKeyword());
                        obj.setRConnectName1(rObj.getConnectName1());
                        obj.setRTel1(rObj.getTel1());
                        obj.setRExten1(rObj.getExten1());
                        obj.setRConnectName2(rObj.getRConnectName2());
                        obj.setRTel2(rObj.getRTel2());
                        obj.setRExten2(rObj.getRExten2());
                        obj.setREfficientTime(rObj.getREfficientTime());
                        markTable.put(obj.getSId(),"Y");
                    }

                }
            }

            rs.close();
            rs=null;
            pstmt.close();
            pstmt=null;
            conn.close();
            conn=null;

        }


        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            try
            {
                if(rs!=null)
                {
                    rs.close();
                    rs=null;
                }
            }
            catch(Exception ex)
            {
                ;
            }

            try
            {
                if(pstmt!=null)
                {
                    pstmt.close();
                    pstmt=null;
                }
            }
            catch(Exception ex)
            {
                ;
            }
        }

        beans.put("QAList",QAList);
        beans.put("QRList",QRList);
        beans.put("userName",userName);
        beans.put("nowDate", nowDate);
        beans.put("startTime", startTime);
        beans.put("endTime", endTime);
        beans.put("section", sectionName);
        String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/QADelete.xls");
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
    }
}
