package com.consilium.service;

import com.consilium.domain.SectionData;
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
public class SectionDataService extends BaseService {

    public List<SectionData> findAll() {
        String sql = "select * from  Section_Data  order by [unitId],[sectionId] desc";
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SectionData.class));

    }


    public List<SectionData> findBySid(int sId) {
        String sql = "select * from  Section_Data where sId=:sId";
        Map<String, Object> params = new HashMap<>();
        params.put("sId", sId);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(SectionData.class));

    }

    public SectionData insert(SectionData sectionData) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into Section_Data(unitId,sectionId,occupation1,connector1,tel1,email1,occupation2,connector2,tel2,email2,creator,createTime,extension1,extension2,needAudit) values(" +
                ":unitId,:sectionId,:occupation1,:connector1,:tel1,:email1,:occupation2,:connector2,:tel2,:email2,:creator,:createTime,:extension1,:extension2,:needAudit)";
        sectionData.setCreator(userInstance.getUserObject(httpServletRequest).getUserID());
        sectionData.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(sectionData),keyHolder);
        int key = keyHolder.getKey().intValue();
        sectionData.setsId(key);
        return sectionData;
    }


    public int update(SectionData sectionData) {

        String sql = "update Section_Data  set unitId=:unitId,sectionId=:sectionId,occupation1=:occupation1,connector1=:connector1,tel1=:tel1,email1=:email1," +
                "occupation2=:occupation2,connector2=:connector2,tel2=:tel2,email2=:email2," +
                "extension1=:extension1,extension2=:extension2 ,needAudit=:needAudit,lastModifier=:lastModifier,lastUpdateTime=:lastUpdateTime where sId=:sId";
        sectionData.setLastModifier(userInstance.getUserObject(httpServletRequest).getUserID());
        sectionData.setLastUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(sectionData));
    }

//    public List<Map> findByUnitIdAndSectionId(String unitId, String sectionId) {
//        String sql = "select * from  Section_Data where (unitId=:unitId or :unitId is null) and (sectionId=:sectionId or :sectionId is null) order by [unitId],[sectionId] desc";
//        Map<String, Object> params = new HashMap<>();
//        params.put("unitId", unitId);
//        params.put("sectionId", sectionId);
//        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Map.class));
//
//
//    }


    public List<Map<String, Object>> findByUnitIdAndSectionId(String unitId, String sectionId) {

        String sql = "SELECT sp.unitName , u.unitName as sectionName,sd.occupation1,sd.connector1,sd.tel1,sd.extension1,sd.email1,sd.occupation2, sd.connector2,sd.tel2,sd.extension2,sd.email2\n" +
                "  FROM [Section_Data] sd " +
                "  inner join Units u on u.unitCode = sd.sectionId" +
                "  inner join (select * from Units u    where u.superUnit is  null) sp on sd.unitId = sp.unitCode" +
                "  where (unitId=:unitId or :unitId is null) and (sectionId=:sectionId or :sectionId is null) " +
                "  order by sd.unitId ";
        Map<String, Object> params = new HashMap<>();
        params.put("unitId", unitId);
        params.put("sectionId", sectionId);
        return namedParameterJdbcTemplate.queryForList(sql, params);


    }
}
