package com.consilium.web.controller;


import com.consilium.domain.ServiceType;
import com.consilium.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/ServiceType", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ServiceTypeController {

    @Autowired
    ServiceTypeService serviceTypeService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<ServiceType> findAll() {
        return serviceTypeService.findAll();
    }

}