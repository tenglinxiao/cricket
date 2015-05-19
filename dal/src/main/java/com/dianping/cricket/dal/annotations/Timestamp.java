package com.dianping.cricket.dal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that time should be auto updated.
 * @author tenglinxiao
 * @since 0.0.1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Timestamp {
	// default value for timestamp field.
	String value() default "CURRENT_TIMESTAMP";
	boolean autorefesh() default false;
}
