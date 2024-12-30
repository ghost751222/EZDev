package com.consilium.view;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.sojo.core.UniqueIdGenerator;
import net.sf.sojo.interchange.Serializer;
import net.sf.sojo.interchange.json.JsonSerializer;

import org.springframework.web.servlet.view.AbstractView;

import com.consilium.common.Const;

/**
 * @author Marco Ma
 * @date 2012-3-14
 * @version $Id$
 */
public class ConfigView extends AbstractView {
	private static final String[] exclude = {"class", UniqueIdGenerator.UNIQUE_ID_PROPERTY};
//	private static final String[] excludeExitCode = {"class", 
//		UniqueIdGenerator.UNIQUE_ID_PROPERTY,
//		"id", "status", "isSystem"};

	 private static final Serializer serializer = new JsonSerializer();

	/* 
	 * @see org.springframework.web.servlet.view.AbstractView
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/javascript");
		response.setCharacterEncoding("UTF-8");
		
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragrma", "no-cache");
		response.setDateHeader("Expires", 0);
		
		PrintWriter out = response.getWriter();
		out.print("Ext.ns(\"Config\");");
		
		StringBuilder buffer = new StringBuilder();
		
		// Menus
		buffer = new StringBuilder("Config.menus=");
		List menus = (List)model.get(Const.MODEL_MENUS);
		buffer.append(serializer.serialize(menus, exclude));
		buffer.append(";");
		out.print(buffer.toString());
		
		out.flush();
		out.close();
	}

}

