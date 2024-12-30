package com.consilium.web.controller;


import com.consilium.domain.QAReason;
import com.consilium.domain.QuestionAnswerRelation;
import com.consilium.service.QAReasonService;
import com.consilium.service.QuestionAnswerRelationService;
import com.consilium.service.QuestionAnswerWindowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/QuestionAnswerRelation", produces = {MediaType.APPLICATION_JSON_VALUE})
public class QuestionAnswerRelationController {

    @Autowired
    QuestionAnswerRelationService questionAnswerRelationService;

    @Autowired
    QAReasonService qaReasonService;

    @Autowired
    QuestionAnswerWindowService questionAnswerWindowService;

    private ArrayNode generateData(List<QuestionAnswerRelation> questionAnswerRelations) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.valueToTree(questionAnswerRelations);

        for (JsonNode jsonNode : arrayNode) {
            int pId = jsonNode.get("sId").asInt();
            ObjectNode objectNode = ((ObjectNode) jsonNode);
            objectNode.putPOJO("questionAnswerWindows", questionAnswerWindowService.findByPId(pId));
            objectNode.putPOJO("qaReasons", qaReasonService.findByQSId(pId));
        }
        return arrayNode;
    }


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public List<QuestionAnswerRelation> findAll() {
        return questionAnswerRelationService.findAll();
    }

    @RequestMapping(value = "/find2", method = RequestMethod.GET)
    @ResponseBody
    public ArrayNode find2(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "keyword", required = false) String keyword) {
        List<QuestionAnswerRelation> questionAnswerRelations = questionAnswerRelationService.find(id, keyword);
        ArrayNode arrayNode = generateData(questionAnswerRelations);
        return arrayNode;
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public List<QuestionAnswerRelation> find(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "keyword", required = false) String keyword) {
        List<QuestionAnswerRelation> questionAnswerRelations = questionAnswerRelationService.findByIdAndKeyword(id, keyword);
        return questionAnswerRelations;
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public QuestionAnswerRelation insert(@RequestBody String data) throws JsonProcessingException {
        return questionAnswerRelationService.insertBLL(data);

    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public QuestionAnswerRelation update(@RequestBody String data) throws JsonProcessingException {
        return questionAnswerRelationService.updateBLL(data);

    }

    @RequestMapping(value = "/{sId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Integer delete(@PathVariable("sId") Integer sId) throws JsonProcessingException {
        return questionAnswerRelationService.deleteBLL(sId);
    }


    @RequestMapping(value = "/on", method = RequestMethod.PUT)
    @ResponseBody
    public QuestionAnswerRelation applyOn(@RequestBody String data) throws JsonProcessingException {
        return questionAnswerRelationService.on(data);

    }


    @RequestMapping(value = "/off", method = RequestMethod.PUT)
    @ResponseBody
    public QuestionAnswerRelation off(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        QAReason qaReason = objectMapper.readValue(data, QAReason.class);
        qaReason.setQsId(qaReason.getsId());
        qaReason.setsId(null);
        qaReason.setType("B");
        qaReasonService.insert(qaReason);

        return questionAnswerRelationService.off(data);

    }


    @RequestMapping(value = "/hit/{sId}", method = RequestMethod.PUT)
    @ResponseBody
    public QuestionAnswerRelation hit(@PathVariable("sId") Integer sId) throws JsonProcessingException {
        return questionAnswerRelationService.hit(sId);

    }

}