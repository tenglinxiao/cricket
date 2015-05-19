package com.dianping.cricket.dal.sql;

import java.util.ArrayList;
import java.util.List;

public class GroupBy extends Token {
	private List<Field> fields;
	
	public GroupBy(List<Field> fields) {
		this.fields = fields;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < fields.size(); index++) {
			builder.append(fields.get(index));
			if (index != fields.size() - 1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}
}
