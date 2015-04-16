package com.dianping.cricket.metadata.mysql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.dianping.cricket.api.exception.NotSupportedException;

/**
 * Abstract class key to describe key category metadata.
 * @author tenglinxiao
 * @since 1.0
 */
public class Key extends MysqlMetaData{
	// Unique Key included columns.
	private List<Column> columns = new ArrayList<Column>();
	
	public Key() {}
	
	public Key(String name) {
		super(name);
	}
	
	@XmlElementWrapper(name="columns")
	@XmlElement(name="column")
	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	public void addColumn(Column column)
	{
		column.addKey(this);
		this.columns.add(column);
	}
	
	public boolean isUnique() throws NotSupportedException 
	{
		return false;
	}
}
