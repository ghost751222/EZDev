package com.consilium.web.controller;


import com.consilium.domain.ServiceItem;
import com.consilium.service.ServiceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/ServiceItem", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ServiceItemController {

    @Autowired
    ServiceItemService serviceItemService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<ServiceItem> findAll() {
        return serviceItemService.findAll();
    }

}