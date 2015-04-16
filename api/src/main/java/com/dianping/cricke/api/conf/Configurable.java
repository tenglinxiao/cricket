package com.dianping.cricke.api.conf;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.dianping.cricket.api.Event;
import com.dianping.cricket.api.JsonEvent;
import com.dianping.cricket.api.Observer;
import com.dianping.cricket.api.exception.InvalidCaseException;

public abstract class Configurable implements Observer {
	private static Logger logger = Logger.getLogger(Configurable.class);
	private JsonNode configuration;
	
	protected Configurable(){
		init();
	}
	
	// Method to init the config params defined in the config file.
	protected void init() {
		ConfigurationLoader.getConfLoader().register(this, getWatchNode());
	}
	
	protected abstract String getWatchNode();
	
	protected abstract void parse(JsonNode data) throws Exception;
	
    public void doAction(Event event) {
    	JsonEvent e = (JsonEvent)event;
    	// Check whether this matches the watch node we registered.
    	if (e.match(getWatchNode())) {
    		// Get the event data passed.
    		configuration = (JsonNode)event.getData();
    		try {
    			// Parse the configuration to validate & extract data wanted.
				parse(configuration);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.fatal("Configuration error when parse json node [" + e.getNodeName() + "]!");
				System.exit(1);
			}
    	}
    }
    
	// Get config property value defined in json.
	public JsonNode getConfValue(String property) throws InvalidCaseException {
		String[] splits = property.split("\\.");
		JsonNode node = configuration;
		for (int index = 0; index < splits.length; index++) {
			if (node == null) {
				throw new InvalidCaseException("Config option [" +property +"] CAN NOT be find in the config files.");
			}
			node = node.get(splits[index]);
		}
		return node;
	}
}
