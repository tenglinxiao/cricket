package com.dianping.cricket.dal.sql;

import java.util.ArrayList;
import java.util.List;

import com.dianping.cricket.api.Visitor;
import com.dianping.cricket.dal.misc.Operator.Op;

public class Conditions extends Condition {
	private List<Condition> conditions = new ArrayList<Condition>(); 
	private List<Op> ops = new ArrayList<Op>();
	
	public Conditions() {
		super(null);
	}
	
	public Conditions condition(Condition condition) {
		conditions.add(condition);
		return this;
	}
	
	public Conditions op(Op op) {
		ops.add(op);
		return this;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < conditions.size(); index++) {
			builder.append(conditions.get(index));
			if (index > 0) {
				builder.append(ops.get(index - 1));
			}
		}
		return builder.toString();
	}
}
