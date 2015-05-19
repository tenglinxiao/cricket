package com.dianping.cricket.scheduler.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface Job {
	// Job start phase.
	public abstract void start(JobExecutionContext context) throws JobExecutionException;
	
	// Job running phase.
	public abstract void run(JobExecutionContext context) throws JobExecutionException;
	
	// Job end phase.
	public abstract void end(JobExecutionContext context) throws JobExecutionException;
}
