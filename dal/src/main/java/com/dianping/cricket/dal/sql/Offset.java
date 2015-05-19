package com.dianping.cricket.dal.sql;

/**
 * Describe the offset token.
 * @author tenglinxiao
 * @since 0.0.1
 */
public class Offset extends Token {
	private int size;
	
	public Offset(int size)
	{
		this.size = size;
	}
	
	public String toString() {
		return "OFFSET " + size;
	}

}
