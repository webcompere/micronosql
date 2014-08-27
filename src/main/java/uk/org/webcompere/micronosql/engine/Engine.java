package uk.org.webcompere.micronosql.engine;

public interface Engine {

	/**
	 * Find a record via its key
	 * @param key value for the record
	 * @param type type of object to retrieve
	 * @return single record or null
	 */
	<T> T find(Object key, Class<T> type);

	/**
	 * Store an object in the storage
	 * will either store it new or write over the old one
	 * @param object to store
	 */
	<T> void store(T object);

}
