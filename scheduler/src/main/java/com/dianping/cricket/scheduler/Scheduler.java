package com.dianping.cricket.scheduler;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.dianping.cricket.scheduler.job.AbstractJob;
import com.dianping.cricket.scheduler.job.JarJob;
import com.dianping.cricket.scheduler.job.ShellJob;
import com.dianping.cricket.scheduler.pojo.Job;

public class Scheduler {
	private static Logger logger = Logger.getLogger(Scheduler.class);
	// Scheduler loader.
	private SchedulerLoader schedulerLoader = SchedulerLoader.getLoader();
	// Scheduler obj of quartz.
	private org.quartz.Scheduler scheduler;
	public void init() {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			
			// Add the scheduler listener.
			scheduler.getListenerManager().addSchedulerListener(new SchedulerListener());
			
			// Start the scheduler.
			scheduler.start();
			
			logger.info("Scheduler started ...");
			
			// Deploy jobs.
			deployJobs();
			
			logger.info("Deploy jobs onto scheduler ...");
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.fatal("Failed on booting scheduler: [" + e.getMessage() + "]");
			System.exit(1);
		}
	}
	
	public void deployJobs() throws SchedulerException {
		// Load all the jobs from db.
		List<Job> jobs = schedulerLoader.loadJobs();
		for (Job job : jobs) {
			// Check whether job is valid.
			if (job.isLoadable()) {
				JobBuilder builder = null;
				if (job.getType() == Job.Type.SHELL_JOB) {
					builder = newJob(ShellJob.class).usingJobData(ShellJob.MAIN_SHELL, job.getMainEntry());
				} else {
					builder = newJob(JarJob.class).usingJobData(JarJob.JOB_JAR, job.getMainEntry());
				}
				// Pass job id for db ops in job.
				builder.usingJobData(AbstractJob.JOB_ID, job.getId());
				
				// Create job detail.
				JobDetail jobDetail = builder.withIdentity(job.getName(), job.getGroup()).build();
				
				// Create job trigger.
				Trigger trigger = newTrigger().withIdentity(job.getName(), job.getGroup()).withSchedule(cronSchedule(job.getSchedule())).build();
				
				// Schedule job in scheduler.
				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				logger.info("Job [" + job.getName() + ", " + job.getGroup() + "] is NOT eligible for deployment! so ignore it!");
			}
		}
	}
}
