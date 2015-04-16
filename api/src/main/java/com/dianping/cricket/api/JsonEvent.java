package com.dianping.cricket.api;

public class JsonEvent extends Event {
	private String nodeName;
	public JsonEvent(String nodeName, Object data) {
		this.nodeName = nodeName;
		setData(data);
	}
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	@Override
	public boolean match(Object obj) {
		if (nodeName != null && nodeName.equals(obj)) {
			return true;
		}
		return false;
	}
}
