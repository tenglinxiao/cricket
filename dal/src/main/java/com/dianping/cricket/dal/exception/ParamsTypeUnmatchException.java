package com.dianping.cricket.dal.exception;

/**
 * Exception to describe the data type that is not matched with expected.
 * @author uknow
 * @since 0.0.1
 */
public class ParamsTypeUnmatchException extends RuntimeException {
	public ParamsTypeUnmatchException(String msg) {
		super(msg);
	}
	
	public ParamsTypeUnmatchException(Throwable throwable) {
		super(throwable);
	}

}
