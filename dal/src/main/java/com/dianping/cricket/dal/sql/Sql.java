package com.dianping.cricket.dal.sql;

public class Sql extends Token {
	private Select select;
	private From from;
	private Where where;
	private GroupBy groupBy;
	private OrderBy orderBy;
	private Limit limit;
	private Offset offset;
	private int depth;
	public Sql() {}
	public Sql(Select select, From from) {
		this.select = select;
		this.from = from;
	}
	
	public Select getSelect() {
		return select;
	}
	public Sql select(Select select) {
		this.select = select;
		return this;
	}
	public From getFrom() {
		return from;
	}
	public Sql from(From from) {
		this.from = from;
		return this;
	}
	public Where getWhere() {
		return where;
	}
	public Sql where(Where where) {
		this.where = where;
		return this;
	}
	public GroupBy getGroupBy() {
		return groupBy;
	}
	public Sql groupBy(GroupBy groupBy) {
		this.groupBy = groupBy;
		return this;
	}
	public OrderBy getOrderBy() {
		return orderBy;
	}
	public Sql orderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
		return this;
	}
	public Limit getLimit() {
		return limit;
	}
	public Sql limit(Limit limit) {
		this.limit = limit;
		return this;
	}
	public Offset getOffset() {
		return offset;
	}
	public Sql offset(Offset offset) {
		this.offset = offset;
		return this;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	private String getIdentition() {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < depth * 4; index++) {
			builder.append(" ");
		}
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String identition = getIdentition();
		builder.append(identition + select + "\n");
		builder.append(identition + from + "\n");
		if (where != null) {
			builder.append(identition + where + "\n");
		}
		if (groupBy != null) {
			builder.append(identition + groupBy + "\n");
		}
		if (orderBy != null) {
			builder.append(identition + orderBy + "\n");
		}
		if (limit != null) {
			builder.append(identition + limit + "\n");
		}
		if (offset != null) {
			builder.append(identition + offset + "\n");
		}
		return builder.toString();
	}
}
