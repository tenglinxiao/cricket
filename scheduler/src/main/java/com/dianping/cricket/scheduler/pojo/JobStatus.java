package com.dianping.cricket.scheduler.pojo;

import java.util.Date;

public class JobStatus {
	public static enum Status {
		OPEN, RUNNING, SUCCESS, FAILURE
	}
	
	// Job.
	private Job job = new Job();
	// Job start time.
	private Date startTime;
	// Job end time.
	private Date endTime;
	// Job status.
	private JobStatus.Status status = Status.OPEN;
	// Whether job is recovered.
	private boolean recovered;

	private Date createdTime;
	private Date updatedTime;
	
	public JobStatus() {}
	
	public JobStatus(int jobId) {
		this.job.setId(jobId);
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public JobStatus.Status getStatus() {
		return status;
	}
	public void setStatus(JobStatus.Status status) {
		this.status = status;
	}
	public boolean isRecovered() {
		return recovered;
	}
	public void setRecovered(boolean recovered) {
		this.recovered = recovered;
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
	public String getTimeCost() {
		if (endTime == null) {
			return "--";
		}
		long timeCost = (endTime.getTime() - startTime.getTime()) / 1000;
		int seconds = (int)(timeCost % 60);
		int hours = (int)(timeCost / 3600);
		int mins = (int)(timeCost % 3600) / 60;
		return hours + "hours " + mins + "mins " + seconds + "secs";
	}
}
