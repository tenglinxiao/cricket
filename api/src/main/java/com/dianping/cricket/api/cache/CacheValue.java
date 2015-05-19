package com.dianping.cricket.api.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CacheValue<K, V> extends AbstractAuditable implements Immutable<K, V> {
	// Cached data.
	private Map<K, V> data = null;
	// Whether the data cached is mutable.
	private boolean mutable = false;
	
	public CacheValue() {
		data = new HashMap<K, V>();
	}
	
	public CacheValue(boolean mutable) {
		// According the fact whether cache is mutable, create different cache instance.
		if (mutable) {
			data = new ConcurrentHashMap<K, V>();
		} else {
			data = new HashMap<K, V>();
		}
	}

	@Override
	public V put(K key, V value) {
		V existed = data.put(key, value);
		// If data is immutable & cache key already existed, throw exception.
		if (!mutable && existed != null) {
			throw new RuntimeException("Can not update immutable cache with new value!");
		}
		return existed;
	}

	@Override
	public V get(K key) {
		return data.get(key);
	}
	
	public void remove(K key)
	{
		this.data.remove(key);
	}
	
	public boolean containsKey(K key)
	{
		return this.data.containsKey(key);
	}
	
	public Set<K> keySet()
	{
		return data.keySet();
	}

	@Override
	public int getCount() {
		if (data == null) {
			return 0;
		}
		return data.size();
	}
}
