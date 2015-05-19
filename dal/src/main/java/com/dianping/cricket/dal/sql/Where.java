package com.dianping.cricket.dal.sql;

public class Where extends Token {
	private Condition condition;
	
	public Where(Condition condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		 return "WHERE " + condition;
	}
}
