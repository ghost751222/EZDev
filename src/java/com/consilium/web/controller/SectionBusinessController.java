package com.consilium.web.controller;


import com.consilium.domain.SectionBusniess;
import com.consilium.service.SectionBusinessService;
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
@RequestMapping(value = "/SectionBusiness",produces = {MediaType.APPLICATION_JSON_VALUE})
public class SectionBusinessController {

    protected static final Logger logger = Logger.getLogger(SectionBusinessController.class);

    @Autowired
    SectionBusinessService sectionBusinessService;


    @RequestMapping(value = "/page/{page}", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable("page") String page) {
        ModelAndView model = new ModelAndView();
        model.setViewName(page);
        return model;
    }

    @RequestMapping(value = {"/{pId}"}, method = RequestMethod.GET)
    @ResponseBody
    public List<SectionBusniess> get(@PathVariable("pId") int pId) throws JsonProcessingException {
        try {
            return sectionBusinessService.findByPId(pId);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }

    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public List<SectionBusniess> getAll() throws JsonProcessingException {
        try {
            return sectionBusinessService.findAll();
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> find(@RequestParam("unitId") String unitId, @RequestParam("sectionId") String sectionId,@RequestParam("keyword") String keyword ) {
        if(unitId.equals("null") ) unitId =null;
        if(sectionId.equals("null") ) sectionId =null;
        if(keyword.equals("null") ) keyword =null;
        return sectionBusinessService.findByUnitIdAndSectionId(  unitId,  sectionId,keyword);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public SectionBusniess insert(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SectionBusniess sectionBusniess = objectMapper.readValue(data, SectionBusniess.class);
        sectionBusinessService.insert(objectMapper.readValue(data, SectionBusniess.class));
        return sectionBusniess;

    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public SectionBusniess update(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SectionBusniess sectionBusniess = objectMapper.readValue(data, SectionBusniess.class);
        sectionBusinessService.update(sectionBusniess);
        return sectionBusniess;

    }


    @RequestMapping(value = "/{sId}", method = RequestMethod.DELETE)
    @ResponseBody
    public int delete(@PathVariable("sId") Integer sId) throws JsonProcessingException {
        return sectionBusinessService.deleteById(sId);

    }

}