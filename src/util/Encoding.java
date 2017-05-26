package util;

/* 
 From http://llvm.org/svn/llvm-project/llvm/trunk/include/llvm/Support/ConvertUTF.h
 and	http://llvm.org/svn/llvm-project/llvm/trunk/include/llvm/Support/ConvertUTF.c
 */
public class Encoding {

	private static final int halfShift = 10; /* used for shifting by 10 bits */

	private static final int halfBase = 0x0010000;
	private static final int halfMask = 0x3FF;

	private static final int UNI_SUR_HIGH_START = 0xD800;
	private static final int UNI_SUR_HIGH_END = 0xDBFF;
	private static final int UNI_SUR_LOW_START = 0xDC00;
	private static final int UNI_SUR_LOW_END = 0xDFFF;
	private static final int UNI_REPLACEMENT_CHAR = 0x0000FFFD;
	private static final int UNI_MAX_BMP = 0x0000FFFF;
	private static final int UNI_MAX_UTF16 = 0x0010FFFF;
	private static final int UNI_MAX_UTF32 = 0x7FFFFFFF;
	private static final int UNI_MAX_LEGAL_UTF32 = 0x0010FFFF;

	private enum ConversionResult {

		conversionOK,
		sourceExhausted,
		targetExhausted,
		sourceIllegal
	}

	private static final int[/*7*/] firstByteMark = {0x00, 0x00, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC};

	private static final byte[/*256*/] trailingBytesForUTF8 = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5
	};

	private static final long[/*6*/] offsetsFromUTF8 = {0x00000000L, 0x00003080L, 0x000E2080L,
			0x03C82080L, 0xFA082080L, 0x82082080L};

	private static final int byteMask = 0xBF;
	private static final int byteMark = 0x80;

	public static byte[] convertUTF8(char[] source) {
		return convertUTF8(source, 0, source.length);
	}

	public static byte[] convertUTF8(char[] source, int start, int end) {
		int length = ConvertUTF16toUTF8Length(source, start, end);
		if (length == -1) {
			Logger.add(Logger.TEXT, "Could not convert UTF16 to UTF8: malformed UTF16 character sequence");
			return null;
		}
		byte[] result = new byte[length];
		ConvertUTF16toUTF8Ready(source, start, end, result);
		return result;
	}

	public static byte[] convertUTF8(int[] source) {
		return convertUTF8(source, 0, source.length);
	}

	public static byte[] convertUTF8(int[] source, int start, int end) {
		int length = ConvertUTF32toUTF8Length(source, start, end);
		if (length == -1) {
			Logger.add(Logger.TEXT, "Could not convert UTF32 to UTF8: malformed UTF32 character sequence");
			return null;
		}
		byte[] result = new byte[length];
		ConvertUTF32toUTF8Ready(source, start, end, result);
		return result;
	}

	public static char[] convertUTF16(byte[] source) {
		return convertUTF16(source, 0, source.length);
	}

	public static char[] convertUTF16(byte[] source, int start, int end) {
		int length = ConvertUTF8toUTF16Length(source, start, end);
		if (length == -1) {
			Logger.add(Logger.TEXT, "Could not convert UTF8 to UTF16: malformed UTF8 character sequence");
			return null;
		}
		char[] result = new char[length];
		ConvertUTF8toUTF16Ready(source, start, end, result);
		return result;
	}

	public static int[] convertUTF32(byte[] source) {
		return convertUTF32(source, 0, source.length);
	}

	public static int[] convertUTF32(byte[] source, int start, int end) {
		int length = ConvertUTF8toUTF32Length(source, start, end);
		if (length == -1) {
			Logger.add(Logger.TEXT, "Could not convert UTF8 to UTF32: malformed UTF8 character sequence");
			return null;
		}
		int[] result = new int[length];
		ConvertUTF8toUTF32Ready(source, start, end, result);
		return result;
	}

	private static ConversionResult ConvertUTF16toUTF8Complete(char[] source, byte[] target, boolean strictConversion) {
		ConversionResult result = ConversionResult.conversionOK;
		int target_index = 0;
		int source_index = 0;
		while (source_index < source.length) {
			int ch;
			int bytesToWrite;
			//int oldSource = source_index; /* In case we have to back up because of target overflow. */
			ch = source[source_index++];
			/* If we have a surrogate pair, convert to UTF32 first. */
			if (ch >= UNI_SUR_HIGH_START && ch <= UNI_SUR_HIGH_END) {
				/* If the 16 bits following the high surrogate are in the source buffer... */
				if (source_index < source.length) {
					int ch2 = source[source_index];
					/* If it's a low surrogate, convert to UTF32. */
					if (ch2 >= UNI_SUR_LOW_START && ch2 <= UNI_SUR_LOW_END) {
						ch = ((ch - UNI_SUR_HIGH_START) << halfShift)
								+ (ch2 - UNI_SUR_LOW_START) + halfBase;
						++source_index;
					} else if (strictConversion) { /* it's an unpaired high surrogate */

						// --source_index; /* return to the illegal value itself */

						result = ConversionResult.sourceIllegal;
						break;
					}
				} else { /* We don't have the 16 bits following the high surrogate. */

					// --source_index; /* return to the high surrogate */

					result = ConversionResult.sourceExhausted;
					break;
				}
			} else if (strictConversion) {
				/* UTF-16 surrogate values are illegal in UTF-32 */
				if (ch >= UNI_SUR_LOW_START && ch <= UNI_SUR_LOW_END) {
					// --source_index; /* return to the illegal value itself */

					result = ConversionResult.sourceIllegal;
					break;
				}
			}
			/* Figure out how many bytes the result will require */
			if (ch < 0x80) {
				bytesToWrite = 1;
			} else if (ch < 0x800) {
				bytesToWrite = 2;
			} else if (ch < 0x10000) {
				bytesToWrite = 3;
			} else if (ch < 0x110000) {
				bytesToWrite = 4;
			} else {
				bytesToWrite = 3; // Should not be reachable
				ch = UNI_REPLACEMENT_CHAR;
			}

			target_index += bytesToWrite;
			if (target_index > target.length) {
				//source_index = oldSource; /* Back up source pointer! */
				// target_index -= bytesToWrite;
				result = ConversionResult.targetExhausted;
				break;
			}
			switch (bytesToWrite) { /* note: everything falls through. */

				case 4:
					target[--target_index] = (byte) ((ch | byteMark) & byteMask);
					ch >>>= 6;
				case 3:
					target[--target_index] = (byte) ((ch | byteMark) & byteMask);
					ch >>>= 6;
				case 2:
					target[--target_index] = (byte) ((ch | byteMark) & byteMask);
					ch >>>= 6;
				case 1:
					target[--target_index] = (byte) (ch | firstByteMark[bytesToWrite]);
			}
			target_index += bytesToWrite;
		}
		return result;
	}

	private static int ConvertUTF16toUTF8Length(char[] source, int start, int end) {
		int target_length = 0;
		int source_index = start;
		while (source_index < end) {
			int ch;
			int bytesToWrite;
			ch = source[source_index++];
			if (ch >= UNI_SUR_LOW_START && ch <= UNI_SUR_LOW_END) {
				target_length = -1;
				break;
			}
			if (ch >= UNI_SUR_HIGH_START && ch <= UNI_SUR_HIGH_END) {
				if (source_index >= end) {
					target_length = -1;
					break;
				}
				int ch2 = source[source_index++];
				if (ch2 < UNI_SUR_LOW_START || ch2 > UNI_SUR_LOW_END) {
					target_length = -1;
					break;
				}
				ch = ((ch - UNI_SUR_HIGH_START) << halfShift)
						+ (ch2 - UNI_SUR_LOW_START) + halfBase;
			}
			if (ch < 0x80) {
				bytesToWrite = 1;
			} else if (ch < 0x800) {
				bytesToWrite = 2;
			} else if (ch < 0x10000) {
				bytesToWrite = 3;
			} else {
				assert (ch <= UNI_MAX_UTF16);
				bytesToWrite = 4;
			}
			target_length += bytesToWrite;
		}
		return target_length;
	}

	private static void ConvertUTF16toUTF8Ready(char[] source, int start, int end, byte[] target) {
		int target_index = 0;
		int source_index = start;
		while (source_index < end) {
			int ch;
			int bytesToWrite;
			ch = source[source_index++];
			if (ch >= UNI_SUR_HIGH_START && ch <= UNI_SUR_HIGH_END) {
				int ch2 = source[source_index++];
				ch = ((ch - UNI_SUR_HIGH_START) << halfShift)
						+ (ch2 - UNI_SUR_LOW_START) + halfBase;
			}
			if (ch < 0x80) {
				bytesToWrite = 1;
			} else if (ch < 0x800) {
				bytesToWrite = 2;
			} else if (ch < 0x10000) {
				bytesToWrite = 3;
			} else {
				bytesToWrite = 4;
			}
			target_index += bytesToWrite;
			for (int i = 0; i < bytesToWrite - 1; i++) {
				target[--target_index] = (byte) ((ch | byteMark) & byteMask);
				ch >>>= 6;
			}
			target[--target_index] = (byte) (ch | firstByteMark[bytesToWrite]);
			target_index += bytesToWrite;
		}
	}

	private static ConversionResult ConvertUTF8toUTF16Complete(byte[] source, char[] target, boolean strictConversion) {
		ConversionResult result = ConversionResult.conversionOK;
		int target_index = 0;
		int source_index = 0;
		while (source_index < source.length) {
			int ch = 0;
			int extraBytesToRead = trailingBytesForUTF8[source[source_index] & 0xFF];
			if (extraBytesToRead >= source.length - source_index) {
				result = ConversionResult.sourceExhausted;
				break;
			}
			/* Do this check whether lenient or strict */
			if (!isLegalUTF8(source, source_index, extraBytesToRead + 1)) {
				result = ConversionResult.sourceIllegal;
				break;
			}
			/*
			 * The cases all fall through. See "Note A" below.
			 */
			switch (extraBytesToRead) {
				case 5:
					ch += source[source_index++];
					ch <<= 6; /* remember, illegal UTF-8 */

				case 4:
					ch += source[source_index++];
					ch <<= 6; /* remember, illegal UTF-8 */

				case 3:
					ch += source[source_index++];
					ch <<= 6;
				case 2:
					ch += source[source_index++];
					ch <<= 6;
				case 1:
					ch += source[source_index++];
					ch <<= 6;
				case 0:
					ch += source[source_index++];
			}
			ch -= offsetsFromUTF8[extraBytesToRead];

			if (target_index >= target.length) {
				// source_index -= (extraBytesToRead + 1); /* Back up source pointer! */

				result = ConversionResult.targetExhausted;
				break;
			}
			if (ch <= UNI_MAX_BMP) { /* Target is a character <= 0xFFFF */
				/* UTF-16 surrogate values are illegal in UTF-32 */

				if (ch >= UNI_SUR_HIGH_START && ch <= UNI_SUR_LOW_END) {
					if (strictConversion) {
						// source_index -= (extraBytesToRead + 1); /* return to the illegal value itself */

						result = ConversionResult.sourceIllegal;
						break;
					} else {
						target[target_index++] = UNI_REPLACEMENT_CHAR;
					}
				} else {
					target[target_index++] = (char) ch; /* normal case */

				}
			} else if (ch > UNI_MAX_UTF16) {
				if (strictConversion) {
					result = ConversionResult.sourceIllegal;
					// source_index -= (extraBytesToRead + 1); /* return to the start */

					break; /* Bail out; shouldn't continue */

				} else {
					target[target_index++] = UNI_REPLACEMENT_CHAR;
				}
			} else {
				/* target is a character in range 0xFFFF - 0x10FFFF. */
				if (target_index + 1 >= target.length) {
					// source_index -= (extraBytesToRead + 1); /* Back up source pointer! */

					result = ConversionResult.targetExhausted;
					break;
				}
				ch -= halfBase;
				target[target_index++] = (char) ((ch >>> halfShift) + UNI_SUR_HIGH_START);
				target[target_index++] = (char) ((ch & halfMask) + UNI_SUR_LOW_START);
			}
		}
		return result;
	}

	private static int ConvertUTF8toUTF16Length(byte[] source, int start, int end) {
		int target_length = 0;
		int source_index = start;
		while (source_index < end) {
			int ch = 0;
			int extraBytesToRead = trailingBytesForUTF8[source[source_index] & 0xFF];
			if (extraBytesToRead >= end - source_index) {
				target_length = -1;
				break;
			}
			if (!isLegalUTF8(source, source_index, extraBytesToRead + 1)) {
				target_length = -1;
				break;
			}
			for (int i = 0; i < extraBytesToRead; i++) {
				ch += source[source_index++] & 0xFF;
				ch <<= 6;
			}
			ch += source[source_index++] & 0xFF;
			ch -= offsetsFromUTF8[extraBytesToRead];
			if (ch <= UNI_MAX_BMP) {
				if (ch >= UNI_SUR_HIGH_START && ch <= UNI_SUR_LOW_END) {
					target_length = -1;
					break;
				}
				target_length++;
			} else if (ch > UNI_MAX_UTF16) {
				target_length = -1;
				break;
			} else {
				target_length += 2;
			}
		}
		return target_length;
	}

	private static void ConvertUTF8toUTF16Ready(byte[] source, int start, int end, char[] target) {
		int target_index = 0;
		int source_index = start;
		while (source_index < end) {
			int ch = 0;
			int extraBytesToRead = trailingBytesForUTF8[source[source_index] & 0xFF];
			for (int i = 0; i < extraBytesToRead; i++) {
				ch += source[source_index++] & 0xFF;
				ch <<= 6;
			}
			ch += source[source_index++] & 0xFF;
			ch -= offsetsFromUTF8[extraBytesToRead];
			if (ch <= UNI_MAX_BMP) {
				target[target_index++] = (char) ch;
			} else {
				ch -= halfBase;
				target[target_index++] = (char) ((ch >>> halfShift) + UNI_SUR_HIGH_START);
				target[target_index++] = (char) ((ch & halfMask) + UNI_SUR_LOW_START);
			}
		}
	}

	private static boolean isLegalUTF8(byte[] source, int source_index, int length) {
		if (length < 1 || length > 4) {
			return false;
		}
		int srcptr = source_index + length;
		for (int i = 0; i < length - 1; i++) {
			int a = source[--srcptr] & 0xFF;
			if (a < 0x80 || a > 0xBF) {
				return false;
			}
			if (length == 2) {
				switch ((int) source[source_index] & 0xFF) {
					case 0xE0:
						if (a < 0xA0) {
							return false;
						}
						break;
					case 0xED:
						if (a > 0x9F) {
							return false;
						}
						break;
					case 0xF0:
						if (a < 0x90) {
							return false;
						}
						break;
					case 0xF4:
						if (a > 0x8F) {
							return false;
						}
						break;
				}
			}
		}
		int a = source[--srcptr] & 0xFF;
		if (a >= 0x80 && a < 0xC2) {
			return false;
		} else if (a > 0xF4) {
			return false;
		}
		return true;
	}

	private static ConversionResult ConvertUTF32toUTF8Complete(int[] source, byte[] target, boolean strictConversion) {
		ConversionResult result = ConversionResult.conversionOK;
		int target_index = 0;
		int source_index = 0;
		while (source_index < source.length) {
			int ch;
			int bytesToWrite;
			ch = source[source_index++];
			if (strictConversion) {
				/* UTF-16 surrogate values are illegal in UTF-32 */
				if (ch >= UNI_SUR_HIGH_START && ch <= UNI_SUR_LOW_END) {
					// --source_index; /* return to the illegal value itself */

					result = ConversionResult.sourceIllegal;
					break;
				}
			}
			/*
			 * Figure out how many bytes the result will require. Turn any
			 * illegally large UTF32 things (> Plane 17) into replacement chars.
			 */
			if (ch < 0x80) {
				bytesToWrite = 1;
			} else if (ch < 0x800) {
				bytesToWrite = 2;
			} else if (ch < 0x10000) {
				bytesToWrite = 3;
			} else if (ch <= UNI_MAX_LEGAL_UTF32) {
				bytesToWrite = 4;
			} else {
				bytesToWrite = 3;
				ch = UNI_REPLACEMENT_CHAR;
				result = ConversionResult.sourceIllegal;
			}

			target_index += bytesToWrite;
			if (target_index > target.length) {
				// --source_index; /* Back up source pointer! */

				// target_index -= bytesToWrite;
				result = ConversionResult.targetExhausted;
				break;
			}
			switch (bytesToWrite) { /* note: everything falls through. */

				case 4:
					target[--target_index] = (byte) ((ch | byteMark) & byteMask);
					ch >>>= 6;
				case 3:
					target[--target_index] = (byte) ((ch | byteMark) & byteMask);
					ch >>>= 6;
				case 2:
					target[--target_index] = (byte) ((ch | byteMark) & byteMask);
					ch >>>= 6;
				case 1:
					target[--target_index] = (byte) (ch | firstByteMark[bytesToWrite]);
			}
			target_index += bytesToWrite;
		}
		return result;
	}

	private static int ConvertUTF32toUTF8Length(int[] source, int start, int end) {
		int target_length = 0;
		int source_index = start;
		while (source_index < end) {
			int ch;
			int bytesToWrite;
			ch = source[source_index++];
			if (ch >= UNI_SUR_HIGH_START && ch <= UNI_SUR_LOW_END) {
				target_length = -1;
				break;
			}
			if (ch < 0x80) {
				bytesToWrite = 1;
			} else if (ch < 0x800) {
				bytesToWrite = 2;
			} else if (ch < 0x10000) {
				bytesToWrite = 3;
			} else if (ch <= UNI_MAX_LEGAL_UTF32) {
				bytesToWrite = 4;
			} else {
				target_length = -1;
				break;
			}
			target_length += bytesToWrite;
		}
		return target_length;
	}

	private static ConversionResult ConvertUTF32toUTF8Ready(int[] source, int start, int end, byte[] target) {
		ConversionResult result = ConversionResult.conversionOK;
		int target_index = 0;
		int source_index = start;
		while (source_index < end) {
			int ch;
			int bytesToWrite;
			ch = source[source_index++];
			if (ch < 0x80) {
				bytesToWrite = 1;
			} else if (ch < 0x800) {
				bytesToWrite = 2;
			} else if (ch < 0x10000) {
				bytesToWrite = 3;
			} else {
				bytesToWrite = 4;
			}
			target_index += bytesToWrite;
			for (int i = 0; i < bytesToWrite - 1; i++) {
				target[--target_index] = (byte) ((ch | byteMark) & byteMask);
				ch >>>= 6;
			}
			target[--target_index] = (byte) (ch | firstByteMark[bytesToWrite]);
			target_index += bytesToWrite;
		}
		return result;
	}

	private static ConversionResult ConvertUTF8toUTF32Complete(byte[] source, int[] target, boolean strictConversion) {
		ConversionResult result = ConversionResult.conversionOK;
		int target_index = 0;
		int source_index = 0;
		while (source_index < source.length) {
			int ch = 0;
			int extraBytesToRead = trailingBytesForUTF8[source[source_index] & 0xFF];
			if (extraBytesToRead >= source.length - source_index) {
				result = ConversionResult.sourceExhausted;
				break;
			}
			/* Do this check whether lenient or strict */
			if (!isLegalUTF8(source, source_index, extraBytesToRead + 1)) {
				result = ConversionResult.sourceIllegal;
				break;
			}
			/*
			 * The cases all fall through. See "Note A" below.
			 */
			switch (extraBytesToRead) {
				case 5:
					ch += source[source_index++];
					ch <<= 6; /* remember, illegal UTF-8 */

				case 4:
					ch += source[source_index++];
					ch <<= 6; /* remember, illegal UTF-8 */

				case 3:
					ch += source[source_index++];
					ch <<= 6;
				case 2:
					ch += source[source_index++];
					ch <<= 6;
				case 1:
					ch += source[source_index++];
					ch <<= 6;
				case 0:
					ch += source[source_index++];
			}
			ch -= offsetsFromUTF8[extraBytesToRead];

			if (target_index >= target.length) {
				// source_index -= (extraBytesToRead + 1); /* Back up source pointer! */

				result = ConversionResult.targetExhausted;
				break;
			}

			if (ch <= UNI_MAX_LEGAL_UTF32) {
				/*
				 * UTF-16 surrogate values are illegal in UTF-32, and anything
				 * over Plane 17 (> 0x10FFFF) is illegal.
				 */
				if (ch >= UNI_SUR_HIGH_START && ch <= UNI_SUR_LOW_END) {
					if (strictConversion) {
						// source_index -= (extraBytesToRead + 1); /* return to the illegal value itself */

						result = ConversionResult.sourceIllegal;
						break;
					} else {
						target[target_index++] = UNI_REPLACEMENT_CHAR;
					}
				} else {
					target[target_index++] = ch;
				}
			} else { /* i.e., ch > UNI_MAX_LEGAL_UTF32 */

				result = ConversionResult.sourceIllegal;
				target[target_index++] = UNI_REPLACEMENT_CHAR;
			}
		}
		return result;
	}

	private static int ConvertUTF8toUTF32Length(byte[] source, int start, int end) {
		int target_length = 0;
		int source_index = start;
		while (source_index < end) {
			int ch = 0;
			int extraBytesToRead = trailingBytesForUTF8[source[source_index] & 0xFF];
			if (extraBytesToRead >= source.length - source_index) {
				target_length = -1;
				break;
			}
			if (!isLegalUTF8(source, source_index, extraBytesToRead + 1)) {
				target_length = -1;
				break;
			}
			for (int i = 0; i < extraBytesToRead; i++) {
				ch += source[source_index++];
				ch <<= 6;
			}
			ch += source[source_index++];
			ch -= offsetsFromUTF8[extraBytesToRead];

			if (ch <= UNI_MAX_LEGAL_UTF32) {
				if (ch >= UNI_SUR_HIGH_START && ch <= UNI_SUR_LOW_END) {
					target_length = -1;
					break;
				} else {
					target_length++;
				}
			} else {
				target_length = -1;
				break;
			}
		}
		return target_length;
	}

	private static void ConvertUTF8toUTF32Ready(byte[] source, int start, int end, int[] target) {
		int target_index = 0;
		int source_index = start;
		while (source_index < end) {
			int ch = 0;
			int extraBytesToRead = trailingBytesForUTF8[source[source_index] & 0xFF];
			for (int i = 0; i < extraBytesToRead; i++) {
				ch += source[source_index++];
				ch <<= 6;
			}
			ch += source[source_index++];
			ch -= offsetsFromUTF8[extraBytesToRead];

			target[target_index++] = ch;
		}
	}
}
