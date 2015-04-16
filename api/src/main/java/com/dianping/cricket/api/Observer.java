package com.dianping.cricket.api;

public interface Observer
{
    // Take the actions due to some event.
    public void doAction(Event event);
}
