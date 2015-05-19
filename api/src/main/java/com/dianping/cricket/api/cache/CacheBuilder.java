package com.dianping.cricket.api.cache;

public interface CacheBuilder<T> {
	/**
	 * Set the config for the cache wants to be built.
	 * @param config
	 * @return cachebuiler.
	 */
	public CacheBuilder<T> config(CacheConfig config);
	
	/**
	 * Complete building the cache and hand over the cache.
	 * @return cache.
	 */
	public T build();
}
