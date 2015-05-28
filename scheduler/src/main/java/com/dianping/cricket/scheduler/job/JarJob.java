package com.dianping.cricket.scheduler.job;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;

import com.dianping.cricket.api.mail.MailBuilder;
import com.dianping.cricket.scheduler.SchedulerConf;
import com.dianping.cricket.scheduler.rest.util.JobUtil;

public class JarJob extends AbstractJob {
	private static Logger logger = Logger.getLogger(JarJob.class);
	public static final String JOB_JAR = "jobJar";
	private Job job;
	private JobKey jobKey;

	@Override
	public int progress() {
		return job.progress();
	}

	@Override
	public void start(JobExecutionContext context) throws JobExecutionException {
		this.jobKey = context.getJobDetail().getKey();
		
		// Get jar file position.
		String jobJarName = context.getMergedJobDataMap().get(JOB_JAR).toString();
		
		// Find job jar path.
		Path path = JobUtil.getJobJarPath(jobJarName);
		
		logger.info("Load job jar file: [" + path + "]!");
		try {
			// Find & load main class of the job entry, and get instance of that class.
			job = JobUtil.getMainClassEntryInstance(path);
			
			job.start(context);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}

	@Override
	public void run(JobExecutionContext context) throws JobExecutionException {
		// Set up monitor for job interruption.
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		
		// Schedule monitor thread to check interruption update every 5 seconds.
		service.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if (JarJob.this.isInterrupted()) {
					try {
						job.interrupt();
					} catch (UnableToInterruptJobException e) {
						e.printStackTrace();
					}
				}
			}
		}, 0, 5, TimeUnit.SECONDS);
		
		job.run(context);
		
		// Shutdown schedule service.
		service.shutdownNow();
	}

	@Override
	public void end(JobExecutionContext context) throws JobExecutionException {
		job.end(context);
	}

	@Override
	public void done(JobExecutionContext context) throws JobExecutionException {
		try {
			MailBuilder.newBuilder().subject("[Job Success]" + jobKey)
				.recipient("linxiao.teng@dianping.com")
				.body("job_success", this.getStatus())
				.build().send();
			logger.info("Jar job notice email is sent out: [" + jobKey + "]!");
		} catch (EmailException e) {
			e.printStackTrace();
			logger.info("Failed to send out email for Jar job: [" + jobKey + "] due to exception: " + e.getMessage());
		}
	}
}
