package com.dianping.cricket.metadata.mysql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.dianping.cricket.metadata.util.MetaDataConstants.MySqlConstants;

@XmlRootElement(name="table")
public class Table extends MysqlMetaData{
	// Primary key.
	private PrimaryKey primaryKey;
	// Columns defintion.
	private List<Column> columns = new ArrayList<Column>();
	// Table foreign keys.
	private List<ForeignKey> foreignKeys;
	// Other non-primary keys.
	private List<Key> keys;
	// Belongs to db name.
	private String dbName;
	// Belongs to host name.
	private String hostName; 
	
	public Table() {}
	
	public Table(String name){ 
		super(name);
	}
	
	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}
	
	public void setPrimaryKey(PrimaryKey primaryKey) {
		primaryKey.setTable(this);
		this.primaryKey = primaryKey;
	}
	
	public List<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}

	public void setForeignKeys(List<ForeignKey> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}

	public List<Column> getColumns() {
		return columns;
	}
	
	@XmlElementWrapper(name="columns")
	@XmlElement(name="column")
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	public List<Key> getKeys() {
		return keys;
	}
	
	public void setKeys(List<Key> keys) {
		this.keys = keys;
	}
	
	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	// Create column if the column doesn't exist.
	public Column getColumn(String columnName)
	{
		for (Column column : columns) {
			if (column.getName().equals(columnName)) {
				return column;
			}
		}
		Column column = new Column(columnName);
		column.setTable(this);
		columns.add(column);
		return column;
	} 
	
	// Create key if the key doesn't exist.
	public Key getKey(String keyName, MySqlConstants.KEY_TYPE type)
	{
		if (keys == null) {
			keys = new ArrayList<Key>();
		} else {
			for (Key key : keys) {
				if (key.getName().equals(keyName)) {
					return key;
				}
			}
		}
		Key key = null;
		if (type == MySqlConstants.KEY_TYPE.UNIQUE) {
			key = new UniqueKey(keyName);
		} else {
			key = new Key(keyName);
		}
		key.setTable(this);
		keys.add(key);
		return key;
	}
	
	public void addForeignKey(ForeignKey key)
	{
		if (foreignKeys == null) {
			foreignKeys = new ArrayList<ForeignKey>();
		}
		key.setTable(this);
		foreignKeys.add(key);
	}
}
