package com.consilium.service;

import com.consilium.domain.QuestionAnswerBak;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuestionAnswerBakService extends BaseService {


    public int insert(QuestionAnswerBak questionAnswerBak) {

        String sql = "insert into QuestionAnswer_bak" +
                "(sId,pId,rId,unitId,sectionId,question,answer,keyword,creator,createTime,lastModifier,lastUpdateTime,status,flag," +
                "efficientTime,score,hit,isPublic,[use],isEdit,applier,applyTime,closeData,closeDataTime,oldQuestion,oldAnswer,disableReason)values" +
                "(:sId,:pId,:rId,:unitId,:sectionId,:question,:answer,:keyword,:creator,:createTime,:lastModifier,:lastUpdateTime,:status,:flag," +
                ":efficientTime,:score,:hit,:isPublic,:use,:isEdit,:applier,:applyTime,:closeData,:closeDataTime,:oldQuestion,:oldAnswer,:disableReason)";
        questionAnswerBak.setrId(0);
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(questionAnswerBak));
    }


    public int deleteByPId(int pId) {
        String sql = "delete from QuestionAnswer_bak where pId=:pId";
        Map<String, Object> params = new HashMap<>();
        params.put("pId", pId);
        return namedParameterJdbcTemplate.update(sql, params);
    }

}
