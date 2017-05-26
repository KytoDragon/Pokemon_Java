package sequenze;

import java.io.File;
import java.text.ParseException;
import static sequenze.InstancePackage.*;
import util.ConV;
import util.FileReader;
import util.FileWriter;
import util.List;
import util.Logger;

public class SaveSystem {

	public static final String SAVE_PATH = System.getProperty("user.home") + "\\save";

	public static String getScreenshotName() {
		return SAVE_PATH + "\\" + Timer.getTimeString() + ".png";
	}

	static SaveGameInfo[] getSaveGameInfos() {
		File folder = new File(SAVE_PATH);
		File[] files = folder.listFiles();
		List<SaveGameInfo> list = new List<>();
		for (File file : files) {
			if (file.getName().endsWith(".sav")) {
				SaveGameInfo info = loadSaveGameInfo(file.getName());
				if (info != null) {
					list.add(info);
				}
			}
		}
		return list.toArray(new SaveGameInfo[list.size()]);
	}

	private static SaveGameInfo loadSaveGameInfo(String filename) {
		FileReader fr = new FileReader(SAVE_PATH + "\\" + filename);
		if (!fr.canRead()) {
			Logger.add(Logger.FILE, "Could not open save file ", filename);
			return null;
		}
		SaveGameInfo result = new SaveGameInfo();
		try {
			result.name = ConV.subCstring(filename.toCharArray(), 0, filename.length() - 4);
			result.charName = fr.readCLine();
			result.lastPlayed = ConV.parseLong(fr.readCLine());
			result.timePlayed = ConV.parseLong(fr.readCLine());
			result.badges = ConV.parseInt(fr.readCLine());
			result.pokemon = ConV.parseInt(fr.readCLine());
			result.place = fr.readCLine();
			result.money = ConV.parseInt(fr.readCLine());
			result.points = ConV.parseInt(fr.readCLine());
		} catch (ParseException e) {
			Logger.add(Logger.FILE, "Unable to parse number in save fale: ", filename);
			return null;
		} catch (NullPointerException e) {
			Logger.add(Logger.FILE, "Reached end of file in save fale: ", filename);
			return null;
		} finally {
			fr.close();
		}
		return result;
	}

	static void writeSaveGameInfo(SaveGameInfo info) {
		String filename = new String(info.name) + ".sav";
		FileWriter fw = new FileWriter(SAVE_PATH + "\\" + filename);
		if (!fw.canWrite()) {
			Logger.add(Logger.FILE, "Could not open save file ", filename);
			return;
		}
		try {
			fw.write(info.charName);
			fw.newLine();
			fw.writeInteger(info.lastPlayed);
			fw.newLine();
			fw.writeInteger(info.timePlayed);
			fw.newLine();
			fw.writeInteger(info.badges);
			fw.newLine();
			fw.writeInteger(info.pokemon);
			fw.newLine();
			fw.write(info.place);
			fw.newLine();
			fw.writeInteger(info.money);
			fw.newLine();
			fw.writeInteger(info.points);
			fw.newLine();
			fw.write(world.name);
			fw.newLine();
			fw.writeInteger(player.x);
			fw.newLine();
			fw.writeInteger(player.y);
			fw.newLine();
			fw.writeInteger(player.z);
			fw.newLine();
		} finally {
			fw.close();
		}
		if (!fw.canWrite()) {
			Logger.add(Logger.FILE, "Could not write to save file ", filename);
		}
	}

}
