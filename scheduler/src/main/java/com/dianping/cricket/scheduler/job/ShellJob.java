package com.dianping.cricket.scheduler.job;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import com.dianping.cricket.scheduler.SchedulerConf;

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
		// Compose the path that boots the shell job.
		Path path = Paths.get("").toAbsolutePath();
		path = path.resolve(Paths.get(SchedulerConf.getConf().getBootShell()));
		logger.info("Kick off shell job with command: [" + path + " " + context.getMergedJobDataMap().get(MAIN_SHELL) + "]!");
		
		// Builder to kick off the job
		ProcessBuilder builder = new ProcessBuilder(path.toString(), context.getMergedJobDataMap().get(MAIN_SHELL).toString());
		try {
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				logger.info(line);
			}
			reader.close();
			logger.info("Shell process exited with code: [" + process.exitValue() + "]!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}

	@Override
	public void end(JobExecutionContext context) {
		logger.info("Shell job with identity: [" + jobKey.getName() + ", " + jobKey.getGroup() + "] end!");
	}
}
