package uk.org.webcompere.micronosql.mapreducesort;

import java.util.Comparator;

/**
 * Comparator for integers
 */
public class IntDescending implements Comparator<Integer> {

	@Override
	public int compare(Integer o1, Integer o2) {
		return o2-o1;
	}

}
