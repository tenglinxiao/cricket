package com.dianping.sso;

import com.dianping.cricket.api.cache.redis.RedisConfig;
import com.dianping.cricket.api.conf.Configurable;
import org.codehaus.jackson.JsonNode;

/**
 * @author uknow
 * @since 0.0.1
 * Created by tenglinxiao on 13/8/15.
 */
public class SSOConfiguration extends Configurable {
    public final static String WATCH_NODE = "sso";
    private static SSOConfiguration conf;
    public RedisConfig redisConfig;

    @Override
    protected String getWatchNode() {
        return WATCH_NODE;
    }

    @Override
    protected void parse(JsonNode jsonNode) throws Exception {
        redisConfig = new RedisConfig();
        redisConfig.setHost(this.getConfValue("redis.host").getTextValue());
        redisConfig.setPort(this.getConfValue("redis.port").getIntValue());
        redisConfig.setCoreSize(this.getConfValue("redis.pool.coreSize").getIntValue());
        redisConfig.setMaxSize(this.getConfValue("redis.pool.maxSize").getIntValue());
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public static SSOConfiguration getConf() {
        synchronized (SSOConfiguration.class) {
            if (conf == null) {
                conf = new SSOConfiguration();
            }
            return conf;
        }
    }
}
