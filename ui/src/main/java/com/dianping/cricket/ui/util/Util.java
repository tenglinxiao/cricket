package com.dianping.cricket.ui.util;

import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;

import com.dianping.cricket.ui.ServiceEndPoint;
import com.dianping.cricket.ui.ServiceEndPointConf;
import com.dianping.cricket.ui.exception.URLMappingNotFoundException;

public class Util {
	public static String convertUrl(final String url) throws URISyntaxException, URLMappingNotFoundException  {
		if (url == null) {
			return "";
		}
		
		String prefix = ServiceEndPointConf.getConf().getContextPrefix();
		String handledUrl = url;
		
		// Strip the beginning slash.
		if (url.contains(prefix)) {
			handledUrl = handledUrl.substring(handledUrl.indexOf(prefix) + prefix.length());
		}
		
		String[] segments = handledUrl.split("/");

		Map<String, ServiceEndPoint> services = ServiceEndPointConf.getConf().getContextMapping();
		if (services.containsKey(segments[0])) {
			// Get mapping service end point.
			ServiceEndPoint endPoint = services.get(segments[0]);
			
			// Replace the first path segment to new path segment.
			segments[0] = endPoint.getContext();
			
			URIBuilder builder = new URIBuilder();
			builder.setScheme("http");
			builder.setHost(endPoint.getHost());
			builder.setPort(endPoint.getPort());
			builder.setPath(getPath(segments));
			return builder.build().toString();
		}
		
		throw new URLMappingNotFoundException("CAN NOT find a mapping url to request url: [" + url + "]");
	}
	
	public static String getPath(String[] segments) {
		StringBuilder builder = new StringBuilder();
		for (String segment : segments) {
			builder.append("/" + segment);
		}
		return builder.toString();
	}

}
