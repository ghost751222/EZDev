package com.consilium.service;

import com.consilium.domain.Units;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitsService extends BaseService {

    public List<Units> findAll() {
        String sql = "select * from Units order by SuperUnit";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Units.class));
    }


    public Units insert(Units units) {
        String sql = "insert into Units(SuperUnit,UnitCode,UnitType,UnitName,CityCode,UnitContact," +
                "                UnitTel,UnitFax,UnitAddress,policeOfficeLevel,sname,orderNumber,spUnitCode,spServiceType) values(" +
                ":SuperUnit,:UnitCode,:UnitType,:UnitName,:CityCode,:UnitContact," +
                ":UnitTel,:UnitFax,:UnitAddress,:policeOfficeLevel,:sname,:orderNumber,:spUnitCode,:spServiceType)";
        int rows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(units));
        return units;
    }

    public Units update(Units units) {
        String sql = "update Units set SuperUnit=:SuperUnit,CityCode=:CityCode,UnitContact=:UnitContact,UnitTel=:UnitTel,UnitFax=:UnitFax,UnitAddress=:UnitAddress," +
                "policeOfficeLevel=:policeOfficeLevel," +
                "sname=:sname,orderNumber=:orderNumber,spUnitCode=:spUnitCode,spServiceType=:spServiceType" +
                " where UnitCode=:UnitCode and UnitType=:UnitType";
        int rows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(units));
        return units;
    }

    public int delete(Units units) {
        String sql = "delete from units where UnitCode=:UnitCode and UnitType=:UnitType";

        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(units));
    }

}
