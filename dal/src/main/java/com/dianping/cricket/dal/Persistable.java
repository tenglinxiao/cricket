package com.dianping.cricket.dal;

/**
 * Interface to describe the dao ops for persisting data.
 * @author tenglinxiao
 * @since 0.0.1
 */
public interface Persistable {
	// Load record.
	public void load() throws RuntimeException;

	// Update record.
	public void update() throws RuntimeException;
	
	// Save record.
	public void save() throws RuntimeException;
	
	// Delete record.
	public void delete() throws RuntimeException;
}
