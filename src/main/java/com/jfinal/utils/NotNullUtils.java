package com.jfinal.utils;

import java.util.Collection;

public class NotNullUtils {
		/**
	    * 
	    * @param object
	    * @return 1. 对象为null 2. 对象为集合并且元素个数为0 3. 对象为字符串 并且为空串
	    */
	   @SuppressWarnings("rawtypes")
	   public static boolean isNotNull(Object object) {
	      if (object == null) {
	         return false;
	      }
	      if (object instanceof Collection && ((Collection) object).size() == 0) {
	         return false;
	      }
	      if (object instanceof String && "".equals(((String) object).trim())) {
	         return false;
	      }
	      return true;
	   }
}
