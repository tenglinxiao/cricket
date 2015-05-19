package com.dianping.cricket.dal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.dianping.cricket.api.exception.InvalidCaseException;
import com.dianping.cricket.dal.annotations.After;
import com.dianping.cricket.dal.annotations.Before;
import com.dianping.cricket.dal.annotations.Id;
import com.dianping.cricket.dal.annotations.Table;
import com.dianping.cricket.dal.annotations.Timestamp;
import com.dianping.cricket.dal.exception.SqlFailedException;
import com.dianping.cricket.dal.sql.util.MysqlSqlTemplate;
import com.dianping.cricket.dal.sql.util.SqlPattern;

/**
 * Basic persist class for persisting obj, which leverages annotations to implement id & fields extraction, and define dependency cut point as well.
 * @author tenglinxiao
 * @since 0.0.1
 */
public abstract class AbstractPersistable implements Persistable, Identifiable {
	private static Logger logger = Logger.getLogger(AbstractPersistable.class);
	// Store for all the class mappings between class & persist info.
	private static Map<Class<?>, PersistInfo> persistInfoStore = new HashMap<Class<?>, PersistInfo>();
	// Current class persist info.
	private PersistInfo persistInfo;
	
	public AbstractPersistable() {
		// If class is already registered, fetch the persist info, else call init method to detect persist info.
		if (persistInfoStore.containsKey(this.getClass())) {
			persistInfo = persistInfoStore.get(this.getClass());
		} else {
			init();
		}
	}
	
	// Init method to get table, id, fields & dependency info.
	public void init() {
		// Get annotation for Table, and extract the table used for storing the obj of this class.
		Table annotation = this.getClass().getAnnotation(Table.class);
		persistInfo = new PersistInfo();
		persistInfo.setDb(annotation.db());
		persistInfo.setTable(annotation.name());
		
		com.dianping.cricket.dal.annotations.Field field = null;
		Id id = null;
		
		Class<?> cls = this.getClass();
		
		// Leverage annotations to extract fields & methods mapping.
		while (cls != AbstractPersistable.class) {
			// Finish fields mapping.
			Field[] fields = cls.getDeclaredFields(); 
			for (Field f : fields) {
				if ((field = f.getAnnotation(com.dianping.cricket.dal.annotations.Field.class)) != null) {
					// Add Field annotated Field to fields list.
					persistInfo.getFields().add(new SimpleImmutableEntry<String, Field>(field.value(), f));
				} else if ((id = f.getAnnotation(Id.class)) != null) {
					// Add Id annotated Field to fields & id list. 
					persistInfo.getId().add(new SimpleImmutableEntry<String, Field>(id.value(), f));
					persistInfo.getFields().add(new SimpleImmutableEntry<String, Field>(id.value(), f));
				} else if (f.isAnnotationPresent(Before.class) || f.isAnnotationPresent(After.class)) {
					// Add it into dependency list only if the field type class is subclass of AbstractPersistable.
					if (AbstractPersistable.class.isAssignableFrom(f.getType())) {
						persistInfo.getDependencyFields().add(f);
					} else {
						logger.info("Ignore annotated field [" + f.getName() + "] due to its type is NOT a subclass of AbstractPersistable!");
					}
				}
			}
			
			// Finish methods mapping.
			Method[] methods = cls.getDeclaredMethods();
			for (Method m : methods) {
				if (m.isAnnotationPresent(Before.class) || m.isAnnotationPresent(After.class)) {
					if (m.getParameterTypes().length == 0) {
						persistInfo.getDependencyMethods().add(m);
					} else {
						logger.info("Ingore dependency method due to the cause that it has method parameters!");
					}
				}
			}
			
			// Find super class of the current class.
			cls = cls.getSuperclass();
		}
		
		// Put the class fields mapping into persist store.
		persistInfoStore.put(this.getClass(), persistInfo);
	}

	@Override
	public void load() throws RuntimeException {
		resolveDependency(true);
		
		// Create template for load op.
		MysqlSqlTemplate template = new MysqlSqlTemplate(SqlPattern.LOAD);
		String sql = template.table(persistInfo.getTable()).fields(persistInfo.getFieldsList()).conditions(getIdConditions()).apply().getSql();
		logger.info("Load: " + sql);
		
		SqlSession session = getSession();
		try {
			Map<String, Object> result = session.selectOne("raw.select", sql);
			if (result != null) {
				map2Fields(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidCaseException(e);
		} finally {
			session.close();
		}

		resolveDependency(false);
	}
	
	@Override
	public void save() throws RuntimeException {
		resolveDependency(true);
		
		// Create template for save sql.
		MysqlSqlTemplate template = new MysqlSqlTemplate(SqlPattern.SAVE);
		String sql = template.table(persistInfo.getTable()).fields(persistInfo.getFieldsList()).values(findFieldValues()).apply().getSql();
		logger.info("Save: " + sql);
		
		// Take save operation on db.
		SqlSession session = getSession();

		try {
			// If id fields length is 1 & it's auto generated field, use the exchangeable parameter api for fetching id.
			if (persistInfo.getId().size() == 1 && persistInfo.getId().get(0).getValue().getAnnotation(Id.class).autoGenerated()) {
				ExchangeableParameter parameter = new ExchangeableParameter(sql);
				session.insert("raw.insert_return", parameter);
				// Map id field to the obj.
				map2IdField(persistInfo.getId().get(0).getKey(), parameter.getId());
			} else {
				// Take raw insert ops if there is no auto generated id field.
				session.insert("raw.insert", sql);
			}
			
			// If has timestamp field, then reload the obj from db again.
			if (hasTimestampField()) {
				load();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SqlFailedException(e);
		} finally {
			session.close();
		} 

		resolveDependency(false);
	}
	
	@Override
	public void update() throws RuntimeException {
		resolveDependency(true);
		
	}

	@Override
	public void delete() throws RuntimeException {
		resolveDependency(true);
		
		// Create template for delete sql.
		MysqlSqlTemplate template = new MysqlSqlTemplate(SqlPattern.DELETE);
		String sql = template.table(persistInfo.getTable()).conditions(getIdConditions()).apply().getSql();
		logger.info("Delete: " + sql);
		
		// Take delete operation on db.
		SqlSession session = getSession();

		try {
			session.delete("raw.delete", sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SqlFailedException(e);
		} finally {
			session.close();
		} 
			
		resolveDependency(false);
	}
	
	public void map2Fields(Map<String, Object> result) {
			List<SimpleImmutableEntry<String, Field>> fields = persistInfo.getFields();
			for (SimpleImmutableEntry<String, Field> field : fields) {
				try {
					setFieldValue(field.getValue(), result.get(field.getKey()));
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("Each result field MUST be mapped to one class obj field.");
					throw new InvalidCaseException(e);
				}
			}
	}
	
	public void map2IdField(String field, Object value) {
		List<SimpleImmutableEntry<String, Field>> fields = persistInfo.getId();
		for (SimpleImmutableEntry<String, Field> f : fields) {
			try {
				setFieldValue(f.getValue(), value);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Each id field MUST be mapped to one class id field.");
				throw new InvalidCaseException(e);
			}
		}
	}
	
	public boolean hasTimestampField() {
		for (SimpleImmutableEntry<String, Field> entry : persistInfo.getFields()) {
			if (entry.getValue().isAnnotationPresent(Timestamp.class)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Object> findFieldValues() {
		List<Object> values = new ArrayList<Object>();
		for (SimpleImmutableEntry<String, Field> entry : persistInfo.getFields()) {
			try {
				values.add(getFieldValue(entry.getValue()));
			} catch (Exception e) {
				e.printStackTrace();
				throw new InvalidCaseException(e);
			}
		}
		return values;
	}
	
	public List<String> getIdConditions() {
		List<String> conditions = new ArrayList<String>();
		List<SimpleImmutableEntry<String, Object>> id = id();
		for (int index = 0; index < id.size(); index++) {
			SimpleImmutableEntry<String, Object> entry = id.get(index);
			// Create condition string in "field = value" format.
			conditions.add(MysqlSqlTemplate.getCondition(entry.getKey(), entry.getValue()));
		}
		return conditions;
	}
	
	public List<SimpleImmutableEntry<String, Object>> id() {
		try {
			List<SimpleImmutableEntry<String, Object>> id = new ArrayList<SimpleImmutableEntry<String, Object>>();
			for (SimpleImmutableEntry<String, Field> entry : persistInfo.getId()) {
				// Generate id with id fields in (field name, field value) pair.
				id.add(new SimpleImmutableEntry<String, Object>(entry.getValue().getName(), getFieldValue(entry.getValue())));
			}
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidCaseException(e);
		}
	}
	
	private Object getFieldValue(Field field) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		if (field.isAnnotationPresent(Timestamp.class)) {
			Timestamp timestamp = field.getAnnotation(Timestamp.class);
			if (field.get(this) == null || timestamp.autorefesh()) {
				return timestamp.value();
			}
		}
		
		return field.get(this);
	}
	
	private void setFieldValue(Field field, Object value) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		field.set(this, value);
	}
	
	public Field findField(String field) {
		for (SimpleImmutableEntry<String, Field> entry : persistInfo.getFields()) {
			if (entry.getKey().equals(field)) {
				return entry.getValue();
			}
		}
		
		throw new InvalidCaseException("Field CAN NOT find an match when do a reverse mapping, it's a miracle! (:p");
	}

	private void resolveDependency(boolean isBefore) {
		try {
			Class<? extends Annotation> cls = isBefore? Before.class: After.class;
			
			// Call dependency method.
			for (Method method : persistInfo.getDependencyMethods()) {
				if (method.isAnnotationPresent(cls)) {
					method.setAccessible(true);
					method.invoke(this);
				}
			}
			
			// Take same db ops on dependency field. 
			for (Field field : persistInfo.getDependencyFields()) {
				if (field.isAnnotationPresent(cls)) {
					AbstractPersistable persistable = (AbstractPersistable)getFieldValue(field);
					String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
					Method method = AbstractPersistable.class.getDeclaredMethod(methodName);
					method.invoke(persistable);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidCaseException(e);
		}
	}
	

	protected SqlSession getSession() {
		return SessionStore.getSessionStore().openSesion(persistInfo.getDb());
	}
	
	public static class PersistInfo {
		// Database id.
		private String db;
		// Table name.
		private String table;
		// Id fields mapping.
		private List<SimpleImmutableEntry<String, Field>> id = new ArrayList<SimpleImmutableEntry<String, Field>>(); 
		// Fields mapping.
		private List<SimpleImmutableEntry<String, Field>> fields = new ArrayList<SimpleImmutableEntry<String, Field>>();
		// Dependency list.
		private List<Field> dependencyFields = new ArrayList<Field>();
		// Dependency functions.
		private List<Method> dependencyMethods = new ArrayList<Method>();
		
		public String getDb() {
			return db;
		}
		public void setDb(String db) {
			this.db = db;
		}
		public String getTable() {
			return table;
		}
		public void setTable(String table) {
			this.table = table;
		}
		public List<SimpleImmutableEntry<String, Field>> getId() {
			return id;
		}
		public void setId(List<SimpleImmutableEntry<String, Field>> id) {
			this.id = id;
		}
		public List<SimpleImmutableEntry<String, Field>> getFields() {
			return fields;
		}
		public List<Field> getDependencyFields() {
			return dependencyFields;
		}
		public void setDependencyFields(List<Field> dependencyFields) {
			this.dependencyFields = dependencyFields;
		}
		public List<Method> getDependencyMethods() {
			return dependencyMethods;
		}
		public void setDependencyMethods(List<Method> dependencyMethods) {
			this.dependencyMethods = dependencyMethods;
		}
		public void setFields(List<SimpleImmutableEntry<String, Field>> fields) {
			this.fields = fields;
		}
		public List<String> getFieldsList() {
			List<String> fields = new ArrayList<String>();
			for (SimpleImmutableEntry<String, Field> field : this.fields) {
				fields.add(field.getKey());
			}
			return fields;
		}
	}
}
