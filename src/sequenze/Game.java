package sequenze;

public abstract class Game {

	/** Whether the game is running or paused. */
	protected boolean isRunning;
	/** Whether the mouse is held down. */
	protected boolean mouseDown;
	/** Whether the mouse was clicked in current tick. */
	protected boolean mouseClicked;
	/** Whether the last mouse action was processed. */
	protected boolean mouseUnprocessed;
	/** Current mouse x. */
	protected int mouseX;
	/** Current mouse y. */
	protected int mouseY;
	/** List of whether a certain key is currently held down. */
	protected boolean[] keyDown;
	/** List of whether a certain key was pressed in the current tick. */
	protected boolean[] wasPressed;
	/** List of whether a certain key action was processed. */
	protected boolean[] isUnprocessed;
	/** Whether a screenshot has been triggeres. */
	protected boolean screenshot;
	/** Whether a close request has been triggeres. */
	protected boolean close;
	/** Whether the game is paused. */
	protected boolean paused;

	protected boolean bike;// whether the bike is selected, TODO place elsewhere and toggle correctly
	protected byte maxBikeSpeed;// maximum bike speed, TODO place elsewhere and calculate correctly
	/** The currently used Game instance. */
	private Game cg;
	private boolean queueA;

	/** Sets all variables to their initial value. */
	public Game() {
		keyDown = new boolean[Button.length()];
		wasPressed = new boolean[Button.length()];
		isUnprocessed = new boolean[Button.length()];
		bike = false;
		maxBikeSpeed = 4;
		isRunning = false;
		mouseClicked = false;
		mouseDown = false;
		mouseUnprocessed = false;
		mouseX = 0;
		mouseY = 0;
		screenshot = false;
		close = false;
		queueA = false;
	}

	/** Sets the currently used input manager. */
	public void setGame(Game g) {
		cg = g;
	}

	public boolean wasPressed(Button button) {
		return wasPressed[button.getID()];
	}

	public boolean keyDown(Button button) {
		return keyDown[button.getID()];
	}

	public boolean mouseDown() {
		return mouseDown;
	}

	public boolean mouseClicked() {
		return mouseClicked;
	}

	/** Returns the mouse x value. */
	public abstract int getMouseX();

	/** Returns the mouse y value. */
	public abstract int getMouseY();

	/** Updates button presses. */
	protected abstract void buttonsMouse();

	/** Updates keyboard presses. */
	protected abstract void buttonsKeyboard();

	/** Processes key-presses and notes them. */
	public void input() {
		processAll();
		mouseX = cg.getMouseX();
		mouseY = cg.getMouseY();
		cg.buttonsMouse();
		cg.buttonsKeyboard();
		if (SystemControll.getFlag(SystemControll.LButtonIsA)) {
			keyDown[Button.A.getID()] |= keyDown[Button.L.getID()];
			isUnprocessed[Button.A.getID()] |= isUnprocessed[Button.L.getID()];
			keyDown[Button.L.getID()] = false;
			isUnprocessed[Button.L.getID()] = false;
		}
		for (int i = 0; i < wasPressed.length; i++) {
			wasPressed[i] = keyDown[i] && isUnprocessed[i];
		}
		if (queueA) {
			wasPressed[Button.A.getID()] = true;
			queueA = false;
		}
		if (wasPressed[Button.DEBUG.getID()]) {
			process(Button.DEBUG);
			screenshot = true;
		}
		if (SystemControll.getFlag(SystemControll.Pausable) && wasPressed[Button.EXIT.getID()]) {
			process(Button.EXIT);
			close = true;
		}
		mouseClicked = mouseDown && mouseUnprocessed;
	}

	/** Marks the given key as processed. */
	public void process(Button button) {
		isUnprocessed[button.getID()] = false;
		wasPressed[button.getID()] = false;
	}

	/** Marks the mouse click as processed. */
	public void processMouse() {
		mouseUnprocessed = false;
		mouseClicked = false;
	}

	/** Marks everything as processed. */
	public void processAll() {
		for (int i = 0; i < wasPressed.length; i++) {
			isUnprocessed[i] = false;
			wasPressed[i] = false;
		}
		mouseUnprocessed = false;
		mouseClicked = false;
	}

	public void queueAPress() {
		queueA = true;
	}
}
