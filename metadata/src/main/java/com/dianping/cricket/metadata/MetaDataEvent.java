package com.dianping.cricket.metadata;

import com.dianping.cricket.api.Event;
import com.dianping.cricket.api.EventType;

public class MetaDataEvent extends Event {
	public static enum MetaDataEventType implements EventType {
		TABLE_CREATE, TABLE_DELETE, TABLE_UPDATE;
	}

	@Override
	public boolean match(Object obj) {
		if (!(obj instanceof MetaDataEventType)) {
			return false;
		}
		return this.getEventType().equals(obj);
	}
}
