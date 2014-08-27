package uk.org.webcompere.micronosql.mapreducesort;

/**
 * Is an item to be included in a search or not
 * @param <T> type of item
 */
public interface Predicate<T> {
	/**
	 * Evaluate an item to see if to include it
	 * @param item item to evaluate
	 * @return true if the item should be included
	 */
	boolean includes(T item);
}
