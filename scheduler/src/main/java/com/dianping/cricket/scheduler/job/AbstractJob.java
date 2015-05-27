package com.dianping.cricket.scheduler.job;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import com.dianping.cricket.scheduler.SchedulerLoader;
import com.dianping.cricket.scheduler.pojo.JobStatus;
import com.dianping.cricket.scheduler.pojo.JobStatus.Status;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerPersistenceException;

public abstract class AbstractJob implements InterruptableJob, TimeAware, Job {
	public static final String JOB_ID = "jobId";
	public static final String RECOVERED = "recovered";
	private static Logger logger = Logger.getLogger(AbstractJob.class);
	private JobStatus status = new JobStatus();
	private boolean isInterrupted;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// Set job key.
		status.getJob().setJobKey(context.getJobDetail().getKey());
		
		// Get start time.
		status.setStartTime(getStartTime());
		
		// Set status to running.
		status.setStatus(Status.RUNNING);
		
		// Update the job current status in db.
		updateStatus();
		
		try {
			// Start the job for preparation.
			start(context);
			
			// Run the job.
			run(context);
			
			// End the job.
			end(context);
			
			// Set status to success.
			status.setStatus(Status.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			
			// Set status to failure.
			status.setStatus(Status.FAILURE);
		} finally {
			// Get end time.
			status.setEndTime(getEndTime());

			// Update status again in db.
			updateStatus();
			
			// Log status in db.
			logStatus();
			
			// Take extra actions on the for the job completion.
			done(context);
		}
	}
	
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		isInterrupted = true;
	}
	
	@Override
	public Date getStartTime() {
		return Calendar.getInstance().getTime();
	}

	@Override
	public Date getEndTime() {
		return Calendar.getInstance().getTime();
	}	
	
	public boolean isInterrupted() {
		return isInterrupted;
	}
	
	public JobStatus getStatus() {
		return status;
	}
	
	// Automatically called by scheduler.
	public void setJobId(int jobId) {
		this.status.getJob().setId(jobId);
	}
	
	public void setRecovered(boolean recovered) {
		this.status.setRecovered(recovered);
	}

	public void updateStatus() {
		// Recovered job status is not updated in db.
		if (status.isRecovered()) {	
			return;
		}
		try {
			SchedulerLoader.getLoader().updateStatus(status);
		} catch (SchedulerPersistenceException e) {
			e.printStackTrace();
			logger.info("Failed to update job status: [" + e.getMessage() + "]!");
		}
	}
	
	public void logStatus() {
		try {
			SchedulerLoader.getLoader().logStatus(status);
		} catch (SchedulerPersistenceException e) {
			e.printStackTrace();
			logger.info("Failed to log job status: [" + e.getMessage() + "]!");
		}
	}
}
