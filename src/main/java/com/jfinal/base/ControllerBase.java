package com.jfinal.base;
import com.jfinal.common.constants.Result;
import com.jfinal.common.constants.ResultCode;
/**
 * @author lee
 * @date 2017年9月27日 下午4:58:50
 * 
 */
import com.jfinal.core.Controller;

/**
 * @Title: ControllerBase.java 
 * @Description: 项目接口基础类，用于对返回结果进行包装统一格式化
 * @author lee  
 * @date 2017年9月27日 下午11:17:07
 */
public class ControllerBase extends Controller {

	/**
	 * @Title: renderResult 
	 * @Description: 默认成功的返回结果 
	 * @param value 返回值
	 */
    protected void renderResult(Object value) {
		Result<Object> result = new Result<Object>(value);
		renderJson(result);
	}
	
	/**
	 * @Title: renderResult 
	 * @Description: 自定义返回的结果 
	 * @param code 返回代码
	 * @param message 返回消息
	 * @param value 返回值
	 */
	protected void renderResult(ResultCode code, String message, Object value) {
		Result<Object> result = new Result<Object>(code.getCode(), message, value);
		renderJson(result);
    }

    protected void renderFailedResult(final String message) {
        Result<Object> result = new Result<Object>(false, "-200", message, null);
        renderJson(result);
    }
    
    protected void renderSucessResult() {
        Result<Object> result = new Result<Object>(true, "200", null, null);
        renderJson(result);
    }

    protected void renderSucessResult(Object value) {
        Result<Object> result = new Result<Object>(true, "200", null, value);
        renderJson(result);
    }
    
    protected void renderResult(String code, String message, Object value) {
		Result<Object> result = new Result<Object>(code, message, value);
		renderJson(result);
    }
}
