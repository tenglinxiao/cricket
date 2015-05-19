package com.dianping.cricket.api.cache;

import com.dianping.cricket.api.exception.InvalidCaseException;

public class RuntimeCacheBuilder implements CacheBuilder<RuntimeCache> {
	// Target cache to be built.
	private RuntimeCache cache = new RuntimeCache();
	
	private RuntimeCacheBuilder() {}
	
	@Override
	public RuntimeCacheBuilder config(CacheConfig config) {
		cache.setConfig(config);
		return this;
	}
	
	// Build sections for ttl/non-ttl cache.
	protected void buildSections() {
		cache.setupSections();
	}

	// Set up daemon threads.
	protected void buildSchedulers() {
		cache.setupThreads();
	}
	
	@Override
	public RuntimeCache build() {
		if (cache.getConfig() == null) {
			throw new InvalidCaseException("Config MUST be offered before build the cache! ");
		}
		buildSections();
		buildSchedulers();
		return cache;
	}
	
	public static RuntimeCacheBuilder newBuilder()
	{
		return new RuntimeCacheBuilder();
	}
}
