package com.dianping.cricket.metadata.util;

import java.util.List;

public class MetaDataQueryAdapter {
	private String dbName;
	private boolean useIncludes;
	private List<String> tableList;
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public boolean getUseIncludes() {
		return useIncludes;
	}
	public void setUseIncludes(boolean useIncludes) {
		this.useIncludes = useIncludes;
	}
	public List<String> getTableList() {
		return tableList;
	}
	public void setTableList(List<String> tableList) {
		this.tableList = tableList;
	}	
}
