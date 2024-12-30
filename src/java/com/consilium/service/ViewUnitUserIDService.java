package com.consilium.service;

import com.consilium.domain.ViewUnitUserID;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ViewUnitUserIDService extends BaseService {

    public List<ViewUnitUserID> findAll() {
        String sql = "select * from View_Unit_UserId order by userId ";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ViewUnitUserID.class));
    }

    public List<ViewUnitUserID> findByAccount(String account) {
        String sql = "select * from View_Unit_UserId where account =:account or userId=:account ";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(ViewUnitUserID.class));
    }


}
