package com.consilium.service;

import com.consilium.domain.QuestionAnswerWindow;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionAnswerWindowService extends BaseService {


    public List<QuestionAnswerWindow> findByPId(int pId) {
        String sql = " select * from QuestionAnswer_Window where pId=:pId";
        Map<String, Object> map = new HashMap<>();
        map.put("pId", pId);
        return namedParameterJdbcTemplate.query(sql, map, new BeanPropertyRowMapper<>(QuestionAnswerWindow.class));
    }


    public List<QuestionAnswerWindow> findAll() {
        String sql = " select * from QuestionAnswer_Window";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuestionAnswerWindow.class));
    }


    public int insert(QuestionAnswerWindow questionAnswerWindow) {
        String sql = "insert into QuestionAnswer_Window(pId,connector,tel,extension) values(:pId,:connector,:tel,:extension)";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(questionAnswerWindow));
    }

    public int update(QuestionAnswerWindow questionAnswerWindow) {
        String sql = "update  QuestionAnswer_Window set connector=:connector,tel=:tel,extension=:extension where pId=:pId";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(questionAnswerWindow));
    }

    public int deleteByPId(int pId) {
        String sql = "delete from  QuestionAnswer_Window where pId=:pId";
        Map<String, Object> map = new HashMap<>();
        map.put("pId", pId);
        return namedParameterJdbcTemplate.update(sql, map);
    }

}
