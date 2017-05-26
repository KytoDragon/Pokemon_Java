package image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import util.Logger;

public class ImageWriter {
	
	public static void storeTexture(Texture texture, String filename){
		BufferedImage img = new BufferedImage(texture.getWidth(), texture.getWidth(), BufferedImage.TYPE_INT_ARGB);
		int width = img.getWidth();
		int height = img.getHeight();
		int[][] imgData = texture.image;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				img.setRGB(x, y, imgData[y][x]);
			}
		}
		storeImage(img, filename);
	}
	
	public static void storeImage(BufferedImage img, String filename){
		try {
			if(!ImageIO.write(img, "png", new File(filename))){
				throw new IOException("No appropriate image writer found.");
			}
		} catch (IOException ioe) {
			Logger.add(Logger.FILE, "Could not write texture ", filename, ": ", ioe.getMessage());
		}
	}
}
