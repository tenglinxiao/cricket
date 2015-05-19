package com.dianping.cricket.api.cache;


public abstract class HashKey<T> extends AbstractAuditable implements Hashable {
	// Map key
	private T key;
	
	public HashKey(T key) {
		this.key = key;
	}
	
	public T getKey() {
		return key;
	}

	public void setKey(T key) {
		this.key = key;
	}
	
	public int hashKey() {
		return this.toString().hashCode();
	}
	
	// Two objs are equal only if the data type and key are equal.
	public boolean hashEquals(Object obj) {
		if (!(obj instanceof HashKey))
			return false;
		return this.key.equals(((HashKey<?>)obj).getKey());
	}
	
	public int hashCode()
	{
		return hashKey();
	}
	
	public boolean equals(Object obj)
	{
		return hashEquals(obj);
	}
	
	public String toString()
	{
		return key.toString();
	}
}
