package com.consilium.service;

import com.consilium.domain.QuestionAnswerBak;
import com.consilium.domain.QuestionAnswerRelation;
import com.consilium.domain.QuestionAnswerWindow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionAnswerRelationService extends BaseService {

    @Autowired
    QuestionAnswerBakService questionAnswerBakService;

    @Autowired
    QuestionAnswerWindowService questionAnswerWindowService;

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;


    public QuestionAnswerRelation insertBLL(String data) throws JsonProcessingException {
        TransactionStatus transactionStatus = null;
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readValue(data, JsonNode.class);
            QuestionAnswerRelation questionAnswerRelation = getQuestionAnswerRelation(data);
            QuestionAnswerWindow[] questionAnswerWindows = objectMapper.readValue(jsonNode.get("questionAnswerWindows").toString(), QuestionAnswerWindow[].class);
            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            questionAnswerRelation.setStatus("K");
            insert(questionAnswerRelation);
            for (QuestionAnswerWindow questionAnswerWindow : questionAnswerWindows) {
                questionAnswerWindow.setpId(questionAnswerRelation.getsId());
                questionAnswerWindowService.insert(questionAnswerWindow);
            }
            dataSourceTransactionManager.commit(transactionStatus);
            return questionAnswerRelation;
        } catch (Exception e) {
            if (transactionStatus != null) dataSourceTransactionManager.rollback(transactionStatus);
            throw e;
        }

    }


    public QuestionAnswerRelation updateBLL(String data) throws JsonProcessingException {
        TransactionStatus transactionStatus = null;
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readValue(data, JsonNode.class);
            QuestionAnswerRelation questionAnswerRelation = objectMapper.readValue(data, QuestionAnswerRelation.class);
            QuestionAnswerWindow[] questionAnswerWindows = objectMapper.readValue(jsonNode.get("questionAnswerWindows").toString(), QuestionAnswerWindow[].class);
            questionAnswerRelation.setIsEdit("Y");
            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            update(questionAnswerRelation);
            questionAnswerWindowService.deleteByPId(questionAnswerRelation.getsId());
            for (QuestionAnswerWindow questionAnswerWindow : questionAnswerWindows) {
                questionAnswerWindow.setpId(questionAnswerRelation.getsId());
                questionAnswerWindowService.insert(questionAnswerWindow);
            }
            dataSourceTransactionManager.commit(transactionStatus);
            return questionAnswerRelation;
        } catch (Exception e) {
            if (transactionStatus != null) dataSourceTransactionManager.rollback(transactionStatus);
            throw e;
        }

    }

    public int deleteBLL(int sId) throws JsonProcessingException {
        TransactionStatus transactionStatus = null;
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            QuestionAnswerRelation questionAnswerRelation = findBySId(sId);
            QuestionAnswerBak questionAnswerBak = objectMapper.readValue(objectMapper.writeValueAsString(questionAnswerRelation), QuestionAnswerBak.class);
            questionAnswerBak.setFlag("B");
            questionAnswerBakService.insert(questionAnswerBak);
            deleteBySId(sId);
            // questionAnswerWindowService.deleteByPId(sId);
            dataSourceTransactionManager.commit(transactionStatus);
            return sId;
        } catch (Exception e) {
            if (transactionStatus != null) dataSourceTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }


    public QuestionAnswerRelation on(String data) throws JsonProcessingException {
        QuestionAnswerRelation questionAnswerRelation = getQuestionAnswerRelation(data);
        QuestionAnswerRelation oriQuestionAnswerRelation = findBySId(questionAnswerRelation.getsId());
        oriQuestionAnswerRelation.setStatus("K");
        update(oriQuestionAnswerRelation);
        return oriQuestionAnswerRelation;
    }


    public QuestionAnswerRelation hit(Integer sId) throws JsonProcessingException {
       String sql = "update QuestionAnswer_Relation set hit = isnull(hit,0)+1 where sId=:sId";
        Map<String, Object> params = new HashMap<>();
        params.put("sId", sId);
        namedParameterJdbcTemplate.update(sql, params);
        return findBySId(sId);
    }

    public QuestionAnswerRelation off(String data) throws JsonProcessingException {
        QuestionAnswerRelation questionAnswerRelation = getQuestionAnswerRelation(data);
        QuestionAnswerRelation oriQuestionAnswerRelation = findBySId(questionAnswerRelation.getsId());
        oriQuestionAnswerRelation.setStatus("D");
        oriQuestionAnswerRelation.setCloseData(userInstance.getUserObject(httpServletRequest).getUserID());
        oriQuestionAnswerRelation.setCloseDataTime(getTimeString());
        update(oriQuestionAnswerRelation);
        return oriQuestionAnswerRelation;
    }


    private QuestionAnswerRelation getQuestionAnswerRelation(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, QuestionAnswerRelation.class);
    }


    public List<QuestionAnswerRelation> findByPid(int pId) {
        String sql = "select * from  QuestionAnswer_Relation where pId=:pId";
        Map<String, Object> params = new HashMap<>();
        params.put("pId", pId);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswerRelation.class));

    }


    public QuestionAnswerRelation findBySId(int sId) {
        String sql = "select * from  QuestionAnswer_Relation where sId=:sId";
        Map<String, Object> params = new HashMap<>();
        params.put("sId", sId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(QuestionAnswerRelation.class));
        } catch (Exception e) {
            return null;
        }

    }

    public List<QuestionAnswerRelation> findAll() {
        String sql = "select * from  QuestionAnswer_Relation";
        Map<String, Object> params = new HashMap<>();
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswerRelation.class));
    }

    public List<QuestionAnswerRelation> find(Integer id, String keyword) {
        String sql = "select * from  QuestionAnswer_Relation" +
                " where status ='K'" +
                " And (sid =:id or :id is null)";
        if (keyword != null) {
            sql += "  and (keyword like :keyword)";
        }
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", '%' + keyword + '%');
        params.put("id", id);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswerRelation.class));
    }


    public List<QuestionAnswerRelation> findByIdAndKeyword(Integer id, String keyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        String sql = "select * from (" +
                "select ROW_NUMBER() over (PARTITION BY qr.sid ORDER BY qr.sid ASC) as rownumber,qr.*" +
                ",qw.connector,qw.tel,qw.extension from QuestionAnswer_Relation qr left join QuestionAnswer_Window qw on qr.sId =qw.pId" +
                "    where qr.status <>'D'  and qr.unitid not in ('B4000','E3000','F9000')" +
                " And (qr.sid =:id or :id is null) ";
        String keyValue  = null;
        if (keyword != null) {
            String[] keywords = keyword.split(",");
            for(int i=0;i<keywords.length;i++){
                keyValue = String.format("keyword%d",i+1);
                params.put(keyValue, '%' + keywords[i] + '%');
                sql +=   String.format("  and (question like :%s or answer like :%s  or keyword like :%s)",keyValue,keyValue,keyValue);
            }
        }

        sql += ") a where a.rownumber = 1 order by hit desc";

        params.put("keyword", '%' + keyword + '%');

        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswerRelation.class));
    }


    public QuestionAnswerRelation insert(QuestionAnswerRelation questionAnswerRelation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into QuestionAnswer_Relation(pId,unitId,sectionId,question,answer,keyword,status,efficientTime,isPublic,creator,createTime) values(" +
                ":pId,:unitId,:sectionId,:question,:answer,:keyword,:status,:efficientTime,:isPublic,:creator,:createTime)";
        questionAnswerRelation.setStatus("K");
        questionAnswerRelation.setCreator(userInstance.getUserObject(httpServletRequest).getUserID());
        questionAnswerRelation.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(questionAnswerRelation), keyHolder);
        int key = keyHolder.getKey().intValue();
        questionAnswerRelation.setsId(key);
        return questionAnswerRelation;
    }

    public int update(QuestionAnswerRelation questionAnswerRelation) {
        String sql = "update QuestionAnswer_Relation  set unitId=:unitId,sectionId=:sectionId,question=:question,answer=:answer,keyword=:keyword,efficientTime=:efficientTime," +
                "status=:status,lastModifier=:lastModifier,lastUpdateTime=:lastUpdateTime,closeData=:closeData,closeDataTime=:closeDataTime,isPublic=:isPublic where sId=:sId";
        //Map<String, Object> params = new HashMap<>();
        questionAnswerRelation.setLastModifier(userInstance.getUserObject(httpServletRequest).getUserID());
        questionAnswerRelation.setIsEdit("Y");
        questionAnswerRelation.setLastUpdateTime(getTimeString());
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(questionAnswerRelation));
    }

    public Integer deleteBySId(Integer sId) {
        String sql = "delete from QuestionAnswer_Relation where sId=:sId";
        Map<String, Object> params = new HashMap<>();
        params.put("sId", sId);
        return namedParameterJdbcTemplate.update(sql, params);
    }


    public Integer deleteByPId(Integer pId) {
        String sql = "delete from QuestionAnswer_Relation where pId=:pId";
        Map<String, Object> params = new HashMap<>();
        params.put("pId", pId);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    private String getTimeString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
