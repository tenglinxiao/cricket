package com.dianping.cricket.scheduler;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

public class SchedulerListener implements org.quartz.SchedulerListener {
	private static Logger logger = Logger.getLogger(SchedulerListener.class);

	@Override
	public void jobScheduled(Trigger trigger) {
		logger.info("Job scheduled: [" + trigger.getJobKey().getName() + ":" + trigger.getJobKey().getGroup() + "]" );
	}

	@Override
	public void jobUnscheduled(TriggerKey triggerKey) {
		logger.info("Job unscheduled: [" + triggerKey.getName() + ":" + triggerKey.getGroup() + "]" );
	}

	@Override
	public void triggerFinalized(Trigger trigger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggerPaused(TriggerKey triggerKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggersPaused(String triggerGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggerResumed(TriggerKey triggerKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggersResumed(String triggerGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobAdded(JobDetail jobDetail) {
		logger.info("Job Added: [" + jobDetail.getKey().getName() + ":" + jobDetail.getKey().getGroup() + "]" );
	}

	@Override
	public void jobDeleted(JobKey jobKey) {
		logger.info("Job Deleted: [" + jobKey.getName() + ":" + jobKey.getGroup() + "]" );
	}

	@Override
	public void jobPaused(JobKey jobKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobsPaused(String jobGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobResumed(JobKey jobKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobsResumed(String jobGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void schedulerError(String msg, SchedulerException cause) {
		logger.error(msg);
	}

	@Override
	public void schedulerInStandbyMode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void schedulerStarted() {
		logger.info("Scheduler started ...");
		
	}

	@Override
	public void schedulerStarting() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void schedulerShutdown() {
		logger.info("Scheduler is down!");
		
	}

	@Override
	public void schedulerShuttingdown() {
		logger.info("Scheduler is shutting down!");
	}

	@Override
	public void schedulingDataCleared() {
		// TODO Auto-generated method stub
		
	}

}
