package com.dianping.cricket.api.event;

/**
 * Base event class for all the event class defined in this system.
 * @author uknow
 * @since 0.0.1
 */
public abstract class Event {
	// Event name.
	private String eventName = this.getClass().getSimpleName();
	// Event type.
	private EventType eventType;
	// Event source target.
	private Object target;
	// Event attached data.
	private Object data;
	
	public Event() {
		this.eventType = DefaultEventType.SYSTEM;
	}
	public Event(EventType eventType) {
		this();
		this.eventType = eventType;
	}
	public Event(EventType eventType, Object data) {
		this(eventType);
		this.data = data;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	// Determine whether the event is expected.
	public abstract boolean match(Object obj);
}
