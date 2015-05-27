package com.dianping.cricket.scheduler.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import com.dianping.cricket.scheduler.SchedulerConf;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerInvalidJobDefinitionException;
import com.dianping.cricket.scheduler.rest.util.JobUtil;

public class Job {
	private static Logger logger = Logger.getLogger(Job.class);
	public static enum Type {
		SHELL_JOB, JAR_JOB
	}
	private int id;
	private JobKey jobKey;
	private String description;
	private String owner;
	private String schedule;
	private String mainEntry;
	private Type type = Type.SHELL_JOB;
	private Date createdTime;
	private Date updatedTime;
	public Job() {
		jobKey = new JobKey("-");
	}
	public Job(int id) {
		this();
		this.id = id;
	}
	public Job(String name, String group) {
		this.jobKey = new JobKey(name, group);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public JobKey getJobKey() {
		return jobKey;
	}
	public void setJobKey(JobKey jobKey) {
		this.jobKey = jobKey;
	}
	public void setName(String name) {
		this.jobKey = new JobKey(name, this.jobKey.getGroup());
	}
	public void setGroup(String group) {
		this.jobKey = new JobKey(this.jobKey.getName(), group);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getMainEntry() {
		return mainEntry;
	}
	public void setMainEntry(String mainEntry) {
		this.mainEntry = mainEntry;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public boolean equals(JobKey key) {
		return this.jobKey.equals(key);
	}
	public Job clone() {
		try {
			Job job = new Job();
			for (Field field : this.getClass().getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers())) {
					field.setAccessible(true);
					field.set(job, field.get(this));
				}
			}
			return job;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public boolean loadable() throws SchedulerException {
		if (this.jobKey.getName().equals("") || this.jobKey.getName().equals("-")) {
			throw new SchedulerInvalidJobDefinitionException("name CAN NOT be null or empty!");
		}
		
		if (mainEntry == null || mainEntry.equals("")) {
			throw new SchedulerInvalidJobDefinitionException("mainEntry CAN NOT be null or empty!");
		}
		
		if (owner == null || owner.equals("")) {
			throw new SchedulerInvalidJobDefinitionException("owner CAN NOT be null or empty!");
		}
		
		if (schedule == null || schedule.equals("")) {
			throw new SchedulerInvalidJobDefinitionException("schedule CAN NOT be null or empty!");
		}
		
		if (type == Type.JAR_JOB) {
			Path path = Paths.get(SchedulerConf.getConf().getJobJars());
			if (!path.isAbsolute()) {
				path = Paths.get("").toAbsolutePath();
				path = path.resolve(Paths.get(SchedulerConf.getConf().getJobJars()));
			}
			path = path.resolve(Paths.get(mainEntry));
			if(!path.toFile().exists()) {
				throw new SchedulerInvalidJobDefinitionException("Main entry jar job CAN NOT be found on path ["+ path + "]!");
			}
			// Verify the job jar.
			JobUtil.getMainClassEntryInstance(path);
		}
		return true;
	}
}
