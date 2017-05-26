package sequenze.opengl;

import image.ImageWriter;
import image.IndexedTexture;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import sequenze.GraphicDisplay;
import sequenze.GraphicHandler;
import sequenze.SaveSystem;
import util.ConV;
import util.Logger;
import util.Map;

public class GraphicDisplayGL extends GraphicDisplay {

	private boolean closing;
	private JFrame jf;
	private Map<Integer, Texture> map;
	private int lastIndex;

	public GraphicDisplayGL() {
		map = new Map<Integer, Texture>();
		try {
			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					Canvas canvas = new Canvas();
					canvas.setPreferredSize(new Dimension(SIZE * MODIFIER * 2, SIZE * MODIFIER));
					canvas.setFocusable(true);
					canvas.setIgnoreRepaint(true);
					jf = new JFrame("Pokemon?");
					jf.add(canvas);
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
					try {
						Display.setParent(canvas);
					} catch (LWJGLException e) {
						Logger.add(e);
						closing = true;
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.add(e);
			closing = true;
			return;
		}

		try {
			Display.create();
		} catch (LWJGLException e) {
			Logger.add(e);
			closing = true;
			return;
		}

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
	}

	@Override
	public void drawRectangle(int x, int y, int w, int h, int r, int g, int b, int a) {
		glDisable(GL_TEXTURE_2D);
		glColor4b((byte) (r / 2), (byte) (g / 2), (byte) (b / 2), (byte) (a / 2));
		glBegin(GL_QUADS);
		glVertex2i(x, y);
		glVertex2i(x + w, y);
		glVertex2i(x + w, y + h);
		glVertex2i(x, y + h);
		glEnd();
		glColor4b((byte) 127, (byte) 127, (byte) 127, (byte) 127);
		glEnable(GL_TEXTURE_2D);

	}

	@Override
	public void drawTexture(int x, int y, int textureID) {
		Texture tex = map.get(new Integer(textureID));
		tex.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2i(x, y);
		glTexCoord2f(tex.getWidth(), 0);
		glVertex2i(x + tex.getImageWidth(), y);
		glTexCoord2f(tex.getWidth(), tex.getHeight());
		glVertex2i(x + tex.getImageWidth(), y + tex.getImageHeight());
		glTexCoord2f(0, tex.getHeight());
		glVertex2i(x, y + tex.getImageHeight());
		glEnd();
	}

	@Override
	public void changeToLeft() {
		glLoadIdentity();
		glViewport(0, 0, SIZE * MODIFIER, SIZE * MODIFIER);
		glOrtho(0, SIZE, SIZE, 0, 1, -1);
	}

	@Override
	public void changeToRight() {
		glLoadIdentity();
		glViewport(SIZE * MODIFIER, 0, SIZE * MODIFIER, SIZE * MODIFIER);
		glOrtho(0, SIZE, SIZE, 0, 1, -1);
	}

	@Override
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void drawToScreen() {
		Display.update();
	}

	@Override
	public int loadTexture(image.Texture image) {
		if (lastIndex == Integer.MAX_VALUE) {
			Logger.add(Logger.GRAPHICS, "Texture buffer full.");
			return GraphicHandler.DEFAULTTEXTUREID;
		}

		IntImageData data = new IntImageData(image);

		Texture tex = getTexture(data);
		if (tex == null) {
			Logger.add(Logger.GRAPHICS, "Unable to load Texture");
			return GraphicHandler.DEFAULTTEXTUREID;
		}
		map.put(new Integer(lastIndex), tex);
		return lastIndex++;
	}

	@Override
	public void close() {
		if (jf != null) {
			Display.destroy();
			final JFrame jframe = jf;
			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					jframe.dispose();
				}
			});
			jf = null;
		}
	}

	@Override
	public boolean isCloseRequested() {
		return closing;
	}

	@Override
	public void move(int x, int y) {
		glTranslated(x, y, 0);
	}

	@Override
	protected void drawIndexedTexture(int x, int y, int textureID, int palette) {
		// TODO
		return;
	}

	@Override
	protected int loadIndexedTexture(IndexedTexture image) {
		// TODO
		return GraphicHandler.DEFAULTPALETTEID;
	}

	@Override
	protected int loadPalette(int[] palette) {
		// TODO
		return 0;
	}

	private class IntImageData implements ImageData {

		private ByteBuffer buffer;
		private int width;
		private int height;
		private int texwidth;
		private int texheight;
		private boolean hasAlpha;

		public IntImageData(image.Texture texture) {
			width = texture.getWidth();
			height = texture.getHeight();

			texwidth = ConV.get2Fold(width);
			texheight = ConV.get2Fold(height);

			buffer = ByteBuffer.allocateDirect(texwidth * texheight * 4);
			IntBuffer buffer2 = buffer.asIntBuffer();
			hasAlpha = texture.alpha != image.Texture.NO_ALPHA;
			int y = 0;
			for (; y < height; y++) {
				buffer2.position(y * texwidth);
				buffer2.put(texture.image[y]);
			}
			buffer2.position(texheight * texwidth);
			buffer.flip();
		}

		@Override
		public int getDepth() {
			return hasAlpha ? 32 : 24;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public int getTexWidth() {
			return texwidth;
		}

		@Override
		public int getTexHeight() {
			return texheight;
		}

		@Override
		public ByteBuffer getImageBufferData() {
			return buffer;
		}

	}

	public Texture getTexture(ImageData dataSource) {

		ByteBuffer textureBuffer = dataSource.getImageBufferData();

		// create the texture ID for this texture 
		int textureID = InternalTextureLoader.createTextureID();
		TextureImpl texture = new TextureImpl(null, GL_TEXTURE_2D, textureID);

		glBindTexture(GL_TEXTURE_2D, textureID);

		int width = dataSource.getWidth();
		int height = dataSource.getHeight();

		boolean hasAlpha = dataSource.getDepth() == 32;

		texture.setTextureWidth(dataSource.getTexWidth());
		texture.setTextureHeight(dataSource.getTexHeight());

		int texWidth = texture.getTextureWidth();
		int texHeight = texture.getTextureHeight();

		int dstPixelFormat = hasAlpha ? GL_RGBA : GL_RGB;

		texture.setWidth(width);
		texture.setHeight(height);
		texture.setAlpha(hasAlpha);

		IntBuffer temp = BufferUtils.createIntBuffer(16);
		glGetInteger(GL_MAX_TEXTURE_SIZE, temp);
		int max = temp.get(0);
		if ((texWidth > max) || (texHeight > max)) {
			Logger.add(Logger.GRAPHICS, "Attempt to allocate a texture to big for the current hardware");
			return null;
		}
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D,
				0,
				dstPixelFormat,
				ConV.get2Fold(width),
				ConV.get2Fold(height),
				0,
				GL_BGRA,
				GL_UNSIGNED_INT_8_8_8_8,
				textureBuffer);

		return texture;
	}

	@Override
	protected void takeScreenshot() {
		BufferedImage img = new BufferedImage(SIZE * MODIFIER * 2, SIZE * MODIFIER, BufferedImage.TYPE_INT_RGB);
		IntBuffer buffer = BufferUtils.createIntBuffer(SIZE * MODIFIER * 2 * SIZE * MODIFIER);
		glReadPixels(0, 0, SIZE * MODIFIER * 2, SIZE * MODIFIER, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
		for (int y = 0; y < SIZE * MODIFIER; y++) {
			for (int x = 0; x < SIZE * MODIFIER * 2; x++) {
				img.setRGB(x, SIZE * MODIFIER - y - 1, buffer.get(x + SIZE * MODIFIER * 2 * y));
			}
		}
		String fileName = SaveSystem.getScreenshotName();
		ImageWriter.storeImage(img, fileName);
	}

	@Override
	protected void unloadTexture(int id) {
		Texture t = map.remove(id);
		t.release();
	}

	@Override
	protected void unloadPalette(int id) {
		// TODO
	}

	@Override
	protected void unloadIndexedTexture(int id) {
		// TODO
	}

}
