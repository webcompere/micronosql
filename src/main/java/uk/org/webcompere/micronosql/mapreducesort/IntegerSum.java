package uk.org.webcompere.micronosql.mapreducesort;

/**
 * Transformation for summing integers
 *
 * @param <T> type of object to run over
 */
public class IntegerSum<T> extends TransformationBase<T, Integer> {
	/**
	 * Construct using a mapping to convert from the object into integer
	 * @param mapToInteger
	 */
	public IntegerSum(Mapping<T, Integer> mapToInteger) {
		super(mapToInteger);
	}

	@Override
	public Integer aggregate(T item, Integer previousTransform) {
		return previousTransform + transform(item);
	}
}
