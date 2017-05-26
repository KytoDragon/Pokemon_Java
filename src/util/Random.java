package util;

import java.util.concurrent.ThreadLocalRandom;

public class Random {

	public static int nextInt(int bound) {
		return ThreadLocalRandom.current().nextInt(bound);
	}

	public static int nextInt() {
		return ThreadLocalRandom.current().nextInt();
	}

	public static long nextLong(long bound) {
		return ThreadLocalRandom.current().nextLong(bound);
	}

	public static long nextLong() {
		return ThreadLocalRandom.current().nextLong();
	}

	public static <T extends java.lang.Enum<T>> T getEnum(Class<T> clazz) {
		T[] values = clazz.getEnumConstants();
		return values[nextInt(values.length)];
	}
}
