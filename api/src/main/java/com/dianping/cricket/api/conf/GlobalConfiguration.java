package com.dianping.cricket.api.conf;

import org.codehaus.jackson.JsonNode;

import com.dianping.cricket.api.cache.CacheConfig;
import com.dianping.cricket.api.exception.InvalidCaseException;
import com.dianping.cricket.api.mail.MailConf;

/**
 * Global configuration class for parsing & storing all the config data defined in "global" section. 
 * Regarding any system level config options, strongly suggest to define it in "global" section.
 * 
 * @author uknow
 * @since 0.0.1
 */
public class GlobalConfiguration extends Configurable {
	private static final String GLOBAL_CONF_NODE= "global";
	private static final String DEBUG = "debug";
	private static final String CACHE_DAEMONS_PER_SECTION = "cache.daemonsPerSection";
	private static final String CACHE_ENABLE_TTL = "cache.enableTTL";
	private static GlobalConfiguration conf;
	private boolean debug;
	private CacheConfig cacheConf;
	private MailConf mailConf;
	
	private GlobalConfiguration() {}
	
	public boolean isDebug() {
		return debug;
	}
	
	public static GlobalConfiguration getConf() {
		if (conf == null) {
			conf = new GlobalConfiguration();
		}
		
		return conf;
	}
	
	public CacheConfig getCacheConf() {
		return cacheConf;
	}
	
	public MailConf getMailConf() {
		return mailConf;
	}

	@Override
	public void parse(JsonNode data) throws InvalidCaseException {
		// Parse whether it's in debug mode.
		JsonNode node = getConfValue(DEBUG);
		if (node != null && (node.getBooleanValue() || Boolean.parseBoolean(node.getTextValue()))) {
			debug = true;
		}
		
		int daemonsPerSection = 0;
		boolean enableTTL = false;
		
		// Whether ttl feature is enabled.
		node = getConfValue(CACHE_ENABLE_TTL);
		if (node != null && (node.getBooleanValue() || Boolean.parseBoolean(node.getTextValue()))) {
			enableTTL = true;
		}
		
		// Parse daemons number per section.
		node = getConfValue(CACHE_DAEMONS_PER_SECTION);
		daemonsPerSection = node.getIntValue();
		
		cacheConf = CacheConfig.getConfig(daemonsPerSection, enableTTL);
		
		
		MailConf conf = MailConf.getConf();
		conf.setHost(getConfValue("mail.host").getTextValue());
		conf.setPort(getConfValue("mail.port").getIntValue());
		conf.setUsername(getConfValue("mail.username").getTextValue());
		conf.setPasswd(getConfValue("mail.password").getTextValue());
		conf.setSsl(getConfValue("mail.ssl").getBooleanValue());
		conf.setSender(getConfValue("mail.sender").getTextValue());
	}

	@Override
	protected String getWatchNode() {
		return GLOBAL_CONF_NODE;
	}
}
