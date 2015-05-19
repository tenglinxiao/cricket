package com.dianping.cricket.api.cache;

public class CacheKeys<T, S> extends CacheKey<T>{
	// Next level key.
	private CacheKey<S> next;
	
	public CacheKeys(T key)
	{
		super(key);
	}
	
	public CacheKeys(T key, S subKey)
	{
		super(key);
		this.next = new CacheKey<S>(subKey);
	}
	
	public CacheKey<S> getNext() {
		return next;
	}

	public void setNext(CacheKey<S> next) {
		this.next = next;
	}
	
	// Use the primary key for hash key and then use sub key hash-key if exists.
	public int hashKey()
	{
		if (next != null) {
			return super.hashKey() + next.hashKey();
		}
		return super.hashKey();
	}
	
	// Determine whether two objs are equals by the sequence type, primary key, sub key.
	public boolean hashEquals(Object obj)
	{
		if (!(obj instanceof CacheKeys))
			return false;
		
		if (this.next != null) {
			return super.hashEquals(obj) && this.next.hashEquals(((CacheKeys<?, ?>)obj).getNext());
		}
		return super.hashEquals(obj);
	}
	
	public String toString()
	{
		if (next != null) {
			return super.toString() + "_" + next.toString();
		}
		return super.toString();
	}
}
