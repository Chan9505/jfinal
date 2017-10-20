package com.jfinal.common.constants;

/**
 * @Title: ResultCode.java 
 * @Description: 返回状态枚举类 
 * @author lee  
 * @date 2017年9月26日 下午10:16:10
 */
public enum ResultCode {

	SUCCESS("200", 200),
	PARAMETER_ERROR("-100", -100),
	SYSTEM_ERROR("-200", -200), 
	INVALID_UNAME_PASSWORD("-201", -201),
	TOKEN_TIMEOUT("-1", -1),;
	
	private String code;
	private int error;
	
	ResultCode(String code, int error) {
		this.code = code;
		this.error = error;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}
}
