package com.dianping.cricket.ui.common;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.JsonNode;

public class HttpProxyImpl implements HttpProxy {
	
	private CloseableHttpClient client = HttpClients.createDefault();
	private HttpHost host = new HttpHost("localhost", 8080);

	@Override
	public JsonNode get(HttpRequest request) {
		try {
		return client.execute(host, request, new ResponseHandler<JsonNode>() {

			@Override
			public JsonNode handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public HttpResponse post(HttpRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpResponse put(HttpRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpResponse delete(HttpRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
