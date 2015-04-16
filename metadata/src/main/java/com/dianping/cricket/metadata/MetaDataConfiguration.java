package com.dianping.cricket.metadata;

import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonNode;

import com.dianping.cricke.api.conf.Configurable;
import com.dianping.cricke.api.conf.ConfigurationLoader;
import com.dianping.cricket.api.exception.InvalidFormatException;
import com.dianping.cricket.api.exception.InvalidOptionValueException;
import com.dianping.cricket.api.exception.OptionMissingException;
import com.dianping.cricket.dal.DALConfiguration;
import com.dianping.cricket.dal.DBConfigs;

/**
 * Class defined to hold all the config options for "metadata" section.
 * @author uknow
 * @since 0.0.1
 */
public class MetaDataConfiguration extends Configurable {
	private static final String CONFIG_PATH = "metadata";
	private static final String TIME_UNIT = "timeunit";
	private static final String PERIOD = "period";
	private static MetaDataConfiguration conf;
	private TimeUnit timeUnit = TimeUnit.MINUTES;
	private long period = 10;
	private DBConfigs dbs;
	
	protected MetaDataConfiguration() {}

	@Override
	protected String getWatchNode() {
		return CONFIG_PATH;
	}
	
	public static MetaDataConfiguration getConf()
	{
		if (conf == null) {
			conf = new MetaDataConfiguration();
		}
		
		return conf;
	}

	@Override
	protected void parse(JsonNode data) throws InvalidOptionValueException, OptionMissingException, InvalidFormatException {
		if (data.get(TIME_UNIT) != null) {
			if (data.get(TIME_UNIT).getTextValue().toUpperCase().equals(TimeUnit.MINUTES.toString())) {
				timeUnit = TimeUnit.MINUTES;
			} else if (data.get(TIME_UNIT).getTextValue().toUpperCase().equals(TimeUnit.HOURS.toString())) {
				timeUnit = TimeUnit.HOURS;
			} else {
				throw new InvalidOptionValueException("Metadata [timeunit] config can only be defined with [minutes/hours]");
			}
		}
		
		if (data.get(PERIOD) != null) {
			period = data.get(PERIOD).getLongValue();
			if (period == 0) {
				period = Long.parseLong(data.get(PERIOD).getTextValue());
			}
			if (period <= 0) {
				throw new InvalidOptionValueException("Metadata period value is illgal! CAN NOT be a value <= 0!");
			}
		}
		
		dbs = new DBConfigs(data.findValue(DBConfigs.DBS));
		if (dbs.isValid(data.findValue(DALConfiguration.DB_SETTINGS))) {
			dbs.parse();
		}		
	}
	public DBConfigs getDBs() {
		return dbs;
	}
	
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
	public long getPeriod() {
		return period;
	}
}
