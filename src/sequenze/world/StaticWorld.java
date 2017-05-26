package sequenze.world;

import sequenze.Event;
import sequenze.GraphicHandler;
import sequenze.TerrainTag;
import static sequenze.world.StaticMap.*;

public class StaticWorld extends World {
	
	StaticMap[][] maps;
	int width;
	int height;
	
	public StaticWorld(char[] name, int width, int height) {
		this.name = name;
		maps = WorldLoader.getStaticWorld(name, width, height);
		this.height = maps.length;
		if(this.height > 0){
			this.width = maps[0].length;
		}else{
			this.width = 0;
		}
	}
	
	@Override
	public int[] toWorldCoord(int x, int y, int index) {
		return new int[] { index % width * TILEWIDTH + x, index / width * TILEHEIGHT + y };
	}
	
	@Override
	public int[] toMapCoord(int x, int y) {
		return new int[] { x % TILEWIDTH, y % TILEHEIGHT };
	}
	
	@Override
	public boolean isInMap(int x, int y, int index) {
		x -= index % width * TILEWIDTH;
		y -= index / width * TILEHEIGHT;
		return x >= 0 && x < TILEWIDTH && y >= 0 && y < TILEHEIGHT;
	}
	
	@Override
	public void drawLDown(int xf, int yf) {
		int x = (xf + 8) / 16;
		int y = (yf + 16) / 16;
		for (int i = 0; i < maps.length; i++) {
			for (int j = 0; j < maps[0].length; j++) {
				if (x + 12 >= j * TILEWIDTH && x < (j + 1) * TILEWIDTH + 12 && y + 12 >= i * TILEHEIGHT && y < (i + 1) * TILEHEIGHT + 12) {
					loadMap(maps[i][j]);
					GraphicHandler.translate(j * TILEWIDTH * 16, i * TILEHEIGHT * 16);
					maps[i][j].drawLDown(xf - j * TILEWIDTH * 16, yf - i * TILEHEIGHT * 16);
					GraphicHandler.translate(-j * TILEWIDTH * 16, -i * TILEHEIGHT * 16);
				}
			}
		}
	}
	
	@Override
	public void drawLUp(int xf, int yf) {
		int x = (xf + 8) / 16;
		int y = (yf + 16) / 16;
		for (int i = 0; i < maps.length; i++) {
			for (int j = 0; j < maps[0].length; j++) {
				if (x + 12 >= j * TILEWIDTH && x < (j + 1) * TILEWIDTH + 12 && y + 12 >= i * TILEHEIGHT && y < (i + 1) * TILEHEIGHT + 12) {
					GraphicHandler.translate(j * TILEWIDTH * 16, i * TILEHEIGHT * 16);
					maps[i][j].drawLUp(xf - j * TILEWIDTH * 16, yf - i * TILEHEIGHT * 16);
					GraphicHandler.translate(-j * TILEWIDTH * 16, -i * TILEHEIGHT * 16);
				}
			}
		}
	}
	
	@Override
	public TerrainTag getBAt(int x, int y) {
		if(isOutsideMatrix(x, y)){
			return TerrainTag.FREE;
		}
		StaticMap map = maps[y / TILEHEIGHT][x / TILEWIDTH];
		loadMap(map);
		return map.getBAt(x % TILEWIDTH, y % TILEHEIGHT);
	}
	
	@Override
	public short[] getHAt(int x, int y) {
		if(isOutsideMatrix(x, y)){
			return new short[] { 0 };
		}
		StaticMap map = maps[y / TILEHEIGHT][x / TILEWIDTH];
		loadMap(map);
		return map.getHAt(y % TILEHEIGHT, x % TILEWIDTH);
	}
	
	void loadMap(StaticMap map) {
		if(!map.isLoaded()){
			map.load();
		}
	}
	
	@Override
	public Event[] getEvents(int x, int y) {
		if(isOutsideMatrix(x, y)){
			return new Event[0];
		}
		StaticMap map = maps[y / TILEHEIGHT][x / TILEWIDTH];
		loadMap(map);
		return map.getEvents();
	}
	
	@Override
	public boolean isOutsideMatrix(int x, int y){
		return x < 0 || y < 0 || x >= width * TILEWIDTH || y >= height * TILEHEIGHT;
	}

	@Override
	public void unloadAll() {
		for (StaticMap[] staticMaps : maps) {
			for (StaticMap map : staticMaps) {
				if(map.isLoaded()){
					map.unload();
				}
			}
		}
	}
	
}
