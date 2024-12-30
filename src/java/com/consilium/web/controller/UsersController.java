package com.consilium.web.controller;


import com.consilium.domain.ViewUnitUserID;
import com.consilium.service.ViewUnitUserIDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/Users", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UsersController {

    @Autowired
    ViewUnitUserIDService viewUnitUserIDService;


    @RequestMapping(value = "/{account}", method = RequestMethod.GET)
    @ResponseBody
    public List<ViewUnitUserID> findByAccount(@PathVariable("account") String account) {

        return viewUnitUserIDService.findByAccount(account);
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<ViewUnitUserID> findAll() {

        return viewUnitUserIDService.findAll();
    }

}