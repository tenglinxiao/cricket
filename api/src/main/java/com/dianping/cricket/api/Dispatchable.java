package com.dianping.cricket.api;

import com.dianping.cricket.api.event.Event;

/**
 * Interface to describe a class that can dispatch event.
 * @author uknow
 * @since 0.0.1
 */
public interface Dispatchable {
	/**
	 * Dispatch event.
	 * @param event
	 */
	public void dispatchEvent(Event event);
}
