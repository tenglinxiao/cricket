package com.dianping.cricket.dal.annotations;

public enum PersistMethod {
	LOAD, SAVE, UPDATE, DELETE;

	public String toString() {
		return super.toString().toLowerCase();
	}
}
