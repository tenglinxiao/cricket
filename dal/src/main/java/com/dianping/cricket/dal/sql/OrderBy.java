package com.dianping.cricket.dal.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe order by sql token.
 * @author tenglinxiao
 * @since 0.0.1
 */
public class OrderBy extends Token {
	private List<Field> fields = new ArrayList<Field>();
	private List<Order> orders = new ArrayList<Order>();
	
	public OrderBy() {}
	
	// Append new order field with specified order.
	public OrderBy append(Field field, Order order) {
		this.fields.add(field);
		this.orders.add(order);
		return this;
	}
	
	// Append new order field.
	public OrderBy append(Field field) {
		this.fields.add(field);
		this.orders.add(Order.ASC);
		return this;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < fields.size(); index++) {
			builder.append(fields.get(index));
			builder.append(" ");
			builder.append(orders.get(index));
			
			if (index != fields.size() - 1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}
	
	public static enum Order {
		ASC, DESC
	}
}
