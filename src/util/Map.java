package util;

import java.util.Iterator;

import util.Map.Entry;

public class Map<K, V> implements Iterable<Entry<K, V>> {

	private java.util.HashMap<K, V> hashmap;

	public Map() {
		hashmap = new java.util.HashMap<>();
	}

	public V put(K key, V value) {
		return hashmap.put(key, value);
	}

	public V get(K key) {
		return hashmap.get(key);
	}

	public boolean containsKey(K key) {
		return hashmap.containsKey(key);
	}

	public static class Entry<K, V> {

		private K key;
		private V value;

		public Entry(K k, V v) {
			key = k;
			value = v;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new Iterator<Entry<K, V>>() {
			Iterator<java.util.Map.Entry<K, V>> iterator = hashmap.entrySet().iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Entry<K, V> next() {
				java.util.Map.Entry<K, V> entry = iterator.next();
				return new Entry<>(entry.getKey(), entry.getValue());
			}

			@Override
			public void remove() {
				iterator.remove();
			}
		};
	}

	public int size() {
		return hashmap.size();
	}

	public boolean isEmpty() {
		return hashmap.isEmpty();
	}

	public V remove(K key) {
		return hashmap.remove(key);
	}

	public void putAll(Map<K, V> map) {
		hashmap.putAll(map.hashmap);
	}

	public void clear() {
		hashmap.clear();
	}
}
