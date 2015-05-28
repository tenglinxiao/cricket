package com.dianping.cricket.scheduler.rest.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.core.MediaType;
public class JsonRequestFilter implements Filter {
	public static final String CONTENT_TYPE = "content-type";
	private static final String FORMAT = "$format";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {		
		String format = request.getParameter(FORMAT);
		
		// If the $format parameter is specified to json, then set the header content-type to application/json.
		if (format != null && format.equalsIgnoreCase("json")) {
			chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest)request) {
				public Enumeration<?> getHeaderNames() {
					Enumeration<String> headerNames = super.getHeaderNames();
					List<String> headers = Collections.list(headerNames);
					if (headers.contains(CONTENT_TYPE)) {
						return headerNames;
					} else {
						headers.add(CONTENT_TYPE);
						return Collections.enumeration(headers);
					}		
				}
				public Enumeration<?> getHeaders(String name)
	            {
	                // Detect the parameter of the content-type, and replace it with application/json type format.
	                if (CONTENT_TYPE.equalsIgnoreCase(name)) {
	                    List<String> list = new ArrayList<String>();
	                    list.add(MediaType.APPLICATION_JSON);
	                    return Collections.enumeration(list);
	                }
	                return super.getHeaders(name);
	            }
			}, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

}
