package uk.org.webcompere.micronosql.engine;

import java.util.List;
import java.util.Set;

import uk.org.webcompere.micronosql.pojo.ExampleDocument;


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
	<T> String store(T object);

	/**
	 * Delete an item from the datasource
	 * @param key key of item to delete
	 * @param type type of item
	 */
	<T> void delete(Object key, Class<T> type);

	/**
	 * Find all keys from the data source
	 * @param type type of data
	 * @return the keys of all the objects - unordered
	 */
	<T> Set<String> findAllKeys(Class<T> type);

	/**
	 * Find all items in the data source
	 * @param type type of item
	 * @return a list of the items, which is a just-in-time populated list
	 */
	<T> List<T> findAll(Class<T> type);

}
