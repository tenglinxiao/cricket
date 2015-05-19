package com.dianping.cricket.dal.sql.util;

import java.util.ArrayList;
import java.util.List;

public class MysqlSqlTemplate extends SqlTemplate {
	private String table;
	private List<String> fields;
	private List<Object> values;
	private List<String> conditions;
	private List<String> assignments;
	
	public MysqlSqlTemplate(String pattern) {
		super(pattern);
	}
	
	public MysqlSqlTemplate(SqlPattern pattern) {
		super(pattern.getPattern());
	}
	
	public MysqlSqlTemplate table(String table) {
		this.table = table;
		return this;
	}

	public MysqlSqlTemplate fields(List<String> fields) {
		this.fields = fields;
		return this;
	}

	public MysqlSqlTemplate values(List<Object> values) {
		this.values = values;
		return this;
	}

	public MysqlSqlTemplate conditions(List<String> conditions) {
		this.conditions = conditions;
		return this;
	}

	public MysqlSqlTemplate assignments(List<String> assignments) {
		this.assignments = assignments;
		return this;
	}

	@Override
	protected String applyTable(String pattern) {
		return pattern.replaceAll(SqlPattern.TABLE, "`" + table + "`");
	}

	@Override
	protected String applyFields(String pattern) {
		if (this.fields != null) {
			List<String> fields = new ArrayList<String>();
			for (int index = 0; index < this.fields.size(); index++) {
				fields.add("`" + this.fields.get(index) + "`");
			}
			return pattern.replaceAll(SqlPattern.FIELDS, join(fields, COMMA));
		}
		return pattern;
	}

	@Override
	protected String applyValues(String pattern) {
		if (this.values != null) {
			List<String> fields = new ArrayList<String>();
			for (int index = 0; index < this.values.size(); index++) {
				fields.add("'" + this.values.get(index) + "'");
			}
			return pattern.replaceAll(SqlPattern.VALUES, join(fields, COMMA));
		}
		return pattern;
	}

	@Override
	protected String applyAssignments(String pattern) {
		return pattern.replaceAll(SqlPattern.ASSIGNMENTS, join(assignments, COMMA));
	}

	@Override
	protected String applyConditions(String pattern) {
		return pattern.replaceAll(SqlPattern.CONDITIONS, join(conditions, AND));
	}
	
	public String join(List<?> tokens, String delimiter) {
		if (tokens == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < tokens.size(); index++) {
			if (index != 0) {
				builder.append(delimiter);
			}
			builder.append(tokens.get(index));
		}
		return builder.toString();
	}
	
	public static String getCondition(String field, Object value) {
		return String.format("%s = %s", field, value);
	}
	
	public static String getAssignment(String field, Object value) {
		return String.format("%s = %s", field, value);
	}
}
