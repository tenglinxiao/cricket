package com.dianping.cricket.api.cache;

/**
 * Interface to describe feature that the cache is mutable.
 * @author uknow
 * @since 0.0.1
 */
public interface Mutable<K, V> {
	/**
	 * Method to cache the key - value entry.
	 * @param key
	 * @param value
	 * @return old value for same key specified in the cache.
	 */
	public V put(K key, V value);
	/**
	 * Method to fetch the cached value.
	 * @param key
	 * @return value cached for the key.
	 */
	public V get(K key);
}
