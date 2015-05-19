package com.dianping.cricket.dal.conf;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.dianping.cricket.api.conf.Configurable;
import com.dianping.cricket.api.exception.InvalidFormatException;
import com.dianping.cricket.api.exception.InvalidOptionValueException;
import com.dianping.cricket.api.exception.OptionMissingException;

public class DALConfiguration extends Configurable {
	// Property name for db settings node.
	public static final String DB_SETTINGS = "db_settings";

	private static Logger logger = Logger.getLogger(DALConfiguration.class);
	
	private static DALConfiguration conf;
	// DB config.
	private DBConfigs confs;
	
	protected DALConfiguration() {}
	
	public static DALConfiguration getConf() {	
		synchronized (DALConfiguration.class) {
			if (conf == null) {
				conf = new DALConfiguration();
			}
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
	protected void parse(JsonNode data) throws OptionMissingException, InvalidFormatException, InvalidOptionValueException {
		DBConfigs confs = new DBConfigs(data.get(DBConfigs.DBS));
		if (confs.isValid(data)) {
			confs.parse();
		}
		
		if (this.confs == null) {
			this.confs = confs;
		} else {
			this.confs.addDBConfigs(confs);
		}
	}

}
