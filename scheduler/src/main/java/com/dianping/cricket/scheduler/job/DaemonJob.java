package com.dianping.cricket.scheduler.job;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dianping.cricket.scheduler.SchedulerConf;
import com.dianping.cricket.scheduler.SchedulerLoader;
import com.dianping.cricket.scheduler.pojo.JobJar;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerPersistenceException;
import com.dianping.cricket.scheduler.rest.util.JobUtil;

public class DaemonJob implements Job {
	private static Logger logger = Logger.getLogger(DaemonJob.class);
	private SchedulerLoader loader = SchedulerLoader.getLoader();

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("Start daemon job!");
		
		try {
			List<JobJar> jobJars= loader.findJobJarsObsolete();
			for (JobJar jobJar : jobJars) {
				Path jarPath = JobUtil.getJobJarPath(jobJar.getStoredName());
				if (jarPath.toFile().exists()) {
					jarPath.toFile().delete();
				} else {
					logger.warn("CAN NOT find the file on path when try to delete: [" + jarPath + "]!");
				}
				
				// Delete the jar file record in db.
				loader.deleteJobJar(jobJar.getStoredName());
				logger.info("Delete obsolete job jar file on path: [" + jarPath + "]!");
			}
		} catch (SchedulerPersistenceException e) {
			e.printStackTrace();
			logger.error("Failed to communicate with db: [" + e.getMessage() + "]!");
		}
		logger.info("End daemon job!");
	}
}
