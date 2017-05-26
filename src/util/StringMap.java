package util;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import util.StringMap.Entry;

public class StringMap<V> implements Iterable<Entry<V>> {

	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	private static final int MAXIMUM_CAPACITY = 1 << 30;

	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private transient Entry<V>[] table;

	private transient int size;

	private int threshold;

	private final float loadFactor;

	private transient volatile int modCount;

	public StringMap(int initialCapacity, float loadFactor) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal initial capacity: "
					+ initialCapacity);
		}
		if (initialCapacity > MAXIMUM_CAPACITY) {
			initialCapacity = MAXIMUM_CAPACITY;
		}
		if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
			throw new IllegalArgumentException("Illegal load factor: "
					+ loadFactor);
		}

		// Find a power of 2 >= initialCapacity
		int capacity = 1;
		while (capacity < initialCapacity) {
			capacity <<= 1;
		}

		this.loadFactor = loadFactor;
		threshold = (int) (capacity * loadFactor);
		table = new Entry[capacity];
	}

	public StringMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	public StringMap() {
		this.loadFactor = DEFAULT_LOAD_FACTOR;
		threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
		table = new Entry[DEFAULT_INITIAL_CAPACITY];
	}

	private static int hash(int h) {
		// This function ensures that hashCodes that differ only by
		// constant multiples at each bit position have a bounded
		// number of collisions (approximately 8 at default load factor).
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	private static int indexFor(int h, int length) {
		return h & (length - 1);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public V get(char[] key) {
		if (key == null) {
			return null;
		}
		int hash = hash(hashCode(key));
		for (Entry<V> e = table[indexFor(hash, table.length)];
			 e != null;
			 e = e.next) {
			char[] k;
			if (e.hash == hash && ((k = e.key) == key || equals(key, k))) {
				return e.value;
			}
		}
		return null;
	}

	public boolean containsKey(char[] key) {
		return getEntry(key) != null;
	}

	private Entry<V> getEntry(char[] key) {
		int hash = (key == null) ? 0 : hash(hashCode(key));
		for (Entry<V> e = table[indexFor(hash, table.length)];
			 e != null;
			 e = e.next) {
			char[] k;
			if (e.hash == hash
					&& ((k = e.key) == key || (key != null && equals(key, k)))) {
				return e;
			}
		}
		return null;
	}

	@Deprecated
	public V put(String key, V value) {
		return put(key.toCharArray(), value);
	}

	public V put(char[] key, V value) {
		if (key == null) {
			return null;
		}
		int hash = hash(hashCode(key));
		int i = indexFor(hash, table.length);
		for (Entry<V> e = table[i]; e != null; e = e.next) {
			char[] k;
			if (e.hash == hash && ((k = e.key) == key || equals(key, k))) {
				V oldValue = e.value;
				e.value = value;
				return oldValue;
			}
		}

		modCount++;
		addEntry(hash, key, value, i);
		return null;
	}

	private void resize(int newCapacity) {
		Entry[] oldTable = table;
		int oldCapacity = oldTable.length;
		if (oldCapacity == MAXIMUM_CAPACITY) {
			threshold = Integer.MAX_VALUE;
			return;
		}

		Entry<V>[] newTable = new Entry[newCapacity];
		transfer(newTable);
		table = newTable;
		threshold = (int) (newCapacity * loadFactor);
	}

	private void transfer(Entry<V>[] newTable) {
		Entry<V>[] src = table;
		int newCapacity = newTable.length;
		for (int j = 0; j < src.length; j++) {
			Entry<V> e = src[j];
			if (e != null) {
				src[j] = null;
				do {
					Entry<V> next = e.next;
					int i = indexFor(e.hash, newCapacity);
					e.next = newTable[i];
					newTable[i] = e;
					e = next;
				} while (e != null);
			}
		}
	}

	public void putAll(StringMap<V> m) {
		int numKeysToBeAdded = m.size();
		if (numKeysToBeAdded == 0) {
			return;
		}

		if (numKeysToBeAdded > threshold) {
			int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);
			if (targetCapacity > MAXIMUM_CAPACITY) {
				targetCapacity = MAXIMUM_CAPACITY;
			}
			int newCapacity = table.length;
			while (newCapacity < targetCapacity) {
				newCapacity <<= 1;
			}
			if (newCapacity > table.length) {
				resize(newCapacity);
			}
		}

		for (Entry<V> e : m) {
			put(e.getKey(), e.getValue());
		}
	}

	public V remove(char[] key) {
		Entry<V> e = removeEntryForKey(key);
		return (e == null ? null : e.value);
	}

	private Entry<V> removeEntryForKey(char[] key) {
		int hash = (key == null) ? 0 : hash(hashCode(key));
		int i = indexFor(hash, table.length);
		Entry<V> prev = table[i];
		Entry<V> e = prev;

		while (e != null) {
			Entry<V> next = e.next;
			char[] k;
			if (e.hash == hash
					&& ((k = e.key) == key || (key != null && equals(key, k)))) {
				modCount++;
				size--;
				if (prev == e) {
					table[i] = next;
				} else {
					prev.next = next;
				}
				return e;
			}
			prev = e;
			e = next;
		}

		return e;
	}

	public void clear() {
		modCount++;
		Entry[] tab = table;
		for (int i = 0; i < tab.length; i++) {
			tab[i] = null;
		}
		size = 0;
	}

	public boolean containsValue(V value) {
		if (value == null) {
			return false;
		}

		Entry[] tab = table;
		for (Entry tab1 : tab) {
			for (Entry e = tab1; e != null; e = e.next) {
				if (value.equals(e.value)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Iterator<Entry<V>> iterator() {
		return new HashIterator();
	}

	public static class Entry<V> {

		private final char[] key;
		private V value;
		private Entry<V> next;
		private final int hash;

		private Entry(int h, char[] k, V v, Entry<V> n) {
			value = v;
			next = n;
			key = k;
			hash = h;
		}

		public final char[] getKey() {
			return key;
		}

		public final V getValue() {
			return value;
		}

		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}
	}

	private void addEntry(int hash, char[] key, V value, int bucketIndex) {
		Entry<V> e = table[bucketIndex];
		table[bucketIndex] = new Entry<>(hash, key, value, e);
		if (size++ >= threshold) {
			resize(2 * table.length);
		}
	}

	private class HashIterator implements Iterator<Entry<V>> {

		Entry<V> next;
		int expectedModCount;
		int index;
		Entry<V> current;

		HashIterator() {
			expectedModCount = modCount;
			if (size > 0) {
				Entry<V>[] t = table;
				while (index < t.length) {
					if (t[index++] != null) {
						next = t[index - 1];
						break;
					}
				}
			}
		}

		@Override
		public final boolean hasNext() {
			return next != null;
		}

		@Override
		public Entry<V> next() {
			return nextEntry();
		}

		final Entry<V> nextEntry() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			Entry<V> e = next;
			if (e == null) {
				return null;
			}

			if ((next = e.next) == null) {
				Entry<V>[] t = table;
				while (index < t.length) {
					if (t[index++] != null) {
						next = t[index - 1];
						break;
					}
				}
			}
			current = e;
			return e;
		}

		@Override
		public void remove() {
			if (current == null) {
				throw new IllegalStateException();
			}
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			char[] k = current.key;
			current = null;
			StringMap.this.removeEntryForKey(k);
			expectedModCount = modCount;
		}

	}

	private static boolean equals(char[] s1, char[] s2) {
		return ConV.equals(s1, s2);
	}

	private static int hashCode(char[] s) {
		return ConV.hashCode(s);
	}
}
