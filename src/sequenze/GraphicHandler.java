package sequenze;

import image.ImageReader;
import image.IndexedTexture;
import image.Texture;
import sequenze.battle.BattleMenu;
import sequenze.menu.*;
import sequenze.overlay.TextInput;
import util.Logger;

public class GraphicHandler {

	private final static String TEXTUREPATH = "lib/textures/";

	private final static String TEXTURETYPE = ".png";

	public static int[] itemtextures;

	public static int[] pokemon_icon_textures;

	public static int[] pokemon_front_textures;

	public static int DEFAULTTEXTUREID;

	public static int DEFAULTPALETTEID;

	/**
	 * Current instance of this class.
	 */
	private static GraphicDisplay gd;

	/**
	 * Closes the display.
	 */
	public static void close() {
		gd.close();
	}

	/**
	 * Returns whether the display is requested to close.
	 */
	public static boolean isCloseRequested() {
		return gd.isCloseRequested();
	}

	/**
	 * Empties the buffer.
	 */
	public static void clear() {
		gd.clear();
	}

	/**
	 * Draws the buffer to the screen.
	 */
	public static void drawToScreen() {
		gd.drawToScreen();
	}

	private static int h = 0;

	/**
	 * Changes to the left display.
	 */
	public static void changeToLeft() {
		gd.changeToLeft();
		h += 32;
		if (h >= 1536) {
			h -= 1536;
		}
		int r = h <= 256 || h >= 1280 ? 255 : h >= 512 && h < 1024 ? 0 : h < 768 ? 512 - h : h - 1024;
		int g = h >= 256 && h <= 768 ? 255 : h >= 1024 ? 0 : h >= 512 ? 1024 - h : h;
		int b = h >= 768 && h <= 1280 ? 255 : h < 512 ? 0 : h >= 1024 ? 1536 - h : h - 512;
		gd.drawRectangle(0, 0, 256, 256, r, g, b, 255);
	}

	/**
	 * Changes to the right display.
	 */
	public static void changeToRight() {
		gd.changeToRight();
		//gd.drawRect(0, 0, 256, 256, -r, 0, r, 255);
	}

	/**
	 * Saves the given texture and returns its index.
	 */
	public static int loadTexture(Texture image) {
		if (image == null) {
			return DEFAULTTEXTUREID;
		}
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				if (image.image[y][x] == 0xFF0080FF) {
					image.image[y][x] = 0;
				}
			}
		}
		return gd.loadTexture(image);
	}

	/**
	 * Saves the given texture and returns its index.
	 */
	public static int loadIndexedTexture(IndexedTexture image) {
		if (image == null) {
			return DEFAULTPALETTEID;
		}
		return gd.loadIndexedTexture(image);
	}

	/**
	 * Saves the given texture and returns its index.
	 */
	public static int loadPalette(int[] palette) {
		if (palette == null) {
			return 0;
		}
		return gd.loadPalette(palette);
	}

	/**
	 * Draws a single colour rectangle to the buffer.
	 */
	public static void drawRectangle(int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		gd.drawRectangle(x, y, width, height, red, green, blue, alpha);
	}

	/**
	 * Draws a single colour rectangle to the buffer.
	 */
	public static void drawRectangle(int x, int y, int width, int height, int red, int green, int blue) {
		drawRectangle(x, y, width, height, red, green, blue, 255);
	}

	/**
	 * Initialises the display.
	 */
	public static void init(GraphicDisplay g) {
		gd = g;
		Texture defaultTexture = new Texture(16, 16, Texture.NO_ALPHA);
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				defaultTexture.image[y][x] = 0xFFFF00FF;
			}
		}
		IndexedTexture defaultPalette = new IndexedTexture(16, 16);
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				defaultPalette.image[y][x] = 0x1;
			}
		}
		// load twice so that the default texture is not index 0
		loadTexture(defaultTexture);
		DEFAULTTEXTUREID = loadTexture(defaultTexture);
		DEFAULTPALETTEID = loadIndexedTexture(defaultPalette);
		Font.initText();
		itemtextures = loadBufs("Items", 32, 17, 32, 32);
		pokemon_icon_textures = loadBufs("Pokemon_icons", 16, 10, 32, 32);
		pokemon_front_textures = loadBufs("Pokemon_front", 16, 10, 80, 80);
	}

	public static int[] giveTextures(char[] texName) {
		return giveTextures(new String(texName));
	}

	/**
	 * Returns an array of texture indices for the object with the given name.
	 */
	public static int[] giveTextures(String texName) {
		// TODO
		switch (texName) {
			case "Gadget": {
				// TODO
				int[] tex = new int[5];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "Gadget" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;
				tex[Gadget.LS] = loadTexture(img.getSubTexture(0, 512, 256, 256));
				tex[Gadget.RS] = loadTexture(img.getSubTexture(0, 256, 256, 256));
				tex[Gadget.BB] = loadTexture(img.getSubTexture(0, 512 + 256, 16, 16));
				tex[Gadget.RC] = loadTexture(img.getSubTexture(0, 512 + 256 + 16, 256, 96));
				tex[Gadget.BG] = loadTexture(img.getSubTexture(0, 0, 256, 256));
				return tex;
			}
			case "Hero":
			case "0": {
				int[] tex = loadBufs("Hero", 6, 8, 32, 32);
				return tex;
			}
			case "Map": {
				int[] tex = loadBufs("Behavior", 8, 10, 16, 16);
				return tex;
			}
			case "Menu": {
				int[] tex = new int[20 * 9 + 6];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "textboxes" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;
				for (int i = 0; i < 20; i++) {
					tex[i * 9] = loadTexture(img.getSubTexture(i % 2 * 48, i / 2 * 48, 16, 16));
					tex[i * 9 + 1] = loadTexture(img.getSubTexture(i % 2 * 48 + 16, i / 2 * 48, 8, 16));
					tex[i * 9 + 2] = loadTexture(img.getSubTexture(i % 2 * 48 + 24, i / 2 * 48, 24, 16));
					tex[i * 9 + 3] = loadTexture(img.getSubTexture(i % 2 * 48, i / 2 * 48 + 16, 16, 16));
					tex[i * 9 + 4] = loadTexture(img.getSubTexture(i % 2 * 48 + 16, i / 2 * 48 + 16, 8, 16));
					tex[i * 9 + 5] = loadTexture(img.getSubTexture(i % 2 * 48 + 24, i / 2 * 48 + 16, 24, 16));
					tex[i * 9 + 6] = loadTexture(img.getSubTexture(i % 2 * 48, i / 2 * 48 + 32, 16, 16));
					tex[i * 9 + 7] = loadTexture(img.getSubTexture(i % 2 * 48 + 16, i / 2 * 48 + 32, 8, 16));
					tex[i * 9 + 8] = loadTexture(img.getSubTexture(i % 2 * 48 + 24, i / 2 * 48 + 32, 24, 16));
				}
				Texture img2 = ImageReader.loadTexture(TEXTUREPATH + "Gui" + TEXTURETYPE);
				img2.alpha = Texture.SIMPLE_ALPHA;
				tex[180] = loadTexture(img2.getSubTexture(0, 556, 8, 32));
				tex[180 + 1] = loadTexture(img2.getSubTexture(8, 556, 8, 32));
				tex[180 + 2] = loadTexture(img2.getSubTexture(8, 556, 1, 32));
				tex[180 + 3] = loadTexture(img2.getSubTexture(16, 556, 9, 34));
				tex[180 + 4] = loadTexture(img2.getSubTexture(25, 556, 9, 34));
				tex[180 + 5] = loadTexture(img2.getSubTexture(25, 556, 1, 34));
				return tex;
			}
			case "SHUD": {
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "tiles" + TEXTURETYPE);
				if (img != null) {
					int[] tex = new int[]{loadTexture(img), img.getWidth(), img.getHeight()};
					return tex;
				} else {
					return null;
				}
			}
			case "IGMenu": {
				int[] tex = new int[30];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "Menu" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;
				tex[Menu.BG] = loadTexture(img.getSubTexture(0, 0, 256, 256));
				tex[Menu.MB] = loadTexture(img.getSubTexture(256, 0, 162, 188));
				tex[Menu.ARB] = loadTexture(img.getSubTexture(0, 256, 256, 51));
				tex[Menu.ARU] = loadTexture(img.getSubTexture(296, 188, 20, 26));
				tex[Menu.ARD] = loadTexture(img.getSubTexture(316, 188, 20, 26));
				tex[Menu.SB] = loadTexture(img.getSubTexture(256, 188, 40, 26));
				tex[Menu.RUN] = loadTexture(img.getSubTexture(498, 0, 72, 42));
				tex[Menu.RUNT] = loadTexture(img.getSubTexture(498, 42, 72, 42));
				tex[Menu.RUNA] = loadTexture(img.getSubTexture(498, 84, 72, 42));
				tex[Menu.RUNAT] = loadTexture(img.getSubTexture(498, 126, 72, 42));
				tex[Menu.REG1] = loadTexture(img.getSubTexture(498, 168, 56, 32));
				tex[Menu.REG1T] = loadTexture(img.getSubTexture(498, 200, 56, 32));
				tex[Menu.REG2] = loadTexture(img.getSubTexture(554, 168, 56, 32));
				tex[Menu.REG2T] = loadTexture(img.getSubTexture(554, 200, 56, 32));
				tex[Menu.AB] = loadTexture(img.getSubTexture(256, 214, 88, 42));
				tex[Menu.ABA] = loadTexture(img.getSubTexture(344, 214, 88, 42));
				for (int i = 0; i < 7; i++) {
					tex[Menu.MI + i * 2] = loadTexture(img.getSubTexture(418, i * 26, 40, 26));
					tex[Menu.MIT + i * 2] = loadTexture(img.getSubTexture(458, i * 26, 40, 26));
				}
				return tex;
			}
			case "Bag": {
				int[] tex = new int[65];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "Bag" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;
				tex[Bag.SBG] = loadTexture(img.getSubTexture(512, 512, 256, 256));
				tex[Bag.DTF] = loadTexture(img.getSubTexture(0, 794, 256, 64));
				tex[Bag.SBF] = loadTexture(img.getSubTexture(408, 862, 26, 26));
				tex[Bag.VO] = loadTexture(img.getSubTexture(280, 768, 94, 26));
				tex[Bag.SO] = loadTexture(img.getSubTexture(374, 768, 94, 26));
				tex[Bag.AO] = loadTexture(img.getSubTexture(468, 768, 94, 26));
				tex[Bag.LA] = loadTexture(img.getSubTexture(472, 794, 24, 24));
				tex[Bag.LAA] = loadTexture(img.getSubTexture(472, 818, 24, 24));
				tex[Bag.RA] = loadTexture(img.getSubTexture(496, 794, 24, 24));
				tex[Bag.RAA] = loadTexture(img.getSubTexture(496, 818, 24, 24));
				tex[Bag.BB] = loadTexture(img.getSubTexture(0, 768, 62, 26));
				tex[Bag.BBA] = loadTexture(img.getSubTexture(62, 768, 62, 26));
				tex[Bag.BBS] = loadTexture(img.getSubTexture(124, 768, 62, 26));
				tex[Bag.IBG] = loadTexture(img.getSubTexture(562, 768, 124, 38));
				tex[Bag.IBGS] = loadTexture(img.getSubTexture(562, 806, 124, 38));
				tex[Bag.IBG2] = loadTexture(img.getSubTexture(562, 844, 124, 38));
				tex[Bag.TI] = loadTexture(img.getSubTexture(0, 858, 162, 44));
				tex[Bag.TAU] = loadTexture(img.getSubTexture(352, 842, 20, 20));
				tex[Bag.TAU2] = loadTexture(img.getSubTexture(372, 842, 20, 20));
				tex[Bag.TAD] = loadTexture(img.getSubTexture(392, 842, 20, 20));
				tex[Bag.TAD2] = loadTexture(img.getSubTexture(412, 842, 20, 20));
				tex[Bag.TA] = loadTexture(img.getSubTexture(162, 858, 76, 24));
				tex[Bag.TAA] = loadTexture(img.getSubTexture(238, 858, 76, 24));
				tex[Bag.TD] = loadTexture(img.getSubTexture(162, 882, 76, 24));
				tex[Bag.TDA] = loadTexture(img.getSubTexture(238, 882, 76, 24));
				for (int i = 0; i < 7 + 1; i++) {
					tex[Bag.BG + i] = loadTexture(img.getSubTexture(i % 3 * 256, i / 3 * 256, 256, 256));
					tex[Bag.SBI + i] = loadTexture(img.getSubTexture(256 + i * 24, 818, 24, 24));
					tex[Bag.SBIS + i] = loadTexture(img.getSubTexture(256 + i * 24, 794, 24, 24));
					tex[Bag.IIBG + i] = loadTexture(img.getSubTexture(i % 4 * 124, 906 + i / 4 * 38, 124, 38));
					tex[Bag.IO + i] = loadTexture(img.getSubTexture(496 + i % 2 * 94, 882 + i / 2 * 26, 94, 26));
				}
				return tex;
			}
			case "Team": {
				int[] tex = new int[45];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "Team" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;
				tex[Team.LBG] = loadTexture(img.getSubTexture(0, 0, 256, 256));
				tex[Team.RBG] = loadTexture(img.getSubTexture(256, 0, 256, 256));
				tex[Team.IB] = loadTexture(img.getSubTexture(0, 256, 256, 49));
				tex[Team.BB] = loadTexture(img.getSubTexture(140, 342, 56, 24));
				tex[Team.BBA] = loadTexture(img.getSubTexture(196, 342, 56, 24));
				tex[Team.MBB] = loadTexture(img.getSubTexture(140, 305, 54, 37));
				tex[Team.MBBS] = loadTexture(img.getSubTexture(140, 366, 54, 37));
				tex[Team.MO] = loadTexture(img.getSubTexture(0, 351, 126, 29));
				tex[Team.MOS] = loadTexture(img.getSubTexture(0, 380, 126, 29));
				tex[Team.PIC] = loadTexture(img.getSubTexture(194, 366, 22, 28));
				tex[Team.PICS] = loadTexture(img.getSubTexture(216, 366, 22, 28));
				for (int i = 0; i < 16; i++) {
					tex[Team.ML + i] = loadTexture(img.getSubTexture(256 + (i % 2 * 128), 256 + (i / 2 * 46), 128, 46));
				}
				tex[Team.MALE] = loadTexture(img.getSubTexture(368, 694, 6, 10));
				tex[Team.FEMALE] = loadTexture(img.getSubTexture(374, 694, 6, 10));
				tex[Team.HPGREEN] = loadTexture(img.getSubTexture(69, 1550, 1, 4));
				tex[Team.HPYELLOW] = loadTexture(img.getSubTexture(70, 1550, 1, 4));
				tex[Team.HPRED] = loadTexture(img.getSubTexture(71, 1550, 1, 4));
				tex[Team.HPBAR] = loadTexture(img.getSubTexture(0, 1550, 69, 7));
				tex[Team.HPBARKO] = loadTexture(img.getSubTexture(0, 1557, 69, 7));
				tex[Team.HPBARMOVE] = loadTexture(img.getSubTexture(0, 1564, 69, 7));
				for (int i = 0; i < 6; i++) {
					tex[Team.STATUSCD + i] = loadTexture(img.getSubTexture(144 + i / 3 * 20, 1476 + i % 3 * 8, 20, 8));
				}
				tex[Team.ITEM] = loadTexture(img.getSubTexture(442, 853, 8, 8));
				tex[Team.LETTER] = loadTexture(img.getSubTexture(434, 853, 8, 8));
				tex[Team.CAPSULE] = loadTexture(img.getSubTexture(426, 853, 8, 8));

				return tex;
			}
			case "Summary": {
				int[] tex = new int[225];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "Summary" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;

				tex[Summary.LIS1] = loadTexture(img.getSubTexture(0, 416, 256, 256));
				tex[Summary.RIS1] = loadTexture(img.getSubTexture(0, 672, 256, 256));
				tex[Summary.LIS2] = loadTexture(img.getSubTexture(0, 928, 256, 256));
				tex[Summary.RIS2] = loadTexture(img.getSubTexture(0, 1184, 256, 256));
				tex[Summary.LIS3] = loadTexture(img.getSubTexture(256, 928, 256, 256));
				tex[Summary.RIS3] = loadTexture(img.getSubTexture(256, 1184, 256, 256));
				tex[Summary.IBB] = loadTexture(img.getSubTexture(256, 624, 60, 24));
				tex[Summary.IBBA] = loadTexture(img.getSubTexture(316, 624, 60, 24));
				tex[Summary.ISB] = loadTexture(img.getSubTexture(256, 648, 40, 31));
				tex[Summary.ISBS] = loadTexture(img.getSubTexture(296, 648, 40, 31));
				tex[Summary.ISBA] = loadTexture(img.getSubTexture(382, 798, 40, 31));
				tex[Summary.SW] = loadTexture(img.getSubTexture(256, 679, 80, 19));
				tex[Summary.SWA] = loadTexture(img.getSubTexture(256, 698, 80, 19));
				tex[Summary.RIBS] = loadTexture(img.getSubTexture(336, 680, 32, 32));
				tex[Summary.SWMS] = loadTexture(img.getSubTexture(256, 717, 126, 39));
				tex[Summary.SWMA] = loadTexture(img.getSubTexture(256, 756, 126, 39));
				tex[Summary.MIB] = loadTexture(img.getSubTexture(384, 624, 128, 174));
				tex[Summary.RIBIB] = loadTexture(img.getSubTexture(256, 861, 256, 67));
				tex[Summary.RIBBB] = loadTexture(img.getSubTexture(382, 829, 44, 16));
				tex[Summary.RIBBBA] = loadTexture(img.getSubTexture(382, 845, 44, 16));
				tex[Summary.IIB] = loadTexture(img.getSubTexture(0, 1440, 100, 36));
				tex[Summary.GIB] = loadTexture(img.getSubTexture(100, 1440, 100, 34));
				for (int i = 0; i < 3; i++) {
					tex[Summary.IN1 + i * 2] = loadTexture(img.getSubTexture(256 + (i * 42), 798, 42, 30));
					tex[Summary.IN1A + i * 2] = loadTexture(img.getSubTexture(256 + (i * 42), 828, 42, 30));
				}
				tex[Summary.RIS1EGG] = loadTexture(img.getSubTexture(256, 1440, 256, 256));
				tex[Summary.XP] = loadTexture(img.getSubTexture(256, 858, 1, 3));
				tex[Summary.MALE] = loadTexture(img.getSubTexture(368, 694, 6, 10));
				tex[Summary.FEMALE] = loadTexture(img.getSubTexture(374, 694, 6, 10));
				for (int i = 0; i < 6; i++) {
					tex[Summary.MARK + i * 2] = loadTexture(img.getSubTexture(200 + (i * 8), 1440, 8, 8));
					tex[Summary.MARKA + i * 2] = loadTexture(img.getSubTexture(200 + (i * 8), 1448, 8, 8));
				}
				for (int i = 0; i < 5; i++) {
					tex[Summary.LEAF + i * 2] = loadTexture(img.getSubTexture(i * 16, 1532, 16, 18));
					tex[Summary.CROWN + i * 2] = loadTexture(img.getSubTexture(225, 1456 + i * 20, 31, 20));
				}
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 5; j++) {
						tex[Summary.TYPE + i * 5 + j] = loadTexture(img.getSubTexture(80 + i * 32, 1520 + j * 14, 32, 14));
					}
				}
				for (int i = 0; i < 4; i++) {
					tex[Summary.STAR + i] = loadTexture(img.getSubTexture(i * 16, 1516, 16, 16));
				}
				tex[Summary.HPGREEN2] = loadTexture(img.getSubTexture(257, 858, 1, 3));
				tex[Summary.HPYELLOW2] = loadTexture(img.getSubTexture(258, 858, 1, 3));
				tex[Summary.HPRED2] = loadTexture(img.getSubTexture(259, 858, 1, 3));
				tex[Summary.PHYSICAL] = loadTexture(img.getSubTexture(452, 798, 32, 14));
				tex[Summary.SPECIAL] = loadTexture(img.getSubTexture(452, 812, 32, 14));
				tex[Summary.STATUS] = loadTexture(img.getSubTexture(452, 826, 32, 14));
				tex[Summary.XPBAR] = loadTexture(img.getSubTexture(256, 617, 89, 7));
				tex[Summary.HPBAR] = loadTexture(img.getSubTexture(426, 845, 67, 7));
				for (int i = 0; i < 25; i++) {
					tex[Summary.BALL + i] = loadTexture(img.getSubTexture(256 + (i % 16) * 16, 416 + (i / 16) * 16, 16, 16));
				}
				for (int i = 0; i < 6; i++) {
					tex[Summary.STATUSCD + i] = loadTexture(img.getSubTexture(144 + i / 3 * 20, 1476 + i % 3 * 8, 20, 8));
				}
				tex[Summary.POKERUS] = loadTexture(img.getSubTexture(144, 1500, 40, 14));
				tex[Summary.POKERUSCURED] = loadTexture(img.getSubTexture(434, 853, 8, 8));
				tex[Summary.PERF] = loadTexture(img.getSubTexture(256, 448, 193, 41));
				tex[Summary.PERFB] = loadTexture(img.getSubTexture(256, 489, 193, 3));
				tex[Summary.RIBBONBG] = loadTexture(img.getSubTexture(484, 798, 28, 28));
				tex[Summary.RIBBONTEXT] = loadTexture(img.getSubTexture(362, 604, 150, 20));
				for (int i = 0; i < 80; i++) {
					tex[Summary.RIBBON + i] = loadTexture(img.getSubTexture(i % 16 * 32, i / 16 * 32, 32, 32));
				}
				for (int i = 0; i < 7; i++) {
					tex[Summary.RIBBONICON + i] = loadTexture(img.getSubTexture(240, 1556 + i * 16, 16, 16));
				}
				tex[Summary.RIBARROWUP] = loadTexture(img.getSubTexture(0, 1476, 24, 40));
				tex[Summary.RIBARROWUPA] = loadTexture(img.getSubTexture(24, 1476, 24, 40));
				tex[Summary.RIBARROWDOWN] = loadTexture(img.getSubTexture(48, 1476, 24, 40));
				tex[Summary.RIBARROWDOWNA] = loadTexture(img.getSubTexture(72, 1476, 24, 40));
				tex[Summary.ATTACK] = loadTexture(img.getSubTexture(256, 492, 129, 39));
				tex[Summary.ATTACKB] = loadTexture(img.getSubTexture(256, 531, 129, 3));
				tex[Summary.ATTACKPOS] = loadTexture(img.getSubTexture(336, 648, 30, 14));
				tex[Summary.ATTACKPOSA] = loadTexture(img.getSubTexture(366, 648, 14, 6));
				tex[Summary.ABILITY] = loadTexture(img.getSubTexture(256, 534, 149, 58));
				tex[Summary.SHINYSTAR] = loadTexture(img.getSubTexture(426, 853, 8, 8));
				return tex;
			}
			case "Options": {
				int[] tex = new int[15];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "Options" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;
				tex[Options.LBG] = loadTexture(img.getSubTexture(256, 0, 256, 256));
				tex[Options.RBG] = loadTexture(img.getSubTexture(0, 0, 256, 256));
				tex[Options.SB] = loadTexture(img.getSubTexture(0, 256, 46, 20));
				tex[Options.SBA] = loadTexture(img.getSubTexture(0, 276, 46, 20));
				tex[Options.LB] = loadTexture(img.getSubTexture(46, 256, 62, 20));
				tex[Options.LBA] = loadTexture(img.getSubTexture(46, 276, 62, 20));
				tex[Options.TF] = loadTexture(img.getSubTexture(166, 256, 70, 20));
				tex[Options.LA] = loadTexture(img.getSubTexture(108, 256, 29, 20));
				tex[Options.LAA] = loadTexture(img.getSubTexture(108, 276, 29, 20));
				tex[Options.RA] = loadTexture(img.getSubTexture(137, 256, 29, 20));
				tex[Options.RAA] = loadTexture(img.getSubTexture(137, 276, 29, 20));
				tex[Options.SS] = loadTexture(img.getSubTexture(0, 296, 256, 30));
				tex[Options.BB] = loadTexture(img.getSubTexture(236, 256, 62, 22));
				tex[Options.BBA] = loadTexture(img.getSubTexture(298, 256, 62, 22));
				tex[Options.BBS] = loadTexture(img.getSubTexture(360, 256, 64, 24));
				return tex;
			}
			case "TrainerCard": {
				int[] tex = new int[16];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "TrainerCard" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;
				tex[TrainerCard.BG] = loadTexture(img.getSubTexture(149, 990, 32, 32));
				for (int i = 0; i < 6; i++) {
					tex[TrainerCard.CARD + i] = loadTexture(img.getSubTexture(0, i * 165, 252, 165));
					tex[TrainerCard.COVER + i] = loadTexture(img.getSubTexture(0, 990 + i * 15, 149, 15));
				}
				tex[TrainerCard.STAR] = loadTexture(img.getSubTexture(181, 990, 11, 12));
				tex[TrainerCard.BB] = loadTexture(img.getSubTexture(149, 1022, 60, 24));
				tex[TrainerCard.BBA] = loadTexture(img.getSubTexture(149, 1046, 60, 24));
				return tex;
			}
			case "StartMenu": {
				int[] tex = new int[5];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "StartMenu" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;
				tex[StartMenu.BGR] = loadTexture(img.getSubTexture(0, 0, 256, 256));
				tex[StartMenu.BGL] = loadTexture(img.getSubTexture(256, 0, 256, 256));
				tex[StartMenu.MENUITEM] = loadTexture(img.getSubTexture(0, 256, 196, 44));
				tex[StartMenu.MENUITEMS] = loadTexture(img.getSubTexture(0, 300, 196, 44));
				tex[StartMenu.OVERVIEW] = loadTexture(img.getSubTexture(196, 256, 196, 92));
				return tex;
			}
			case "TextInput": {
				int[] tex = new int[22];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "TextInput" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;
				tex[TextInput.BGR] = loadTexture(img.getSubTexture(0, 0, 256, 256));
				for (int i = 0; i < 4; i++) {
					tex[TextInput.SHEET1 + i] = loadTexture(img.getSubTexture(256, i * 111, 236, 111));
				}
				tex[TextInput.COVER] = loadTexture(img.getSubTexture(0, 256, 240, 104));
				tex[TextInput.BB] = loadTexture(img.getSubTexture(492, 0, 37, 22));
				tex[TextInput.BBA] = loadTexture(img.getSubTexture(492, 22, 37, 22));
				tex[TextInput.AB] = loadTexture(img.getSubTexture(529, 0, 37, 22));
				tex[TextInput.ABA] = loadTexture(img.getSubTexture(529, 22, 37, 22));
				for (int i = 0; i < 3; i++) {
					tex[TextInput.SEL1 + i] = loadTexture(img.getSubTexture(492 + i * 30, 44, 30, 22));
					tex[TextInput.SEL1A + i] = loadTexture(img.getSubTexture(492 + i * 30, 66, 30, 22));
				}
				tex[TextInput.TB] = loadTexture(img.getSubTexture(0, 360, 240, 46));
				tex[TextInput.SPACE] = loadTexture(img.getSubTexture(566, 7, 12, 3));
				tex[TextInput.SHADOW] = loadTexture(img.getSubTexture(566, 0, 22, 7));
				tex[TextInput.CHARSELECT] = loadTexture(img.getSubTexture(582, 44, 16, 16));
				tex[TextInput.SELS] = loadTexture(img.getSubTexture(492, 202, 30, 22));
				tex[TextInput.BS] = loadTexture(img.getSubTexture(492, 224, 37, 22));
				return tex;
			}
			case "BattleMenu": {
				int[] tex = new int[102];
				Texture img = ImageReader.loadTexture(TEXTUREPATH + "BattleMenu" + TEXTURETYPE);
				img.alpha = Texture.SIMPLE_ALPHA;
				for (int i = 0; i < 3; i++) {
					tex[BattleMenu.BUTTONATTACK + i] = loadTexture(img.getSubTexture(0, i * 88, 208, 88));
					tex[BattleMenu.BUTTONBAG + i] = loadTexture(img.getSubTexture(208, i * 46, 78, 46));
					tex[BattleMenu.BUTTONRUN + i] = loadTexture(img.getSubTexture(286, i * 46, 78, 46));
					tex[BattleMenu.BUTTONTEAM + i] = loadTexture(img.getSubTexture(364, i * 46, 78, 46));
					tex[BattleMenu.BUTTONCANCEL + i] = loadTexture(img.getSubTexture(208, 138 + i * 46, 238, 46));
				}
				for (int i = 0; i < 4; i++) {
					tex[BattleMenu.BUTTONSEL + i] = loadTexture(img.getSubTexture(442 + i % 2 * 12, i / 2 * 12, 12, 12));
				}
				for (int i = 0; i < 3; i++) {
					tex[BattleMenu.BALL + i] = loadTexture(img.getSubTexture(466 + i * 16, 0, 16, 16));
				}
				tex[BattleMenu.BALL + 3] = loadTexture(img.getSubTexture(464, 33, 16, 16));
				tex[BattleMenu.CAUGHT] = loadTexture(img.getSubTexture(466, 16, 7, 7));
				tex[BattleMenu.MALE] = loadTexture(img.getSubTexture(508, 16, 6, 9));
				tex[BattleMenu.FEMALE] = loadTexture(img.getSubTexture(502, 16, 6, 9));
				tex[BattleMenu.HPBAR] = loadTexture(img.getSubTexture(442, 25, 66, 8));
				for (int i = 0; i < 3; i++) {
					tex[BattleMenu.HPGREEN + i] = loadTexture(img.getSubTexture(508 + i, 25, 1, 3));
				}
				tex[BattleMenu.XPBAR] = loadTexture(img.getSubTexture(538, 78, 102, 7));
				tex[BattleMenu.XP] = loadTexture(img.getSubTexture(511, 25, 1, 2));
				tex[BattleMenu.INFOENEMY] = loadTexture(img.getSubTexture(514, 0, 122, 35));
				tex[BattleMenu.INFOFRIENDLY] = loadTexture(img.getSubTexture(512, 35, 128, 43));
				for (int i = 0; i < 6; i++) {
					tex[BattleMenu.STATUS + i] = loadTexture(img.getSubTexture(442, 33 + i * 8, 22, 8));
				}
				for (int i = 0; i < 20; i++) {
					tex[BattleMenu.TYPE + i] = loadTexture(img.getSubTexture(446 + (i / 10) * 32, 81 + (i % 10) * 14, 32, 14));
					tex[BattleMenu.ATTACKBG + i] = loadTexture(img.getSubTexture((i % 5) * 124, 276 + (i / 5) * 54 * 2, 124, 54));
					tex[BattleMenu.ATTACKBGA + i] = loadTexture(img.getSubTexture((i % 5) * 124, 276 + 54 + (i / 5) * 54 * 2, 124, 54));
				}
				tex[BattleMenu.ATTACKBGBASE] = loadTexture(img.getSubTexture(516, 85, 124, 54));
				tex[BattleMenu.ATTACKBGEMPTY] = loadTexture(img.getSubTexture(514, 139, 126, 54));
				return tex;
			}
			default:
				Logger.add(Logger.GRAPHICS, "Unknown graphics file: ", texName);
		}
		return null;
	}

	public static int[][][] getTextTextures() {
		int[] charStyleDims = Font.getCharStyleDims();
		int char_styles = charStyleDims[0];
		int rows = charStyleDims[1];
		int columns = charStyleDims[2];

		int[][] indtex = new int[char_styles][rows * columns];
		int[][] palettes = new int[char_styles][Font.PALETTENUM];

		Texture img = ImageReader.loadTexture(GraphicHandler.TEXTUREPATH + "Text" + GraphicHandler.TEXTURETYPE);
		Texture img2 = ImageReader.loadTexture(GraphicHandler.TEXTUREPATH + "TextPalettes" + GraphicHandler.TEXTURETYPE);
		img.alpha = Texture.SIMPLE_ALPHA;
		int y_base = 0;
		for (int style = 0; style < char_styles; style++) {
			for (int i = 0; i < Font.PALETTENUM; i++) {
				palettes[style][i] = loadPalette(img2.image[style * Font.PALETTENUM + i]);
			}
			int height = Font.getCharHeight(style);
			int width = Font.getCharWidth('\0', style);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					Texture sub = img.getSubTexture(j * width, i * height + y_base, width, height);
					IndexedTexture sub2 = new IndexedTexture(sub, img2.image[style * Font.PALETTENUM]);
					indtex[style][i * columns + j] = loadIndexedTexture(sub2);
				}
			}
			y_base += height * rows;
		}
		return new int[][][]{indtex, palettes};
	}

	/**
	 * Draws a texture to the buffer.
	 */
	public static void drawBox(int x, int y, int textureID) {
		gd.drawTexture(x, y, textureID);
	}

	/**
	 * Draws a texture to the buffer.
	 */
	public static void drawBox(int[] coords, int textureID) {
		gd.drawTexture(coords[0], coords[1], textureID);
	}

	/**
	 * Draws a texture to the buffer.
	 */
	public static void drawBox(int[] coords, int row, int collumn, int textureID) {
		gd.drawTexture(coords[0] + coords[4] * collumn, coords[1] + coords[5] * row, textureID);
	}

	/**
	 * Loads multiple textures stored in a file.
	 */
	public static int[] loadBufs(String name, int w, int h, int pw, int ph) {
		Texture img = ImageReader.loadTexture(TEXTUREPATH + name + TEXTURETYPE);
		int[] subs = new int[w * h];
		for (int i = 0; i < subs.length; i++) {
			subs[i] = loadTexture(img.getSubTexture(i % w * pw, i / w * ph, pw, ph));
		}
		return subs;
	}

	/**
	 * Return the index of the icon of the item with the given id.
	 */
	public static int getItemIcon(int id) {
		if (id < 0 || id >= itemtextures.length) {
			Logger.add(Logger.GRAPHICS, "Trying to draw illegal item icons");
			return DEFAULTTEXTUREID;
		}
		return itemtextures[id];
	}

	/**
	 * Loads the tileset with the given index and returns in array of texture
	 * indices.
	 */
	public static int[] giveTexturesSet(int i) {
		int[] tex = new int[16 * 16];
		Texture img = ImageReader.loadTexture(TEXTUREPATH + "tiles" + TEXTURETYPE);
		for (int j = 0; j < tex.length; j++) {
			Texture sub = img.getSubTextureAlpha(j % 8 * 16, j / 8 * 16, 16, 16);
			tex[j] = loadTexture(sub);
		}
		return tex;
	}

	/**
	 * Return the index of the icon of the pokemon with the given id in the
	 * given scale.
	 */
	public static int getPokemonIcon(Pokemon p, int scale) {
		// TODO Auto-generated method stub
		if (p.isEgg()) {
			return getEggIcon(scale);
		}
		if (scale == 0 && p.id <= 151) {
			return pokemon_icon_textures[p.id];
		}
		if (scale == 1 && p.id <= 151) {
			return pokemon_front_textures[p.id];
		}
		return DEFAULTTEXTUREID;
	}

	/**
	 * Return the index of the icon of a pokemon egg in the given scale.
	 */
	public static int getEggIcon(int scale) {
		// TODO Auto-generated method stub
		return DEFAULTTEXTUREID;
	}

	/**
	 * Sets the given instance of this class.
	 */
	public static void set(GraphicDisplay graphicDisplay) {
		gd = graphicDisplay;
	}

	/**
	 * Translates all future drawings by the given coordinates.
	 */
	public static void translate(int startx, int starty) {
		gd.move(startx, starty);
	}

	/**
	 * Translates all future drawings by the given coordinates, relative to the
	 * screen size.
	 */
	public static void translateStep(int x, int y, int step) {
		gd.move(x * (GraphicDisplay.SIZE / step), y * (GraphicDisplay.SIZE / step));
	}

	/**
	 * Draws a single colour rectangle to the whole screen.
	 */
	public static void drawRectangleMax(int red, int blue, int green, int alpha) {
		gd.drawRectangle(0, 0, GraphicDisplay.SIZE, GraphicDisplay.SIZE, red, blue, green, alpha);
	}

	/**
	 * Draws a single colour rectangle to the whole screen.
	 */
	public static void drawRectangleMax(int red, int blue, int green) {
		gd.drawRectangle(0, 0, GraphicDisplay.SIZE, GraphicDisplay.SIZE, red, blue, green, 255);
	}

	public static void takeScreenshot() {
		gd.takeScreenshot();
	}

	public static void drawIndexedBox(int x, int y, int textureID, int palette) {
		gd.drawIndexedTexture(x, y, textureID, palette);
	}

	public static void unloadTextures(int[] ids) {
		for (int id : ids) {
			gd.unloadTexture(id);
		}
	}

	public static void unloadPalettes(int[] ids) {
		for (int id : ids) {
			gd.unloadPalette(id);
		}
	}

	public static void unloadIndexedTextures(int[] ids) {
		for (int id : ids) {
			gd.unloadIndexedTexture(id);
		}
	}

	public static void unloadTextures(int[][] ids) {
		for (int[] id : ids) {
			unloadTextures(id);
		}
	}

	public static void unloadPalettes(int[][] ids) {
		for (int[] id : ids) {
			unloadPalettes(id);
		}
	}

	public static void unloadIndexedTextures(int[][] ids) {
		for (int[] id : ids) {
			unloadIndexedTextures(id);
		}
	}

}
