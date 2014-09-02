package uk.org.webcompere.micronosql.engine;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import uk.org.webcompere.micronosql.mapreducesort.Mapping;
import uk.org.webcompere.micronosql.mapreducesort.Predicate;
import uk.org.webcompere.micronosql.mapreducesort.StringAscending;
import uk.org.webcompere.micronosql.mapreducesort.Transformation;
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
	 * Store an object in the storage
	 * must be new - will throw exception if the object
	 * already exists
	 * @param object to store
	 */
	<T> String storeNew(T object) throws LockingException;	
	
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
	 * Find all items in the data source which match the predicate
	 * @param type type of data
	 * @param predicate whether to include the item in the search or not
	 * @return a list of the items, which acts as a facade over the data source - note adding items
	 * to this list will break its role as a filtered list
	 */
	<T> ListWithKeys<T> find(Class<T> type, Predicate<T> predicate);
	
	/**
	 * Find all items in the data source which match the predicate and sort by the mapped field with sort order
	 * @param type type of data
	 * @param predicate whether to include the item in the search or not
	 * @param mapping defines how to convert from the contents of the item into a sortable
	 * @param sortOrder runs over the sortable mapped items
	 * @return a list of the items, which acts as a facade over the data source - note adding items
	 * to this list will break its role as a filtered list
	 */
	<T,M> ListWithKeys<T> find(Class<T> type, Predicate<T> predicate, Mapping<T,M> mapping, Comparator<M> sortOrder);
	
	/**
	 * Go through all items in the list and perform the transformation on them returning it - this is most likely to be
	 * used to calculate sums, or bulk extractions etc
	 * @param type type of data
	 * @param predicate whether to include an item in the search
	 * @param transformation transformation function to run over the items
	 * @return a transformation
	 */
	<T,R> R createTransformation(Class<T> type, Predicate<T> predicate, Transformation<T,R> transformation);
}
