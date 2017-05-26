package sequenze.overlay;

import sequenze.Button;
import sequenze.Game;
import sequenze.Gender;
import sequenze.GraphicHandler;
import sequenze.SystemControll;
import sequenze.Text;
import sequenze.menu.Cell;
import sequenze.menu.Icon;
import sequenze.menu.Icon.DTD;
import util.ConV;
import util.FileReader;
import util.Logger;

public class TextInput extends MenuObject {

	int position;
	int selection;
	private static final int START = -1;
	private static final int BACK = -2;
	private static final int SELECT = -3;
	int sheet;
	char[] text;
	boolean canBeEmpty;
	Gender gender;

	static char[][] chars = new char[3][];
	public static int[] tex;

	public static final byte BGR = 0;
	public static final byte SHEET1 = 1;
	public static final byte SHEET2 = 2;
	public static final byte SHEET3 = 3;
	public static final byte SHEET4 = 4;
	public static final byte COVER = 5;
	public static final byte BB = 6;
	public static final byte BBA = 7;
	public static final byte AB = 8;
	public static final byte ABA = 9;
	public static final byte SEL1 = 10;
	public static final byte SEL2 = 11;
	public static final byte SEL3 = 12;
	public static final byte SEL1A = 13;
	public static final byte SEL2A = 14;
	public static final byte SEL3A = 15;
	public static final byte TB = 16;
	public static final byte SPACE = 17;
	public static final byte SHADOW = 18;
	public static final byte CHARSELECT = 19;
	public static final byte SELS = 20;
	public static final byte BS = 21;

	private static Cell backgroundr;
	private static Cell sheets;
	private static Cell cover;
	private static Cell backbutton;
	private static Cell acceptbutton;
	private static Cell sheetselection;
	private static Cell textbox;
	private static Cell charspace;
	private static Cell shadow;
	private static Cell icon;
	private static Cell characters;
	private static Cell gendericon;

	public TextInput(int length, boolean canBeEmpty, Gender gender) {
		text = new char[length];
		this.canBeEmpty = canBeEmpty;
		this.gender = gender;
	}

	public static void init() {
		FileReader fr = new FileReader("lib/res/TextInput.txt");
		if (!fr.canRead()) {
			Logger.add(Logger.FILE, "Could not open character file: ", "lib/res/TextInput.txt");
		}
		try {
			for (int i = 0; i < 3; i++) {
				char[] s = fr.readCLine();
				assert (s != null && (i == 0 || s.length == chars[i - 1].length));
				chars[i] = s;
			}
		} finally {
			fr.close();
		}
		tex = GraphicHandler.giveTextures("TextInput");
		setupCells();
		assert (chars[0].length % characters.columns == 0);
	}

	public static void setupCells() {
		backgroundr = new Cell();
		backgroundr.x = 0;
		backgroundr.y = 0;
		backgroundr.w = 256;
		backgroundr.h = 265;
		backgroundr.icon = new Icon(tex[BGR], 0, 0);

		characters = new Cell();
		characters.x = 14;
		characters.y = 8;
		characters.w = 16;
		characters.h = 19;
		characters.arangement = Cell.GRID;
		characters.columns = 13;
		characters.x_col = 16;
		characters.y_row = 19;
		characters.addText(5, 4, 0, 0x1B, Text.TEXT_LEFT);
		Icon chari = new Icon();
		chari.sel = new DTD[]{new DTD(tex[CHARSELECT], 0, 3)};
		characters.icon = chari;

		sheets = new Cell();
		sheets.x = 13;
		sheets.y = 80;
		sheets.w = 236;
		sheets.h = 111;
		sheets.icon_type = Cell.MULTIICON;
		sheets.starts = new int[]{0};
		sheets.icons = new Icon[1][4];
		for (int i = 0; i < sheets.icons[0].length; i++) {
			sheets.icons[0][i] = new Icon(tex[SHEET1 + i], 0, 0);
		}
		sheets.setChildren(characters);

		backbutton = new Cell();
		backbutton.x = 149;
		backbutton.y = 12;
		backbutton.w = 37;
		backbutton.h = 22;
		backbutton.icon = new Icon(tex[BB], tex[BS], tex[BBA]);

		acceptbutton = new Cell();
		acceptbutton.x = 189;
		acceptbutton.y = 12;
		acceptbutton.w = 37;
		acceptbutton.h = 22;
		acceptbutton.icon = new Icon(tex[AB], tex[BS], tex[ABA]);

		sheetselection = new Cell();
		sheetselection.x = 16;
		sheetselection.y = 12;
		sheetselection.w = 30;
		sheetselection.h = 22;
		sheetselection.arangement = Cell.ROW;
		sheetselection.x_col = 32;
		sheetselection.icon_type = Cell.MULTIICON;
		sheetselection.starts = new int[]{0, 1, 2};
		sheetselection.icons = new Icon[3][2];
		for (int i = 0; i < sheetselection.icons.length; i++) {
			sheetselection.icons[i][0] = new Icon(tex[SEL1 + i], tex[SELS], 0);
			sheetselection.icons[i][1] = new Icon(tex[SEL1A + i], tex[SELS], 0);
		}

		cover = new Cell();
		cover.x = 10;
		cover.y = 48;
		cover.w = 240;
		cover.h = 104;
		cover.icon = new Icon(tex[COVER], 0, 0);
		cover.setChildren(backbutton, acceptbutton, sheetselection);

		charspace = new Cell();
		charspace.x = 72;
		charspace.y = 38;
		charspace.w = 12;
		charspace.h = 3;
		charspace.arangement = Cell.ROW;
		charspace.x_col = 12;
		charspace.addText(3, -15, 0, 0x1B, Text.TEXT_LEFT);
		Icon csi = new Icon();
		csi.normal = new DTD[]{new DTD(tex[SPACE])};
		csi.sel = new DTD[]{new DTD(tex[SPACE], 0, 1)};
		charspace.icon = csi;

		shadow = new Cell();
		shadow.x = 21;
		shadow.y = 31;
		shadow.w = 22;
		shadow.h = 7;
		shadow.icon = new Icon(tex[SHADOW], 0, 0);

		textbox = new Cell();
		textbox.x = 8;
		textbox.y = 1;
		textbox.w = 240;
		textbox.h = 46;
		textbox.icon = new Icon(tex[TB], 0, 0);
		textbox.setChildren(charspace, shadow, icon, gendericon);
		// TODO add others
		/*
		 shadow
		 icon
		 gendericon
		 */
	}

	@Override
	void drawL() {
		// TODO Auto-generated method stub

	}

	@Override
	void drawR() {
		drawBackground();
		drawText(text, position);
		drawIcon();
		drawGender(gender);
		drawSheet(chars[sheet]);
		drawCover(sheet, selection);
	}

	private void drawGender(Gender gender) {
		if (gender == Gender.IRRELEVANT) {
			return;
		}
		// TODO Auto-generated method stub

	}

	private void drawIcon() {
		textbox.getChild(1).draw();
		// TODO Auto-generated method stub

	}

	private void drawSheet(char[] chars) {
		sheets.draw(sheet, false);
		char[] c = new char[1];
		Cell wall = sheets.getChild(0);
		for (int i = 0; i < chars.length; i++) {
			c[0] = chars[i];
			wall.drawMulti(c, i, selection == i);
		}
		cover.draw();
	}

	private void drawCover(int currentSheet, int selection) {
		for (int i = 0; i < chars.length; i++) {
			if (currentSheet == i) {
				cover.getChild(2).drawMulti(null, i, 1, SELECT == i + selection);
			} else {
				cover.getChild(2).drawMulti(null, i, 0, SELECT == i + selection);
			}
		}
		cover.getChild(0).draw(null, selection == BACK);
		cover.getChild(1).draw(null, selection == START);
	}

	private void drawText(char[] text, int position) {
		textbox.draw();
		char[] c = new char[1];
		for (int i = 0; i < text.length; i++) {
			if (text[i] != 0) {
				c[0] = text[i];
				textbox.getChild(0).drawMulti(c, i, position == i);
			} else {
				textbox.getChild(0).drawMulti(null, i, position == i);
			}
		}
	}

	private void drawBackground() {
		backgroundr.draw();
	}

	@Override
	void update(Game g, MenuObjectHandler m) {
		if (backbutton.animationRunning || acceptbutton.animationRunning) {
			if (backbutton.animationFinished()) {
				if (position > 0) {
					position--;
					text[position] = 0;
				} else {
					// MEEP
				}
			} else if (acceptbutton.animationFinished()) {
				if (position == 0 && !canBeEmpty) {
					// MEEP
					return;
				}
				finish(m);
			}
		}
		if (g.wasPressed(Button.START)) {
			g.process(Button.START);
			selection = START;
		} else if (g.wasPressed(Button.SELECT)) {
			g.process(Button.SELECT);
			sheet = (sheet + 1) % chars.length;
		} else if (g.wasPressed(Button.B)) {
			g.process(Button.B);
			backbutton.animate(selection == BACK);
		} else if (g.wasPressed(Button.A)) {
			g.process(Button.A);
			if (selection == START) {
				acceptbutton.animate(true);
			} else if (selection <= SELECT && selection > SELECT - chars.length) {
				sheet = -selection + SELECT;
			} else if (selection == BACK) {
				backbutton.animate(true);
			} else if (selection >= 0) {
				if (isDakuten(chars[sheet][selection])) {
					if (position > 0) {
						text[position - 1] = toDakuten(text[position - 1], chars[sheet][selection]);
					} else {
						// MEEP
					}
				} else if (position < text.length) {
					text[position] = chars[sheet][selection];
					position++;
					if (position == text.length) {
						selection = START;
					}
				}
			}
		} else if (g.wasPressed(Button.UP)) {
			g.process(Button.UP);
			if (selection >= 0) {
				// TODO don't hardcode values
				if (selection >= characters.columns) {
					selection -= characters.columns;
				} else if (selection < 6) {
					selection = SELECT - selection * chars.length / 6;
				} else if (selection > 10) {
					selection = START;
				} else if (selection > 7) {
					selection = BACK;
				} else {
					selection += chars[0].length - characters.columns;
				}
			} else {
				if (selection == START) {
					selection = chars[0].length - 2;
				} else if (selection == BACK) {
					selection = chars[0].length - 4;
				} else {
					selection = chars[0].length - characters.columns + (-selection + SELECT) * 6 / chars.length;
				}
			}
		} else if (g.wasPressed(Button.DOWN)) {
			g.process(Button.DOWN);
			if (selection >= 0) {
				// TODO don't hardcode values
				if (selection < chars[0].length - characters.columns) {
					selection += characters.columns;
				} else if (selection % characters.columns < 6) {
					selection = SELECT - (selection % characters.columns) * chars.length / 6;
				} else if (selection % characters.columns > 10) {
					selection = START;
				} else if (selection % characters.columns > 7) {
					selection = BACK;
				} else {
					selection = selection % characters.columns;
				}
			} else {
				if (selection == START) {
					selection = characters.columns - 2;
				} else if (selection == BACK) {
					selection = characters.columns - 4;
				} else {
					selection = (-selection + SELECT) * 6 / chars.length;
				}
			}
		} else if (g.wasPressed(Button.LEFT)) {
			g.process(Button.LEFT);
			if (selection >= 0) {
				selection--;
				if ((selection + characters.columns) % characters.columns == characters.columns - 1) {
					selection += characters.columns;
				}
			} else {
				if (selection == START) {
					selection = BACK;
				} else if (selection == BACK) {
					selection = SELECT - chars.length + 1;
				} else if (selection == SELECT) {
					selection = START;
				} else {
					selection++;
				}
			}
		} else if (g.wasPressed(Button.RIGHT)) {
			g.process(Button.RIGHT);
			if (selection >= 0) {
				selection++;
				if (selection % characters.columns == 0) {
					selection -= characters.columns;
				}
			} else {
				if (selection == BACK) {
					selection = START;
				} else if (selection == SELECT - chars.length + 1) {
					selection = BACK;
				} else if (selection == START) {
					selection = SELECT;
				} else {
					selection--;
				}
			}
		} else {
			click(g);
		}
	}

	private void click(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		if (cover.getChild(1).click(g.getMouseX(), g.getMouseY())) {
			g.processMouse();
			selection = START;
			acceptbutton.animate(true);
			return;
		}
		int sel = cover.getChild(2).clickMulti(g.getMouseX(), g.getMouseY(), chars.length);
		if (sel != -1) {
			g.processMouse();
			sheet = sel;
			selection = SELECT - sheet;
			return;
		}
		if (cover.getChild(0).click(g.getMouseX(), g.getMouseY())) {
			g.processMouse();
			selection = BACK;
			backbutton.animate(true);
			return;
		}
		sel = sheets.getChild(0).clickMulti(g.getMouseX(), g.getMouseY(), chars[0].length);
		if (sel != -1) {
			selection = sel;
			if (isDakuten(chars[sheet][selection])) {
				if (position > 0) {
					text[position - 1] = toDakuten(text[position - 1], chars[sheet][selection]);
				} else {
					// MEEP
				}
			} else if (position < text.length) {
				text[position] = chars[sheet][selection];
				position++;
				if (position == text.length) {
					selection = START;
				}
			}
			return;
		}
	}

	void finish(MenuObjectHandler m) {
		char[] result = ConV.subCstring(text, 0, position);
		SystemControll.setString(SystemControll.InputText, result);
		m.deleteObject();
	}

	@Override
	boolean getsInput() {
		return true;
	}

	private boolean isDakuten(char c) {
		return c == '゙' || c == '゚';
	}

	private char toDakuten(char c, char dakuten) {
		if ((c & 0xff00) != 0x3000) {
			return c;
		}
		if (dakuten == '゚') {
			if (c >= 'は' && c <= 'ぽ' || c >= 'ハ' && c <= 'ポ') {
				int mod = c % 3;
				c += -mod + (~mod & 0x2);
			}
		} else if (dakuten == '゙') {
			if (c >= 'か' && c <= 'ぢ' || c >= 'か' && c <= 'ヂ') {
				c += ((c & 0x1) * 2) - 1;
			} else if (c >= 'つ' && c <= 'ど' || c >= 'ツ' && c <= 'ド') {
				c ^= 0x1;
			} else if (c >= 'は' && c <= 'ぽ' || c >= 'ハ' && c <= 'ポ') {
				int mod = c % 3;
				c += -mod + (~mod & 0x1);
			} else if (c == 'う' || c == 'ウ') {
				c += 'ゔ' - 'う';
			} else if (c == 'ゔ' || c == 'ヴ') {
				c -= 'ゔ' - 'う';
			}
		}
		return c;
	}

	@Override
	void close(MenuObjectHandler m) {
		// TODO auto generated method body
		m.deleteObject();
		return;
	}
}
