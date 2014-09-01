package uk.org.webcompere.micronosql.codec;

/**
 * Convert an object to and from its representation
 */
public interface Codec {
	/**
	 * Convert the object to the representation (String)
	 * @param object to convert
	 * @return representation
	 */
	<T> String encode(T object);
	
	/**
	 * Convert to the object from the representation (String)
	 * @param encoded string with the representation in it
	 * @param type type of object to decode
	 * @return an object built from the data in the encoding
	 */
	<T> T decode(String encoded, Class<T> type);
}
