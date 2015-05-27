package com.dianping.cricket.scheduler.rest.exceptions;

import org.quartz.SchedulerException;

public class SchedulerPersistenceException extends SchedulerException {
	public SchedulerPersistenceException(String msg) {
		super(msg);
	}
	
	public SchedulerPersistenceException(Throwable throwable) {
		super(throwable);
	}
}
