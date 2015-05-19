package com.dianping.cricket.api;

import com.dianping.cricket.api.event.Event;

/**
 * Interface for define class in Observer pattern.
 * @author uknow
 * @since 0.0.1
 */
public interface Observer
{
    // Take the actions due to some event.
    public void doAction(Event event);
}
