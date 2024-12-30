package com.consilium.web;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class BaseAjaxController extends MultiActionController {
	protected static final Logger logger = Logger.getLogger(BaseAjaxController.class);
	
	protected final static String FAILURE = "failure";
	protected final static String SUCCESS = "success";
	protected final static String ERROR = "error";
	protected final static String COUNT = "count";
	private  String successView = "jsonView";
	private String errorView = "jsonView";
	
	protected ModelAndView getSuccessModelView() {
		ModelAndView mv = new ModelAndView(getSuccessView());
		
		mv.addObject(SUCCESS, Boolean.TRUE);
		
		return mv;
	}
	protected ModelAndView getErrorModelView() {
		ModelAndView mv = new ModelAndView(getErrorView());
		
		mv.addObject(FAILURE, Boolean.TRUE);
		mv.addObject(SUCCESS, Boolean.FALSE);
		
		return mv;
	}
	
	protected ModelAndView getErrorMessageView(String errorMsg) {
		ModelAndView mv = this.getErrorModelView();
		mv.addObject(ERROR, errorMsg);
		return mv;
	}
	
	public String getSuccessView() {
		return successView;
	}
	public void setSuccessView(String viewName) {
		this.successView = viewName;
	}
	public String getErrorView() {
		return errorView;
	}
	public void setErrorView(String viewName) {
		this.errorView = viewName;
	}
	
	public ModelAndView validParams(String... strs) {
		if (!isEmptys(strs))
			return null;
		ModelAndView mv = this.getErrorModelView();
		mv.addObject(ERROR, "您所提交的参数有误!");
		return mv;
	}

	public static boolean isEmptys(String... strs) {
		for(String s : strs) {
			if (StringUtils.isEmpty(s))
				return true;
		}
		return false;
	}
}

