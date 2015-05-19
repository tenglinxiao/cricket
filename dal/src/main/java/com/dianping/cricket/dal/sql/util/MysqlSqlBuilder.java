package com.dianping.cricket.dal.sql.util;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;

import com.dianping.cricket.dal.sql.OrderBy.Order;

public class MysqlSqlBuilder extends SqlBuilder {

	public MysqlSqlBuilder(SqlType type) {
		super(type);
	}

	@Override
	public void buildSelect(List<String> fields, String table) {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < fields.size(); index++) {
			if (index != 0) {
				builder.append(", ");
			}
			builder.append(fields.get(index));
		}
		
		select = "SELECT " + builder.toString();
		
		buildFrom(table);
		
	}
	
	public void buildDelete(String table) {
		
	}
	
	public void buildInsert() {
		
	}

	@Override
	public void buildFrom(String fromTable) {
		from = "FROM " + fromTable;
		
	}

	@Override
	public void buildWhere(List<String> conditions) {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < conditions.size(); index++) {
			if (index != 0) {
				builder.append(" AND ");
			}
			builder.append(conditions.get(index));
		}
		where = "WHERE " + builder.toString();
		
	}

	@Override
	public void buildGroupBy(List<String> fields) {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < fields.size(); index++) {
			if (index != 0) {
				builder.append(", ");
			}
			builder.append(fields.get(index));
		}
		
		groupBy = "GROUP BY " + builder.toString();
		
	}

	@Override
	public void buildOrderBy(List<SimpleImmutableEntry<String, Order>> orders) {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < orders.size(); index++) {
			if (index != 0) {
				builder.append(", ");
			}
			builder.append(orders.get(index).getKey() + " " + orders.get(index).getValue());
		}
		
		orderBy = "ORDER BY  " + builder.toString();
		
	}

	@Override
	public void buildLimit(int limit) {
		this.limit = "LIMIT " + limit;
		
	}

	@Override
	public void buildOffset(int offset) {
		this.offset = "OFFSET " + offset;
	}

	@Override
	public void buildUpdate(Map<String, Object> fieldValues, String table) {
		update = "UPDATE " + table;
		buildSet(fieldValues);
		
	}

	@Override
	public void buildInsert(Map<String, Object> fieldValues, String table) {
		insert = "INSERT INTO " + table;
		buildValues(fieldValues);
		
	}

	@Override
	public void buildValues(Map<String, Object> fieldValues) {
		insert += "(";
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for (String field : fieldValues.keySet()) {
			if (index++ != 0) {
				insert += ", ";
				builder.append(", ");
			}
			insert += field;
			builder.append(field);
		}
		insert += ")";
		
		values = "VALUES (" + builder.toString() + ")";
		
	}

	@Override
	public void buildSet(Map<String, Object> fieldValues) {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for (String field : fieldValues.keySet()) {
			if (index++ != 0) {
				builder.append(", ");
			}
			builder.append(String.format("%s = %s", field, fieldValues.get(field)));
		}
		
		set = "SET " + builder.toString();
	}
}
