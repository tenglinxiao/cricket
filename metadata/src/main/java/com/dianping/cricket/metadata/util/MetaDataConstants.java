package com.dianping.cricket.metadata.util;

/**
 * Class defining the constants used for metadata parsing and utility class can do a favor to metadata building.
 * @author tenglinxiao
 * @since 1.0
 */
public class MetaDataConstants {
	// Constants class.
	public static class MySqlConstants {
		public final static String TABLE_SCHEMA = "table_schema";
		public final static String TABLE_NAME = "table_name";
		public final static String COLUMN_NAME = "column_name";
		public final static String REFERENCED_TABLE_NAME = "referenced_table_name";
		public final static String REFERENCED_COLUMN_NAME = "referenced_column_name";
		public final static String COLUMN_COMMENT = "column_comment";
		public final static String COLUMN_DEFAULT = "column_default";
		public final static String IS_NULLABLE = "is_nullable";
		public final static String DATA_TYPE = "data_type";
		public final static String CHARACTER_MAXIMUM_LENGTH = "character_maximum_length";
		public final static String CONSTRAINT_NAME = "constraint_name";
		public final static String CONSTRAINT_TYPE = "constraint_type";
		public final static String COLUMN_KEY = "column_key";
		
		public final static String PRIMARY_KEY = "primary key";
		public final static String PRIMARY_KEY_NAME = "primary";
		public final static String UNIQUE_KEY = "unique";

		public static enum KEY_TYPE {PRIMARY, UNIQUE, KEY};
	}
	
	// Constants utility class.
	public static class MySqlConstantsUtil {
		
		/**
		 * To determine whether the value indicating nullable or not.
		 * @param isNullable
		 * 		  value for nullable.
		 * @return whether it's nullable.
		 */
		public static boolean isNullable(Object isNullable) {
			if (isNullable == null) {
				return false;
			}
			
			return isNullable.toString().equalsIgnoreCase("NO")? false: true;
		}
		
		/**
		 * Get text format for the passed in value.
		 * @param text 
		 * 		  object will be converted to string.
		 * @return text format data.
		 */
		public static String getText(Object text) {
			return text == null? "": text.toString();
		}
		
		/**
		 * Get number format value for the passed in object.
		 * @param text
		 * 		  object be converted.
		 * @return number value be converted to.
		 */
		public static int getNumber(Object text) {
			return text == null? 0: Integer.parseInt(text.toString());
		}
		
		/**
		 * Get the key type according to the passed in key object.
		 * @param key 
		 * 		  key object
		 * @return key type.
		 */
		public static MySqlConstants.KEY_TYPE getConstraintType(Object key)
		{
			if (key == null) {
				return MySqlConstants.KEY_TYPE.KEY;
			}
			if (MySqlConstants.PRIMARY_KEY.equalsIgnoreCase(key.toString())) {
				return MySqlConstants.KEY_TYPE.PRIMARY;
			} else if (MySqlConstants.UNIQUE_KEY.equalsIgnoreCase(key.toString())) {
				return MySqlConstants.KEY_TYPE.UNIQUE;
			} 
			return MySqlConstants.KEY_TYPE.KEY;
		}
	}

}
