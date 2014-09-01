package uk.org.webcompere.micronosql.engine;

import org.codehaus.jackson.map.ObjectMapper;

/** 
 * Implementation of the Codec using JSON representation
 */
public class JSONCodec implements Codec {
	
	protected final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public <T> String encode(T object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException("Could not write", e);
		}
	}

	@Override
	public <T> T decode(String encoded, Class<T> type) {
		try {
			return mapper.reader(type).readValue(encoded);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to read payload for type "+ type.getCanonicalName());
		}
	}

}
