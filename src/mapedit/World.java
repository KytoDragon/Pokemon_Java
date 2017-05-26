package mapedit;

import java.awt.Graphics;

public class World {
	
	int worldID = 0;
	Map[] maps;
	int startx[]; // offset to the worlds x coordinate
	int starty[]; // offset to the worlds y coordinate
	int width[]; // width of the map
	int height[]; // height of the map
	
	int selected;
	
	public World(int id) {
		worldID = id;
		int[][] sizes = WorldLoader.getMapSizes(id);
		startx = sizes[0];
		starty = sizes[1];
		width = sizes[2];
		height = sizes[3];
		maps = new Map[startx.length];
	}
	
	int[] toWorldCoord(int x, int y, int mapId) {
		return new int[] { x + startx[mapId], y + starty[mapId] };
	}
	
	int[] toMapCoord(int x, int y, int mapId) {
		return new int[] { x - startx[mapId], y - starty[mapId] };
	}
	
	int[] toMapCoord(int x, int y) {
		for (int i = 0; i < maps.length; i++) {
			if (isInMap(x, y, i)) {
				return toMapCoord(x, y, i);
			}
		}
		return null;
	}
	
	boolean isInMap(int x, int y, int mapId) {
		return (x >= startx[mapId] && x < startx[mapId] + width[mapId] && y >= starty[mapId] && y < starty[mapId] + height[mapId]);
	}
	
	void draw(Graphics g) {
		// maps[selected].draw(g);
	}
	
}