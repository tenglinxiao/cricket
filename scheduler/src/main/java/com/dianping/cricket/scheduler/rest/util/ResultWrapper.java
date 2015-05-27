package com.dianping.cricket.scheduler.rest.util;

import javax.ws.rs.core.Response;

public class ResultWrapper {
	public static class JsonResult {
		private Response.Status status = Response.Status.OK;
		private Object data;
		private String msg = "";
		
		public JsonResult() {}
		public JsonResult(Object data) {
			this.data = data;
		}
		public Response.Status getStatus() {
			return status;
		}
		public void setStatus(Response.Status status) {
			this.status = status;
		}
		public Object getData() {
			return data;
		}
		public void setData(Object data) {
			this.data = data;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
	
	public static JsonResult wrap(Object data) {
		return new JsonResult(data);
	}
	
	public static JsonResult wrap(Response.Status status, String msg) {
		JsonResult result = new JsonResult();
		result.setStatus(status);
		result.setData("");
		result.setMsg(msg);
		return result;
	}


}
