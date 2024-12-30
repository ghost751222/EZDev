package com.consilium.excel.esound;

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

import com.consilium.excel.interfaces.ExcelInterface;
import com.consilium.excel.models.ProcessTypeReportObject;

import com.consilium.excel.models.ServiceInstance;
import com.consilium.excel.models.ServiceItemObject;
import com.consilium.excel.models.ServiceTypeObject;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("ProcessTypeReportExcelImpl")
public class ProcessTypeReportExcelImpl  implements ExcelInterface {
	@Autowired
	HttpServletRequest httpServletRequest;

	@Autowired
	DataSource ds;

	@Override
	public ResponseEntity<byte[]> exportExcel(JsonNode jsonNode) throws Exception {


			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sqlQuery = "";
			ArrayList<ProcessTypeReportObject> list = new ArrayList<ProcessTypeReportObject>();
			NumberFormat format = NumberFormat.getInstance();
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar now = Calendar.getInstance();
			//String bDataTime_YYYY = request.getParameter("bDataTime_YYYY");
			//String eDataTime_YYYY = request.getParameter("eDataTime_YYYY");

			format.setMinimumIntegerDigits(2);
			//String bDataTime_MM = format.format(Long.valueOf(request.getParameter("bDataTime_MM")));
			//String bDataTime_DD = format.format(Long.valueOf(request.getParameter("bDataTime_DD")));
			//String eDataTime_MM = format.format(Long.valueOf(request.getParameter("eDataTime_MM")));
			//String eDataTime_DD = format.format(Long.valueOf(request.getParameter("eDataTime_DD")));
			//String startDate=bDataTime_YYYY + "-" + bDataTime_MM + '-' + bDataTime_DD;
			//String endDate=eDataTime_YYYY + "-" + eDataTime_MM + '-' + eDataTime_DD;
			String startDate = jsonNode.get("time").get("startTime").asText();
			String endDate = jsonNode.get("time").get("endTime").asText();
			String printDate = dateFormater.format(now.getTime());

			Map beans = new HashMap();
			try {
				sqlQuery =
						" SELECT      												" +
								" 	S.ITEMID, F.PROCESSTYPE, COUNT(1) AS AMOUNT             " +
								" FROM                                                      " +
								" 	CALLLOG_FORM F, CALLLOG_SERVICEITEM S                   " +
								" WHERE                                                     " +
								" 	F.ACTIONID = S.ACTIONID                                 " +
								" AND                                                       " +
								" 	F.CREATETIME > ?      " +
								" AND                                                       " +
								" 	F.CREATETIME <  ?     " +
								" GROUP BY                                                  " +
								" 	S.ITEMID, F.PROCESSTYPE                                 ";


				conn = ds.getConnection();
				pstmt = conn.prepareStatement(sqlQuery);
				pstmt.setString(1, startDate + " 00:00:00");
				pstmt.setString(2, endDate + " 23:59:59");
				rs = pstmt.executeQuery();
				Hashtable<String, Integer> valueTable = new Hashtable<String, Integer>();
				String key = "";
				while (rs.next()) {
					key = rs.getString("ITEMID") + "-" + rs.getString("PROCESSTYPE");
					valueTable.put(key, new Integer(rs.getInt("AMOUNT")));
				}
				rs.close();
				pstmt.close();


				ArrayList<ServiceTypeObject> typeList = ServiceInstance.getInstance().getTypeEnableList();
				for (ServiceTypeObject typeObj : typeList) {
					ArrayList<ServiceItemObject> itemList = ServiceInstance.getInstance().getTypeObject(typeObj.getTypeId()).getItemEnableList();
					for (ServiceItemObject iObj : itemList) {
						ProcessTypeReportObject obj = new ProcessTypeReportObject();
						obj.setTypeId(typeObj.getTypeId());
						obj.setTypeName(typeObj.getTypeName());
						obj.setItemId(iObj.getItemId());
						obj.setItemName(iObj.getItemName());

						int valueA = valueTable.get(obj.getItemId() + "-A") == null ? 0 : valueTable.get(obj.getItemId() + "-A");
						int valueB = valueTable.get(obj.getItemId() + "-B") == null ? 0 : valueTable.get(obj.getItemId() + "-B");
						int valueC = valueTable.get(obj.getItemId() + "-C") == null ? 0 : valueTable.get(obj.getItemId() + "-C");
						int valueD = valueTable.get(obj.getItemId() + "-D") == null ? 0 : valueTable.get(obj.getItemId() + "-D");

						obj.setValueA(valueA);
						obj.setValueB(valueB);
						obj.setValueC(valueC);
						obj.setValueD(valueD);
						list.add(obj);
					}
				}

				beans.put("list", list);
				beans.put("startDate", startDate);
				beans.put("endDate", endDate);
				beans.put("printDate", printDate);


				String path = httpServletRequest.getServletContext().getRealPath("/WEB-INF/report/esound/ProcessType.xls");
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
