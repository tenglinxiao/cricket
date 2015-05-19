package com.dianping.cricket.metadata.hive;

public interface LazyLoadable {
	public boolean isLoaded();
	public void lazyLoad();
}
