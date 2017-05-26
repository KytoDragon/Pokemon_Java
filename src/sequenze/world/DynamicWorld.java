package sequenze.world;

import sequenze.Event;
import sequenze.GraphicHandler;
import sequenze.TerrainTag;

public class DynamicWorld extends World {
	
	DynamicMap[] maps;
	
	public DynamicWorld(char[] name, int amount) {
		this.name = name;
		maps = WorldLoader.getDynamicWorld(name, amount);
	}
	
	@Override
	public int[] toWorldCoord(int x, int y, int mapId) {
		return new int[] { x + maps[mapId].startx, y + maps[mapId].starty };
	}
	
	int[] toMapCoord(int x, int y, int mapId) {
		return new int[] { x - maps[mapId].startx, y - maps[mapId].starty };
	}
	
	@Override
	public int[] toMapCoord(int x, int y) {
		for (int i = 0; i < maps.length; i++) {
			if (isInMap(x, y, i)) {
				return toMapCoord(x, y, i);
			}
		}
		return new int[] { x, y };
	}
	
	@Override
	public boolean isInMap(int x, int y, int index) {
		return (x >= maps[index].startx && x < maps[index].startx + maps[index].width && y >= maps[index].starty && y < maps[index].starty + maps[index].height);
	}
	
	@Override
	public void drawLDown(int xf, int yf) {
		int x = (xf + 8) / 16;
		int y = (yf + 16) / 16;
		for (int i = 0; i < maps.length; i++) {
			if (x + 12 >= maps[i].startx && x < maps[i].startx + maps[i].width + 12 && y + 12 >= maps[i].starty && y < maps[i].starty + maps[i].height + 12) {
				if (!maps[i].isLoaded()) {
					loadMap(i);
				}
				GraphicHandler.translate(maps[i].startx * 16, maps[i].starty * 16);
				maps[i].drawLDown(xf - maps[i].startx * 16, yf - maps[i].starty * 16);
				GraphicHandler.translate(-maps[i].startx * 16, -maps[i].starty * 16);
			}
		}
	}
	
	@Override
	public void drawLUp(int xf, int yf) {
		int x = xf / 16;
		int y = yf / 16;
        for (DynamicMap map : maps) {
            if (x + 12 > map.startx && x < map.startx + map.width + 12 && y + 12 > map.starty && y < map.starty + map.height + 12) {
                GraphicHandler.translate(map.startx * 16, map.starty * 16);
                map.drawLUp(xf - map.startx * 16, yf - map.starty * 16);
                GraphicHandler.translate(-map.startx * 16, -map.starty * 16);
            }
        }
	}
	
	@Override
	public TerrainTag getBAt(int x, int y) {
		int tmpx, tmpy;
		for (int i = 0; i < maps.length; i++) {
			tmpx = x - maps[i].startx;
			tmpy = y - maps[i].starty;
			if (tmpy >= 0 && tmpy < maps[i].height && tmpx >= 0 && tmpx < maps[i].width) {
				if (!maps[i].isLoaded()) {
					loadMap(i);
				}
				return maps[i].getBAt(tmpx, tmpy);
			}
		}
		return TerrainTag.FREE;
	}
	
	@Override
	public short[] getHAt(int x, int y) {
		int tmpx, tmpy;
		for (int i = 0; i < maps.length; i++) {
			tmpx = x - maps[i].startx;
			tmpy = y - maps[i].starty;
			if (tmpy >= 0 && tmpy < maps[i].height && tmpx >= 0 && tmpx < maps[i].width) {
				if (!maps[i].isLoaded()) {
					loadMap(i);
				}
				return maps[i].getHAt(tmpx, tmpy);
			}
		}
		return new short[] { 0 };
	}
	
	@Override
	public Event[] getEvents(int x, int y) {
		int tmpx, tmpy;
		for (int i = 0; i < maps.length; i++) {
			tmpx = x - maps[i].startx;
			tmpy = y - maps[i].starty;
			if (tmpy >= 0 && tmpy < maps[i].height && tmpx >= 0 && tmpx < maps[i].width) {
				if (!maps[i].isLoaded()) {
					loadMap(i);
				}
				return maps[i].getEvents();
			}
		}
		return new Event[0];
	}
	
	void loadMap(int index) {
		maps[index].load();
	}
	
	@Override
	public boolean isOutsideMatrix(int x, int y){
		if (x < 0 || y < 0){
			return true;
		}
		for (DynamicMap map : maps) {
			if(x >= map.startx && y >= map.starty && x - map.startx < map.width && y - map.starty < map.height){
				return false;
			}
		}
		return true;
	}

	@Override
	public void unloadAll() {
		for (DynamicMap map : maps) {
			if(map.isLoaded()){
				map.unload();
			}
		}
	}
}
