package uk.org.webcompere.micronosql.engine;

import java.util.List;

/**
 * Represents a list of items with a key field for each
 * @param <T> item type
 */
public interface ListWithKeys<T> extends List<T> {
	/**
	 * What is the key field for the given index
	 * @param index to find the key for
	 * @return key value for that index
	 */
	String getKey(int index);
}
