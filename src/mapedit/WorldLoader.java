package mapedit;

import java.text.ParseException;
import script.ByteCode;
import util.ConV;
import util.FileReader;
import util.FileWriter;
import util.Logger;

public class WorldLoader {

	private static final String WORLDPATH = "lib/mapdata/";

	public static int[] getMapTextureSet(String map_name) {
		// TODO Auto-generated method stub
		int[] tmp = {0}; // texture sets for the given map
		return tmp;
	}

	public static int[][] loadMapPart(String map_name, int layer, int def) {
		String name = map_name + "/" + map_name + "_" + layer + ".lay";
		try (FileReader fr = new FileReader(WORLDPATH + name)) {
			if (!fr.canRead()) {
				return new int[0][0];
			}
			int x, y;
			try {
				y = ConV.parseInt(fr.readCLine());
				x = ConV.parseInt(fr.readCLine());
			} catch (ParseException e) {
				Logger.add(Logger.EDITOR, "Map size for ", map_name, " is not a number");
				return new int[0][0];
			}
			int result[][] = new int[y][x];
			for (int i = 0; i < y; i++) {
				String zeile = fr.readLine();
				if (zeile == null) {
					for (int j = 0; j < x; j++) {
						result[i][j] = def;
					}
					continue;
				}
				String[] tiles = zeile.split(";", -1);
				for (int j = 0; j < x; j++) {
					if (tiles[j].equals("")) {
						result[i][j] = -1;
					} else {
						try {
							result[i][j] = ConV.parseInt(tiles[j]);
						} catch (ParseException e) {
							result[i][j] = 0;
							Logger.add(Logger.EDITOR, "Tilevalue in map ", map_name, " is not a number");
						}
					}
				}
			}
			if (!fr.canRead()) {
				return new int[0][0];
			}
			return result;
		} catch (NullPointerException e) {
			return new int[0][0];
		}
	}

	public static int[][] getMapBehavs(String map_name, int def) {
		String name = map_name + "/" + map_name + "_p.lay";
		try (FileReader fr = new FileReader(WORLDPATH + name)) {
			if (!fr.canRead()) {
				return new int[0][0];
			}
			int x;
			int y;
			try {
				y = ConV.parseInt(fr.readCLine());
				x = ConV.parseInt(fr.readCLine());
			} catch (ParseException e) {
				Logger.add(Logger.EDITOR, "Permission map size for ", map_name, " is not a number");
				return new int[0][0];
			}
			int result[][] = new int[y][x];
			for (int i = 0; i < y; i++) {
				String zeile = fr.readLine();
				if (zeile == null) {
					for (int j = 0; j < x; j++) {
						result[i][j] = def;
					}
					continue;
				}
				String[] tiles = zeile.split(";", -1);
				for (int j = 0; j < x; j++) {
					if (tiles[j].equals("")) {
						result[i][j] = 1;
					} else {
						try {
							result[i][j] = ConV.parseInt(tiles[j]);
						} catch (ParseException e) {
							result[i][j] = 0;
							Logger.add(Logger.EDITOR, "Tilevalue for Permissiond in map ", map_name, " is not a number");
						}
					}
				}
			}
			if (!fr.canRead()) {
				return new int[0][0];
			}
			return result;
		} catch (NullPointerException e) {
			return new int[0][0];
		}
	}

	public static Event[] getMapEvents(String map_name) {
		String name = map_name + "/" + map_name + "_e.lay";
		try (FileReader fr = new FileReader(WORLDPATH + name)) {
			if (!fr.canRead()) {
				return new Event[0];
			}
			String zeile;
			int n;
			try {
				n = ConV.parseInt(fr.readCLine());
			} catch (ParseException e) {
				Logger.add(Logger.EDITOR, "Corrupted Event list of map ", name);
				return new Event[0];
			}
			Event[] result = new Event[n];
			try {
				for (int i = 0; i < n; i++) {
					Event event = new Event();
					zeile = fr.readLine();
					String[] parts = zeile.split(";", -1);
					event.name = parts[0];
					event.x = ConV.parseInt(parts[1]);
					event.y = ConV.parseInt(parts[2]);
					event.orientation = Byte.parseByte(parts[3]);
					int scriptnumber = ConV.parseInt(parts[4]);
					Script[] scripts = new Script[scriptnumber];
					for (int j = 0; j < scriptnumber; j++) {
						Script script = new Script();
						zeile = fr.readLine();
						String[] sparts = zeile.split(";", -1);
						int yxc = ConV.parseInt(sparts[0]);
						script.invisible = (yxc & 1) == 1;
						script.moveanimation = (yxc & 2) == 2;
						script.alwaysontop = (yxc & 4) == 4;
						script.fixeddirection = (yxc & 8) == 8;
						script.noclip = (yxc & 16) == 16;
						script.type = Byte.parseByte(sparts[1]);
						if (script.type == Script.MESSAGE) {
							script.message = sparts[2];
						} else if (script.type == Script.EVENTTOUCH) {
							script.range = Byte.parseByte(sparts[2]);
						}
						script.tex = ConV.parseInt(sparts[3]);
						int condNr = ConV.parseInt(sparts[4]);
						Condition[] conditions = new Condition[condNr];
						zeile = fr.readLine();
						String[] cparts = zeile.split(";");
						for (int k = 0; k < condNr; k++) {
							conditions[k] = getCondition(cparts[k]);
						}
						script.conditions = conditions;
						int lineNr = ConV.parseInt(sparts[5]);
						script.isRaw = Boolean.parseBoolean(sparts[6]);
						int byteCount = ConV.parseInt(sparts[7]);
						String lines = "";
						for (int k = 0; k < lineNr; k++) {
							if(k != 0){
								lines += '\n';
							}
							lines += fr.readLine();
						}
						script.lines = lines;
						if(script.isRaw){
							fr.skipBytes(byteCount);
							script.code = new ByteCode(0, 0, new byte[0]);
						}else{
							script.code = new ByteCode(fr.readByte(), fr.readByte(), fr.readByteArray(byteCount - 2));
						}
						script.moveType = Byte.parseByte(sparts[8]);
						if (script.moveType == Script.CUSTOM) {
							script.moveroutes = getMoveRoute(fr.readLine());
						} else if (script.moveType == Script.LOOKAROUND) {
							script.directions = Byte.parseByte(sparts[9]);
							fr.skipLines(1);
						} else {
							fr.skipLines(1);
						}
						scripts[j] = script;
					}
					event.scripts = scripts;
					result[i] = event;
				}
			} catch (IllegalArgumentException | ParseException e) {
				Logger.add(Logger.EDITOR, "Could not parse events for map ", map_name, ":", e.getMessage());
				return new Event[0];
			}
			if (!fr.canRead()) {
				return new Event[0];
			}
			return result;
		} catch (NullPointerException e) {
			return new Event[0];
		}
	}

	private static Condition getCondition(String s) {
		int type = ConV.getInt(s, 0);
		int start = s.indexOf(',');
		int nr = ConV.getInt(s, start + 1);
		switch (type) {
		case 0:
			return new ConditionSwitch(nr);
		case 1:
			return new ConditionFlag(nr);
		case 2:
			int min = ConV.getInt(s, s.indexOf(',', start + 1));
			return new ConditionVariable(nr, min);
		}
		throw new IllegalArgumentException("Unknown Event Condition " + s);
	}

	private static String[] getMoveRoute(String s) {
		return s.split(",", -1);
	}

	public static short[][][] getMapHeight(String map_name) {
		// TODO Auto-generated method stub
		short[][][] tmp = {}; // (y)(x)(i) height of the given map
		return tmp;
	}

	public static int[][] getMapSizes(int mapId) {
		// TODO Auto-generated method stub
		int[][] tmp = {{0}, {0}, {16}, {16}};
		return tmp;
	}

	public static boolean hasMapHeight(String map_name) {
		// TODO Auto-generated method stub
		boolean tmp = false;
		return tmp;
	}

	public static short getMapBaseHeight(String map_name) {
		// TODO Auto-generated method stub
		short tmp = 0;
		return tmp;
	}

	public static void saveMapPart(String map_name, int layer, int[][] data) {
		String name = map_name + "/" + map_name + "_" + layer + ".lay";
		try (FileWriter fw = new FileWriter(WORLDPATH + name)) {
			if (!fw.canWrite()) {
				return;
			}
			fw.writeInteger(data[0].length);
			fw.newLine();
			fw.writeInteger(data.length);
			for (int[] data1 : data) {
				fw.newLine();
				for (int j = 0; j < data[0].length; j++) {
					if (j > 0) {
						fw.write(';');
					}
					if (data1[j] != -1) {
						fw.writeInteger(data1[j]);
					}
				}
			}
			if (!fw.canWrite()) {
				return;
			}
		}
	}

	public static void saveMapBehav(String map_name, int[][] data) {
		String name = map_name + "/" + map_name + "_p.lay";
		try (FileWriter fw = new FileWriter(WORLDPATH + name)) {
			if (!fw.canWrite()) {
				return;
			}
			fw.writeInteger(data[0].length);
			fw.newLine();
			fw.writeInteger(data.length);
			for (int[] data1 : data) {
				fw.newLine();
				for (int j = 0; j < data[0].length; j++) {
					if (j > 0) {
						fw.write(';');
					}
					fw.writeInteger(data1[j]);
				}
			}
			if (!fw.canWrite()) {
				return;
			}
		}
	}

	public static void saveMapEvents(String map_name, Event[] events) {
		String name = map_name + "/" + map_name + "_e.lay";
		try (FileWriter fw = new FileWriter(WORLDPATH + name)) {
			if (!fw.canWrite()) {
				return;
			}
			fw.writeInteger(events.length);
			for (Event event : events) {
				fw.newLine();
				fw.write(event.name + ";" + event.x + ";" + event.y + ";" + event.orientation + ";" + event.scripts.length);
				for (Script script : event.scripts) {
					fw.newLine();
					int yxc = 0;
					if (script.invisible) {
						yxc += 1;
					}
					if (script.moveanimation) {
						yxc += 2;
					}
					if (script.alwaysontop) {
						yxc += 4;
					}
					if (script.fixeddirection) {
						yxc += 8;
					}
					if (script.noclip) {
						yxc += 16;
					}
					fw.write(yxc + ";" + script.type + ";");
					if (script.type == Script.MESSAGE) {
						fw.write(script.message);
					} else if (script.type == Script.EVENTTOUCH) {
						fw.write(Byte.toString(script.range));
					}
					fw.write(";" + script.tex + ";" + script.conditions.length + ";" + script.lines.split("\n").length + ";" + Boolean.toString(script.isRaw) + ";"
						+ (script.isRaw ? 0 : script.code.code.length + 2) + ";" + script.moveType);
					if (script.moveType == Script.LOOKAROUND) {
						fw.write(";" + script.directions);
					}
					fw.newLine();
					for (int k = 0; k < script.conditions.length; k++) {
						if (k > 0) {
							fw.write(';');
						}
						fw.write(script.conditions[k].getString());
					}
					fw.newLine();
					fw.write(script.lines);
					fw.newLine();
					if (!script.isRaw) {
						fw.writeByte(script.code.vars_num);
						fw.writeByte(script.code.pars_num);
						fw.write(script.code.code);
					}
					if (script.moveType == Script.CUSTOM) {
						fw.write(getMoveRouteString(script.moveroutes));
					}
				}
			}
			if (!fw.canWrite()) {
				return;
			}
		}
	}

	private static String getMoveRouteString(String[] moveroutes) {
		String result = moveroutes[0];
		for (int i = 1; i < moveroutes.length; i++) {
			result = result + ',' + moveroutes[i];
		}
		return result;
	}

}
