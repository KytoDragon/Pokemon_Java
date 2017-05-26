package mapedit;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SelectedArea {
	
	int startx = 0, starty = 0, width = 0, height = 0;
	boolean isActive = false;
	int[][] field;
	BufferedImage[][] texs;
	BufferedImage[] btexs;
	int lock;
	
	public SelectedArea() {
		
	}
	
	public void setTexs(BufferedImage[][] t, BufferedImage[] b) {
		texs = t;
		btexs = b;
	}
	
	public void setArea(int sx, int sy, int[][] f, int lock) {
		if (f != null) {
			startx = sx;
			starty = sy;
			height = f.length;
			if (height == 0) {
				width = 0;
			} else {
				width = f[0].length;
			}
			field = f;
			isActive = true;
			this.lock = lock;
		}
	}
	
	public boolean isInside(int x, int y) {
		return (x >= startx && x < startx + width && y >= starty && y < starty + height);
	}
	
	public void reset() {
		startx = 0;
		starty = 0;
		width = 0;
		height = 0;
		isActive = false;
		field = null;
	}
	
	public void draw(Graphics2D g, int l) {
		g.setColor(Color.BLACK);
		if (lock != l) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		} else {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		if (isActive && field != null && texs != null && lock == l) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (field[y][x] != -1) {
						if (lock != 4) {
							g.drawImage(texs[field[y][x] / 256][field[y][x] % 256], (startx + x) * 32, (starty + y) * 32, null);
						} else {
							g.drawImage(btexs[field[y][x]], (startx + x) * 32, (starty + y) * 32, null);
						}
					}
				}
			}
		}
		g.drawRect(startx * 32, starty * 32, width * 32 - 1, height * 32 - 1);
	}
	
	public void move(int xdif, int ydif) {
		startx = startx + xdif;
		starty = starty + ydif;
	}
	
}