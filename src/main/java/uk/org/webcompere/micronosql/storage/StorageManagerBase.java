package uk.org.webcompere.micronosql.storage;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public abstract class StorageManagerBase {
	protected final ObjectMapper mapper = new ObjectMapper();

	protected <T> T convertToObject(Class<T> type, String payload) {
		try {
			return mapper.reader(type).readValue(payload);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to read payload for type "+ type.getCanonicalName());
		}
	}
	
	protected <T> String convertToString(T object) throws IOException,
			JsonGenerationException, JsonMappingException {
		String document = mapper.writeValueAsString(object);
		return document;
	}
}
