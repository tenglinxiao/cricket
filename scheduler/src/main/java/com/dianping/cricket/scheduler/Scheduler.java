package com.dianping.cricket.scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.web.context.ServletContextAware;

import com.dianping.cricket.scheduler.job.AbstractJob;
import com.dianping.cricket.scheduler.job.DaemonJob;
import com.dianping.cricket.scheduler.job.JarJob;
import com.dianping.cricket.scheduler.job.ShellJob;
import com.dianping.cricket.scheduler.pojo.Job;
import com.dianping.cricket.scheduler.pojo.JobStatus;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerJobNotFoundException;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerPersistenceException;

public class Scheduler implements ServletContextAware {
	public static final String RECOVERY_GROUP = "recovery";
	private static Logger logger = Logger.getLogger(Scheduler.class);
	// Scheduler loader.
	private SchedulerLoader schedulerLoader = SchedulerLoader.getLoader();
	// Scheduler obj of quartz.
	private org.quartz.Scheduler scheduler;
	private List<Job> jobs = new ArrayList<Job>();
	
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
		// deploy daemon jobs.
		deployDaemon();
		
		// Load all the jobs from db.
		List<Job> jobs = schedulerLoader.loadJobs();
		for (Job job : jobs) {
			deployJob(job, false);
		}
		
		logger.info("Deployed jobs Number: [" + jobs.size() + "]!");
	}
	
	public void deployDaemon() throws SchedulerException {
		JobDetail jobDetail = newJob(DaemonJob.class).withIdentity(DaemonJob.class.getSimpleName()).build();
		Trigger trigger = newTrigger().forJob(jobDetail).withSchedule(
				simpleSchedule()
				.withIntervalInSeconds(SchedulerConf.getConf().getDaemonFrequency()).repeatForever())
				.startNow()
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
	}
	
	public List<Job> getDeployedJobs() {
		return jobs;
	}
	
	public List<Job> getOnSchdulingJobs() {
		try {
			List<Job> jobs = new ArrayList<Job>();
			List<JobExecutionContext> contexts = scheduler.getCurrentlyExecutingJobs();
			for (JobExecutionContext context : contexts) {
				for (int index = 0; index < this.jobs.size(); index++) {
					Job job = this.jobs.get(index);
					if (job.equals(context.getJobDetail().getKey())) {
						jobs.add(job);
						break;
					}
				}
			}
			return jobs;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Failed to fetch the on scheduling jobs list!");
			return null;
		}
	}
	
	public void deployJob(Job job, boolean recovered) throws SchedulerException {
		// Check whether job is valid.
		if (job.loadable()) {
			this.jobs.add(job.clone());
			
			JobBuilder jobBuilder = null;
			if (job.getType() == Job.Type.SHELL_JOB) {
				jobBuilder = newJob(ShellJob.class).usingJobData(ShellJob.MAIN_SHELL, job.getMainEntry());
			} else {
				jobBuilder = newJob(JarJob.class).usingJobData(JarJob.JOB_JAR, job.getMainEntry());
			}
			// Pass job id for db ops in job.
			jobBuilder.usingJobData(AbstractJob.JOB_ID, job.getId());
			
			// Pass recovered value to let job instance know whether it's a recovered job.
			jobBuilder.usingJobData(AbstractJob.RECOVERED, recovered);
			
			// Pass mail for notification.
			jobBuilder.usingJobData(AbstractJob.MAIL, job.getMail());
			
			// If it is deployed as recovered job, then set the group name to 'recovery'.
			if (recovered) {
				jobBuilder.withIdentity(job.getJobKey().toString(), RECOVERY_GROUP);
			} else {
				jobBuilder.withIdentity(job.getJobKey());
			}
			
			JobDetail jobDetail = jobBuilder.build();
			
			// Create job trigger.
			TriggerBuilder<Trigger> triggerBuilder = newTrigger().forJob(jobDetail.getKey());
			
			// If the job is deployed as recovered job, then run it immediately.
			if (recovered) {
				triggerBuilder.startNow();
			} else {
				triggerBuilder.withSchedule(cronSchedule(job.getSchedule()));
			}
			
			// Schedule job in scheduler.
			scheduler.scheduleJob(jobDetail, triggerBuilder.build());
			
			logger.info("Done deploying scheduling job: [" + jobDetail.getKey() + "]!");
		
		} else {
			logger.warn("Job [" + job.getJobKey() + "] is NOT eligible for deployment! so ignore it!");
		}
	}
	
	public boolean undeployJob(int jobId) throws SchedulerJobNotFoundException {
		Job job = null;
		
		// Find the job with the id passed in.
		for (int index = 0; index < jobs.size(); index++) {
			if (jobs.get(index).getId() == jobId) {
				job = jobs.get(index);
				break;
			}
		}
		
		// If can not find the job, throw the exception.
		if (job == null) {
			throw new SchedulerJobNotFoundException("CAN NOT find the scheduler job with id: [" + jobId + "]");
		}
		
		try {
			if (scheduler.deleteJob(job.getJobKey())) {
				logger.info("Delete job with key [" + job.getJobKey() + "] from scheduler!");
				return true;
			}
			return false;
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error("Failed to undeploy the job: [" + job.getJobKey() + "]!");
			return false;
		} 
	}
	
	public void recover() {
		logger.info("Try to find the jobs require recovery!");
		try {
			List<JobStatus> jobStatuses = schedulerLoader.findRecoveredJobs(this.scheduler.getMetaData().getRunningSince());
			for (JobStatus jobStatus : jobStatuses) {
				deployJob(jobStatus.getJob(), true);
			}
			logger.info("Done deploying jobs for recovery process.");
		} catch (SchedulerPersistenceException e) {
			e.printStackTrace();
			logger.info("Failed to communicate with db when working on job recovery process: [" + e.getMessage() + "]");
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.info("Exception when take recovery process: [" + e.getMessage() + "]");
		}
	}
	
	public void shutdown(boolean waitForCompletion) {
		try {
			this.scheduler.shutdown(waitForCompletion);
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error("Exception when shutdown the scheduler: [" + e.getMessage() + "]");
		}
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		// Register the root dir for the project.
		SchedulerConf.getConf().setRoot(servletContext.getRealPath("/"));
	}
}
