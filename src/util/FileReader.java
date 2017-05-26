package util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileReader implements AutoCloseable {

	private final File f;
	private InputStream is;
	private int error = FILE_OK;
	private int read;
	private final int length;
	private static final int FILE_OK = 0;
	private static final int FILE_NOT_FOUND = 1;
	private static final int FILE_NOT_ACCESSIBLE = 2;

	public FileReader(char[] name) {
		this(new String(name));
	}

	public FileReader(String name) {
		f = new File(name);
		length = (int) f.length();
		if (!f.exists()) {
			error = FILE_NOT_FOUND;
			Logger.add(Logger.FILE, "File not found: ", f.getAbsolutePath());
			return;
		}
		if (f.length() >= Integer.MAX_VALUE) {
			error = FILE_NOT_ACCESSIBLE;
			Logger.add(Logger.FILE, "File is to large for Java to handle: ", f.getAbsolutePath());
			return;
		}
		try {
			is = new BufferedInputStream(new FileInputStream(f.getPath()));
		} catch (FileNotFoundException e) {
			error = FILE_NOT_ACCESSIBLE;
			Logger.add(Logger.FILE, "Failed to open file \"", f.getName(), "\" : ", e.getMessage());
		}
	}

	public boolean exists() {
		return error != FILE_NOT_FOUND;
	}

	public boolean canRead() {
		return error == FILE_OK;
	}

	public String readLine() {
		return readLines(1);
	}

	public char[] readCLine() {
		return readCLines(1);
	}

	public String readLines(int lineNum) {
		char[] lines = readCLines(lineNum);
		if (lines == null) {
			return null;
		}
		return new String(lines);
	}

	public char[] readCLines(int lineNum) {
		if (is != null && error == FILE_OK) {
			try {
				if (lineNum == 0) {
					return new char[0];
				}
				int b = is.read();
				if (b == -1) {
					return null;
				}
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while (b != -1) {
					if (b == '\n') {
						lineNum--;
						if (lineNum == 0) {
							break;
						}
					}
					baos.write(b);
					b = is.read();
				}
				if (b == -1 && lineNum > 1) {
					throw new IOException("Reached end of file while reading multiple lines");
				}
				byte[] array = baos.toByteArray();
				read += array.length;
				return Encoding.convertUTF16(array);
			} catch (IOException e) {
				error = FILE_NOT_ACCESSIBLE;
				Logger.add(Logger.FILE, "Failed to read file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
		return null;
	}

	public String readAll() {
		return new String(Encoding.convertUTF16(readBytes()));
	}

	public byte[] readBytes() {
		if (is != null && error == FILE_OK) {
			try {
				byte[] array = new byte[length - read];
				int r = is.read(array);
				assert (r == array.length) : "Inconsistent file size: " + f.getName();
				read += r;
				return array;
			} catch (IOException e) {
				error = FILE_NOT_ACCESSIBLE;
				Logger.add(Logger.FILE, "Failed to read file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
		return null;
	}

	public void skipLines(int lineNum) {
		if (is != null && error == FILE_OK) {
			try {
				int b = is.read();
				while (b != -1) {
					read++;
					if (b == '\n') {
						lineNum--;
						if (lineNum == 0) {
							return;
						}
					}
					b = is.read();
				}
				if (lineNum != 1) {
					throw new IOException("Reached end of file while skipping lines");
				}
			} catch (IOException e) {
				error = FILE_NOT_ACCESSIBLE;
				Logger.add(Logger.FILE, "Failed to read file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
	}

	public void skipBytes(int byteNum) {
		if (is != null && error == FILE_OK) {
			try {
				if (is.skip(byteNum) != byteNum) {
					throw new IOException("Reached end of file while skipping bytes");
				}
				read += byteNum;
			} catch (IOException e) {
				error = FILE_NOT_ACCESSIBLE;
				Logger.add(Logger.FILE, "Failed to read file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
	}

	public byte[] readByteArray(int size) {
		if (is != null && error == FILE_OK) {
			try {
				byte[] array = new byte[size];
				if (is.read(array, 0, size) != size) {
					throw new IOException("Reached end of file while reading byte array");
				}
				read += array.length;
				return array;
			} catch (IOException e) {
				error = FILE_NOT_ACCESSIBLE;
				Logger.add(Logger.FILE, "Failed to read file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
		return null;
	}

	public int readByte() {
		if (is != null && error == FILE_OK) {
			try {
				int res = is.read();
				if (res == -1) {
					throw new IOException("Reached end of file while reading single byte");
				}
				read += 1;
				return res;
			} catch (IOException e) {
				error = FILE_NOT_ACCESSIBLE;
				Logger.add(Logger.FILE, "Failed to read file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
		return -1;
	}

	@Override
	public void close() {
		if (is != null) {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				Logger.add(Logger.FILE, "Failed to close file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
	}
}
