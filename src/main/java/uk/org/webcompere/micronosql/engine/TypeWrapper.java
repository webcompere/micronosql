package uk.org.webcompere.micronosql.engine;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import uk.org.webcompere.micronosql.annotation.Key;

/**
 * Helps the engine to interpret documents
 */
public class TypeWrapper {
	private Class<?> clazz;
	private Method keyReadMethod;
	
	/**
	 * Construct with the type to wrap
	 * @param clazz type
	 */
	public TypeWrapper(Class<?> clazz) {
		this.clazz=clazz;
		
		findKeyField(clazz);
	}

	private void findKeyField(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field:fields) {
			Annotation annotation = field.getAnnotation(Key.class);
			if (annotation!=null) {
				try {
					keyReadMethod = new PropertyDescriptor(field.getName(), clazz).getReadMethod();
				} catch (IntrospectionException e) {
					// fails to introspect, so skip this one
				}
				break;
			}
		}
	}

	public String getKey(Object object) {
		if (!object.getClass().equals(clazz)) {
			throw new IllegalArgumentException("Cannot read key for object of type " + object.getClass().getCanonicalName() +" with type wrapper for " + clazz.getCanonicalName());
		}

		try {
			return keyReadMethod.invoke(object).toString();
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot read key field for object of type " + clazz.getCanonicalName(), e);
		}
	}
}
