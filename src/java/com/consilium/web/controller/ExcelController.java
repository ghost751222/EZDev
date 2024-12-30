package com.consilium.web.controller;


import com.consilium.excel.BeanFactoryDynamicAutowireService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping(value = "/Excel")
public class ExcelController {

    @Autowired
    BeanFactoryDynamicAutowireService beanFactoryDynamicAutowireService;

    @RequestMapping(value = "/Excel", method = RequestMethod.GET)
    public ModelAndView page() {
        ModelAndView model = new ModelAndView();
        model.setViewName("Excel");
        return model;
    }


    @RequestMapping(value = "/Esound", method = RequestMethod.GET)
    public ModelAndView page1() {
        ModelAndView model = new ModelAndView();
        model.setViewName("Esound");
        return model;
    }

    @RequestMapping(value = "/Consilium", method = RequestMethod.GET)
    public ModelAndView page2() {
        ModelAndView model = new ModelAndView();
        model.setViewName("Consilium");
        return model;
    }

    @RequestMapping(value = "/Consilium/{page}", method = RequestMethod.GET)
    public ModelAndView consiliumPage(@PathVariable("page") String page) {
        String path = "consilium/";
        ModelAndView model = new ModelAndView();
        model.setViewName(path + page);
        return model;
    }

    @RequestMapping(value = "/Esound/{page}", method = RequestMethod.GET)
    public ModelAndView eSoundPage(@PathVariable("page") String page) {
        String path = "esound/";
        ModelAndView model = new ModelAndView();
        model.setViewName(path + page);
        return model;
    }

    @RequestMapping(value = "/{page}/{serviceName}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportExcel(@RequestParam(value = "params",required = false) String params  ,@PathVariable("page") String page, @PathVariable("serviceName") String serviceName) throws Exception {
        JsonNode jsonNode;
        if(params != null)
            jsonNode = new ObjectMapper().readValue(params,JsonNode.class);
        else
            jsonNode = JsonNodeFactory.instance.objectNode();
        return beanFactoryDynamicAutowireService.exportExcel(serviceName,jsonNode);

    }

}

