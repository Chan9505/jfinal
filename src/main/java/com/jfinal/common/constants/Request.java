package com.jfinal.common.constants;

/**
 * @Title: Request.java 
 * @Description: 请求基础类 
 * @author lee  
 * @date 2017年9月27日 下午9:54:03
 */
public class Request<T> {

	private String userId;
	
	private T param;
	
	private int page = 0;
	
	private int pageSize = 20;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public T getParam() {
		return param;
	}

	public void setParam(T param) {
		this.param = param;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
