package com.dianping.cricket.metadata.mysql;

import javax.xml.bind.annotation.XmlTransient;

import com.dianping.cricket.metadata.MetaData;

public abstract class MysqlMetaData extends MetaData {
	// The table this metadata belongs to.
	private Table table;

	public MysqlMetaData() {}
	
	public MysqlMetaData(String name){
		super(name);
	}
	
	@XmlTransient
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
}
