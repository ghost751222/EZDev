package com.consilium.web.controller;


import com.consilium.domain.SectionData;
import com.consilium.domain.SectionSuperManager;
import com.consilium.service.SectionDataService;
import com.consilium.service.SectionSuperManagerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/SectionSuperManager",produces = {MediaType.APPLICATION_JSON_VALUE})
public class SectionSuperManagerController {

    @Autowired
    SectionSuperManagerService sectionSuperManagerService;


    @RequestMapping(value = "/{unitId}", method = RequestMethod.GET)
    @ResponseBody
    public List<SectionSuperManager> get(@PathVariable("unitId") String sId) throws JsonProcessingException {
        return sectionSuperManagerService.findUnitId(sId);
    }


//    @RequestMapping(value = "/{sId}", method = RequestMethod.GET)
//    @ResponseBody
//    public List<SectionSuperManager> get(@PathVariable("sId") int sId) throws JsonProcessingException {
//        return sectionSuperManagerService.findBySid(sId);
//    }




    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public SectionSuperManager insert(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SectionSuperManager sectionSuperManager = objectMapper.readValue(data, SectionSuperManager.class);
        return  sectionSuperManagerService.insert(sectionSuperManager);

    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public SectionSuperManager update(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SectionSuperManager sectionSuperManager = objectMapper.readValue(data, SectionSuperManager.class);
        sectionSuperManagerService.update(sectionSuperManager);
        return sectionSuperManager;

    }

}