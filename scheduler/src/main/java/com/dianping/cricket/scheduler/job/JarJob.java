package com.dianping.cricket.scheduler.job;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dianping.cricket.scheduler.SchedulerConf;

public class JarJob extends AbstractJob {
	private static Logger logger = Logger.getLogger(JarJob.class);
	public static final String JOB_JAR = "jobJar";
	private Job job;

	@Override
	public int progress() {
		return -1;
	}

	@Override
	public void start(JobExecutionContext context) throws JobExecutionException {
		String jarFile = context.getMergedJobDataMap().get(JOB_JAR).toString();
		Path path = Paths.get("").toAbsolutePath();
		path = path.resolve(Paths.get(SchedulerConf.getConf().getJobJars()));
		path = path.resolve(Paths.get(jarFile));
		logger.info("Load job jar file: [" + path + "]!");
		
		URLClassLoader loader = null;
		JarFile jar = null;
		try {
			ClassLoader parent = this.getClass().getClassLoader();
			loader = new URLClassLoader(new URL[]{path.toUri().toURL()}, parent);
			String mainCls = jarFile.split("\\.")[0];
			jar = new JarFile(path.toFile());
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.isDirectory() && entry.getName().endsWith(mainCls + ".class")) {
					String fullName = entry.getName().split("\\.")[0];
					fullName = fullName.replaceAll("/", ".");
					logger.info("Try to load target class [" + fullName + "]");
					
					Class<?> cls = loader.loadClass(fullName);
					if (!Job.class.isAssignableFrom(cls)) {
						logger.info("Job entry class is not subclass of Job!");
						throw new JobExecutionException("This case should never happen! Verify packaged jar & db entry records plz.");
					}
					
					// Create job instance.
					job = (Job)cls.newInstance();
					break;
				}
			}
			job.start(context);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.info("Job entry class must can be found on class path!");
			throw new JobExecutionException(e);
		} catch (InstantiationException e) {
			e.printStackTrace();
			logger.info("Job class must define one no-args constructor!");
			throw new JobExecutionException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.info("Job class CAN NOT be accessed!");
			throw new JobExecutionException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		} finally {
			if (jar != null) {
				try {
					jar.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (loader != null) {
				try {
					loader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run(JobExecutionContext context) throws JobExecutionException {
		job.run(context);
	}

	@Override
	public void end(JobExecutionContext context) throws JobExecutionException {
		job.end(context);
	}
}
