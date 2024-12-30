package com.consilium.service;

import com.consilium.domain.QAEditHistory;
import com.consilium.domain.QuestionAnswer;
import com.consilium.domain.QuestionAnswerBak;
import com.consilium.domain.QuestionAnswerWindow;
import com.consilium.excel.models.UserInstance;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionAnswerService extends BaseService {

    @Autowired
    QuestionAnswerBakService questionAnswerBakService;

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    QuestionAnswerWindowService questionAnswerWindowService;

    @Autowired
    QAEditHistoryService qaEditHistoryService;

    @Autowired
    QuestionAnswerRelationService questionAnswerRelationService;




    public QuestionAnswer insertBLL(String data) throws JsonProcessingException {
        TransactionStatus transactionStatus = null;
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        try {

            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readValue(data, JsonNode.class);

            QuestionAnswer questionAnswer = objectMapper.readValue(data, QuestionAnswer.class);
            questionAnswer.setStatus("A");
            QuestionAnswerWindow[] questionAnswerWindows = objectMapper.readValue(jsonNode.get("questionAnswerWindows").toString(), QuestionAnswerWindow[].class);
            insert(questionAnswer);
            for (QuestionAnswerWindow questionAnswerWindow : questionAnswerWindows) {
                questionAnswerWindow.setpId(questionAnswer.getsId());
                questionAnswerWindowService.insert(questionAnswerWindow);
            }
            dataSourceTransactionManager.commit(transactionStatus);

            return questionAnswer;
        } catch (Exception e) {
            if (transactionStatus != null) dataSourceTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }


    public QuestionAnswer updateBLL(String data) throws JsonProcessingException {
        TransactionStatus transactionStatus = null;
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        try {

            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readValue(data, JsonNode.class);
            QuestionAnswer questionAnswer = objectMapper.readValue(data, QuestionAnswer.class);
            QuestionAnswerWindow[] questionAnswerWindows = objectMapper.readValue(jsonNode.get("questionAnswerWindows").toString(), QuestionAnswerWindow[].class);

            QuestionAnswer lastQuestionAnswer = findBySId(questionAnswer.getsId());
            qaEditHistoryService.insert(createQAEditHistory(lastQuestionAnswer));
            update(questionAnswer);
            questionAnswerWindowService.deleteByPId(questionAnswer.getsId());
            for (QuestionAnswerWindow questionAnswerWindow : questionAnswerWindows) {
                questionAnswerWindow.setpId(questionAnswer.getsId());
                questionAnswerWindowService.insert(questionAnswerWindow);
            }


            //  questionAnswerBakService.insert(questionAnswerBak);
            dataSourceTransactionManager.commit(transactionStatus);

            return questionAnswer;
        } catch (Exception e) {
            if (transactionStatus != null) dataSourceTransactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    public int delBLL(int sId) throws JsonProcessingException {
        TransactionStatus transactionStatus = null;
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            QuestionAnswer questionAnswer = findBySId(sId);


            QuestionAnswerBak questionAnswerBak = objectMapper.readValue(objectMapper.writeValueAsString(questionAnswer), QuestionAnswerBak.class);
            questionAnswerBak.setFlag("A");
            questionAnswerBakService.insert(questionAnswerBak);
            deleteBySId(sId);
            // questionAnswerRelationService.deleteByPId(sId);
            //questionAnswerWindowService.deleteByPId(sId);

            dataSourceTransactionManager.commit(transactionStatus);
            return 0;
        } catch (Exception e) {
            if (transactionStatus != null) dataSourceTransactionManager.rollback(transactionStatus);
            throw e;
        }

    }


    public QuestionAnswer findBySId(int sId) {
        String sql = "select * from  Question_Answer where sId=:sId";
        Map<String, Object> params = new HashMap<>();
        params.put("sId", sId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(QuestionAnswer.class));
        } catch (Exception e) {
            return null;
        }
    }

    public List<QuestionAnswer> findAll() {
        String sql = "select * from  Question_Answer";
        Map<String, Object> params = new HashMap<>();
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswer.class));
    }

    public List<QuestionAnswer> findByUnitIdAndSectionId(String unitId, String sectionId) {
        String sql = "select * from  Question_Answer " +
                " where status in('K','A')" +
                " And unitId=:unitId and sectionId=:sectionId order by orderTime desc";
        Map<String, Object> params = new HashMap<>();
        params.put("unitId", unitId);
        params.put("sectionId", sectionId);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswer.class));
    }


    public List<QuestionAnswer> findOpinionVerifyByUnitIdAndSectionId(String unitId, String sectionId, String startTime, String endTime) {
        String sql = "select distinct qa.* from  Question_Answer qa " +
                "  inner join QuestionAnswer_Relation qar on qa.sId = qar.pId " +
                "  inner join QuestionAnswer_Opinion qap on qap.pId = qar.sId" +
                " where 1=1" +
                //"       qa.unitId=:unitId and qa.sectionId=:sectionId" +
                "       and cast(qap.createTime as date) between cast(:startTime as date) and cast(:endTime as date) order by orderTime desc";
        Map<String, Object> params = new HashMap<>();
        params.put("unitId", unitId);
        params.put("sectionId", sectionId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswer.class));
    }


    public List<QuestionAnswer> findVerify(String unitId, String sectionId, String startTime, String endTime) {
        String sql = "select qa.* from  Question_Answer qa " +

                " where 1=1 " +
                "  and qa.status ='S' " +
                //"       qa.unitId=:unitId and qa.sectionId=:sectionId" +
                "       and cast(qa.applyTime as date) between cast(:startTime as date) and cast(:endTime as date)";
        Map<String, Object> params = new HashMap<>();
        params.put("unitId", unitId);
        params.put("sectionId", sectionId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswer.class));
    }

    public List<QuestionAnswer> findOverTime(String startTime, String endTime) {
        String sql = "select qa.* from  Question_Answer qa " +
                " where 1=1 " +
                "  and (qa.status ='K' or qa.status ='A')" +
                //   "  and sId not in (14642,14643,14644,14645)" +
                "  and efficientTime is not null" +
                "  and DateDiff(DAY,efficientTime  , getDate() ) > 0" +
                "  and cast(qa.efficientTime as date) between cast(:startTime as date) and cast(:endTime as date)";
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswer.class));
    }

    public List<QuestionAnswer> findExpiration(Integer intervalDay, String startTime, String endTime) {
        if (intervalDay == null) intervalDay = 30;
        String sql = "select qa.* from  Question_Answer qa " +
                " where 1=1 " +
                "  and (qa.status ='K' or qa.status ='A')" +
                "  and DateDiff(DAY,getDate(),efficientTime) between 0 and :intervalDay " +
                //    "  and sId not in (14642,14643,14644,14645)" +
                "  and efficientTime is not null" +
                "  and cast(qa.efficientTime as date) between cast(:startTime as date) and cast(:endTime as date)";

        Map<String, Object> params = new HashMap<>();
        params.put("intervalDay", intervalDay);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswer.class));
    }


    public List<QuestionAnswer> findEdit(String unitId, String sectionId, String startTime, String endTime) {
        String sql = "select qa.* from  Question_Answer qa " +

                " where 1=1 " +
                " And isEdit='Y' " +
                " And  (unitId=:unitId or  :unitId is null)     " +
                " And  (sectionId = :sectionId or :sectionId is null)" +
                " And cast(lastUpdateTime as date) between cast(:startTime as date) and cast(:endTime as date)"+
                " order by orderTime DESC";
        Map<String, Object> params = new HashMap<>();
        params.put("unitId", unitId);
        params.put("sectionId", sectionId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswer.class));
    }

    //查詢已刪除或下架的資料
    public List<QuestionAnswer> findDeleteList(String startTime, String endTime, String unitId, String sectionId) {
        String sql = " 	SELECT                                                                                  " +
                " 	   ANSWER, APPLIER, APPLYTIME,CLOSEDATA, CLOSEDATATIME, CREATETIME,                             " +
                " 	   CREATOR, DISABLEREASON, EFFICIENTTIME, HIT, ISEDIT,                                          " +
                " 	   KEYWORD, LASTMODIFIER, LASTUPDATETIME,OLDANSWER, OLDQUESTION,                                " +
                " 	   QUESTION, SCORE, SECTIONID,SID, STATUS, UNITID,'A' AS TYPE                                   " +
                " 	FROM                                                                                            " +
                " 		QUESTION_ANSWER                                                                             " +
                " Where 1=1 " +
                //" And (cast(closeDataTime as date) between :startTime and :endTime  or closeDataTime is null)" +
                " And (cast(closeDataTime as date) between :startTime and :endTime)" +
                " And  (unitId=:unitId or  :unitId is null)     " +
                " And  (sectionId = :sectionId or :sectionId is null)" +
                " 	AND                                                                                             " +
                " 	(                                                                                               " +
                " 		STATUS='D'                                                                                  " +
                " 		OR                                                                                          " +
                " 		SID IN                                                                                      " +
                " 		(                                                                                           " +
                " 		    SELECT PID FROM QUESTIONANSWER_BAK WHERE PID<>0   And  (unitId=:unitId or  :unitId is null) and (sectionId = :sectionId or :sectionId is null)" +
                " 		    UNION                                                                                   " +
                " 		    SELECT PID FROM QUESTIONANSWER_RELATION WHERE STATUS='D'   And  (unitId=:unitId or  :unitId is null) and (sectionId = :sectionId or :sectionId is null) " +
                " 		)                                                                                           " +
                " 	)                                                                                               " +

                " UNION ALL                                                                                         " +
                " 	SELECT                                                                                          " +
                " 		ANSWER, APPLIER, APPLYTIME,CLOSEDATA, CLOSEDATATIME, CREATETIME,                            " +
                " 		CREATOR, DISABLEREASON, EFFICIENTTIME, HIT, ISEDIT,                                         " +
                " 		KEYWORD, LASTMODIFIER, LASTUPDATETIME,OLDANSWER, OLDQUESTION,                               " +
                " 		QUESTION, SCORE, SECTIONID,SID, STATUS, UNITID,'B' AS TYPE                                  " +
                " 	FROM                                                                                            " +
                " 		QUESTIONANSWER_BAK                                                                          " +
                " 	WHERE                                                                                           " +
                " 		FLAG='A'                                                                                    " +
                " And  (unitId = :unitId or :unitId is null)  " +
                " And  (sectionId = :sectionId or :sectionId is null)  " +
                " 	AND                                                                                             " +
                " 		cast(LASTUPDATETIME as date) between :startTime and :endTime                     		    ";

        Map<String, Object> params = new HashMap<>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("unitId", unitId);
        params.put("sectionId", sectionId);

        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(QuestionAnswer.class));
    }

    public QuestionAnswer insert(QuestionAnswer questionAnswer) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into Question_Answer(unitId,sectionId,question,answer,keyword,status,isEdit,isPublic,efficientTime,creator,createTime,orderTime) values(" +
                ":unitId,:sectionId,:question,:answer,:keyword,:status,:isEdit,:isPublic,:efficientTime,:creator,:createTime,getDate())";
        questionAnswer.setIsEdit("N");
        questionAnswer.setCreator(userInstance.getUserObject(httpServletRequest).getUserID());
        questionAnswer.setCreateTime(getTimeString());
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(questionAnswer), keyHolder);
        int key = keyHolder.getKey().intValue();
        questionAnswer.setsId(key);
        return questionAnswer;
    }

    public QuestionAnswer update(QuestionAnswer questionAnswer) throws JsonProcessingException {

        String sql = "update Question_Answer  set unitId=:unitId,sectionId=:sectionId,question=:question,answer=:answer,keyword=:keyword,efficientTime=:efficientTime," +
                "status=:status ,applier=:applier,applyTime=:applyTime,closeData=:closeData,closeDataTime=:closeDataTime,score=:score,hit=:hit, " +
                "disableReason=:disableReason,disableTime=:disableTime,isPublic=:isPublic,isEdit=:isEdit, " +
                "lastModifier=:lastModifier,lastUpdateTime=:lastUpdateTime,orderTime=getDate() where sId=:sId";

        questionAnswer.setLastModifier(userInstance.getUserObject(httpServletRequest).getUserID());
        questionAnswer.setIsEdit("Y");
        questionAnswer.setLastUpdateTime(getTimeString());

        int rows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(questionAnswer));

        return questionAnswer;

    }

    public int deleteBySId(int sId) {
        String sql = "delete from Question_Answer where sId=:sId";
        Map<String, Object> params = new HashMap<>();
        params.put("sId", sId);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    //下架
    public QuestionAnswer off(QuestionAnswer questionAnswer) throws JsonProcessingException {
        questionAnswer.setStatus("D");
        questionAnswer.setCloseData(userInstance.getUserObject(httpServletRequest).getUserID());
        questionAnswer.setCloseDataTime(getTimeString());
        return update(questionAnswer);
    }

    //申請下架
    public QuestionAnswer applyOff(QuestionAnswer questionAnswer) throws JsonProcessingException {
        questionAnswer.setStatus("S");
        questionAnswer.setApplier(userInstance.getUserObject(httpServletRequest).getUserID());
        questionAnswer.setApplyTime(getTimeString());
        return update(questionAnswer);
    }

    //取消下架
    public QuestionAnswer cancelOff(QuestionAnswer questionAnswer) throws JsonProcessingException {
        questionAnswer.setStatus("K");
        return update(questionAnswer);
    }

    private String getTimeString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private QAEditHistory createQAEditHistory(QuestionAnswer questionAnswer) {
        QAEditHistory qaEditHistory = new QAEditHistory();
        qaEditHistory.setQsId(questionAnswer.getsId());
        qaEditHistory.setQuestion(questionAnswer.getQuestion());
        qaEditHistory.setAnswer(questionAnswer.getAnswer());
        qaEditHistory.setKeyword(questionAnswer.getKeyword());
        qaEditHistory.setLastModifier(userInstance.getUserObject(httpServletRequest).getUserID());
        qaEditHistory.setLastUpdateTime(getTimeString());
        qaEditHistory.setEditDate(getTimeString().replaceAll("-", "").substring(0, 8));
        qaEditHistory.setSource("A");
        return qaEditHistory;

    }
}
