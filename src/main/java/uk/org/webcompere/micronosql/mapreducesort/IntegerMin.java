package uk.org.webcompere.micronosql.mapreducesort;


/**
 * The min function - works on an mapping from the item to integer
 * @param <T> type of item to work on
 */
public class IntegerMin<T> extends TransformationBase<T, Integer> {

	public IntegerMin(Mapping<T, Integer> mapToInteger) {
		super(mapToInteger);
	}

	@Override
	public Integer aggregate(T item, Integer previousTransform) {
		return Math.min(previousTransform, transform(item));
	}

}
