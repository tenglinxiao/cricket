package com.dianping.cricket.dal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import com.dianping.cricke.api.conf.Configurable;
import com.dianping.cricke.api.conf.ConfigurationLoader;
import com.dianping.cricket.api.exception.InvalidFormatException;
import com.dianping.cricket.api.exception.OptionMissingException;

public class DALConfiguration extends Configurable {

	// Property name for db setings node.
	public static final String DB_SETTINGS = "db_settings";

	private static Logger logger = Logger.getLogger(DALConfiguration.class);
	
	private static DALConfiguration conf;
	// DB config.
	private DBConfigs confs;
	
	protected DALConfiguration() {}
	
	public static DALConfiguration getConf() {	
		if (conf == null) {
			conf = new DALConfiguration();
		}
		
		return conf;
	}
	
	public DBConfigs getDBs()
	{
		return confs;
	}
	

	@Override
	protected String getWatchNode() {
		return DB_SETTINGS;
	}

	@Override
	protected void parse(JsonNode data) {
		try {
			confs = new DBConfigs(data.get(DBConfigs.DBS));
			if (confs.isValid(data)) {
				confs.parse();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal("Fatal error encountered while parsing the db config file.");
			logger.fatal(e.getMessage());
			System.exit(1);
		}
	}

}
