package com.dianping.cricket.api.conf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.dianping.cricket.api.Dispatchable;
import com.dianping.cricket.api.Observable;
import com.dianping.cricket.api.Observer;
import com.dianping.cricket.api.event.Event;
import com.dianping.cricket.api.event.JsonEvent;
import com.dianping.cricket.api.exception.InvalidCaseException;

/**
 * Base Configuration class defined for loading config properties defined in json format config file. 
 * Refer to class GlobalConfiguration to figure out how to use this class.
 * Note: the register process is a MUST-HAVE step for using this api.
 * 
 * @author uknow
 * @since 0.0.1
 */
public class ConfigurationLoader implements Observable {
	public static final String PATH_DELIMITER = "/";
	public static final String CONFIG_PATH = "classpath*:/*.json";
	private static Logger logger = Logger.getLogger(ConfigurationLoader.class);
	// Singleton config loader obj.
	private static ConfigurationLoader loader;
	// Registered config obj.
	private HashMap<String, Configurable> configurables = new HashMap<String, Configurable>();
	// All config json nodes.
	private HashMap<Resource, JsonNode> confs = new HashMap<Resource, JsonNode>();

	private ConfigurationLoader() {};
	
	/**
	 * Load all the config files defined.
	 * @throws IOException 
	 */
	protected void loadConf() throws IOException {
		Resource resource = null;
		try {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			BufferedReader reader = null;
			
			// Load all the files located under root directory.
			Resource[] resources = resolver.getResources(CONFIG_PATH);
			for (int index = 0; index < resources.length; index++) {
				resource = resources[index];
				reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
				
				// Read json file and convert to json node.
				JsonNode conf = readJson(reader);
				
				// Put json config node into confs mapping.
				confs.put(resource, conf);
				
				logger.info("Done loading conf file from location: [" +  resource.getURL() + "]");
			}

		} catch(Exception e) {
			e.printStackTrace();
			logger.fatal("Configuration file load error: [" + resource.getURL() + "]");
			logger.fatal(e.getMessage());
			System.exit(1);
		}
		
	}
	
	// Read the json text from reader and convert to json obj.
	protected JsonNode readJson(BufferedReader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		String line = null;
		
		// Deal with the commented line and read the real config content.
		while ((line = reader.readLine()) != null) {
			if(!line.trim().startsWith("#")) {
				builder.append(line);
			}
		}
		reader.close();
		
		// Convert json text to json obj.
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(builder.toString());
	}

	public void addObserver(Observer observer)
	{
		// Not required at all, already has the list of observers.
	}
	
	public void notify(Event event) {
		JsonEvent e = (JsonEvent)event;
		if (configurables.containsKey(e.getNodeName())) {
			// Only notify the class that registered for this node kind.
			configurables.get(e.getNodeName()).doAction(event);
		}
	}
	
	public HashMap<String, Configurable> getConfs()
	{
		return configurables;
	}
	
	public static ConfigurationLoader getConfLoader() {
		if (loader == null) {
			loader = new ConfigurationLoader();
			try {
				loader.loadConf();
			} catch (IOException e) {
				e.printStackTrace();
				logger.fatal("Configuration file load error: " + e.getMessage());
				System.exit(1);
			}
		}
		return loader;
	}
	
	// Register the config obj and the watch node.
	public void register(Configurable conf, String watchNode) {
		if (configurables.containsKey(watchNode)) {
			logger.warn("Already registered an config class for watch node: [" + watchNode + "], the old one will be replaced!");
		}
		
		// Register the watch node.
		configurables.put(watchNode, conf);
		
		// Pick up the watch node from conf json nodes.
		parse(watchNode);
	}
	
	private void parse(String watchNode)
	{
		for (JsonNode conf : confs.values()) {
			JsonNode node = conf.findValue(watchNode);
			if (node != null) {
				notify(new JsonEvent(watchNode, node));
			}
		}
	}
}
