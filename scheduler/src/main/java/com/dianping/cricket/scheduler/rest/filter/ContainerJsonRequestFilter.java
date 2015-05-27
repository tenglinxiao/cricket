package com.dianping.cricket.scheduler.rest.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.dianping.cricket.scheduler.rest.util.ResultWrapper;


@Provider
@PreMatching
public class ContainerJsonRequestFilter implements ContainerRequestFilter {
	private static Logger logger = Logger.getLogger(ContainerJsonRequestFilter.class);

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		// Get the content type header.
		String contentType = context.getHeaderString(JsonRequestFilter.CONTENT_TYPE);
		
		// Only multipart/json request is allowed.
		if (contentType == null || (!contentType.contains(MediaType.MULTIPART_FORM_DATA) && !contentType.contains(MediaType.APPLICATION_JSON))) {
			context.abortWith(Response.status(Status.BAD_REQUEST)
					.entity(ResultWrapper.wrap(Status.BAD_REQUEST, "Only json request is allowed for this service, use [Content-Type] header or [$format] parameter to issue an json request!"))
					.build());
			
			logger.warn("Request is rejected due to the content type header: [" + contentType + "]!");
		}
		
	}

}
