package com.dianping.cricket.dal.exception;

public class SqlFailedException extends RuntimeException {
	public SqlFailedException(String msg) {
		super(msg);
	}
	
	public SqlFailedException(Throwable throwable) {
		super(throwable);
	}
}
