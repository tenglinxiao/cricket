package com.dianping.cricket.metadata.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dianping.cricket.api.exception.InvalidCaseException;
import com.dianping.cricket.metadata.mysql.Column;
import com.dianping.cricket.metadata.mysql.ForeignKey;
import com.dianping.cricket.metadata.mysql.Key;
import com.dianping.cricket.metadata.mysql.PrimaryKey;
import com.dianping.cricket.metadata.mysql.Table;
import com.dianping.cricket.metadata.util.MetaDataConstants.MySqlConstants;
import com.dianping.cricket.metadata.util.MetaDataConstants.MySqlConstantsUtil;


/**
 * Metadata parser class to parse the metadata fetched from information_schema table, 
 * and use it to build the relationships for table metadata includes primary key, 
 * foreign keys, unique keys and common keys. Use this class & pojos to build an 
 * ecosystem for metadata to let any of the metadata can depict the whole picture
 * of table.
 * @author tenglinxiao
 * @since 1.0
 */
public class MetadataParser {
	// All tables mapping (table name => table)
	private HashMap<String, Table> tables = new HashMap<String, Table>();
	// Host that deployed this db.
	private String host;
	
	private MetadataParser(String host) {
		this.host = host;
	}
	
	private MetadataParser(String host, HashMap<String, Table> tables) {
		this.tables = tables;
	}
	
	/**
	 * Parse to add basic columns metadata to the tables/columns.
	 * @param columnsMetaData
	 * 		  basic metadata for columns.
	 * @return parsed well organized tables.
	 */
	protected HashMap<String, Table> parseForColumns(List<HashMap<String, Object>> columnsMetaData) {
		Table table = null;
		Column column = null;
		
		// Fill basic columns definitions into tables.
		for (HashMap<String, Object> metadata: columnsMetaData) {
			String tableName = (String)metadata.get(MySqlConstants.TABLE_NAME);
			if (tables.containsKey(tableName)) {
				table = tables.get(tableName);
			} else {
				table = new Table(tableName);
				table.setHostName(host);
				table.setDbName((String)metadata.get(MySqlConstants.TABLE_SCHEMA));
				tables.put(tableName, table);
			}
			
			String columnName = (String)metadata.get(MySqlConstants.COLUMN_NAME);
			
			// Create the column obj and set the column properties.
			column = table.getColumn(columnName);
			column.setType(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.DATA_TYPE)));
			column.setDefaultValue(metadata.get(MySqlConstants.COLUMN_DEFAULT));
			column.setNullable(MySqlConstantsUtil.isNullable(metadata.get(MySqlConstants.IS_NULLABLE)));
			column.setComment(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.COLUMN_COMMENT)));
			column.setLength(MySqlConstantsUtil.getNumber(metadata.get(MySqlConstants.CHARACTER_MAXIMUM_LENGTH)));
		}
		return tables;
	}
	
	
	/**
	 * Add constraints data like key/primary key into the tables objects.
	 * @param constraintsMetaData
	 * @return parsed well organized tables.
	 * @throws InvalidCaseException
	 */
	protected HashMap<String, Table> parseForConstraints(List<HashMap<String, Object>> constraintsMetaData) throws InvalidCaseException
	{
		// Build constraints metadata based on the columns.
		for (HashMap<String, Object> metadata: constraintsMetaData) {
			String tableName = (String)metadata.get(MySqlConstants.TABLE_NAME);
			Table table = tables.get(tableName);
			if (table == null) {
				throw new InvalidCaseException("When define the constraints, related table MUST be already existed!");
			}
			
			// Determine the key type.
			MySqlConstants.KEY_TYPE keyType = MySqlConstantsUtil.getConstraintType(metadata.get(MySqlConstants.CONSTRAINT_TYPE));
			
			// Deal with primary key case.
			if (keyType == MySqlConstants.KEY_TYPE.PRIMARY) {
				// If the primary key is not defined, create it first and then add the column to primary key, otherwise directly do it.
				if (table.getPrimaryKey() == null) {
					PrimaryKey primaryKey = new PrimaryKey(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.CONSTRAINT_NAME)));
					primaryKey.addColumn(table.getColumn(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.COLUMN_NAME))));
					table.setPrimaryKey(primaryKey);
				} else {
					table.getPrimaryKey().addColumn(table.getColumn(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.COLUMN_NAME))));
				}
			} else if (keyType == MySqlConstants.KEY_TYPE.UNIQUE || keyType == MySqlConstants.KEY_TYPE.KEY) {
				// Unique key & non-unique key are stored together but in different class instances.
				Key key = table.getKey(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.CONSTRAINT_NAME)), keyType);
				key.addColumn(table.getColumn(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.COLUMN_NAME))));
			} 
		}
		return tables;
	}
	
	/**
	 * Add the foreign keys constraints to the tables.
	 * @param foreignKeyMetaData 
	 * @return parsed well organized tables.
	 * @throws InvalidCaseException 
	 */
	protected HashMap<String, Table> parseForFK(List<HashMap<String, Object>> foreignKeyMetaData) throws InvalidCaseException
	{
		// Build the foreign keys at last, because it has references to other tables, it's better to define its relationships at the end.
		for (HashMap<String, Object> metadata: foreignKeyMetaData) {
			String tableName = (String)metadata.get(MySqlConstants.TABLE_NAME);
			Table table = tables.get(tableName);
			if (table == null) {
				throw new InvalidCaseException("When define the foreign key, related table MUST be already existed!");
			}
			
			ForeignKey key = new ForeignKey(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.CONSTRAINT_NAME)));
			
			// Set the foreign key column.
			List<Column> columns = new ArrayList<Column>();
			columns.add(table.getColumn(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.COLUMN_NAME))));
			key.setColumns(columns);
			
			// Find the referenced table from the mapping table.
			Table referencedTable = tables.get(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.REFERENCED_TABLE_NAME)));
			
			// If the referenced table is not defined, exception will be thrown. This is the case never allowed to happen.
			if (referencedTable == null) {
				throw new InvalidCaseException("When define the foreign key, referenced table MUST be already existed!");
			}
			
			// Set referenced table & column.
			key.setReferencedTable(referencedTable);
			key.setReferencedColumn(referencedTable.getColumn(MySqlConstantsUtil.getText(metadata.get(MySqlConstants.REFERENCED_COLUMN_NAME))));
			table.addForeignKey(key);
		}
		
		return tables;
	}
	
	public HashMap<String, Table> parse(List<HashMap<String, Object>> columnsMetaData, List<HashMap<String, Object>> constraintsMetaData, List<HashMap<String, Object>> foreignKeyMetaData) throws InvalidCaseException {
		this.parseForColumns(columnsMetaData);
		this.parseForConstraints(constraintsMetaData);
		this.parseForFK(foreignKeyMetaData);
		return tables;
	}
	
	/**
	 * Factory method to get metadata parser object.
	 * @return parser object.
	 */
	public static MetadataParser getParser(String host) {
		return new MetadataParser(host);
	}
	
	/**
	 * Factory method to get metadata parser object.
	 * @param 
	 * @return parser object.
	 */
	public static MetadataParser getParser(String host, HashMap<String, Table> tables) {
		return new MetadataParser(host, tables);
	}
}
