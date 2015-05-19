package com.dianping.cricket.scheduler.pojo;

import java.util.Date;

public class JobStatus {
	public static enum Status {
		OPEN, RUNNING, SUCCESS, FAILURE
	}
	
	// Job id.
	private int jobId;
	// Job start time.
	private Date startTime;
	// Job end time.
	private Date endTime;
	// Job status.
	private JobStatus.Status status = Status.OPEN;
	// Whether job is recovered.
	private boolean recovered;
	
	public JobStatus() {}
	
	public JobStatus(int jobId) {
		this.jobId = jobId;
	}
	
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
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
}
