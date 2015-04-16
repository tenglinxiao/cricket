package com.dianping.cricket.api.exception;

/**
 * Exception used to describe the case that the op/feature asked is not supported.
 * @author uknow
 * @since 0.0.1
 */
public class NotSupportedException extends Exception {
	public NotSupportedException(String msg) {
		super(msg);
	}
	
	public NotSupportedException(Throwable throwable) {
		super(throwable);
	}
}
