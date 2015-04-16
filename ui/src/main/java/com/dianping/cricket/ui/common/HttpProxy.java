package com.dianping.cricket.ui.common;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.codehaus.jackson.JsonNode;

/**
 * Proxy for forward request to target host.
 * @author tenglinxiao
 * @since 0.0.1
 */
public interface HttpProxy {
	
	// Issue get request.
	public JsonNode get(HttpRequest request);
	
	// Issue post request.
	public HttpResponse post(HttpRequest request);
	
	// Issue put request.
	public HttpResponse put(HttpRequest request);
	
	// Issue delete request.
	public HttpResponse delete(HttpRequest request);

}
