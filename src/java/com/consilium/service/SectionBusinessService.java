package com.consilium.service;

import com.consilium.domain.SectionBusniess;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SectionBusinessService extends BaseService {


    public List<SectionBusniess> findAll() {
        String sql = "select * from  Section_Business";
        Map<String, Object> params = new HashMap<>();
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(SectionBusniess.class));

    }

    public List<Map<String, Object>> findByUnitIdAndSectionId(String unitId, String sectionId,String keyword) {

        String sql = "SELECT sb.sId,sp.unitName , u.unitName as sectionName, sb.[businessName],sb.[businessContent], sb.[connector],sb.[tel],sb.[extension]" +
                "  FROM  Section_Business sb " +
                "  inner join Section_Data sd on sb.pId  = sd.sId" +
                "  inner join Units u on u.unitCode = sd.sectionId" +
                "  inner join (select * from Units u    where u.superUnit is  null) sp on sd.unitId = sp.unitCode" +
                "  where (unitId=:unitId or :unitId is null) and (sectionId=:sectionId or :sectionId is null) " ;

                if(keyword !=null){
                    sql += "  and (businessContent like :keyword or businessName like :keyword)";
                }


        sql+="  order by sd.unitId ";
        Map<String, Object> params = new HashMap<>();
        params.put("unitId", unitId);
        params.put("sectionId", sectionId);
        params.put("keyword", '%' + keyword + '%');
        return namedParameterJdbcTemplate.queryForList(sql, params);


    }

    public List<SectionBusniess> findByPId(int pId) {
        String sql = "select * from  Section_Business where pId=:pId";
        Map<String, Object> params = new HashMap<>();
        params.put("pId", pId);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(SectionBusniess.class));
    }


    public int insert(SectionBusniess sectionBusniess) {
        String sql = "INSERT INTO Section_Business (pId,businessName,businessContent,keyword,connector,tel,status,editContent,creator,createTime,addKeyword,score,hitCount,extension,email)" +
                "VALUES(:pId,:businessName,:businessContent,:keyword,:connector,:tel,:status,:editContent,:creator,:createTime,:addKeyword,:score,:hitCount,:extension,:email)";


        sectionBusniess.setCreator(userInstance.getUserObject(httpServletRequest).getUserID());
        sectionBusniess.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        int rows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(sectionBusniess));
        return rows;
    }


    public int update(SectionBusniess sectionBusniess) {

        String sql = "update Section_Business  set businessName=:businessName,businessContent=:businessContent,keyword=:keyword,connector=:connector," +
                "tel=:tel,status=:status,editContent=:editContent" +
                ",addKeyword=:addKeyword,score=:score,hitCount=:hitCount,extension=:extension,email=:email " +
                ",lastModifier=:lastModifier,lastUpdateTime=:lastUpdateTime"+
                " where sId=:sId";
        sectionBusniess.setLastModifer(userInstance.getUserObject(httpServletRequest).getUserID());
        sectionBusniess.setLastUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(sectionBusniess));
    }


    public int deleteById(Integer sId) {
        String sql = "delete from Section_Business where sId=:sId";
        Map<String, Object> params = new HashMap<>();
        params.put("sId", sId);
        return namedParameterJdbcTemplate.update(sql, params);
    }
}
