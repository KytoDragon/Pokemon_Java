package sequenze;

import util.ConV;
import util.Logger;

public class Text implements Cloneable {

	public static final int MAX_TEXT_SIZE = 256;

	int x;
	int y;
	int line_off;
	int style;
	int palette;
	int lines;
	int type = TEXT_LEFT;
	public static final int TEXT_LEFT = 0;
	public static final int TEXT_MIDDLE = 1;
	public static final int TEXT_RIGHT = 2;
	public static final int TEXT_SLASH = 3;
	final char[][] chars;
	int[] lengths;
	private static char[][] paras;

	// escape characters
	public static final char ESCAPE_CHAR = '\\';
	public static final char NEW_LINE = 'n';
	public static final char STOP = 'f';
	public static final char NEW_BOX = 'r';
	public static final char STRING = 's';
	public static final char COLOR = 'c';
	public static final char SYMBOL_START = '[';
	public static final char SYMBOL_END = ']';

	public Text(int x, int y, int style, int palette, int type) {
		this(x, y, style, palette, type, 1, 0);
	}

	public Text(int x, int y, int style, int palette, int type, int lines, int line_off) {
		this.x = x;
		this.y = y;
		this.style = style;
		this.palette = palette;
		this.type = type;
		this.lines = lines;
		this.line_off = line_off;
		this.chars = new char[lines][MAX_TEXT_SIZE];
		this.lengths = new int[lines];
	}

	public void setText(char[] text) {
		int text_index = 0;
		for (int i = 0; i < chars.length; i++) {
			char[] goal = chars[i];
			int goal_index = 0;
			L:
			while (text_index < text.length) {
				char c = text[text_index++];
				if (c == ESCAPE_CHAR) {
					if (text_index == text.length) {
						Logger.add(Logger.TEXT, "Single escape character at end of text: ", text);
						text_index = text.length;
						break;
					}
					c = text[text_index];
					if (c == NEW_LINE || c == STOP || c == NEW_BOX) {
						text_index++;
						break;
					} else if (c == COLOR) {
						if (goal_index + 3 >= goal.length) {
							Logger.add(Logger.TEXT, "Text legth exceeds buffer size: ", text);
							text_index = text.length;
							break;
						}

						int code = ConV.getHex(text, text_index + 1, 2);
						if (code == -1 || !Font.paletteExists(style, code)) {
							Logger.add(Logger.TEXT, "Non hexadezimal or non existing color index: ", text);
							text_index = text.length;
							break;
						}
						goal[goal_index++] = '\\';
						goal[goal_index++] = text[++text_index];
						goal[goal_index++] = text[++text_index];
						text_index++;
					} else if (c == STRING) {
						if (text_index + 4 >= text.length) {
							Logger.add(Logger.TEXT, "Less than four digits for string index in text:", text);
							text_index = text.length;
							break;
						}
						int hex_index = 0;
						for (int j = 0; j < 4; j++) {
							hex_index <<= 4;
							char s = text[text_index + 1 + j];
							if (s >= '0' && s <= '9') {
								hex_index += s - '0';
							} else if (s >= 'A' && s <= 'F') {
								hex_index += s - 'A' + 0xA;
							} else if (s >= 'a' && s <= 'f') {
								hex_index += s - 'a' + 0xa;
							} else {
								Logger.add(Logger.TEXT, "Non hexadezimal digit in string index in text: ", text);
								text_index = text.length;
								break L;
							}
						}
						char[] string = getString(hex_index, paras);
						if (string == null) {
							Logger.add(Logger.TEXT, "Could not find string reference: ", text);
							text_index = text.length;
							break;
						} else if (goal_index + string.length >= goal.length) {
							Logger.add(Logger.TEXT, "Inserted text legth exceeds buffer size: ", text);
							text_index = text.length;
							break;
						}
						ConV.arrayCopy(string, 0, goal, goal_index, string.length);
						goal_index += string.length;
						text_index += 5;
					}
				} else if (goal_index >= goal.length) {
					Logger.add(Logger.TEXT, "Text legth exceeds buffer size: ", text);
					text_index = text.length;
					break;
				} else if (c == SYMBOL_START) {
					int stop = ConV.indexOf(text, text_index + 1, text.length, SYMBOL_END);
					if (stop == -1) {
						Logger.add(Logger.TEXT, "Found unclosed symbol reference in text: ", text);
						text_index = text.length;
						break;
					}
					char symbol = getSymbol(text, text_index, stop);
					goal[goal_index++] = symbol;
					text_index = stop + 1;
				} else {
					goal[goal_index++] = c;
				}
			}
			lengths[i] = goal_index;
		}
		if (text_index != text.length) {
			Logger.add(Logger.TEXT, "Text contained more lines than the text field holds: ", text);
		}
	}

	public static char getSymbol(char[] s, int start, int end) {
		if (ConV.equals(s, start, end, "Lv")) {
			return 'ᴸ';
		}
		// TODO get symbol described by s[start, end)
		return 0;
	}

	private static char[] getString(int index, char[][] paras) {
		int group = index >>> 12;
		index &= 0x0FFF;
		// TODO add more groups (pokemon names, item names, move names, location names ...)
		switch (group) {
			case 0:
				if (index > paras.length) {
					Logger.add(Logger.TEXT, "Missing text parameter");
					return null;
				}
				return paras[index];
			case 1: {
				// TODO bounds check
				return SystemControll.getString(index);
			}
			case 2: {
				// TODO bounds check
				return EventControll.getString(index);
			}
			default:
				Logger.add(Logger.TEXT, "Illegal string group: ", group);
				return null;
		}
	}

	public void draw(char[] text) {
		setText(text);
		GraphicHandler.translate(x, y);
		if (type == TEXT_LEFT) {
			drawLinesLeft(line_off, style, palette);
		} else if (type == TEXT_MIDDLE) {
			drawLinesMiddle(line_off, style, palette);
		} else if (type == TEXT_RIGHT) {
			drawLinesRight(line_off, style, palette);
		} else if (type == TEXT_SLASH) {
			drawLinesSlash(line_off, style, palette);
		} else {
			Logger.add(Logger.TEXT, "unknown text display type");
		}
		GraphicHandler.translate(-x, -y);
	}

	private void drawLinesLeft(int lineheight, int charStyle, int palette) {
		for (int i = 0; i < lines; i++) {
			Font.drawCharArray(chars[i], 0, lengths[i], 0, i * lineheight, charStyle, palette);
		}
	}

	private void drawLinesMiddle(int lineheight, int charStyle, int palette) {
		for (int i = 0; i < lines; i++) {
			Font.drawCharArrayMiddle(chars[i], 0, lengths[i], 0, i * lineheight, charStyle, palette);
		}
	}

	private void drawLinesRight(int lineheight, int charStyle, int palette) {
		for (int i = 0; i < lines; i++) {
			Font.drawCharArrayRight(chars[i], 0, lengths[i], 0, i * lineheight, charStyle, palette);
		}
	}

	private void drawLinesSlash(int lineheight, int charStyle, int palette) {
		for (int i = 0; i < lines; i++) {
			int len = lengths[i];
			int pos = ConV.max(ConV.indexOf(chars[i], 0, len, '/'), ConV.indexOf(chars[i], 0, len, '／'));
			if (pos == -1) {
				Font.drawCharArrayMiddle(chars[i], 0, len, Font.getCharWidth('/', charStyle) / 2, i * lineheight, charStyle, palette);
			} else {
				Font.drawCharArrayRight(chars[i], 0, pos, 0, i * lineheight, charStyle, palette);
				Font.drawCharArray(chars[i], pos, len, 0, i * lineheight, charStyle, palette);
			}
		}
	}

	@Override
	public Text clone() {
		return new Text(x, y, style, palette, type, lines, line_off);
	}

}
