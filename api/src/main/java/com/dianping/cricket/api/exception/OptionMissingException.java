package com.dianping.cricket.api.exception;

/**
 * Exception used to describe required options are missing when parse/extract for it under some cases.
 * @author uknow
 * @since 0.0.1
 */
public class OptionMissingException extends RuntimeException {
	public OptionMissingException(String msg) {
		super(msg);
	}
	
	public OptionMissingException(Throwable throwable) {
		super(throwable);
	}
}
