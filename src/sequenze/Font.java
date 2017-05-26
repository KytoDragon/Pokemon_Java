package sequenze;

import util.CharHashMap;
import util.ConV;
import util.FileReader;
import util.Logger;

public class Font {

	/** The number of palettes per text style */
	public static final int PALETTENUM = 32;

	private static CharHashMap cmap;

	static int[/* style */][/* char */] textindtex;

	static int[/* style */][/* id */] palettes;

	public static void initText() {
		int[] charStyleDims = getCharStyleDims();
		int rows = charStyleDims[1];
		int columns = charStyleDims[2];

		int[][][] tmp = GraphicHandler.getTextTextures();
		textindtex = tmp[0];
		palettes = tmp[1];

		cmap = new CharHashMap(rows * columns);

		// TODO hardcoded path
		FileReader fr = new FileReader("lib/res/Text.txt");
		if (!fr.canRead()) {
			Logger.add(Logger.FILE, "Could not open character file: ", "lib/res/Text.txt");
		}
		try {
			for (int i = 0; i < rows; i++) {
				char[] s = fr.readCLine();
				assert (s != null && s.length == columns);
				for (int j = 0; j < columns; j++) {
					cmap.put(s[j], i * columns + j + 1);
				}
			}
		} finally {
			fr.close();
		}
	}

	/** Draws a single character to the buffer. */
	public static void drawChar(char c, int x, int y, int style, int palette) {
		if (c == ' ') {
			return;
		}
		int index = cmap.get(c);
		if (index == 0) {
			GraphicHandler.drawRectangle(x, y, getCharHeight(style), getCharWidth('\0', style), 255, 0, 255);
			return;
		}
		GraphicHandler.drawIndexedBox(x, y, textindtex[style][index - 1], palettes[style][palette]);
	}

	/** Draws an array of characters to the buffer. */
	public static void drawCharArray(char[] s, int start, int end, int x, int y, int style, int def_palette) {
		int palette = def_palette;
		for (int i = start; i < end; i++) {
			if (s[i] != ' ' && s[i] != '\\') {
				drawChar(s[i], x, y, style, palette);
			} else if (s[i] == '\\') {
				if (i + 2 >= end) {
					return;
				}
				palette = ConV.getHex(s, i + 1, 2);
				i += 2;
				if (palette == -1) {
					return;
				} else if (palette == 0) {
					palette = def_palette;
				}
				continue;
			}
			x += getCharWidth(s[i], style);
		}
	}

	public static int stringWidth(char[] s, int style) {
		int result = 0;
		for (int i = 0; i < s.length; i++) {
			if (s[i] == '\\') {
				i++;
				continue;
			}
			result += getCharWidth(s[i], style);
		}
		return result;
	}

	public static int stringWidth(char[] s, int start, int end, int style) {
		int result = 0;
		for (int i = start; i < end; i++) {
			if (s[i] == '\\') {
				i++;
				continue;
			}
			result += getCharWidth(s[i], style);
		}
		return result;
	}

	/** Draws a string to the buffer. */
	public static void drawString(char[] s, int x, int y, int style, int palette) {
		if (s == null) {
			return;
		}
		drawCharArray(s, 0, s.length, x, y, style, palette);
	}

	/** Draws a string centered in the given rectangle to the buffer. */
	public static void drawStringMiddle(char[] s, int x, int y, int w, int h, int style, int palette) {
		if (s == null) {
			return;
		}
		int s_width = stringWidth(s, style);
		int c_height = getCharHeight(style);
		drawCharArray(s, 0, s.length, x + (w - s_width) / 2, y + (h - c_height) / 2, style, palette);
	}

	/** Draws a string centered in the given rectangle to the buffer. */
	public static void drawCharArrayMiddle(char[] s, int start, int end, int x, int y, int style, int palette) {
		if (s == null) {
			return;
		}
		int s_width = stringWidth(s, start, end, style);
		drawCharArray(s, start, end, x - s_width / 2, y, style, palette);
	}

	/** Draws a string moved to the right side. */
	public static void drawStringRight(char[] s, int x, int y, int w, int style, int palette) {
		if (s == null) {
			return;
		}
		int s_width = stringWidth(s, style);
		drawCharArray(s, 0, s.length, x + w - s_width, y, style, palette);
	}

	/** Draws a string moved to the right side. */
	public static void drawCharArrayRight(char[] s, int start, int end, int x, int y,int style, int palette) {
		if (s == null) {
			return;
		}
		int s_width = stringWidth(s, start, end, style);
		drawCharArray(s, start, end, x - s_width, y, style, palette);
	}

	public static boolean paletteExists(int style, int id) {
		return id >= 0 && id < palettes[style].length;
	}

	public static int getCharWidth(char c, int style) {
		// TODO Auto-generated method stub
		if (style > 4) {
			return 42;
		}
		if (c == '\0') {
			return 16;
		}

		if (style == 4) {
			// TODO missing fullwidth counterparts
			switch (c) {
				case 'ᴸ':
					return 9;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '/':
					return 8;
				case '／':
					return 7;
				case ' ':
					return 5;
				case '․':
					return 2;
			}
			return 42;
		}
		if (style == 3) {
			// TODO missing fullwidth counterparts
			// TODO missing lower case counterparts
			switch (c) {
				case 'C':
				case 'F':
				case 'H':
				case 'K':
				case 'M':
				case 'N':
				case 'O':
				case 'S':
				case 'U':
				case 'X':
				case 'Z':
					return 12;
				case 'B':
				case 'D':
				case 'E':
				case 'G':
				case 'L':
				case 'Q':
				case 'V':
				case 'W':
				case 'Y':
					return 11;
				case '!':
				case '?':
				case 'I':
				case 'J':
				case 'T':
					return 10;
				case 'P':
				case 'R':
					return 9;
				case 'A':
					return 8;
				case ' ':
					return 6;
			}
			return 42;
		}
		if (style == 2) {
			switch (c) {
				case 'i':
					return 4;
				case 'l':
				case ' ':
					return 5;
				case 'f':
				case 'j':
				case '.':
				case ',':
					return 6;
				case '$':
					return 8;
				default:
					return 7;
			}
		}
		switch (c) {
			case 'ᴸ':
				return 11;
			case '․':
				return 2;
			case 'i':
				return 3;
			case 'l':
			case ' ':
				return 4;
			case 'f':
			case 'j':
			case '.':
			case ',':
			case '\'':
				return 5;
			case '$':
			case '０':
			case '２':
			case '３':
			case '４':
			case '５':
			case '６':
			case '７':
			case '８':
			case '９':
				return 7;
			case ' ':
			case '／':
				return 8;
			default:
				return 6;
		}
	}

	public static int getCharHeight(int style) {
		// TODO Auto-generated method stub
		int tmp = 16; // height of the given character style
		return tmp;
	}

	public static int[] getCharStyleDims() {
		// TODO Auto-generated method stub
		int[] tmp = {5, 16, 32}; // (styles, rows, columns)
		return tmp;
	}
}
