package image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import util.ConV;
import util.Logger;

public class ImageReader {
	
	public static Texture loadTexture(String filename){
		BufferedImage img;
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException ioe) {
			Logger.add(Logger.FILE, "Could not read texture ", filename, ": ", ioe.getMessage());
			return null;
		}
		int width = img.getWidth();
		int height = img.getHeight();
		Texture texture = new Texture(width, height, Texture.COMPLEX_ALPHA);
		int[] imgData = img.getRGB(0, 0, width, height, null, 0, width);
		for (int y = 0; y < height; y++) {
			ConV.arrayCopy(imgData, y * width, texture.image[y], 0, width);
		}
		return texture;
	}
}
