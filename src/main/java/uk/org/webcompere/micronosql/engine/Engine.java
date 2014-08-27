package uk.org.webcompere.micronosql.engine;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import uk.org.webcompere.micronosql.mapreducesort.Predicate;
import uk.org.webcompere.micronosql.mapreducesort.StringAscending;
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
	 * Find all keys from the data source in the given order
	 * @param type type of data
	 * @param sortOrder predicate/comparator for sorting
	 * @return keys in order
	 */
	<T> List<String> findAllKeys(Class<T> type, Comparator<String> sortOrder);
	
	/**
	 * Find all items in the data source
	 * @param type type of item
	 * @return a list of the items, which acts as a facade over the data source
	 */
	<T> ListWithKeys<T> findAll(Class<T> type);

	/**
	 * Find all items in the data source, sorted by key in the given order
	 * @param type type of data
	 * @param keySortOrder sortOrder predicate/comparator for sorting
	 * @return a list of the items, which acts as a facade over the data source
	 */
	<T> ListWithKeys<T> findAll(Class<T> type, Comparator<String> keySortOrder);

	/**
	 * Find all items in the data source which match the predicate and sort by the sort order
	 * @param type type of data
	 * @param predicate whether to include the item in the search or not
	 * @return a list of the items, which acts as a facade over the data source - note adding items
	 * to this list will break its role as a filtered list
	 */
	<T> ListWithKeys<T> find(Class<T> type, Predicate<T> predicate);
}
