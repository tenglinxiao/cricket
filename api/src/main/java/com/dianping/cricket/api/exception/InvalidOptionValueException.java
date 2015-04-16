package com.dianping.cricket.api.exception;

/**
 * Exception used to describe the case that the option value offered is not legal in our program 
 * based on the design principles or requirements.
 * @author uknow
 * @since 0.0.1
 */
public class InvalidOptionValueException extends Exception {
	public InvalidOptionValueException(String msg) {
		super(msg);
	}
	
	public InvalidOptionValueException(Throwable throwable) {
		super(throwable);
	}
}
