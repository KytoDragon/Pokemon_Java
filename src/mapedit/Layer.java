package mapedit;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import util.ConV;
import util.Logger;

public class Layer {

	private int[][] feld;
	private BufferedImage[] texs = null;

	public Layer(int[][] f) {
		feld = f;
	}

	public void setTexs(BufferedImage[] t) {
		texs = t;
	}

	public int[][] getArea(int startx, int starty, int width, int height) {
		int[][] res = new int[height][width];
		for (int y = starty; y < starty + height; y++) {
			for (int x = startx; x < startx + width; x++) {
				res[y - starty][x - startx] = feld[y][x];
			}
		}
		return res;
	}

	public void draw(Graphics g) {
		if (texs != null) {
			for (int y = 0; y < feld.length; y++) {
				for (int x = 0; x < feld[0].length; x++) {
					if (feld[y][x] != -1) {
						g.drawImage(texs[feld[y][x]], x * 32, y * 32, null);
					}
				}
			}
		}
	}

	public void setAt(int x, int y, int i) {
		feld[y][x] = i;
	}

	public int height() {
		return feld.length;
	}

	public int width() {
		if (feld.length > 0) {
			return feld[0].length;
		}
		return 0;
	}

	public void addLeft(int lines) {
		if (width() + lines < 0) {
			feld = new int[feld.length][0];
			return;
		}
		int[][] newf = new int[feld.length][width() + lines];
		for (int y = 0; y < newf.length; y++) {
			for (int i = 0; i < lines; i++) {
				newf[y][i] = -1;
			}
			for (int x = 0; x < feld[0].length && x < newf[0].length; x++) {
				if (lines > 0) {
					newf[y][x + lines] = feld[y][x];
				} else {
					newf[y][x] = feld[y][x - lines];
				}
			}
		}
		feld = newf;
	}

	public void addRight(int lines) {
		if (width() + lines < 0) {
			feld = new int[feld.length][0];
			return;
		}
		int[][] newf = new int[feld.length][width() + lines];
		for (int y = 0; y < newf.length; y++) {
			for (int x = 0; x < feld[0].length && x < newf[0].length; x++) {
				newf[y][x] = feld[y][x];
			}
			for (int i = 0; i < lines; i++) {
				newf[y][i + width()] = -1;
			}
		}
		feld = newf;
	}

	public void addUp(int lines) {
		if (feld.length + lines < 0) {
			feld = new int[0][width()];
			return;
		}
		int[][] newf = new int[feld.length + lines][width()];
		for (int i = 0; i < lines; i++) {
			for (int x = 0; x < width(); x++) {
				newf[i][x] = -1;
			}
		}
		for (int y = 0; y < feld.length && y < newf.length; y++) {
			for (int x = 0; x < feld[0].length; x++) {
				if (lines > 0) {
					newf[y + lines][x] = feld[y][x];
				} else {
					newf[y][x] = feld[y - lines][x];
				}
			}
		}
		feld = newf;
	}

	public void addDown(int lines) {
		if (feld.length + lines < 0) {
			feld = new int[0][width()];
			return;
		}
		int[][] newf = new int[feld.length + lines][width()];
		ConV.arrayCopy2D(feld, 0, 0, newf, 0, 0, feld[0].length, ConV.min(feld.length, newf.length));
		for (int i = 0; i < lines; i++) {
			for (int x = 0; x < width(); x++) {
				newf[i + feld.length][x] = -1;
			}
		}
		feld = newf;
	}

	public void save(String name) throws Exception {
		if (feld.length == 0 || feld[0].length == 0) {
			Logger.add(Logger.EDITOR, "Nothing to save ...");
			return;
		}
		File f = new File(name);
		if (f.exists()) {
			f.renameTo(new File(name + ".old"));
			f = new File(name);
		}
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
		} catch (FileNotFoundException e) {
			Logger.add(Logger.EDITOR, "Fehler beim Dateierstellen!");
			return;
		}
		try {
			bw.write(feld.length + "\n" + feld[0].length + "\n");
			for (int[] feld1 : feld) {
				for (int x = 0; x < feld[0].length - 1; x++) {
					bw.write(feld1[x] + "");
					bw.write(";");
				}
				bw.write(feld1[feld[0].length - 1] + "\n");
			}
		} catch (IOException e) {
			Logger.add(Logger.EDITOR, "Fahler beim Schreiben!");
		}
		try {
			bw.close();
		} catch (IOException e) {
		}
	}

	public void setArea(int startx, int starty, int[][] field) {
		if (field == null || field.length == 0) {
			return;
		}
		int endx = Math.min(startx + field[0].length, width());
		int endy = Math.min(starty + field.length, height());
		for (int y = starty; y < endy; y++) {
			for (int x = startx; x < endx; x++) {
				if (field[y - starty][x - startx] != -1) {
					feld[y][x] = field[y - starty][x - startx];
				}
			}
		}
	}
}
