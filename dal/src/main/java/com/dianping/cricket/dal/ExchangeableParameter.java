package com.dianping.cricket.dal;

public class ExchangeableParameter {
	private String sql;
	private int id;
	public ExchangeableParameter(String sql)
	{
		this.sql = sql;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
