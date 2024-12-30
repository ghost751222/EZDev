package com.consilium.service;

import com.consilium.domain.SectionData;
import com.consilium.domain.SectionSuperManager;
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
public class SectionSuperManagerService extends BaseService {




    public List<SectionSuperManager> findBySid(int sId) {
        String sql = "select * from  [Section_Super_Manager] where sId=:sId";
        Map<String, Object> params = new HashMap<>();
        params.put("sId", sId);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(SectionSuperManager.class));

    }

    public SectionSuperManager insert(SectionSuperManager sectionSuperManager) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into Section_Super_Manager(unitId,superOccupation,superConnector,superTel,superEmail,superOccupation2,superConnector2,superTel2,superEmail2,creator,createTime,superExtension,superExtension2) values(" +
                ":unitId,:superOccupation,:superConnector,:superTel,:superEmail,:superOccupation2,:superConnector2,:superTel2,:superEmail2,:creator,:createTime,:superExtension,:superExtension2)";
        sectionSuperManager.setCreator(userInstance.getUserObject(httpServletRequest).getUserID());
        sectionSuperManager.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(sectionSuperManager),keyHolder);
        int key = keyHolder.getKey().intValue();
        sectionSuperManager.setsId(key);
        return sectionSuperManager;
    }


    public int update(SectionSuperManager sectionSuperManager) {

        String sql = "update Section_Super_Manager  set unitId=:unitId,superOccupation=:superOccupation,superConnector=:superConnector,superTel=:superTel,superEmail=:superEmail," +
                "superOccupation2=:superOccupation2,superConnector2=:superConnector2,superTel2=:superTel2,superEmail2=:superEmail2," +
                "superExtension=:superExtension,superExtension2=:superExtension2 ,lastModifier=:lastModifier,lastUpdateTime=:lastUpdateTime where sId=:sId";
        sectionSuperManager.setLastModifier(userInstance.getUserObject(httpServletRequest).getUserID());
        sectionSuperManager.setLastUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(sectionSuperManager));
    }


    public List<SectionSuperManager> findUnitId(String unitId) {
        String sql = "select * from  [Section_Super_Manager] where unitId=:unitId";
        Map<String, Object> params = new HashMap<>();
        params.put("unitId", unitId);
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(SectionSuperManager.class));
    }
}
