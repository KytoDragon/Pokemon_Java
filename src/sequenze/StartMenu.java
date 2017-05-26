package sequenze;

import sequenze.menu.Cell;
import sequenze.menu.Icon;
import sequenze.menu.Icon.DTD;
import sequenze.overlay.MenuObjectHandler;
import sequenze.overlay.TextInput;
import sequenze.overlay.YNQuestion;
import util.ConV;

public class StartMenu {

	SaveGameInfo[] saves;
	int[] tex;
	int animation = 0;
	int selection = 0;
	int viewtop = 0;

	public final static byte BGR = 0;
	public final static byte BGL = 1;
	public final static byte MENUITEM = 2;
	public final static byte MENUITEMS = 3;
	public final static byte OVERVIEW = 4;

	byte status = MENU;
	final static byte MENU = 0;
	final static byte SAVES = 1;
	final static byte OPTIONS = 2;
	final static byte NEWGAME = 3;
	final static byte NEWGAME2 = 4;
	final static byte NEWGAME3 = 5;
	final static byte FINISHED = 6;

	private Cell backgroundr;
	private Cell backgroundl;
	private Cell items;
	private Cell overview;
	private Cell names;
	private Cell stats;

	public StartMenu() {
		saves = SaveSystem.getSaveGameInfos();
		if (saves.length == 0) {
			selection = 1;
		}
		tex = GraphicHandler.giveTextures("StartMenu");
		setupCells();
	}

	public void setupCells() {
		backgroundr = new Cell();
		backgroundr.x = 0;
		backgroundr.y = 0;
		backgroundr.w = 256;
		backgroundr.h = 265;
		backgroundr.icon = new Icon(tex[BGR], 0, 0);

		backgroundl = new Cell();
		backgroundl.x = 0;
		backgroundl.y = 0;
		backgroundl.w = 256;
		backgroundl.h = 265;
		backgroundl.icon = new Icon(tex[BGL], 0, 0);

		items = new Cell();
		items.x = 4;
		items.y = 2;
		items.w = 196;
		items.h = 44;
		items.arangement = Cell.COLLUMN;
		// TODO store elsewhere
		items.rows = 3;
		items.y_row = 48;
		items.addText(6, 5, 0, 0x1, Text.TEXT_LEFT);
		Icon iic = new Icon();
		iic.normal = new DTD[]{new DTD(tex[MENUITEM])};
		iic.sel = new DTD[]{new DTD(tex[MENUITEMS])};
		items.icon = iic;

		stats = new Cell();
		stats.x = 0;
		stats.y = 0;
		stats.icon_type = Cell.TEXTONLY;
		stats.addText(120, 0, 0, 0x12, Text.TEXT_RIGHT);

		names = new Cell();
		names.x = 38;
		names.y = 22;
		names.arangement = Cell.COLLUMN;
		names.y_row = 16;
		names.icon_type = Cell.TEXTONLY;
		names.addText(0, 0, 0, 0x12, Text.TEXT_LEFT);
		names.setChildren(stats);

		overview = new Cell();
		overview.x = 4;
		overview.y = 2;
		overview.w = 196;
		overview.h = 92;
		overview.addText(6, 5, 0, 0x1, Text.TEXT_LEFT);
		overview.icon = new Icon(tex[OVERVIEW], 0, 0);
		overview.setChildren(names);
	}

	public void update(Game g) {
		switch (status) {
			case MENU: {
				if (animation > 0) {
					if (animation == 16) {
						animation = 0;
						if (selection == 0) {
							if (saves.length == 1) {
								saves[0].load();
								status = FINISHED;
							} else {
								status = SAVES;
								selection = 0;
							}
						} else if (selection == 1) {
							status = NEWGAME;
						} else if (selection == 2) {
							status = OPTIONS;
							selection = 0;
						}
					} else {
						animation++;
					}
					break;
				}
				if (g.wasPressed(Button.A)) {
					g.process(Button.A);
					animation = 1;
				} else if (g.wasPressed(Button.UP)) {
					g.process(Button.UP);
					if (selection > 1 || (selection > 0 && saves.length > 0)) {
						selection--;
					}
				} else if (g.wasPressed(Button.DOWN)) {
					g.process(Button.DOWN);
					if (selection < 2) {
						selection++;
					}
				}
				break;
			}
			case SAVES: {
				if (animation > 0) {
					if (animation == 16) {
						status = MENU;
						animation = 0;
						selection = 0;
						viewtop = 0;
					} else {
						animation++;
					}
					break;
				}
				if (g.wasPressed(Button.A)) {
					g.process(Button.A);
					saves[selection].load();
					status = FINISHED;
				} else if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					animation = 1;
				} else if (g.wasPressed(Button.UP)) {
					g.process(Button.UP);
					if (selection > 0) {
						selection--;
						if (viewtop > selection) {
							viewtop--;
						}
					}
				} else if (g.wasPressed(Button.DOWN)) {
					g.process(Button.DOWN);
					if (selection < saves.length - 1) {
						selection++;
						if (viewtop <= selection - items.rows) {
							viewtop++;
						}
					}
				}
				break;
			}
			case OPTIONS: {
				// TODO change options
				break;
			}
			case NEWGAME: {
				TextInput input = new TextInput(7, false, Gender.IRRELEVANT);
				MenuObjectHandler.current.setObject(input);
				status = NEWGAME2;
				break;
			}
			case NEWGAME2: {
				char[] name = SystemControll.getString(SystemControll.InputText);
				if (name == null || name.length == 0) {
					status = MENU;
					selection = 1;
				} else {
					boolean found = false;
					for (SaveGameInfo save : saves) {
						if (ConV.equals(save.name, name)) {
							found = true;
							break;
						}
					}
					if (found) {
						MenuObjectHandler.current.setObject(new YNQuestion("Save state with that name already exists. Do you want to overwrite it?".toCharArray()));
						status = NEWGAME3;
					} else {
						SaveGameInfo[] tmp = ConV.arrayCopy(saves, saves.length + 1);
						SaveGameInfo newSafe = new SaveGameInfo();
						newSafe.name = name;
						tmp[saves.length] = newSafe;
						saves = tmp;
						newSafe.init();
						status = FINISHED;
					}
				}
				break;
			}
			case NEWGAME3: {
				if (!SystemControll.getFlag(SystemControll.QuestionAnswer)) {
					TextInput input = new TextInput(7, false, Gender.IRRELEVANT);
					MenuObjectHandler.current.setObject(input);
					status = NEWGAME2;
					break;
				}
				char[] name = SystemControll.getString(SystemControll.InputText);
				SaveGameInfo newSafe = new SaveGameInfo();
				newSafe.name = name;
				for (int i = 0; i < saves.length; i++) {
					if (ConV.equals(saves[i].name, name)) {
						saves[i].remove();
						saves[i] = newSafe;
						break;
					}
				}
				newSafe.init();
				status = FINISHED;
				break;
			}
		}
	}

	public boolean isActive() {
		return status != FINISHED;
	}

	public void drawR() {
		switch (status) {
			case MENU:
				drawBackgroundR();
				drawMenu();
				break;
			case SAVES:
				drawBackgroundR();
				drawSaves();
				break;
			case OPTIONS:
				drawBackgroundR();
				drawOptions();
				break;
		}
	}

	public void drawL() {
		switch (status) {
			case MENU:
				drawBackgroundL();
				if (selection == 0 && saves.length == 1) {
					drawSaveInfo(0);
				}
				break;
			case SAVES:
				drawBackgroundL();
				drawSaveInfo(selection);
				break;
			case OPTIONS:
				drawBackgroundL();
				drawOptionDescription();
				break;
		}
	}

	private void drawOptionDescription() {
		// TODO Auto-generated method stub

	}

	private void drawSaveInfo(int selection) {
		SaveGameInfo save = saves[selection];
		overview.draw(save.name);
		Cell list = overview.getChild(0);
		list.drawMulti("PLAYER".toCharArray(), 0);
		list.getChildMulti(0, 0).draw(save.charName);
		list.drawMulti("TIME".toCharArray(), 1);
		long minutes = save.timePlayed / 60;
		list.getChildMulti(0, 1).draw(ConV.sprintf("\\s0000:\\s0001\\s0002", (minutes / 60), (minutes % 60 / 10), (minutes % 10)));
		list.drawMulti("BADGES".toCharArray(), 2);
		list.getChildMulti(0, 2).draw(ConV.toCString(save.badges));
		list.drawMulti("POKéDEX".toCharArray(), 3);
		list.getChildMulti(0, 3).draw(ConV.toCString(save.pokemon));
	}

	private void drawBackgroundL() {
		backgroundl.draw();
	}

	private void drawOptions() {
		// TODO Auto-generated method stub

	}

	private void drawSaves() {
		for (int i = 0; i < items.rows && viewtop + i < saves.length; i++) {
			items.drawMulti(saves[viewtop + i].name, i, selection == viewtop + i);
		}
	}

	private void drawMenu() {
		if (saves.length == 1) {
			items.drawMulti("CONTINUE".toCharArray(), 0, selection == 0);
		} else if (saves.length > 1) {
			items.drawMulti("SAVES".toCharArray(), 0, selection == 0);
		}
		// TODO make NEW GAME first when no save available?
		items.drawMulti("NEW GAME".toCharArray(), 1, selection == 1);
		items.drawMulti("OPTIONS".toCharArray(), 2, selection == 2);
	}

	private void drawBackgroundR() {
		backgroundr.draw();
	}
}
