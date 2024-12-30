package com.consilium.web.controller;


import com.consilium.domain.QuestionAnswerAttachment;
import com.consilium.service.QuestionAnswerAttachmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping(value = "/QuestionAnswerAttachment", produces = {MediaType.APPLICATION_JSON_VALUE})
public class QuestionAnswerAttachmentController {

    @Autowired
    QuestionAnswerAttachmentService questionAnswerAttachmentService;


    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public QuestionAnswerAttachment insert(@RequestBody String data) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            QuestionAnswerAttachment questionAnswerAttachment = objectMapper.readValue(data, QuestionAnswerAttachment.class);
            return questionAnswerAttachmentService.insert(questionAnswerAttachment);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    @RequestMapping(value = "/{sId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> insert(@PathVariable("sId") int sId) throws JsonProcessingException {
        try {
            List<QuestionAnswerAttachment> questionAnswerAttachmentList = questionAnswerAttachmentService.findBySId(sId);
            QuestionAnswerAttachment questionAnswerAttachment = questionAnswerAttachmentList.get(0);
            String fileName = questionAnswerAttachment.getFileName();
            String fileContent = questionAnswerAttachment.getFileContent();
            byte[] decodedBytes = Base64.getDecoder().decode(fileContent);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)))
                    .body(decodedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }


    @RequestMapping(value = "/{sId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Integer delete(@PathVariable("sId") int sId) throws JsonProcessingException {
       return questionAnswerAttachmentService.deleteBySId(sId);

    }

}
