package com.dianping.cricket.mdx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.NamedListImpl;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.mdx.ParseTreeNode;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Property;

import com.dianping.cricket.dal.annotations.Field;
import com.dianping.cricket.dal.annotations.Table;
import com.dianping.cricket.mdx.util.MdxUtil;

@Table(name = "MEMBER", db = "mdx")
public class MdxMember extends MdxBase implements Member, Named {
	// Member desc.
	private String description;
	// Member type.
	private Type type;
	// Ordinal
	private int ordinal;
	// Expression.
	private ParseTreeNode expression;
	// Member level.
	private MdxLevel level;
	// Children members.
	private NamedList<MdxMember> childrenMembers;
	// Children properties.
	private Map<Property, Object> properties = new HashMap<Property, Object>();
	// Parent member.
	private MdxMember parent;
	
	public MdxMember(int id) {
		super(id);
	}
	
	public MdxMember(String name, MdxLevel level) {
		super(name);
		this.level = level;
	}

	@Override
	public String getName() {
		return getName();
	}

	@Override
	public String getUniqueName() {
		return MdxUtil.formatUniqueName(level.getUniqueName(), getName());
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
	public NamedList<? extends Member> getChildMembers() throws OlapException {
		return childrenMembers;
	}

	@Override
	public int getChildMemberCount() throws OlapException {
		return childrenMembers.size();
	}

	@Override
	public Member getParentMember() {
		return parent;
	}

	@Override
	public Level getLevel() {
		return level;
	}

	@Override
	public Hierarchy getHierarchy() {
		return level.getHierarchy();
	}

	@Override
	public Dimension getDimension() {
		return getHierarchy().getDimension();
	}

	@Override
	public Type getMemberType() {
		return type;
	}

	@Override
	public boolean isAll() {
		return false;
	}

	@Override
	public boolean isChildOrEqualTo(Member member) {
		return false;
	}

	@Override
	public boolean isCalculated() {
		if (type == Type.FORMULA) {
			return true;
		}
		return false;
	}

	@Override
	public int getSolveOrder() {
		return 0;
	}

	@Override
	public ParseTreeNode getExpression() {
		return expression;
	}

	@Override
	public List<Member> getAncestorMembers() {
		return null;
	}

	@Override
	public boolean isCalculatedInQuery() {
		return false;
	}

	@Override
	public Object getPropertyValue(Property property) throws OlapException {
		return properties.get(property);
	}

	@Override
	public String getPropertyFormattedValue(Property property)
			throws OlapException {
		return properties.get(property).toString();
	}

	@Override
	public void setProperty(Property property, Object value)
			throws OlapException {
		properties.put(property, value);
		
	}

	@Override
	public NamedList<Property> getProperties() {
		NamedList<MdxProperty> props = new NamedListImpl<MdxProperty>();
		for (Property p : properties.keySet()) {
			props.add((MdxProperty)p);
		}
		return Olap4jUtil.cast(props);
	}

	@Override
	public int getOrdinal() {
		return ordinal;
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public Member getDataMember() {
		return null;
	}

}
