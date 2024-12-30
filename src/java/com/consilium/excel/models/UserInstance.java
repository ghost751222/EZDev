package com.consilium.excel.models;

import com.consilium.domain.User;
import com.consilium.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class UserInstance implements InitializingBean {

    private static final Logger logger = Logger.getLogger(UserInstance.class);
    @Autowired
    HttpSession httpSession;

    private List<User> users;

    @Autowired
    UserService userService;
    private static UserInstance userInstance;

    public static UserInstance getInstance() {
        return userInstance;
    }

    public UserObject getUserObject(HttpServletRequest request) {
        logger.info("request url  = " +   request.getRequestURL() + request.getQueryString() +" request method = " + request.getMethod());
        String userID = request.getParameter("userId");
        logger.info("UserInstance getUserObject request userID= " + userID);
        if (userID == null)
            userID = (String) httpSession.getAttribute("userID");
        logger.info("UserInstance getUserObject session userID= " + userID);
        return getUserObject(userID);
    }

    public UserObject getUserObject(String userID) {
        UserObject userObject = new UserObject();
        userObject.setUserName("");
        this.users = userService.findAll();
        for (User u : users) {
            if ( "Y".equalsIgnoreCase(u.getIsActive().trim())  && (u.getUserId().equalsIgnoreCase(userID) || u.getAccount().equalsIgnoreCase(userID))   ) {
                userObject.setUserID(u.getUserId());
                userObject.setUserName(u.getUserName());
                break;
            }
        }
        return userObject;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        userInstance = this;
        this.users = userService.findAll();
    }
}
