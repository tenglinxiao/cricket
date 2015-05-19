package com.dianping.cricket.dal.sql;

import com.dianping.cricket.dal.misc.DataType.Type;

public class Field extends Token {
	public static final String FIELD_PATTERN = "`%s`";
	private String name;
	private Type type;
	
	public Field(String name, Type type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return String.format(FIELD_PATTERN, name);
	}
}
