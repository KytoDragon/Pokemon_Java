package sequenze.menu;

import sequenze.Game;
import sequenze.GraphicHandler;
import util.Random;

public class MyApp extends GadgetApp {
	
	@Override
	public void drawL() {
	}
	
	@Override
	public void drawR() {
	}
	
	@Override
	public void update(Game g, Gadget gadget) {
		if (Random.nextInt(199) == 0) {
			gadget.closeApp();
		}
	}
	
	@Override
	public void activate() {
	}
	
	@Override
	public void deactivate() {
	}
	
	@Override
	public boolean secondScreen() {
		return true;
	}
	
	@Override
	public void stop() {
	}
	
	@Override
	public void drawMenuItem() {
		GraphicHandler.drawRectangle(80, 80, 32, 32, 255, 255, 255, 255);
	}
	
	@Override
	public boolean getsFocus() {
		return true;
	} 
	
}
