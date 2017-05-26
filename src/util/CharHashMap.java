package util;

public class CharHashMap {

	private final byte number_bits;
	private char[] keys;
	private int[] values;

	public CharHashMap(int size) {
		byte bits = ConV.get2FoldBit(size);
		keys = new char[1 << bits];
		values = new int[1 << bits];
		number_bits = bits;
	}

	/** Inserts the key into the hashmap using a hash function and linear probing. */
	public void put(char key, int value) {
		int hash = hash(key);
		int count = 0;
		while (keys[hash] != key && keys[hash] != 0) {
			hash = (hash + 1) & ~(1 << number_bits);
			if (++count == 1 << number_bits) {
				Logger.add(Logger.UTIL, "CharHashMap full.");
				return;
			}
		}
		keys[hash] = key;
		values[hash] = value;
	}

	/** Returns the value associated with this key or 0 if it does not exist. */
	public int get(char key) {
		int hash = hash(key);
		while (keys[hash] != key && keys[hash] != 0) {
			hash = (hash + 1) & ~(1 << number_bits);
		}
		return values[hash];
	}

	/** Integer hashing using Knuth's multiplicative method and the golden ratio (transformed to a signed integer). */
	private int hash(char i) {
		return (i * -1640531535) >>> (32 - number_bits);
	}
}
