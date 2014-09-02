package uk.org.webcompere.micronosql.mapreducesort;

/**
 * Convert all objects of type T into a single transformation result
 * @param <T> type to transform
 * @param <R> type of the result that will be built by transforming multiple objects
 */
public interface Transformation<T, R> {
	/**
	 *	Convert single item into a transformation - used on the first in a list 
	 * @param item - to transform
	 */
	R transform(T item);
	
	/**
	 * Add a single item to the 
	 * @param item to transform
	 * @param previousTransform thing to aggregate to
	 * @return the result of transform
	 */
	R aggregate(T item, R previousTransform);
}
