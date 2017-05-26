package util;

import java.io.PrintStream;

import script.ScriptException;

public class Logger {

	public static final int GAME = 0;
	public static final int BAG = 1;
	public static final int TEAM = 2;
	public static final int GADGET = 3;
	public static final int POKEDEX = 4;
	public static final int MENU = 5;
	public static final int MAP = 6;
	public static final int FILE = 7;
	public static final int GRAPHICS = 8;
	public static final int AUDIO = 9;
	public static final int SYSTEM = 10;
	public static final int EDITOR = 11;
	public static final int SCRIPT = 12;
	public static final int UTIL = 13;
	public static final int TEXT = 14;
	public static final int CELL = 15;

	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	private static final PrintStream OUTPUT = System.out;
	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	private static final PrintStream ERROR = System.err;

	public static void flush() {
		OUTPUT.flush();
		ERROR.flush();
	}

	public Logger() {

	}

	/** Prints the given message to the console. */
	public static void add(int id, Object... parts) {
		OUTPUT.print(getModule(id));
		OUTPUT.print(": ");
		for (Object part : parts) {
			if (part == null) {
				OUTPUT.print("null");
			} else if (part.getClass() == char[].class) {
				OUTPUT.print((char[]) part);
			} else if (part.getClass() == Integer.class) {
				OUTPUT.print((int) part);
			} else if (part.getClass() == Long.class) {
				OUTPUT.print((int) part);
			} else if (part.getClass() == Character.class) {
				OUTPUT.print((char) part);
			} else {
				assert (part.getClass() == String.class);
				OUTPUT.print((String)part);
			}
		}
		OUTPUT.println();
	}

	public static void debug(String log) {
		OUTPUT.println(log);
	}

	public static void add(Throwable t) {
		OUTPUT.flush();
		t.printStackTrace(ERROR);
		ERROR.flush();
	}

	public static void add(ScriptException sc) {
		OUTPUT.flush();
		sc.printStackTrace(ERROR);
		ERROR.flush();
	}

	/** Returns the name of the object with the given id */
	private static String getModule(int id) {
		switch (id) {
			case GAME:
				return "Game";
			case BAG:
				return "Bag";
			case TEAM:
				return "Team";
			case GADGET:
				return "Gadget";
			case POKEDEX:
				return "Pokedex";
			case MENU:
				return "Menu";
			case MAP:
				return "Map";
			case FILE:
				return "File";
			case GRAPHICS:
				return "Graphics";
			case AUDIO:
				return "Audio";
			case SYSTEM:
				return "System";
			case EDITOR:
				return "Editor";
			case SCRIPT:
				return "Script";
			case UTIL:
				return "Util";
			case TEXT:
				return "Text";
			case CELL:
				return "Cell";
			default:
				return "Undefined nr." + id;
		}
	}

}