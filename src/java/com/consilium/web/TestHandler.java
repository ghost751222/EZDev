package com.consilium.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.stereotype.Controller;
import com.consilium.service.UserService;

@Controller (value = "TestHandler")
public class TestHandler extends BaseAjaxController {
	@Autowired
	private UserService service;
	public void setService(UserService service) {
		this.service = service;
	}
	public ModelAndView doTest (HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ModelAndView mv= null;
		String no = request.getParameter("item");
		mv= this.getSuccessModelView();
		int i = service.Test(no);
		mv.addObject("infos", i);
		return mv;
	}
}
