package com.consilium.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.consilium.common.Const;
import com.consilium.domain.User;

public class SecurityFilter implements Filter {
	private String loginPage;	// @see applicationContext.xml
	public void setLoginPage(String url) {
		this.loginPage = url;
	}
	
//	private UserService userService;
//	public void setUserService(UserService service) {
//		this.userService = service;
//	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String path = httpRequest.getServletPath();
		
		if ( path.indexOf(loginPage) == -1 ) {
			HttpSession session = httpRequest.getSession(true);
			User user = (User)session.getAttribute(Const.SESSION_USER);
			if ( user == null ) {
				httpResponse.sendRedirect(httpRequest.getContextPath() + loginPage);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
}

