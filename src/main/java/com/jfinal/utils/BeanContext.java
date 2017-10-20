package com.jfinal.utils;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Enhancer;

/**
 * JFinal aop类会一直生成，比较浪费，包装enhancer成单例方法
 * @author lee
 *
 */
public class BeanContext {

	/** 存放enhancer出来的类 */
	@SuppressWarnings("rawtypes")
	private static Map beanMap = new HashMap<>(); 
	
	/**
	 * 根据bean类获取bean
	 * 	如果未生成过，则重新生成
	 * 	如果生成过，从已经生成的列表获取
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		if (null == beanMap.get(clazz)) {
			beanMap.put(clazz, Enhancer.enhance(clazz));
		}
		return (T) beanMap.get(clazz);
	}
}
