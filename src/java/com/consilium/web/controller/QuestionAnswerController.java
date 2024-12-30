package com.consilium.web.controller;


import com.consilium.domain.QAReason;
import com.consilium.domain.QuestionAnswer;
import com.consilium.domain.QuestionAnswerRelation;
import com.consilium.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/QuestionAnswer", produces = {MediaType.APPLICATION_JSON_VALUE})
public class QuestionAnswerController {

    @Autowired
    QuestionAnswerService questionAnswerService;

    @Autowired
    QuestionAnswerRelationService questionAnswerRelationService;

    @Autowired
    QuestionAnswerWindowService questionAnswerWindowService;

    @Autowired
    QuestionAnswerOpinionService questionAnswerOpinionService;

    @Autowired
    QuestionAnswerAttachmentService questionAnswerAttachmentService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    QAReasonService qaReasonService;

    private ArrayNode generateData(List<QuestionAnswer> questionAnswers) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.valueToTree(questionAnswers);
        List<QuestionAnswerRelation> questionAnswerRelations;
        for (JsonNode jsonNode : arrayNode) {
            int pId = jsonNode.get("sId").asInt();

            ObjectNode objectNode = ((ObjectNode) jsonNode);
            questionAnswerRelations = questionAnswerRelationService.findByPid(pId);
            objectNode.set("questionAnswerRelations", objectMapper.valueToTree(questionAnswerRelations));
            objectNode.putPOJO("questionAnswerWindows", questionAnswerWindowService.findByPId(pId));
            objectNode.putPOJO("questionAnswerAttaches", questionAnswerAttachmentService.findByPId(pId));
            objectNode.putPOJO("qaReasons", qaReasonService.findByQSId(pId));
            ArrayNode questionAnswerRelationsArrayNode = (ArrayNode) jsonNode.get("questionAnswerRelations");
            for (JsonNode questionAnswerRelationNode : questionAnswerRelationsArrayNode) {

                int qSId = questionAnswerRelationNode.get("sId").asInt();
                ((ObjectNode) questionAnswerRelationNode).putPOJO("questionAnswerOpinions", questionAnswerOpinionService.findByPId(qSId));
                ((ObjectNode) questionAnswerRelationNode).putPOJO("questionAnswerWindows", questionAnswerWindowService.findByPId(qSId));
                ((ObjectNode) questionAnswerRelationNode).putPOJO("questionAnswerAttaches", questionAnswerAttachmentService.findByPId(qSId));

            }


        }
        return arrayNode;
    }

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable("page") String page) {
        ModelAndView model = new ModelAndView();
        model.setViewName(page);
        return model;
    }


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public ArrayNode findAll() {
        return generateData(questionAnswerService.findAll());
    }


    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public ArrayNode findByUnitIdAndSectionId(@RequestParam("unitId") String unitId, @RequestParam("sectionId") String sectionId) {
        return generateData(questionAnswerService.findByUnitIdAndSectionId(unitId, sectionId));
    }

    @RequestMapping(value = "/findOpinionVerify", method = RequestMethod.GET)
    @ResponseBody
    public ArrayNode findOpinionVerifyByUnitIdAndSectionId(@RequestParam("unitId") String unitId, @RequestParam("sectionId") String sectionId, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        return generateData(questionAnswerService.findOpinionVerifyByUnitIdAndSectionId(unitId, sectionId, startTime, endTime));
    }


    @RequestMapping(value = "/findVerify", method = RequestMethod.GET)
    @ResponseBody
    public ArrayNode findVerify(@RequestParam("unitId") String unitId, @RequestParam("sectionId") String sectionId, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        return generateData(questionAnswerService.findVerify(unitId, sectionId, startTime, endTime));
    }


    @RequestMapping(value = "/findOverTime", method = RequestMethod.GET)
    @ResponseBody
    public ArrayNode findOverTime(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        return generateData(questionAnswerService.findOverTime(startTime, endTime));
    }

    @RequestMapping(value = "/findDeleteList", method = RequestMethod.GET)
    @ResponseBody
    public ArrayNode findDeleteList(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam(value = "unitId", required = false) String unitId, @RequestParam(value = "sectionId", required = false) String sectionId) {
        return generateData(questionAnswerService.findDeleteList(startTime, endTime, unitId, sectionId));
    }

    @RequestMapping(value = "/findExpiration", method = RequestMethod.GET)
    @ResponseBody
    public ArrayNode findExpiration(@RequestParam(value = "intervalDay", required = false) Integer intervalDay, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        return generateData(questionAnswerService.findExpiration(intervalDay, startTime, endTime));
    }

    @RequestMapping(value = "/findEdit", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode findEdit(@RequestParam Map<String, String> params) {
        //@RequestParam(value = "unitId",required = false) String unitId, @RequestParam(value ="sectionId",required = false) String sectionId, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
        String unitId = params.get("unitId");
        String sectionId = params.get("sectionId");
        String startTime = params.get("startTime");
        String endTime = params.get("endTime");
        int from = Integer.parseInt(params.get("from"));
        int to = Integer.parseInt(params.get("to"));

        List<QuestionAnswer> questionAnswers = questionAnswerService.findEdit(unitId, sectionId, startTime, endTime);
        ArrayNode arrayNode = null;
        if(questionAnswers.size() <= to)
            arrayNode = generateData(questionAnswers.subList(from, questionAnswers.size()));
        else
            arrayNode = generateData(questionAnswers.subList(from, to));



        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("total",questionAnswers.size());
        objectNode.putPOJO("data",arrayNode);
        return objectNode;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public QuestionAnswer insert(@RequestBody String data) throws JsonProcessingException {
        return questionAnswerService.insertBLL(data);

    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public QuestionAnswer update(@RequestBody String data) throws JsonProcessingException {
        return questionAnswerService.updateBLL(data);
    }

    @RequestMapping(value = "/{sId}", method = RequestMethod.DELETE)
    @ResponseBody
    public int delete(@PathVariable("sId") int sId) throws JsonProcessingException {
        questionAnswerService.delBLL(sId);
        return sId;
    }


    @RequestMapping(value = "/off", method = RequestMethod.POST)
    @ResponseBody
    public QuestionAnswer off(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        QuestionAnswer questionAnswer = objectMapper.readValue(data, QuestionAnswer.class);
        QAReason qaReason = objectMapper.readValue(data, QAReason.class);
        qaReason.setsId(null);
        qaReason.setQsId(questionAnswer.getsId());
        qaReason.setType("A");
        List<QuestionAnswerRelation> questionAnswerRelations = questionAnswerRelationService.findByPid(questionAnswer.getsId());
        qaReasonService.insert(qaReason);
        questionAnswerService.off(questionAnswer);
        for (QuestionAnswerRelation q : questionAnswerRelations) {
            questionAnswerRelationService.off(objectMapper.writeValueAsString(q));
        }
        return questionAnswer;

    }

    @RequestMapping(value = "/applyOff", method = RequestMethod.POST)
    @ResponseBody
    public QuestionAnswer applyOff(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        QuestionAnswer questionAnswer = objectMapper.readValue(data, QuestionAnswer.class);
        return questionAnswerService.applyOff(questionAnswer);

    }

    @RequestMapping(value = "/cancelOff", method = RequestMethod.POST)
    @ResponseBody
    public QuestionAnswer cancelOff(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        QuestionAnswer questionAnswer = objectMapper.readValue(data, QuestionAnswer.class);
        return questionAnswerService.cancelOff(questionAnswer);

    }
}