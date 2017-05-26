package sequenze.overlay;

import sequenze.Game;
import sequenze.GraphicHandler;

public class MenuObjectHandler {

	public int[] tex; // list of texture indices
	static final int YNL = 180;
	static final int YNM = 181;
	static final int YNR = 182;
	static final int YNLS = 183;
	static final int YNMS = 184;
	static final int YNRS = 185;
	MenuObject mo; // currently active menu object
	TextBox text; // currently active text box
	public static MenuObjectHandler current;

	// Pictures
	// Shaking
	// Action-Command
	public MenuObjectHandler() {
		tex = GraphicHandler.giveTextures("Menu");
		current = this;
	}

	/** Draws the right section to the screen. */
	public void drawR() {
		if (mo != null) {
			mo.drawR();
		}
		if (text != null) {
			text.drawR();
		}
	}

	/** Draws the left section to the screen. */
	public void drawL() {
		if (mo != null) {
			mo.drawL();
		}
		if (text != null) {
			text.drawL();
		}
	}

	/** Returns whether a menu object is active and reads key-presses. */
	public boolean isActive() {
		return (mo != null && mo.getsInput() || text != null && text.getsInput());
	}

	/** Updates the active menu object and processes events and key-presses. */
	public void update(Game g) {
		if (text != null) {
			text.update(g, this);
		}
		// TODO system breaks completly when user forgets to call Input.clear()
		if (mo != null) {
			mo.update(g, this);
		}
	}

	/** Deletes the currently active menu object. */
	public void closeObject() {
		mo.close(this);
	}

	/** Deletes the currently active menu object. */
	public void deleteObject() {
		mo = null;
	}

	/** Deletes the currently active Text. */
	public void deleteText() {
		text = null;
	}

	/** Sets the menu object to the given one. */
	public void setObject(MenuObject m) {
		mo = m;
	}

	/** Sets the menu object to the given one. */
	public void setText(TextBox t) {
		text = t;
	}

}
