package com.dianping.cricket.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import com.dianping.cricket.api.conf.Configurable;

public class ServiceEndPointConf extends Configurable {
	public static final String WATCH_NODE = "services";
	public static final String SSO_SERVICE = "sso";
	public static final String CONTEXT_PREFIX = "contextPrefix";
	public static final String CONTEXT_MAPPING = "contextMapping";
	private static ServiceEndPointConf conf;
	private String ssoService;
	private String contextPrefix;
	private Map<String, ServiceEndPoint> contextMapping;
	
	public static ServiceEndPointConf getConf() {	
		synchronized (ServiceEndPointConf.class) {
			if (conf == null) {
				conf = new ServiceEndPointConf();
			}
		}

		return conf;
	}
	
	private ServiceEndPointConf() {}
	
	public Map<String, ServiceEndPoint> getContextMapping() {
		return contextMapping;
	}
	
	public String getContextPrefix() {
		return contextPrefix;
	}

	public String getSsoService() {
		return this.ssoService;
	}

	@Override
	protected String getWatchNode() {
		return WATCH_NODE;
	}

	@Override
	protected void parse(JsonNode watchNode) {
		this.ssoService = getConfValue(SSO_SERVICE).getTextValue();
		this.contextMapping = new HashMap<String, ServiceEndPoint>();
		this.contextPrefix = getConfValue(CONTEXT_PREFIX).getTextValue();
		
		// Find mapping node.
		ArrayNode mapping = (ArrayNode)getConfValue(CONTEXT_MAPPING);
		Iterator<JsonNode> iter= mapping.getElements();
		while (iter.hasNext()) {
			JsonNode node = iter.next();
			ServiceEndPoint endPoint = new ServiceEndPoint();
			endPoint.setHost(node.get("host").getTextValue());
			endPoint.setPort(node.get("port").getIntValue());
			endPoint.setContext(node.get("context").getTextValue());
			endPoint.setName(node.get("name").getTextValue());
			this.contextMapping.put(node.get("name").getTextValue(), endPoint);
		}
	}
}
