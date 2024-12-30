package com.consilium.excel.esound;

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.UserInstance;
import com.consilium.service.BaseService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("SectionDataReportExcelImpl")
public class SectionDataReportExcelImpl extends BaseService implements ExcelInterface {




    @Autowired
    HttpServletRequest httpServletRequest;


    private List<Map<String,Object>> findExcelData(String unitId,String sectionId){

        String sql="SELECT sp.unitName , u.unitName as sectionName,sd.occupation1,sd.connector1,sd.tel1,sd.extension1,sd.email1,sd.occupation2, sd.connector2,sd.tel2,sd.extension2,sd.email2\n" +
                "  FROM  [Section_Data] sd " +
                "  inner join Units u on u.unitCode = sd.sectionId and right(u.superUnit,2) !='_D' " +
                "  inner join (select * from Units u    where u.superUnit is  null) sp on sd.unitId = sp.unitCode " +
                "  where (sd.unitId = :unitId or :unitId is null) " +
                "   and  (sd.sectionId = :sectionId or :sectionId is null)"+
                "  order by sd.unitId " ;

        Map<String,Object> params = new HashMap<>();
        params.put("unitId",unitId);
        params.put("sectionId",sectionId);
        return  namedParameterJdbcTemplate.queryForList(sql,params);


    }

    @Override
    public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {
        int startRow =3, startColumn = 0;

        List<Map<String,Object>> excelData= findExcelData(jsonNode.get("unitId").asText(null),jsonNode.get("sectionId").asText(null));
        String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/連絡窗口清單.xlsx");
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow xssfRow = sheet.getRow(startRow);
        XSSFCell xssfCell = xssfRow.getCell(startColumn);
        XSSFCellStyle xssfCellStyle=   xssfCell.getCellStyle();

        String exportMemberName = UserInstance.getInstance().getUserObject(httpServletRequest).getUserName();
        sheet.getRow(1).getCell(0).setCellValue(String.format("匯出人員：%s",exportMemberName));

        for (Map<String,Object> map: excelData) {
            startColumn = 0;
             xssfRow =  sheet.createRow(startRow);
            for (Map.Entry<String, Object> pair : map.entrySet()) {
                xssfCell = xssfRow.createCell(startColumn);
                xssfCell.setCellValue(pair.getValue() == null? "":pair.getValue().toString());
                xssfCell.setCellStyle(xssfCellStyle);
                //sheet.getRow(startRow).getCell(startcolumn).setCellValue(pair.getValue().toString());
                startColumn++;
            }
            startRow++;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)))
                .body(outputStream.toByteArray());
    }
}
