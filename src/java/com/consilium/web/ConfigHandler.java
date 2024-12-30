package com.consilium.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.consilium.common.Const;
import com.consilium.domain.Menu;
import com.consilium.domain.User;
import com.consilium.service.ConfigService;
import com.consilium.utils.Utils;

/**
 * @author Greco
 * @date 2010-2-24
 * @version $Id$
 */
public class ConfigHandler extends BaseAjaxController {
	private ConfigService service;
	public void setService(ConfigService service) {
		this.service = service;
	}
	
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = Utils.getUserFromSession(request);
		List<Menu> menus = null;
		menus = service.getMenus(user);
		
		model.put(Const.MODEL_MENUS, menus);		
		return new ModelAndView("configView", model);
	}
}

