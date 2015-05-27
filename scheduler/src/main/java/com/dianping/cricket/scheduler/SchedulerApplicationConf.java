package com.dianping.cricket.scheduler;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.dianping.cricket.scheduler.rest.provider.SchedulerApplicationListener;

public class SchedulerApplicationConf extends ResourceConfig {
	public SchedulerApplicationConf() {
		register(MultiPartFeature.class);
	}
}
