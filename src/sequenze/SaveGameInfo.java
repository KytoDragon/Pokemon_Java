package sequenze;

import java.io.File;
import java.text.ParseException;

import static sequenze.InstancePackage.*;
import static sequenze.SaveSystem.SAVE_PATH;

import sequenze.world.World;
import util.ConV;
import util.FileReader;
import util.Logger;
import util.Time;

public class SaveGameInfo {

	char[] name;
	char[] charName;
	long lastPlayed;
	long timePlayed;
	int badges;
	int pokemon;
	char[] place;
	int money;
	int points;

	public void load() {
		SystemControll.setString(SystemControll.PlayerName, charName);
		SystemControll.setVariable(SystemControll.BadgeNum, badges);
		SystemControll.setVariable(SystemControll.Money, money);
		SystemControll.setVariable(SystemControll.PlayerScore, points);
		// TODO should be saved as long
		SystemControll.setVariable(SystemControll.TimePlayed, (int) timePlayed);
		SystemControll.setVariable(SystemControll.TimeStart, (int) Time.getTime());

		String filename = new String(name) + ".sav";
		FileReader fr = new FileReader(SAVE_PATH + "\\" + filename);
		if (!fr.canRead()) {
			Logger.add(Logger.FILE, "Could not open save file ", filename);
			return;
		}
		try {
			SystemControll.setString(SystemControll.PlayerName, charName);
			fr.skipLines(8); // skip save info
			world = World.getWorld(fr.readCLine());
			player.x = ConV.parseInt(fr.readCLine());
			player.y = ConV.parseInt(fr.readCLine());
			player.z = ConV.parseInt(fr.readCLine());
			save = this;
		} catch (ParseException e) {
			Logger.add(Logger.FILE, "Unable to parse number in save fale: ", filename);
		} catch (NullPointerException e) {
			Logger.add(Logger.FILE, "Reached end of file in save fale: ", filename);
		} finally {
			fr.close();
		}
	}

	public void init() {
		save = this;
		SystemControll.setString(SystemControll.PlayerName, name);
		world = World.getWorld("overworld".toCharArray());
		player.x = 0;
		player.y = 0;
		player.z = 0;
	}

	public void remove() {
		File file = new File(SAVE_PATH + "\\" + new String(name) + ".sav");
		file.delete();
	}

	public void save() {
		charName = SystemControll.getString(SystemControll.PlayerName);
		badges = SystemControll.getVariable(SystemControll.BadgeNum);
		pokemon = pokedex.caughtNum(0);
		place = "Nowhere".toCharArray();
		money = SystemControll.getVariable(SystemControll.Money);
		points = SystemControll.getVariable(SystemControll.PlayerScore);
		timePlayed = SystemControll.getVariable(SystemControll.TimePlayed);
		lastPlayed = Time.getTime();

		SaveSystem.writeSaveGameInfo(this);
	}
}
