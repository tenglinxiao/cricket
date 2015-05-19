package com.dianping.cricket.mdx;

import java.util.List;

import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.NamedListImpl;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Property;

import com.dianping.cricket.dal.annotations.Field;
import com.dianping.cricket.dal.annotations.Relationship;
import com.dianping.cricket.dal.annotations.Table;
import com.dianping.cricket.mdx.util.MdxUtil;

@Table(name = "LEVEL", db = "mdx")
public class MdxLevel extends MdxBase implements Level, Named {
	// Level desc.
	private String description;
	// Level type.
	private Type type;
	// Cardinality
	private int cardinality;
	// Properties.
	private NamedList<MdxProperty> properties = new  NamedListImpl<MdxProperty>();
	// Members.
	@Relationship("LEVEL-MEMBER")
	private NamedList<MdxMember> members = new NamedListImpl<MdxMember>();
	// Parent hierarchy.
	private MdxHierarchy hierarchy;
	
	public MdxLevel(int id) {
		super(id);
	}

	@Override
	public String getUniqueName() {
		return MdxUtil.formatUniqueName(hierarchy.getUniqueName(), getName());
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
	public int getDepth() {
		return 0;
	}

	@Override
	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	@Override
	public Dimension getDimension() {
		return getHierarchy().getDimension();
	}

	@Override
	public Type getLevelType() {
		return type;
	}

	@Override
	public boolean isCalculated() {
		return false;
	}

	@Override
	public NamedList<Property> getProperties() {
		return Olap4jUtil.cast(properties);
	}

	@Override
	public List<Member> getMembers() throws OlapException {
		return Olap4jUtil.cast(members);
	}

	@Override
	public int getCardinality() {
		return cardinality;
	}
	
	public MdxMember lookupMember(String name)
	{
		for (MdxMember m : members) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

}
