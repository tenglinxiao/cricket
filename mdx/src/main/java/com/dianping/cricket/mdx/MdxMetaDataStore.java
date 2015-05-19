package com.dianping.cricket.mdx;

import java.util.concurrent.ConcurrentHashMap;

public class MdxMetaDataStore {
	private ConcurrentHashMap<String, MdxCatalog> store = new ConcurrentHashMap<String, MdxCatalog>();
	
	public void load() {
		
	}
	
	public MdxCatalog getCatalog(String name) {
		return store.get(name);
	}

}
