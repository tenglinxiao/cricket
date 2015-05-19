package com.dianping.cricket.dal.util;

import com.dianping.cricket.dal.misc.DataType;
import com.dianping.cricket.dal.misc.DataType.Type;
import com.dianping.cricket.dal.sql.Field;

public class DataTypeConverter {
	public static Type convertJavaType2DataType(Object obj) {
		String clsName = obj.getClass().getName().toUpperCase();
		switch (clsName) {
		case "STRING": 
			return DataType.Text.ANY;
		case "INT":
		case "INTEGER":
		case "DOUBLE":
		case "FLOAT":
		case "SHORT":
		case "LONG":
		case "CHAR":
		case "BOOLEAN":
		case "BYTE":
			return DataType.Number.ANY;
		case "DATE":
			return DataType.DateTime.ANY;
		default:
			if (clsName.contains("TEXT") || clsName.contains("CHAR"))
				return DataType.Text.UNKNOWN;
			else if (clsName.contains("DATE") || clsName.contains("TIME")) 
				return DataType.DateTime.UNKNOWN;
			return DataType.Virtual.UNKNOWN;
		}
	}

}
