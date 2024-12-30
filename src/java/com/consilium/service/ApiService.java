package com.consilium.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ApiService {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public List<QAItem> findQaAllView() {
//        String sql = "select ROW_NUMBER() OVER (ORDER  by qa.sId) as ROWNUM,  u.unitName as UNITNAME ,u2.unitName as DEPTNAME,qa.question as QUESTION,qa.answer as ANSWER,qa.sId as SID from question_answer qa " +
//                "  inner join Units u on u.unitCode = qa.unitId" +
//                "  inner join Units u2 on u2.unitCode = qa.sectionId " +
//                " where isPublic = 'Y'  and  status <>'D' " +
//                " and (efficientTime >= cast(getDate() as date) or efficientTime is null) " +
//                " order by sId";

        String sql = "select ROW_NUMBER() OVER (  ORDER BY isnull((SELECT MAX(HIT) FROM QUESTIONANSWER_RELATION R WHERE R.PID = qa.SID), 0) DESC ) as ROWNUM, " +
                " u.unitName as UNITNAME ,u2.sname as DEPTNAME,qa.question as QUESTION,qa.answer as ANSWER,qa.sId as SID, " +
                " isnull((SELECT MAX(HIT) FROM QUESTIONANSWER_RELATION R WHERE R.PID = qa.SID), 0) HIT " +
                " from question_answer qa  " +
                "                  inner join Units u on u.unitCode = qa.unitId " +
                "                  inner join Units u2 on u2.unitCode = qa.sectionId  " +
                "                 where isPublic = 'Y'  and  status <>'D'  " +
                "                 ORDER BY HIT DESC, QA.SID";

        Map<String, Object> params = new HashMap<>();
        List<QAItem> qaitems = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QAItem.class));
        return qaitems;
    }
}
