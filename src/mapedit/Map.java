package mapedit;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import util.ConV;

public class Map extends JPanel implements MouseListener, MouseMotionListener {
	
	String map_name; // id of this map
	BufferedImage[/* texture set id */][/* texture id */] tex; // texture id list
	BufferedImage[/* texture id */] btex; // texture id list
	BufferedImage[/* texture id */] etex; // texture id list
	int[/* number of texture sets */] texIds; // texture set id list
	int[/* y */][/* x */] layerbottom; // texture id of each tile, bottom part, drawn below
	int[/* y */][/* x */] layermiddle; // texture id of each tile, middle part, drawn below
	int[/* y */][/* x */] layertop; // texture id of each tile, top part, drawn above
	int[/* y */][/* x */] behav; // behaviour of each tile
	short[/* y */][/* x */][/* nr */] heightmap; // height of each part of each tile
	Event[/* number of events */] events; // list of events on the map
	boolean hasHeight; // whether the map has height differences
	short baseHeight; // the base height of the map
	boolean is_static;
	
	int lock;
	Editor e;
	EventEdit ev;
	SelectedArea sa;
	int lastx;
	int lasty;
	
	/** Creates a map and loads its layers. */
	public Map(String name) {
		map_name = name;
		texIds = WorldLoader.getMapTextureSet(map_name);
		behav = WorldLoader.getMapBehavs(map_name, 0);
		baseHeight = WorldLoader.getMapBaseHeight(map_name);
		hasHeight = WorldLoader.hasMapHeight(map_name);
		if (hasHeight) {
			heightmap = WorldLoader.getMapHeight(map_name);
		}
		layerbottom = WorldLoader.loadMapPart(map_name, 0, 0);
		layermiddle = WorldLoader.loadMapPart(map_name, 1, -1);
		layertop = WorldLoader.loadMapPart(map_name, 2, -1);
		events = WorldLoader.getMapEvents(map_name);
		sa = new SelectedArea();
		this.setPreferredSize(new Dimension(behav.length > 0 ? behav[0].length * 32 : 0, behav.length * 32));
	}
	
	public Map(String name, int width, int height) {
		map_name = name;
		texIds = new int[] { 0 };
		behav = new int[height][width];
		baseHeight = 0;
		hasHeight = false;
		layerbottom = new int[height][width];
		layermiddle = new int[height][width];
		layertop = new int[height][width];
		for (int i = 0; i < layermiddle.length; i++) {
			for (int j = 0; j < layermiddle[0].length; j++) {
				layermiddle[i][j] = -1;
				layertop[i][j] = -1;
			}
		}
		events = new Event[0];
		sa = new SelectedArea();
		this.setPreferredSize(new Dimension(width * 32, height * 32));
	}
	
	/** draws the lower layers of the left section. */
	@Override
	public void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		if (lock == 4) {
			for (int y = 0; y < behav.length; y++) {
				for (int x = 0; x < behav[0].length; x++) {
					g.drawImage(btex[behav[y][x]], x * 32, y * 32, 32, 32, null);
				}
			}
			sa.draw(g, lock);
		}
		if (lock != 0 && lock != 1) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		} else {
			g.setColor(Color.MAGENTA);
			g.fillRect(0, 0, layerbottom.length * 32, layerbottom.length > 0 ? layerbottom[0].length * 32 : 0);
		}
		for (int y = 0; y < layerbottom.length; y++) {
			for (int x = 0; x < layerbottom[0].length; x++) {
				if (layerbottom[y][x] != -1) {
					g.drawImage(tex[layerbottom[y][x] / 256][layerbottom[y][x] % 256], x * 32, y * 32, 32, 32, null);
				}
			}
		}
		if (lock != 0 && lock != 2) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		} else {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		for (int y = 0; y < layermiddle.length; y++) {
			for (int x = 0; x < layermiddle[0].length; x++) {
				if (layermiddle[y][x] != -1) {
					g.drawImage(tex[layermiddle[y][x] / 256][layermiddle[y][x] % 256], x * 32, y * 32, 32, 32, null);
				}
			}
		}
		if (lock != 0 && lock != 5) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		} else {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		for (Event event : events) {
			event.draw(g, etex);
		}
		if (lock != 0 && lock != 3) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		} else {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		for (int y = 0; y < layertop.length; y++) {
			for (int x = 0; x < layertop[0].length; x++) {
				if (layertop[y][x] != -1) {
					g.drawImage(tex[layertop[y][x] / 256][layertop[y][x] % 256], x * 32, y * 32, 32, 32, null);
				}
			}
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		sa.draw(g, lock);
	}
	
	public void setEditor(Editor editor) {
		e = editor;
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (lock == 0) {
			return;
		}
		int x = arg0.getX() / 32;
		int y = arg0.getY() / 32;
		if (y < 0 || x < 0 || y >= layerbottom.length || x >= layerbottom[0].length) {
			return;
		}
		if (SwingUtilities.isLeftMouseButton(arg0)) {
			if (x == lastx && y == lasty) {
				return;
			}
			if (sa.isInside(lastx, lasty)) {
				sa.move(x - lastx, y - lasty);
			} else if (lock == 1) {
				layerbottom[y][x] = e.getImgi();
			} else if (lock == 2) {
				layermiddle[y][x] = e.getImgi();
			} else if (lock == 3) {
				layertop[y][x] = e.getImgi();
			} else if (lock == 4) {
				int tmp = e.getImgi();
				if (tmp == -1) {
					behav[y][x] = 0;
				} else {
					behav[y][x] = tmp;
				}
			} else if (lock == 5) {
				for (Event event : events) {
					if (event.x == lastx && event.y == lasty) {
						event.x = x;
						event.y = y;
						ev.updatePosition();
						break;
					}
				}
			}
			lastx = x;
			lasty = y;
		} else if (SwingUtilities.isRightMouseButton(arg0)) {
			int[][] f = new int[ConV.abs(y - lasty) + 1][ConV.abs(x - lastx) + 1];
			if (lock == 1) {
				for (int i = 0; i < f.length; i++) {
					for (int j = 0; j < f[0].length; j++) {
						f[i][j] = layerbottom[Math.min(y, lasty) + i][Math.min(x, lastx) + j];
					}
				}
			} else if (lock == 2) {
				for (int i = 0; i < f.length; i++) {
					for (int j = 0; j < f[0].length; j++) {
						f[i][j] = layermiddle[Math.min(y, lasty) + i][Math.min(x, lastx) + j];
					}
				}
			} else if (lock == 3) {
				for (int i = 0; i < f.length; i++) {
					for (int j = 0; j < f[0].length; j++) {
						f[i][j] = layertop[Math.min(y, lasty) + i][Math.min(x, lastx) + j];
					}
				}
			} else if (lock == 4) {
				for (int i = 0; i < f.length; i++) {
					for (int j = 0; j < f[0].length; j++) {
						f[i][j] = behav[Math.min(y, lasty) + i][Math.min(x, lastx) + j];
					}
				}
			}
			sa.setArea(Math.min(x, lastx), Math.min(y, lasty), f, lock);
		}
		e.repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		if (lock == 0) {
			return;
		}
		int x = arg0.getX() / 32;
		int y = arg0.getY() / 32;
		if (y < 0 || x < 0 || y >= layerbottom.length || x >= layerbottom[0].length) {
			return;
		}
		lastx = x;
		lasty = y;
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			if (sa.isActive) {
				if (sa.isInside(x, y)) {
					return;
				} else {
					sa.reset();
				}
			}
			if (lock == 1) {
				layerbottom[y][x] = e.getImgi();
			} else if (lock == 2) {
				layermiddle[y][x] = e.getImgi();
			} else if (lock == 3) {
				layertop[y][x] = e.getImgi();
			} else if (lock == 4) {
				int tmp = e.getImgi();
				if (tmp == -1) {
					behav[y][x] = 0;
				} else {
					behav[y][x] = tmp;
				}
			} else if (lock == 5) {
				int i;
				for (i = 0; i < events.length; i++) {
					if (events[i].x == x && events[i].y == y) {
						e.setEvent(events[i]);
						break;
					}
				}
				if (i == events.length) {
					e.setEvent(null);
				}
			}
		} else if (arg0.getButton() == MouseEvent.BUTTON3) {
			int[][] f = new int[1][1];
			if (lock == 1) {
				f[0][0] = layerbottom[y][x];
			} else if (lock == 2) {
				f[0][0] = layermiddle[y][x];
			} else if (lock == 3) {
				f[0][0] = layertop[y][x];
			} else if (lock == 4) {
				f[0][0] = behav[y][x];
			}
			sa.setArea(x, y, f, lock);
		} else if (arg0.getButton() == MouseEvent.BUTTON2) {
			if (sa.isInside(x, y) && sa.lock == lock) {
				int[][] f = sa.field;
				if (lock == 1) {
					for (int i = 0; i < f.length; i++) {
						ConV.arrayCopy(f[i], 0, layerbottom[sa.starty + i], sa.startx, f[0].length);
					}
				} else if (lock == 2) {
					for (int i = 0; i < f.length; i++) {
						ConV.arrayCopy(f[i], 0, layermiddle[sa.starty + i], sa.startx, f[0].length);
					}
				} else if (lock == 3) {
					for (int i = 0; i < f.length; i++) {
						ConV.arrayCopy(f[i], 0, layertop[sa.starty + i], sa.startx, f[0].length);
					}
				} else if (lock == 4) {
					for (int i = 0; i < f.length; i++) {
						ConV.arrayCopy(f[i], 0, behav[sa.starty + i], sa.startx, f[0].length);
					}
				}
				sa.reset();
			}
		}
		e.repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	public void setTexs(BufferedImage[][] texs, BufferedImage[] btexs, BufferedImage[] etexs) {
		tex = texs;
		btex = btexs;
		etex = etexs;
		sa.setTexs(texs, btexs);
	}
	
	public void setLock(int layer) {
		lock = layer;
	}
	
	public void saveMap() {
		WorldLoader.saveMapPart(map_name, 0, layerbottom);
		WorldLoader.saveMapPart(map_name, 1, layermiddle);
		WorldLoader.saveMapPart(map_name, 2, layertop);
		WorldLoader.saveMapBehav(map_name, behav);
		WorldLoader.saveMapEvents(map_name, events);
	}
	
	public void addUp(int lines) {
		if(is_static){
			return;
		}
		int p_lines = lines > 0 ? lines : 0;
		int n_lines = lines > 0 ? 0 : lines;
		int height = behav.length;
		int width = height == 0 ? 0 : behav[0].length;
		int[][] new_layerbottom = new int[height + lines][];
		int[][] new_layermiddle = new int[height + lines][];
		int[][] new_layertop = new int[height + lines][];
		int[][] new_behav = new int[height + lines][];

		ConV.arrayCopy(layerbottom, 0, new_layerbottom, 0, layerbottom.length - n_lines);
		ConV.arrayCopy(layermiddle, 0, new_layermiddle, 0, layermiddle.length - n_lines);
		ConV.arrayCopy(layertop, 0, new_layertop, 0, layertop.length - n_lines);
		ConV.arrayCopy(behav, 0, new_behav, 0, behav.length - n_lines);
		
		for(int i = height; i < height + lines; i++){
			layerbottom[i] = new int[width];
			layermiddle[i] = new int[width];
			layertop[i] = new int[width];
			behav[i] = new int[width];
			
			Arrays.fill(layerbottom[i], 0);
			Arrays.fill(layermiddle[i], -1);
			Arrays.fill(layertop[i], -1);
			Arrays.fill(behav[i], 0);
		}
		layerbottom = new_layerbottom;
		layermiddle = new_layermiddle;
		layertop = new_layertop;
		behav = new_behav;
		
		if(hasHeight){
			short[][][] new_heightmap = new short[height + lines][][];
			ConV.arrayCopy(heightmap, 0, new_heightmap, 0, heightmap.length - n_lines);
			for(int i = height; i < height + lines; i++){
				heightmap[i] = new short[width][1];
				for (int j = 0; j < width; j++) {
					heightmap[i][j] = new short[]{baseHeight};
				}
			}
			heightmap = new_heightmap;
		}
	}
	
	public void addDown(int lines) {
		if(is_static){
			return;
		}
		int p_lines = lines > 0 ? lines : 0;
		int n_lines = lines > 0 ? 0 : lines;
		int height = behav.length;
		int width = height == 0 ? 0 : behav[0].length;
		int[][] new_layerbottom = new int[height + lines][];
		int[][] new_layermiddle = new int[height + lines][];
		int[][] new_layertop = new int[height + lines][];
		int[][] new_behav = new int[height + lines][];

		ConV.arrayCopy(layerbottom, n_lines, new_layerbottom, p_lines, layerbottom.length);
		ConV.arrayCopy(layermiddle, n_lines, new_layermiddle, p_lines, layermiddle.length);
		ConV.arrayCopy(layertop, n_lines, new_layertop, p_lines, layertop.length);
		ConV.arrayCopy(behav, n_lines, new_behav, p_lines, behav.length);
		
		for(int i = 0; i < lines; i++){
			layerbottom[i] = new int[width];
			layermiddle[i] = new int[width];
			layertop[i] = new int[width];
			behav[i] = new int[width];
			
			Arrays.fill(layerbottom[i], 0);
			Arrays.fill(layermiddle[i], -1);
			Arrays.fill(layertop[i], -1);
			Arrays.fill(behav[i], 0);
		}
		layerbottom = new_layerbottom;
		layermiddle = new_layermiddle;
		layertop = new_layertop;
		behav = new_behav;
		
		if(hasHeight){
			short[][][] new_heightmap = new short[height + lines][][];
			ConV.arrayCopy(heightmap, n_lines, new_heightmap, p_lines, heightmap.length);
			for(int i = 0; i < lines; i++){
				heightmap[i] = new short[width][1];
				for (int j = 0; j < width; j++) {
					heightmap[i][j] = new short[]{baseHeight};
				}
			}
			heightmap = new_heightmap;
		}
		
		for (Event event : events) {
			event.y += lines;
		}
	}
	
	public void addLeft(int lines) {
		// TODO Auto-generated method stub
		
	}
	
	public void addRight(int lines) {
		// TODO Auto-generated method stub
		
	}
	
}
