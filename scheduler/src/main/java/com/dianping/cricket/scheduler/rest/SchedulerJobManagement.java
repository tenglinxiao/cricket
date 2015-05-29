package com.dianping.cricket.scheduler.rest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.cricket.scheduler.Scheduler;
import com.dianping.cricket.scheduler.SchedulerLoader;
import com.dianping.cricket.scheduler.pojo.Job;
import com.dianping.cricket.scheduler.pojo.Recipient;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerInvalidJobDefinitionException;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerInvalidJobJarException;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerJobNotFoundException;
import com.dianping.cricket.scheduler.rest.exceptions.SchedulerPersistenceException;
import com.dianping.cricket.scheduler.rest.util.JobUtil;
import com.dianping.cricket.scheduler.rest.util.ResultWrapper;
import com.dianping.cricket.scheduler.rest.util.ResultWrapper.JsonResult;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class SchedulerJobManagement {
	private static Logger logger = Logger.getLogger(SchedulerJobManagement.class);
	
	@Autowired
	private Scheduler scheduler;
	
	@POST
	@Path("createJob")
	@Consumes(MediaType.APPLICATION_JSON)
	public JsonResult createJob(Job job) {
		try {
			// Run the job verification.
			job.loadable();
			
			SchedulerLoader loader = SchedulerLoader.getLoader();
			if (!loader.existed(job)) {
				if (job.getType() == Job.Type.JAR_JOB && !loader.deleteJobJar(job.getMainEntry())) {
					return ResultWrapper.wrap(Status.NOT_FOUND, "CAN NOT find a match with jar name: [" + job.getMainEntry() + "]!");
				}
				// Create the job in db.
				loader.createJob(job);
				
				// Schedule new created job.
				scheduler.deployJob(job, false);
				
				return ResultWrapper.wrap(true);
			} else {
				return ResultWrapper.wrap(Status.CONFLICT, "Job with key [" + job.getJobKey() + "] is already defined in scheduler!");
			}
		} catch (SchedulerInvalidJobJarException e) {
			e.printStackTrace();
			return ResultWrapper.wrap(Status.NOT_ACCEPTABLE, "Invalid job jar: [" + e.getMessage() + "]!");
		} catch (SchedulerInvalidJobDefinitionException e) {
			e.printStackTrace();
			return ResultWrapper.wrap(Status.NOT_ACCEPTABLE, "Invalid job definition: [" + e.getMessage() + "]!");
		} catch (SchedulerPersistenceException e) {
			e.printStackTrace();
			logger.error("Failed to create job onto db: [" + e.getMessage() + "]!");
			return ResultWrapper.wrap(Status.INTERNAL_SERVER_ERROR, "Failed to create job onto db!");
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error("Failed to schedule created job: [" + e.getMessage() + "]!");
			return ResultWrapper.wrap(Status.INTERNAL_SERVER_ERROR, "Job created, but failed to schedule the new created job!");
		}
	}
	
	@POST
	@Path("uploadJar")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public JsonResult uploadJar(@FormDataParam("file") InputStream in, @FormDataParam("file") FormDataContentDisposition disposition) {
		java.nio.file.Path path = JobUtil.getJobJarPath(disposition.getFileName());

		if (!JobUtil.isJarFile(path)) {
			return ResultWrapper.wrap(Status.BAD_REQUEST, "Uploaded file is constrained to jar file only!");
		}
		
		// Append timestamp to path.
		path = JobUtil.appendTimestamp(path);
		
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(path.toFile());
			byte[] bytes = new byte[1024];
			int size = 0;
			while((size = in.read(bytes)) != -1) {
				output.write(bytes, 0, size);
			}
			output.close();
			Map<String, Object> result = SchedulerLoader.getLoader().createJobJar(disposition.getFileName(), path.getFileName().toString());
			return ResultWrapper.wrap(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultWrapper.wrap(Status.INTERNAL_SERVER_ERROR, "Failed to upload file due to internal errors: [" + e.getMessage() + "]");
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@POST
	@Path("deleteJob")
	@Consumes(MediaType.APPLICATION_JSON)
	public JsonResult deleteJob(int jobId) {
		try {
			// Undeploy the job from scheduler.
			scheduler.undeployJob(jobId);
			
			// Delete job from db.
			SchedulerLoader.getLoader().deleteJob(jobId);

			return ResultWrapper.wrap(true);
		} catch (SchedulerJobNotFoundException e) {
			return ResultWrapper.wrap(Status.NOT_FOUND, "CAN NOT find the job requested on scheduler!");
		} catch (SchedulerPersistenceException e) {
			e.printStackTrace();
			logger.error("Failed to delete job from db: [" + e.getMessage() + "]!");
			return ResultWrapper.wrap(Status.INTERNAL_SERVER_ERROR, "Failed to communicate to db to delete the job!");
		} 
	}
	
	@POST
	@Path("createRecipient")
	@Consumes(MediaType.APPLICATION_JSON)
	public JsonResult createRecipient(Recipient recipient) {
		try {
			SchedulerLoader.getLoader().createRecipient(recipient);
			return ResultWrapper.wrap(true);
		} catch (SchedulerPersistenceException e) {
			e.printStackTrace();
			logger.error("Failed to create recipient: [" + e.getMessage() + "]!");
			return ResultWrapper.wrap(Status.INTERNAL_SERVER_ERROR, "Failed to communicate to db to create recipient");
		}
	}
	
	@POST
	@Path("deleteRecipient")
	@Consumes(MediaType.APPLICATION_JSON)
	public JsonResult deleteRecipient(Recipient recipient) {
		try {
			SchedulerLoader.getLoader().deleteRecipient(recipient);
			return ResultWrapper.wrap(true);
		} catch (SchedulerPersistenceException e) {
			e.printStackTrace();
			logger.error("Failed to delete recipient: [" + e.getMessage() + "]!");
			return ResultWrapper.wrap(Status.INTERNAL_SERVER_ERROR, "Failed to communicate to db to delete recipient");
		}
	}

}
