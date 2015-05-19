package com.dianping.cricket.dal.exception;

/**
 * Used to describe exception that passed params are not enough.
 * @author uknow
 * @since 0.0.1
 */
public class ParamsNotEnoughException extends RuntimeException {
	public ParamsNotEnoughException(String msg) {
		super(msg);
	}
	
	public ParamsNotEnoughException(Throwable throwable) {
		super(throwable);
	}

}
