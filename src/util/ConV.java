package util;

import java.text.ParseException;
import java.util.Arrays;

public final class ConV {

	private ConV() {
	}

	public static int modulo(int numerator, int denominator) {
		int result = numerator % denominator;
		if (result < 0) {
			result += denominator;
		}
		return result;
	}

	/**
	 * Returns the integer parsed from s starting at start.
	 */
	public static int getInt(String s, int start) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		boolean negativ = false;
		if (s.charAt(start) == '-') {
			start++;
			negativ = true;
		} else if (s.charAt(start) == '+') {
			start++;
		}
		int result = 0;
		char c = s.charAt(start);
		while (c >= '0' && c <= '9') {
			result = result * 10;
			result = result + (c - '0');
			start++;
			if (start >= s.length()) {
				break;
			}
			c = s.charAt(start);
		}
		if (negativ) {
			result = -result;
		}
		return result;
	}

	@Deprecated
	public static boolean isInteger(String s) {
		if (s == null || s.length() == 0) {
			return false;
		}
		return isInteger(s.toCharArray());
	}

	public static boolean isInteger(char[] s) {
		if (s == null || s.length == 0) {
			return false;
		}
		int start = 0;
		if (s[0] == '-' || s[0] == '+') {
			start++;
		}
		if (start == s.length) {
			return false;
		}
		while (start < s.length) {
			char c = s[start];
			if (c < '0' || c > '9') {
				return false;
			}
			start++;
		}
		return true;
	}

	@Deprecated
	public static int parseInteger(String s) {
		return (int) parseIntegerL(s);
	}

	@Deprecated
	public static long parseIntegerL(String s) {
		return parseIntegerL(s.toCharArray());
	}

	public static int parseInteger(char[] s) {
		return (int) parseIntegerL(s);
	}

	public static long parseIntegerL(char[] s) {
		boolean negativ = false;
		int start = 0;
		if (s[0] == '-') {
			start++;
			negativ = true;
		} else if (s[0] == '+') {
			start++;
		}
		long result = 0;
		while (start < s.length) {
			char c = s[start];
			result = result * 10;
			result = result + (c - '0');
			start++;
		}
		if (negativ) {
			result = -result;
		}
		return result;
	}

	@Deprecated
	public static int parseInt(String s) throws ParseException {
		return (int) parseLong(s);
	}

	@Deprecated
	public static long parseLong(String s) throws ParseException {
		if (s == null || s.length() == 0) {
			throw new ParseException("String is empty or null", 0);
		}
		return parseLong(s.toCharArray());
	}

	@Deprecated
	public static boolean parseBoolean(char[] s) throws ParseException {
		if (equals(s, "true")) {
			return true;
		} else if (equals(s, "false")) {
			return false;
		} else {
			throw new ParseException("String is not a boolean", 0);
		}
	}

	@Deprecated
	public static byte parseByte(char[] s) throws ParseException {
		return (byte) parseLong(s);
	}

	@Deprecated
	public static int parseInt(char[] s) throws ParseException {
		return (int) parseLong(s);
	}

	@Deprecated
	public static long parseLong(char[] s) throws ParseException {
		if (s == null || s.length == 0) {
			throw new ParseException("String is empty or null", 0);
		}
		boolean negativ = false;
		int start = 0;
		if (s[0] == '-') {
			start++;
			negativ = true;
		} else if (s[0] == '+') {
			start++;
		}
		if (start == s.length) {
			throw new ParseException("String only contains a sign", 1);
		}
		long result = 0;
		while (start < s.length) {
			char c = s[start];
			if (c < '0' || c > '9') {
				throw new ParseException("Found non numeric symbol: " + c, start);
			}
			result = result * 10;
			result = result + (c - '0');
			start++;
		}
		if (negativ) {
			result = -result;
		}
		return result;
	}

	public static int fromHex(char code) {
		if (code >= '0' && code <= '9') {
			return code - '0';
		} else if (code >= 'A' && code <= 'F') {
			return code - 'A' + 0xA;
		} else if (code >= 'a' && code <= 'f') {
			return code - 'a' + 0xa;
		} else {
			return -1;
		}
	}

	/**
	 * Returns the integer parsed from s starting at start.
	 */
	public static int getInt(char[] s, int start) {
		if (s == null || s.length == 0) {
			return 0;
		}
		boolean negativ = false;
		if (s[start] == '-') {
			start++;
			negativ = true;
		} else if (s[start] == '+') {
			start++;
		}
		if (start == s.length) {
			return 0;
		}
		int result = 0;
		char c = s[start];
		while (c >= '0' && c <= '9') {
			result = result * 10;
			result = result + (c - '0');
			start++;
			if (start >= s.length) {
				break;
			}
			c = s[start];
		}
		if (negativ) {
			result = -result;
		}
		return result;
	}

	/**
	 * Returns base to the power of exp.
	 */
	public static int pow(int base, int exp) {
		int erg = 1;
		for (int i = 0; i < exp; i++) {
			erg = erg * base;
		}
		return erg;
	}

	/**
	 * Returns the absolute value of the given value.
	 */
	public static int abs(int x) {
		return (x >= 0 ? x : -x);
	}

	/**
	 * Returns whether the point (xm,ym) is in the rectangle starting at (x,y)
	 * with width w and height h.
	 */
	public static boolean isIn(int xm, int ym, int x, int y, int w, int h) {
		return (xm >= x && xm < x + w && ym >= y && ym < y + h);
	}

	/**
	 * Returns whether the point (xm,ym) is in the rectangle starting at (x,y)
	 * with width w and height h.
	 */
	public static boolean isIn(int x, int y, int[] coords) {
		return isIn(x, y, coords[0], coords[1], coords[2], coords[3]);
	}

	public static char[] concat(char[]... s) {
		int length = 0;
		for (char[] cs : s) {
			length += cs.length;
		}
		char[] res = new char[length];
		for (char[] cs : s) {
			arrayCopy(cs, 0, res, res.length - length, cs.length);
			length -= cs.length;
		}
		return res;
	}

	public static boolean equals(char[] s, String s2) {
		return equals(s, 0, s.length, s2);
	}

	public static boolean equals(char[] s, int start, int end, String s2) {
		if (end - start != s2.length()) {
			return false;
		}
		for (int i = 0; i < s2.length(); i++) {
			if (s[start + i] != s2.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	public static boolean equals(char[] s, char[] s2) {
		if (s == null) {
			return s2 == null;
		}
		return equals(s, 0, s.length, s2);
	}

	public static boolean equals(char[] s, int start, int end, char[] s2) {
		if (s2 == null) {
			return false;
		}
		if (end - start != s2.length) {
			return false;
		}
		for (int i = 0; i < s2.length; i++) {
			if (s[start + i] != s2[i]) {
				return false;
			}
		}
		return true;
	}

	public static boolean startsWith(char[] s, char[] s2) {
		return startsWith(s, 0, s.length, s2);
	}

	public static boolean startsWith(char[] s, int start, int end, char[] s2) {
		int i;
		for (i = 0; start < end; start++, i++) {
			if (i >= s2.length) {
				return true;
			}
			if (s[start] != s2[i]) {
				return false;
			}
		}
		return i == s2.length;
	}

	public static boolean endsWith(char[] s, char[] s2) {
		return endsWith(s, 0, s.length, s2);
	}

	public static boolean endsWith(char[] s, int start, int end, char[] s2) {
		if (start > end - s2.length) {
			return false;
		}
		start = end - s2.length;
		for (int i = 0; i < s2.length; i++) {
			if (s[start + i] != s2[i]) {
				return false;
			}
		}
		return true;
	}

	public static boolean contains(char[] s, char[] s2) {
		return indexOf(s, s2) != -1;
	}

	public static boolean contains(char[] s, int start, int end, char[] s2) {
		return indexOf(s, start, end, s2) != -1;
	}

	public static boolean contains(char[] s, char c) {
		return indexOf(s, c) != -1;
	}

	public static boolean contains(char[] s, int start, int end, char c) {
		return indexOf(s, start, end, c) != -1;
	}

	public static int indexOf(char[] s, char c) {
		return indexOf(s, 0, s.length, c);
	}

	public static int indexOf(char[] s, int start, int end, String s2) {
		L:
		for (int i = start; i < end; i++) {
			if (i + s2.length() > end) {
				break;
			}
			for (int j = 0; j < s2.length(); j++) {
				if (s[i + j] != s2.charAt(j)) {
					continue L;
				}
			}
			return i;
		}
		return -1;
	}

	public static int indexOf(char[] s, char[] s2) {
		return indexOf(s, 0, s.length, s2);
	}

	public static int indexOf(char[] s, int start, int end, char[] s2) {
		L:
		for (int i = start; i < end; i++) {
			if (i + s2.length > end) {
				break;
			}
			for (int j = 0; j < s2.length; j++) {
				if (s[i + j] != s2[j]) {
					continue L;
				}
			}
			return i;
		}
		return -1;
	}

	public static int indexOf(char[] s, int start, int end, char c) {
		for (int i = start; i < end; i++) {
			if (s[i] == c) {
				return i;
			}
		}
		return -1;
	}

	public static char[] trimToCString(char[] s, int start, int end) {
		while (start < end && (s[start] == ' ' || s[start] == '\t')) {
			start++;
		}
		while (end > start && (s[end - 1] == ' ' || s[end - 1] == '\t')) {
			end--;
		}
		if (start >= end) {
			return new char[0];
		}
		char[] result = new char[end - start];
		arrayCopy(s, start, result, 0, end - start);
		return result;
	}

	public static String substring(char[] s, int start, int end) {
		return new String(subCstring(s, start, end));
	}

	public static char[] subCstring(char[] s, int start, int end) {
		char[] result = new char[end - start];
		arrayCopy(s, start, result, 0, end - start);
		return result;
	}

	public static int max(int a, int b) {
		return (a > b ? a : b);
	}

	public static int min(int a, int b) {
		return (a < b ? a : b);
	}

	public static int log10(int x) {
		int res = 0;
		if (x >= 100000000) {
			res += 8;
			x /= 100000000;
		}
		if (x >= 10000) {
			res += 4;
			x /= 10000;
		}
		if (x >= 100) {
			res += 2;
			x /= 100;
		}
		if (x >= 10) {
			res += 1;
		}
		return res;
	}

	public static int log10(long x) {
		int res = 0;
		if (x >= 10000000000000000L) {
			res += 16;
			x /= 10000000000000000L;
		}
		if (x >= 100000000) {
			res += 8;
			x /= 100000000;
		}
		if (x >= 10000) {
			res += 4;
			x /= 10000;
		}
		if (x >= 100) {
			res += 2;
			x /= 100;
		}
		if (x >= 10) {
			res += 1;
		}
		return res;
	}

	public static String toString(int i) {
		return new String(toCString(i));
	}

	public static char[] toCString(int i) {
		char[] res;
		int start = 0;
		if (i >= 0) {
			res = new char[log10(i) + 1];
		} else {
			i = -i;
			res = new char[log10(i) + 2];
			res[0] = '-';
			start++;
		}
		for (int j = res.length; j-- > 0 && j >= start; ) {
			res[j] = (char) ((i % 10) + '0');
			i /= 10;
		}
		return res;
	}

	public static char[] toCString(long i) {
		char[] res;
		int start = 0;
		if (i >= 0) {
			res = new char[log10(i) + 1];
		} else {
			i = -i;
			res = new char[log10(i) + 2];
			res[0] = '-';
			start++;
		}
		for (int j = res.length; j-- > 0 && j >= start; ) {
			res[j] = (char) ((i % 10) + '0');
			i /= 10;
		}
		return res;
	}

	public static char[] toFixedCString(int i) {
		char[] res = new char[5];
		for (int j = 5; j-- > 0; ) {
			res[j] = (char) ((i % 10) + '0');
			i /= 10;
		}
		return res;
	}

	public static int hashCode(char[] s) {
		int h = 0;
		for (char value : s) {
			h = 31 * h + value;
		}
		return h;
	}

	public static int get2Fold(int i) {
		return 1 << get2FoldBit(i);
	}

	public static byte get2FoldBit(int i) {
		byte bit = 0;
		while (i > (1 << bit)) {
			bit++;
		}
		return bit;
	}

	public static void arrayCopy(int[] src, int[] dest) {
		arrayCopy(src, 0, dest, 0, src.length);
	}

	public static void arrayCopy(boolean[] src, boolean[] dest) {
		arrayCopy(src, 0, dest, 0, src.length);
	}

	public static void arrayCopy(int[] src, int srcPos, int[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
		//for (int x = 0; x < length; x++) {
		//	dest[x + destPos] = src[x + srcPos];
		//}
	}

	public static void arrayCopy(boolean[] src, int srcPos, boolean[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
		//for (int x = 0; x < length; x++) {
		//	dest[x + destPos] = src[x + srcPos];
		//}
	}

	public static void arrayCopy(char[] src, int srcPos, char[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
		//for (int x = 0; x < length; x++) {
		//	dest[x + destPos] = src[x + srcPos];
		//}
	}

	public static void arrayCopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
		//for (int x = 0; x < length; x++) {
		//	dest[x + destPos] = src[x + srcPos];
		//}
	}

	public static <T> void arrayCopy(T[] src, T[] dest) {
		arrayCopy(src, 0, dest, 0, src.length);
	}

	public static <T> void arrayCopy(T[] src, int srcPos, T[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
		//for (int x = 0; x < length; x++) {
		//	dest[x + destPos] = src[x + srcPos];
		//}
	}

	public static int[] arrayCopy(int[] src, int newLength) {
		return Arrays.copyOf(src, newLength);
	}

	public static <T> T[] arrayCopy(T[] src, int newLength) {
		return Arrays.copyOf(src, newLength);
	}

	public static void arrayCopy2D(int[][] src, int srcPosX, int srcPosY, int[][] dest, int destPosX, int destPosY, int width, int height) {
		for (int y = 0; y < height; y++) {
			arrayCopy(src[y + srcPosY], srcPosX, dest[y + destPosY], destPosX, width);
		}
	}

	public static void arrayCopy2D(byte[][] src, int srcPosX, int srcPosY, byte[][] dest, int destPosX, int destPosY, int width, int height) {
		for (int y = 0; y < height; y++) {
			arrayCopy(src[y + srcPosY], srcPosX, dest[y + destPosY], destPosX, width);
		}
	}

	public static int max(int[] values) {
		int max = Integer.MIN_VALUE;
		for (int value : values) {
			if (max < value) {
				max = value;
			}
		}
		return max;
	}

	public static int getHex(char[] s, int pos, int length) {
		int result = 0;
		for (int i = 0; i < length; i++) {
			result <<= 4;
			char c = s[pos + i];
			int tmp = fromHex(c);
			if (tmp == -1) {
				return -1;
			}
			result += tmp;
		}
		return result;
	}

	@Deprecated
	public static int getHex(String s, int pos, int length) {
		int result = 0;
		for (int i = 0; i < length; i++) {
			result <<= 4;
			char c = s.charAt(pos + i);
			int tmp = fromHex(c);
			if (tmp == -1) {
				return -1;
			}
			result += tmp;
		}
		return result;
	}

	public static int textLength(Object part) {
		if (part == null) {
			return 4;
		} else if (part.getClass() == char[].class) {
			return ((char[]) part).length;
		} else if (part.getClass() == String.class) {
			return ((String) part).length();
		} else if (part.getClass() == Integer.class) {
			int number = ((Integer) part).intValue();
			if (number < 0) {
				return log10(-number) + 2;
			} else {
				return log10(number) + 1;
			}
		} else if (part.getClass() == Long.class) {
			long number = ((Long) part).longValue();
			if (number < 0) {
				return log10(-number) + 2;
			} else {
				return log10(number) + 1;
			}
		} else if (part.getClass() == Character.class) {
			return 1;
		} else {
			Logger.add(Logger.TEXT, "Unsupported text parameter: ", part.getClass().getName());
			return 0;
		}
	}

	public static int writeTo(char[] result, int index, Object part) {
		if (part == null) {
			result[index] = 'n';
			result[index + 1] = 'u';
			result[index + 2] = 'l';
			result[index + 3] = 'l';
			return 4;
		} else if (part.getClass() == char[].class) {
			char[] source = ((char[]) part);
			for (int j = 0; j < source.length; j++) {
				result[index + j] = source[j];
			}
			return source.length;
		} else if (part.getClass() == String.class) {
			String source = ((String) part);
			for (int j = 0; j < source.length(); j++) {
				result[index + j] = source.charAt(j);
			}
			return source.length();
		} else if (part.getClass() == Integer.class) {
			int number = ((Integer) part).intValue();
			return toCString(result, index, number);
		} else if (part.getClass() == Long.class) {
			long number = ((Long) part).longValue();
			return toCString(result, index, number);
		} else if (part.getClass() == Character.class) {
			result[index] = (char) part;
			return 1;
		} else {
			Logger.add(Logger.TEXT, "Unsupported text parameter: ", part.getClass().getName());
			return 0;
		}
	}

	public static char[] concat(Object... parts) {
		int length = 0;
		for (Object part : parts) {
			length += textLength(part);
		}
		char[] result = new char[length];
		int index = 0;
		for (Object part : parts) {
			index += writeTo(result, index, part);
		}
		assert (index == result.length);
		return result;
	}

	public static char[] sprintf(char[] format, Object... parts) {
		int length = format.length;
		for (int i = 0; i < format.length; i++) {
			if (i + 6 <= format.length && format[i] == '\\' && format[i + 1] == 's' && format[i + 2] == '0') {
				length -= 6;
				int id = getHex(format, i + 3, 3);
				if (id < 0) {
					Logger.add(Logger.TEXT, "Invalid hexadezimal number: ", substring(format, i, i + 3));
					return null;
				} else if (id >= parts.length) {
					Logger.add(Logger.TEXT, "Parameter index too big: ", id);
					return null;
				} else if (parts[id] == null) {
					return null;
				} else {
					length += textLength(parts[id]);
				}
				i += 5;
			}
		}
		char[] result = new char[length];
		int index = 0;
		for (int i = 0; i < format.length; i++) {
			if (format[i] == '\\' && format[i + 1] == 's' && format[i + 2] == '0') {
				int id = getHex(format, i + 3, 3);
				index += writeTo(result, index, parts[id]);
				i += 5;
			} else {
				result[index++] = format[i];
			}
		}
		assert (index == result.length);
		return result;
	}

	@Deprecated
	public static char[] sprintf(String format, Object... parts) {
		int length = format.length();
		for (int i = 0; i < format.length(); i++) {
			if (i + 6 <= format.length() && format.charAt(i) == '\\' && format.charAt(i + 1) == 's' && format.charAt(i + 2) == '0') {
				length -= 6;
				int id = (format.charAt(i + 3) - '0') * 0x100 + (format.charAt(i + 4) - '0') * 0x10 + format.charAt(i + 5) - '0';
				if (id < 0) {
					Logger.add(Logger.TEXT, "Invalid hexadezimal number: ", format.substring(i, i + 3));
					return null;
				} else if (id >= parts.length) {
					Logger.add(Logger.TEXT, "Parameter index too big: ", id);
					return null;
				} else if (parts[id] == null) {
					return null;
				} else {
					length += textLength(parts[id]);
				}
				i += 5;
			}
		}
		char[] result = new char[length];
		int index = 0;
		for (int i = 0; i < format.length(); i++) {
			if (format.charAt(i) == '\\' && format.charAt(i + 1) == 's' && format.charAt(i + 2) == '0') {
				int id = (format.charAt(i + 3) - '0') * 0x100 + (format.charAt(i + 4) - '0') * 0x10 + format.charAt(i + 5) - '0';
				index += writeTo(result, index, parts[id]);
				i += 5;
			} else {
				result[index++] = format.charAt(i);
			}
		}
		assert (index == result.length);
		return result;
	}

	public static int toCString(char[] result, int start, int i) {
		int length = log10(i) + 1;
		if (i < 0) {
			i = -i;
			result[start] = '-';
			length++;
		} else if (i == 0) {
			result[start] = '0';
			return 1;
		}
		for (int j = length - 1; i > 0; j--) {
			result[start + j] = (char) ((i % 10) + '0');
			i /= 10;
		}
		return length;
	}

	public static int toCString(char[] result, int start, long i) {
		int length = log10(i) + 1;
		if (i < 0) {
			i = -i;
			result[start] = '-';
			length++;
		} else if (i == 0) {
			result[start] = '0';
			return 1;
		}
		for (int j = length - 1; i > 0; j--) {
			result[start + j] = (char) ((i % 10) + '0');
			i /= 10;
		}
		return length;
	}

	public static char[][] split(char[] s, char c) {
		int num = 1;
		for (char c2 : s) {
			if (c2 == c) {
				num++;
			}
		}
		char[][] result = new char[num][];
		int last = 0;
		int index = 0;
		for (int i = 0; i < s.length; i++) {
			if (s[i] != c) {
				continue;
			}
			result[index++] = ConV.subCstring(s, last, i);
			last = i + 1;
		}
		result[index] = ConV.subCstring(s, last, s.length);
		return result;
	}

}
