package uk.org.webcompere.micronosql.mapreducesort;

/**
 * Common implementation of transformation
 *
 * @param <R> result type
 */
public abstract class TransformationBase<T, R> implements Transformation<T, R> {
	protected final Mapping<T, R> mapping;
	
	public TransformationBase(Mapping<T, R> mapping) {
		this.mapping = mapping;
	}
	
	
	@Override
	public R transform(T item) {
		return mapping.map(item);
	}
	
}
