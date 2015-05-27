package com.dianping.cricket.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.dianping.cricket.dal.SessionStore;
import com.dianping.cricket.scheduler.pojo.Job;
import com.dianping.cricket.scheduler.pojo.JobJar;
import com.dianping.cricket.scheduler.pojo.JobStatus;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerPersistenceException;

public class SchedulerLoader {
	private static SchedulerLoader schedulerLoader;
	private SqlSessionFactory factory = SessionStore.getSessionStore().getSessionFactory("scheduler");
	private SchedulerLoader() {}
	
	// Load db jobs.
	public List<Job> loadJobs() throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			List<Job> jobs = session.selectList("scheduler.findJobs");
			return jobs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	// Create new job in db.
	public boolean createJob(Job job) throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			int effected = session.insert("scheduler.createJob", job);
			if (effected == 1) { 
				session.insert("scheduler.createJobStatus", new JobStatus(job.getId()));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	// Find jobs that require immediate second runnings.
	public List<JobStatus> findRecoveredJobs(Date startTime) throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			return session.selectList("scheduler.findRecoveredJobs", startTime);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	// Delete one job in db.
	public boolean deleteJob(int jobId) throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			int effected = session.delete("scheduler.deleteJob", jobId);
			if (effected == 1) { 
				session.delete("scheduler.deleteJobStatus", jobId);
				session.delete("scheduler.deleteHistoryStatus", jobId);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	// Update current job status.
	public boolean updateStatus(JobStatus status) throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			session.update("scheduler.updateStatus", status);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	// Get the sla & sle ratio.
	public List<Map<String, Object>> getServiceLevelStatistics() throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			return session.selectList("scheduler.sla_sle");
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	// Log job status in history table.
	public boolean logStatus(JobStatus status) throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			session.insert("scheduler.logStatus", status);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	// Log job status in history table.
	public Map<String, Object> createJobJar(String fileName, String storedFileName) throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("name", fileName);
			params.put("stored_name", storedFileName);
			session.insert("scheduler.createJobJar", params);
			return params;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public boolean existed(Job job) throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			return (int)session.selectOne("scheduler.isExisted", job) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public boolean deleteJobJar(String jarName) throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			return session.delete("scheduler.deleteJobJar", jarName) == 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<JobJar> findJobJarsObsolete() throws SchedulerPersistenceException {
		SqlSession session = null;
		try {
			session = factory.openSession();
			return session.selectList("scheduler.findObsoleteJars");
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulerPersistenceException(e);
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
