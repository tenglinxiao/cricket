package com.dianping.cricket.dal;

/**
 * Interface to describe the dao classes that need to solve dependencies when carry on ops on db.
 * @author tenglinxiao
 * @since 0.0.1
 */
public interface Resolvable<T> {
	// Load dependency if necessary.
	public void resolveLoadDependency(T obj);
	
	// Update dependency if necessary. 
	public void resolveUpdateDependency(T obj);
	
	// Save dependency if necessary. 
	public void resolveSaveDependency(T obj);
	
	// Delete dependency if necessary.
	public void resolveDeleteDependency(T obj);
}
