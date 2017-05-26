package image;

import util.ConV;

public class Texture {

	public int[][] image;
	public byte alpha;
	public static final byte NO_ALPHA = 0;
	public static final byte SIMPLE_ALPHA = 1;
	public static final byte COMPLEX_ALPHA = 2;

	public Texture(int width, int height, byte alpha) {
		image = new int[height][width];
		this.alpha = alpha;
	}

	public int getHeight() {
		return image.length;
	}

	public int getWidth() {
		return image[0].length;
	}

	public Texture getSubTexture(int x, int y, int width, int height) {
		if (x < 0 || y < 0 || width < 0 || height < 0 || y + height > image.length || x + width > image[0].length) {
			return null;
		}
		Texture result = new Texture(width, height, alpha);
		ConV.arrayCopy2D(image, x, y, result.image, 0, 0, width, height);
		return result;
	}

	public Texture getSubTextureAlpha(int x, int y, int width, int height) {
		if (x < 0 || y < 0 || width < 0 || height < 0 || y + height > image.length || x + width > image[0].length) {
			return null;
		}
		Texture result = new Texture(width, height, NO_ALPHA);
		ConV.arrayCopy2D(image, x, y, result.image, 0, 0, width, height);
		S: switch (alpha) {
			case SIMPLE_ALPHA: {
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						int a = result.image[i][j] >>> 24;
						if (a == 0x00) {
							result.alpha = SIMPLE_ALPHA;
							break S;
						}
					}
				}
			}
			case COMPLEX_ALPHA: {
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						int a = result.image[i][j] >>> 24;
						if (a == 0x00) {
							result.alpha = SIMPLE_ALPHA;
						} else if (a != 0xFF) {
							result.alpha = COMPLEX_ALPHA;
							break S;
						}
					}
				}
			}
		}
		return result;
	}
}
