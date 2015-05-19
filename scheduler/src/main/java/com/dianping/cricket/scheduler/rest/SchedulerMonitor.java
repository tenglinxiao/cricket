package com.dianping.cricket.scheduler.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("monitor")
public class SchedulerMonitor {
	@GET
	public String say() {
		return "test";
	}
}
