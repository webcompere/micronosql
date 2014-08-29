package uk.org.webcompere.micronosql.mapreducesort;

/**
 * The identity and mapping of an item that's been mapped in map-reduce style
 * @param <M> mapping type
 */
public class MappedItem<M> {
	private String key;
	private M mapping;
	
	public MappedItem(String key, M mapping) {
		super();
		this.key = key;
		this.mapping = mapping;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public M getMapping() {
		return mapping;
	}
	public void setMapping(M mapping) {
		this.mapping = mapping;
	}
}
