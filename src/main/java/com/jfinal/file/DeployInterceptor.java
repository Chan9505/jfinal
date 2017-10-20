package com.jfinal.file;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * @author lee
 * @date 2017年10月9日 下午3:42:25
 * 
 */
public class DeployInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		 inv.getController().getResponse().setHeader("Access-Control-Allow-Origin", "*"); // 解决跨域访问报错
	     inv.getController().getResponse();
	     inv.invoke();
	}

}
