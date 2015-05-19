package com.dianping.cricket.dal.conf;

import java.util.HashMap;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import com.dianping.cricket.api.exception.InvalidFormatException;
import com.dianping.cricket.api.exception.InvalidOptionValueException;
import com.dianping.cricket.api.exception.OptionMissingException;

public class DBConfigs extends DBConfig {
	// Property name for dbs settings.
	public static final String DBS = "dbs";
	private HashMap<String, DBConfig> dbs = new HashMap<String, DBConfig>();

	public DBConfigs(JsonNode conf) {
		super(conf);
	}

	public boolean isValid(JsonNode defaultConfig) throws OptionMissingException, InvalidFormatException, InvalidOptionValueException {
		if (conf == null) {
			throw new OptionMissingException("Option [dbs] is required for db config!");
		}
		
		if (!(conf instanceof ArrayNode)) {
			throw new InvalidFormatException("Option [dbs] must be defined in json array format!");
		}
		
		ArrayNode arr = (ArrayNode)conf;
		Iterator<JsonNode> iter = arr.iterator();
		while (iter.hasNext()) {
			DBConfig db = new DBConfig(iter.next());
			if (db.isValid(defaultConfig)) {
				dbs.put(db.getConf().get(ID).getTextValue(), db);
			}
		}
		
		return true;
	}

	public void parse() {
		for (DBConfig db : dbs.values()) {
			db.parse();
		}
	}
	
	public HashMap<String, DBConfig> getDBs() {
		return dbs;
	}
	
	public void addDBConfigs(DBConfigs dbConfigs) {
		HashMap<String, DBConfig> dbs = dbConfigs.getDBs();
		for (String id : dbs.keySet()) {
			this.dbs.put(id, dbs.get(id));
		}
	}

}
