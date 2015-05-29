package com.dianping.cricket.ui.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;

/**
 * Proxy for forward request to target host.
 * @author tenglinxiao
 * @since 0.0.1
 */
public interface HttpProxy {
	// Issue get request.
	public HttpResponse get(HttpServletRequest request);
	
	// Issue post request.
	public HttpResponse post(HttpServletRequest request);
	
	// Issue put request.
	public HttpResponse put(HttpServletRequest request);
	
	// Issue delete request.
	public HttpResponse delete(HttpServletRequest request);

}
