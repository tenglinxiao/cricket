package com.dianping.cricket.mdx;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.NamedListImpl;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Schema;

import com.dianping.cricket.dal.annotations.Field;
import com.dianping.cricket.dal.annotations.Relationship;
import com.dianping.cricket.dal.annotations.Table;

@Table(name = "SCHEMA", db = "mdx")
public class MdxSchema extends MdxBase implements Schema, Named {
	// Belongs to catalog.
	private Catalog catalog;
	// Cubes held by schema.
	@Relationship("SCHEMA-CUBE")
	private NamedList<MdxCube> cubes = new NamedListImpl<MdxCube>();
	// TODO use this feature in future.
	private NamedList<MdxDimension> sharedDimensions = new NamedListImpl<MdxDimension>();
	
	public MdxSchema(int id) {
		super(id);
	}
	public MdxSchema(String name, Catalog catalog) {
		super(name);
		this.catalog = catalog;
	}

	@Override
	public Catalog getCatalog() {
		return catalog;
	}

	@Override
	public NamedList<Cube> getCubes() throws OlapException {
		return Olap4jUtil.cast(cubes);
	}

	@Override
	public NamedList<Dimension> getSharedDimensions() throws OlapException {
		return Olap4jUtil.cast(sharedDimensions);
	}

	@Override
	public Collection<Locale> getSupportedLocales() throws OlapException {
		return Collections.emptyList();
	}
	
	public Cube getCube(String name) {
		return cubes.get(name);
	}
	
	public void addCube(MdxCube cube) {
		this.cubes.add(cube);
	}

}
