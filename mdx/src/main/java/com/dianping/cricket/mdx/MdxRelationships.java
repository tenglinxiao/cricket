package com.dianping.cricket.mdx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.dianping.cricket.api.exception.InvalidCaseException;
import com.dianping.cricket.dal.annotations.Table;

@Table(name = "RELATIONSHIP", db = "mdx")
public class MdxRelationships extends MdxRelationship {
	private List<MdxRelationship> relationships = new ArrayList<MdxRelationship>();

	public MdxRelationships(String relationshipName, Class<? extends MdxBase> childClass) {
		super(relationshipName, childClass);
	}
	
	public void addRelationShip(MdxRelationship relationship) {
		this.relationships.add(relationship);
	}

	@Override
	public void load() throws RuntimeException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("parent", getParent());
		params.put("type", getRelationshipName());
		SqlSession session = getSession();
		try {
			List<MdxRelationship> relationships = session.selectList("mdx.relationships", params);
			for (MdxRelationship relationship : relationships) {
				MdxRelationship r = relationship.clone();
				r.setRelationshipName(getRelationshipName());
				r.setChildClass(getChildClass());
				r.setPersisted(true);
				this.relationships.add(r);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidCaseException(e);
		} finally {
			session.close();
		}
	}

	@Override
	public void save() throws RuntimeException {
		for (MdxRelationship relationship : relationships) {
			relationship.setParent(getParent());
			relationship.save();
		}
	}

	@Override
	public void update() throws RuntimeException {
		for (MdxRelationship relationship : relationships) {
			relationship.update();
		}
	}

	@Override
	public void delete() throws RuntimeException {
		for (MdxRelationship relationship : relationships) {
			relationship.delete();
		}
		
		relationships.clear();
	}
	
	public void loadChildren() {
		for (MdxRelationship relationship : relationships) {
			relationship.loadChild();
		}
	}
	
	public void merge(List<MdxBase> children) {
		if (children != null) {
			for (MdxBase child : children) {
				if (!contains(child)) {
					relationships.add(MdxRelationship.getRelationship(getParent(), child, getRelationshipName()));
				}
			}
		}
	}
	
	public boolean contains(MdxBase child) {
		for (MdxRelationship relationship : relationships) {
			if (relationship.getChildTarget().equals(child)) {
				return true;
			}
		}
		return false;
	}
	
	public void clear() {
		relationships.clear();
	}
}
