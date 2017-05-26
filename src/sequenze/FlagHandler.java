package sequenze;

import util.Logger;
import util.Random;

public class FlagHandler {

	private static FlagHandler current = null;
	private int itemsleft = 215;

	public static FlagHandler current() {
		if (current == null) {
			current = new FlagHandler();
		}
		return current;
	}

	public static boolean isRegistert(int id) {
		int max = SystemControll.getVariable(SystemControll.RegisteredNum);
		for (int i = 0; i < max; i++) {
			if (SystemControll.getVariable(SystemControll.Registered1 + i) == id) {
				return true;
			}
		}
		return false;
	}

	public static void unregister(int id) {
		int max = SystemControll.getVariable(SystemControll.RegisteredNum);
		for (int i = 0; i < max; i++) {
			if (SystemControll.getVariable(SystemControll.Registered1 + i) == id) {
				SystemControll.setVariable(SystemControll.Registered1 + i, 0);
				return;
			}
		}
		Logger.add(Logger.GAME, "Registered item not found: ", InfoLoader.getInternalItemName(id));
	}

	public static void register(int id) {
		int max = SystemControll.getVariable(SystemControll.RegisteredNum);
		for (int i = 0; i < max; i++) {
			if (SystemControll.getVariable(SystemControll.Registered1 + i) == 0) {
				SystemControll.setVariable(SystemControll.Registered1 + i, id);
				return;
			}
		}
		Logger.add(Logger.GAME, "Not enough space to register item: ", InfoLoader.getInternalItemName(id));
	}

	public static boolean hasRegisterSpace() {
		int max = SystemControll.getVariable(SystemControll.RegisteredNum);
		for (int i = 0; i < max; i++) {
			if (SystemControll.getVariable(SystemControll.Registered1 + i) == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean hasItem() {
		// TODO Auto-generated method stub
		boolean tmp = false; // whether there is still an unloaded item in the savefile
		if (itemsleft > 0) {
			tmp = true;
		}
		return tmp;
	}

	public void initItemLoader() {
		// TODO Auto-generated method stub
		// load item part of the save file
	}

	public int[] nextItem() {
		// TODO Auto-generated method stub
		int[] tmp = {Random.nextInt(536) + 1, Random.nextInt(256) + 1}; // next item in the item save file
		itemsleft--;
		return tmp;
	}

	public void initTeamLoader() {
		// TODO Auto-generated method stub

	}

	public ExtPokemon nextTeamPokemon() {
		// TODO Auto-generated method stub
		Pokemon pk = new Pokemon();
		pk.id = Random.nextInt(151) + 1;
		pk.generatePersonality();
		pk.level = Random.nextInt(101);
		pk.trainer_name = SystemControll.getString(SystemControll.PlayerName);
		for (int i = 0; i < pk.attackids.length; i++) {
			pk.attackids[i] = Random.nextInt(100);
		}
		if (Random.nextInt(10) == 0) {
			pk.shiny = true;
		}
		if (Random.nextInt(2) == 0) {
			pk.itemid = Random.nextInt(537);
		}
		if (Random.nextInt(10) == 0) {
			pk.pokerus = Random.getEnum(Pokerus.class);
		}
		for (int i = 0; i < pk.markings.length; i++) {
			pk.markings[i] = Random.nextInt(5) == 0;
		}
		pk.pokeball = Random.nextInt(25);
		pk.shiny_leaf = Random.nextInt(7);
		pk.ribbons = new int[Random.nextInt(81)];
		for (int i = 0; i < pk.ribbons.length; i++) {
			int ribbon = Random.nextInt(80 - i);
			for (int j = 0; j < i; j++) {
				if (ribbon >= pk.ribbons[j]) {
					ribbon++;
				}
			}
			for (int j = 0; j < i; j++) {
				if (ribbon < pk.ribbons[j]) {
					int tmp = pk.ribbons[j];
					pk.ribbons[j] = ribbon;
					ribbon = tmp;
				}
			}
			pk.ribbons[i] = ribbon;
		}
		pk.caught_time = Random.nextLong(1451606400L);
		pk.encounter_type = Random.getEnum(EncounterType.class);
		if (pk.encounter_type == EncounterType.Egg) {
			pk.caught_level = 0;
			pk.hatch_time = pk.caught_time + Random.nextLong(1451606400L - pk.caught_time);
		} else {
			pk.caught_level = Random.nextInt(pk.level + 1);
		}
		ExtPokemon epk = pk.extend();
		if (epk.level == Pokemon.LEVELMAX) {
			epk.currentep = epk.xptype.getXpForLevel(epk.level);
		} else {
			epk.currentep = epk.xptype.getXpForLevel(epk.level) + epk.xptype.getXPDifference(epk.level) * Random.nextInt(100) / 100;
		}
		epk.hp = epk.hp * Random.nextInt(101) / 100;
		if (epk.hp == 0) {
			epk.status = StatusCondition.KO;
		} else if (Random.nextInt(20) == 0) {
			epk.status = Random.getEnum(StatusCondition.class);
		}
		return epk;
	}

}
