package com.consilium.service;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.consilium.domain.Menu;
import com.consilium.domain.User;

/**
 * @author Greco
 * @date 2010-2-24
 * @version 
 */
@Service
public class ConfigService extends BaseService {
	private static final String SQL_GET_MENUS = 
		"SELECT DISTINCT m.parentId,m.menuId,m.name,m.icon,m.url,m.type,m.menuOrder FROM MENU m " + 
		"LEFT JOIN r_roles_menus rm ON m.menuId = rm.menuId WHERE (rm.role_id IN (" + 
		"SELECT role_id FROM r_users_roles WHERE user_id = ?) )";
	private static final String SQL_GET_MENUS_PARAMETER = 
		"SELECT DISTINCT m.menuId,m.name,m.parentId,m.icon,m.url,m.type,m.menuOrder " +
		"FROM MENU m LEFT JOIN r_roles_menus rm ON m.menuId = rm.menuId " +
		"WHERE (rm.role_id IN (SELECT role_id FROM r_users_roles WHERE user_id = ?) OR m.parentId = 0) " + 
		"and m.type=1 " +
		"union SELECT DISTINCT m.menuId,m.name,m.parentId,m.icon,m.url,m.type,m.menuOrder " + 
		"FROM MENU m LEFT JOIN r_roles_menus rm ON m.menuId = rm.menuId " + 
		"WHERE (rm.role_id IN (" +
		"SELECT role_id FROM r_users_roles WHERE user_id = ?) and m.parentId = ?) " + 
		"and m.type=2";
	@SuppressWarnings("unchecked")
	public List<Menu> getMenus(final User user) {
		if (user == null)
			return new ArrayList<Menu>();
		@SuppressWarnings("rawtypes")
		List<Menu> results = jdbcTemplate.query(SQL_GET_MENUS, new Object[] {user.getUserId()}, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Menu menu = new Menu();
				menu.setMenuId(rs.getInt("menuId"));
				menu.setName(rs.getString("name"));
				menu.setParentId(rs.getInt("parentId"));
				menu.setMenuOrder(rs.getInt("menuOrder"));
				menu.setUrl(rs.getString("url"));
				menu.setIcon(rs.getString("icon"));
				menu.setType(rs.getString("type"));
				return menu;
			}
		});
		
		return results;
	}
	@SuppressWarnings("unchecked")
	public List<Menu> getMenus(final User user,final String menuId) {
		if (user == null)
			return new ArrayList<Menu>();
		@SuppressWarnings("rawtypes")
		List<Menu> results = jdbcTemplate.query(SQL_GET_MENUS_PARAMETER, new Object[] {user.getUserId(),user.getUserId(),menuId}, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Menu menu = new Menu();
				menu.setMenuId(rs.getInt("menu_id"));
				menu.setName(rs.getString("name"));
				menu.setParentId(rs.getInt("parent_menu_id"));
				menu.setMenuOrder(rs.getInt("menu_order"));
				menu.setUrl(rs.getString("url"));
				menu.setIcon(rs.getString("icon"));
				menu.setType(rs.getString("type"));
				return menu;
			}
		});
		
		return results;
	}
	
}

