package uk.org.webcompere.micronosql.storage;

import uk.org.webcompere.micronosql.codec.Codec;

/**
 * Represents an item that's being transferred
 *
 * @param <T> type of item
 */
public class ItemTransfer<T> {
	private Codec codec;
	private T item;
	
	public ItemTransfer(Codec codec, T item) {
		super();
		this.codec = codec;
		this.item = item;
	}

	public T getItem() {
		return item;
	}
	
	public Codec getCodec() {
		return codec;
	}

	/**
	 * Convert item to string
	 * @return string representation of item
	 */
	public String encodeToString() {
		return codec.encode(item);
	}
	
	/**
	 * Factory method: construct an item transfer object from a string
	 * @param type type of item
	 * @param codec codec used to construct the item
	 * @param content item - can be null
	 * @return wrapper for the item
	 */
	public static <T> ItemTransfer<T> fromString(Class<T> type, Codec codec, String content) {
		return new ItemTransfer<T>(codec, content == null ? null : codec.decode(content, type));
	}
}
