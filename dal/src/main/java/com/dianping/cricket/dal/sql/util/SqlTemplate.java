package com.dianping.cricket.dal.sql.util;

public abstract class SqlTemplate implements Template {
	protected static final String AND = " AND ";
	protected static final String COMMA = ", ";
	private String pattern;
	private String sql;
	private boolean applied;
	public SqlTemplate(String pattern) {
		this.pattern = pattern;
	}
	
	// Get the target pattern.
	protected abstract String applyTable(String pattern);
	
	// Get the pattern for apply.
	protected abstract String applyFields(String pattern);
	
	// Get the pattern for apply.
	protected abstract String applyValues(String pattern);
	
	// Get the pattern for apply.
	protected abstract String applyAssignments(String pattern);
	
	// Get the pattern for apply.
	protected abstract String applyConditions(String pattern);

	@SuppressWarnings("unchecked")
	@Override
	public SqlTemplate apply() {
		sql = applyTable(pattern);
		sql = applyFields(sql);
		sql = applyValues(sql);
		sql = applyAssignments(sql);
		sql = applyConditions(sql);
		return this;
	}
	
	public String getSql() {
		if (!applied) {
			apply();
			applied = true;
		}
		return sql;
	}
}
