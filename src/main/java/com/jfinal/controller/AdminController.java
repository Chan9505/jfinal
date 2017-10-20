package com.jfinal.controller;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Clear;
import com.jfinal.base.ControllerBase;
import com.jfinal.common.constants.Constants;
import com.jfinal.common.constants.ResultCode;
import com.jfinal.common.model.User;
import com.jfinal.file.AdminInterceptor;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.service.UserService;

public class AdminController extends ControllerBase{
	
	UserService userService = new UserService();
	
	/*
	 * 登录
	 */
	@Clear(AdminInterceptor.class)
	public void login() {
		String user = getRequest().getParameter("user");
		String password = getRequest().getParameter("password");
		User u = userService.getUser(user,password);
		if(u!=null) {
			String token = UUID.randomUUID().toString();
			u.setToken(token);
			u.update();
			if (StringUtils.isNotBlank(token)) {
				getSession().setAttribute(Constants.REQ_PARAM_TOKEN, token);
				CacheKit.put("login", token, u.getId());
				renderSucessResult(u.getName());
			} else {
				renderResult(ResultCode.INVALID_UNAME_PASSWORD, "登录失败",null);
			}
		}else {
			renderResult(ResultCode.INVALID_UNAME_PASSWORD, "登录失败",null);
		}
	}
	/*
	 * 登出
	 */
	public void logout() {
		String  token = getSession().getAttribute(Constants.REQ_PARAM_TOKEN).toString();
		CacheKit.remove("login", token);
		getSession().removeAttribute(Constants.REQ_PARAM_TOKEN);
		renderSucessResult();
	}
	
	
}
