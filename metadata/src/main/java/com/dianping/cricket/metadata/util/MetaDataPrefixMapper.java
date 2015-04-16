package com.dianping.cricket.metadata.util;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

/**
 * Class to support better xml namespace format.
 * @author tenglinxiao
 * @since 1.0
 */
public class MetaDataPrefixMapper extends NamespacePrefixMapper {
	@Override
	public String getPreferredPrefix(String namespaceUri, String defaultPrefix, boolean isRequired) {
		if (namespaceUri.contains("XMLSchema-instance")) {
			return "o";
		}
		return "s";
	}
	
	public String[] getPreDeclaredNamespaceUris()
	{
		return new String[]{"http://www.w3.org/2001/XMLSchema", "http://www.w3.org/2001/XMLSchema-instance"};
	}
}