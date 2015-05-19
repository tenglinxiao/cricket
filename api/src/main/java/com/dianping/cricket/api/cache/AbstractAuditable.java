package com.dianping.cricket.api.cache;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractAuditable implements Auditable {
	private static int referenceSize = 8;
	private List<Field> fields = new ArrayList<Field>();
	private Set<Object> stackFields = new HashSet<Object>();
	static {
		String arch = System.getProperty("sun.arch.data.model");
		// If jvm is 32 bit, set reference size to 4.
		if (arch.contains("32")) {
			referenceSize = 4;
		}
	}

	@Override
	public long getTotalSize() {
		findFields();
		try {
			int size = 0;
			for (Field field : fields) {
				size += obtainSize(field, this) + referenceSize;
			}
			stackFields.clear();
			return size;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int obtainSize(Field field, Object target) throws IllegalArgumentException, IllegalAccessException {
		Object value = target;
		int size = 0;
		// If the field is not null, then get the field value size.
		if (field != null) {
			field.setAccessible(true);
			value = field.get(target);
			// If the field value is not null, add into stack fields.
			if (value != null) {
				// If the field is already estimated, return 0.
				if (!stackFields.add(value)) {
					return referenceSize;
				}
			}
		}
		
		// If the value is null, return size 0.
		if (value == null) {
			return 0;
		}
		
		if (field != null) {
			// If the field is primitive type, then estimate size.
			if (field.getType().isPrimitive()) {
				return CacheUtil.getTypeSize(field.getType()) + referenceSize; 
			}
			
			if (field.getType() == String.class) {
				return CacheUtil.getStringSize(value.toString()); 
			}
			
			// If field is array type, iterate for estimate the size.
			if (field.getType().isArray()) {
				// If it's primitive type array, estimate it directly.
				if (field.getType().getComponentType().isPrimitive()) {
					return Array.getLength(value) * CacheUtil.getTypeSize(field.getType().getComponentType()) + referenceSize;
				} else {
					for (Object obj : (Object[])value) {
						size += obtainSize(null, obj);
					}
					return size;
				}
			}
		}
		
		Class<?> valueClz = value.getClass();
		
		// If it is the reference type, recursive dig into the details to estimate the size.
		while (valueClz != Object.class) {
			for (Field f : valueClz.getDeclaredFields()) {
				// Estimate not static field.
				if (!Modifier.isStatic(f.getModifiers())) {
					size += obtainSize(f, value) + referenceSize;
				}
			}
			
			valueClz = valueClz.getSuperclass();
		}
		
		return size;
	}
	
	public void findFields() {
		if (fields.size() != 0) {
			return;
		}
		Class<?> cls = this.getClass();
		while (cls != AbstractAuditable.class) {
			for (Field field : cls.getDeclaredFields()) {
				fields.add(field);
			}
			
			cls = cls.getSuperclass();
		}
	}

}
