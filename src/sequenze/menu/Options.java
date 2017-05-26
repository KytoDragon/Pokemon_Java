package sequenze.menu;

import sequenze.Button;
import sequenze.Direction;
import sequenze.Game;
import sequenze.GraphicHandler;
import sequenze.Localisation;
import sequenze.MessageHandler;
import sequenze.SystemControll;

import static sequenze.InstancePackage.*;

import sequenze.Text;
import sequenze.overlay.TextBox;

public class Options extends GuiItem {

	/** Index of the currently selected field. (0-7 for options, 8 for accept, 9 for back) */
	private int selection = 0;
	/** Array of texture indices. */
	public int[] tex;
	public final static byte LBG = 0; // left screen background
	public final static byte RBG = 1; // right screen background
	public final static byte SB = 2; // small box
	public final static byte SBA = 3; // small box activated
	public final static byte LB = 4; // large box
	public final static byte LBA = 5; // large box activated
	public final static byte TF = 6; // text field
	public final static byte LA = 7; // left arrow
	public final static byte LAA = 8; // left arrow activated
	public final static byte RA = 9; // right arrow
	public final static byte RAA = 10; // right arrow activated
	public final static byte SS = 11; // selection box
	public final static byte BB = 12; // back button
	public final static byte BBA = 13; // back button activated
	public final static byte BBS = 14; // back button selection
	private byte status = CLOSED;
	final static byte STARTUP = 0; // opening the options with a fade in
	final static byte SHUTDOWN = 1; // closing the options with a fade out
	final static byte ACTIVE = 2; // selecting an option
	final static byte CLOSED = 8; // closed
	private static int[] options;
	private static int[] max;
	private TextBox textbox;
	private boolean clickClose;

	private Cell background;
	private Cell backgroundL;
	private Cell accept;
	private Cell back;
	private Cell items;
	private Cell buttonsSmall;
	private Cell buttonsSmallA;
	private Cell buttonsBig;
	private Cell buttonsBigA;
	private Cell arrows;
	private Cell list;

	public Options() {
		options = new int[8];
		max = new int[8];
		updateOptions();
		tex = GraphicHandler.giveTextures("Options");
		textbox = new TextBox(Localisation.getOptionsFrameLabel(options[5]), true);
		textbox.intoBackground();
		setupCells();
	}

	public void setupCells() {
		background = new Cell();
		background.x = 0;
		background.y = 0;
		background.w = 256;
		background.h = 256;
		background.addText(10, 5, 0, 0x5, Text.TEXT_LEFT);
		background.icon = new Icon(tex[RBG], 0, 0);

		backgroundL = new Cell();
		backgroundL.x = 0;
		backgroundL.y = 0;
		backgroundL.w = 256;
		backgroundL.h = 256;
		backgroundL.icon = new Icon(tex[LBG], 0, 0);

		accept = new Cell();
		accept.x = 117;
		accept.y = 231;
		accept.w = 62;
		accept.h = 22;
		accept.addText(31, 3, 0, 0x5, Text.TEXT_MIDDLE);
		accept.icon = new Icon(tex[BB], tex[BBS], tex[BBA]).moveSel(-1, -1);

		back = new Cell();
		back.x = 189;
		back.y = 231;
		back.w = 62;
		back.h = 22;
		back.addText(31, 3, 0, 0x5, Text.TEXT_MIDDLE);
		back.icon = new Icon(tex[BB], tex[BBS], tex[BBA]).moveSel(-1, -1);

		buttonsSmall = new Cell();
		buttonsSmall.x = 106;
		buttonsSmall.y = 5;
		buttonsSmall.w = 46;
		buttonsSmall.h = 20;
		buttonsSmall.arangement = Cell.ROW;
		buttonsSmall.x_col = 48;
		buttonsSmall.addText(26, 3, 0, 0x5, Text.TEXT_MIDDLE);
		buttonsSmall.icon = new Icon(tex[SB], 0, 0);

		buttonsSmallA = new Cell();
		buttonsSmallA.x = 106;
		buttonsSmallA.y = 5;
		buttonsSmallA.w = 46;
		buttonsSmallA.h = 20;
		buttonsSmallA.arangement = Cell.ROW;
		buttonsSmallA.x_col = 48;
		buttonsSmallA.addText(26, 3, 0, 0xA, Text.TEXT_MIDDLE);
		buttonsSmallA.icon = new Icon(tex[SBA], 0, 0);

		buttonsBig = new Cell();
		buttonsBig.x = 106;
		buttonsBig.y = 5;
		buttonsBig.w = 62;
		buttonsBig.h = 20;
		buttonsBig.arangement = Cell.ROW;
		buttonsBig.x_col = 80;
		buttonsBig.addText(34, 3, 0, 0x5, Text.TEXT_MIDDLE);
		buttonsBig.icon = new Icon(tex[LB], 0, 0);

		buttonsBigA = new Cell();
		buttonsBigA.x = 106;
		buttonsBigA.y = 5;
		buttonsBigA.w = 62;
		buttonsBigA.h = 20;
		buttonsBigA.arangement = Cell.ROW;
		buttonsBigA.x_col = 80;
		buttonsBigA.addText(34, 3, 0, 0xA, Text.TEXT_MIDDLE);
		buttonsBigA.icon = new Icon(tex[LBA], 0, 0);

		list = new Cell();
		list.x = 29;
		list.y = 0;
		list.w = 70;
		list.h = 20;
		list.addText(35, 3, 0, 0xA, Text.TEXT_MIDDLE);
		list.icon = new Icon(tex[TF], 0, 0);

		arrows = new Cell();
		arrows.x = 116;
		arrows.y = 5;
		arrows.w = 29;
		arrows.h = 20;
		arrows.arangement = Cell.ROW;
		arrows.x_col = 99;
		arrows.icon_type = Cell.MULTIICON;
		arrows.starts = new int[]{0, 1};
		arrows.icons = new Icon[2][1];
		arrows.icons[0][0] = new Icon(tex[LA], 0, tex[LAA]);
		arrows.icons[1][0] = new Icon(tex[RA], 0, tex[RAA]);
		arrows.setChildren(list);

		items = new Cell();
		items.x = 0;
		items.y = 21;
		items.w = 256;
		items.h = 30;
		items.arangement = Cell.COLLUMN;
		items.y_row = 24;
		items.addText(12, 8, 0, 0x5, Text.TEXT_LEFT);
		items.icon = new Icon(0, tex[SS], 0);
		items.setChildren(buttonsSmall, buttonsSmallA, buttonsBig, buttonsBigA, arrows);
	}

	private void updateOptions() {
		options[0] = 2 - SystemControll.getVariable(SystemControll.DefaultTextSpeed);
		max[0] = 2; // TODO arbitrary maximum
		options[1] = SystemControll.getFlag(SystemControll.BattleScene) ? 1 : 0;
		max[1] = 1;
		options[2] = SystemControll.getFlag(SystemControll.BattleStyle) ? 1 : 0;
		max[2] = 1;
		options[3] = SystemControll.getFlag(SystemControll.StereoSound) ? 1 : 0;
		max[3] = 1;
		options[4] = SystemControll.getFlag(SystemControll.LButtonIsA) ? 1 : 0;
		max[4] = 1;
		options[5] = SystemControll.getVariable(SystemControll.DefaultTextboxStyle);
		max[5] = 19; // TODO arbitrary maximum
		options[6] = 0;
		max[6] = 0;
		options[7] = 0;
		max[7] = 0;
	}

	private void saveSettings() {
		SystemControll.setVariable(SystemControll.DefaultTextSpeed, 2 - options[0]);
		SystemControll.setFlag(SystemControll.BattleScene, options[1] != 0);
		SystemControll.setFlag(SystemControll.BattleStyle, options[2] != 0);
		SystemControll.setFlag(SystemControll.StereoSound, options[3] != 0);
		SystemControll.setFlag(SystemControll.LButtonIsA, options[4] != 0);
		SystemControll.setVariable(SystemControll.DefaultTextboxStyle, options[5]);
	}

	/** Updates the options and processes events and key-presses. */
	@Override
	public void update(Game g) {
		switch (status) {
			case CLOSED:
				if (MessageHandler.hasMessage(MessageHandler.OPTIONS_OPEN)) {
					clickClose = false;
					vfx.addShutter(Direction.NORTH, true);
					selection = 0;
					status = STARTUP;
				}
				// TODO check for other messages
				break;
			case STARTUP:
				if (vfx.shutterFinished()) {
					status = ACTIVE;
				}
				break;
			case SHUTDOWN:
				if (vfx.shutterFinished()) {
					status = CLOSED;
					MessageHandler.add(MessageHandler.OPTIONS_CLOSE, clickClose);
				}
				break;
			case ACTIVE:
				if (accept.animationRunning || back.animationRunning) {
					if (accept.animationFinished()) {
						saveSettings();
						vfx.addShutter(Direction.NORTH, false);
						status = SHUTDOWN;
					} else if (back.animationFinished()) {
						vfx.addShutter(Direction.NORTH, false);
						status = SHUTDOWN;
					}
					break;
				}
				arrows.animationFinished();
				if (g.wasPressed(Button.B)) {
					g.process(Button.B);
					back.animate(selection == 9);
					break;
				}
				if (selection < 8) {
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						if (selection == 7) {
							selection = 9;
						} else {
							selection++;
						}
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						if (options[selection] == max[selection]) {
							options[selection] = 0;
						} else {
							options[selection]++;
						}
						if (selection == 0) {
							textbox.setTextSpeed(2 - options[0]);
							textbox.setText(Localisation.getOptionsFrameLabel(options[5]));
						} else if (selection == 5) {
							arrows.animate(1, false);
							textbox.setBoxStyle(options[5]);
							textbox.setText(Localisation.getOptionsFrameLabel(options[5]));
							textbox.fastForward();
						}
						break;
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						if (selection == 0) {
							selection = 9;
						} else {
							selection--;
						}
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						if (options[selection] == 0) {
							options[selection] = max[selection];
						} else {
							options[selection]--;
						}
						if (selection == 0) {
							textbox.setTextSpeed(2 - options[0]);
							textbox.setText(Localisation.getOptionsFrameLabel(options[5]));
						} else if (selection == 5) {
							arrows.animate(0, false);
							textbox.setBoxStyle(options[5]);
							textbox.setText(Localisation.getOptionsFrameLabel(options[5]));
							textbox.fastForward();
						}
						break;
					}
				} else if (selection == 8) {
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						accept.animate(true);
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						selection = 0;
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						selection = 9;
						break;
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						selection = 7;
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						break;
					}
				} else if (selection == 9) {
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						back.animate(true);
						break;
					} else if (g.wasPressed(Button.DOWN)) {
						g.process(Button.DOWN);
						selection = 0;
						break;
					} else if (g.wasPressed(Button.RIGHT)) {
						g.process(Button.RIGHT);
						break;
					} else if (g.wasPressed(Button.UP)) {
						g.process(Button.UP);
						selection = 7;
						break;
					} else if (g.wasPressed(Button.LEFT)) {
						g.process(Button.LEFT);
						selection = 8;
						break;
					}
				}
				selectionClick(g);
				break;
		}
		textbox.update(g, null);
	}

	/** Processes clicking while selecting an option. */
	private void selectionClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		int x = g.getMouseX();
		int y = g.getMouseY();

		if (clickSmallButtons(x, y, 3, 0)) {
			g.processMouse();
			textbox.setTextSpeed(2 - options[0]);
			textbox.setText(Localisation.getOptionsFrameLabel(options[5]));
			return;
		}
		if (clickSmallButtons(x, y, 2, 1)) {
			g.processMouse();
			return;
		}
		if (clickBigButtons(x, y, 2, 2)) {
			g.processMouse();
			return;
		}
		if (clickBigButtons(x, y, 2, 3)) {
			g.processMouse();
			return;
		}
		if (clickBigButtons(x, y, 2, 4)) {
			g.processMouse();
			return;
		}
		if (clickArrows(x, y, 5)) {
			g.processMouse();
			return;
		}

		if (accept.click(x, y)) {
			g.processMouse();
			accept.animate(true);
			selection = 8;
			clickClose = true;
			return;
		}
		if (back.click(x, y)) {
			g.processMouse();
			back.animate(true);
			selection = 9;
			clickClose = true;
			return;
		}
	}

	private boolean clickSmallButtons(int x, int y, int amount, int optionID) {
		int sel = items.getChildMulti(0, optionID).clickMulti(x, y, amount);
		if (sel != -1) {
			options[optionID] = sel;
			return true;
		}
		return false;
	}

	private boolean clickBigButtons(int x, int y, int amount, int optionID) {
		int sel = items.getChildMulti(2, optionID).clickMulti(x, y, amount);
		if (sel != -1) {
			options[optionID] = sel;
			return true;
		}
		return false;
	}

	private boolean clickArrows(int x, int y, int optionID) {
		int sel = items.getChildMulti(4, optionID).clickMulti(x, y, 2);
		if (sel == 0) {
			if (options[optionID] == 0) {
				options[optionID] = max[optionID];
			} else {
				options[optionID]--;
			}
			if (optionID == 5) {
				arrows.animate(0, false);
				textbox.setBoxStyle(options[5]);
				textbox.setText(Localisation.getOptionsFrameLabel(options[5]));
				textbox.fastForward();
			}
			return true;
		} else if (sel == 1) {
			if (options[optionID] == max[optionID]) {
				options[optionID] = 0;
			} else {
				options[optionID]++;
			}
			if (optionID == 5) {
				arrows.animate(1, false);
				textbox.setBoxStyle(options[5]);
				textbox.setText(Localisation.getOptionsFrameLabel(options[5]));
				textbox.fastForward();
			}
			return true;
		}
		return false;
	}

	/** Draws the left part of the option screen. */
	@Override
	public void drawL() {
		if (status != CLOSED) {
			drawScreenBackground();
			textbox.drawL();
		}
	}

	/** Draws the right part of the option screen. */
	@Override
	public void drawR() {
		if (status != CLOSED) {
			drawBackground();
			drawButtons();
			drawSelection();
			drawBackButton();
		}
	}

	private void drawSelection() {
		items.drawMulti(Localisation.getOptionsText(), 8, selection);
	}

	private void drawButtons() {
		char[][] text = Localisation.getOptionsOptions();

		drawSmallButtons(0, 3, options[0], text, 0);
		drawSmallButtons(1, 2, options[1], text, 3);
		drawBigButtons(2, 2, options[2], text, 5);
		drawBigButtons(3, 2, options[3], text, 7);
		drawBigButtons(4, 2, options[4], text, 9);
		drawArrows(5, Localisation.getOptionsTextStyle(options[5]));
	}

	private void drawSmallButtons(int index, int amount, int active, char[][] text, int t_index) {
		for (int i = 0; i < amount; i++) {
			if (active == i) {
				items.getChildMulti(1, index).drawMulti(text[t_index + i], i, false);
			} else {
				items.getChildMulti(0, index).drawMulti(text[t_index + i], i, false);
			}
		}
	}

	private void drawBigButtons(int index, int amount, int active, char[][] text, int t_index) {
		for (int i = 0; i < amount; i++) {
			if (active == i) {
				items.getChildMulti(3, index).drawMulti(text[t_index + i], i, false);
			} else {
				items.getChildMulti(2, index).drawMulti(text[t_index + i], i, false);
			}
		}
	}

	private void drawArrows(int index, char[] text) {
		Cell ar = items.getChildMulti(4, index);
		ar.drawMulti(2, -1);
		ar.getChild(0).draw(text);
	}

	/** Draws the background of the left screen. */
	private void drawScreenBackground() {
		backgroundL.draw();
	}

	/** Draws the back button. */
	private void drawBackButton() {
		char[][] s = Localisation.getOptionsBackButton();
		accept.draw(s[0], selection == 8);
		back.draw(s[1], selection == 9);
	}

	/** Draws the background of the right screen. */
	private void drawBackground() {
		background.draw(Localisation.getOptionsTitle());
	}

	@Override
	public boolean hasFocus() {
		return status != CLOSED;
	}

	@Override
	public boolean occupiesLeft() {
		return status != CLOSED;
	}

	@Override
	public boolean occupiesRight() {
		return status != CLOSED;
	}

}
