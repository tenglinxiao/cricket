package com.dianping.cricket.scheduler.job;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import com.dianping.cricket.api.mail.MailBuilder;
import com.dianping.cricket.scheduler.SchedulerConf;
import com.dianping.cricket.scheduler.SchedulerLoader;
import com.dianping.cricket.scheduler.pojo.Recipient;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerPersistenceException;
import com.dianping.cricket.scheduler.rest.util.JobUtil;

public class ShellJob extends AbstractJob {
	private static Logger logger = Logger.getLogger(ShellJob.class);
	public static final String MAIN_SHELL = "mainShell";
	private JobKey jobKey;

	@Override
	public int progress() {
		// Currently no way to estimate the progress of shell job.
		return -1;
	}

	@Override
	public void start(JobExecutionContext context) {
		jobKey = context.getJobDetail().getKey(); 
		logger.info("Shell job with identity: [" + jobKey.getName() + ", " + jobKey.getGroup() + "] is started!");
	}

	@Override
	public void run(JobExecutionContext context) throws JobExecutionException {
		// Compose the boot shell path.
		Path boot = JobUtil.getBootShellPath();
		
		// Get Job shell path.
		Path shell = JobUtil.getJobShell(context.getMergedJobDataMap().get(MAIN_SHELL).toString());

		logger.info("Kick off shell job with command: [" + boot + " " + shell + "]!");
		
		if (!shell.toFile().exists()) {
			throw new JobExecutionException("Boot shell CAN NOT be found!");
		}
		
		// Builder to kick off the job
		ProcessBuilder builder = new ProcessBuilder(boot.toString(), shell.toString());
		try {
			final Process process = builder.start();
			
			// Set up monitor for job interruption.
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			
			// Schedule monitor thread to check interruption update every 5 seconds.
			service.scheduleAtFixedRate(new Runnable() {
				public void run() {
					if (ShellJob.this.isInterrupted()) {
						process.destroy();
					}
				}
			}, 0, 5, TimeUnit.SECONDS);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				logger.info(line);
			}
			reader.close();
			
			// Shut down service scheduler.
			service.shutdownNow();
			
			logger.info("Shell process exited with code: [" + process.waitFor() + "]!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}

	@Override
	public void end(JobExecutionContext context) {
		logger.info("Shell job with identity: [" + jobKey.getName() + ", " + jobKey.getGroup() + "] end!");
	}

	@Override
	public void done(JobExecutionContext context) throws JobExecutionException {
		try {
			List<Recipient> recipients = SchedulerLoader.getLoader().getRecipients(this.getStatus().getJob().getId());
			recipients.add(Recipient.fakeRecipient(this.getStatus().getJob().getMail()));
			JobUtil.sendMail(recipients, this.getStatus());
			logger.info("Shell job notice email is sent out: [" + jobKey+ "]!");
		} catch (EmailException | SchedulerPersistenceException e) {
			e.printStackTrace();
			logger.info("Failed to send out email for Shell job: [" + jobKey + "] due to exception: " + e.getMessage());
		}
		
	}
}
