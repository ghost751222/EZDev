package com.consilium.web.controller;


import com.consilium.domain.ViewUnitUserID;
import com.consilium.service.QuestionAnswerRelationService;
import com.consilium.service.QuestionAnswerWindowService;
import com.consilium.service.ViewUnitUserIDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/Test")
public class TestController {

    @Autowired
    QuestionAnswerRelationService questionAnswerRelationService;

    @Autowired
    QuestionAnswerWindowService questionAnswerWindowService;

    @Autowired
    ViewUnitUserIDService viewUnitUserIDService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ModelAndView page() {
        ModelAndView model = new ModelAndView();
        model.setViewName("test");
        List<ViewUnitUserID> a = viewUnitUserIDService.findAll();
        return model;
    }


}