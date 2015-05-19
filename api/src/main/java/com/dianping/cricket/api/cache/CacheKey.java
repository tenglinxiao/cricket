package com.dianping.cricket.api.cache;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CacheKey<T> extends HashKey<T> implements Expireable {
	// Time unit for ttl.
	private TimeUnit unit = TimeUnit.MINUTES;
	// TTL time.
	private int ttl = -1;
	// Key created time.
	private Date createdTime;
	
	public CacheKey(T key) {
		super(key);
		this.createdTime = Calendar.getInstance().getTime();
	}
	
	public CacheKey(T key, int ttl, TimeUnit unit) {
		super(key);
		this.ttl = ttl;
		this.unit = unit;
		this.createdTime = Calendar.getInstance().getTime();
	}
	
	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public boolean isExpired() {
		// Never be expired if the ttl is set to -1.
		if (ttl == -1) {
			return false;
		}
		long milliseconds = Calendar.getInstance().getTime().getTime();
		if (unit == TimeUnit.MINUTES) {
			if (milliseconds - createdTime.getTime() > 60 * 1000 * ttl)
			{
				return true;
			}
		} else if (unit == TimeUnit.HOURS) {
			if (milliseconds - createdTime.getTime() > 3600 * 1000 * ttl)
			{
				return true;
			}
		} if (unit == TimeUnit.DAYS) {
			if (milliseconds - createdTime.getTime() > 24 * 3600 * 1000 * ttl)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public int getCount() {
		return 1;
	}
}
