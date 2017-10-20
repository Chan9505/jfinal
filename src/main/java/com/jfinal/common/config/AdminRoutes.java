package com.jfinal.common.config;

import com.jfinal.config.Routes;
import com.jfinal.controller.AdminController;
import com.jfinal.controller.IndexController;
import com.jfinal.file.AdminInterceptor;
public class AdminRoutes extends Routes {

	@Override
	public void config() {
		addInterceptor(new AdminInterceptor());
		add("/", IndexController.class,"/view");	// 第三个参数为该Controller的视图存放路径
		add("/admin",AdminController.class);
	}

}
