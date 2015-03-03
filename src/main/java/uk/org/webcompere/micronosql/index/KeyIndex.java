package uk.org.webcompere.micronosql.index;

import java.util.ArrayList;
import java.util.List;

import uk.org.webcompere.micronosql.annotation.Key;

/**
 * Represents an index of keys relating to a particular entity
 */
public class KeyIndex<T> {
	@Key
	private Class<T> type;
	
	// keys in order
	private List<String> keys = new ArrayList<String>();
	
	public KeyIndex(Class<T> type) {
		this.type = type;
	}
	
	public KeyIndex() {
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}
}
