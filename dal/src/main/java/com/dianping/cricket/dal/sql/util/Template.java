package com.dianping.cricket.dal.sql.util;

public interface Template {
	// Apply template onto the target.
	public <T> T apply();
}
