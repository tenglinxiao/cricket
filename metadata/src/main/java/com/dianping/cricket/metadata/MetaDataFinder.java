package com.dianping.cricket.metadata;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dianping.cricket.dal.SessionStore;
import com.dianping.cricket.dal.conf.DBConfig;
import com.dianping.cricket.dal.conf.DBConfigs;
import com.dianping.cricket.metadata.conf.MetaDataConfiguration;

public class MetaDataFinder {
	private static Logger logger = Logger.getLogger(MetaDataFinder.class);
	// Store mapping.
	private HashMap<String, MetaDataStore> stores = new HashMap<String, MetaDataStore>();
	@Autowired
	private SessionStore sessionStore;
	
	public void init()
	{
		try {
			logger.info("Init metadata finder ...");
			DBConfigs dbs = MetaDataConfiguration.getConf().getDBs();
			for (DBConfig db : dbs.getDBs().values()) {
				MetaDataStore store = new MetaDataStore(db, sessionStore.getSessionFactory(db.getId()));
				
				// Init store via init method.
				store.init();
				
				// Store metadata store with db id.
				stores.put(db.getId(), store);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal("MetaDataFinder initailization failure: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public HashMap<String, MetaDataStore> getMetadataStores()
	{
		return stores;
	}
	
	
	public MetaDataStore getMetadataStore(String id)
	{
		return stores.get(id);
	}
	
    public static void main(String args[]) {
    	ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
    	context.getBean("metadataFinder");

    }

}
