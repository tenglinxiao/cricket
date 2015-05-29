package com.dianping.cricket.scheduler.pojo;

import java.util.Date;

public class Recipient {
	private Job job;
	private String recipient;
	private String mail;
	private Date createdTime;
	
	public Recipient(){
		this.job = new Job();
	}
	public Recipient(int jobId, String mail) {
		this.job = new Job(jobId);
		this.mail = mail;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public void setId(int id) {
		job.setId(id);
	}
	public int getId() {
		return job.getId();
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public static Recipient fakeRecipient(String mail) {
		return new Recipient(-1, mail);
	}
}
