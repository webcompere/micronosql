package uk.org.webcompere.micronosql.storage;

import java.util.Set;

import uk.org.webcompere.micronosql.codec.Codec;

public interface StorageManager {
	/**
	 * Store the item in the repository
	 * @param key key of the item
	 * @param payload representation of the item
	 * @param type type of item
	 */
	<T> void store(String key, ItemTransfer<T> payload, Class<T> type);

	
	/**
	 * Find the item from the storage
	 * @param key key of the items
	 * @param type type to look up
	 * @param codec to use to transfer the item
	 * @return item wrapped in item transfer
	 */
	<T> ItemTransfer<T> find(String key, Class<T> type, Codec codec);

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
