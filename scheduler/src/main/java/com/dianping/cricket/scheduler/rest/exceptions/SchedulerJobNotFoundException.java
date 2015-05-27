package com.dianping.cricket.scheduler.rest.exceptions;

import org.quartz.SchedulerException;

public class SchedulerJobNotFoundException extends SchedulerException {
	public SchedulerJobNotFoundException(String msg) {
		super(msg);
	}
	
	public SchedulerJobNotFoundException(Throwable throwable) {
		super(throwable);
	}
}
