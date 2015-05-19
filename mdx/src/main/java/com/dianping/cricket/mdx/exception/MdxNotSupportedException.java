package com.dianping.cricket.mdx.exception;

import org.olap4j.OlapException;

public class MdxNotSupportedException extends OlapException {
	public MdxNotSupportedException(String msg) {
		super(msg);
	}
	
	public MdxNotSupportedException(Throwable throwable) {
		super(throwable);
	}

}
