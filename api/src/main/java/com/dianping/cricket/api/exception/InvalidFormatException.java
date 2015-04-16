package com.dianping.cricket.api.exception;

/**
 * Exception used to describe the case that parameter or required option value is not defined in expected data format.
 * @author uknow
 * @since 0.0.1
 */
public class InvalidFormatException extends Exception {
	public InvalidFormatException(String msg) {
		super(msg);
	}
	
	public InvalidFormatException(Throwable throwable) {
		super(throwable);
	}
}
