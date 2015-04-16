package com.dianping.cricket.api.exception;

public class OptionMissingException extends Exception {
	public OptionMissingException(String msg) {
		super(msg);
	}
	
	public OptionMissingException(Throwable throwable) {
		super(throwable);
	}
}
