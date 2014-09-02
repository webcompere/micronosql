package uk.org.webcompere.micronosql.annotation;

public interface KeyGenerator {
	/**
	 * Generate a new unique key for the object
	 * @return the new key
	 */
	Object generate();

}
