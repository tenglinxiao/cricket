package com.dianping.cricket.api.cache;

public interface Hashable {
	/**
	 * Generate the hash key.
	 * @return hash key.
	 */
	public int hashKey();
	
	/**
	 * Determine whether two object are equal.
	 * @param obj
	 * @return whether equals.
	 */
	public boolean hashEquals(Object obj);
	
	/**
	 * Indicate overriding the hashCode function is necessary in the subclass.
	 * @return hash code.
	 */
	public int hashCode();
	
	/**
	 * Indicate overriding the hashCode function is necessary in the subclass.
	 * @param obj
	 * @return whether equals.
	 */
	public boolean equals(Object obj);
}
