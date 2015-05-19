package com.dianping.cricket.dal.misc;

import com.dianping.cricket.dal.sql.Condition;


public class Operator {
	public static interface Op {
		// Apply the condition parameters onto the operator.
		public String visit(Condition contition);
		public String getPattern();
	}
	
	public static enum LogicalOperator implements Op {
		AND, OR, NOT, XOR;

		@Override
		public String visit(Condition condition) {
			return super.toString();
		}

		@Override
		public String getPattern() {
			return super.toString();
		}
	}
	
	public static enum ComparableOperator implements Op {
		BETWEEN_AND("BETWEEN %s AND %s", 2), 
		EQUAL("= %s", 1), 
		NOT_EQUAL("!= %s", 1), 
		IS_NULL("IS NULL", 0), 
		IS_NOT_NULL("IS NOT NULL", 0), 
		IN("IN (%s)", Integer.MAX_VALUE), 
		NOT_IN("NOT IN (%s)", Integer.MAX_VALUE), 
		LESS_THAN("< %s", 1), 
		LESS_OR_EQUAL_THAN("<= %s", 1), 
		GREATER_THAN("> %s", 1) , 
		GREATER_OR_EQUAL_THAN(">= %s", 1), 
		LIKE("LIKE %s", 1), 
		NOT_LIKE("NOT LIKE %s", 1);
		
		private String pattern;
		private int operands;
		
		private ComparableOperator(String pattern, int operands) {
			this.pattern = pattern;
			this.operands = operands;
		}
		
		public String visit(Condition condition) {
			return condition.accept(this);
		}
		
		public String getPattern() {
			return pattern;
		}
		
		public int getOperands() {
			return operands;
		}
	}
}
