package com.jfinal.common.constants;

/**
 * @Title: Result.java 
 * @Description: 前端交互结果类
 * @author lee  
 * @date 2017年9月26日 下午10:14:53
 */
public class Result<T> {
    /**
     * 返回状态
     */
    private boolean success;
	/**
	 * 返回状态
	 */
	private String code;
	/**
	 * 返回信息
	 */
	private String message;
	/**
	 * 返回值
	 */
	private T data;
	
	/**
	 * <p>Title: </p> 
	 * <p>Description: </p>正常返回结果
	 */
	public Result(T data) {
		this.code = ResultCode.SUCCESS.getCode();
		this.data = data;
	}

	/**
	 * <p>Title: </p> 
	 * <p>Description: </p>自定义带返回状态跟参数的结果
	 */
	public Result(String code, String message, T value) {
	    this(true, code, message, value);
	}

    public Result(boolean success, String code, String message, T value) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = value;
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
