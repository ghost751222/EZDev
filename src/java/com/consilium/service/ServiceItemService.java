package com.consilium.service;

import com.consilium.domain.ServiceItem;
import com.consilium.domain.ServiceType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceItemService extends BaseService {

    public List<ServiceItem> findAll() {
        String sql = "select * from SERVICE_ITEM where itemId not in (Select codeCode from app_code where category='R10') order by showIndex";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ServiceItem.class));
    }


    public List<ServiceItem> findAllByState(int state) {
        String sql = "select * from SERVICE_ITEM where state = :state and itemId not in (Select codeCode from app_code where category='R10') order by showIndex";
        Map<String, Object> params = new HashMap<>();
        params.put("state", state);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(ServiceItem.class));
    }


}
