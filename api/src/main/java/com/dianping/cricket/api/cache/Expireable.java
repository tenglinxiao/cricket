package com.dianping.cricket.api.cache;

/**
 * Interface to describe cache expiration feature.
 * @author uknow
 * @since 0.0.1
 */
public interface Expireable {
	// Determine whether cache it is expired.
	public boolean isExpired();
}
