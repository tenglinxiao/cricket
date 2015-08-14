package com.dianping.cricket.api.authority;

import com.dianping.cricket.api.cache.redis.RedisConnectionPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by tenglinxiao on 12/8/15.
 */
public class SSOContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        RedisConnectionPool.getConnectionPool(SSOConfiguration.getConf().getRedisConfig());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        RedisConnectionPool.getConnectionPool().close();
    }
}
