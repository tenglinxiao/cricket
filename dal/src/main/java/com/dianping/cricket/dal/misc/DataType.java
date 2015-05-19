package com.dianping.cricket.dal.misc;

/**
 * Describe mysql data types. All data types are divided into 3 categories, Number, Text & DateTime.
 * @author uknow
 * @since 0.0.1
 */
public class DataType {
	public static interface Type {
		// Get type id.
		public int getId();
	}
	public static enum Number implements Type {
		BIT(0x1, "bit"),
		TINYINT(0x2,"tinyint"),
		SMALLINT(0x4, "smallint"),
		INT(0x8, "int"),
		BIGINT(0x10, "bigint"),
		DICIMAL(0x20, "dicimal"),
		FLOAT(0x40, "float"),
		DOUBLE(0x80, "double"),
		UNKNOWN(0x0, "unknown"),
		ANY(~0x0, "*");
		
		private int id;
		private String typeName;
		private Number(int id, String typeName) {
			this.id = id;
			this.typeName = typeName;
		}
		
		public int getId() {
			return id;
		}
		public String getTypeName() {
			return this.typeName;
		}
	}
	
	public static enum Text implements Type {
		TEXT(0x1, "text"),
		TINYTEXT(0x2, "tinytext"),
		MEDIUMTEXT(0x4, "mediumtext"),
		BLOB(0x8, "blob"),
		MEDIUMBLOB(0x10, "mediumblob"),
		LONGBLOB(0x20, "longblob"),
		VARCHAR(0x40, "varchar"),
		CHAR(0x80, "char"),
		UNKNOWN(0x0, "unknown"),
		ANY(~0x0, "*");
		
		private int id;
		private String typeName;
		private Text(int id, String typeName) {
			this.id = id;
			this.typeName = typeName;
		}
		public int getId() {
			return id;
		}
		public String getTypeName() {
			return this.typeName;
		}
	}
	
	public static enum DateTime implements Type {
		TIMESTAMP(0x1, "timestamp"),
		DATE(0x2, "date"),
		TIME(0x4, "time"),
		DATETIME(0x8, "datetime"),
		UNKNOWN(0x0, "unknown"),
		ANY(~0x0, "*");
		
		private int id;
		private String typeName;
		private DateTime(int id, String typeName) {
			this.typeName = typeName;
		}
		public int getId() {
			return id;
		}
		public String getTypeName() {
			return this.typeName;
		}
	}
	
	public static enum Virtual implements Type {
		ANY(~0x0, "*"), UNKNOWN(0x0, "unknown");
		
		private int id;
		private String typeName;
		private Virtual(int id, String typeName) {
			this.typeName = typeName;
		}
		public int getId() {
			return id;
		}
		public String getTypeName() {
			return this.typeName;
		}
	}
	
	public static Type valueOf(String value) {
		Type type = Number.valueOf(value);
		
		if (type == null) {
			type = Text.valueOf(value);
		}
		
		if (type == null) {
			type = DateTime.valueOf(value);
		}
		
		if (type == null) {
			return Virtual.UNKNOWN;
		} else {
			return type;
		}
	}
}