package com.jfinal.file;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class AinjectionInterceptor implements Interceptor{
	//防注入拦截器
	@Override
	public void intercept(Invocation inv) {
		String a = "";
		Controller controller= inv.getController();
		 for (String[] parames : controller.getParaMap().values()) {
	            for (int i = 0; i < parames.length; i++) {
	            	a+=clearXss(parames[i]);
	            }
	        }
         if(a.indexOf("1")!=-1){
        	 controller.renderJson("404");
         }else{
        	 inv.invoke();
         }
         
	}
	private String clearXss(String lowStr) { 
		String flag = "0";
		Pattern SCRIPT_PATTERN = Pattern.compile("<script.*>.*<\\/script\\s*>");
		Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");
		  // 过滤html标签
		  Matcher mHtml = HTML_PATTERN.matcher(lowStr);
		  if (mHtml.find()) {
		   flag = "1";
		  }
		  // 过滤script脚本
		  Matcher m = SCRIPT_PATTERN.matcher(lowStr);
		  if (m.find()) {
		   flag = "1";
		  }
		  // 过滤sql转换函数
		  if (lowStr.contains("ascii(") || lowStr.contains("ascii (")
		|| lowStr.contains("chr(") || lowStr.contains("chr (")) {
		   flag = "1";
		  }
		  // 过滤sql关键字
		  if (lowStr.contains("alter ") || lowStr.contains("create ")
		|| lowStr.contains("truncate ")
		|| lowStr.contains("drop ")
		|| lowStr.contains("lock table")
		|| lowStr.contains("insert ")
		|| lowStr.contains("update ")
		|| lowStr.contains("delete ")
		|| lowStr.contains("select ")
		|| lowStr.contains("or ")
		|| lowStr.contains("grant ")) {
		   flag = "1";
		  }
	
		return flag; 
	}

}
