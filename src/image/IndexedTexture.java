package image;

import util.ConV;
import util.Logger;

public class IndexedTexture{
	
	public byte[][] image;
	
	public IndexedTexture(int width, int height) {
		image = new byte[height][width];
	}

	public IndexedTexture(Texture sub, int[] palette) {
		image = new byte[sub.getHeight()][sub.getWidth()];
		for (int y = 0; y < image.length; y++) {
			L: for (int x = 0; x < image[0].length; x++) {
				for (int i = 0; i < palette.length; i++) {
					if(sub.image[y][x] == palette[i]){
						image[y][x] = (byte) i;
						continue L;
					}
				}
				Logger.add(Logger.GRAPHICS, "Unable to find color in palette");
				return;
			}
		}
	}
	
	public int getHeight() {
		return image.length;
	}
	
	public int getWidth() {
		return image[0].length;
	}
	
	public IndexedTexture getSubTexture(int x, int y, int width, int height){
		if(x < 0 || y < 0 || width < 0 || height < 0 || y + height > image.length || x + width > image[0].length){
			return null;
		}
		IndexedTexture result = new IndexedTexture(width, height);
		ConV.arrayCopy2D(image, x, y, result.image, 0, 0, width, height);
		return result;
	}
	
	public IndexedTexture getSubTextureAlpha(int x, int y, int width, int height){
		if(x < 0 || y < 0 || width < 0 || height < 0 || y + height > image.length || x + width > image[0].length){
			return null;
		}
		IndexedTexture result = new IndexedTexture(width, height);
		ConV.arrayCopy2D(image, x, y, result.image, 0, 0, width, height);
		return result;
	}
}
