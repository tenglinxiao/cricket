package com.dianping.cricket.api.cache.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Redis connection pool
 *
 * @author uknow
 * @since 0.0.1
 */
public class RedisConnectionPool implements Runnable {
    private static Logger logger = Logger.getLogger(RedisConnectionPool.class);
    private static RedisConnectionPool pool = null;
    // Redis host.
    private String host;
    // Redis port.
    private int port = 6379;
    // Redis minimum size.
    private int coreSize;
    // Redis maximum size.
    private int maxSize;
    // Connection pool.
    private List<RedisConnection> connectionPool = new ArrayList<RedisConnection>();
    // Schedule service.
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    private RedisConnectionPool(String host, int port, int coreSize, int maxSize) {
        this.host = host;
        this.port = port;
        this.coreSize = coreSize;
        this.maxSize = maxSize;
    }

    private void startDaemon() {
        service.scheduleAtFixedRate(this, 0, 5, TimeUnit.MINUTES);
    }

    public synchronized void run() {
        logger.info("Run redis connection pool cleanup process!");
        if (connectionPool.size() <= coreSize) {
            logger.info("Abort redis cleanup process because size is not up to the constraints!");
            return;
        }

        // Clean up the connection if necessary.
        for (int index = connectionPool.size() - 1; index >= 0; index--) {
            if (connectionPool.size() > coreSize) {
                RedisConnection connection = connectionPool.get(index);
                if (connection.isExpired() && connection.isIdle()) {
                    connectionPool.remove(index);
                }
            } else {
                break;
            }
        }

        logger.info("Done redis running connection pool cleanup process");
    }

    public synchronized RedisConnection getConnection() {
        RedisConnection connection = null;

        // Try to acquire connection from connection pool.
        for (int index = 0; index < connectionPool.size(); index++) {
            if ((connection = connectionPool.get(index).use()) != null) {
                return connection;
            }
        }

        // If all are used, and not up to the limit max size.
        // add one connection in the queue.
        if (maxSize > connectionPool.size()) {
            connectionPool.add(new RedisConnection(this, this.host, this.port));

            // Acquire the last added semaphore, and then
            if ((connection = connectionPool.get(connectionPool.size() - 1).use()) != null) {
                return connection;
            }
        }

        // If can't acquire wanted connection, wait util it's ready.
        try {
            this.wait();
            return this.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void release(RedisConnection connection) {
        // Release connection and release the semaphore as well.
        for (int index = 0; index < connectionPool.size(); index++) {
            if (connectionPool.get(index) == connection) {
                connection.release();
            }
        }
        this.notifyAll();
    }

    public static RedisConnectionPool getConnectionPool(RedisConfig config) {
        synchronized (RedisConnectionPool.class) {
            if (pool == null) {
                pool = new RedisConnectionPool(config.getHost(), config.getPort(), config.getCoreSize(), config.getMaxSize());
                pool.startDaemon();
            }
        }
        return pool;
    }

    public static RedisConnectionPool getConnectionPool() {
        return pool;
    }

    public void close() {
        this.service.shutdownNow();
        for (RedisConnection c : connectionPool) {
            c.close();
        }
    }
}
