package mapedit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import util.ConV;
import util.Logger;

public class PermissionLayer {
	
	private int perms[][];
	private BufferedImage[] btexs;
	
	public PermissionLayer(int[][] p) {
		perms = new int[p.length][p[0].length];
		ConV.arrayCopy2D(p, 0, 0, perms, 0, 0, perms[0].length, perms.length);
	}
	
	public int getAt(int x, int y) {
		try {
			return perms[y][x];
		} catch (ArrayIndexOutOfBoundsException ie) {
			return 0;
		}
	}
	
	public void draw(Graphics2D g) {
		for (int y = 0; y < perms.length; y++) {
			for (int x = 0; x < perms[0].length; x++) {
				if (perms[y][x] != -1) {
					g.drawImage(btexs[perms[y][x]], x * 32, y * 32, null);
				}
			}
		}
		g.setColor(Color.WHITE);
	}
	
	public void setAt(int x, int y, int p) {
		perms[y][x] = p;
	}
	
	public void save(String name) {
		if (perms.length == 0 || perms[0].length == 0) {
			return;
		}
		File f = new File(name);
		if (f.exists()) {
			if (new File(name + ".old").exists()) {
				File f2 = new File(name + ".old");
				f2.delete();
			}
			f.renameTo(new File(name + ".old"));
			f = new File(name);
		}
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
		} catch (IOException e) {
			Logger.add(Logger.EDITOR, "Fehler beim Dateierstellen!");
			return;
		}
		try {
			bw.write(perms.length + "\n" + perms[0].length + "\n");
			for (int[] perm : perms) {
				for (int x = 0; x < perms[0].length - 1; x++) {
					bw.write(perm[x] + "");
					bw.write(";");
				}
				bw.write(perm[perms[0].length - 1] + "\n");
			}
		} catch (IOException e) {
			Logger.add(Logger.EDITOR, "Fehler beim Schreiben!");
		}
		try {
			bw.close();
		} catch (IOException e) {
		}
	}
	
	public int height() {
		return perms.length;
	}
	
	public int width() {
		if (perms.length > 0) {
			return perms[0].length;
		}
		return 0;
	}
	
	public void addLeft(int lines) {
		if (width() + lines < 0) {
			perms = new int[perms.length][0];
			return;
		}
		int[][] newf = new int[perms.length][width() + lines];
		for (int y = 0; y < newf.length; y++) {
			for (int i = 0; i < lines; i++) {
				newf[y][i] = 0;
			}
			for (int x = 0; x < perms[0].length && x < newf[0].length; x++) {
				if (lines > 0) {
					newf[y][x + lines] = perms[y][x];
				} else {
					newf[y][x] = perms[y][x - lines];
				}
			}
		}
		perms = newf;
	}
	
	public void addRight(int lines) {
		if (width() + lines < 0) {
			perms = new int[perms.length][0];
			return;
		}
		int[][] newf = new int[perms.length][width() + lines];
		for (int y = 0; y < newf.length; y++) {
			for (int x = 0; x < perms[0].length && x < newf[0].length; x++) {
				newf[y][x] = perms[y][x];
			}
			for (int i = 0; i < lines; i++) {
				newf[y][i + width()] = 0;
			}
		}
		perms = newf;
	}
	
	public void addUp(int lines) {
		if (perms.length + lines < 0) {
			perms = new int[0][width()];
			return;
		}
		int[][] newf = new int[perms.length + lines][width()];
		for (int i = 0; i < lines; i++) {
			for (int x = 0; x < width(); x++) {
				newf[i][x] = 0;
			}
		}
		for (int y = 0; y < perms.length && y < newf.length; y++) {
			for (int x = 0; x < perms[0].length; x++) {
				if (lines > 0) {
					newf[y + lines][x] = perms[y][x];
				} else {
					newf[y][x] = perms[y - lines][x];
				}
			}
		}
		perms = newf;
	}
	
	public void addDown(int lines) {
		if (perms.length + lines < 0) {
			perms = new int[0][width()];
			return;
		}
		int[][] newf = new int[perms.length + lines][width()];
		ConV.arrayCopy2D(perms, 0, 0, newf, 0, 0, perms[0].length, ConV.min(perms.length, newf.length));
		for (int i = 0; i < lines; i++) {
			for (int x = 0; x < width(); x++) {
				newf[i + perms.length][x] = 0;
			}
		}
		perms = newf;
	}
	
	public int[][] getArea(int startx, int starty, int width, int height) {
		int[][] res = new int[height][width];
		for (int y = starty; y < starty + height; y++) {
			for (int x = startx; x < startx + width; x++) {
				res[y - starty][x - startx] = perms[y][x];
			}
		}
		return res;
	}
	
	public void setArea(int startx, int starty, int[][] field) {
		if (field == null || field.length == 0) {
			return;
		}
		int endx = Math.min(startx + field[0].length, width());
		int endy = Math.min(starty + field.length, height());
		for (int y = starty; y < endy; y++) {
			for (int x = startx; x < endx; x++) {
				perms[y][x] = field[y - starty][x - startx];
			}
		}
	}
	
	public void setTexs(BufferedImage[] btexs) {
		this.btexs = btexs;
	}
}
