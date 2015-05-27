package com.dianping.cricket.scheduler.rest.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
	private ObjectMapper mapper;
	public ObjectMapperProvider() {
		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	@Override
	public ObjectMapper getContext(Class<?> cls) {
		return mapper;
	}

}
