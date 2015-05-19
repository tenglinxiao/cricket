package com.dianping.cricket.scheduler.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DaemonJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

	}
}
