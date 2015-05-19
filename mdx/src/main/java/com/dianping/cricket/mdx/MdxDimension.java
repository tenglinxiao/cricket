package com.dianping.cricket.mdx;

import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.NamedListImpl;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.NamedList;

import com.dianping.cricket.dal.annotations.After;
import com.dianping.cricket.dal.annotations.Field;
import com.dianping.cricket.dal.annotations.PersistMethod;
import com.dianping.cricket.dal.annotations.Relationship;
import com.dianping.cricket.dal.annotations.Table;
import com.dianping.cricket.mdx.util.MdxUtil;

@Table(name = "DIMENSION", db = "mdx")
public class MdxDimension extends MdxBase implements Dimension, Named {
	// Dimension desc.
	private String description;
	// Dimension type.
	private Type type;
	// Belongs to cube.
	private MdxCube cube;
	// Children hierarchies.
	@Relationship("DIMENSION-HIERARCHY")
	private NamedList<MdxHierarchy> hierarchies = new NamedListImpl<MdxHierarchy>();
	// Default hierarchy.
	private MdxHierarchy defaultHierarchy;
	
	public MdxDimension(int id) {
		super(id);
	}
	
	public MdxDimension(String name, MdxCube cube) {
		super(name);
		this.cube = cube;
	}

	@Override
	public String getUniqueName() {
		return MdxUtil.formatUniqueName(cube.getUniqueName(), getName());
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
	public NamedList<Hierarchy> getHierarchies() {
		return Olap4jUtil.cast(hierarchies);
	}

	@Override
	public Type getDimensionType() throws OlapException {
		return type;
	}

	@Override
	public Hierarchy getDefaultHierarchy() {
		return defaultHierarchy;
	}
	
	public Hierarchy lookupHierarchy(String name)
	{
		for (Hierarchy h : hierarchies) {
			if (h.getName().equals(name)) {
				return h;
			}
		}
		
		return null;
	}

}
