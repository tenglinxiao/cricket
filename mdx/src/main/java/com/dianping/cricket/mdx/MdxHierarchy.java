package com.dianping.cricket.mdx;

import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.NamedListImpl;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;

import com.dianping.cricket.dal.annotations.Field;
import com.dianping.cricket.dal.annotations.Table;
import com.dianping.cricket.mdx.util.MdxUtil;

@Table(name = "HIERARCHY", db = "mdx")
public class MdxHierarchy extends MdxBase implements Hierarchy, Named {
	// Description.
	private String description;
	// Parent dimension.
	private MdxDimension dimension;
	// Children levels.
	private NamedList<MdxLevel> levels = new NamedListImpl<MdxLevel>();
	// Default member.
	private MdxMember defaultMember;
	
	public MdxHierarchy(int id) {
		super(id);
	}
	
	public MdxHierarchy(String name, MdxDimension dimension) {
		super(name);
		this.dimension = dimension;
	}

	@Override
	public String getName() {
		return getName();
	}

	@Override
	public String getUniqueName() {
		return MdxUtil.formatUniqueName(dimension.getUniqueName(), getName());
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
		return true;
	}

	@Override
	public Dimension getDimension() {
		return dimension;
	}

	@Override
	public NamedList<Level> getLevels() {
		return Olap4jUtil.cast(levels);
	}

	@Override
	public boolean hasAll() {
		return false;
	}

	@Override
	public Member getDefaultMember() throws OlapException {
		return defaultMember;
	}

	@Override
	public NamedList<Member> getRootMembers() throws OlapException {
		NamedList<MdxMember> members = new NamedListImpl<MdxMember>();
		for (MdxLevel level : levels) {
			for (Member m : level.getMembers()) {
				members.add((MdxMember)m);
			}
		}
		return Olap4jUtil.cast(members);
	}
	
	public MdxLevel lookupLevel(String name)
	{
		for (MdxLevel level : levels) {
			if (level.getName().equals(name)) {
				return level;
			}
		}
		return null;
	}

}
