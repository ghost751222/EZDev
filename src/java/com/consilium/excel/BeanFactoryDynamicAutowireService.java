package com.consilium.excel;

import com.consilium.excel.interfaces.ExcelInterface;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BeanFactoryDynamicAutowireService {
    private static final String SERVICE_NAME_SUFFIX = "ExcelImpl";
    private final BeanFactory beanFactory;

    @Autowired
    public BeanFactoryDynamicAutowireService(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ResponseEntity<byte[]> exportExcel(String serviceName,JsonNode jsonNode) throws Exception {
        ExcelInterface service = beanFactory.getBean(getRegionServiceBeanName(serviceName),
                ExcelInterface.class);

        return service.exportExcel(jsonNode);
    }

    private String getRegionServiceBeanName(String serviceName) {
        return serviceName + SERVICE_NAME_SUFFIX;
    }
}