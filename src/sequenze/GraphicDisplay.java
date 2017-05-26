package sequenze;

import image.IndexedTexture;
import image.Texture;

public abstract class GraphicDisplay {

	/** The width and height of each screen. */
	public final static int SIZE = 256;
	/** The magnifying factor. */
	public final static int MODIFIER = 2;

	/** Draw a single colour rectangle at the given position. */
	protected abstract void drawRectangle(int x, int y, int width, int height, int red, int green, int blue, int alpha);

	/** Draw the given texture at the given position. */
	protected abstract void drawTexture(int x, int y, int textureID);

	/** Draw the given indexed texture at the given position. */
	protected abstract void drawIndexedTexture(int x, int y, int textureID, int palette);

	/** Change to the left screen. */
	protected abstract void changeToLeft();

	/** Change to the right screen. */
	protected abstract void changeToRight();

	/** Clear the screen. */
	protected abstract void clear();

	/** Finalise the drawing of a frame. */
	protected abstract void drawToScreen();

	/** Return the index of the given texture. */
	protected abstract int loadTexture(Texture image);

	/** Return the index of the given indexed texture. */
	protected abstract int loadIndexedTexture(IndexedTexture image);

	protected abstract int loadPalette(int[] palette);

	/** Close the screen. */
	protected abstract void close();

	/** Return whether the screen is requested to close. */
	protected abstract boolean isCloseRequested();

	/** Translate all future drawings by the given coordinates. */
	protected abstract void move(int x, int y);

	protected abstract void takeScreenshot();

	protected abstract void unloadTexture(int id);

	protected abstract void unloadPalette(int id);

	protected abstract void unloadIndexedTexture(int id);

}
