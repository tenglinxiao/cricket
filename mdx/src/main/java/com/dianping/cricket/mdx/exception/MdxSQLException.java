package com.dianping.cricket.mdx.exception;

import java.sql.SQLException;

public class MdxSQLException extends SQLException {
	public MdxSQLException(String msg) {
		super(msg);
	}
	
	public MdxSQLException(Throwable throwable) {
		super(throwable);
	}
}
