package uk.org.webcompere.micronosql.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.org.webcompere.micronosql.storage.StorageManager;

public class EngineImpl implements Engine {
	private StorageManager storageManager;
	private Map<Class<?>, TypeWrapper> typeWrappers = new HashMap<>();
	
	public EngineImpl(StorageManager storageManager) {
		this.storageManager = storageManager;
	}
	
	@Override
	public <T> T find(Object key, Class<T> type) {
		TypeWrapper wrapper = getTypeWrapper(type);
		String keyAsString = key.toString();
		
		return storageManager.find(keyAsString, type);
	}

	@Override
	public <T> String store(T object) {
		TypeWrapper wrapper = getTypeWrapper(object.getClass());
		String key = wrapper.getKey(object);
		storageManager.store(key, object);
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
	public <T> List<T> findAll(Class<T> type) {
		return new OnDemandList<T>(type, this, findAllKeys(type));
	}

}
