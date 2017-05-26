package sequenze.opengl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import sequenze.Button;
import sequenze.Game;
import sequenze.GraphicDisplay;

public class GameGL extends Game {
	
	public GameGL() {
		super();
		setGame(this);
	}
	
	@Override
	public int getMouseX() {
		return Mouse.getX() / GraphicDisplay.MODIFIER - GraphicDisplay.SIZE;
	}
	
	@Override
	public int getMouseY() {
		return GraphicDisplay.SIZE - Mouse.getY() / GraphicDisplay.MODIFIER - 1;
	}
	
	@Override
	protected void buttonsMouse() {
		if (Mouse.isButtonDown(0)) {
			if (!mouseDown) {
				mouseDown = true;
				mouseUnprocessed = true;
			}
		} else {
			if (mouseDown) {
				mouseDown = false;
				mouseUnprocessed = true;
			}
		}
	}
	
	@Override
	protected void buttonsKeyboard() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.UP.getID()]) {
					keyDown[Button.UP.getID()] = true;
					isUnprocessed[Button.UP.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.UP.getID()]) {
					keyDown[Button.UP.getID()] = false;
					isUnprocessed[Button.UP.getID()] = true;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.DOWN.getID()]) {
					keyDown[Button.DOWN.getID()] = true;
					isUnprocessed[Button.DOWN.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.DOWN.getID()]) {
					keyDown[Button.DOWN.getID()] = false;
					isUnprocessed[Button.DOWN.getID()] = true;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.LEFT.getID()]) {
					keyDown[Button.LEFT.getID()] = true;
					isUnprocessed[Button.LEFT.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.LEFT.getID()]) {
					keyDown[Button.LEFT.getID()] = false;
					isUnprocessed[Button.LEFT.getID()] = true;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.RIGHT.getID()]) {
					keyDown[Button.RIGHT.getID()] = true;
					isUnprocessed[Button.RIGHT.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.RIGHT.getID()]) {
					keyDown[Button.RIGHT.getID()] = false;
					isUnprocessed[Button.RIGHT.getID()] = true;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.A.getID()]) {
					keyDown[Button.A.getID()] = true;
					isUnprocessed[Button.A.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.A.getID()]) {
					keyDown[Button.A.getID()] = false;
					isUnprocessed[Button.A.getID()] = true;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_BACK) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.B.getID()]) {
					keyDown[Button.B.getID()] = true;
					isUnprocessed[Button.B.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.B.getID()]) {
					keyDown[Button.B.getID()] = false;
					isUnprocessed[Button.B.getID()] = true;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_N) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.START.getID()]) {
					keyDown[Button.START.getID()] = true;
					isUnprocessed[Button.START.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.START.getID()]) {
					keyDown[Button.START.getID()] = false;
					isUnprocessed[Button.START.getID()] = true;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_M) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.SELECT.getID()]) {
					keyDown[Button.SELECT.getID()] = true;
					isUnprocessed[Button.SELECT.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.SELECT.getID()]) {
					keyDown[Button.SELECT.getID()] = false;
					isUnprocessed[Button.SELECT.getID()] = true;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_F3) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.DEBUG.getID()]) {
					keyDown[Button.DEBUG.getID()] = true;
					isUnprocessed[Button.DEBUG.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.DEBUG.getID()]) {
					keyDown[Button.DEBUG.getID()] = false;
					isUnprocessed[Button.DEBUG.getID()] = true;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_P) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.PAUSE.getID()]) {
					keyDown[Button.PAUSE.getID()] = true;
					isUnprocessed[Button.PAUSE.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.PAUSE.getID()]) {
					keyDown[Button.PAUSE.getID()] = false;
					isUnprocessed[Button.PAUSE.getID()] = true;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				if (Keyboard.getEventKeyState() && !keyDown[Button.EXIT.getID()]) {
					keyDown[Button.EXIT.getID()] = true;
					isUnprocessed[Button.EXIT.getID()] = true;
				}
				if (!Keyboard.getEventKeyState() && keyDown[Button.EXIT.getID()]) {
					keyDown[Button.EXIT.getID()] = false;
					isUnprocessed[Button.EXIT.getID()] = true;
				}
			}
		}
	}
	
}
