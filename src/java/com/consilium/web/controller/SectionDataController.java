package com.consilium.web.controller;


import com.consilium.domain.SectionData;
import com.consilium.service.SectionDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/SectionData",produces = {MediaType.APPLICATION_JSON_VALUE})
public class SectionDataController {

    private static final Logger logger = Logger.getLogger(SectionBusinessController.class);

    @Autowired
    SectionDataService sectionDataService;


    @RequestMapping(value = "/page/{page}", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable("page") String page) {
        ModelAndView model = new ModelAndView();
        model.setViewName(page);
        return model;
    }



    @RequestMapping(value = "/{sId}", method = RequestMethod.GET)
    @ResponseBody
    public List<SectionData> get(@PathVariable("sId") int sId) throws JsonProcessingException {
        return sectionDataService.findBySid(sId);
    }


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public List<SectionData> findAll() {
        return sectionDataService.findAll();
    }


    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> find(@RequestParam("unitId") String unitId, @RequestParam("sectionId") String sectionId ) throws JsonProcessingException {
            if(unitId.equals("null") ) unitId =null;
            if(sectionId.equals("null") ) sectionId =null;
        return sectionDataService.findByUnitIdAndSectionId(  unitId,  sectionId);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public SectionData insert(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SectionData sectionData = objectMapper.readValue(data, SectionData.class);
        return  sectionDataService.insert(sectionData);

    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public SectionData update(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SectionData sectionData = objectMapper.readValue(data, SectionData.class);
        sectionDataService.update(sectionData);
        return sectionData;

    }

}