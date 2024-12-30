package com.consilium.view;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.json.JsonExceptionHandler;

/**
 * @author Greco
 * @date 2010-3-6
 * @version $Id$
 */
public class StackTraceExceptionHandler implements JsonExceptionHandler {
	private static final Logger logger = Logger.getLogger(StackTraceExceptionHandler.class);
	
	public static final String STACKTRACE_MODEL_KEY = "stacktrace";
	private String modelKey = STACKTRACE_MODEL_KEY;
	private boolean replaceLineBreakes = true;
	
	@SuppressWarnings("unchecked")
	@Override
	public void triggerException(Exception exception, Map model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringWriter swr = new StringWriter();
		exception.printStackTrace(new PrintWriter(swr));
		String stackTrace = swr.getBuffer().toString();

		if (isReplaceLineBreakes())
			stackTrace = stackTrace.replaceAll("\n", "</br>");

		model.put(getModelKey(), stackTrace);
		
		logger.error("[1996 Exception]", exception);
	}

	public void setModelKey(String modelKey) {
		this.modelKey = modelKey;
	}
	public String getModelKey() {
		return modelKey;
	}

	public void setReplaceLineBreakes(boolean replaceLineBreakes) {
		this.replaceLineBreakes = replaceLineBreakes;
	}
	public boolean isReplaceLineBreakes() {
		return replaceLineBreakes;
	}

}
