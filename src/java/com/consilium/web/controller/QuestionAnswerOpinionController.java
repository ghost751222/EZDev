package com.consilium.web.controller;


import com.consilium.domain.QuestionAnswerOption;
import com.consilium.service.QuestionAnswerOpinionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/QuestionAnswerOpinion",produces = {MediaType.APPLICATION_JSON_VALUE})
public class QuestionAnswerOpinionController {

    @Autowired
    QuestionAnswerOpinionService questionAnswerOpinionService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ModelAndView page() {
        ModelAndView model = new ModelAndView();
        model.setViewName("questionAnswer");
        return model;
    }


    @RequestMapping(value = "/find/{pId}", method = RequestMethod.GET)
    @ResponseBody
    public List<QuestionAnswerOption> findByPId(@PathVariable("pId") int pId) {
      return  questionAnswerOpinionService.findByPId(pId);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public QuestionAnswerOption insert(@RequestBody String data) throws JsonProcessingException {
        return questionAnswerOpinionService.insertBLL(data);

    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public QuestionAnswerOption update(@RequestBody String data) throws JsonProcessingException {
        return questionAnswerOpinionService.updateBLL(data);

    }
}