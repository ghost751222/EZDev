package com.consilium.service;

import com.consilium.domain.QuestionAnswerAttachment;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionAnswerAttachmentService extends BaseService {


    public List<QuestionAnswerAttachment> findBySId(int sId) {
        String sql = "select * from  QuestionAnswer_Attachment where sId=:sId";
        Map<String, Object> params = new HashMap<>();
        params.put("sId", sId);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswerAttachment.class));
    }

    public List<QuestionAnswerAttachment> findByPId(int pId) {
        String sql = "select * from  QuestionAnswer_Attachment where pId=:pId";
        Map<String, Object> params = new HashMap<>();
        params.put("pId", pId);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswerAttachment.class));
    }


    public List<QuestionAnswerAttachment> findAll() {
        String sql = "select * from  QuestionAnswer_Attachment";
        Map<String, Object> params = new HashMap<>();
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswerAttachment.class));
    }


    public QuestionAnswerAttachment insert(QuestionAnswerAttachment questionAnswerAttachment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into QuestionAnswer_Attachment(pId,fileName,fileSize,fileContent,fileType) values(" +
                ":pId,:fileName,:fileSize,:fileContent,:fileType)";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(questionAnswerAttachment), keyHolder);
        int key = keyHolder.getKey().intValue();
        questionAnswerAttachment.setsId(key);
        return questionAnswerAttachment;
    }

    public int deleteBySId(int sId) throws JsonProcessingException {
        String sql = "delete from  QuestionAnswer_Attachment where sId=:sId";
        Map<String, Object> params = new HashMap<>();
        params.put("sId", sId);
        int rows = namedParameterJdbcTemplate.update(sql, params);
        return rows;
    }

}
