package com.consilium.excel.models;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ServiceLocator implements InitializingBean {

    private static ServiceLocator serviceLocator;
    @Autowired
    DataSource ds;

    public static ServiceLocator getInstance() {
        return serviceLocator;
    }

    public DataSource getDataSource() {
        return ds;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        serviceLocator = this;

    }
}
