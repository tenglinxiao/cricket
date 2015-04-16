package com.dianping.cricket.metadata.mysql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 * Column definition for depicting column.
 * @author tenglinxiao
 * @since 1.0
 */

@XmlRootElement(name="column")
public class Column extends MysqlMetaData {
	// Default value.
	private Object defaultValue;
	// Whether field is nullable.
	private boolean nullable = true;
	// Field length if it has.
	private int length;
	// Primary key that this field belongs to.
	private PrimaryKey primaryKey;
	// Foreign key that this field belongs to.
	private ForeignKey foreignkey;
	// Unique keys or other keys defined.
	private List<Key> keys;
	
	public Column() {}
	
	public Column(String name) {
		super(name);
	}

	public Object getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public boolean isNullable() {
		return nullable;
	}
	
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	@XmlTransient
	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}
	
	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	@XmlTransient
	public ForeignKey getForeignkey() {
		return foreignkey;
	}
	
	public void setForeignkey(ForeignKey foreignkey) {
		this.foreignkey = foreignkey;
	}
	@XmlTransient
	public List<Key> getKeys() {
		return keys;
	}

	public void setKeys(List<Key> keys) {
		this.keys = keys;
	}
	
	public void addKey(Key key)
	{
		if (keys == null) {
			keys = new ArrayList<Key>();
		}
		keys.add(key);
	}
}
