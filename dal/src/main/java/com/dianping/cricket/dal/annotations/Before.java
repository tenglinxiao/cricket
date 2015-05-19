package com.dianping.cricket.dal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation annotated for the method/field, which will be run or carry out some actions before persist methods.
 * @author tenglinxiao
 * @since 0.0.1
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {
	PersistMethod value();
}
