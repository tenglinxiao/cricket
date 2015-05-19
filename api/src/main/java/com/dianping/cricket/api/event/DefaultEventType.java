package com.dianping.cricket.api.event;

/**
 * Default event type.
 * @author uknow
 * @since 0.0.1
 */
public enum DefaultEventType implements EventType {
	SYSTEM("system"), USER("user");
	
	private String category;
	private DefaultEventType(String category)
	{
		this.category = category;
	}
	
	public String getCategory()
	{
		return category;
	}
}
