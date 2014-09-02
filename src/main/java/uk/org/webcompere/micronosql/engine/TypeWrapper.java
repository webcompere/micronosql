package uk.org.webcompere.micronosql.engine;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import uk.org.webcompere.micronosql.annotation.Key;
import uk.org.webcompere.micronosql.annotation.KeyGenerator;
import uk.org.webcompere.micronosql.annotation.NoKeyGenerator;
import uk.org.webcompere.micronosql.pojo.ExampleDocumentWithKeyGeneration;

/**
 * Helps the engine to interpret documents
 */
public class TypeWrapper {
	private Class<?> clazz;
	private Method keyReadMethod;
	private Method keyWriteMethod;
	private KeyGenerator keyGenerator;
	
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
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
					keyReadMethod = propertyDescriptor.getReadMethod();
					keyWriteMethod = propertyDescriptor.getWriteMethod();
					constructKeyGenerator(annotation);
					
				} catch (IntrospectionException e) {
					// fails to introspect, so skip this one
				}
				break;
			}
		}
	}

	private void constructKeyGenerator(Annotation annotation) {
		Class<?> keyGeneratorClazz = ((Key)annotation).generator();
		try {
			if (!keyGeneratorClazz.equals(NoKeyGenerator.class)) {
				keyGenerator = (KeyGenerator)keyGeneratorClazz.newInstance();
			}
		} catch (InstantiationException e) {
			// ignore - it means we can't set the key generator
		} catch (IllegalAccessException e) {
			// ignore - it means we can't set the key generator
		}
	}

	/**
	 * Read the key field from the object
	 * @param object to read from
	 * @return key value as a string
	 */
	public String getKey(Object object) {
		if (!object.getClass().equals(clazz)) {
			throw new IllegalArgumentException("Cannot read key for object of type " + object.getClass().getCanonicalName() +" with type wrapper for " + clazz.getCanonicalName());
		}

		try {
			Object o = keyReadMethod.invoke(object);
			return o==null ? null : o.toString();
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot read key field for object of type " + clazz.getCanonicalName(), e);
		}
	}

	/**
	 * Use the key generator to construct a new key and write it into the object
	 * @param object to write new key into
	 */
	public void writeNewKey(Object object) {
		if (keyWriteMethod==null || keyGenerator==null) {
			throw new IllegalArgumentException("Cannot generate key, need to specify a key generator with default constructor and a write method for the key field");
		}
		try {
			keyWriteMethod.invoke(object, keyGenerator.generate());
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot write key field for object of type " + clazz.getCanonicalName(), e);
		}
	}
}
