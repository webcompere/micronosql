package uk.org.webcompere.micronosql.mapreducesort;

import java.util.Comparator;

/**
 * Sort strings into ascending order
 */
public class StringAscending implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		return o1.compareTo(o2);
	}

}
