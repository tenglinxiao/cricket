package com.dianping.cricket.scheduler.rest.exceptions;

import org.quartz.SchedulerException;

public class SchedulerInvalidJobJarException extends SchedulerException {
	public SchedulerInvalidJobJarException(String msg) {
		super(msg);
	}
	
	public SchedulerInvalidJobJarException(Throwable throwable) {
		super(throwable);
	}
}
