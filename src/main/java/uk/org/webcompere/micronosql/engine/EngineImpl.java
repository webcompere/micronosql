package uk.org.webcompere.micronosql.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import uk.org.webcompere.micronosql.mapreducesort.Predicate;
import uk.org.webcompere.micronosql.storage.StorageManager;

/**
 * Implementation of data repository using a storage manager to
 * keep all the records in a persistence layer, probably filesystem
 * but could be anything.
 */
public class EngineImpl implements Engine {
	private StorageManager storageManager;
	
	protected final ObjectMapper mapper = new ObjectMapper();

	private Map<Class<?>, TypeWrapper> typeWrappers = new HashMap<>();
	
	public EngineImpl(StorageManager storageManager) {
		this.storageManager = storageManager;
	}
	
	@Override
	public <T> T find(Object key, Class<T> type) {
		String keyAsString = key.toString();
		
		String objectAsString = storageManager.find(keyAsString, type);
		if (objectAsString == null) {
			return null;
		}
		return convertToObject(type, objectAsString);
	}

	@Override
	public <T> String store(T object) {
		try {
			Class<?> type = object.getClass();
			
			String key = keyFromObject(object, type);
			String payload = convertToString(object);
			
			storageManager.store(key, payload, type);
			return key;
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not store object", e);
		}
	}

	private <T> String keyFromObject(T object, Class<?> type) {
		TypeWrapper wrapper = getTypeWrapper(type);
		String key = wrapper.getKey(object);
		return key;
	}
	
	private synchronized TypeWrapper getTypeWrapper(Class<?> clazz) {
		if (typeWrappers.containsKey(clazz)) {
			return typeWrappers.get(clazz);
		}
		TypeWrapper wrapper = new TypeWrapper(clazz);
		typeWrappers.put(clazz,  wrapper);
		return wrapper;
	}

	@Override
	public <T> void delete(Object key, Class<T> type) {
		String keyAsString = key.toString();
		
		storageManager.delete(keyAsString, type);
	}

	@Override
	public <T> Set<String> findAllKeys(Class<T> type) {
		return storageManager.findAllKeys(type);
	}


	@Override
	public <T> List<String> findAllKeys(Class<T> type, Comparator<String> sortOrder) {
		List<String> allKeys = new ArrayList<String>();
		allKeys.addAll(findAllKeys(type));
		Collections.sort(allKeys, sortOrder);;
		return allKeys;
	}

	
	@Override
	public <T> ListWithKeys<T> findAll(Class<T> type) {
		return new OnDemandListAdapter<T>(type, this, findAllKeys(type));
	}
	
	@Override
	public <T> ListWithKeys<T> findAll(Class<T> type, Comparator<String> keySortOrder) {
		return new OnDemandListAdapter<T>(type, this, findAllKeys(type, keySortOrder));
	}
	

	@Override
	public <T> ListWithKeys<T> find(Class<T> type, Predicate<T> predicate) {
		List<String> allMatchingKeys = new ArrayList<String>();
		for(String key:findAllKeys(type)) {
			if (predicate.includes(find(key, type))) {
				allMatchingKeys.add(key);
			}
		}
		
		ListWithKeys<T> result = new OnDemandListAdapter<T>(type, this, allMatchingKeys);
		
		return result;
	}
	
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
