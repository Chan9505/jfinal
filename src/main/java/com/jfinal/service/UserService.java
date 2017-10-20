package com.jfinal.service;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.base.ServiceBase;
import com.jfinal.common.model.User;

public class UserService extends ServiceBase<User>{

	@Override
	public User getDao() {
		return User.dao;
	}

	public User getUser(String user, String password) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", user);
		params.put("password", password);
		return findFirstLoadColumns("*", params, "");
	}

}
