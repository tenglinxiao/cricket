package com.dianping.cricket.dal.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dianping.cricket.api.exception.InvalidCaseException;
import com.dianping.cricket.dal.exception.ParamsNotEnoughException;
import com.dianping.cricket.dal.misc.DataType.Type;
import com.dianping.cricket.dal.misc.Function;

public class Expression extends Field {
	private String expression;
	private List<Field> fields = new ArrayList<Field>();
	private boolean check;
	public Expression(String name, Type type, String expression) {
		super(name, type);
		this.expression = expression;
	}
	
	public void interpret() throws ParamsNotEnoughException, InvalidCaseException {

	}
	
	public void addReferencedField(Field field) {
		this.fields.add(field);
	}
	
	public void enableExpressionCheck() {
		this.check = true;
	}
	
	public String toString() {
		try {
			if (this.check) {
				interpret();
			}
		} catch (ParamsNotEnoughException e) {
			e.printStackTrace();
		} catch (InvalidCaseException e) {
			e.printStackTrace();
		}
		return expression;
	}

}
