package com.consilium.service;

import com.consilium.domain.QuestionAnswerOption;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionAnswerOpinionService extends BaseService {


    public QuestionAnswerOption insertBLL(String data) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        QuestionAnswerOption questionAnswerOption = objectMapper.readValue(data, QuestionAnswerOption.class);
        //if (update(questionAnswerOption) == 0)
        insert(questionAnswerOption);
        return questionAnswerOption;
    }


    public QuestionAnswerOption updateBLL(String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        QuestionAnswerOption questionAnswerOption = objectMapper.readValue(data, QuestionAnswerOption.class);
        update(questionAnswerOption);
        return questionAnswerOption;
    }

    public QuestionAnswerOption insert(QuestionAnswerOption questionAnswerOption) {

        String sql = "insert into QuestionAnswer_Opinion(pId,content,creator,createTime,visible) values(:pId,:content,:creator,:createTime,:visible)";
        questionAnswerOption.setCreator("creator");
        questionAnswerOption.setCreateTime(getTimeString());
        questionAnswerOption.setVisible("Y");
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(questionAnswerOption));
        return questionAnswerOption;
    }

    public int update(QuestionAnswerOption questionAnswerOption) throws JsonProcessingException {

        String sql = "update QuestionAnswer_Opinion  set content=:content,visible=:visible where sId=:sId";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(questionAnswerOption));

    }

    private String getTimeString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    public List<QuestionAnswerOption> findByPId(int pId) {
        String sql = "select * from QuestionAnswer_Opinion where pId=:pId";
        Map<String, Object> map = new HashMap<>();
        map.put("pId", pId);
        return namedParameterJdbcTemplate.query(sql, map, new BeanPropertyRowMapper<>(QuestionAnswerOption.class));
    }
}
