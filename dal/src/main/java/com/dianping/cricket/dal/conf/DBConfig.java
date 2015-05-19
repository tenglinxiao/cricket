package com.dianping.cricket.dal.conf;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.dianping.cricket.api.exception.InvalidFormatException;
import com.dianping.cricket.api.exception.InvalidOptionValueException;
import com.dianping.cricket.api.exception.OptionMissingException;

/**
 * Class holding db config data, which includes host, name, port, username & password.
 * @author tenglinxiao
 * @since 0.0.2
 */
public class DBConfig {
	public static final String ID = "id";
	public static final String HOST = "host"; 
	public static final String NAME = "name"; 
	public static final String PORT = "port"; 
	public static final String USERNAME = "username"; 
	public static final String PASSWORD = "password"; 
	
	protected JsonNode conf;
	private String id;
	private String host;
	private String name;
	private int port;
	private String username;
	private String password;
	
	public DBConfig(JsonNode conf) {
		this.conf = conf;
	}
	
	// Test whether the db config is valid.
	public boolean isValid(JsonNode defaultConfig) throws OptionMissingException, InvalidOptionValueException, InvalidFormatException {
		// If no default options offered, then check the specified options in db to ensure config file is legal.
		
		if (conf.get(ID) == null || conf.get(ID).getTextValue().trim().equals("")) {
			throw new OptionMissingException("Property [id] in db node is a MUST-HAVE option!");
		}
		
		// Check the host setting.
		if (defaultConfig.get(HOST) == null && conf.get(HOST) == null) {
			throw new OptionMissingException("Since no default [host] specified, host in db node is a MUST-HAVE option now!");
		} else if (conf.get(HOST) == null) {
			((ObjectNode)conf).put(HOST, defaultConfig.get(HOST));
		}

		// Check the port setting.
		if (defaultConfig.get(PORT) == null) {
			if (conf.get(PORT) == null) {
				throw new OptionMissingException("Since no default [port] specified, port in db node is a MUST-HAVE option now!");
				
			}
			if (conf.get(PORT).getIntValue() == 0) {
				throw new InvalidOptionValueException("Invalid port value detected for db [" + conf.get(PORT).getTextValue() + "]!");
			}
		} else if (conf.get(PORT) == null) {
			((ObjectNode)conf).put(PORT, defaultConfig.get(PORT));
		}
		
		// Check the username setting.
		if (defaultConfig.get(USERNAME) == null && conf.get(USERNAME) == null) {
			throw new OptionMissingException("Since no default [username] specified, username in db node is a MUST-HAVE option now!");
		} else if (conf.get(USERNAME) == null) {
			((ObjectNode)conf).put(USERNAME, defaultConfig.get(USERNAME));
		}
		
		// Check the password setting.
		if (defaultConfig.get(PASSWORD) == null && conf.get(PASSWORD) == null) {
			throw new OptionMissingException("Since no default [password] specified, password in db node is a MUST-HAVE option now!");
		} else if (conf.get(PASSWORD) == null) {
			((ObjectNode)conf).put(PASSWORD, defaultConfig.get(PASSWORD));
		}
		
		return true;
	}
	
	public void parse()
	{
		this.id = conf.get(ID).getTextValue();
		this.host =  conf.get(HOST).getTextValue();
		this.name =  conf.get(NAME).getTextValue();
		this.port = conf.get(PORT).getIntValue();
		this.username = conf.get(USERNAME).getTextValue();
		this.password = conf.get(PASSWORD).getTextValue();
	}
	
	public JsonNode getConf()
	{
		return conf;
	}

	public String getId() {
		return id;
	}

	public String getHost() {
		return host;
	}

	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
