package com.dianping.cricket.scheduler.rest.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import com.dianping.cricket.api.mail.MailBuilder;
import com.dianping.cricket.scheduler.SchedulerConf;
import com.dianping.cricket.scheduler.job.Job;
import com.dianping.cricket.scheduler.pojo.JobStatus;
import com.dianping.cricket.scheduler.pojo.Recipient;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerInvalidJobJarException;

public class JobUtil {
	private static Logger logger = Logger.getLogger(JobUtil.class);
	private static String TIMESTAMP = "[%d]";
	
	public static Path getJobJarPath(String jobJarName) {
		// Find the jars path in the config.
		Path jobJarsPath = Paths.get(SchedulerConf.getConf().getJobJars());
		
		// If the path is absolute, then append the jar name for path,
		// otherwise use the current dir as root path.
		if (jobJarsPath.isAbsolute()) {
			return jobJarsPath.resolve(jobJarName);
		} else {
			return Paths.get(SchedulerConf.getConf().getRoot()).resolve(jobJarsPath).resolve(jobJarName);
		}
	}
	
	public static Path getBootShellPath() {
		// Find the shell path in the config.
		Path shellPath = Paths.get(SchedulerConf.getConf().getBootShell());
		
		// If the path is absolute, then append the shell name for path,
		// otherwise use the current dir as root path.
		if (shellPath.isAbsolute()) {
			return shellPath;
		} else {
			return Paths.get(SchedulerConf.getConf().getRoot()).resolve(shellPath);
		}
	}
	
	public static Path getJobShell(String jobShellName) {
		// Find the job shells path in the config.
		Path jobShells = Paths.get(SchedulerConf.getConf().getJobShells());
		
		// If the path is absolute, then append the shell name for path,
		// otherwise use the current dir as root path.
		if (jobShells.isAbsolute()) {
			return jobShells.resolve(jobShellName);
		} else {
			return Paths.get(SchedulerConf.getConf().getRoot()).resolve(jobShells).resolve(jobShellName);
		}
	}
	
	public static Job getMainClassEntryInstance(Path jarFile) throws SchedulerException {
		// Find the class that implements Job interface.
		Class<?> cls = findMainClassEntry(jarFile);
		
		// If can not find the class, then throw exception.
		if (cls == null) {
			throw new SchedulerInvalidJobJarException("There is no class implements Job interface defined!");
		}
		
		Job job = null;
		try {
			job = (Job)cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new SchedulerInvalidJobJarException("public default constructor is a MUST-HAVE for class!");
		}
		return job;
		
	}
	
	// Find the main class entry.
	public static Class<?> findMainClassEntry(Path jarFile) throws SchedulerException {
		JarFile jar = null;
		URLClassLoader loader = null;
		try {
			// Create loader for the jar.
			loader = new URLClassLoader(new URL[]{jarFile.toUri().toURL()}, JobUtil.class.getClassLoader());

			// File name without extension.
			String fileNameWithoutEx = jarFile.getFileName().toString().split("\\.")[0];
			
			// Strip the timestamp on the file.
			fileNameWithoutEx = fileNameWithoutEx.replaceAll("\\[\\d+\\]", "");
			
			
			jar = new JarFile(jarFile.toFile());
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.isDirectory() && entry.getName().endsWith(fileNameWithoutEx + ".class")) {
					String fullName = entry.getName().split("\\.")[0];
					fullName = fullName.replaceAll("/", ".");
					
					logger.info("Try to load target class [" + fullName + "]");
					Class<?> cls = loader.loadClass(fullName);
					if (!Job.class.isAssignableFrom(cls)) {
						logger.info("Job class[" + fullName + "] is not subclass of Job!");
						continue;
					}
					return cls;
				}
			}
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new SchedulerInvalidJobJarException("Job path generated URL is illegal!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SchedulerInvalidJobJarException("CAN NOT find class: [" + e.getMessage() + "]");
		} catch (IOException e) {
			e.printStackTrace();
			throw new SchedulerInvalidJobJarException("Exception when access the jar file: [" + e.getMessage() + "]");
		} catch(Exception e) {
			e.printStackTrace();
			throw new SchedulerException(e);
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
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Path appendTimestamp(Path jarFile) {
		// Get parent path.
		Path path = jarFile.getParent();
		
		// Get file name.
		String fileName = jarFile.getFileName().toString();
		int pos = fileName.lastIndexOf('.');
		
		// Get extension.
		String extension = fileName.substring(pos);
		
		// Format timestamp part.
		String timestamp = String.format(TIMESTAMP, System.currentTimeMillis());
		
		fileName = fileName.substring(0, pos).concat(timestamp).concat(extension);
		
		// Append the parent to the path if it has.
		if (path == null) {
			return Paths.get(fileName);
		} else {
			return path.resolve(fileName);
		}
	}
	
	public static boolean isJarFile(Path jarFile) {
		if (jarFile != null && jarFile.getFileName().toString().toLowerCase().endsWith(".jar")) {
			return true;
		}
		return false;
	}
	
	
	public static void sendMail(List<Recipient> recipients, JobStatus status) throws EmailException {
		MailBuilder builder = MailBuilder.newBuilder()
				.subject("[Job Success]" + status.getJob().getJobKey())
				.body("job_success", status);
		for (Recipient recipient : recipients) {
			builder.recipient(recipient.getMail());
		}	
		builder.build().send();
	}

}
