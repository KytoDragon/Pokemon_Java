package sequenze.overlay;

import sequenze.Game;

public abstract class MenuObject {
	
	/** Draws the left section of the object. */
	abstract void drawL();
	
	/** Draws the right section of the object. */
	abstract void drawR();
	
	/** Updates the object and processes events and key-presses. */
	abstract void update(Game g, MenuObjectHandler m);
	
	/** Returns whether the object gets key-presses. */
	abstract boolean getsInput();

	abstract void close(MenuObjectHandler m);
	
}
