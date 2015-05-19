package com.dianping.cricket.dal.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dianping.cricket.dal.misc.DataType;
import com.dianping.cricket.dal.misc.Function;

public class Test {
	public static Sql get(){
		Select select = new Select();
		select.field(new Field("a", DataType.Number.INT));
		select.field(new Field("b", DataType.Number.INT));
		Expression expression = new Expression("a", DataType.Number.INT,"{f:count} * {f:sum}");
		expression.addReferencedField(new Field("b", DataType.Number.INT));
		select.field(expression);
		From from = new From("A");
		Sql sql = new Sql(select, from);
		return sql;
	}
	public static void main(String args[]) {
		Sql sql = get();
		Sql s = get();
		s.getFrom().setFromSql(sql);
		
		//from.setFromSql(sql);
		System.out.println(s);
		
	}
}
