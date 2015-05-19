package com.dianping.cricket.mdx;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.dianping.cricket.api.exception.InvalidCaseException;
import com.dianping.cricket.dal.AbstractPersistable;
import com.dianping.cricket.dal.annotations.Field;
import com.dianping.cricket.dal.annotations.Id;
import com.dianping.cricket.dal.annotations.Table;

@Table(name = "RELATIONSHIP", db = "mdx")
public class MdxRelationship extends AbstractPersistable implements Cloneable {
	private static Map<String, Integer> relationshipTypes = new HashMap<String, Integer>();
	@Id("PARENT")
	private int parent;
	@Id("CHILD")
	private int child;
	@Field("RELATIONSHIP_TYPE")
	private int type;
	
	private String relationshipName;
	private boolean persisted;
	
	// Child class type.
	private Class<? extends MdxBase> childClass;
	private MdxBase childTarget;
	
	// Default constructor for mybatis.
	public MdxRelationship() {}
	
	public MdxRelationship(String relationshipName) {
		this.relationshipName = relationshipName;
	}
	public MdxRelationship(String relationshipName, Class<? extends MdxBase> childClass) {
		this(relationshipName);
		this.childClass = childClass;
	}
	
	public String getRelationshipName() {
		return relationshipName;
	}
	public void setRelationshipName(String relationshipName) {
		this.relationshipName = relationshipName;
	}
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public int getChild() {
		return child;
	}
	public void setChild(int child) {
		this.child = child;
	}
	public void setChildClass(Class<? extends MdxBase> childClass) {
		this.childClass = childClass;
	}
	public Class<? extends MdxBase> getChildClass() {
		return this.childClass;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public MdxBase getChildTarget() {
		return childTarget;
	}
	public void setChildTarget(MdxBase childTarget) {
		this.childTarget = childTarget;
		if (childClass == null) {
			this.childClass = this.childTarget.getClass();
		}
	}
	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}
	public boolean isPersisted() {
		return persisted;
	}
	public void autoComplete() {
		if (type == 0) {
			if (relationshipTypes.containsKey(relationshipName)) {
				this.type = relationshipTypes.get(relationshipName);
			} else {
				this.type = fetchRelationshipType();
				relationshipTypes.put(relationshipName, this.type);
			}
		}
	}
	
	@Override
	public void load() throws RuntimeException {
		super.load();
	}
	
	@Override
	public void save() throws RuntimeException {
		// Save the target child first.
		childTarget.save();
		
		// Set the child id in case the child is new created.
		setChild(childTarget.getId());
		
		// If the relationship is not persisted onto db, then take the save op.
		if (!persisted) {
			super.save();
			persisted = true;
		}
	}
	
	@Override
	public void update() throws RuntimeException {
		super.update();
	}
	
	@Override
	public void delete() throws RuntimeException {
		// Delete the child target first.
		childTarget.delete();
		
		// Delete the relationship.
		super.delete();
	}
	
	@SuppressWarnings("unchecked")
	public void loadChild() {
		try {
			Constructor<MdxBase> constructor = (Constructor<MdxBase>)childClass.getDeclaredConstructor(int.class);
			childTarget = constructor.newInstance(child);
			childTarget.load();
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidCaseException("Child class must define one constructor with parameter id!");
		}
	}
	
	public int fetchRelationshipType() {
		SqlSession session = getSession();
		try {
			return session.selectOne("relationshipType", relationshipName);
		} catch (Exception e) {
			throw new InvalidCaseException("CAN NOT find the relationship name in the db!");
		} finally {
			session.close();
		}
	}

	// Clone for db resultset.
	protected MdxRelationship clone() {
		MdxRelationship relationship = new MdxRelationship();
		relationship.setParent(getParent());
		relationship.setChild(getChild());
		relationship.setType(getType());
		return relationship;
	}
	
	public static MdxRelationship getRelationship(int parent, int child, String relationshipName) {
		MdxRelationship relationship = new MdxRelationship(relationshipName);
		relationship.setParent(parent);
		relationship.setChild(child);
		relationship.autoComplete();
		return relationship;
	}
	
	public static MdxRelationship getRelationship(int parent, MdxBase childTarget, String relationshipName) {
		MdxRelationship relationship = new MdxRelationship(relationshipName);
		relationship.setParent(parent);
		relationship.setChildTarget(childTarget);
		relationship.autoComplete();
		return relationship;
	}
}
