package com.consilium.excel.componet;

import com.consilium.domain.AppCode;
import com.consilium.service.AppCodeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Vector;

@Component
public class ApplicationCode implements InitializingBean {


    private static ApplicationCode applicationCode;

    @Autowired
    AppCodeService appCodeService;


    public static ApplicationCode getInstance() {
        return applicationCode;
    }

    public String getCodeDisplay(String category, String codeCode) {
        String display = "";
        List<AppCode> appCodes = appCodeService.findAllByCategoryAndCodeCode(category, codeCode);
        if (appCodes.size() > 0) display = appCodes.get(0).getCodeDisp();
        return display;
    }

    public Vector<String> getCodeCodeList(String category) {
        List<AppCode> appCodes = appCodeService.findAllByCategory(category);
        Vector<String> vector = new Vector<>();
        for (AppCode appCode : appCodes) {
            vector.add(appCode.getCodeCode());

        }

        return vector;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationCode = this;
    }
}
