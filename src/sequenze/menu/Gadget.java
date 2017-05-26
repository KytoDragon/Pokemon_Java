package sequenze.menu;

import sequenze.Button;
import sequenze.Game;
import sequenze.GraphicHandler;
import sequenze.MessageHandler;
import util.ConV;

public class Gadget extends GuiItem {

	/** Index of the currently active app. */
	private int active;
	/** List of apps. */
	private GadgetApp[] apps;
	/** Currently selected button. */
	private byte selection = 0;
	/** Index of the current animation. */
	private byte animation = 0;
	/** Index of the animation for opening/closing the second screen. */
	private byte secondScreenAnimation = 0;
	/** Whether a phone call is waiting. */
	private boolean hasCall = false;
	/** List of used texture indices. */
	public int[] tex;
	public final static byte LS = 0; // left screen overlay
	public final static byte RS = 1; // right screen overlay
	public final static byte BB = 2; // exit button
	public final static byte RC = 3; // red cover for incoming calls
	public final static byte BG = 4; // right screen background
	/** Status of the gadget. */
	private byte status = CLOSED;
	final static byte STARTUP = 0;
	final static byte RUNAPP = 1;
	final static byte RUNAPPPASSIVE = 2;
	final static byte HAVECALL = 4;
	final static byte ENDCALL = 5;
	final static byte IDLE = 6;
	final static byte IDLEPASSIVE = 7;
	final static byte SHUTDOWN = 8;
	final static byte CLOSED = 9;

	private boolean clickClose;

	public Gadget() {
		tex = GraphicHandler.giveTextures("Gadget");
		if (apps != null) {
			for (GadgetApp app : apps) {
				app.stop();
			}
		}
		// TODO load apps
		apps = new GadgetApp[1];
		apps[0] = new MyApp();
	}

	/** Sets the gadget to inactive. */
	public void deactivate() {
		if (status == RUNAPP) {
			apps[active].deactivate();
		}
		animation = 17;
		status = SHUTDOWN;
	}

	/** Draws the left section of the gadget. */
	@Override
	public void drawL() {
		if (secondScreenAnimation > 0) {
			drawLeftScreen();
		}
		if (status == RUNAPP && apps[active].secondScreen()) {
			apps[active].drawL();
		}
	}

	/** Draws the background for the left Screen. */
	private void drawLeftScreen() {
		GraphicHandler.translateStep(16 - secondScreenAnimation, 0, 16);
		GraphicHandler.drawBox(0, 0, tex[LS]);
		GraphicHandler.translateStep(-16 + secondScreenAnimation, 0, 16);
	}

	/** Draws the right section of the gadget. */
	@Override
	public void drawR() {
		if (status == CLOSED) {
			return;
		}
		GraphicHandler.drawBox(0, 0, tex[BG]);
		if (status == RUNAPP) {
			drawRightScreen();
			apps[active].drawR();
		} else {
			if (status == STARTUP) {
				drawMenu();
				drawRightScreen();
				GraphicHandler.translateStep(0, animation - 16, 16);
				GraphicHandler.drawRectangleMax(0, 0, 0);
				GraphicHandler.translateStep(0, -animation + 16, 16);
			} else if (status == SHUTDOWN) {
				if (active != -1) {
					apps[active].drawR();
				} else {
					drawMenu();
				}
				drawRightScreen();
				GraphicHandler.translateStep(0, animation - 32, 16);
				GraphicHandler.drawRectangleMax(0, 0, 0);
				GraphicHandler.translateStep(0, -animation + 32, 16);
			} else {
				drawMenu();
				drawRightScreen();
				if (hasCall) {
					drawIncomingCall();
				}
			}
		}
	}

	/** Draws the background for the right screen */
	private void drawRightScreen() {
		GraphicHandler.drawBox(0, 0, tex[RS]);
		if (status == RUNAPP) {
			GraphicHandler.drawBox(DataLoader.getGadgetBackButton(), tex[BB]);
		}
	}

	/** Draws the overlay caused by an incomming call. */
	private void drawIncomingCall() {
		GraphicHandler.drawBox(DataLoader.getGadgetRedCover(), tex[RC]);
		// TODO get caller name
		// TODO draw caller name
	}

	/** Updates the gadget and processes events and key-presses */
	@Override
	public void update(Game g) {
		switch (status) {
			case CLOSED:
				if (MessageHandler.hasMessage(MessageHandler.GADGET_OPEN)) {
					clickClose = false;
					animation = 17;
					status = STARTUP;
				}
				// TODO check other for messages
				break;
			case STARTUP:
				if (animation == 32) {// TODO change max
					if (hasCall) {
						animation = 16;// TODO change starting value
					} else {
						animation = 0;
					}
					status = IDLE;
				} else {
					animation++;
				}
				break;
			case RUNAPP:
				if (g.wasPressed(Button.START)) {
					g.process(Button.START);
					deactivate();
				}
				if (apps[active].secondScreen() && secondScreenAnimation != 16) {// TODO change max
					secondScreenAnimation++;
				} else if (!apps[active].secondScreen() && secondScreenAnimation != 0) {
					secondScreenAnimation--;
				}
				apps[active].update(g, this);
				clickRun(g);
				break;
			case RUNAPPPASSIVE:
				if (g.wasPressed(Button.START)) {
					g.process(Button.START);
					status = RUNAPP;
				}
				apps[active].update(g, this);
				break;
			case HAVECALL:
				// TODO
				break;
			case ENDCALL:
				// TODO
				break;
			case IDLE:
				if (hasCall) {
					animation++;
					if (animation == 16) {// TODO change max and loop
						animation = 8;
					}
					if (g.mouseClicked() && ConV.isIn(g.getMouseX(), g.getMouseY(), DataLoader.getGadgetRedCover())) {
						g.processMouse();
						status = HAVECALL;
						if (secondScreenAnimation < 16) {// TODO change max
							secondScreenAnimation++;
						}
					}
					if (g.wasPressed(Button.A)) {
						g.process(Button.A);
						status = HAVECALL;
					} else if (g.wasPressed(Button.B) || g.wasPressed(Button.START)) {
						g.process(Button.B);
						g.process(Button.START);
						deactivate();
					} else if (g.wasPressed(Button.SELECT)) {
						g.process(Button.SELECT);
						status = IDLEPASSIVE;
					}
				}
				if (secondScreenAnimation > 0) {
					secondScreenAnimation--;
				}
				tileClick(g);
				if (g.wasPressed(Button.B) || g.wasPressed(Button.START)) {
					g.process(Button.B);
					g.process(Button.START);
					deactivate();
				} else if (g.wasPressed(Button.SELECT)) {
					g.process(Button.SELECT);
					status = IDLEPASSIVE;
				}
				break;
			case IDLEPASSIVE:
				if (hasCall) {
					animation++;
					if (animation == 16) {// TODO change max and loop
						animation = 8;
					}
					if (g.mouseClicked() && ConV.isIn(g.getMouseX(), g.getMouseY(), DataLoader.getGadgetRedCover())) {
						g.processMouse();
						status = HAVECALL;
						if (secondScreenAnimation < 16) {// TODO change max
							secondScreenAnimation++;
						}
						break;
					}
				}
				if (secondScreenAnimation > 0) {
					secondScreenAnimation--;
				}
				if (g.wasPressed(Button.START)) {
					g.process(Button.START);
					status = IDLE;
					break;
				}
				tileClick(g);
				break;
			case SHUTDOWN:
				if (secondScreenAnimation != 0) {
					secondScreenAnimation--;
				}
				if (animation == 32) {
					if (secondScreenAnimation == 0) {
						animation = 0;
						active = -1;
						status = CLOSED;
						MessageHandler.add(MessageHandler.GADGET_CLOSE, clickClose);
					}
				} else {
					animation++;
				}
				break;
		}
	}

	/** Processes mouse clicks on the individual app icons. */
	private void tileClick(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		int p = getPosition(g.getMouseX(), g.getMouseY());
		for (int i = 0; i < apps.length; i++) {
			if (apps[i].position == p) {
				apps[i].activate();
				active = i;
				if (apps[active].getsFocus()) {
					status = RUNAPP;
				} else {
					status = RUNAPPPASSIVE;
				}
				if (apps[active].secondScreen() && secondScreenAnimation != 16) {// TODO change max
					secondScreenAnimation++;
				} else if (!apps[active].secondScreen() && secondScreenAnimation != 0) {
					secondScreenAnimation--;
				}
			}
		}
		if (ConV.isIn(g.getMouseX(), g.getMouseY(), DataLoader.getGadgetBackButton())) {
			clickClose = true;
			deactivate();
		}
	}

	/** Searches for an icomming call. */
	public void lookForCalls() {
		boolean foundCall = false;
		// TODO check if new call available
		if (foundCall) {
			hasCall = true;
		}
	}

	/** Draws the app list */
	void drawMenu() {
		GraphicHandler.drawBox(0, 0, tex[BG]);
		for (GadgetApp app : apps) {
			app.drawMenuItem();
		}
	}

	/** Return whether the second screen in shown. */
	public boolean secondScreen() {
		if (status == RUNAPP) {
			return apps[active].secondScreen();
		}
		return false;
	}

	/** Processes clicking while the gadget is open. */
	void click(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		if (ConV.isIn(g.getMouseX(), g.getMouseY(), DataLoader.getGadgetBackButton())) {
			clickClose = true;
			deactivate();
		}
	}

	void clickRun(Game g) {
		if (!g.mouseClicked()) {
			return;
		}
		if (ConV.isIn(g.getMouseX(), g.getMouseY(), DataLoader.getGadgetBackButton())) {
			g.processMouse();
			clickClose = true;
			deactivate();
			return;
		}
		if (ConV.isIn(g.getMouseX(), g.getMouseY(), DataLoader.getGadgetReturnButton())) {
			g.processMouse();
			apps[active].deactivate();
			closeApp();
		}
	}

	/** Closes the currently running app. */
	public void closeApp() {
		active = -1;
		status = IDLE;
	}

	/** Return the icon index of the current position */
	private int getPosition(int x, int y) {
		int[][] positions = DataLoader.getGadgetPositions();
		for (int i = 0; i < positions.length; i++) {
			if (ConV.isIn(x, y, positions[i])) {
				return i;
			}
		}
		return -1;
	}

	/** Whether the gadget has the input focus. */
	@Override
	public boolean hasFocus() {
		return status != CLOSED;
	}

	/** Whether the gadget draws to the left screen. */
	@Override
	public boolean occupiesLeft() {
		return secondScreenAnimation > 0 || status == RUNAPP && apps[active].secondScreen();
	}

	@Override
	public boolean occupiesRight() {
		return status != CLOSED;
	}
}
