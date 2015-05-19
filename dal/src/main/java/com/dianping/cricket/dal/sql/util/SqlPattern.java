package com.dianping.cricket.dal.sql.util;

public enum SqlPattern {
	LOAD("SELECT {FIELDS} FROM {TABLE} WHERE {CONDITIONS}"), 
	SAVE("INSERT INTO {TABLE}({FIELDS}) VALUES ({VALUES})"), 
	UPDATE("UPDATE {TABLE} SET {ASSIGNMENTS} WHERE {CONDITIONS}"), 
	DELETE("DELETE FROM {TABLE} WHERE {CONDITIONS}");
	
	private String pattern;
	private SqlPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public static final String FIELDS = "\\{FIELDS\\}";
	public static final String TABLE = "\\{TABLE\\}";
	public static final String CONDITIONS = "\\{CONDITIONS\\}";
	public static final String ASSIGNMENTS = "\\{ASSIGNMENTS\\}";
	public static final String VALUES = "\\{VALUES\\}";
}
