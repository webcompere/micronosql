package uk.org.webcompere.micronosql.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.org.webcompere.micronosql.codec.Codec;
import uk.org.webcompere.micronosql.codec.JSONCodec;
import uk.org.webcompere.micronosql.mapreducesort.MappedItem;
import uk.org.webcompere.micronosql.mapreducesort.MappedItemComparator;
import uk.org.webcompere.micronosql.mapreducesort.Mapping;
import uk.org.webcompere.micronosql.mapreducesort.Predicate;
import uk.org.webcompere.micronosql.storage.ItemTransfer;
import uk.org.webcompere.micronosql.storage.StorageManager;

/**
 * Implementation of data repository using a storage manager to
 * keep all the records in a persistence layer, probably filesystem
 * but could be anything.
 */
public class EngineImpl implements Engine {
	private StorageManager storageManager;
	private Codec codec = new JSONCodec();


	private Map<Class<?>, TypeWrapper> typeWrappers = new HashMap<>();
	
	public EngineImpl(StorageManager storageManager) {
		this.storageManager = storageManager;
	}
	
	@Override
	public <T> T find(Object key, Class<T> type) {
		String keyAsString = key.toString();
		
		return storageManager.find(keyAsString, type, codec).getItem();
	}

	@Override
	public <T> String store(T object) {
		try {
			// this gets around a linguistic problem - the class MUST be of type T
			@SuppressWarnings("unchecked")
			Class<T> type = (Class<T>)object.getClass();
			
			String key = keyFromObject(object, type);
			
			if (key == null) {
				return generateKeyAndStore(object);
			}
			
			storageManager.store(key, new ItemTransfer<T>(codec, object), type);
			
			return key;
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not store object", e);
		}
	}
	
	private <T> String generateKeyAndStore(T object) {
		Class<?> type = object.getClass();
		TypeWrapper wrapper = getTypeWrapper(type);
		
		boolean successfullyStored = false;
		do {
			try {
				wrapper.writeNewKey(object);
				return storeNew(object);
			} catch (LockingException exception) {
				// this means we've had a key clash, just try again
			}
			
		} while (!successfullyStored);
		
		// note - this is unreachable - we return 
		return null;
	}
	

	@Override
	public <T> String storeNew(T object) throws LockingException {
		if (exists(object)) {
			throw new LockingException("Duplicate object cannot be stored as new");
		}
		return store(object);
	}

	private <T> String keyFromObject(T object, Class<?> type) {
		TypeWrapper wrapper = getTypeWrapper(type);
		String key = wrapper.getKey(object);
		return key;
	}
	
	private <T> boolean exists(T object) {
		Class<?> type = object.getClass();
		String key = keyFromObject(object, type);
		return find(key, type) != null;
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
		return find(type, predicate, null, null);
	}
	

	@Override
	public <T, M> ListWithKeys<T> find(Class<T> type, Predicate<T> predicate,
			Mapping<T, M> mapping, Comparator<M> sortOrder) {
		
		List<String> allMatchingKeys = new ArrayList<String>();
		List<MappedItem<M>> mappedItems = new ArrayList<>();
		
		for(String key:findAllKeys(type)) {
			T item = find(key, type);
			if (predicate.includes(item)) {
				if (mapping!=null) {
					// add a mapped item when we have a mapping
					mappedItems.add(new MappedItem<M>(key, mapping.map(item)));
				} else {
					// add to the all keys when there's no mapping
					allMatchingKeys.add(key);
				}
			}
		}
		
		// when there's a mapping put the keys into the target list based on the mapped items
		if (mapping!=null) {
			sortAndConvert(sortOrder, allMatchingKeys, mappedItems);
		}
	
		
		return new OnDemandListAdapter<T>(type, this, allMatchingKeys);

	}
	
	public StorageManager getStorageManager() {
		return storageManager;
	}

	public void setStorageManager(StorageManager storageManager) {
		this.storageManager = storageManager;
	}

	public Codec getCodec() {
		return codec;
	}

	public void setCodec(Codec codec) {
		this.codec = codec;
	}

	private <M> void sortAndConvert(Comparator<M> sortOrder, List<String> allMatchingKeys, List<MappedItem<M>> mappedItems) {
		if (sortOrder!=null) {
			Collections.sort(mappedItems, new MappedItemComparator<M>(sortOrder));
		}
		
		for(MappedItem<M> item:mappedItems) {
			allMatchingKeys.add(item.getKey());
		}
	}


}
