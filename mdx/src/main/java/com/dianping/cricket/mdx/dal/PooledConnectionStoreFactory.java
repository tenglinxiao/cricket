package com.dianping.cricket.mdx.dal;

import javax.sql.DataSource;

import com.dianping.cricket.mdx.conf.MdxConfiguration;


public class PooledConnectionStoreFactory {
	private static PooledConnectionStoreFactory factory;
	private PooledConnectionStoreFactory() {}
	
	private void load() {
	}
	
	
	public static PooledConnectionStoreFactory newFactory() {
		if (factory == null) {
			factory = new PooledConnectionStoreFactory();
			factory.load();
		}
		return factory;
	}
	
	public static PooledConnectionStore newConnectionStore(DataSource ds) {
		MdxConfiguration conf = MdxConfiguration.getMdxConfiguration();
		return new PooledConnectionStore(ds, conf.getCoreSize(), conf.getMaximumSize(), conf.getMaxIdleTime());
	}
}
