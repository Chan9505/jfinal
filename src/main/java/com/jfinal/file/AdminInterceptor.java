package com.jfinal.file;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.common.constants.Constants;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.utils.NotNullUtils;

public class AdminInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		String token = (String) inv.getController().getSession().getAttribute(Constants.REQ_PARAM_TOKEN);
		HttpServletRequest request = inv.getController().getRequest();
		if(NotNullUtils.isNotNull(token)) {
			Integer uid = CacheKit.get("login", token);
			if(uid!=null) {
				request.setAttribute(Constants.REQ_PARAM_UID,Long.valueOf(uid));
				inv.invoke();
			}else {
				inv.getController().render("/view/login.html");
				//inv.getController().renderJson(Constants.REQ_LOGIN_ERROR);
			}
		}else {
			inv.getController().render("/view/login.html");
			//inv.getController().renderJson(Constants.REQ_LOGIN_ERROR);
		}
	}

}
