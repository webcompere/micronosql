package uk.org.webcompere.micronosql.mapreducesort;

import java.util.Comparator;

/**
 * Sort comparator for mapped items
 * relies on a comparator for the mapping type
 *
 * @param <M> how the item is mapped
 */
public class MappedItemComparator<M> implements Comparator<MappedItem<M>> {

	private Comparator<M> internalComparator;
	
	public MappedItemComparator(Comparator<M> comparator) {
		internalComparator = comparator;
	}
	
	@Override
	public int compare(MappedItem<M> o1, MappedItem<M> o2) {
		return internalComparator.compare(o1.getMapping(), o2.getMapping());
	}

}
