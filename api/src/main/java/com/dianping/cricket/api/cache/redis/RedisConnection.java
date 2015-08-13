package com.dianping.cricket.api.cache.redis;

import redis.clients.jedis.Jedis;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 * @author uknow.
 * @since 0.0.1
 */
public class RedisConnection extends Jedis {
    private Date createdTime;
    private Date lastAccessTime;
    private Semaphore semaphore = new Semaphore(1);

    public RedisConnection(String host, int port) {
        super(host, port);
        this.createdTime = Calendar.getInstance().getTime();
        this.lastAccessTime = this.createdTime;
    }

    public synchronized RedisConnection use() {
        if (semaphore.tryAcquire()) {
            this.lastAccessTime = Calendar.getInstance().getTime();
            return this;
        }
        return null;
    }

    public boolean isExpired() {
        return (Calendar.getInstance().getTimeInMillis() - lastAccessTime.getTime()) / 1000 > 300;
    }

    public boolean isIdle() {
        return semaphore.availablePermits() > 0;
    }

    public synchronized void release() {
        semaphore.release();
    }
}
