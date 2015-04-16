package com.dianping.cricket.metadata;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.dianping.cricket.dal.DBConfig;

public abstract class MetaDataLoader implements Runnable {
	// DB config data.
	protected DBConfig conf;
	// Schedule time unit.
	private TimeUnit timeUnit;
	// Schedule time period number.
	private long period;
	// Event queue for dealing with metadata changes.
	private Queue<MetaDataEvent> eventQueue = new LinkedList<MetaDataEvent>();  
	
	private SqlSessionFactory sessionFactory;
	
	protected MetaDataLoader(DBConfig conf, SqlSessionFactory sessionFactory) {
		this.conf = conf;
		this.sessionFactory = sessionFactory;
		this.timeUnit = MetaDataConfiguration.getConf().getTimeUnit();
		this.period = MetaDataConfiguration.getConf().getPeriod();
	}
	
	protected void init() {
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this, period, period, timeUnit);
	}
	
	public abstract boolean checkSum();
	public abstract void create(MetaDataEvent event);
	public abstract void delete(MetaDataEvent event);
	public abstract void update(MetaDataEvent event);
	
    // Take the actions due to events
    public void doAction(MetaDataEvent event) {
    	if (event.match(MetaDataEvent.MetaDataEventType.TABLE_CREATE)) {
    		create(event);
    	} else if (event.match(MetaDataEvent.MetaDataEventType.TABLE_DELETE)) {
    		delete(event);
    	} else if (event.match(MetaDataEvent.MetaDataEventType.TABLE_UPDATE)) {
    		update(event);
    	}
    }
    
    public void run() {
    	if (!checkSum()) {
    		MetaDataEvent event = null;
    		while ((event = eventQueue.poll()) != null) {
    			doAction(event);
    		}
    	}
    }
    
    public SqlSession openSession()
    {
    	return sessionFactory.openSession();
    }
}
