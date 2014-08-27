package uk.org.webcompere.micronosql.engine;

import java.util.HashMap;
import java.util.Map;

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
	public <T> void store(T object) {
		TypeWrapper wrapper = getTypeWrapper(object.getClass());
		storageManager.store(wrapper.getKey(object), object);
	}
	
	private synchronized TypeWrapper getTypeWrapper(Class<?> clazz) {
		if (typeWrappers.containsKey(clazz)) {
			return typeWrappers.get(clazz);
		}
		TypeWrapper wrapper = new TypeWrapper(clazz);
		typeWrappers.put(clazz,  wrapper);
		return wrapper;
	}

}
