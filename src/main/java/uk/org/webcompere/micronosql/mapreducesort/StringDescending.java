package uk.org.webcompere.micronosql.mapreducesort;

import java.util.Comparator;

/**
 * Sort strings into descending order
 */
public class StringDescending implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		return o2.compareTo(o1);
	}

}
