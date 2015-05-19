package com.dianping.cricket.dal.sql;

import com.dianping.cricket.dal.misc.DataType;
import com.dianping.cricket.dal.misc.DataType.Type;

public class ConstantField extends Field {
	public static final String CONSTANT_PATTERN = "'%s'"; 
	private Object value;
	
	public ConstantField(Object value) {
		super(ConstantField.class.getName(), DataType.Virtual.ANY);
		this.value = value;
	}

	public ConstantField(Object value, Type type) {
		super(ConstantField.class.getName(), type);
		this.value = value;
	}
	
	public String toString() {
		return String.format(CONSTANT_PATTERN, value);
	}
	
	public static ConstantField getConstantField(Object value) {
		return new ConstantField(value); 
	}

}
