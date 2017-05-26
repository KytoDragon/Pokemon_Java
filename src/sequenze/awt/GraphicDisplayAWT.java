package sequenze.awt;

import image.ImageWriter;
import image.IndexedTexture;
import image.Texture;
import util.Logger;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import util.Map;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import sequenze.GraphicDisplay;
import sequenze.GraphicHandler;
import sequenze.SaveSystem;

public class GraphicDisplayAWT extends GraphicDisplay {

	/** Current translation x offset. */
	private int xoff;
	/** Current translation y offset. */
	private int yoff;
	/** The currently used screen. */
	private JFrame jf;
	/** Buffer for the left part of the screen. */
	private Screen leftPart;
	/** Buffer for the right part of the screen. */
	private Screen rightPart;
	/** Current graphics for the screen buffer. */
	private Screen current;
	/** Texture list. */
	private Map<Integer, Texture> tex;
	/** Last used texture index. */
	private int lastIndex;
	/** Indexed-Texture list. */
	private Map<Integer, IndexedTexture> indtex;
	/** Last used indexed-texture index. */
	private int lastIndtexIndex;
	/** Indexed-Texture list. */
	private Map<Integer, int[]> palettes;
	/** Last used indexed-texture index. */
	private int lastPalIndex;
	/** Buffer whether the display is requested to close. */
	private boolean closing;

	public GraphicDisplayAWT() {
		tex = new Map<>();
		indtex = new Map<>();
		palettes = new Map<>();
		leftPart = new Screen(SIZE, SIZE);
		rightPart = new Screen(SIZE, SIZE);
		try {
			EventQueue.invokeAndWait(() -> {
				jf = new JFrame("Pokemon?");
				jf.getContentPane().setPreferredSize(new Dimension(SIZE * MODIFIER * 2, SIZE * MODIFIER));
				jf.setResizable(false);
				jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jf.addWindowListener(new WindowListener() {

					@Override
					public void windowOpened(WindowEvent arg0) {
					}

					@Override
					public void windowIconified(WindowEvent arg0) {
					}

					@Override
					public void windowDeiconified(WindowEvent arg0) {
					}

					@Override
					public void windowDeactivated(WindowEvent arg0) {
					}

					@Override
					public void windowClosing(WindowEvent arg0) {
						closing = true;
					}

					@Override
					public void windowClosed(WindowEvent arg0) {
						closing = true;
					}

					@Override
					public void windowActivated(WindowEvent arg0) {
					}
				});
				jf.pack();
				jf.setVisible(true);
			});
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.add(e);
			if (jf != null) {
				jf.dispose();
			}
			jf = null;
		}
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		current.drawRect(x + xoff, y + yoff, width, height, red, green, blue, alpha);
	}

	@Override
	public void drawTexture(int x, int y, int textureID) {
		Texture img = tex.get(new Integer(textureID));
		if (img == null) {
			img = tex.get(new Integer(GraphicHandler.DEFAULTTEXTUREID));
		}
		current.drawTexture(img, x + xoff, y + yoff);
	}

	@Override
	public void changeToLeft() {
		current = leftPart;
	}

	@Override
	public void changeToRight() {
		current = rightPart;
	}

	@Override
	public void clear() {
		leftPart.clear();
		rightPart.clear();
	}

	@Override
	public void drawToScreen() {
		if (xoff != 0 || yoff != 0) {
			Logger.add(Logger.GRAPHICS, "Screen translation was not reset!");
			xoff = 0;
			yoff = 0;
		}
		try {
			EventQueue.invokeAndWait(() -> {
				Graphics g = jf.getContentPane().getGraphics();
				leftPart.drawTo(g, 0, 0, MODIFIER);
				rightPart.drawTo(g, SIZE * MODIFIER, 0, MODIFIER);
				g.dispose();
			});
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.add(e);
		}
	}

	@Override
	public int loadTexture(Texture image) {
		if (lastIndex == Integer.MAX_VALUE) {
			Logger.add(Logger.GRAPHICS, "Texture buffer full.");
			return GraphicHandler.DEFAULTTEXTUREID;
		}
		tex.put(new Integer(lastIndex), image);
		return lastIndex++;
	}

	@Override
	public void unloadTexture(int id) {
		assert (id != GraphicHandler.DEFAULTTEXTUREID);
		tex.remove(new Integer(id));
	}

	@Override
	public void close() {
		if (jf != null) {
			final JFrame jframe = jf;
			EventQueue.invokeLater(() -> jframe.dispose());
			jf = null;
		}
	}

	@Override
	public boolean isCloseRequested() {
		return closing;
	}

	@Override
	public void move(int x, int y) {
		xoff += x;
		yoff += y;
	}

	/** Returns the currently used sreen. */
	public JFrame getScreen() {
		return jf;
	}

	Screen getCurrent() {
		return current;
	}

	@Override
	protected void takeScreenshot() {
		BufferedImage img = new BufferedImage(SIZE * MODIFIER * 2, SIZE * MODIFIER, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		leftPart.drawTo(g, 0, 0, MODIFIER);
		rightPart.drawTo(g, SIZE * MODIFIER, 0, MODIFIER);
		g.dispose();
		String fileName = SaveSystem.getScreenshotName();
		ImageWriter.storeImage(img, fileName);
	}

	@Override
	protected void drawIndexedTexture(int x, int y, int textureID, int palette) {
		IndexedTexture img = indtex.get(new Integer(textureID));
		int[] paletteA = palettes.get(new Integer(palette));
		current.drawPaletteTexture(img, x + xoff, y + yoff, paletteA);
	}

	@Override
	protected int loadIndexedTexture(IndexedTexture image) {
		if (lastIndtexIndex == Integer.MAX_VALUE) {
			Logger.add(Logger.GRAPHICS, "Indexed-texture buffer full.");
			return GraphicHandler.DEFAULTTEXTUREID;
		}
		indtex.put(new Integer(lastIndtexIndex), image);
		return lastIndtexIndex++;
	}

	@Override
	public void unloadIndexedTexture(int id) {
		indtex.remove(new Integer(id));
	}

	@Override
	protected int loadPalette(int[] palette) {
		if (lastPalIndex == Integer.MAX_VALUE) {
			Logger.add(Logger.GRAPHICS, "Palette buffer full.");
			return GraphicHandler.DEFAULTTEXTUREID;
		}
		palettes.put(new Integer(lastPalIndex), palette);
		return lastPalIndex++;
	}

	@Override
	public void unloadPalette(int id) {
		palettes.remove(new Integer(id));
	}
}
