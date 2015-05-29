package com.dianping.cricket.ui.common;

/**
 * Mapper for proxy to convert url to the right context.
 * @author tenglinxiao
 * @since 0.0.1
 */
public interface UrlMapper {
	// Convert url.
	public String convertUrl(String url) throws Exception;
}
