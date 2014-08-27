package uk.org.webcompere.micronosql.storage;

public interface StorageManager {


	/**
	 * Store the item in the storage
	 * @param key key to store under
	 * @param object value to store with
	 */
	<T> void store(String key, T object);

	
	/**
	 * Find the item from the storage
	 * @param key key of the items
	 * @param type type to look up
	 * @return item
	 */
	<T> T find(String key, Class<T> type);
}
