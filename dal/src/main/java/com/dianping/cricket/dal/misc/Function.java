package com.dianping.cricket.dal.misc;

import java.util.List;

import com.dianping.cricket.api.exception.InvalidCaseException;
import com.dianping.cricket.dal.exception.ParamsNotEnoughException;
import com.dianping.cricket.dal.misc.DataType.Type;
import com.dianping.cricket.dal.sql.Field;

/**
 * Enum type for mostly used functions.
 * @author tenglinxiao
 * @since 0.0.1
 */
public enum Function {
	COUNT(
			"COUNT(%s)", 
			Integer.MAX_VALUE, 
			ParamsType.MANY_ANY_TYPE,
			DataType.Virtual.ANY
	), 
	SUM(
			"SUM(%s)", 
			1, 
			ParamsType.ONE_SPECIFIED_TYPE, 
			DataType.Number.ANY
	),
	MAX(
			"MAX(%s)",
			1,
			ParamsType.ONE_ANY_TYPE,
			DataType.Virtual.ANY
	),
	MIN(
			"MIN(%s)",
			1,
			ParamsType.ONE_ANY_TYPE,
			DataType.Virtual.ANY
	);
	
	// Pattern for using the function.
	private String pattern;
	// Operands required for this function.
	private int operands;
	// Params type for this function.
	private ParamsType paramsType;
	// Types for the parameters in sequence.
	private Type[] types;

	private Function(String pattern, int operands, ParamsType paramsType, Type... types ) {
		this.pattern = pattern;
		this.operands = operands;
		this.paramsType = paramsType;
		this.types = types;
	}
	
	public String apply(List<Field> fields) throws ParamsNotEnoughException, InvalidCaseException {
		// Check for operands number first, then check the types.
		if (operands != Integer.MAX_VALUE && operands != fields.size()) {
			throw new ParamsNotEnoughException("Params for function [" + this + "] is not enough!");
		} else if (isTypesMatch(fields)){
			String[] fieldNames = new String[fields.size()];
			for (int index = 0; index < fields.size(); index++) {
				fieldNames[index] = fields.get(index).getName();
			}
			return String.format(pattern, fieldNames);
		}
		
		throw new InvalidCaseException("CAN NOT apply function to these values.");
	}

	public boolean isTypesMatch(List<Field> fields) {
		int id = 0;
		switch (paramsType) {
		case ONE_SPECIFIED_TYPE: 
			id = fields.get(0).getType().getId();
			if ((types[0].getId() & id) != id) {
				return false;
			}
			return true;
		case MANY_SAME_TYPE: 
			for (Field field : fields) {
				id = field.getType().getId();
				if ((types[0].getId() & id) != id) {
					return false;
				}
			}
			return true;
		case MANY_SPECIFIED_TYPE:
			for (int index = 0; index < fields.size(); index++){
				id = fields.get(index).getType().getId();
				if ((types[index].getId() & id) != id) {
					return false;
				}
			}
			return true;
		case ONE_ANY_TYPE:
		case MANY_ANY_TYPE:
			return true;
		default:
			return false;
		}
	}
	
	public static enum ParamsType {
		// function requires one param defined in some type.
		ONE_SPECIFIED_TYPE, 
		
		// function requires one param in any type.
		ONE_ANY_TYPE, 
		
		// function requires many params in one same type.
		MANY_SAME_TYPE, 
		
		// function requires many params in specified types.
		MANY_SPECIFIED_TYPE, 
		
		// functions requires many params in any type.
		MANY_ANY_TYPE
	}
}
