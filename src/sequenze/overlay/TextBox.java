package sequenze.overlay;

import sequenze.*;
import util.ConV;
import util.Logger;

public class TextBox {

	public static final int MAX_TEXT_SIZE = 256;

	int x = 0;
	int y = 208;
	int width = 27;
	int height = 1;
	int charStyle = 1;
	int palette = 1;
	int boxStyle = -1;
	final char[][] chars;
	int[] lengths;
	int sum_length;
	char[] text;
	int text_index;
	int index = 0;
	int counter = 0;
	int textspeed;
	boolean end;
	boolean left = true;
	boolean active = true;
	char[][] paras;
	int lineheight = 16;

	// escape characters
	public static final char ESCAPE_CHAR = '\\';
	public static final char NEW_LINE = 'n';
	public static final char STOP = 'f';
	public static final char NEW_BOX = 'r';
	public static final char STRING = 's';
	public static final char COLOR = 'c';
	public static final char SYMBOL_START = '[';
	public static final char SYMBOL_END = ']';

	// TODO add missing arrow to indicate that the textbox is waiting for input
	public TextBox(char[] text, boolean left) {
		this(text, left, 27, 1);
	}

	public TextBox(char[] text, boolean left, int width, int height) {
		this(text, left, width, height, null);
	}

	public TextBox(char[] text, boolean left, int width, int height, char[][] paras) {
		setParameters(paras);
		setTextSpeed(SystemControll.getVariable(SystemControll.DefaultTextSpeed));
		this.width = width;
		this.height = height;
		this.left = left;
		this.chars = new char[height + 1][MAX_TEXT_SIZE];
		this.lengths = new int[height + 1];
		setText(text);
	}

	public void intoBackground() {
		active = false;
	}

	public void setText(char[] text) {
		text_index = 0;
		this.text = text;
		assert (chars.length > 0);
		lengths[0] = processText(chars[0], text);
		sum_length = lengths[0];
		int i = 0;
		while (i < height && text_index <= text.length - 1 && text[text_index] == NEW_LINE) {
			text_index++;
			i++;
			lengths[i] = processText(chars[i], text);
			sum_length += lengths[i];
		}
		index = 0;
		end = false;
	}

	private int processText(char[] goal, char[] text) {
		int goal_index = 0;
		L:
		while (text_index < text.length) {
			char c = text[text_index++];
			if (c == ESCAPE_CHAR) {
				if (text_index == text.length) {
					Logger.add(Logger.TEXT, "Single escape character at end of text");
					text_index = text.length;
					break;
				}
				c = text[text_index];
				if (c == NEW_LINE || c == STOP || c == NEW_BOX) {
					break;
				} else if (c == COLOR) {
					if (goal_index + 3 >= goal.length) {
						Logger.add(Logger.TEXT, "Text legth exceeds buffer size");
						text_index = text.length;
						break;
					}

					int code = (ConV.fromHex(text[text_index + 1]) << 4) | ConV.fromHex(text[text_index + 2]);
					if (code == -1 || !Font.paletteExists(charStyle, code)) {
						Logger.add(Logger.TEXT, "Non hexadezimal or non existing color index");
						text_index = text.length;
						break;
					}
					goal[goal_index++] = '\\';
					goal[goal_index++] = text[++text_index];
					goal[goal_index++] = text[++text_index];
					text_index++;
				} else if (c == STRING) {
					if (text_index + 4 >= text.length) {
						Logger.add(Logger.TEXT, "Less than four digits for string index in text");
						text_index = text.length;
						break;
					}
					int hex_index = 0;
					for (int i = 0; i < 4; i++) {
						hex_index <<= 4;
						char s = text[text_index + 1 + i];
						if (s >= '0' && s <= '9') {
							hex_index += s - '0';
						} else if (s >= 'A' && s <= 'F') {
							hex_index += s - 'A' + 0xA;
						} else if (s >= 'a' && s <= 'f') {
							hex_index += s - 'a' + 0xa;
						} else {
							Logger.add(Logger.TEXT, "Non hexadezimal digit in string index in text");
							text_index = text.length;
							break L;
						}
					}
					char[] string = getString(hex_index, paras);
					if (string == null) {
						Logger.add(Logger.TEXT, "Could not find string reference");
						text_index = text.length;
						break;
					} else if (goal_index + string.length >= goal.length) {
						Logger.add(Logger.TEXT, "Inserted text legth exceeds buffer size");
						text_index = text.length;
						break;
					}
					ConV.arrayCopy(string, 0, goal, goal_index, string.length);
					goal_index += string.length;
					text_index += 5;
				}
			} else if (goal_index >= goal.length) {
				Logger.add(Logger.TEXT, "Text legth exceeds buffer size");
				text_index = text.length;
				break;
			} else if (c == SYMBOL_START) {
				int stop = ConV.indexOf(text, text_index + 1, text.length, SYMBOL_END);
				if (stop == -1) {
					Logger.add(Logger.TEXT, "Found unclosed symbol reference in text");
					text_index = text.length;
					break;
				}
				char symbol = Text.getSymbol(text, text_index, stop);
				goal[goal_index++] = symbol;
			} else {
				goal[goal_index++] = c;
			}
		}
		return goal_index;
	}

	public static char[] getString(int index, char[][] paras) {
		int group = index >>> 12;
		index &= 0x0FFF;
		// TODO add more groups (pokemon names, item names, move names, location names ...)
		switch (group) {
			case 0:
				if (index > paras.length) {
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
			case 3: {
				// TODO bounds check
				return InstancePackage.team.team[index].getName();
			}
			default:
				Logger.add(Logger.TEXT, "Illegal string group: ", group);
				return null;
		}
	}

	public void drawL() {
		if (left) {
			drawFrame();
			GraphicHandler.translate(x + 16, y + 8);
			drawLines();
			GraphicHandler.translate(-x - 16, -y - 8);
		}
	}

	public void drawLines() {
		int ind = this.index;
		for (int i = 0; i <= height; i++) {
			Font.drawCharArray(chars[i], 0, ConV.min(lengths[i], ind), 0, i * lineheight, charStyle, palette);
			ind -= lengths[i];
		}
	}

	public void drawFrame() {
		if (boxStyle == -1) {
			// TODO is this needed or should all menus update themselves
			drawFrame(x, y, width, height);
		} else {
			drawFrame(x, y, width, height, boxStyle);
		}
	}

	public static void drawFrame(int x, int y, int w, int h) {
		drawFrame(x, y, w, h, SystemControll.getVariable(SystemControll.DefaultTextboxStyle));
	}

	public static void drawFrame(int x, int y, int w, int h, int boxStyle) {
		GraphicHandler.translate(x, y);
		int[] tex = MenuObjectHandler.current.tex;
		GraphicHandler.drawBox(0, 0, tex[boxStyle * 9]);
		for (int i = 0; i < w; i++) {
			GraphicHandler.drawBox(16 + i * 8, 0, tex[boxStyle * 9 + 1]);
		}
		GraphicHandler.drawBox(16 + w * 8, 0, tex[boxStyle * 9 + 2]);
		for (int i = 0; i < h; i++) {
			GraphicHandler.drawBox(0, 16 + i * 16, tex[boxStyle * 9 + 3]);
			for (int j = 0; j < w; j++) {
				GraphicHandler.drawBox(16 + j * 8, 16 + i * 16, tex[boxStyle * 9 + 4]);
			}
			GraphicHandler.drawBox(16 + w * 8, 16 + i * 16, tex[boxStyle * 9 + 5]);
		}
		GraphicHandler.drawBox(0, 16 + h * 16, tex[boxStyle * 9 + 6]);
		for (int i = 0; i < w; i++) {
			GraphicHandler.drawBox(16 + i * 8, 16 + h * 16, tex[boxStyle * 9 + 7]);
		}
		GraphicHandler.drawBox(16 + w * 8, 16 + h * 16, tex[boxStyle * 9 + 8]);
		GraphicHandler.translate(-x, -y);
	}

	public void update(Game g, MenuObjectHandler m) {
		if (end) {
			if (m != null) {
				m.deleteText();
			}
			return;
		}
		counter++;
		if (index < sum_length) {
			if (counter > textspeed || (counter >= 0 && active && (g.keyDown(Button.B) || g.keyDown(Button.A) || g.mouseDown()))) {
				index++;
				counter = 0;
				if (index < sum_length) {
					int ind = index;
					int i = 0;
					while (ind >= lengths[i]) {
						ind -= lengths[i];
						i++;
					}
					if (chars[i][ind] == '\\') {
						index += 3;
					}
				}
			}
		} else if (text_index < text.length) {
			if (text[text_index] == STOP) {
				if (active && (g.wasPressed(Button.A) || g.wasPressed(Button.B) || g.mouseClicked())) {
					g.process(Button.A);
					g.process(Button.B);
					g.processMouse();
					text_index++;
					index -= lengths[0];
					for (int i = 0; i < height; i++) {
						ConV.arrayCopy(chars[i + 1], 0, chars[i], 0, lengths[i + 1]);
						lengths[i] = lengths[i + 1];
					}
					lengths[height] = processText(chars[height], text);
					sum_length = index + lengths[height];
				}
			} else if (text[text_index] == NEW_BOX) {
				if (active && (g.wasPressed(Button.A) || g.wasPressed(Button.B) || g.mouseClicked())) {
					g.process(Button.A);
					g.process(Button.B);
					g.processMouse();
					text_index++;
					lengths[0] = processText(chars[0], text);
					sum_length = lengths[0];
					int i = 0;
					while (i < height && text_index <= text.length - 1 && text[text_index] == NEW_LINE) {
						text_index++;
						i++;
						lengths[i] = processText(chars[i], text);
						sum_length += lengths[i];
					}
					index = 0;
				}
			} else {
				text_index++;
				index -= lengths[0];
				for (int i = 0; i < height; i++) {
					ConV.arrayCopy(chars[i + 1], 0, chars[i], 0, lengths[i + 1]);
					lengths[i] = lengths[i + 1];
				}
				lengths[height] = processText(chars[height], text);
				sum_length = index + lengths[height];
			}
		} else {
			if (active && (g.wasPressed(Button.A) || g.wasPressed(Button.B) || g.mouseClicked())) {
				g.process(Button.A);
				g.process(Button.B);
				g.processMouse();
				end = true;
			}
		}
	}

	public void fastForward() {
		index = sum_length;
	}

	public boolean getsInput() {
		return active;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setTextStyle(int style, int palette) {
		this.charStyle = style;
		this.palette = palette;
	}

	public void setTextSpeed(int ts) {
		textspeed = ts;
	}

	public void setBoxStyle(int style) {
		this.boxStyle = style;
	}

	public void drawR() {
		if (!left) {
			drawFrame();
			GraphicHandler.translate(x + 16, y + 8);
			drawLines();
			GraphicHandler.translate(-x - 16, -y - 8);
		}
	}

	public void setParameters(char[][] paras) {
		this.paras = paras;
	}

	public boolean isFinished() {
		return end;
	}

	void close(MenuObjectHandler m) {
		m.deleteText();
	}

}
