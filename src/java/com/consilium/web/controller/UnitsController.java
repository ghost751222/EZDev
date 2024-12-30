package com.consilium.web.controller;


import com.consilium.domain.Units;
import com.consilium.service.UnitsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/Units",produces = {MediaType.APPLICATION_JSON_VALUE})
public class UnitsController {

    @Autowired
    UnitsService unitsService;


    @RequestMapping(value = "/page" , method = RequestMethod.GET)
    public ModelAndView page() {
        ModelAndView model = new ModelAndView();
        model.setViewName("units");
        return model;
    }


    @RequestMapping(value = "/findAll" , method = RequestMethod.GET)
    @ResponseBody
    public List<Units> findAll() {
        return unitsService.findAll();
    }


    @RequestMapping(value = "/" , method = RequestMethod.POST)
    @ResponseBody
    public Units insert(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Units Units = objectMapper.readValue(data, Units.class);
        unitsService.insert(Units);
        return Units;

    }

    @RequestMapping(value = "/" , method = RequestMethod.PUT)
    @ResponseBody
    public Units update(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Units Units = objectMapper.readValue(data, Units.class);
        unitsService.update(Units);
        return Units;

    }

    @RequestMapping(value = "/" , method = RequestMethod.DELETE)
    @ResponseBody
    public Units delete(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Units Units = objectMapper.readValue(data, Units.class);
        unitsService.delete(Units);
        return Units;

    }

}