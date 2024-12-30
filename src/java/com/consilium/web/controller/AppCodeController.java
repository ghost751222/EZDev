package com.consilium.web.controller;


import com.consilium.domain.AppCode;
import com.consilium.service.AppCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/AppCode", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AppCodeController {

    @Autowired
    AppCodeService appCodeService;


    @RequestMapping(value = "/findAllByCategory/{category}", method = RequestMethod.GET)
    @ResponseBody
    public List<AppCode> findAllByCategory(@PathVariable("category") String category) {

        return appCodeService.findAllByCategory(category);
    }


}