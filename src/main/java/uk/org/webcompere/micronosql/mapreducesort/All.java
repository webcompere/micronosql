package uk.org.webcompere.micronosql.mapreducesort;

/**
 * Predicate for returning all items
 * @param <T> type of item it processees
 */
public class All<T> implements Predicate<T> {

	@Override
	public boolean includes(T item) {
		return true;
	}

}
