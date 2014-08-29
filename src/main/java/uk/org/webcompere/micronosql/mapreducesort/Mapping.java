package uk.org.webcompere.micronosql.mapreducesort;

/**
 * For converting items from the storage into
 * something that can be further processed
 *
 * @param <T> object this will be used over
 * @param <M> target type of the mapping - could be a primitive or a POJO
 */
public interface Mapping<T, M> {
	/**
	 * Convert whole object into the bit of interest
	 * @param item item to convert
	 * @return thing of interest
	 */
	M map(T item);
}
