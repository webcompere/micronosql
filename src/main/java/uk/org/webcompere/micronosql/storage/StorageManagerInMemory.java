package uk.org.webcompere.micronosql.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StorageManagerInMemory implements StorageManager {
	private Map<Class<?>, Map<String, String>> storage = new HashMap<>();

	@Override
	public <T> void store(String key, String payload, Class<T> type) {
		Map<String, String> table = getTable(type);
		try {
			writeToTable(table, key, payload);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot store " + key, e);
		}
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


	@Override
	public <T> String find(String key, Class<T> type) {
		Map<String, String> table = getTable(type);
		return readFromTable(key, table);
	}


	@Override
	public <T> Set<String> findAllKeys(Class<T> type) {
		Set<String> keys = new HashSet<String>();
		Map<String, String> table = getTable(type);
		readKeysFromTable(table, keys);
		return keys;
	}
	
	@Override
	public <T> void delete(String key, Class<T> type) {
		Map<String, String> table = getTable(type);
		deleteFromTable(key, table);		
	}

	
	private void deleteFromTable(String key, Map<String, String> table) {
		synchronized(table) {
			if (table.containsKey(key)) {
				table.remove(key);
			}
		}
	}

	private void writeToTable(Map<String, String> table, String key, String payload) {
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
	
	private void readKeysFromTable(Map<String, String> table, Set<String> keys) {
		synchronized(table) {
			keys.addAll(table.keySet());
		}
	}


}
