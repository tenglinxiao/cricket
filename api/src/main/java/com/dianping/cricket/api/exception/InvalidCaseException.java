package com.dianping.cricket.api.exception;

/**
 * Exception used to describe the case that program encountered an scenario that should never happen.
 * @author uknow
 * @since 0.0.1
 */
public class InvalidCaseException extends RuntimeException {
	public InvalidCaseException(String msg) {
		super(msg);
	}
	
	public InvalidCaseException(Throwable throwable) {
		super(throwable);
	}
}
