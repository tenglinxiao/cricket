package com.dianping.cricket.scheduler.job;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import com.dianping.cricket.scheduler.SchedulerLoader;
import com.dianping.cricket.scheduler.pojo.JobStatus;
import com.dianping.cricket.scheduler.pojo.JobStatus.Status;

public abstract class AbstractJob implements InterruptableJob, TimeAware, Progressable, Job {
	public static final String JOB_ID = "jobId";
	private JobStatus status = new JobStatus();
	private boolean isInterrupted;
	private int percentage;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// Get start time.
		status.setStartTime(getStartTime());
		
		// Set status to running.
		status.setStatus(Status.RUNNING);
		
		// Update the job current status in db.
		updateStatus();
		
		// Start the job for preparation.
		start(context);
		
		// Set up progress monitor thread.
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		
		// Schedule progress update every 5 mins.
		service.scheduleAtFixedRate(new Runnable() {
			public void run() {
				percentage = progress();
			}
		}, 0, 5, TimeUnit.SECONDS);
	
		// Run the job.
		run(context);
		
		// Shut down service scheduler.
		service.shutdownNow();
		
		percentage = 100;
		
		// End the job.
		end(context);
		
		// Get end time.
		status.setEndTime(getEndTime());
		
		// Set status to success.
		status.setStatus(Status.SUCCESS);
		
		// Update status again in db.
		updateStatus();
		
		// Log status in db.
		logStatus();
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
	
	public int getPercentage() {
		return percentage;
	}
	
	public JobStatus getStatus() {
		return status;
	}
	
	public void setJobId(int jobId) {
		this.status.setJobId(jobId);
	}

	public void updateStatus() {
		SchedulerLoader.getLoader().updateStatus(status);
	}
	
	public void logStatus() {
		SchedulerLoader.getLoader().logStatus(status);
	}
}
