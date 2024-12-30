package com.consilium.service;

import com.consilium.domain.Announce;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AnnounceService extends BaseService {

    public List<Announce> findAll() {
        String sql = "select * from announce";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Announce.class));
    }

    public List<Announce> findAllEfficient() {
        String sql = "select * from announce where  cast(startTime as date) <= getDate() and  (    endTime is null or cast(getDate() as date)  <= cast(endTime as date)         )          ";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Announce.class));
    }

    public Announce insert(Announce announce) {
        String sql = "insert into announce(subject,category,importance,startTime,endTime,creator,createTime)" +
                " values(" +
                ":subject,:category,:importance,:startTime,:endTime,:creator,:createTime)";
        announce.setCreator("creator");
        announce.setCreateTime(getTimeString());
        int rows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(announce));
        return announce;
    }

    public Announce update(Announce announce) {
        String sql = "update announce set subject=:subject,category=:category,importance=:importance,startTime=:startTime,endTime=:endTime,updater=:updater,updateTime=:updateTime" +
                "  where id=:id";
        announce.setUpdater("updater");
        announce.setUpdateTime(getTimeString());
        int rows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(announce));
        return announce;
    }

    public int delete(Announce announce) {
        String sql = "delete from announce where id=:id";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(announce));
    }

    private String getTimeString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
