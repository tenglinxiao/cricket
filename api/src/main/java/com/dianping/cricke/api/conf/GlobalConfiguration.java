package com.dianping.cricke.api.conf;

import org.codehaus.jackson.JsonNode;

import com.dianping.cricket.api.exception.InvalidCaseException;

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
	private static GlobalConfiguration conf;
	private boolean debug;
	
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

	@Override
	public void parse(JsonNode data) {
		try {
			// Parse whether it's in debug mode.
			JsonNode node = getConfValue(DEBUG);
			if (node != null && (node.getBooleanValue() || Boolean.parseBoolean(node.getTextValue()))) {
				debug = true;
			}
		} catch (InvalidCaseException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected String getWatchNode() {
		return GLOBAL_CONF_NODE;
	}
}
