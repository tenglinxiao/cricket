package com.dianping.cricket.api;

/**
 * Interface for define class in visitor pattern.
 * @author uknow
 * @since 0.0.1
 */
public interface Visitor<T> {
	public void visit(T t);
}
