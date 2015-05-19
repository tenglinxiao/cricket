package com.dianping.cricket.dal.sql.util;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;

import com.dianping.cricket.api.Builder;
import com.dianping.cricket.dal.sql.OrderBy.Order;

public abstract class SqlBuilder implements Builder<String>{
	public static enum SqlType {
		SELECT, SAVE, UPDATE, DELETE;
	}
	
	protected SqlType type;
	protected String select;
	protected String update;
	protected String delete;
	protected String insert;
	protected String values;
	protected String set;
	protected String from;
	protected String where;
	protected String groupBy;
	protected String orderBy;
	protected String limit;
	protected String offset;
	
	public SqlBuilder(SqlType type) {
		this.type = type;
	}
	
	public abstract void buildSelect(List<String> fields, String table);
	public abstract void buildUpdate(Map<String, Object> fieldValues, String table);
	public abstract void buildDelete(String table);
	public abstract void buildInsert(Map<String, Object> fieldValues, String table);
	public abstract void buildFrom(String fromTable);
	public abstract void buildValues(Map<String, Object> fieldValues);
	public abstract void buildSet(Map<String, Object> fieldValues);
	public abstract void buildWhere(List<String> conditions);
	public abstract void buildGroupBy(List<String> fields);
	public abstract void buildOrderBy(List<SimpleImmutableEntry<String, Order>> orders);
	public abstract void buildLimit(int limit);
	public abstract void buildOffset(int offset);
	public String build() {
		StringBuilder builder = new StringBuilder();
		if (type == SqlType.SELECT) {
			builder.append(select + "\n");
			builder.append(from + "\n");
			if (where != null) {
				builder.append(where + "\n");
			}
			if (groupBy != null) {
				builder.append(groupBy + "\n");
			}
			if (orderBy != null) {
				builder.append(orderBy + "\n");
			}
			if (limit != null) {
				builder.append(limit + "\n");
			}
			if (offset != null) {
				builder.append(offset + "\n");
			}
		} else if (type == SqlType.SAVE) {
			builder.append(insert + "\n");
			builder.append(values + "\n");
		} else if (type == SqlType.UPDATE) {
			builder.append(update + "\n");
			builder.append(set + "\n");
			if (where != null) {
				builder.append(where + "\n");
			}
		} else {
			builder.append(delete + "\n");
			builder.append(from + "\n");
			if (where != null) {
				builder.append(where + "\n");
			}
		}
		return builder.toString();
	}
}
