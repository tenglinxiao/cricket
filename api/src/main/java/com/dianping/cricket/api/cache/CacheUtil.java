package com.dianping.cricket.api.cache;

import java.lang.reflect.Type;

public class CacheUtil {
	public static int getTypeSize(Type type) {
		switch(type.toString().toUpperCase()) {
		case "INT": 
		case "FLOAT": return 4;
		case "LONG":
		case "DOUBLE": return 8;
		case "BOOLEAN":
		case "BYTE": return 1;
		case "SHORT":
		case "CHAR": return 2;
		default: return -1;
		}
	}
	
	public static int getStringSize(String string) {
		return string.toCharArray().length * 2;
	}

}
