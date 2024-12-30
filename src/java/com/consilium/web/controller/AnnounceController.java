package com.consilium.web.controller;


import com.consilium.domain.Announce;
import com.consilium.domain.Announce;
import com.consilium.service.AnnounceService;
import com.consilium.service.AnnounceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/Announce",produces = {MediaType.APPLICATION_JSON_VALUE})
public class AnnounceController {

    @Autowired
    AnnounceService announceService;


    @RequestMapping(value = "/{page}" , method = RequestMethod.GET)
    public ModelAndView page(@PathVariable("page") String page) {
        ModelAndView model = new ModelAndView();
        model.setViewName(page);
        return model;
    }


    @RequestMapping(value = "/findAll" , method = RequestMethod.GET)
    @ResponseBody
    public List<Announce> findAll() {
        return announceService.findAll();
    }


    @RequestMapping(value = "/findAllEfficient" , method = RequestMethod.GET)
    @ResponseBody
    public List<Announce> findAllEfficient() {
        return announceService.findAllEfficient();
    }

    @RequestMapping(value = "/" , method = RequestMethod.POST)
    @ResponseBody
    public Announce insert(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Announce announce = objectMapper.readValue(data, Announce.class);
        announceService.insert(announce);
        return announce;

    }

    @RequestMapping(value = "/" , method = RequestMethod.PUT)
    @ResponseBody
    public Announce update(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Announce announce = objectMapper.readValue(data, Announce.class);
        announceService.update(announce);
        return announce;

    }

    @RequestMapping(value = "/" , method = RequestMethod.DELETE)
    @ResponseBody
    public Announce delete(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Announce announce = objectMapper.readValue(data, Announce.class);
        announceService.delete(announce);
        return announce;

    }

}