package com.dianping.cricket.dal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the class that corresponds to some table.
 * @author tenglinxiao
 * @since 0.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	// name means the table name.
	public String name();
	
	// indicates the db that table belongs to.
	public String db();

}
