package com.jfinal.utils;

import java.util.Random;

/**
 * @author lee
 * @date 2017年10月10日 下午1:39:23
 * 
 */
public class RandomUtils {
	public static String getRandomString(int length) {  
		  int[] ints = new int[length];  
		  int index = 0;  
		  Random random = new Random();  
		  StringBuffer buf = new StringBuffer();  
		  int digit = random.nextInt(9) + 1;  
		  ints[index++] = digit;  
		  buf.append(digit);  
		  while (index < length) {  
		    digit = random.nextInt(10);  
		    if (checkUnique(ints, index, digit)) {  
		      ints[index++] = digit;  
		      buf.append(digit);  
		    }  
		  }  
		  return buf.toString();  
		}  
		  
	private static boolean checkUnique(int[] ints, int size, int digit) {  
	  for (int i = 0; i < size; i++) {  
	    if (ints[i] == digit) {  
	      return false;  
	    }  
	  }  
	  return true;  
	}  
	public static void main(String[] args) { 
		System.out.println(getRandomString(8));
	} 

}
