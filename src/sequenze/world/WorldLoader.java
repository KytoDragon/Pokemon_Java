package sequenze.world;

import util.ConV;
import util.Logger;

import java.text.ParseException;

import script.Code;
import sequenze.Condition;
import sequenze.ConditionFlag;
import sequenze.ConditionSwitch;
import sequenze.ConditionVariable;
import sequenze.Direction;
import sequenze.Event;
import sequenze.GraphicHandler;
import sequenze.Moveroute;
import sequenze.Script;
import sequenze.TerrainTag;
import util.FileReader;

public class WorldLoader {

	private static final char[] MAPPATH = "lib/mapdata/".toCharArray();
	private static final char[] WORLDPATH = "lib/worlds/".toCharArray();

	public static int[] getMapTextureSet(char[] map_name) {
		// TODO Auto-generated method stub
		int[] tmp = {0}; // texture sets for the given map
		return tmp;
	}

	public static int[][] loadMapPart(char[] map_name, int layer) {
		char[] name = ConV.concat(map_name, "/".toCharArray(), map_name, "_".toCharArray(), ConV.toCString(layer), ".lay".toCharArray());
		FileReader fr = new FileReader(ConV.concat(MAPPATH, name));
		if (!fr.canRead()) {
			Logger.add(Logger.FILE, "Could not open map file ", name);
			return new int[0][0];
		}
		int[][] result;
		try {
			char[] cy = fr.readCLine();
			char[] cx = fr.readCLine();
			if (!ConV.isInteger(cx) || !ConV.isInteger(cy)) {
				Logger.add(Logger.MAP, "Non-numeric value at lines 1-2 in map file ", name);
				return new int[0][0];
			}
			int x = ConV.parseInteger(cx);
			int y = ConV.parseInteger(cy);
			if (y < 0 || x < 0) {
				Logger.add(Logger.MAP, "Negativ value at lines 1-2 in map file ", name);
				return new int[0][0];
			}
			result = new int[y][x];
			for (int i = 0; i < y; i++) {
				char[] zeile = fr.readCLine();
				if (zeile == null) {
					Logger.add(Logger.MAP, "Missing lines stating at line ", i + 2, " in map file ", name);
					for (; i < y; i++) {
						for (int j = 0; j < x; j++) {
							result[i][j] = -1;
						}
					}
					break;
				}
				char[][] tiles = ConV.split(zeile, ';');
				if (tiles.length != x) {
					Logger.add(Logger.MAP, "Line ", i + 2, " in map file ", name, " does not contain the right number ef entries");
					for (int j = 0; j < x; j++) {
						result[i][j] = -1;
					}
					continue;
				}
				for (int j = 0; j < x; j++) {
					if (!ConV.isInteger(tiles[j])) {
						result[i][j] = -1;
					} else {
						result[i][j] = ConV.parseInteger(tiles[j]);
						if (result[i][j] < -1) {
							Logger.add(Logger.MAP, "Invalid value at line ", i + 2, " in map file ", name);
							result[i][j] = -1;
						}
					}
				}
			}
		} finally {
			fr.close();
		}
		return result;
	}

	public static TerrainTag[][] getMapBehavs(char[] map_name) {
		char[] name = ConV.concat(map_name, "/".toCharArray(), map_name, "_p.lay".toCharArray());
		FileReader fr = new FileReader(ConV.concat(MAPPATH, name));
		if (!fr.canRead()) {
			Logger.add(Logger.FILE, "Could not open map file ", name);
			return new TerrainTag[0][0];
		}
		TerrainTag[][] result;
		try {
			char[] cy = fr.readCLine();
			char[] cx = fr.readCLine();
			if (!ConV.isInteger(cx) || !ConV.isInteger(cy)) {
				Logger.add(Logger.MAP, "Non-numeric value at lines 1-2 in map file ", name);
				return new TerrainTag[0][0];
			}
			int x = ConV.parseInteger(cx);
			int y = ConV.parseInteger(cy);
			if (y < 0 || x < 0) {
				Logger.add(Logger.MAP, "Negativ value at lines 1-2 in map file ", name);
				return new TerrainTag[0][0];
			}
			result = new TerrainTag[y][x];
			for (int i = 0; i < y; i++) {
				char[] zeile = fr.readCLine();
				if (zeile == null) {
					Logger.add(Logger.MAP, "Missing lines stating at line ", i + 2, " in map file ", name);
					for (; i < y; i++) {
						for (int j = 0; j < x; j++) {
							result[i][j] = TerrainTag.BLOCKED;
						}
					}
					break;
				}
				char[][] tiles = ConV.split(zeile, ';');
				if (tiles.length != x) {
					Logger.add(Logger.MAP, "Line ", i + 2, " in map file ", name, " does not contain the right number ef entries");
					for (int j = 0; j < x; j++) {
						result[i][j] = TerrainTag.BLOCKED;
					}
					continue;
				}
				for (int j = 0; j < x; j++) {
					if (!ConV.isInteger(tiles[j])) {
						Logger.add(Logger.MAP, "Non-numeric value at line ", i + 2, " in map file ", name);
						result[i][j] = TerrainTag.BLOCKED;
					} else {
						result[i][j] = TerrainTag.get(ConV.parseInteger(tiles[j]));
						if (result[i][j] == null) {
							Logger.add(Logger.MAP, "Invalid value at line ", i + 2, " in map file ", name);
							result[i][j] = TerrainTag.BLOCKED;
						}
					}
				}
			}
		} finally {
			fr.close();
		}
		return result;
	}

	public static Event[] getMapEvents(char[] map_name) {
		char[] name = ConV.concat(map_name, "/".toCharArray(), map_name, "_e.lay".toCharArray());
		FileReader fr = new FileReader(ConV.concat(MAPPATH, name));
		if (!fr.canRead()) {
			Logger.add(Logger.FILE, "Could not open map file ", name);
			return new Event[0];
		}
		Event[] result;
		try {
			char[] nc = fr.readCLine();
			if (!ConV.isInteger(nc)) {
				Logger.add(Logger.MAP, "Non-numeric value at line 1 in map file ", name);
				return new Event[0];
			}
			int n = ConV.parseInteger(nc);
			if (n < 0) {
				Logger.add(Logger.MAP, "Negativ value at lines 1 in map file ", name);
				return new Event[0];
			}
			result = new Event[n];
			for (int i = 0; i < n; i++) {
				Event event = new Event();
				try {
					char[][] parts = ConV.split(fr.readCLine(), ';');
					event.name = parts[0];
					event.x = ConV.parseInt(parts[1]);
					event.y = ConV.parseInt(parts[2]);
					event.orientation = Direction.getByID(ConV.parseByte(parts[3]));
					int scriptnumber = ConV.parseInt(parts[4]);
					Script[] scripts = new Script[scriptnumber];
					for (int j = 0; j < scriptnumber; j++) {
						Script script = new Script();
						char[][] sparts = ConV.split(fr.readCLine(), ';');
						int yxc = ConV.parseInt(sparts[0]);
						script.invisible = (yxc & 1) == 1;
						script.moveanimation = (yxc & 2) == 2;
						script.alwaysontop = (yxc & 4) == 4;
						script.fixeddirection = (yxc & 8) == 8;
						script.noclip = (yxc & 16) == 16;
						script.type = ConV.parseByte(sparts[1]);
						if (script.type == Script.MESSAGE) {
							script.message = sparts[2];
						} else if (script.type == Script.EVENTTOUCH) {
							script.range = ConV.parseByte(sparts[2]);
						}
						script.tex = GraphicHandler.giveTextures(sparts[3]);
						int condNr = ConV.parseInt(sparts[4]);
						Condition[] conditions = new Condition[condNr];
						char[][] cparts = ConV.split(fr.readCLine(), ';');
						for (int k = 0; k < condNr; k++) {
							conditions[k] = getCondition(cparts[k]);
							if (conditions[k] == null) {
								return new Event[0];
							}
						}
						script.conditions = conditions;
						int lineNr = ConV.parseInt(sparts[5]);
						boolean isRaw = ConV.parseBoolean(sparts[6]);
						int codeCount = ConV.parseInt(sparts[7]);
						script.code = Code.getCode(lineNr, isRaw, codeCount, fr);
						script.moveType = ConV.parseByte(sparts[8]);
						if (script.moveType == Script.CUSTOM) {
							script.moveroutes = getMoveRoute(fr.readCLine());
						} else if (script.moveType == Script.LOOKAROUND) {
							script.directions = ConV.parseByte(sparts[9]);
							fr.skipLines(1);
						} else {
							fr.skipLines(1);
						}
						scripts[j] = script;
					}
					event.scripts = scripts;
				} catch (NullPointerException | ParseException | IndexOutOfBoundsException e) {
					Logger.add(Logger.MAP, "Corrupted Event, nr. ", i, ", of map ", name, ", message: ", e.getMessage());
					return new Event[0];
				}
				result[i] = event;
			}
		} finally {
			fr.close();
		}
		return result;
	}

	private static Condition getCondition(char[] s) {
		// TODO remove exceptions
		int type = ConV.getInt(s, 0);
		int start = ConV.indexOf(s, ',');
		int nr = ConV.getInt(s, start + 1);
		if (nr < 0) {
			Logger.add(Logger.FILE, "Negativ event condition nr ", nr);
			return null;
		}
		switch (type) {
			case 0:
				return new ConditionSwitch(nr);
			case 1:
				return new ConditionFlag(nr);
			case 2:
				int min = ConV.getInt(s, ConV.indexOf(s, start + 1, s.length, ','));
				if (min < 0) {
					Logger.add(Logger.FILE, "Negativ event condition min ", min);
					return null;
				}
				return new ConditionVariable(nr, min);
		}
		Logger.add(Logger.FILE, "Unknown event condition type ", type);
		return null;
	}

	private static char[][] getMoveRoute(char[] s) {
		char[][] s2 = ConV.split(s, ';');
		for (int i = 0; i < s2.length; i++) {
			s2[i] = Moveroute.getMoveroute(s2[i]);
		}
		return s2;
	}

	public static short[][][] getMapHeight(char[] map_name) {
		// TODO Auto-generated method stub
		short[][][] tmp = {}; // (y)(x)(i) height of the given map
		return tmp;
	}

	public static boolean hasMapHeight(char[] map_name) {
		// TODO Auto-generated method stub
		boolean tmp = false;
		return tmp;
	}

	public static short getMapBaseHeight(char[] map_name) {
		// TODO Auto-generated method stub
		short tmp = 0;
		return tmp;
	}

	static DynamicMap[] getDynamicWorld(char[] world_name, int amount) {
		char[] name = ConV.concat(world_name, ".wld".toCharArray());
		DynamicMap[] result = new DynamicMap[amount];
		FileReader fr = new FileReader(ConV.concat(WORLDPATH, name));
		if (!fr.canRead()) {
			Logger.add(Logger.FILE, "Could not open world file ", name);
			return new DynamicMap[0];
		}
		try {
			for (int i = 0; i < result.length; i++) {
				char[] line = fr.readCLine();
				char[][] parts = ConV.split(line, ';');
				char[] map_name = parts[0];
				int x = ConV.parseInt(parts[1]);
				int y = ConV.parseInt(parts[2]);
				int width = ConV.parseInt(parts[3]);
				int height = ConV.parseInt(parts[4]);
				result[i] = new DynamicMap(map_name, x, y, width, height);
			}
		} catch (ParseException | NullPointerException | IndexOutOfBoundsException e) {
			Logger.add(Logger.MAP, "Corrupted World list: ", name, ", message: ", e.getMessage());
			return new DynamicMap[0];
		} finally {
			fr.close();
		}
		return result;
	}

	static StaticMap[][] getStaticWorld(char[] world_name, int width, int height) {
		char[] name = ConV.concat(world_name, ".wld".toCharArray());
		StaticMap[][] result = new StaticMap[height][width];
		FileReader fr = new FileReader(ConV.concat(WORLDPATH, name));
		if (!fr.canRead()) {
			Logger.add(Logger.FILE, "Could not open world file ", name);
			return new StaticMap[0][0];
		}
		try {
			for (int y = 0; y < height; y++) {
				char[] line = fr.readCLine();
				char[][] parts = ConV.split(line, ';');
				for (int x = 0; x < width; x++) {
					char[] map_name = parts[x];
					result[y][x] = new StaticMap(map_name);
				}
			}
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			Logger.add(Logger.MAP, "Corrupted World list: ", name, ", message: ", e.getMessage());
			return new StaticMap[0][0];
		} finally {
			fr.close();
		}
		return result;
	}
}
