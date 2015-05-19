package com.dianping.cricket.api;

import com.dianping.cricket.api.event.Event;

/**
 * Interface for define class in Observer pattern.
 * @author uknow
 * @since 0.0.1
 */
public interface Observable {
	// Add observer to some container.
	public void addObserver(Observer observer);
	
	// Notify changes.
    public void notify(Event event);
}
