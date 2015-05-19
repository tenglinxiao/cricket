package com.dianping.cricket.dal;

import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

public interface Identifiable {
	// Get id values.
	public List<SimpleImmutableEntry<String, Object>> id();
}
