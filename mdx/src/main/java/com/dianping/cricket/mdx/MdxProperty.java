package com.dianping.cricket.mdx;

import java.util.Set;

import org.olap4j.impl.Named;
import org.olap4j.metadata.Datatype;
import org.olap4j.metadata.MetadataElement;
import org.olap4j.metadata.Property;

import com.dianping.cricket.mdx.util.MdxUtil;

public class MdxProperty implements Property, Named {
	// Property name.
	private String name;
	// Property desc.
	private String description;
	// Property data type.
    private Datatype datatype;
    // Property set type.
    private Set<TypeFlag> type;
    // Property content type.
    private ContentType contentType;
    // Property attached to.
    private MetadataElement parent;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUniqueName() {
		return MdxUtil.formatUniqueName(parent.getUniqueName(), name);
	}

	@Override
	public String getCaption() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public Datatype getDatatype() {
		return datatype;
	}

	@Override
	public Set<TypeFlag> getType() {
		return type;
	}

	@Override
	public ContentType getContentType() {
		return contentType;
	}
}
