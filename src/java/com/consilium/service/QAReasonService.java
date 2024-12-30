package com.consilium.service;

import com.consilium.domain.QAReason;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QAReasonService extends BaseService {

    public List<QAReason> findByQSId(int qSId) {
        String sql = "select qa.* from  qa_reason qa where qSId =:qSId order by createTime desc";
        Map<String, Object> params = new HashMap<>();
        params.put("qSId", qSId);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QAReason.class));
    }


    public QAReason insert(QAReason qaReason) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into QA_REASON(qSId,reason,createTime,creator,lastModifier,lastUpdateTime,Type) values(" +
                ":qsId,:reason,:createTime,:creator,:lastModifier,:lastUpdateTime,:type)";

        qaReason.setCreator("creator");
        qaReason.setCreateTime(getTimeString());
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(qaReason), keyHolder);
        int key = keyHolder.getKey().intValue();
        qaReason.setsId(key);
        return qaReason;
    }

    private String getTimeString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
