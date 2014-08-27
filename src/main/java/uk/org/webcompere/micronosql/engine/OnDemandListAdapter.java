package uk.org.webcompere.micronosql.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Represents a set of data taken from the micronosql database
 * warning modifying this list will commit to the repository
 */
public class OnDemandListAdapter<T> implements ListWithKeys<T> {
	private Class<T> clazz;
	private Engine engine;
	private List<String> keys = new ArrayList<String>();
	
	public OnDemandListAdapter(Class<T> clazz, Engine engine, Set<String> keys) {
		this.clazz = clazz;
		this.engine = engine;
		this.keys.addAll(keys);
	}
	
	public OnDemandListAdapter(Class<T> clazz, Engine engine, List<String> keys) {
		this.clazz = clazz;
		this.engine = engine;
		this.keys = keys;
	}
	
	@Override
	public boolean add(T e) {
		keys.add(engine.store(e));
		return true;
	}
	@Override
	public void add(int index, T element) {
		keys.add(index, engine.store(element));
	}
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return addAll(size(), c);
	}
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		for(T t:c) {
			add(index++, t);
		}
		return true;
	}
	@Override
	public void clear() {
		for(String key:keys) {
			engine.delete(key, clazz);
		}
		keys.clear();
	}
	@Override
	public boolean contains(Object o) {
		for(T t:this) {
			if (t.equals(o)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o:c) {
			if (!contains(o)) {
				return false;
			}
		}
		
		return true;
	}
	@Override
	public T get(int index) {
		return engine.find(keys.get(index), clazz);
	}
	@Override
	public int indexOf(Object o) {
		for(int i=0; i<size(); i++) {
			if (get(i).equals(o)) {
				return i;
			}
		}
		return -1;
	}
	@Override
	public boolean isEmpty() {
		return keys.isEmpty();
	}
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Iterator<String> keyIterator = keys.iterator();
			
			@Override
			public boolean hasNext() {
				return keyIterator.hasNext();
			}

			@Override
			public T next() {
				return engine.find(keyIterator.next(), clazz);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not implemented - not available");				
			}
			
		};
	}

	@Override
	public int lastIndexOf(Object o) {
		for(int i=size()-1; i>=0; i--) {
			if (get(i).equals(o)) {
				return i;
			}
		}
		return -1;
	}
	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException("Not implemented - not available");	
	}
	@Override
	public ListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException("Not implemented - not available");	
	}
	@Override
	public boolean remove(Object o) {
		int index = indexOf(o);
		if (index!=-1) {
			String key = keys.get(index);
			keys.remove(index);
			engine.delete(key, clazz);
			return true;
		}
		return false;
	}
	@Override
	public T remove(int index) {
		T item = get(index);
		String key =keys. get(index);
		keys.remove(index);
		engine.delete(key, clazz);
		return item;
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		for(Object o:c) {
			remove(o);
		}
		return true;
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		for(int i=size()-1; i>=0; i--) {
			T item = get(i);
			if (!c.contains(item)) {
				remove(i);
			}
		}
		return true;
	}
	@Override
	public T set(int index, T element) {
		remove(index);
		add(index, element);
		return element;
	}
	@Override
	public int size() {
		return keys.size();
	}
	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return new OnDemandListAdapter<T>(clazz, engine, keys.subList(fromIndex, toIndex));
	}
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size()];
		for(int i=0; i<size(); i++) {
			array[i]=get(i);
		}
		return array;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		T[] array = Arrays.copyOf(a, size());
		for(int i=0; i<size(); i++) {
			array[i]=(T)get(i);
		}
		return array;
	}

	@Override
	public String getKey(int index) {
		return keys.get(index);
	}
}
