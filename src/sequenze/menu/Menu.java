package sequenze.menu;

import sequenze.Button;
import sequenze.Direction;
import sequenze.Event;
import sequenze.Game;
import sequenze.GraphicHandler;

import static sequenze.InstancePackage.*;

import sequenze.Localisation;
import sequenze.overlay.MenuObjectHandler;
import sequenze.MessageHandler;
import sequenze.SystemControll;
import sequenze.Text;
import util.ConV;

public class Menu extends GuiItem {

	int selection = 0; // currently selected option
	boolean[] visible; // whether each option is visible
	public int[] tex; // textures of the menu
	public final static int BG = 0; // background
	public final static int MB = 1; // menu box
	public final static int ARB = 2; // arrow box
	public final static int ARU = 3; // arrow up
	public final static int ARD = 4; // arrow down
	public final static int SB = 5; // selection box
	public final static int RUN = 6; // run button
	public final static int RUNT = 7; // run button unavailable
	public final static int RUNA = 8; // run button activated
	public final static int RUNAT = 9; // run button activated unavailable
	public final static int REG1 = 10; // 1. registered item box
	public final static int REG1T = 11; // 1. registered item box unavailable
	public final static int REG2 = 12; // 2. registered item box
	public final static int REG2T = 13; // 2. registered item box unavailable
	public final static int AB = 14; // A box
	public final static int ABA = 15; // A box activated
	public final static int MI = 16; // menu item box 1 - 7
	public final static int MIT = 17; // menu item box unavailable 1 - 7
	byte status = MENUD; // current status of the menu
	final static byte MENUA = 0;
	final static byte MENUD = 1;
	final static byte SAVE = 6;
	final static byte CLOSED = 9;
	final static byte AnimDownStart = 8;
	final static byte AnimRightStart = 16;
	final static byte AnimUpStart = 24;
	final static byte AnimLeftStart = 32;
	final static byte AnimActivationStart = 48;
	/** Whether the A button box is currently being pressed. */
	private boolean mouseDownA;

	private Cell background;
	private Cell menubox;
	private Cell arrowbox;
	private Cell arrow;
	private Cell runbutton;
	private Cell regis;
	private Cell regisIcon;
	private Cell abutton;
	private Cell items;

	public Menu() {
		pokedex = new Pokedex();
		team = new Team();
		bag = new Bag();
		gadget = new Gadget();
		options = new Options();
		card = new TrainerCard();
		summary = new Summary();
		tex = GraphicHandler.giveTextures("IGMenu");
		setupCells();
		// TODO update visibility
		visible = new boolean[items.columns * items.rows];
		for (int i = 0; i < 8; i++) {
			visible[i] = SystemControll.getFlag(SystemControll.PokedexUnlocked + i);
		}

		arrow.animate(false);
	}

	public void setupCells() {
		background = new Cell();
		background.x = 0;
		background.y = 0;
		background.w = 256;
		background.h = 256;
		background.icon = new Icon(tex[BG], 0, 0);

		arrow = new Cell();
		arrow.x = 114;
		arrow.y = 16;
		arrow.w = 20;
		arrow.h = 26;
		arrow.icon = new Icon(tex[ARU], 0, tex[ARD]);

		arrowbox = new Cell();
		arrowbox.x = 0;
		arrowbox.y = 205;
		arrowbox.w = 256;
		arrowbox.h = 51;
		arrowbox.icon = new Icon(tex[ARB], 0, 0);
		arrowbox.setChildren(arrow);

		runbutton = new Cell();
		runbutton.x = 184;
		runbutton.y = 98;
		runbutton.w = 72;
		runbutton.h = 42;
		runbutton.icon_type = Cell.MULTIICON;
		runbutton.starts = new int[]{0};
		runbutton.icons = new Icon[1][4];
		runbutton.icons[0][0] = new Icon(tex[RUN], 0, 0);
		runbutton.icons[0][1] = new Icon(tex[RUNT], 0, 0);
		runbutton.icons[0][2] = new Icon(tex[RUNA], 0, 0);
		runbutton.icons[0][3] = new Icon(tex[RUNAT], 0, 0);

		regisIcon = new Cell();
		regisIcon.x = 21;
		regisIcon.y = 4;
		regisIcon.w = 24;
		regisIcon.h = 24;

		regis = new Cell();
		regis.x = 200;
		regis.y = 8;
		regis.w = 56;
		regis.h = 38;
		regis.arangement = Cell.COLLUMN;
		regis.y_row = 40;
		regis.icon_type = Cell.MULTIICON;
		regis.starts = new int[]{0, 1};
		regis.icons = new Icon[2][2];
		regis.icons[0][0] = new Icon(tex[REG1], 0, 0);
		regis.icons[0][1] = new Icon(tex[REG1T], 0, 0);
		regis.icons[1][0] = new Icon(tex[REG2], 0, 0);
		regis.icons[1][1] = new Icon(tex[REG2T], 0, 0);
		regis.setChildren(regisIcon);

		abutton = new Cell();
		abutton.x = 168;
		abutton.y = 147;
		abutton.w = 88;
		abutton.h = 42;
		abutton.icon_type = Cell.MULTIICON;
		abutton.addText(24, 13, 2, 0x1, Text.TEXT_LEFT);
		abutton.starts = new int[]{0};
		abutton.icons = new Icon[1][2];
		abutton.icons[0][0] = new Icon(tex[AB], 0, 0);
		abutton.icons[0][1] = new Icon(tex[ABA], 0, 0);

		// TODO needs multiple animations for selecting menu icons
		// TODO needs different animation for activation
		items = new Cell();
		items.x = 21;
		items.y = 22;
		items.w = 40;
		items.h = 26;
		items.arangement = Cell.GRID;
		items.x_col = 80;
		items.y_row = 40;
		items.columns = 2;
		// TODO will not be saved, store differently
		items.rows = 4;
		items.icon_type = Cell.MULTIICON;
		items.addText(20, 26, 0, 0x2, Text.TEXT_MIDDLE);
		items.starts = new int[]{0, 1, 2, 3, 4, 5, 6};
		items.icons = new Icon[7][2];
		for (int i = 0; i < 7; i++) {
			items.icons[i][0] = new Icon(tex[MI + i * 2], tex[SB], 0);
			items.icons[i][1] = new Icon(tex[MIT + i * 2], 0, 0);
		}

		menubox = new Cell();
		menubox.x = 3;
		menubox.y = 0;
		menubox.w = 162;
		menubox.h = 188;
		menubox.addText(69, 0, 0, 0x2, Text.TEXT_LEFT);
		menubox.icon = new Icon(tex[MB], 0, 0);
		menubox.setChildren(items);
	}

	/** Draws the left section of the menu. */
	@Override
	public void drawL() {
		switch (status) {
			case MENUA:
				drawHintArrow();
				break;
			case MENUD:
				break;
			case CLOSED:
				pokedex.drawL();
				team.drawL();
				bag.drawL();
				gadget.drawL();
				options.drawL();
				card.drawL();
				summary.drawL();
				break;
		}
	}

	/** Draws the right section of the menu. */
	@Override
	public void drawR() {
		switch (status) {
			case MENUA:
				drawMenu(true);
				break;
			case MENUD:
				if (SystemControll.getFlag(SystemControll.MenuAvailable)) {
					drawMenu(false);
				}
				break;
			case CLOSED:
				pokedex.drawR();
				team.drawR();
				bag.drawR();
				gadget.drawR();
				options.drawR();
				card.drawR();
				summary.drawR();
				break;
		}
	}

	/** Draws the menu itself. */
	private void drawMenu(boolean active) {
		drawMenuBG();
		drawMenuItems(active);
		if (!active) {
			drawRegistered();
			drawBootShortcut();
			drawActionButton();
		}
	}

	/** Draws the arrow telling the player that the focus is at the menu. */
	private void drawHintArrow() {
		arrowbox.draw();
		arrowbox.getChild(0).draw();
	}

	/** Draw the icons for the registered items. */
	private void drawRegistered() {
		boolean transparent = MenuObjectHandler.current.isActive();
		for (int i = 0; i < SystemControll.getVariable(SystemControll.RegisteredNum); i++) {
			int item = SystemControll.getVariable(SystemControll.Registered1 + i);
			if (item == 0) {
				return;
			}
			int t = 0;
			int item_t;
			if (transparent) {
				t++;
				// TODO missing semi transparancy on item icon
				item_t = GraphicHandler.getItemIcon(item);
			} else {
				item_t = GraphicHandler.getItemIcon(item);
			}
			regis.drawMulti(null, i, t, false);
			regis.getChildMulti(0, i).draw(new Icon(item_t, 0, 0), null, false);
		}
	}

	/** Draws the icon for the running boots button. */
	private void drawBootShortcut() {
		if (!SystemControll.getFlag(SystemControll.ShoesUnlocked)) {
			return;
		}
		int t;
		if (SystemControll.getFlag(SystemControll.ShoeOverride)) {
			t = 2;
		} else {
			t = 0;
		}
		// TODO not just menu objects but scripts as well block interaction
		if (MenuObjectHandler.current.isActive()) {
			t++;
		}
		runbutton.draw(t, false);
	}

	/** Draws the icon for the button that triggers an activate event. */
	private void drawActionButton() {
		char[][] text = Localisation.getMenuABox();
		int t_index = 0;
		if (MenuObjectHandler.current.isActive()) {
			t_index = 1;
		} else {
			Event[] events = world.getEvents(player.x, player.y);
			int[] pos = world.toMapCoord(player.x, player.y);
			for (Event event : events) {
				if (event.x != pos[0] && event.y != pos[1]) {
					continue;
				}
				if (Direction.getSimpleDirection(pos[0], pos[1], event.x, event.y) != player.orientation) {
					continue;
				}
				if (ConV.abs(pos[0] - event.x) > 1 || ConV.abs(pos[1] - event.y) > 1) {
					continue;
				}
				// TODO check if event is person or pokemon, otherwise continue;
				t_index = 2;
				break;
			}
		}
		int t;
		if (mouseDownA) {
			t = 1;
		} else {
			t = 0;
		}
		abutton.draw(text[t_index], t, false);
	}

	/** Draws the menu options. */
	private void drawMenuItems(boolean active) {
		menubox.draw(Localisation.getMenuTitle());
		Cell its = menubox.getChild(0);

		char[][] text = Localisation.getMenuItems();
		for (int i = 0; i < 7; i++) {
			if (!visible[i]) {
				continue;
			}
			if (!active) {
				its.drawMulti(text[i], i, 0, false);
				continue;
			}
			if (selection != i) {
				its.drawMulti(text[i], i, 1, false);
				continue;
			}
			its.drawMulti(text[i], i, 0, true);
		}
	}

	/** Draws the background for the menu. */
	private void drawMenuBG() {
		background.draw();
	}

	/** Updates the menu and processes events and key-presses. */
	@Override
	public void update(Game game) {
		switch1:
		switch (status) {
			case MENUA:
				if (items.animationRunning) {
					if (items.animationFinished()) {
						activate(selection);
					}
					break;
				}
				if (arrow.animationFinished()) {
					arrow.animate(false);
				}
				// TODO update left/right/up/down animation
				if (game.wasPressed(Button.START) || game.wasPressed(Button.B)) {
					game.process(Button.START);
					game.process(Button.B);
					status = MENUD;
				} else if (game.wasPressed(Button.A)) {
					game.process(Button.A);
					items.animate(selection, true);
				} else if (game.wasPressed(Button.SELECT)) {
					game.process(Button.SELECT);
					// TODO activate base item 0
				} else {
					int rows = items.rows;
					int cols = items.columns;
					int row = selection / cols;
					int col = selection % cols;
					if (game.wasPressed(Button.DOWN)) {
						game.process(Button.DOWN);
						// animation down start
						for (int i = selection + cols; i < rows * cols; i += cols) {
							if (visible[i]) {
								selection = (byte) i;
								break switch1;
							}
						}
						for (int i = col; i < selection; i += cols) {
							if (visible[i]) {
								selection = (byte) i;
								break switch1;
							}
						}
						for (int i = 1; i < Math.max(col, cols - col - 1); i++) {
							for (int j = selection + cols; j < rows * cols; j += cols) {
								if (col - i >= 0 && visible[j - i]) {
									selection = (byte) (j - i);
									break switch1;
								} else if (col + i < cols && visible[j + i]) {
									selection = (byte) (j + i);
									break switch1;
								}
							}
						}
						for (int i = 1; i < Math.max(col, cols - col - 1); i++) {
							for (int j = col; j < selection; j += cols) {
								if (col - i >= 0 && visible[j - i]) {
									selection = (byte) (j - i);
									break switch1;
								} else if (col + i < cols && visible[j + i]) {
									selection = (byte) (j + i);
									break switch1;
								}
							}
						}
					} else if (game.wasPressed(Button.RIGHT)) {
						game.process(Button.RIGHT);
						// animation right start
						for (int i = selection + 1; i < (row + 1) * cols; i++) {
							if (visible[i]) {
								selection = (byte) i;
								break switch1;
							}
						}
						for (int i = row * cols; i < selection; i++) {
							if (visible[i]) {
								selection = (byte) i;
								break switch1;
							}
						}
						for (int i = 1; i < Math.max(row, rows - row - 1); i++) {
							for (int j = selection + 1; j < (row + 1) * cols; j++) {
								if (row - i >= 0 && visible[j - i * cols]) {
									selection = (byte) (j - i * cols);
									break switch1;
								} else if (row + i < rows && visible[j + i * cols]) {
									selection = (byte) (j + i * cols);
									break switch1;
								}
							}
						}
						for (int i = 1; i < Math.max(row, rows - row - 1); i++) {
							for (int j = row * cols; j < selection; j++) {
								if (row - i >= 0 && visible[j - i * cols]) {
									selection = (byte) (j - i * cols);
									break switch1;
								} else if (row + i < rows && visible[j + i * cols]) {
									selection = (byte) (j + i * cols);
									break switch1;
								}
							}
						}
					} else if (game.wasPressed(Button.UP)) {
						game.process(Button.UP);
						// animation up start
						for (int i = selection - cols; i >= 0; i -= cols) {
							if (visible[i]) {
								selection = (byte) i;
								break switch1;
							}
						}
						for (int i = (rows - 1) * cols + col; i > selection; i -= cols) {
							if (visible[i]) {
								selection = (byte) i;
								break switch1;
							}
						}
						for (int i = 1; i < Math.max(col, cols - col - 1); i++) {
							for (int j = selection - cols; j < 0; j -= cols) {
								if (col - i >= 0 && visible[j - i]) {
									selection = (byte) (j - i);
									break switch1;
								} else if (col + i < cols && visible[j + i]) {
									selection = (byte) (j + i);
									break switch1;
								}
							}
						}
						for (int i = 1; i < Math.max(col, cols - col - 1); i++) {
							for (int j = (row - 1) * cols + col; j < selection; j -= cols) {
								if (col - i >= 0 && visible[j - i]) {
									selection = (byte) (j - i);
									break switch1;
								} else if (col + i < cols && visible[j + i]) {
									selection = (byte) (j + i);
									break switch1;
								}
							}
						}
					} else if (game.wasPressed(Button.LEFT)) {
						game.process(Button.LEFT);
						// animation left start
						for (int i = selection - 1; i >= row * cols; i--) {
							if (visible[i]) {
								selection = (byte) i;
								break switch1;
							}
						}
						for (int i = (row + 1) * cols - 1; i > selection; i--) {
							if (visible[i]) {
								selection = (byte) i;
								break switch1;
							}
						}
						for (int i = 1; i < Math.max(row, rows - row - 1); i++) {
							for (int j = selection - 1; j >= row * cols; j--) {
								if (row - i >= 0 && visible[j - i * cols]) {
									selection = (byte) (j - i * cols);
									break switch1;
								} else if (row + i < rows && visible[j + i * cols]) {
									selection = (byte) (j + i * cols);
									break switch1;
								}
							}
						}
						for (int i = 1; i < Math.max(row, rows - row - 1); i++) {
							for (int j = (row + 1) * cols - 1; j > selection; j--) {
								if (row - i >= 0 && visible[j - i * cols]) {
									selection = (byte) (j - i * cols);
									break switch1;
								} else if (row + i < rows && visible[j + i * cols]) {
									selection = (byte) (j + i * cols);
									break switch1;
								}
							}
						}
					}

				}
				menuClick(game);
				break;
			case MENUD:
				if (SystemControll.getFlag(SystemControll.MenuAvailable)) {
					if (game.wasPressed(Button.START)) {
						game.process(Button.START);
						status = MENUA;
					} else if (game.wasPressed(Button.SELECT)) {
						game.process(Button.SELECT);
						// TODO activate base item 0
					}
					menuClick(game);
				}
				break;
			case CLOSED:
				if (MessageHandler.hasMessage(MessageHandler.POKEDEX_CLOSE) || MessageHandler.hasMessage(MessageHandler.BAG_CLOSE) || MessageHandler.hasMessage(MessageHandler.GADGET_CLOSE)
						|| MessageHandler.hasMessage(MessageHandler.TEAM_CLOSE) || MessageHandler.hasMessage(MessageHandler.OPTIONS_CLOSE) || MessageHandler.hasMessage(MessageHandler.CARD_CLOSE)) {
					if (MessageHandler.getBool()) {
						status = MENUD;
					} else {
						status = MENUA;
					}
				}
				break;
		}
		pokedex.update(game);
		team.update(game);
		bag.update(game);
		gadget.update(game);
		options.update(game);
		card.update(game);
		summary.update(game);
	}

	private void menuClick(Game g) {
		if (clickABox(g)) {
			return;
		}
		if (!g.mouseClicked()) {
			return;
		}
		int xm = g.getMouseX();
		int ym = g.getMouseY();

		if (clickMenuItems(xm, ym)) {
			g.processMouse();
			return;
		}
		if (clickRegistered(xm, ym)) {
			g.processMouse();
			return;
		}
		if (clickBootShortcut(xm, ym)) {
			g.processMouse();
			return;
		}
	}

	public boolean clickABox(Game g) {
		int xm = g.getMouseX();
		int ym = g.getMouseY();
		if (abutton.click(xm, ym)) {
			if (!mouseDownA && g.mouseClicked()) {
				g.processMouse();
				g.queueAPress();
				mouseDownA = true;
			} else if (!g.mouseDown()) {
				mouseDownA = false;
			}
			return true;
		} else {
			mouseDownA = false;
			return false;
		}
	}

	private boolean clickMenuItems(int xm, int ym) {
		int sel = menubox.getChild(0).clickMulti(xm, ym, 7);
		if (sel == -1 || !visible[sel]) {
			return false;
		}
		selection = sel;
		items.animate(sel, true);
		status = MENUA;
		return true;
	}

	private boolean clickRegistered(int xm, int ym) {
		int sel = regis.clickMulti(xm, ym, SystemControll.getVariable(SystemControll.RegisteredNum));
		if (sel == -1) {
			return false;
		}
		int item = SystemControll.getVariable(SystemControll.Registered1 + sel);
		if (item == 0) {
			return false;
		}
		// TODO activate base item sel
		return true;
	}

	private boolean clickBootShortcut(int xm, int ym) {
		if (!SystemControll.getFlag(SystemControll.ShoesUnlocked)) {
			return false;
		}
		if (runbutton.click(xm, ym)) {
			SystemControll.setFlag(SystemControll.ShoeOverride, !SystemControll.getFlag(SystemControll.ShoeOverride));
			return true;
		}
		return false;
	}

	public void activate(int selected) {
		if (selected == 0) {
			status = CLOSED;
			MessageHandler.add(MessageHandler.POKEDEX_OPEN, null);
		} else if (selected == 1) {
			status = CLOSED;
			MessageHandler.add(MessageHandler.CARD_OPEN, null);
		} else if (selected == 2) {
			status = CLOSED;
			MessageHandler.add(MessageHandler.TEAM_OPEN, null);
		} else if (selected == 3) {
			//status = SAVE;
			save.save();
		} else if (selected == 4) {
			status = CLOSED;
			MessageHandler.add(MessageHandler.BAG_OPEN, null);
		} else if (selected == 5) {
			status = CLOSED;
			MessageHandler.add(MessageHandler.OPTIONS_OPEN, null);
		} else if (selected == 6) {
			status = CLOSED;
			MessageHandler.add(MessageHandler.GADGET_OPEN, null);
		}
	}

	@Override
	public boolean hasFocus() {
		return status == MENUA || pokedex.hasFocus() || team.hasFocus() || bag.hasFocus() || gadget.hasFocus() || options.hasFocus() || card.hasFocus() || summary.hasFocus();
	}

	@Override
	public boolean occupiesLeft() {
		return pokedex.occupiesLeft() || bag.occupiesLeft() || team.occupiesLeft() || gadget.occupiesLeft() || options.occupiesLeft() || card.occupiesLeft() || summary.occupiesLeft();
	}

	@Override
	public boolean occupiesRight() {
		return status != CLOSED || pokedex.occupiesRight() || bag.occupiesRight() || team.occupiesRight() || gadget.occupiesRight() || options.occupiesRight() || card.occupiesRight() || summary.occupiesRight();
	}
}
