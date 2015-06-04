package com.dianping.cricket.scheduler.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.cricket.api.system.SystemMonitor;
import com.dianping.cricket.scheduler.Scheduler;
import com.dianping.cricket.scheduler.SchedulerLoader;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerPersistenceException;
import com.dianping.cricket.scheduler.rest.util.ResultWrapper;
import com.dianping.cricket.scheduler.rest.util.ResultWrapper.JsonResult;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class SchedulerMonitor {
	private static Logger logger = Logger.getLogger(SchedulerMonitor.class);
	
	@Autowired
	private Scheduler scheduler;
	
	private Map<String, Object> systemUsages = SystemMonitor.getUsages();
	
	@GET
	@Path("system") 
	public JsonResult getSystemUsages() {
		return ResultWrapper.wrap(systemUsages);
	}
	
	@GET
	@Path("scheduled")
	public JsonResult getScheduledJobs() {
		return ResultWrapper.wrap(scheduler.getDeployedJobs());
	}
	
	@GET
	@Path("scheduling")
	public JsonResult getOnSchedulingJobs() {
		return ResultWrapper.wrap(scheduler.getOnSchdulingJobs());
	}
	
	@GET
	@Path("sla_sle")
	public JsonResult getServiceLevelStatistics() {
		try {
			List<Map<String, Object>> statistics = SchedulerLoader.getLoader().getServiceLevelStatistics();
			return ResultWrapper.wrap(statistics);
		} catch (SchedulerPersistenceException e) {
			e.printStackTrace();
			logger.error("Failed to fetch the service level statistics data! ");
			return ResultWrapper.wrap(Status.INTERNAL_SERVER_ERROR, "Failed to communicate with db: [" + e.getMessage() + "]");
		}
		
	}
}
