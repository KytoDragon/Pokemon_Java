package sequenze.awt;

import util.Logger;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import sequenze.Button;
import sequenze.Game;
import sequenze.GraphicDisplay;

public class GameAWT extends Game implements KeyListener, MouseListener, MouseMotionListener {
	
	/** Buffer for key presses. */
	private boolean[] keybuffer;
	/** Buffer for a mouse clicks. */
	private boolean mousebuffer;
	/** Buffer for the mouse x coordinate. */
	private int mousexbuffer;
	/** Buffer for the mouse y coordinate. */
	private int mouseybuffer;
	
	public GameAWT(final JFrame screen) {
		super();
		if(screen == null){
			close = true;
			return;
		}
		setGame(this);
		keybuffer = new boolean[Button.length()];
		final GameAWT g = this;
		try {
			EventQueue.invokeAndWait(() -> {
				screen.addKeyListener(g);
				Container c = screen.getContentPane();
				c.addMouseListener(g);
				c.addMouseMotionListener(g);
			});
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.add(e);
			close = true;
		}
	}
	
	@Override
	public int getMouseX() {
		return mousexbuffer / GraphicDisplay.MODIFIER - GraphicDisplay.SIZE;
	}
	
	@Override
	public int getMouseY() {
		return mouseybuffer / GraphicDisplay.MODIFIER;
	}
	
	@Override
	protected void buttonsMouse() {
		if (mousebuffer) {
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
		if (keybuffer[Button.UP.getID()] && !keyDown[Button.UP.getID()]) {
			keyDown[Button.UP.getID()] = true;
			isUnprocessed[Button.UP.getID()] = true;
		}
		if (!keybuffer[Button.UP.getID()] && keyDown[Button.UP.getID()]) {
			keyDown[Button.UP.getID()] = false;
			isUnprocessed[Button.UP.getID()] = true;
		}
		if (keybuffer[Button.DOWN.getID()] && !keyDown[Button.DOWN.getID()]) {
			keyDown[Button.DOWN.getID()] = true;
			isUnprocessed[Button.DOWN.getID()] = true;
		}
		if (!keybuffer[Button.DOWN.getID()] && keyDown[Button.DOWN.getID()]) {
			keyDown[Button.DOWN.getID()] = false;
			isUnprocessed[Button.DOWN.getID()] = true;
		}
		if (keybuffer[Button.LEFT.getID()] && !keyDown[Button.LEFT.getID()]) {
			keyDown[Button.LEFT.getID()] = true;
			isUnprocessed[Button.LEFT.getID()] = true;
		}
		if (!keybuffer[Button.LEFT.getID()] && keyDown[Button.LEFT.getID()]) {
			keyDown[Button.LEFT.getID()] = false;
			isUnprocessed[Button.LEFT.getID()] = true;
		}
		if (keybuffer[Button.RIGHT.getID()] && !keyDown[Button.RIGHT.getID()]) {
			keyDown[Button.RIGHT.getID()] = true;
			isUnprocessed[Button.RIGHT.getID()] = true;
		}
		if (!keybuffer[Button.RIGHT.getID()] && keyDown[Button.RIGHT.getID()]) {
			keyDown[Button.RIGHT.getID()] = false;
			isUnprocessed[Button.RIGHT.getID()] = true;
		}
		if (keybuffer[Button.A.getID()] && !keyDown[Button.A.getID()]) {
			keyDown[Button.A.getID()] = true;
			isUnprocessed[Button.A.getID()] = true;
		}
		if (!keybuffer[Button.A.getID()] && keyDown[Button.A.getID()]) {
			keyDown[Button.A.getID()] = false;
			isUnprocessed[Button.A.getID()] = true;
		}
		if (keybuffer[Button.B.getID()] && !keyDown[Button.B.getID()]) {
			keyDown[Button.B.getID()] = true;
			isUnprocessed[Button.B.getID()] = true;
		}
		if (!keybuffer[Button.B.getID()] && keyDown[Button.B.getID()]) {
			keyDown[Button.B.getID()] = false;
			isUnprocessed[Button.B.getID()] = true;
		}
		if (keybuffer[Button.START.getID()] && !keyDown[Button.START.getID()]) {
			keyDown[Button.START.getID()] = true;
			isUnprocessed[Button.START.getID()] = true;
		}
		if (!keybuffer[Button.START.getID()] && keyDown[Button.START.getID()]) {
			keyDown[Button.START.getID()] = false;
			isUnprocessed[Button.START.getID()] = true;
		}
		if (keybuffer[Button.SELECT.getID()] && !keyDown[Button.SELECT.getID()]) {
			keyDown[Button.SELECT.getID()] = true;
			isUnprocessed[Button.SELECT.getID()] = true;
		}
		if (!keybuffer[Button.SELECT.getID()] && keyDown[Button.SELECT.getID()]) {
			keyDown[Button.SELECT.getID()] = false;
			isUnprocessed[Button.SELECT.getID()] = true;
		}
		if (keybuffer[Button.DEBUG.getID()] && !keyDown[Button.DEBUG.getID()]) {
			keyDown[Button.DEBUG.getID()] = true;
			isUnprocessed[Button.DEBUG.getID()] = true;
		}
		if (!keybuffer[Button.DEBUG.getID()] && keyDown[Button.DEBUG.getID()]) {
			keyDown[Button.DEBUG.getID()] = false;
			isUnprocessed[Button.DEBUG.getID()] = true;
		}
		if (keybuffer[Button.EXIT.getID()] && !keyDown[Button.EXIT.getID()]) {
			keyDown[Button.EXIT.getID()] = true;
			isUnprocessed[Button.EXIT.getID()] = true;
		}
		if (!keybuffer[Button.EXIT.getID()] && keyDown[Button.EXIT.getID()]) {
			keyDown[Button.EXIT.getID()] = false;
			isUnprocessed[Button.EXIT.getID()] = true;
		}
		if (keybuffer[Button.PAUSE.getID()] && !keyDown[Button.PAUSE.getID()]) {
			keyDown[Button.PAUSE.getID()] = true;
			isUnprocessed[Button.PAUSE.getID()] = true;
		}
		if (!keybuffer[Button.PAUSE.getID()] && keyDown[Button.PAUSE.getID()]) {
			keyDown[Button.PAUSE.getID()] = false;
			isUnprocessed[Button.PAUSE.getID()] = true;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			mousebuffer = true;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			mousebuffer = false;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		switch (key.getKeyCode()) {
		case KeyEvent.VK_UP:
			keybuffer[Button.UP.getID()] = true;
			break;
		case KeyEvent.VK_DOWN:
			keybuffer[Button.DOWN.getID()] = true;
			break;
		case KeyEvent.VK_LEFT:
			keybuffer[Button.LEFT.getID()] = true;
			break;
		case KeyEvent.VK_RIGHT:
			keybuffer[Button.RIGHT.getID()] = true;
			break;
		case KeyEvent.VK_ENTER:
			keybuffer[Button.A.getID()] = true;
			break;
		case KeyEvent.VK_BACK_SPACE:
			keybuffer[Button.B.getID()] = true;
			break;
		case KeyEvent.VK_N:
			keybuffer[Button.START.getID()] = true;
			break;
		case KeyEvent.VK_M:
			keybuffer[Button.SELECT.getID()] = true;
			break;
		case KeyEvent.VK_F3:
			keybuffer[Button.DEBUG.getID()] = true;
			break;
		case KeyEvent.VK_P:
			keybuffer[Button.PAUSE.getID()] = true;
			break;
		case KeyEvent.VK_ESCAPE:
			keybuffer[Button.EXIT.getID()] = true;
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent key) {
		switch (key.getKeyCode()) {
		case KeyEvent.VK_UP:
			keybuffer[Button.UP.getID()] = false;
			break;
		case KeyEvent.VK_DOWN:
			keybuffer[Button.DOWN.getID()] = false;
			break;
		case KeyEvent.VK_LEFT:
			keybuffer[Button.LEFT.getID()] = false;
			break;
		case KeyEvent.VK_RIGHT:
			keybuffer[Button.RIGHT.getID()] = false;
			break;
		case KeyEvent.VK_ENTER:
			keybuffer[Button.A.getID()] = false;
			break;
		case KeyEvent.VK_BACK_SPACE:
			keybuffer[Button.B.getID()] = false;
			break;
		case KeyEvent.VK_N:
			keybuffer[Button.START.getID()] = false;
			break;
		case KeyEvent.VK_M:
			keybuffer[Button.SELECT.getID()] = false;
			break;
		case KeyEvent.VK_F3:
			keybuffer[Button.DEBUG.getID()] = false;
			break;
		case KeyEvent.VK_P:
			keybuffer[Button.PAUSE.getID()] = false;
			break;
		case KeyEvent.VK_ESCAPE:
			keybuffer[Button.EXIT.getID()] = false;
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	@Override
	public void mouseDragged(MouseEvent mouse) {
		mousexbuffer = mouse.getX();
		mouseybuffer = mouse.getY();
	}
	
	@Override
	public void mouseMoved(MouseEvent mouse) {
		mousexbuffer = mouse.getX();
		mouseybuffer = mouse.getY();
	}
}
