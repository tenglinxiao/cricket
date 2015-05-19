package com.dianping.cricket.mdx;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dianping.cricket.api.exception.InvalidCaseException;
import com.dianping.cricket.dal.AbstractPersistable;
import com.dianping.cricket.dal.annotations.Field;
import com.dianping.cricket.dal.annotations.Id;
import com.dianping.cricket.dal.annotations.Relationship;
import com.dianping.cricket.dal.annotations.Timestamp;

public class MdxBase extends AbstractPersistable {
	private static Map<Class<? extends MdxBase>, java.lang.reflect.Field> relationshipStore = new HashMap<Class<? extends MdxBase>, java.lang.reflect.Field>();
	@Id
	private int id;
	
	@Field("NAME")
	private String name;
	
	@Timestamp
	@Field("CREATED_TIME")
	private Date createdTime;
	
	@Timestamp(autorefesh = true)
	@Field("UPDATED_TIME")
	private Date updatedTime;
	
	// Whether obj is persisted to db.
	private boolean persisted;
	
	// Relationships for current obj.
	private MdxRelationships relationships = new MdxRelationships(findRelationshipName(), findRelationshipChildClass());
	
	public MdxBase(int id) {
		this.id = id;
	}
	
	public MdxBase(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime (Date updatedTime) {
		this.updatedTime = updatedTime;
	} 
	
	public boolean getPersisted() {
		return this.persisted;
	}
	
	public boolean isPersisted() {
		return persisted;
	}
	
	@Override
	public void load() throws RuntimeException {
		// Load the main obj first.
		super.load();
		
		// Set persisted to true.
		persisted = true;
		
		// Set the parent id.
		relationships.setParent(id);
		
		// Load the relationships.
		relationships.load();
		
		// Load the relationship linked children.
		relationships.loadChildren();
	}

	@Override
	public void save() throws RuntimeException {
		// Save the main obj first.
		if (!persisted) {
			super.save();
			persisted = true;
		}
		
		// Set parent id because may have new relationships/child.
		getRelationships().setParent(id);
		
		// Save relationships.
		getRelationships().save();
	}

	@Override
	public void update() throws RuntimeException {
		super.update();
		getRelationships().update();
	}

	@Override
	public void delete() throws RuntimeException {
		// Delete the relationship first.
		getRelationships().delete();
		
		// Delete the main object.
		super.delete();
	}
	
	public MdxRelationships getRelationships() {
		// Merge children into the existed relationship members.
		relationships.merge(getChildren());
		return relationships;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<MdxBase> getChildren() {
		try {
			java.lang.reflect.Field field = findRelationship();
			if (field == null) {
				return null;
			}
			field.setAccessible(true);
			return (List<MdxBase>)field.get(this);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidCaseException(e);
		}
	}
	
	// Find the field that annotated by relationship.
	public java.lang.reflect.Field findRelationship() {
		// If no relationship info detected, try to search for it.
		if (!relationshipStore.containsKey(this.getClass())) {
			java.lang.reflect.Field[] fields = this.getClass().getDeclaredFields();
			for (java.lang.reflect.Field field : fields) {
				if (field.isAnnotationPresent(Relationship.class)) {
					relationshipStore.put(this.getClass(), field);
				}
			}
		}
		
		return relationshipStore.get(this.getClass());
	}
	
	// Find relationship name specified by annotation.
	public String findRelationshipName() {
		return findRelationship().getAnnotation(Relationship.class).value();
	}
	
	// Find relationship generic class parameter type.
	@SuppressWarnings("unchecked")
	public Class<? extends MdxBase> findRelationshipChildClass() {
		java.lang.reflect.Field field = findRelationship();
		return (Class<? extends MdxBase>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
	}
	
	public boolean equals(Object obj) {
		if (obj.getClass() == this.getClass()) {
			MdxBase base = (MdxBase)obj;
			if (base.getId() == this.getId()) {
				return true;
			}
		}
		return false;
	}
}
