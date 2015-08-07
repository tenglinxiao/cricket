package com.dianping.cricket.ui.common;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.http.HttpMethod;

import com.dianping.cricket.ui.exception.URLMappingNotFoundException;
import com.dianping.cricket.ui.util.Util;

public class HttpProxyImpl implements HttpProxy, UrlMapper {
	private static Logger logger = Logger.getLogger(HttpProxyImpl.class);
	// Singleton proxy instance.
	private static HttpProxyImpl httpProxy;
	// Client instance.
	private CloseableHttpClient client = HttpClients.createDefault();

	@Override
	public HttpResponse get(HttpServletRequest request) {
		try {
			String url = convertUrl(request.getRequestURI());

			String queryString = request.getQueryString();

			if (queryString != null) {
				url += "?" + queryString;
			}

			// Create get request.
			HttpGet get = new HttpGet(url);
			
			// Add json header to content type.
			get.addHeader("Content-Type", "application/json");
			
			return client.execute(get);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to send get request for url: [" + e.getMessage() + "]!");
			return null;
		}
	}

	@Override
	public HttpResponse post(HttpServletRequest request) {
		try {
			String url = convertUrl(request.getRequestURI());
			
			// Create get request.
			HttpPost post = new HttpPost(url);
			
			// Add json header to content type.
			post.addHeader("Content-Type", "application/json");
			
			// Fill the entity with the request input stream.
			post.setEntity(new InputStreamEntity(request.getInputStream()));
			
			return client.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to send get request for url: [" + e.getMessage() + "]!");
			return null;
		}
	}

	@Override
	public HttpResponse put(HttpServletRequest request) {
		return null;
	}

	@Override
	public HttpResponse delete(HttpServletRequest request) {
		return null;
	}

	@Override
	public String convertUrl(String url) throws URISyntaxException, URLMappingNotFoundException {
		return Util.convertUrl(url);
	}
	
	
	public HttpResponse execute(HttpServletRequest request) {
		HttpMethod method = HttpMethod.valueOf(request.getMethod());
		switch (method) {
		case GET: 
			return get(request);
		case POST:
			return post(request);
		case PUT: 
			return put(request);
		case DELETE:
			return delete(request);
		default: return get(request);
		}
	}
	
	public static HttpProxyImpl getHttpProxyImpl() {
		if (httpProxy == null) {
			httpProxy = new HttpProxyImpl();
		}
		return httpProxy;
	}
}
