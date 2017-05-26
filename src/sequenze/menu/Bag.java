package sequenze.menu;

import script.Interpreter;
import sequenze.Button;
import sequenze.Direction;
import sequenze.EventMethodLibrary;
import sequenze.FlagHandler;
import sequenze.Game;
import sequenze.GraphicHandler;
import sequenze.InfoLoader;

import static sequenze.InstancePackage.*;

import sequenze.Localisation;
import sequenze.overlay.MenuObjectHandler;
import sequenze.MessageHandler;
import sequenze.SystemControll;
import sequenze.Text;
import sequenze.overlay.TextBox;
import util.ConV;

// TODO use Localisation
public class Bag extends GuiItem {

	/** An array of item bags, index 0 is the base item bag. */
	private BagList[] bags;
	/** Index of the currently active item bag, 0 is the base item bag, 1-7 are item bags left to right. */
	private int currentSubBag = 1;
	/** Index of the currently selected button. (0 for items, 1-4 for options, 5 for back, 8 - 15 for bags) */
	private int selection = 0;
	/** Amount of items about to be deleted. */
	private int deletionAmount = 1;
	/** Position that the selected item is to be moved to. */
	private int moveTo = 0;
	/** Array of texture indices. */
	public int[] tex;
	public final static byte SBG = 0; // left screen background
	public final static byte DTF = 1; // text field for descriptions
	public final static byte SBF = 2; // selection frame for sub-bags
	public final static byte VO = 3; // visible option field
	public final static byte SO = 4; // selection for option fields
	public final static byte AO = 5; // selected and activated option field
	public final static byte LA = 6; // left arrow
	public final static byte LAA = 7; // left arrow activated
	public final static byte RA = 8; // right arrow
	public final static byte RAA = 9; // right arrow activated
	public final static byte BB = 10; // back button
	public final static byte BBA = 11; // back button activated
	public final static byte BBS = 12; // back button selected
	public final static byte IBG = 13; // background for item icons in the selection
	public final static byte IBGS = 14; // background for item icons in the selection if selected
	public final static byte IBG2 = 15; // background for the item icon that is selected
	public final static byte TI = 16; // trash input box
	public final static byte TAU = 17; // trash input arrow up
	public final static byte TAU2 = 18; // trash input arrow up activated
	public final static byte TAD = 19; // trash input arrow down
	public final static byte TAD2 = 20; // trash input arrow down activated
	public final static byte TA = 21; // trash accept button
	public final static byte TAA = 22; // trash accept button activated
	public final static byte TD = 23; // trash deny button
	public final static byte TDA = 24; // trash deny button activated
	public final static byte BG = 25; // sub-bag background, 0 - 7
	public final static byte SBI = 33; // sub-bag icon, 10 - 17
	public final static byte SBIS = 41; // selected sub-bag icon, 18 - 25
	public final static byte IIBG = 49; // background for invisible items in the selection
	public final static byte IO = 57; // invisible option field
	private byte status = CLOSED;
	final static byte STARTUP = 0; // opening the bag with a fade in
	final static byte SHUTDOWN = 1; // closing the bag with a fade out
	final static byte ITEMSELECTION = 2; // selecting an item
	final static byte ITEMOPTIONS = 3; // choosing what do to with the selected item
	final static byte MOVEITEM = 4; // moving an item to a different place
	final static byte DELETEITEM = 5; // deleting an item, choosing the amount
	final static byte CLOSED = 8; // closed
	private boolean clickClose;

	private Cell tb;
	private Cell options;
	private Cell back;
	private Cell basebag;
	private Cell itembags;
	private Cell arrows;
	private Cell trashaccept;
	private Cell trashback;
	private Cell trasharrows;
	private Cell trashbox;
	private Cell trashnumbers;
	private Cell backgrounds;
	private Cell backgroundL;
	private Cell descBox;
	private Cell items;
	private Cell itemicon;
	private Cell selitem;
	private Cell page;

	/** Creates a new bag and fills it using the flag handler and loads textures. */
	public Bag() {
		FlagHandler fh = FlagHandler.current();
		fh.initItemLoader();
		bags = new BagList[InfoLoader.getBagCount() + 1];
		bags[0] = new BaseItemList();
		for (int i = 0; i < bags.length - 1; i++) {
			bags[i + 1] = new ItemList();
		}
		while (fh.hasItem()) {
			int[] item = fh.nextItem();
			int bag = InfoLoader.getBagForItem(item[0]);
			if (bag < 0) {
				continue;
			}
			if (bag == 0) {
				bags[bag].initAdd(item[0], 0);
			} else {
				bags[bag].initAdd(item[0], item[1]);
			}
		}
		tex = GraphicHandler.giveTextures("Bag");

		setupCells();
	}

	public void setupCells() {
		tb = new Cell();
		tb.x = 0;
		tb.y = 0;
		tb.w = 256;
		tb.h = 32;
		tb.icon_type = Cell.TEXTBOX;
		tb.generateBoxText(0, 0x10);

		options = new Cell();
		options.x = 1;
		options.y = 195;
		options.w = 94;
		options.h = 26;
		options.arangement = Cell.GRID;
		options.x_col = 96;
		options.y_row = 32;
		options.columns = 2;
		options.icon_type = Cell.MULTIICON;
		options.addText(47, 5, 0, 0x11, Text.TEXT_MIDDLE);
		options.starts = new int[]{0};
		options.icons = new Icon[1][1 + bags.length];
		options.icons[0][0] = new Icon(tex[VO], tex[SO], tex[AO]);
		for (int i = 0; i < bags.length; i++) {
			options.icons[0][i + 1] = new Icon(tex[IO + i], tex[SO], 0);
		}

		back = new Cell();
		back.x = 193;
		back.y = 227;
		back.w = 62;
		back.h = 26;
		back.addText(31, 5, 0, 0x11, Text.TEXT_MIDDLE);
		back.icon = new Icon(tex[BB], tex[BBS], tex[BBA]);

		basebag = new Cell();
		basebag.x = 228;
		basebag.y = 4;
		basebag.w = 24;
		basebag.h = 24;
		basebag.icon_type = Cell.MULTIICON;
		basebag.icons = new Icon[1][2];
		basebag.starts = new int[]{0};
		basebag.icons[0][0] = new Icon(tex[SBI], tex[SBF], 0).moveSel(-1, -1);
		basebag.icons[0][1] = new Icon(tex[SBIS], tex[SBF], 0).moveSel(-1, -1);

		itembags = new Cell();
		itembags.x = 4;
		itembags.y = 4;
		itembags.w = 24;
		itembags.h = 24;
		itembags.arangement = Cell.ROW;
		itembags.x_col = 32;
		itembags.icon_type = Cell.MULTIICON;
		itembags.starts = new int[]{0, 1, 2, 3, 4, 5, 6};
		itembags.icons = new Icon[7][2];
		for (int i = 0; i < itembags.icons.length; i++) {
			itembags.icons[i][0] = new Icon(tex[SBI + i + 1], tex[SBF], 0).moveSel(-1, -1);
			itembags.icons[i][1] = new Icon(tex[SBIS + i + 1], tex[SBF], 0).moveSel(-1, -1);
		}

		arrows = new Cell();
		arrows.x = 8;
		arrows.y = 228;
		arrows.w = 24;
		arrows.h = 24;
		arrows.arangement = Cell.ROW;
		arrows.x_col = 40;
		arrows.icon_type = Cell.MULTIICON;
		arrows.starts = new int[]{0, 1};
		arrows.icons = new Icon[2][1];
		arrows.icons[0][0] = new Icon(tex[LA], 0, tex[LAA]);
		arrows.icons[1][0] = new Icon(tex[RA], 0, tex[RAA]);

		trashaccept = new Cell();
		trashaccept.x = 98;
		trashaccept.y = 228;
		trashaccept.w = 76;
		trashaccept.h = 24;
		trashaccept.addText(19, 4, 0, 0x11, Text.TEXT_LEFT);
		trashaccept.icon = new Icon(tex[TA], 0, tex[TAA]);

		trashback = new Cell();
		trashback.x = 178;
		trashback.y = 228;
		trashback.w = 76;
		trashback.h = 24;
		trashback.addText(19, 4, 0, 0x11, Text.TEXT_LEFT);
		trashback.icon = new Icon(tex[TD], 0, tex[TDA]);

		trasharrows = new Cell();
		trasharrows.x = 32;
		trasharrows.y = -13;
		trasharrows.w = 20;
		trasharrows.h = 20;
		trasharrows.arangement = Cell.GRID;
		trasharrows.x_col = 32;
		trasharrows.y_row = 50;
		trasharrows.columns = 3;
		trasharrows.icon_type = Cell.MULTIICON;
		trasharrows.starts = new int[]{0, 3};
		trasharrows.icons = new Icon[2][1];
		trasharrows.icons[0][0] = new Icon(tex[TAU], 0, tex[TAU2]);
		trasharrows.icons[1][0] = new Icon(tex[TAD], 0, tex[TAD2]);

		trashnumbers = new Cell();
		trashnumbers.x = 34;
		trashnumbers.y = 14;
		trashnumbers.arangement = Cell.ROW;
		trashnumbers.x_col = 32;
		trashnumbers.icon_type = Cell.TEXTONLY;
		trashnumbers.addText(1, 0, 0, 0x10, Text.TEXT_LEFT);

		trashbox = new Cell();
		trashbox.x = 94;
		trashbox.y = 144;
		trashbox.w = 162;
		trashbox.h = 44;
		trashbox.icon = new Icon(tex[TI], 0, 0);
		trashbox.setChildren(trasharrows, trashnumbers);

		backgrounds = new Cell();
		backgrounds.x = 0;
		backgrounds.y = 0;
		backgrounds.w = 256;
		backgrounds.h = 256;
		backgrounds.icon_type = Cell.MULTIICON;
		backgrounds.icons = new Icon[1][bags.length];
		backgrounds.starts = new int[]{0};
		for (int i = 0; i < bags.length; i++) {
			backgrounds.icons[0][i] = new Icon(tex[BG + i], 0, 0);
		}

		backgroundL = new Cell();
		backgroundL.x = 0;
		backgroundL.y = 0;
		backgroundL.w = 256;
		backgroundL.h = 256;
		backgroundL.icon = new Icon(tex[SBG], 0, 0);

		descBox = new Cell();
		descBox.x = 0;
		descBox.y = 192;
		descBox.w = 256;
		descBox.h = 64;
		descBox.addText(20, 8, 0, 0x11, Text.TEXT_LEFT, 3, 18);
		descBox.icon = new Icon(tex[DTF], 0, 0);

		itemicon = new Cell();
		itemicon.x = 4;
		itemicon.y = 6;
		itemicon.w = 24;
		itemicon.h = 24;
		itemicon.addText(26, -3, 0, 0x10, Text.TEXT_LEFT);

		items = new Cell();
		items.x = 2;
		items.y = 40;
		items.w = 124;
		items.h = 38;
		items.arangement = Cell.GRID;
		items.x_col = 128;
		items.y_row = 44;
		items.columns = 2;
		// TODO will get lost, save differently
		items.rows = 4;
		items.icon_type = Cell.MULTIICON;
		items.addText(84, 20, 0, 0x10, Text.TEXT_LEFT);
		items.starts = new int[]{0};
		items.icons = new Icon[1][1 + bags.length];
		items.icons[0][0] = new Icon(tex[IBG], tex[IBGS], 0);
		for (int i = 0; i < bags.length; i++) {
			items.icons[0][i + 1] = new Icon(tex[IIBG + i], tex[IBGS], 0);
		}
		items.setChildren(itemicon);

		selitem = new Cell();
		selitem.x = 64;
		selitem.y = 64;
		selitem.w = 124;
		selitem.h = 38;
		selitem.addText(84, 20, 0, 0x10, Text.TEXT_LEFT);
		selitem.icon = new Icon(tex[IBG2], 0, 0);
		selitem.setChildren(itemicon);

		page = new Cell();
		page.x = 98;
		page.y = 228;
		page.icon_type = Cell.TEXTONLY;
		page.addText(0, 0, 0, 0x11, Text.TEXT_SLASH);
	}

	/** Adds the item with the given id and amount to the bag. */
	public void addItem(int id, int amount) {
		bags[InfoLoader.getBagForItem(id)].addItem(id, amount);
	}

	/** Removes the given amount of the items with the given id from the bag. */
	public boolean removeItem(int id, int amount) {
		return bags[InfoLoader.getBagForItem(id)].removeItem(id, amount);
	}

	/** Returns whether or not their is a given amount of items with the given id in the bag. */
	public boolean containsItem(int id, int amount) {
		return bags[InfoLoader.getBagForItem(id)].hasItem(id, amount);
	}

	/** Updates the bag and processes events and key-presses. */
	@Override
	public void update(Game g) {
		BagList bag = bags[currentSubBag];
		switch (status) {
			case CLOSED:
				if (MessageHandler.hasMessage(MessageHandler.BAG_OPEN)) {
					clickClose = false;
					vfx.addShutter(Direction.NORTH, true);
					status = STARTUP;
				}
				// TODO check other for messages
				break;
			case STARTUP:
				if (vfx.shutterFinished()) {
					status = ITEMSELECTION;
				}
				break;
			case SHUTDOWN:
				if (vfx.shutterFinished()) {
					status = CLOSED;
					MessageHandler.add(MessageHandler.BAG_CLOSE, clickClose);
				}
				break;
			case ITEMSELECTION:
				if (back.animationRunning || items.animationRunning) {
					if (back.animationFinished()) {
						vfx.addShutter(Direction.NORTH, false);
						status = SHUTDOWN;
					} else if (items.animationFinished()) {
						selection = 1;
						status = ITEMOPTIONS;
					}
					break;
				}
				arrows.animationFinished();
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					arrows.stopAnimation();
					back.animate(selection == 5);
					break;
				}
				int columns = items.columns;
				int size = columns * items.rows;
				if (selection == 0) {
					int pos = bag.getPosition(size);
					int pages = 1 + (bag.getLength() - 1) / size;
					if (pages == 0) {
						pages++;
					}
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						if (bag.getLength() > bag.getSelectedPosition()) {
							arrows.stopAnimation();
							items.animate(bag.getPosition(size), true);
						}
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						if (pos + 1 == size) {
							selection = 5;
						} else if (pos + columns >= size) {
							selection = currentSubBag + 8;
						} else {
							bag.addPosition(columns);
						}
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						int add = 1;
						if ((pos + 1) % columns == 0) {
							add += size - columns;
							arrows.animate(1, false);
						}
						bag.addPosition(add);
						if (bag.getSelectedPosition() >= pages * size) {
							bag.addPosition(-pages * size);
						}
						break;
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						if (pos < columns) {
							selection = currentSubBag + 8;
						} else {
							bag.addPosition(-columns);
						}
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						int add = -1;
						if ((pos) % columns == 0) {
							add -= size - columns;
							arrows.animate(0, false);
						}
						bag.addPosition(add);
						if (bag.getSelectedPosition() < 0) {
							bag.addPosition(pages * size);
						}
						break;
					}
				} else if (selection == 5) {
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						arrows.stopAnimation();
						back.animate(true);
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						selection = currentSubBag + 8;
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						bag.setPosition(size, -1);
						selection = 0;
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
					}
				} else if (selection >= 8) {
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						currentSubBag = (byte) (selection - 8);
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						// TODO should work with more columns than 2
						int pos;
						if (selection >= 9 && selection <= bags.length / 2 + 8) {
							pos = 0;
						} else {
							pos = 1;
						}
						bag.setPosition(size, pos);
						selection = 0;
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						selection++;
						if (selection > 15) {
							selection = 8;
						}
						break;
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						if (selection >= 1 && selection <= bags.length / 2) {
							bag.setPosition(size, -1);
							selection = 0;
						} else {
							selection = 5;
						}
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						selection--;
						if (selection < 8) {
							selection = 15;
						}
						break;
					}
				}
				selectionClick(g);
				break;
			case ITEMOPTIONS:
				// animation: 0 nothing, 1-16 back, 17-32 accept,
				boolean usable,
						giveable,
						trashable,
						registable,
						movable,
						isRegistered;
				// TODO what about registering normal items and unusable base items?
				if (currentSubBag == 0) {
					usable = true;
					giveable = false;
					trashable = false;
					registable = true;
					movable = bag.getLength() > 1;
					isRegistered = FlagHandler.isRegistert(bag.getCurrent()[0]);
				} else {
					boolean[] b = InfoLoader.getItemOptions(bag.getCurrent()[0]);
					usable = b[0];
					giveable = b[1];
					trashable = b[2];
					registable = false;
					movable = bag.getLength() > 1;
					isRegistered = false;
				}
				if (back.animationFinished()) {
					selection = 0;
					status = ITEMSELECTION;
					break;
				} else if (options.animationFinished()) {
					int option = options.animationPos;
					if (option == 0) {
						// TODO sent use item message instead, so it can be intercepted
						MessageHandler.add(MessageHandler.EVENT, Interpreter.getNewInterpreter(EventMethodLibrary.callScript("bag/item"), EventMethodLibrary.current(), null));
					} else if (option == 1) {
						if (trashable) {
							if (bag.getCurrent()[1] == 1) {
								// TODO ask question if sure directly
							} else {
								selection = 0;
								status = DELETEITEM;
							}
						} else if (registable) {
							if (isRegistered) {
								FlagHandler.unregister(bag.getCurrent()[0]);
							} else if (FlagHandler.hasRegisterSpace()) {
								FlagHandler.register(bag.getCurrent()[0]);
							} else {
								MessageHandler.add(MessageHandler.EVENT, Interpreter.getNewInterpreter(EventMethodLibrary.callScript("bag/no_space"), EventMethodLibrary.current(), null));
							}
						}
					} else if (option == 2) {
						vfx.addShutter(Direction.NORTH, false);
						selection = 0;
						removeItem(bag.getCurrent()[0], 1);
						//MessageHandler.add("Give item", bag.getCurrent()[0]);
						status = SHUTDOWN;
					} else if (option == 3) {
						moveTo = bag.getSelectedPosition();
						selection = 0;
						status = MOVEITEM;
					}
					break;
				} else if (back.animationRunning || options.animationRunning) {
					break;
				}
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					back.animate(selection == 5);
					break;
				} else if (g.wasPressed(Button.A)) {
					g.process(Button.A);
					if (selection == 1) {
						if (usable) {
							options.animate(0, true);
						}
					} else if (selection == 2) {
						if (trashable || registable) {
							options.animate(1, true);
						}
					} else if (selection == 3) {
						if (giveable) {
							options.animate(2, true);
						}
					} else if (selection == 4) {
						if (movable) {
							options.animate(3, true);
						}
					} else if (selection == 5) {
						back.animate(true);
					}
					break;
				} else if (g.wasPressed(Button.DOWN)) {
					g.process(Button.DOWN);
					if (selection == 1 || selection == 2) {
						selection += 2;
					}
					break;
				} else if (g.wasPressed(Button.RIGHT)) {
					g.process(Button.RIGHT);
					if (selection == 1 || selection == 3 || selection == 4) {
						selection++;
					}
					break;
				} else if (g.wasPressed(Button.UP)) {
					g.process(Button.UP);
					if (selection == 3 || selection == 4) {
						selection -= 2;
					}
					break;
				} else if (g.wasPressed(Button.LEFT)) {
					g.process(Button.LEFT);
					if (selection == 2 || selection == 4 || selection == 5) {
						selection--;
					}
					break;
				}
				optionClick(g);
				break;
			case MOVEITEM:
				// 0 nothing, 1-16 back, 17-32 accept, 33-48 right, 49-64 left
				if (items.animationRunning || back.animationRunning) {
					if (back.animationFinished()) {
						moveTo = 0;
						status = ITEMSELECTION;
					} else if (items.animationFinished()) {
						bag.moveItem(bag.getSelectedPosition(), moveTo);
						bag.addPosition(-bag.getSelectedPosition() + moveTo);
						moveTo = 0;
						selection = 0;
						status = ITEMSELECTION;
					}
					break;
				}
				arrows.animationFinished();
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					arrows.stopAnimation();
					back.animate(selection == 5);
					break;
				}
				columns = items.columns;
				size = columns * items.rows;
				int pos = moveTo % size;
				if (selection == 0) {
					int pages = 1;
					pages += bag.getLength() / size;
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						if (bag.getLength() > moveTo) {
							arrows.stopAnimation();
							items.animate(bag.getPosition(size), true);
						}
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						if (pos + 1 == size) {
							selection = 5;
						} else if (pos + columns >= size) {
							moveTo += -size + columns;
						} else {
							moveTo += columns;
						}
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						int add = 1;
						if ((pos + 1) % columns == 0) {
							add += size - columns;
							arrows.animate(1, false);
						}
						moveTo += add;
						if (moveTo >= pages * size) {
							moveTo -= pages * size;
						}
						break;
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						if (pos + 1 == columns) {
							selection = 5;
						} else if (pos - columns < 0) {
							moveTo += size - columns;
						} else {
							moveTo -= columns;
						}
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						int add = -1;
						if (pos % columns == 0) {
							add -= size - columns;
							arrows.animate(0, false);
						}
						moveTo += add;
						if (moveTo < 0) {
							moveTo += pages * size;
						}
						break;
					}
				} else if (selection == 5) {
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						arrows.stopAnimation();
						back.animate(true);
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						moveTo += -pos + columns - 1;
						selection = 0;
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						moveTo += -pos + size - 1;
						selection = 0;
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
					}
				}
				moveClick(g);
				break;
			case DELETEITEM:
				if (trashaccept.animationRunning || trashback.animationRunning) {
					if (trashaccept.animationFinished()) {
						// TODO ask question if sure
						bag.removeItem(bag.getCurrent()[0], deletionAmount);

						int sizes = items.columns * items.rows;
						int pages = 1 + (bag.getLength() - 1) / sizes;
						if (bag.getSelectedPosition() >= pages * sizes) {
							bag.addPosition(-sizes);
						}
						deletionAmount = 1;
						selection = 0;
						status = ITEMSELECTION;
					} else if (trashback.animationFinished()) {
						deletionAmount = 1;
						selection = 0;
						status = ITEMSELECTION;
					}
					break;
				}
				trasharrows.animationFinished();
				int maxAmount = bag.getCurrent()[1];
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					trasharrows.stopAnimation();
					trashback.animate(1, false);
					break;
				} else if (g.wasPressed(Button.A)) {
					g.process(Button.A);
					trasharrows.stopAnimation();
					trashaccept.animate(0, false);
					break;
				} else if (g.wasPressed(Button.LEFT)) {
					g.process(Button.LEFT);
					trasharrows.animate(1, false);
					deletionAmount += 10;
					if (deletionAmount > maxAmount) {
						deletionAmount = maxAmount;
					}
					break;
				} else if (g.wasPressed(Button.RIGHT)) {
					g.process(Button.RIGHT);
					trasharrows.animate(4, false);
					deletionAmount -= 10;
					if (deletionAmount <= 0) {
						deletionAmount = 1;
					}
					break;
				} else if (g.wasPressed(Button.UP)) {
					g.process(Button.UP);
					trasharrows.animate(2, false);
					deletionAmount++;
					if (deletionAmount > maxAmount) {
						deletionAmount = 1;
					}
					break;
				} else if (g.wasPressed(Button.DOWN)) {
					g.process(Button.DOWN);
					trasharrows.animate(5, false);
					deletionAmount--;
					if (deletionAmount <= 0) {
						deletionAmount = maxAmount;
					}
					break;
				}
				deletionClick(g);
				break;
		}
	}

	/** Processes clicking while deleting items. */
	private void deletionClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		g.processMouse();
		int x = g.getMouseX();
		int y = g.getMouseY();

		int maxAmount = bags[currentSubBag].getCurrent()[1];
		Cell tra = trashbox.getChild(0);
		int sel = tra.clickMulti(x, y, 6);
		if (sel == 0) {
			deletionAmount += 100;
			if (deletionAmount > maxAmount) {
				deletionAmount = maxAmount;
			}
		} else if (sel == 1) {
			deletionAmount += 10;
			if (deletionAmount > maxAmount) {
				deletionAmount = maxAmount;
			}
		} else if (sel == 2) {
			deletionAmount++;
			if (deletionAmount > maxAmount) {
				deletionAmount = 1;
			}
		} else if (sel == 3) {
			deletionAmount -= 100;
			if (deletionAmount <= 0) {
				deletionAmount = 1;
			}
		} else if (sel == 4) {
			deletionAmount -= 10;
			if (deletionAmount <= 0) {
				deletionAmount = 1;
			}
		} else if (sel == 5) {
			deletionAmount--;
			if (deletionAmount <= 0) {
				deletionAmount = maxAmount;
			}
		}
		if (sel != -1) {
			trasharrows.animate(sel, false);
			return;
		}

		if (trashaccept.click(x, y)) {
			trasharrows.stopAnimation();
			trashaccept.animate(false);
			return;
		}
		if (trashback.click(x, y)) {
			trasharrows.stopAnimation();
			trashback.animate(false);
		}
	}

	/** Processes clicking while moving items. */
	private void moveClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		g.processMouse();
		int x = g.getMouseX();
		int y = g.getMouseY();

		int itemPerPage = items.columns * items.rows;
		int sel = items.clickMulti(x, y, itemPerPage);
		if (sel != -1) {
			moveTo += -moveTo % itemPerPage + sel;
			if (bags[currentSubBag].getLength() > moveTo) {
				arrows.stopAnimation();
				items.animate(sel, true);
			}
			return;
		}

		BagList bag = bags[currentSubBag];
		int maxPage = (bag.getLength() - 1) / itemPerPage + 1;
		if (maxPage == 0) {
			maxPage++;
		}
		sel = arrows.clickMulti(x, y, 2);
		if (sel == 1) {
			arrows.animate(1, false);
			moveTo += itemPerPage;
			if (moveTo >= itemPerPage * maxPage) {
				moveTo -= itemPerPage * maxPage;
			}
			return;
		}
		if (sel == 0) {
			arrows.animate(0, false);
			moveTo -= itemPerPage;
			if (moveTo < 0) {
				moveTo += itemPerPage * maxPage;
			}
			return;
		}

		if (back.click(x, y)) {
			arrows.stopAnimation();
			back.animate(true);
			selection = 5;
		}
	}

	/** Processes clicking while viewing the options of an item. */
	private void optionClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		g.processMouse();
		int x = g.getMouseX();
		int y = g.getMouseY();

		boolean usable, giveable, trashable, registable, movable;
		if (currentSubBag == 0) {
			usable = true;
			giveable = false;
			trashable = false;
			registable = true;
			movable = true;
		} else {
			boolean[] b = InfoLoader.getItemOptions(bags[currentSubBag].getCurrent()[0]);
			usable = b[0];
			giveable = b[1];
			trashable = b[2];
			registable = false;
			movable = b[3];
		}
		int option = options.clickMulti(x, y, 4);
		if (usable && option == 0) {
			selection = 1;
			options.animate(0, true);
			return;
		}
		if ((trashable || registable) && option == 1) {
			selection = 2;
			options.animate(1, true);
			return;
		}
		if (giveable && option == 2) {
			selection = 3;
			options.animate(2, true);
			return;
		}
		if (movable && option == 3) {
			selection = 4;
			options.animate(3, true);
			return;
		}

		if (back.click(x, y)) {
			back.animate(true);
			selection = 5;
		}
	}

	/** Processes clicking while selecting an item. */
	private void selectionClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		g.processMouse();
		int x = g.getMouseX();
		int y = g.getMouseY();

		int itemPerPage = items.columns * items.rows;
		BagList bag = bags[currentSubBag];
		if (basebag.click(x, y)) {
			if (selection >= 8) {
				selection = 8;
			}
			bags[0].setPosition(itemPerPage, bag.getPosition(itemPerPage));
			currentSubBag = 0;
			return;
		}
		int sel = itembags.clickMulti(x, y, 7);
		if (sel != -1) {
			if (selection >= 8) {
				selection = sel + 8 + 1;
			}
			bags[sel + 1].setPosition(itemPerPage, bag.getPosition(itemPerPage));
			currentSubBag = sel + 1;
			return;
		}
		int[] ids = bag.getNumbers(itemPerPage, bag.getSelectedPosition())[0];
		sel = items.clickMulti(x, y, itemPerPage);
		if (sel != -1) {
			selection = 0;
			bag.setPosition(itemPerPage, sel);
			if (ids[sel] != 0) {
				arrows.stopAnimation();
				items.animate(sel, true);
			}
			return;
		}

		int maxPage = (bag.getLength() - 1) / itemPerPage + 1;
		if (maxPage == 0) {
			maxPage++;
		}
		sel = arrows.clickMulti(x, y, 2);
		if (sel == 1) {
			arrows.animate(1, false);
			bag.addPosition(itemPerPage);
			if (bag.getSelectedPosition() >= itemPerPage * maxPage) {
				bag.addPosition(-itemPerPage * maxPage);
			}
			return;
		}
		if (sel == 0) {
			arrows.animate(0, false);
			bag.addPosition(-itemPerPage);
			if (bag.getSelectedPosition() < 0) {
				bag.addPosition(itemPerPage * maxPage);
			}
			return;
		}

		if (back.click(x, y)) {
			arrows.stopAnimation();
			back.animate(true);
			selection = 5;
			clickClose = true;
		}
	}

	/** Draws the left part of the bag screen. */
	@Override
	public void drawL() {
		if (status == CLOSED) {
			return;
		}
		drawScreenBackground();
		drawSelectedBagArea();
		drawDescription();
	}

	/** Draws the right part of the bag screen. */
	@Override
	public void drawR() {
		switch (status) {
			case STARTUP:
				drawBagBackground();
				drawCategories();
				drawSelection();
				drawNavigation();
				drawBackButton();
				break;
			case SHUTDOWN:
				drawBagBackground();
				drawCategories();
				drawSelection();
				drawNavigation();
				drawBackButton();
				break;
			case ITEMSELECTION:
				drawBagBackground();
				drawCategories();
				drawSelection();
				drawNavigation();
				drawBackButton();
				break;
			case ITEMOPTIONS:
				drawBagBackground();
				drawSelectedName();
				drawSingleItem();
				drawOptions();
				drawBackButton();
				break;
			case MOVEITEM:
				drawBagBackground();
				drawMoveName();
				drawMoveSelection();
				drawNavigation();
				drawBackButton();
				break;
			case DELETEITEM:
				drawBagBackground();
				drawSelectedName();
				drawSingleItem();
				drawAmountInput();
				drawAmountAcceptDeny();
				break;
		}
	}

	private void drawSelectedName() {
		tb.draw(ConV.sprintf(Localisation.getBagSelection(), Localisation.getItemName(bags[currentSubBag].getCurrent()[0])));
	}

	private void drawMoveName() {
		tb.draw(ConV.sprintf(Localisation.getBagMove(), Localisation.getItemName(bags[currentSubBag].getCurrent()[0])));
	}

	private void drawAmountAcceptDeny() {
		char[][] trashText = Localisation.getTrashButtons();
		trashaccept.draw(trashText[0]);
		trashback.draw(trashText[1]);
	}

	/** Draws a single item icon to the middle of the screen. */
	private void drawSingleItem() {
		int[] tmp = bags[currentSubBag].getCurrent();
		int id = tmp[0];
		char[] text = null;
		if (currentSubBag != 0) {
			text = ConV.sprintf(Localisation.getBagAmount(), tmp[1]);
		}
		selitem.draw(text);
		Cell ic = selitem.getChild(0);
		ic.draw(new Icon(GraphicHandler.getItemIcon(id), 0, 0), Localisation.getItemName(id), false);
	}

	/** Draws the item seletion while moving an item. */
	private void drawMoveSelection() {
		int itemsPerPage = items.columns * items.rows;
		int[] ids;
		int[] amounts;
		int current;
		int[][] tmp = bags[currentSubBag].getNumbers(itemsPerPage, moveTo);
		ids = tmp[0];
		amounts = tmp[1];
		current = moveTo % (itemsPerPage);
		char[] text = Localisation.getBagAmount();
		for (int i = 0; i < itemsPerPage; i++) {
			if (ids[i] == 0) {
				items.drawMulti(null, i, currentSubBag + 1, i == current && selection == 0);
				continue;
			}
			int amount = amounts[i];
			items.drawMulti(ConV.sprintf(text, amount), i, i == current && selection == 0);
			Cell ic = items.getChildMulti(0, i);
			ic.draw(new Icon(GraphicHandler.getItemIcon(ids[i]), 0, 0), Localisation.getItemName(ids[i]), false);
		}
	}

	/** Draws the description of the current selected item / sub-bag. */
	private void drawDescription() {
		char[] descText = null;
		if (selection >= 8) {
			descText = Localisation.getBagSubDesc(selection - 8);
		} else {
			int id = bags[currentSubBag].getCurrent()[0];
			if (id != 0) {
				descText = Localisation.getBagItemDesc(id);
			}
		}
		descBox.draw(descText);
	}

	/** Draws the diagram that shows the location of the current sub-bag. */
	private void drawSelectedBagArea() {
		// TODO Auto-generated method stub

	}

	/** Draws the background of the left screen. */
	private void drawScreenBackground() {
		backgroundL.draw();
	}

	/** Draws the option field for the current selected item. */
	private void drawOptions() {
		boolean usable, giveable, trashable, registable, movable, isRegistert;
		if (currentSubBag == 0) {
			usable = true;
			giveable = false;
			trashable = false;
			registable = true;
			movable = true;
			isRegistert = FlagHandler.isRegistert(bags[0].getCurrent()[0]);
		} else {
			boolean[] b = InfoLoader.getItemOptions(bags[currentSubBag].getCurrent()[0]);
			usable = b[0];
			giveable = b[1];
			trashable = b[2];
			registable = false;
			movable = b[3];
			isRegistert = false;
		}
		char[][] text = Localisation.getBagOptions();
		if (usable) {
			options.drawMulti(text[0], 0, selection == 1);
		} else {
			options.drawMulti(null, 0, currentSubBag + 1, selection == 1);
		}
		if (trashable) {
			options.drawMulti(text[1], 1, selection == 2);
		} else if (registable && isRegistert) {
			options.drawMulti(text[2], 1, selection == 2);
		} else if (registable) {
			options.drawMulti(text[3], 1, selection == 2);
		} else {
			options.drawMulti(null, 1, currentSubBag + 1, selection == 2);
		}
		if (giveable) {
			options.drawMulti(text[4], 2, selection == 3);
		} else {
			options.drawMulti(null, 2, currentSubBag + 1, selection == 3);
		}
		if (movable) {
			options.drawMulti(text[5], 3, selection == 4);
		} else {
			options.drawMulti(null, 3, currentSubBag + 1, selection == 4);
		}
	}

	/** Draws the input box for the amount of items that should be trashed. */
	private void drawAmountInput() {
		trashbox.draw();
		Cell tra = trashbox.getChild(0);
		tra.drawMulti(6, -1);

		Cell trn = trashbox.getChild(1);
		char[] cn = new char[1];
		cn[0] = (char) (deletionAmount / 100 % 10 + '0');
		trn.drawMulti(cn, 0);
		cn[0] = (char) (deletionAmount / 10 % 10 + '0');
		trn.drawMulti(cn, 1);
		cn[0] = (char) (deletionAmount % 10 + '0');
		trn.drawMulti(cn, 2);
	}

	/** Draws the back button. */
	private void drawBackButton() {
		back.draw(Localisation.getBagCancel(), selection == 5);
	}

	/** Draws the items contained in the current sub-bag. */
	private void drawSelection() {
		int itemsPerPage = items.columns * items.rows;
		int[][] tmp = bags[currentSubBag].getNumbers(itemsPerPage, bags[currentSubBag].getSelectedPosition());
		int[] ids = tmp[0];
		int[] amounts = tmp[1];
		int current = bags[currentSubBag].getPosition(itemsPerPage);
		char[] textF = Localisation.getBagAmount();
		for (int i = 0; i < itemsPerPage; i++) {
			if (ids[i] == 0) {
				items.drawMulti(null, i, currentSubBag + 1, i == current && selection == 0);
				continue;
			}
			char[] text = null;
			if (currentSubBag != 0) {
				text = ConV.sprintf(textF, amounts[i]);
			}
			items.drawMulti(text, i, i == current && selection == 0);

			Cell ic = items.getChildMulti(0, i);
			ic.draw(new Icon(GraphicHandler.getItemIcon(ids[i]), 0, 0), Localisation.getItemName(ids[i]), false);
		}
	}

	/** Draws the navigation arrows and the page numbers. */
	private void drawNavigation() {
		arrows.drawMulti(2, -1);

		int itemPerPage = items.columns * items.rows;
		int length = bags[currentSubBag].getLength();
		int position = bags[currentSubBag].getSelectedPosition();
		int maxPage = (length - 1) / itemPerPage + 1;
		if (maxPage == 0) {
			maxPage = 1;
		}
		int currentPage = position / itemPerPage + 1;

		assert (maxPage <= 999);
		//TODO string formatting
		char[] s = new char[7];
		s[0] = currentPage >= 100 ? (char) ('0' + currentPage / 100) : ' ';
		s[1] = currentPage >= 10 ? (char) ('0' + (currentPage / 10 % 10)) : ' ';
		s[2] = (char) ('0' + currentPage % 10);
		s[3] = '/';
		s[4] = maxPage >= 100 ? (char) ('0' + maxPage / 100) : ' ';
		s[5] = maxPage >= 10 ? (char) ('0' + (maxPage / 10 % 10)) : ' ';
		s[6] = (char) ('0' + maxPage % 10);
		page.draw(s);
	}

	/** Draws all sub-bag icons. */
	private void drawCategories() {
		basebag.draw(currentSubBag == 0 ? 1 : 0, selection == 8);
		for (int i = 1; i < bags.length; i++) {
			itembags.drawMulti(null, i - 1, currentSubBag == i ? 1 : 0, selection == i + 8);
		}
	}

	/** Draws the background of the right screen. */
	private void drawBagBackground() {
		backgrounds.draw(currentSubBag, false);
	}

	/** Returns whether the bag has the input focus. */
	@Override
	public boolean hasFocus() {
		return status != CLOSED;
	}

	/** Returns whether the bag draws to the left screen. */
	@Override
	public boolean occupiesLeft() {
		return status != CLOSED;
	}

	@Override
	public boolean occupiesRight() {
		return status != CLOSED;
	}

	public void initSelection() {
		selection = 0;
	}

	public void drawRSelection() {
		drawBagBackground();
		drawCategories();
		drawSelection();
		drawNavigation();
		drawBackButton();
	}

	public void drawLSelection() {
		drawScreenBackground();
		drawSelectedBagArea();
		drawDescription();
	}

	public boolean updateSelection(Game g) {
		BagList bag = bags[currentSubBag];
		if (back.animationRunning || items.animationRunning) {
			if (back.animationFinished()) {
				SystemControll.setFlag(SystemControll.QuestionAnswer, false);
				return true;
			} else if (items.animationFinished()) {
				int item = bag.getCurrent()[0];
				// TODO not all item selecctions are about holding
				if (InfoLoader.getItemOptions(item)[1] == false) {
					TextBox tbx = new TextBox("The \\s0000 can't be held.".toCharArray(), false, 27, 1, new char[][]{Localisation.getItemName(item)});
					tbx.setPosition(0, 0);
					MenuObjectHandler.current.setText(tbx);
					return false;
				}
				SystemControll.setFlag(SystemControll.QuestionAnswer, true);
				SystemControll.setVariable(SystemControll.MCQuestionAnswer, bag.getCurrent()[0]);
				return true;
			}
			return false;
		}
		arrows.animationFinished();
		if (g.wasPressed(Button.B)) {
			g.process(Button.B);
			arrows.stopAnimation();
			back.animate(selection == 5);
			return false;
		}
		int columns = items.columns;
		int size = columns * items.rows;
		if (selection == 0) {
			int pos = bag.getPosition(size);
			int pages = 1 + (bag.getLength() - 1) / size;
			if (pages == 0) {
				pages++;
			}
			if (g.wasPressed(Button.A)) {
				g.process(Button.A);
				if (bag.getLength() > bag.getSelectedPosition()) {
					arrows.stopAnimation();
					items.animate(bag.getPosition(size), true);
				}
				return false;
			} else if (g.wasPressed(Button.DOWN)) {
				g.process(Button.DOWN);
				if (pos + 1 == size) {
					selection = 5;
				} else if (pos + columns >= size) {
					selection = currentSubBag + 8;
				} else {
					bag.addPosition(columns);
				}
				return false;
			} else if (g.wasPressed(Button.RIGHT)) {
				g.process(Button.RIGHT);
				int add = 1;
				if ((pos + 1) % columns == 0) {
					add += size - columns;
					arrows.animate(1, false);
				}
				bag.addPosition(add);
				if (bag.getSelectedPosition() >= pages * size) {
					bag.addPosition(-pages * size);
				}
				return false;
			} else if (g.wasPressed(Button.UP)) {
				g.process(Button.UP);
				if (pos < columns) {
					selection = currentSubBag + 8;
				} else {
					bag.addPosition(-columns);
				}
				return false;
			} else if (g.wasPressed(Button.LEFT)) {
				g.process(Button.LEFT);
				int add = -1;
				if ((pos) % columns == 0) {
					add -= size - columns;
					arrows.animate(0, false);
				}
				bag.addPosition(add);
				if (bag.getSelectedPosition() < 0) {
					bag.addPosition(pages * size);
				}
				return false;
			}
		} else if (selection == 5) {
			if (g.wasPressed(Button.A)) {
				g.process(Button.A);
				arrows.stopAnimation();
				back.animate(true);
				return false;
			} else if (g.wasPressed(Button.DOWN)) {
				g.process(Button.DOWN);
				selection = currentSubBag + 8;
				return false;
			} else if (g.wasPressed(Button.RIGHT)) {
				g.process(Button.RIGHT);
				return false;
			} else if (g.wasPressed(Button.UP)) {
				g.process(Button.UP);
				bag.setPosition(size, -1);
				selection = 0;
				return false;
			} else if (g.wasPressed(Button.LEFT)) {
				g.process(Button.LEFT);
				return false;
			}
		} else if (selection >= 8) {
			if (g.wasPressed(Button.A)) {
				g.process(Button.A);
				currentSubBag = (byte) (selection - 8);
				return false;
			} else if (g.wasPressed(Button.DOWN)) {
				g.process(Button.DOWN);
				// TODO should work with more columns than 2
				int pos;
				if (selection >= 9 && selection <= bags.length / 2 + 8) {
					pos = 0;
				} else {
					pos = 1;
				}
				bag.setPosition(size, pos);
				selection = 0;
				return false;
			} else if (g.wasPressed(Button.RIGHT)) {
				g.process(Button.RIGHT);
				selection++;
				if (selection > 15) {
					selection = 8;
				}
				return false;
			} else if (g.wasPressed(Button.UP)) {
				g.process(Button.UP);
				if (selection >= 1 && selection <= bags.length / 2) {
					bag.setPosition(size, -1);
					selection = 0;
				} else {
					selection = 5;
				}
				return false;
			} else if (g.wasPressed(Button.LEFT)) {
				g.process(Button.LEFT);
				selection--;
				if (selection < 8) {
					selection = 15;
				}
				return false;
			}
		}
		selectionClick(g);
		return false;
	}

}
