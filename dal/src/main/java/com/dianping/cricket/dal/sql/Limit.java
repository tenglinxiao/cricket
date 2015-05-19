package com.dianping.cricket.dal.sql;

/**
 * Describe the limit token.
 * @author tenglinxiao
 * @since 0.0.1
 */
public class Limit extends Token {
	private int size;

	public Limit(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "LIMIT " + size;
	}
}
