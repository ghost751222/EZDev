package com.consilium.service;

import com.consilium.domain.QAEditHistory;
import com.consilium.domain.Units;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QAEditHistoryService extends BaseService {

    public List<QAEditHistory> findAll() {
        String sql = "select * from QA_Edit_History";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QAEditHistory.class));
    }


    public QAEditHistory insert(QAEditHistory qaEditHistory) {
        String sql = "insert into QA_Edit_History(qsId,question,answer,keyword,lastUpdateTime,lastModifier,editDate,source)" +
                    "values(:qsId,:question,:answer,:keyword,:lastUpdateTime,:lastModifier,:editDate,:source)";
        int rows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(qaEditHistory));
        return qaEditHistory;
    }



    public List<QAEditHistory> findByQSId(int qSId) {
        String sql = "select * from QA_Edit_History where qSId=:qSId order by EditDate desc";
        Map<String,Object> params = new HashMap<>();
        params.put("qSId",qSId);
        return namedParameterJdbcTemplate.query(sql, params,new BeanPropertyRowMapper<>(QAEditHistory.class));
    }

}
