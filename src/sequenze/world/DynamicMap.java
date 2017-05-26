package sequenze.world;

public class DynamicMap extends Map {
	
	int startx; // offset to the worlds x coordinate
	int starty; // offset to the worlds y coordinate
	int width; // width of the map
	int height; // height of the map
	
	public DynamicMap(char[] map_name, int x, int y, int width, int height) {
		super(map_name);
		startx = x;
		starty = y;
		this.width = width;
		this.height = height;
	}
	
}
