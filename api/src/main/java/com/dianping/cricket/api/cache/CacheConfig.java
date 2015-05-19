package com.dianping.cricket.api.cache;

import org.apache.log4j.Logger;

public class CacheConfig {
	private static Logger logger = Logger.getLogger(CacheConfig.class);
	private static CacheConfig config = null;
	// Daemons used for data refresh.
	private int daemonsPerSection = 1;
	// Whether enable ttl feature.
	private boolean enableTTL = true;
	
	private CacheConfig() {}
	
	private CacheConfig(int daemonsPerSection, boolean enableTTL) {
		this.daemonsPerSection = daemonsPerSection;
		this.enableTTL = enableTTL;
	}
	
	public int getDaemonsPerSection() {
		return daemonsPerSection;
	}
	
	public boolean isEnableTTL() {
		return enableTTL;
	}
	
	// Singleton instance method.
	public static CacheConfig getConfig()
	{
		if (config == null) {
			config = new CacheConfig();
		} 
		return config;
	}
	
	// Singleton instance method.
	public static CacheConfig getConfig(int daemonsPerSection, boolean enableTTL)
	{
		if (config == null) {
			config = new CacheConfig(daemonsPerSection, enableTTL);
		} 
		
		return config;
	}
}
