package com.consilium.service;

import com.consilium.domain.ServiceType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceTypeService extends BaseService {

    public List<ServiceType> findAll() {
        String sql = "select * from SERVICE_TYPE where typeId not in (Select codeCode from APP_CODE where category='R09') order by showIndex";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ServiceType.class));
    }


    public List<ServiceType> findAllByState(int state) {
        String sql = "select * from SERVICE_TYPE where state = :state and typeId not in (Select codeCode from APP_CODE where category='R09') order by showIndex";
        Map<String, Object> params = new HashMap<>();
        params.put("state", state);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(ServiceType.class));
    }


}
