package com.dianping.cricket.api.cache;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class RuntimeCache extends AbstractAuditable {
	private static Logger logger = Logger.getLogger(RuntimeCache.class);
	// Singleton cache instance.
	private static RuntimeCache cache = null;
	// Cache config instance.
	private CacheConfig config = null;
	// Executor service instance.
	private ScheduledExecutorService service = null;
	// Cached data no need to be refreshed.
	private ConcurrentHashMap<CacheKey<?>, CacheValue<?, ?>> data = new ConcurrentHashMap<CacheKey<?>, CacheValue<?, ?>>();
	// Cache data has expired time. 
	private ConcurrentHashMap<TimeUnit, CacheValue<CacheKey<?>, CacheValue<?, ?>>> ttlData = new ConcurrentHashMap<TimeUnit,  CacheValue<CacheKey<?>, CacheValue<?, ?>>>();
	// Bind obj for thread sync.
	private Object flag = new Object(); 
	
	static {
		// Start the underlying cache daemons.
		RuntimeCache.getCache().start();
	}
	
	public class DaemonRunnable implements Runnable {
		private TimeUnit unit;
		private int counter = 0;
		public DaemonRunnable(TimeUnit unit)
		{
			this.unit = unit;
		}

		@Override
		public void run() {
			while(true) {
				// Be notified every min.
				synchronized(flag) {
					try {
						flag.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				boolean run = false;
				counter++;

				// Check run the check or not depends on time unit.
				if (unit == TimeUnit.MINUTES) {
					run = true;
				} else if (unit == TimeUnit.HOURS && counter % 60 == 0 ) {
					run = true;
				} else if (unit == TimeUnit.DAYS && counter % 1440 == 0) {
					run = true;
				}
				
				// Check the corresponding data set and remove the expired data.
				if (run) {
					logger.info("Run data expiry check for time unit [" + unit + "]...");
					CacheValue<CacheKey<?>, CacheValue<?, ?>> values = RuntimeCache.this.ttlData.get(unit);
					for (CacheKey<?> key : values.keySet()) {
						// Check whether is expired according to ttl.
						if (key.isExpired()) {
							values.remove(key);
							logger.info("Remove cached data due to expiry: [" + key.toString() + "]");
						}
					}
					logger.info("End data expiry check for time unit [" + unit + "]...");
					counter = 0;
				}
			}
		}
	}
	
	public CacheConfig getConfig() {
		return config;
	}

	public void setConfig(CacheConfig config) {
		this.config = config;
	}
	
	public void setupSections()
	{
		if (config.isEnableTTL()) {
			// Init the data set for cache data belongs to different time unit according to the TTL.
			ttlData.put(TimeUnit.MINUTES, new CacheValue<CacheKey<?>, CacheValue<?, ?>>());
			ttlData.put(TimeUnit.HOURS, new  CacheValue<CacheKey<?>, CacheValue<?, ?>>());
			ttlData.put(TimeUnit.DAYS, new  CacheValue<CacheKey<?>, CacheValue<?, ?>>());
			logger.info("Set up sections for ttl data.");
		}
	}
	
	public void setupThreads() {
		service = Executors.newScheduledThreadPool(config.getDaemonsPerSection() * 3 + 1);
		
		if (config.isEnableTTL()) {
			// Create daemon threads for refresh cache data.
			for (int index = 0; index < config.getDaemonsPerSection(); index++) {
				service.execute(new DaemonRunnable(TimeUnit.MINUTES));
				service.execute(new DaemonRunnable(TimeUnit.HOURS));
				service.execute(new DaemonRunnable(TimeUnit.DAYS));
			}
			logger.info("Set up daemon threads for data expiry check ...");
		}
	}
	
	private void start()
	{
		// Regularly run one thread as time clock for daemon threads.
		service.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				synchronized(flag) {
					flag.notifyAll();
				}
			}
		}, 0, 1, TimeUnit.MINUTES);
		logger.info("Time clock to notify threads ...");
	}
	
	public void addCacheData(CacheKey<?> key, CacheValue<?, ?> value)
	{
		// If the ttl is -1, then add it to the non-expired cache section, 
		// eitherwise to the section based on the timeunit.
		if (key.getTtl() == -1) {
			this.data.put(key, value);
		} else {
			this.ttlData.get(key.getUnit()).put(key, value);
		}
	}
	
	public CacheValue<?, ?> getCacheData(CacheKey<?> key, TimeUnit unit)
	{
		if (unit == null) {
			return this.data.get(key);
		}
		return this.ttlData.get(unit).get(key);
	}
	
	public void destroy()
	{
		// Shut down the thread pool service.
		service.shutdownNow();
	}
	
	@Override
	public long getTotalSize() {
		int size = 0;
		synchronized (data) {
			for (CacheKey<?> key : data.keySet()) {
				size += key.getTotalSize() + data.get(key).getTotalSize();
			}
		}
		synchronized (ttlData) {
			Iterator<CacheValue<CacheKey<?>, CacheValue<?, ?>>> iter = ttlData.values().iterator();
			while (iter.hasNext()) {
				size += iter.next().getTotalSize();
			}	
		}
		return size;
	}
	
	@Override
	public int getCount() {
		return getDataCount() + getTtlDataCount();
	}
	
	public int getDataCount() {
		return data.size();
	}
	
	public int getTtlDataCount() {
		int count = 0;
		synchronized (ttlData) {
			Iterator<CacheValue<CacheKey<?>, CacheValue<?, ?>>> iter = ttlData.values().iterator();
			while (iter.hasNext()) {
				count += iter.next().getCount();
			}	
		}
		return count;
	}

	public static RuntimeCache getCache()
	{
		if (cache == null) {
			cache = RuntimeCacheBuilder.newBuilder().config(CacheConfig.getConfig()).build();
		}
		return cache;
	}
}
