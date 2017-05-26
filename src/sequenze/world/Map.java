package sequenze.world;

import sequenze.Event;
import sequenze.GraphicHandler;
import sequenze.TerrainTag;
import util.ConV;

public class Map {
	
	char[] name; // id of this map
	int[/* texture set id */][/* texture id */] tex; // texture id list
	int[/* number of texture sets */] texIds; // texture set id list
	TerrainTag[/* y */][/* x */] behav; // behaviour of each tile
	short[/* y */][/* x */][/* nr */] heightmap; // height of each part of each tile
	int[/* y */][/* x */] layerbottom; // texture id of each tile, bottom part, drawn below
	int[/* y */][/* x */] layermiddle; // texture id of each tile, middle part, drawn below
	int[/* y */][/* x */] layertop; // texture id of each tile, top part, drawn above
	Event[/* number of events */] events; // list of events on the map
	boolean hasHeight; // whether the map has height differences
	short baseHeight; // the base height of the map
	
	/** Creates a map and loads its layers. */
	public Map(char[] map_name) {
		name = map_name;
	}
	
	public void load(){
		texIds = WorldLoader.getMapTextureSet(name);
		tex = new int[texIds.length][];
		for (int i = 0; i < texIds.length; i++) {
			tex[i] = GraphicHandler.giveTexturesSet(texIds[i]);
		}
		behav = WorldLoader.getMapBehavs(name);
		baseHeight = WorldLoader.getMapBaseHeight(name);
		hasHeight = WorldLoader.hasMapHeight(name);
		if (hasHeight) {
			heightmap = WorldLoader.getMapHeight(name);
		}
		layerbottom = WorldLoader.loadMapPart(name, 0);
		layermiddle = WorldLoader.loadMapPart(name, 1);
		layertop = WorldLoader.loadMapPart(name, 2);
		events = WorldLoader.getMapEvents(name);
	}
	
	public boolean isLoaded(){
		return behav != null;
	}
	
	/** draws the lower layers of the left section. */
	public void drawLDown(int xf, int yf) {
		// if(map_id == 0)
		// System.out.println(xf/16+", "+yf/16);
		int maxY = ConV.max(0, (yf + 16) / 16 - 12);
		int maxX = ConV.max(0, (xf + 8) / 16 - 12);
		int minY = ConV.min((yf + 16) / 16 + 12, layerbottom.length);
		int minX = ConV.min((xf + 8) / 16 + 12, layerbottom.length > 0 ? layerbottom[0].length : 0);
		for (int y = maxY; y < minY; y++) {
			for (int x = maxX; x < minX; x++) {
				if (layerbottom[y][x] != -1) {
					GraphicHandler.drawBox(x * 16, y * 16, tex[layerbottom[y][x] / 256][layerbottom[y][x] % 256]);
				}
			}
		}
		// TODO drawReflection
		for (int y = maxY; y < minY; y++) {
			for (int x = maxX; x < minX; x++) {
				if (layermiddle[y][x] != -1) {
					GraphicHandler.drawBox(x * 16, y * 16, tex[layermiddle[y][x] / 256][layermiddle[y][x] % 256]);
				}
			}
		}
        for (Event event : events) {
            if (event.yf <= yf) {
                event.drawLDown();
            }
        }
	}
	
	/** Draws the upper layers of the left section. */
	public void drawLUp(int xf, int yf) {
        for (Event event : events) {
            if (event.yf > yf) {
                event.drawLDown();
            }
        }
		int maxY = ConV.max(0, (yf + 16) / 16 - 12);
		int maxX = ConV.max(0, (xf + 8) / 16 - 12);
		int minY = ConV.min((yf + 16) / 16 + 12, layerbottom.length);
		int minX = ConV.min((xf + 8) / 16 + 12, layerbottom.length > 0 ? layerbottom[0].length : 0);
		for (int y = maxY; y < minY; y++) {
			for (int x = maxX; x < minX; x++) {
				if (layertop[y][x] != -1) {
					GraphicHandler.drawBox(x * 16, y * 16, tex[layertop[y][x] / 256][layertop[y][x] % 256]);
				}
			}
		}
		
        for (Event event : events) {
            event.drawLUp();
        }
	}
	
	/** Returns the array of events on this map. */
	public Event[] getEvents() {
		return events;
	}
	
	/** Returns the behaviour id of the given coordinate. */
	public TerrainTag getBAt(int x, int y) {
		return behav[y][x];
	}
	
	/** Returns the height at the given coordinate. */
	public short[] getHAt(int x, int y) {
		if (!hasHeight) {
			return new short[] { baseHeight };
		}
		return heightmap[y][x];
	}

	public void unload() {
		texIds = null;
		tex = null;
		behav = null;
		heightmap = null;
		layerbottom = null;
		layermiddle = null;
		layertop = null;
		events = null;
	}
	
}
