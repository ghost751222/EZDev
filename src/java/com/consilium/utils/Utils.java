package com.consilium.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.consilium.common.Const;
import com.consilium.domain.User;


public class Utils {
	private static final Logger logger = Logger.getLogger(Utils.class);
	
	//#########################################################################
	//						Link Sql Functions
	//#########################################################################
	public static final String SQL_WHERE_STUB = ":W:";
	public static final String SQL_PARAM_STUB = ":P:";
		
	public static String formatSqlWhere(String sql, String where) {
		String result = sql.replaceFirst(SQL_WHERE_STUB, where);
		logger.debug(result);
		return result;
	}

	public static String mergeAND(String prefix, List<String> params) {
		return merge(prefix, params, " AND ");
	}
	
	public static String mergeOR(String prefix, List<String> params) {
		return merge(prefix, params, " OR ");
	}
	
	public static String merge(String prefix, List<String> params, String conn) {
		int len = params.size();
		if (len == 0) return "";
		StringBuilder result = new StringBuilder();
		boolean addAnd = false;
		for (String p : params) {
			if (p.length() == 0) continue;
			if (addAnd) result.append(conn);
			result.append(p);
			addAnd = true;
		}
		if (result.length() != 0) {
			result.insert(0, prefix + " ");
		}
		return result.toString();
	}
	
	
	public static String getCondition(String sql, String value) {
		if (StringUtils.isNotBlank(value))
			return sql.replaceFirst(Utils.SQL_PARAM_STUB, value);
		return "";
	}
	//#########################################################################
	//					Get Upload Path
	//#########################################################################
//	public static String getBizUpload(String type){
//		String outStream = "";
//		if (type.equals("1"))
//			outStream =Utils.BIZUPLOAD;
//		if (type.equals("2"))
//			outStream =Utils.ENTRUSTUPLOAD;
//		if (type.equals("3"))
//			outStream =Utils.SLIPUPLOAD;
//		if (type.equals("4"))
//			outStream =Utils.POLICYUPLOAD;
//		if (type.equals("5"))
//			outStream =Utils.AGREEUPLOAD;
//		if (type.equals("6"))
//			outStream = Utils.LISTUPLOAD;
//		if (type.equals("7"))
//			outStream= Utils.OTHUPLOAD;
//		if (type.equals("8"))
//			outStream= Utils.QUOUPLOAD;
//		return outStream;
//	}
	//#########################################################################
	//					Get Attributes From Session
	//#########################################################################
	public static User getUserFromSession(HttpServletRequest request) {
		return (User)getFromSession(request, Const.SESSION_USER);
	}
	public static String getCompFromSession(HttpServletRequest request){
		return (String)getFromSession(request,Const.SESSION_COMP);
	}
	public static Object getFromSession(HttpServletRequest request, String key) {
		return request.getSession().getAttribute(key);
	}
	
	public static Map<String, String> getParams(HttpServletRequest request, String...params) {
		Map<String, String> result = new HashMap<String, String>();
		String tmp;
		for(String p : params) {
			tmp = request.getParameter(p);
			result.put(p, StringUtils.isEmpty(tmp) ? "" : tmp);
		}
		return result;
	}
}

