package com.dianping.cricket.mdx.exception;

import java.sql.SQLException;

public class MdxSQLNotSupportedException extends SQLException {
	public MdxSQLNotSupportedException() {
		super("Not required!");
	}
	
	public MdxSQLNotSupportedException(String msg) {
		super(msg);
	}
	
	public MdxSQLNotSupportedException(Throwable throwable) {
		super(throwable);
	}

}
