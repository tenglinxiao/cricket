package com.dianping.cricket.mdx;

import org.olap4j.OlapDatabaseMetaData;
import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.NamedListImpl;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Database;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Schema;

import com.dianping.cricket.dal.annotations.Field;
import com.dianping.cricket.dal.annotations.Relationship;
import com.dianping.cricket.dal.annotations.Table;

@Table(name = "CATALOG", db = "mdx")
public class MdxCatalog extends MdxBase implements Catalog, Named {
	// Schemas held by this catalog.
	@Relationship("CATALOG-SCHEMA")
	private NamedList<MdxSchema> schemas = new NamedListImpl<MdxSchema>();
	// Database.
	private MdxDatabase database;
	
	public MdxCatalog(String name) {
		super(name);
	}
	
	public MdxCatalog(int id) {
		super(id);
	}
	
	public void addSchema(MdxSchema schema) {
		this.schemas.add(schema);
	}

	@Override
	public NamedList<Schema> getSchemas() throws OlapException {
		return Olap4jUtil.cast(schemas);
	}

	@Override
	public OlapDatabaseMetaData getMetaData() {
		return null;
	}

	@Override
	public Database getDatabase() {
		return database;
	}
	
	public Schema getSchema(String name) {
		return schemas.get(name);
	}
}
