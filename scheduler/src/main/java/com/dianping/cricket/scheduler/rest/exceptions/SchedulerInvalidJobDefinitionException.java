package com.dianping.cricket.scheduler.rest.exceptions;

import org.quartz.SchedulerException;

public class SchedulerInvalidJobDefinitionException extends SchedulerException {
	public SchedulerInvalidJobDefinitionException(String msg) {
		super(msg);
	}
	
	public SchedulerInvalidJobDefinitionException(Throwable throwable) {
		super(throwable);
	}
}
