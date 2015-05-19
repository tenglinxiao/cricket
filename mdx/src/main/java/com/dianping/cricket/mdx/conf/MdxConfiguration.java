package com.dianping.cricket.mdx.conf;

import org.codehaus.jackson.JsonNode;

import com.dianping.cricket.api.conf.Configurable;

public class MdxConfiguration extends Configurable {
	// Watch node.
	private static final String MDX_NODE = "mdx";
	private static final String CONNECTIONS = "connections";
	private static final String POOL = "pool";
	private static final String CORE_SIZE = "core";
	private static final String MAXIMUM_SIZE = "maximum";
	
	private int coreSize;
	private int maximumSize;
	private int maxIdleTime;
	
	// Singleton conf.
	private static MdxConfiguration conf; 
	
	private MdxConfiguration() {}

	@Override
	protected String getWatchNode() {
		return MDX_NODE;
	}

	@Override
	protected void parse(JsonNode data) throws Exception {
		JsonNode pool = data.get(CONNECTIONS).get(POOL);
		coreSize = pool.get(CORE_SIZE).getIntValue();
		maximumSize = pool.get(MAXIMUM_SIZE).getIntValue();
	}
	
	public int getCoreSize() {
		return coreSize;
	}
	
	public int getMaximumSize() {
		return maximumSize;
	}
	
	public int getMaxIdleTime() {
		return maxIdleTime;
	}
	
	public static MdxConfiguration getMdxConfiguration()
	{
		if (conf == null) {
			conf = new MdxConfiguration();
		}
		return conf;
	}
}
