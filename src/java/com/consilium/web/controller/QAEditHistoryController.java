package com.consilium.web.controller;


import com.consilium.domain.QAEditHistory;
import com.consilium.service.QAEditHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/QAEditHistory",produces = {MediaType.APPLICATION_JSON_VALUE})
public class QAEditHistoryController {

    @Autowired
    QAEditHistoryService qaEditHistoryService;


    @RequestMapping(value = "/find/{qSId}", method = RequestMethod.GET)
    @ResponseBody
    public List<QAEditHistory> findByUnitIdAndSectionId(@PathVariable("qSId") int qSId) {
        return qaEditHistoryService.findByQSId(qSId);
    }


}