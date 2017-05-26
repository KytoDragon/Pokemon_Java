package sequenze.menu;

import sequenze.Game;

public abstract class GuiItem {
	
	public abstract void drawL();
	
	public abstract void drawR();
	
	public abstract void update(Game g);
	
	public abstract boolean hasFocus();
	
	public abstract boolean occupiesLeft();
	
	public abstract boolean occupiesRight();
	
}
