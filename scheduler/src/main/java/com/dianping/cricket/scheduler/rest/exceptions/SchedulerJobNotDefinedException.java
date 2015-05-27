package com.dianping.cricket.scheduler.rest.exceptions;

import org.quartz.SchedulerException;

public class SchedulerJobNotDefinedException extends SchedulerException {
	public SchedulerJobNotDefinedException(String msg) {
		super(msg);
	}
	
	public SchedulerJobNotDefinedException(Throwable throwable) {
		super(throwable);
	}
}
