package com.dianping.cricket.mdx;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.NamedListImpl;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.mdx.IdentifierSegment;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Measure;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.Member.TreeOp;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.NamedSet;
import org.olap4j.metadata.Schema;

import com.dianping.cricket.dal.annotations.Field;
import com.dianping.cricket.dal.annotations.Relationship;
import com.dianping.cricket.dal.annotations.Table;
import com.dianping.cricket.mdx.exception.MdxNotSupportedException;
import com.dianping.cricket.mdx.util.MdxUtil;

@Table(name = "CUBE", db = "mdx")
public class MdxCube extends MdxBase implements Cube, Named {
	// Cube description.
	private String description;
	// Cube schema.
	private MdxSchema schema;
	// Dimensions. 
	@Relationship("CUBE-DIMENSION")
	private NamedList<MdxDimension> dimensions = new NamedListImpl<MdxDimension>();
	// Measures.
	private NamedList<MdxMeasure> measures = new NamedListImpl<MdxMeasure>();
	
	public MdxCube(int id) {
		super(id);
	}
	
	public MdxCube(String name, MdxSchema schema) {
		super(name);
		this.schema = schema;
	}

	@Override
	public String getUniqueName() {
		return MdxUtil.formatUniqueName(schema.getName(), getName());
	}

	@Override
	public String getCaption() {
		return getName();
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean isVisible() {
		return false;
	}
	
	@Override
	public Schema getSchema() {
		return schema;
	}

	@Override
	public NamedList<Dimension> getDimensions() {
		return Olap4jUtil.cast(dimensions);
	}

	@Override
	public NamedList<Hierarchy> getHierarchies() {
		NamedList<MdxHierarchy> hierarchies = new NamedListImpl<MdxHierarchy>();
		for (Dimension d : dimensions) {
			for (Hierarchy h: d.getHierarchies()) {
				hierarchies.add((MdxHierarchy)h);
			}
		}
		return Olap4jUtil.cast(hierarchies);
	}

	@Override
	public List<Measure> getMeasures() {
		return Olap4jUtil.cast(measures);
	}

	@Override
	public NamedList<NamedSet> getSets() {
		return null;
	}

	@Override
	public Collection<Locale> getSupportedLocales() {
		return Collections.emptyList();
	}

	@Override
	public Member lookupMember(List<IdentifierSegment> nameParts)
			throws OlapException {
		StringBuilder builder = new StringBuilder();
		for (IdentifierSegment identifier : nameParts) {
			builder.append(identifier);
		}
		return measures.get(builder.toString());
	}

	@Override
	public List<Member> lookupMembers(Set<TreeOp> treeOps,
			List<IdentifierSegment> nameParts) throws OlapException {
		throw new MdxNotSupportedException("Functions CHILDREN, SIBLINGS, PARENT, SELF, DESCENDANTS are not supported so far!");
	}

	@Override
	public boolean isDrillThroughEnabled() {
		return false;
	}



}
