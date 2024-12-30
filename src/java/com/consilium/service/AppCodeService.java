package com.consilium.service;

import com.consilium.domain.AppCode;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppCodeService extends BaseService {


    public List<AppCode> findAllByCategory(String category) {
        String sql = "select * from APP_CODE where category=:category";
        Map<String, Object> map = new HashMap<>();
        map.put("category", category);
        return namedParameterJdbcTemplate.query(sql, map, new BeanPropertyRowMapper<>(AppCode.class));
    }

    public List<AppCode> findAllByCategoryAndCodeCode(String category,String codeCode) {
        String sql = "select * from APP_CODE where category=:category and codeCode =:codeCode";
        Map<String, Object> map = new HashMap<>();
        map.put("category", category);
        map.put("codeCode", codeCode);
        return namedParameterJdbcTemplate.query(sql, map, new BeanPropertyRowMapper<>(AppCode.class));
    }

}
