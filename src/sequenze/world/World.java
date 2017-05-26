package sequenze.world;

import sequenze.Event;
import sequenze.TerrainTag;
import util.ConV;

public abstract class World {
	
	public char[] name;
	
	public abstract int[] toWorldCoord(int x, int y, int mapId);
	
	public abstract int[] toMapCoord(int x, int y) ;
	
	public abstract boolean isInMap(int x, int y, int mapId) ;
	
	public abstract void drawLDown(int xf, int yf) ;
	
	public abstract void drawLUp(int xf, int yf) ;
	
	public TerrainTag[] getBto(int fromX, int fromY, int toX, int toY) {
		TerrainTag[] result = new TerrainTag[ConV.abs(fromX - toX + fromY - toY) + 1];
		if (fromX == toX) {
			int d = fromY - toY;
			if (fromY > toY) {
				for (int i = 0; i < result.length; i++) {
					result[i] = getBAt(fromX, fromY - i);
				}
			} else {
				for (int i = 0; i < result.length; i++) {
					result[i] = getBAt(fromX, fromY + i);
				}
			}
		} else {
			int d = fromX - toX;
			if (fromX > toX) {
				for (int i = 0; i < result.length; i++) {
					result[i] = getBAt(fromX - i, fromY);
				}
			} else {
				for (int i = 0; i < result.length; i++) {
					result[i] = getBAt(fromX + i, fromY);
				}
			}
		}
		return result;
	}
	
	public abstract TerrainTag getBAt(int x, int y) ;
	
	/** Returns the height at the given coordinate. */
	public abstract short[] getHAt(int x, int y) ;
	
	/** Returns an array of heights around the given coordinates. */
	public short[][] getHAround(int x, int y) {
		short[][] res = new short[5][];
		res[0] = getHAt(x, y + 1);
		res[1] = getHAt(x + 1, y);
		res[2] = getHAt(x, y - 1);
		res[3] = getHAt(x - 1, y);
		res[4] = getHAt(x, y);
		return res;
	}
	
	public abstract Event[] getEvents(int x, int y) ;
	
	/** Returns an array of behaviour ids around the given coordinates. */
	public TerrainTag[] getBAround(int x, int y) {
		TerrainTag[] res = new TerrainTag[5];
		res[0] = getBAt(x, y + 1);
		res[1] = getBAt(x + 1, y);
		res[2] = getBAt(x, y - 1);
		res[3] = getBAt(x - 1, y);
		res[4] = getBAt(x, y);
		return res;
	}
	
	public static World getWorld(char[] name){
		// static
		if(ConV.equals(name, "house1")){
			return new StaticWorld(name, 1, 1);
		}else{
			return new DynamicWorld(name, 2);
		}
	}
	
	public abstract boolean isOutsideMatrix(int x, int y);

	public abstract void unloadAll();
	
}