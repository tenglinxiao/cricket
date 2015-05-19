package com.dianping.cricket.scheduler.job;

import java.util.Date;

public interface TimeAware {
	// Get job start time.
	public Date getStartTime();
	// Get job end time.
	public Date getEndTime();
}
