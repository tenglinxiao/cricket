package com.dianping.cricket.ui.action;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.dianping.cricket.ui.common.HttpProxyImpl;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class HttpProxyAction extends ActionSupport {
	private InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		inputStream = HttpProxyImpl.getHttpProxyImpl().execute(request).getEntity().getContent();
		return Action.SUCCESS;
		
	}

	@Override
	public void validate() {
		super.validate();
	}
	

}
