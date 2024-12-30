package com.consilium.service;

import com.consilium.excel.models.UserInstance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.servlet.http.HttpServletRequest;


public abstract class BaseService {
    protected static final Logger logger = Logger.getLogger(BaseService.class);


    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    UserInstance userInstance;

    @Autowired
    protected JdbcTemplate jdbcTemplate;


    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

}
