package com.dianping.cricket.dal.sql;

import com.dianping.cricket.dal.misc.Operator.Op;

public class Condition extends Token {
	// Left operand.
	private Field left;
	// Right operand.
	private Field right;
	// Operator.
	private Op op;
	
	public Condition(Field left) {
		this.left = left;
	}
	
	public Condition(Field left, Field right) {
		this(left);
		this.right = right;
	}
	
	public Condition(Field left, Field right, Op op) {
		this(left, right);
		this.op = op;
	}
	
	public void setOperator(Op op) {
		this.op = op;
	}
	
	public String accept(Op op) {
		if (this.right == null) {
			return String.format(op.getPattern(), this.left);
		} else {
			return String.format(op.getPattern(), this.left, this.right);
		}
	}

	@Override
	public String toString() {
		return op.visit(this);
	}


}
