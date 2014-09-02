package uk.org.webcompere.micronosql.mapreducesort;

/**
 * The max function - works on an mapping from the item to integer
 * @param <T> type of item to work on
 */
public class IntegerMax<T> extends TransformationBase<T, Integer> {

	public IntegerMax(Mapping<T, Integer> mapToInteger) {
		super(mapToInteger);
	}

	@Override
	public Integer aggregate(T item, Integer previousTransform) {
		return Math.max(previousTransform, transform(item));
	}

}
