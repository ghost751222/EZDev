package com.consilium.excel.consilium;


import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.QAExcelObject;
import com.consilium.excel.models.UnitCode;
import com.consilium.excel.models.UserInstance;
import com.consilium.excel.models.UserObject;
import com.consilium.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import java.text.SimpleDateFormat;
import java.util.*;


@Service("ConsiliumFAQDataListExcelImpl")
public class ConsiliumFAQDataListExcelImpl implements ExcelInterface {
    private static final Logger logger = Logger.getLogger(ConsiliumFAQDataListExcelImpl.class);

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    DataSource ds;

    private String excelName = "FAQ清單.xls";

    private String reportPath = "/WEB-INF/report/consilium/QAData.xls";

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar now = Calendar.getInstance();
        String nowDate = dateFormater.format(now.getTime());
        UserObject userObj = UserInstance.getInstance().getUserObject(httpServletRequest);
        String userName = userObj.getUserName();
        Map beans = new HashMap();
        String sql = "";
        ArrayList<QAExcelObject> QAList = new ArrayList<>();
        ArrayList<QAExcelObject> QRList = new ArrayList<>();
        String sectionId = jsonNode.has("sectionId") ? jsonNode.get("sectionId").asText(null) : "";

        String mainUnit = jsonNode.has("mainUnit") ? jsonNode.get("mainUnit").asText(null) : "";
        String subUnit= jsonNode.has("subUnit") ?  jsonNode.get("subUnit").asText(null):"";
        String functionFlag = jsonNode.has("flag") ? jsonNode.get("flag").asText(null) : "";

        try {
            String sqlUnitCondition = "";
            String sqlSectionCondition = "";

            if ((functionFlag != null) && ("true".equals(functionFlag))) {

                if ((mainUnit != null) && (mainUnit.length() != 0)) {
                    sqlUnitCondition = "AND UNITID = ? ";
                }
                if ((subUnit != null) && (subUnit.length() != 0)) {
                    sqlSectionCondition = "AND SECTIONID = ? ";
                }
            } else {
                sqlUnitCondition = "";
                sqlSectionCondition = "AND SECTIONID = ? ";
                subUnit = sectionId;
            }

            conn = ds.getConnection();
            sql =
                    "       SELECT                                                                                                                                                                                                            " +
                            "               SID,                                                                                                                                                                                                      " +
                            "               UNITID,                                                                                                                                                                                                 " +
                            "               SECTIONID,                                                                                                                                                                                                 " +
                            "               QUESTION,                                                                                                                                                                                                 " +
                            "               ANSWER,                                                                                                                                                                                                   " +
                            "               KEYWORD,                                                                                                                                                                                                  " +
                            "               EFFICIENTTIME,                                                                                                                                                                                            " +
                            "               CLOSEDATATIME,                                                                                                                                                                                            " +
                            "               ORDERTIME,CREATETIME,LASTUPDATETIME,                                                                                                                                                                                                " +
                            "               (select WININFO1 from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  isnull(CONNECTOR,'') + ',' + isnull(TEL,'') + ',' + isnull(EXTENSION,'') AS WININFO1 FROM QuestionAnswer_Window WHERE PID= A.SID)) a where a.RowNum = 1 ) as WININFO1," +
                            "               (select WININFO2 from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  isnull(CONNECTOR,'') + ',' + isnull(TEL,'') + ',' + isnull(EXTENSION,'') AS WININFO2 FROM QuestionAnswer_Window WHERE PID= A.SID)) a where a.RowNum = 2 ) as WININFO12" +
                            "       FROM                                                                                                                                                                                                              " +
                            "               QUESTION_ANSWER  A  inner  join units u on a.sectionId = u.unitcode and u.superunit not like '%_D'                                                                                                                                                                                        " +
                            "       WHERE                                                                                                                                                                                                             " +
                            "               STATUS<>'D' And unitid not in ('B4000','E3000','F9000') " + sqlUnitCondition + sqlSectionCondition + " ORDER BY  ORDERTIME DESC                                                                                                                                                                         ";

            pstmt = conn.prepareStatement(sql);
            int i = 0;
            if (sqlUnitCondition.length() > 0) {
                pstmt.setString(++i, mainUnit);
            }

            if (sqlSectionCondition.length() > 0) {
                pstmt.setString(++i, subUnit);
            }

            rs = pstmt.executeQuery();
            QAExcelObject obj;
            Hashtable<String, QAExcelObject> mappingTable = new Hashtable<String, QAExcelObject>();
            while (rs.next()) {
                obj = new QAExcelObject();
                obj.setSId(rs.getString("SID"));
                obj.setQuestion(rs.getString("QUESTION"));
                obj.setAnswer(rs.getString("ANSWER"));
                obj.setKeyword(rs.getString("KEYWORD"));

                String winInfo1 = rs.getString("WININFO1");
                if (winInfo1 != null && winInfo1.length() > 0) {
                    String[] tmpArr = winInfo1.split(",");
                    if (tmpArr.length == 3) {
                        obj.setConnectName1(tmpArr[0]);
                        obj.setTel1(tmpArr[1]);
                        obj.setExten1(tmpArr[2]);
                    } else if (tmpArr.length == 2) {
                        obj.setConnectName1(tmpArr[0]);
                        obj.setTel1(tmpArr[1]);
                    }

                }

                String winInfo2 = rs.getString("WININFO12");
                if (winInfo2 != null && winInfo2.length() > 0) {
                    String[] tmpArr = winInfo2.split(",");
                    if (tmpArr.length == 3) {
                        obj.setConnectName2(tmpArr[0]);
                        obj.setTel2(tmpArr[1]);
                        obj.setExten2(tmpArr[2]);
                    } else if (tmpArr.length == 2) {
                        obj.setConnectName2(tmpArr[0]);
                        obj.setTel2(tmpArr[1]);
                    }

                }

                obj.setUnitId(UnitCode.getInstance().getUnitName("M", rs.getString("UNITID")));
                obj.setSectionId(UnitCode.getInstance().getUnitName("M", rs.getString("SECTIONID")));
                obj.setEfficientTime(rs.getString("EFFICIENTTIME"));
                obj.setCreateTime(StringUtil.formatChineseDate(rs.getTimestamp("CREATETIME")));
                obj.setLastUpdateTime(StringUtil.formatChineseDate(rs.getTimestamp("LASTUPDATETIME")));
                QAList.add(obj);
                mappingTable.put(obj.getSId(), obj);

            }

            rs.close();
            pstmt.close();

            sql =
                    " SELECT                                                                                                                                                                                " +
                            " 	SID AS RSID,                                                                                                                                                                         " +
                            " 	PID AS RPID,                                                                                                                                                                         " +
                            "  UNITID,                                                                                                                                                                                                 " +
                            "  SECTIONID,                                                                                                                                                                                                 " +
                            " 	QUESTION AS RQUESTION,                                                                                                                                                               " +
                            " 	ANSWER AS RANSWER,                                                                                                                                                                   " +
                            " 	KEYWORD AS RKEYWORD,                                                                                                                                                                 " +
                            " 	EFFICIENTTIME AS REFFICIENTTIME,                                                                                                                                                     " +
                            " 	CLOSEDATATIME AS RCLOSEDATATIME,                                                                                                                                                     " +
                            " 	CREATETIME,LASTUPDATETIME,                                                                                                                                                                      " +
                            "               (select WININFO1 from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  isnull(CONNECTOR,'') + ',' + isnull(TEL,'') + ',' + isnull(EXTENSION,'') AS WININFO1 FROM QuestionAnswer_Window WHERE PID= B.SID)) a where a.RowNum = 1 ) as WININFO1," +
                            "               (select WININFO2 from (  (SELECT ROW_NUMBER() over (order by PID) as RowNum,  isnull(CONNECTOR,'') + ',' + isnull(TEL,'') + ',' + isnull(EXTENSION,'') AS WININFO2 FROM QuestionAnswer_Window WHERE PID= B.SID)) a where a.RowNum = 2 ) as WININFO12" +
                            " FROM                                                                                                                                                                                  " +
                            " 	QUESTIONANSWER_RELATION  B    inner  join units u on b.sectionId = u.unitcode and u.superunit not like '%_D'                                                                                                                                                       " +
                            " WHERE                                                                                                                                                                                 " +
                            " 	EXISTS(SELECT MAX(LASTUPDATETIME),PID FROM QUESTIONANSWER_RELATION  BB  GROUP BY PID HAVING B.PID=BB.PID AND isnull(B.LASTUPDATETIME,getdate())= isnull(MAX(LASTUPDATETIME),getdate())) AND STATUS<>'D' " + sqlUnitCondition + sqlSectionCondition;


            pstmt = conn.prepareStatement(sql);

            i = 0;

            if (sqlUnitCondition.length() > 0) {
                pstmt.setString(++i, mainUnit);
            }

            if (sqlSectionCondition.length() > 0) {
                pstmt.setString(++i, subUnit);
            }

            rs = pstmt.executeQuery();
            QAExcelObject rObj = null;
            while (rs.next()) {
                obj = mappingTable.get(rs.getString("RPID"));
                if (obj != null) {
                    obj.setRSId(rs.getString("RSID"));
                    obj.setRPId(rs.getString("RPID"));
                    obj.setRQuestion(rs.getString("RQUESTION"));
                    obj.setRAnswer(rs.getString("RANSWER"));
                    obj.setRKeyWord(rs.getString("RKEYWORD"));

                    String winInfo1 = rs.getString("WININFO1");
                    if (winInfo1 != null && winInfo1.length() > 0) {
                        String[] tmpArr = winInfo1.split(",");
                        if (tmpArr.length == 3) {
                            obj.setRConnectName1(tmpArr[0]);
                            obj.setRTel1(tmpArr[1]);
                            obj.setRExten1(tmpArr[2]);
                        } else if (tmpArr.length == 2) {
                            obj.setRConnectName1(tmpArr[0]);
                            obj.setRTel1(tmpArr[1]);
                        }

                    }

                    String winInfo2 = rs.getString("WININFO12");
                    if (winInfo2 != null && winInfo2.length() > 0) {
                        String[] tmpArr = winInfo2.split(",");
                        if (tmpArr.length == 3) {
                            obj.setRConnectName2(tmpArr[0]);
                            obj.setRTel2(tmpArr[1]);
                            obj.setRExten2(tmpArr[2]);
                        } else if (tmpArr.length == 2) {
                            obj.setRConnectName2(tmpArr[0]);
                            obj.setRTel2(tmpArr[1]);
                        }


                    }

                    obj.setREfficientTime(rs.getString("REFFICIENTTIME"));
                }

                rObj = new QAExcelObject();
                rObj.setSId(rs.getString("RSID"));
                rObj.setQuestion(rs.getString("RQUESTION"));
                rObj.setAnswer(rs.getString("RANSWER"));
                rObj.setKeyword(rs.getString("RKEYWORD"));

                String rWinInfo1 = rs.getString("WININFO1");
                if (rWinInfo1 != null && rWinInfo1.length() > 0) {
                    String[] tmpArr = rWinInfo1.split(",");
                    if (tmpArr.length == 3) {
                        rObj.setRConnectName1(tmpArr[0]);
                        rObj.setRTel1(tmpArr[1]);
                        rObj.setRExten1(tmpArr[2]);
                    } else if (tmpArr.length == 2) {
                        rObj.setRConnectName1(tmpArr[0]);
                        rObj.setRTel1(tmpArr[1]);
                    }

                }

                String rWinInfo2 = rs.getString("WININFO12");
                if (rWinInfo2 != null && rWinInfo2.length() > 0) {
                    String[] tmpArr = rWinInfo2.split(",");
                    if (tmpArr.length == 3) {
                        rObj.setRConnectName2(tmpArr[0]);
                        rObj.setRTel2(tmpArr[1]);
                        rObj.setRExten2(tmpArr[2]);
                    } else if (tmpArr.length == 2) {
                        rObj.setRConnectName2(tmpArr[0]);
                        rObj.setRTel2(tmpArr[1]);
                    }


                }

                rObj.setEfficientTime(rs.getString("REFFICIENTTIME"));
                rObj.setUnitId(UnitCode.getInstance().getUnitName("M", rs.getString("UNITID")));
                rObj.setSectionId(UnitCode.getInstance().getUnitName("M", rs.getString("SECTIONID")));
                rObj.setCreateTime(StringUtil.formatChineseDate(rs.getTimestamp("CREATETIME")));
                rObj.setLastUpdateTime(StringUtil.formatChineseDate(rs.getTimestamp("LASTUPDATETIME")));
                if (rObj.getQuestion().trim().length() > 0) QRList.add(rObj);
            }

            rs.close();
            rs = null;
            pstmt.close();
            pstmt = null;
            conn.close();
            conn = null;

        } catch (Exception e) {
            logger.error(e);
            throw e;
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

            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (Exception ex) {
                ;
            }
        }


        beans.put("QAList", QAList);
        beans.put("QRList", QRList);
        beans.put("userName", userName);
        beans.put("nowDate", nowDate);
        beans.put("section", UnitCode.getInstance().getUnitName("M", sectionId));

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
    }
}
