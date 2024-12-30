package com.consilium.service;

import com.consilium.domain.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseService {


    public List<User> findAll() {
        String sql = "select * from users";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public int Test(final String No) {
        String sql = "select * from dbo.Test";
        @SuppressWarnings("rawtypes")
        List result = jdbcTemplate.queryForList(sql);
        return result.size();
    }
}