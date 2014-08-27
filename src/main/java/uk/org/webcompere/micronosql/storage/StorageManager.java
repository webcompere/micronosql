package uk.org.webcompere.micronosql.storage;

import java.util.Set;

public interface StorageManager {
	/**
	 * Store the item in the repository
	 * @param key key of the item
	 * @param payload representation of the item
	 * @param type type of item
	 */
	<T> void store(String key, String payload, Class<T> type);

	
	/**
	 * Find the item from the storage
	 * @param key key of the items
	 * @param type type to look up
	 * @return item in encoded representation
	 */
	<T> String find(String key, Class<T> type);

	/**
	 * Find all keys for all objects in the data store
	 * @param type type of object
	 * @return all known keys - always returns a set
	 */
	<T> Set<String> findAllKeys(Class<T> type);
	
	/**
	 * Delete the item if it exists
	 * @param key key of the item
	 * @param type type of the item
	 */
	<T> void delete(String key, Class<T> type);
	
}
