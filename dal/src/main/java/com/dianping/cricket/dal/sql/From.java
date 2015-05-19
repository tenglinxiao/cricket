package com.dianping.cricket.dal.sql;

public class From extends Token {
	private static final String SELECT_AS_TABLE = "FROM (\n%s) AS %s";
	private String tableName;
	private Sql fromSQL;
	private int depth;
	
	public From(String tableName) {
		this.tableName = tableName;
	}
	
	public From(String tableName, int depth) {
		this.tableName = tableName;
		this.depth = depth;
	}
	
	public From(String tableName, Sql fromSQL) {
		this.tableName = tableName;
		this.fromSQL = fromSQL;
	}
	
	public void setFromSql(Sql fromSQL) {
		this.fromSQL = fromSQL;
		this.fromSQL.setDepth(depth + 1);
	}
	
	public String toString() {
		if (fromSQL == null) {
			return "FROM " + tableName;
		}
		return String.format(SELECT_AS_TABLE, fromSQL.toString(), tableName);
	}

}
