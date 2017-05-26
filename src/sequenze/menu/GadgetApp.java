package sequenze.menu;

import sequenze.Game;

public abstract class GadgetApp {
	
	/** Internal name. */
	String name;
	/** Index of the position of the app. */
	int position;
	
	/** Draws the left section of the app. */
	public abstract void drawL();
	
	/** Draws the right section of the app. */
	public abstract void drawR();
	
	/** Updates the app and processes events and key-presses. */
	public abstract void update(Game g, Gadget gadget);
	
	/** Sets the app to active. */
	public abstract void activate();
	
	/** Sets the app to inactive. */
	public abstract void deactivate();
	
	/** Returns whether the app needs the left screen. */
	public abstract boolean secondScreen();
	
	/** Terminates the app. */
	public abstract void stop();
	
	/** Draws the menu icon of the app. */
	public abstract void drawMenuItem();
	
	/** Returns whether the app gets key-presses. */
	public abstract boolean getsFocus(); 
	
}
