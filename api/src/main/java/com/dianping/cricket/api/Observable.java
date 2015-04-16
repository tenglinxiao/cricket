package com.dianping.cricket.api;

public interface Observable {
	// Add observer to some containers.
	public void addObserver(Observer observer);
	
	// Notify changes.
    public void notify(Event event);
}
