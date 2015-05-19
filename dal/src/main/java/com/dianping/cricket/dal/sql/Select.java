package com.dianping.cricket.dal.sql;
import java.util.ArrayList;
import java.util.List;

public class Select extends Token {
	private List<Field> fields = new ArrayList<Field>();
	
	public Select() {}
	
	public Select(List<Field> fields)
	{
		this.fields = fields;
	}
	
	
	public Select field(Field field) {
		this.fields.add(field);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(); 
		builder.append("SELECT ");
		for (int index = 0; index < fields.size(); index++) {
			builder.append(fields.get(index).toString());
			if (index != fields.size() - 1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

}
