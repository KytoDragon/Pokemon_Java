package util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileWriter implements AutoCloseable {

	private OutputStream os;
	private File f;
	private int error = FILE_OK;
	private static final int FILE_OK = 0;
	private static final int FILE_NOT_ACCESSIBLE = 1;
	private static final int FILE_WRITE_ERROR = 2;

	public FileWriter(char[] name) {
		this(new String(name));
	}

	public FileWriter(String name) {
		f = new File(name);
		File folder = f.getParentFile();
		if (!folder.exists()) {
			folder.mkdirs();
		} else if (f.exists()) {
			try {
				Files.move(f.toPath(), new File(name + "old").toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (SecurityException | IOException e) {
				error = FILE_NOT_ACCESSIBLE;
				Logger.add(Logger.FILE, "Could not rename file \"", f.getName(), "\" to \"", new File(name + "old").getName(), "\" : ", e.getMessage());
				return;
			}
		}
		try {
			f.createNewFile();
		} catch (SecurityException | IOException e) {
			error = FILE_NOT_ACCESSIBLE;
			Logger.add(Logger.FILE, "Could not create file \"", f.getName(), "\" : ", e.getMessage());
			return;
		}
		try {
			os = new BufferedOutputStream(new FileOutputStream(f.getPath()));
		} catch (SecurityException | IOException e) {
			error = FILE_NOT_ACCESSIBLE;
			Logger.add(Logger.FILE, "Could not open file \"", f.getName(), "\" : ", e.getMessage());
		}
	}

	public boolean canWrite() {
		return error == FILE_OK;
	}

	public void write(byte[] b) {
		if (os != null && error == FILE_OK) {
			try {
				os.write(b);
			} catch (IOException e) {
				error = FILE_WRITE_ERROR;
				Logger.add(Logger.FILE, "Could not write to file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
	}

	public void newLine() {
		if (os != null && error == FILE_OK) {
			try {
				os.write('\n');
			} catch (IOException e) {
				error = FILE_WRITE_ERROR;
				Logger.add(Logger.FILE, "Could not write to file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
	}

	public void write(char c) {
		assert (c < 128) : "Tried to write non ascii character as single byte";
		writeByte(c);
	}

	public void writeByte(int c) {
		if (os != null && error == FILE_OK) {
			try {
				os.write(c);
			} catch (IOException e) {
				error = FILE_WRITE_ERROR;
				Logger.add(Logger.FILE, "Could not write to file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
	}

	public void writeInteger(int i) {
		write(ConV.toCString(i));
	}

	public void writeInteger(long i) {
		write(ConV.toCString(i));
	}

	@Override
	public void close() {
		if (os != null) {
			try {
				os.close();
				os = null;
			} catch (IOException e) {
				Logger.add(Logger.FILE, "Failed to close file \"", f.getName(), "\" : ", e.getMessage());
			}
		}
	}

	public void write(String s) {
		write(s.toCharArray());
	}

	public void write(char[] s) {
		write(Encoding.convertUTF8(s));
	}
}
