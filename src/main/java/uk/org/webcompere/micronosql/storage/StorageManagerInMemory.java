package uk.org.webcompere.micronosql.storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

public class StorageManagerInMemory extends StorageManagerBase implements StorageManager {
	private Map<Class<?>, Map<String, String>> storage = new HashMap<>();
	@Override
	public <T> void store(String key, T object) {
		Map<String, String> table = getTable(object);
		try {
			String document = convertToString(object);
			writeToTable(table, key, document);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot store " + key, e);
		}
	}

	private <T> Map<String, String> getTable(T object) {
		return getTable(object.getClass());
	}
	
	private <T> Map<String, String> getTable(Class<T> type) {
		synchronized(storage) {
			if (storage.containsKey(type)) {
				return storage.get(type);
			}
			Map<String, String> table = new HashMap<String, String>();
			storage.put(type, table);
			return table;
		}
	}
	
	private <T> void writeToTable(Map<String, String> table, String key, String payload) {
		synchronized(table) {
			table.put(key, payload);
		}
	}
	
	private String readFromTable(String key, Map<String, String> table) {
		synchronized(table) {
			if (!table.containsKey(key)) {
				return null;
			}
			return table.get(key);
		}
	}

	@Override
	public <T> T find(String key, Class<T> type) {
		Map<String, String> table = getTable(type);
		String payload = readFromTable(key, table);
		if (payload == null) {
			return null;
		}
		
		return convertToObject(type, payload);
	}

	
}
