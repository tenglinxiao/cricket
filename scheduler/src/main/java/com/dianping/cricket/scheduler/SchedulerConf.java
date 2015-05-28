package com.dianping.cricket.scheduler;

import org.codehaus.jackson.JsonNode;

import com.dianping.cricket.api.conf.Configurable;

public class SchedulerConf extends Configurable {
	private static final String WATCH_NODE = "scheduler";
	private static SchedulerConf conf;
	private String bootShell;
	private String jobJars;
	private String root;
	private int daemonFrequency;

	@Override
	protected String getWatchNode() {
		return WATCH_NODE;
	}

	@Override
	protected void parse(JsonNode data) throws Exception {
		bootShell = getConfValue("bootShell").getTextValue();
		jobJars = getConfValue("jobJars").getTextValue();
		daemonFrequency = getConfValue("daemonFrequency").getIntValue();
	}
	
	public String getBootShell() {
		return bootShell;
	}
	
	public String getJobJars() {
		return jobJars;
	}
	
	public String getRoot() {
		return root;
	}
	
	public void setRoot(String root) {
		this.root = root;
	}
	
	public int getDaemonFrequency() {
		return daemonFrequency;
	}
	
	public static SchedulerConf getConf() {
		synchronized (SchedulerConf.class) {
			if (conf == null) {
				conf = new SchedulerConf();
			}
			return conf;
		}
	}
}
