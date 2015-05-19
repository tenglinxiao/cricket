package com.dianping.cricket.scheduler.pojo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionException;

import com.dianping.cricket.scheduler.SchedulerConf;
import com.dianping.cricket.scheduler.job.AbstractJob;

public class Job {
	private static Logger logger = Logger.getLogger(Job.class);
	public static enum Type {
		SHELL_JOB, JAR_JOB
	}
	private int id;
	private String name;
	private String group;
	private String description;
	private String owner;
	private String schedule;
	private String mainEntry;
	private Type type = Type.SHELL_JOB;
	private Date createdTime;
	private Date updatedTime;
	public Job() {}
	public Job(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
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
	public boolean isLoadable() {
		if (mainEntry == null) {
			logger.info("Job main entry CAN NOT be null!");
			return false;
		}
		if (type == Type.JAR_JOB) {
			Path path = Paths.get("").toAbsolutePath();
			path = path.resolve(Paths.get(SchedulerConf.getConf().getJobJars()));
			path = path.resolve(Paths.get(mainEntry));
			if(!path.toFile().exists()) {
				logger.info("Main entry jar job CAN NOT be found!");
				return false;
			}
		}
		return true;
	}
}
