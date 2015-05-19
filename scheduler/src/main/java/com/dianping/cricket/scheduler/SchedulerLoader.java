package com.dianping.cricket.scheduler;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.dianping.cricket.dal.SessionStore;
import com.dianping.cricket.scheduler.pojo.Job;
import com.dianping.cricket.scheduler.pojo.JobStatus;

public class SchedulerLoader {
	private static SchedulerLoader schedulerLoader;
	private SqlSessionFactory factory = SessionStore.getSessionStore().getSessionFactory("scheduler");
	private SchedulerLoader() {}
	
	// Load db jobs.
	public List<Job> loadJobs() {
		SqlSession session = null;
		try {
			session = factory.openSession();
			List<Job> jobs = session.selectList("scheduler.findJobs");
			return jobs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	// Create new job in db.
	public void createJob(Job job) {
		SqlSession session = null;
		try {
			session = factory.openSession();
			int effected = session.insert("scheduler.createJob", job);
			if (effected == 1) { 
				session.insert("scheduler.createJobStatus", new JobStatus(job.getId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	// Update current job status.
	public void updateStatus(JobStatus status) {
		SqlSession session = null;
		try {
			session = factory.openSession();
			session.update("scheduler.updateStatus", status);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	// Log job status in history table.
	public void logStatus(JobStatus status) {
		SqlSession session = null;
		try {
			session = factory.openSession();
			session.insert("scheduler.logStatus", status);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public static SchedulerLoader getLoader() {
		if (schedulerLoader == null) {
			schedulerLoader = new SchedulerLoader();
		}
		return schedulerLoader;
	}
}
