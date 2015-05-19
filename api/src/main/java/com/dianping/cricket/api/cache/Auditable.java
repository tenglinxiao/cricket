package com.dianping.cricket.api.cache;

/**
 * Describe feature that the cache is auditable.
 * @author uknow
 * @since 0.0.1
 */
public interface Auditable {
	// Get cached total size.
	public long getTotalSize();
	
	// Get the records number.
	public int getCount();

}
