package com.dianping.cricket.scheduler.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

public interface Job extends Progressable {
	// Job start phase.
	public abstract void start(JobExecutionContext context) throws JobExecutionException;
	
	// Job running phase.
	public abstract void run(JobExecutionContext context) throws JobExecutionException;
	
	// Job end phase.
	public abstract void end(JobExecutionContext context) throws JobExecutionException;
	
	// Job finished, take extra actions in this method.
	public abstract void done(JobExecutionContext context) throws JobExecutionException;
	
	// Interrupt job.
	public void interrupt() throws UnableToInterruptJobException;
}
