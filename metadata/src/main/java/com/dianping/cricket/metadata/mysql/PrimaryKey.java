package com.dianping.cricket.metadata.mysql;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * Primary key definition.
 * @author tenglinxiao
 * @since 1.0
 *
 */
@XmlRootElement(name="primaryKey")
public class PrimaryKey extends UniqueKey{
	public PrimaryKey() {}
	
	public PrimaryKey(String name) {
		super(name);
	}
	
	public void addColumn(Column column)
	{
		column.setPrimaryKey(this);
		this.getColumns().add(column);
	}
}
