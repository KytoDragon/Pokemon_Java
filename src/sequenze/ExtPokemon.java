package sequenze;

import static sequenze.Statistic.*;

import util.ConV;

public class ExtPokemon extends Pokemon {

	public int hp; // the current health points of this pokemon
	public StatusCondition status = StatusCondition.OK;
	public final int[] pps; // the current attack points for each attack

	public final Type[] attacktypes; // types of the attacks
	public final int[] maxpps; // the current max attack points for each attack
	public final int[] stats; // stats of this pokemon
	public final int[] base; // base stats of this pokemon
	public int nextlevelep; // the experience needed to reach the next level
	public XPType xptype = XPType.FAST; // type of experience growth
	public Type type1 = Type.NONE; // first type
	public Type type2 = Type.NONE; // second type
	public int characteristic; // [0, 25)

	public ExtPokemon() {
		super();
		attacktypes = new Type[ATTACKS];
		pps = new int[ATTACKS];
		maxpps = new int[ATTACKS];
		stats = new int[Statistic.length()];
		base = new int[Statistic.length()];
	}

	/** Creates a pokemon based on the given one. */
	public ExtPokemon(Pokemon p) {
		this();
		assert (!(p instanceof ExtPokemon)) : ("Given pokemon is already extended.");
		id = p.id;
		form = p.form;
		shiny = p.shiny;
		gender = p.gender;
		level = p.level;
		currentep = p.currentep;
		pokerus = p.pokerus;
		trainerid = p.trainerid;
		strainerid = p.strainerid;
		itemid = p.itemid;
		ability = p.ability;
		nature = p.nature;
		ConV.arrayCopy(p.attackids, attackids);
		ConV.arrayCopy(p.ppups, ppups);
		ConV.arrayCopy(p.evs, evs);
		ConV.arrayCopy(p.ivs, ivs);
		ConV.arrayCopy(p.markings, markings);
		name = p.name;
		caught_level = p.caught_level;
		caught_region = p.caught_region;
		caught_location = p.caught_location;
		caught_time = p.caught_time;
		hatch_time = p.hatch_time;
		encounter_type = p.encounter_type;
		ribbons = p.ribbons;
		personality = p.personality;
		pokeball = p.pokeball;
		shiny_leaf = p.shiny_leaf;

		recalculateStats();

		hp = stats[HEALTH.getID()];
		xptype = InfoLoader.getXpType(id, form);
		nextlevelep = level == Pokemon.LEVELMAX ? 0 : xptype.getXpForLevel(level + 1);

		Type[] types = InfoLoader.getTypes(id, form);
		type1 = types[0];
		type2 = types[1];
		ConV.arrayCopy(InfoLoader.getBaseStats(id, form), base);
		ConV.arrayCopy(InfoLoader.getAttackTypes(attackids), attacktypes);

		ConV.arrayCopy(maxpps, pps);

		int maxIV = ConV.max(ivs);
		int currentPos = ConV.modulo(personality, Statistic.length());
		while (currentPos == 0 ? (ivs[HEALTH.getID()] != maxIV) : (ivs[currentPos - 1] != maxIV)) {
			currentPos = (currentPos + 1) % Statistic.length();
		}
		characteristic = (maxIV % CHARACTERISTIC_NUM) * Statistic.length() + currentPos;
	}

	public void recalculateStats() {
		for (int i = 0; i < Statistic.length(); i++) {
			if (i == HEALTH.getID()) {
				continue;
			}
			stats[i] = (ivs[i] + 2 * base[i] + evs[i] / 4) * level / 100 + 5;
			stats[i] = natureCheck(i, stats[i]);
		}
		stats[HEALTH.getID()] = (ivs[HEALTH.getID()] + 2 * base[HEALTH.getID()] + evs[HEALTH.getID()] / 4 + 100) * level / 100 + 10;

		for (int i = 0; i < ATTACKS; i++) {
			maxpps[i] = InfoLoader.getBasePP(attackids[i]) * (5 + ppups[i]) / 5;
		}
	}

	private int natureCheck(int stat, int value) {
		byte modifier = 10;
		if (nature.getID() / 5 == stat) {
			modifier++;
		}
		if (nature.getID() % 5 == stat) {
			modifier--;
		}
		return value * modifier / 10;
	}

	/** Returns the number of field attacks this pokemon has. */
	public int getFieldAttackCount() {
		int count = 0;
		for (int i = 0; i < ATTACKS; i++) {
			if (InfoLoader.isFieldAttack(attackids[i])) {
				count++;
			}
		}
		return count;
	}

	/** Returns the id of the field attack with the given number. */
	public int getFieldAttackNr(int nr) {
		int count = nr;
		for (int i = 0; i < ATTACKS; i++) {
			if (InfoLoader.isFieldAttack(attackids[i])) {
				if (count == 0) {
					return attackids[i];
				}
				count--;
			}
		}
		assert (false) : ("Field attack nr. " + nr + " not found!");
		return 0;
	}

	/** Return whether this pokemon knows the given attack. */
	public boolean hasAttack(int id) {
		for (int i = 0; i < ATTACKS; i++) {
			if (attackids[i] == id) {
				return true;
			}
		}
		return false;
	}

	/** Switches the attacks at the given positions. */
	public void switchAttacks(int moveFrom, int moveTo) {
		if (moveTo == moveFrom) {
			return;
		}
		int tmpid = attackids[moveFrom];
		Type tmptype = attacktypes[moveFrom];
		int tmpmaxap = maxpps[moveFrom];
		int tmpap = pps[moveFrom];

		attackids[moveFrom] = attackids[moveTo];
		attacktypes[moveFrom] = attacktypes[moveTo];
		maxpps[moveFrom] = maxpps[moveTo];
		pps[moveFrom] = pps[moveTo];

		attackids[moveTo] = tmpid;
		attacktypes[moveTo] = tmptype;
		maxpps[moveTo] = tmpmaxap;
		pps[moveTo] = tmpap;
	}

	public int getEggQoute() {
		if (friendship > 40) {
			return 3;
		} else if (friendship > 10) {
			return 2;
		} else if (friendship > 5) {
			return 1;
		} else {
			return 0;
		}
	}
}
