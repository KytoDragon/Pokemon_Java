package sequenze.awt;

import image.IndexedTexture;
import image.Texture;
import util.ConV;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

class Screen {
	
	private int[][] data;
	
	private int width;
	private int total_width;
	private static final int wpadding = 20;
	private int height;
	private int total_height;
	private static final int hpadding = 20;
	private BufferedImage image;
	private int[] image_raster;
	
	public Screen(int width, int height) {
		this.width = width;
		total_width = width + 2 * wpadding;
		this.height = height;
		total_height = height + 2 * hpadding;
		data = new int[total_height][total_width];
		image_raster = new int[height * width];
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	public void drawTo(Graphics g, int xOffset, int yOffset, int modifier) {
		WritableRaster raster = image.getRaster();
		for (int y = 0; y < height; y++) {
			ConV.arrayCopy(data[y + hpadding], wpadding, image_raster, y * width, width);
		}
		raster.setDataElements(0, 0, width, height, image_raster);
		g.drawImage(image, xOffset, yOffset, width * modifier, height * modifier, null);
	}
	
	public void clear() {
		for (int y = 0; y < total_height; y++) {
			for (int x = 0; x < total_width; x++) {
				data[y][x] = 0;
			}
		}
	}
	
	public void drawTexture(Texture texture, int x, int y) {
		if (x + texture.getWidth() < -wpadding || y + texture.getHeight()< -hpadding || x >= width + wpadding || y >= height + hpadding){
			return;
		}
		x += wpadding;
		y += hpadding;
		if (texture.alpha == Texture.NO_ALPHA){
			int max_x = ConV.max(0, -x);
			int max_y = ConV.max(0, -y);
			ConV.arrayCopy2D(texture.image, max_x, max_y, data, x + max_x, y + max_y, ConV.min(total_width - x, texture.getWidth()) - max_x, ConV.min(total_height - y, texture.getHeight()) - max_y);
		} else if (texture.alpha == Texture.SIMPLE_ALPHA){
			for (int i = ConV.max(0, -y); i < ConV.min(total_height - y, texture.getHeight());i++) {
				for (int j = ConV.max(0, -x); j < ConV.min(total_width - x, texture.getWidth());j++) {
					int color = texture.image[i][j];
					if ((color & 0xFF000000) != 0) {
						data[y+i][x+j] = color;
					}
				}
			}
		}else{
			for (int i = ConV.max(0, -y); i < ConV.min(total_height - y, texture.getHeight());i++) {
				for (int j = ConV.max(0, -x); j < ConV.min(total_width - x, texture.getWidth());j++) {
					int color = texture.image[i][j];
					int alpha = (color >>> 24);
					if (alpha == 0xFF) {
						data[y+i][x+j] = color;
					} else if(alpha != 0x00){
						// (255 - x) = ~x for u8
						
						int blueSrc = (color & 0xFF) * alpha;
						int greenSrc = ((color >>> 8) & 0xFF) * alpha;
						int redSrc = ((color >>> 16) & 0xFF) * alpha;
						
						alpha = ~alpha & 0xFF;
						
						int blueDst = (data[y+i][x+j] & 0xFF) * alpha;
						int greenDst = ((data[y+i][x+j] >>> 8) & 0xFF) * alpha;
						int redDst = ((data[y+i][x+j] >>> 16) & 0xFF) * alpha;


						redDst = (redSrc + redDst) / 255;
						greenDst = (greenSrc + greenDst) / 255;
						blueDst = (blueSrc + blueDst) / 255;

						data[y+i][x+j] = 0xFF000000 | redDst << 16 | greenDst << 8 | blueDst;
					}
				}
			}
		}
	}
	
	public void drawRect(int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		x += wpadding;
		y += hpadding;
		int color = ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
		width = ConV.min(x + width, total_width);
		height = ConV.min(y + height, total_height);
		x = ConV.max(0, x);
		y = ConV.max(0, y);
		if (alpha == 0xFF) {
			for (int i = y; i < height; i++) {
				for (int j = x; j < width; j++) {
					data[i][j] = color;
				}
			}
		} else if (alpha == 0x00) {
		} else {
			
			red *= alpha;
			green *= alpha;
			blue *= alpha;
			alpha = ~alpha & 0xFF;
			
			for (int i = y; i < height; i++) {
				for (int j = x; j < width; j++) {
					int blueDst = (data[i][j] & 0xFF) * alpha;
					int greenDst = ((data[i][j] >>> 8) & 0xFF) * alpha;
					int redDst = ((data[i][j] >>> 16) & 0xFF) * alpha;
					redDst = (red + redDst) / 255;
					
					greenDst = (green + greenDst) / 255;
					
					blueDst = (blue + blueDst) / 255;
					
					data[i][j] = 0xFF000000 | redDst << 16 | greenDst << 8 | blueDst;
				}
			}
		}

	}
	
	int[][] getData(){
		return data;
	}
	
	public Texture getScreenshot(){
		Texture texture = new Texture(width, height, Texture.NO_ALPHA);
		ConV.arrayCopy2D(data, wpadding, hpadding, texture.image, 0, 0, width, height);
		return texture;
	}
	
	void setData(int[][] data){
		this.data = data;
	}

	void drawPaletteTexture(IndexedTexture texture, int x, int y, int[] palette) {
		if (x + texture.getWidth() < -wpadding || y + texture.getHeight()< -hpadding || x >= width + wpadding || y >= height + hpadding){
			return;
		}
		x += wpadding;
		y += hpadding;
		for (int i = ConV.max(0, -y); i < ConV.min(total_height - y, texture.getHeight());i++) {
			for (int j = ConV.max(0, -x); j < ConV.min(total_width - x, texture.getWidth());j++) {
				int color = palette[texture.image[i][j]];
				int alpha = (color >>> 24);
				if (alpha == 0xFF) {
					data[y+i][x+j] = color;
				} else if(alpha != 0x00){
					int blueSrc = (color & 0xFF) * alpha;
					int greenSrc = ((color >>> 8) & 0xFF) * alpha;
					int redSrc = ((color >>> 16) & 0xFF) * alpha;

					alpha = ~alpha & 0xFF;

					int blueDst = (data[y+i][x+j] & 0xFF) * alpha;
					int greenDst = ((data[y+i][x+j] >>> 8) & 0xFF) * alpha;
					int redDst = ((data[y+i][x+j] >>> 16) & 0xFF) * alpha;


					redDst = (redSrc + redDst) / 255;
					greenDst = (greenSrc + greenDst) / 255;
					blueDst = (blueSrc + blueDst) / 255;

					data[y+i][x+j] = 0xFF000000 | redDst << 16 | greenDst << 8 | blueDst;
				}
			}
		}
	}
}
