package com.consilium.filter;

import com.consilium.domain.ViewUnitUserID;
import com.consilium.excel.models.UserInstance;
import com.consilium.service.ViewUnitUserIDService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@Component
public class RequestProcessingInterceptor implements HandlerInterceptor {

    @Autowired
    HttpSession httpSession;

    @Autowired
    ViewUnitUserIDService viewUnitUserIDService;
    private static final Logger logger = Logger.getLogger(RequestProcessingInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        String url = request.getRequestURI();
        String userID = request.getParameter("userId");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws IOException {
        try {
            String url = request.getRequestURI();
            String userID = request.getParameter("userId");
            if (userID == null) {
                userID = (String) httpSession.getAttribute("userID");
            }

            List<ViewUnitUserID> viewUnitUserIDList = viewUnitUserIDService.findByAccount(userID);
            ViewUnitUserID viewUnitUserID = null;
            if (!viewUnitUserIDList.isEmpty()) {
                for (ViewUnitUserID v : viewUnitUserIDList) {
                    if ("Y".equals(v.getIsActive())) {
                        viewUnitUserID = v;
                        break;
                    }
                }
                String authorization = new ObjectMapper().writeValueAsString(viewUnitUserID);
                httpSession.setAttribute("userID", userID);
                httpSession.setAttribute("authorization", authorization);
                if(url.toLowerCase().contains("question"))
                    logger.info("request url  = " +   request.getRequestURL() + request.getQueryString() +" request method = " + request.getMethod());
                if (modelAndView != null)
                    modelAndView.addObject("authorization", authorization);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception e) {
            if (!e.getMessage().equals("Cannot call sendError() after the response has been committed") && !e.getMessage().equals("Cannot create a session after the response has been committed")) {
                throw e;
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
    }

}
