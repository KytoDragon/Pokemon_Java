package sequenze;

import sequenze.menu.Pokedex;
import util.Map;
import util.Random;
import util.StringMap;

public final class InfoLoader {

	private InfoLoader() {
	}

	private static Map<Integer, PokemonBaseStats> base_stats;
	// NOTE String (internal_name) to Integer (id) instead, since that is all we ever access with the name?
	private static StringMap<PokemonBaseStats> base_stats2;
	private static ItemStats[] item_stats;
	private static StringMap<ItemStats> item_stats2;
	private static AttackStats[] attack_stats;
	private static StringMap<AttackStats> attack_stats2;
	private static AbilityStats[] ability_stats;
	private static StringMap<AbilityStats> ability_stats2;

	public static void init() {
		base_stats = new Map<>();
		base_stats2 = new StringMap<>();
		for (int i = 0; i < Pokedex.PokemonNum; i++) {
			PokemonBaseStats base = new PokemonBaseStats();
			base.id = i;
			base.internal_name = ("POKEMON_" + i).toCharArray();
			base.gender_ration = Random.nextInt(256);
			base.abbilities = new int[]{Random.nextInt(100), Random.nextInt(100)};
			base.xp_type = Random.getEnum(XPType.class);
			if ((Random.nextInt() & 1) == 0) {
				base.types = new Type[]{Random.getEnum(Type.class), Type.NONE};
			} else {
				base.types = new Type[]{Random.getEnum(Type.class), Random.getEnum(Type.class)};
			}
			base.base_stats = new int[]{Random.nextInt(256), Random.nextInt(256), Random.nextInt(256), Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)};

			base_stats.put(base.getUUID(), base);
			base_stats2.put(base.internal_name, base);
		}

		item_stats = new ItemStats[537];
		item_stats2 = new StringMap<>();
		for (int i = 0; i < item_stats.length; i++) {
			ItemStats item = new ItemStats();
			item.id = i;
			if (i > 0) {
				item.internal_name = ("ITEM_" + i).toCharArray();
			} else {
				item.internal_name = "NONE".toCharArray();
			}
			if (i >= 1 && i <= 16) {
				item.bag = 3;
			} else if (i >= 17 && i <= 54) {
				item.bag = 2;
			} else if (i >= 55 && i <= 67) {
				item.bag = 7;
			} else if (i >= 68 && i <= 112) {
				item.bag = 1;
			} else if (i >= 135 && i <= 136) {
				item.bag = 1;
			} else if (i >= 137 && i <= 148) {
				item.bag = 6;
			} else if (i >= 149 && i <= 212) {
				item.bag = 5;
			} else if (i >= 213 && i <= 327) {
				item.bag = 1;
			} else if (i >= 328 && i <= 427) {
				item.bag = 4;
			} else if (i >= 428 && i <= 484) {
				item.bag = 0;
			} else if (i >= 485 && i <= 491) {
				item.bag = 1;
			} else if (i >= 492 && i <= 500) {
				item.bag = 3;
			} else if (i >= 501 && i <= 504) {
				item.bag = 0;
			} else if (i >= 505 && i <= 531) {
				item.bag = 1;
			} else if (i >= 532 && i <= 536) {
				item.bag = 0;
			} else {
				item.bag = -1;
			}
			item.options = new boolean[]{true, true, true, true};

			item_stats[i] = item;
			item_stats2.put(item.internal_name, item);
		}

		attack_stats = new AttackStats[100];
		attack_stats2 = new StringMap<>();
		for (int i = 0; i < attack_stats.length; i++) {
			AttackStats attack = new AttackStats();
			attack.id = i;
			if (i > 0) {
				attack.internal_name = ("ATTACK_" + i).toCharArray();
			} else {
				attack.internal_name = "NONE".toCharArray();
			}
			attack.power = i % 25 * 10;
			attack.accuracy = i % 11 * 10;
			attack.is_field_attack = true;
			attack.base_pp = i % 50;
			attack.contact_type = Random.getEnum(ContactType.class);
			attack.type = Random.getEnum(Type.class);

			attack_stats[i] = attack;
			attack_stats2.put(attack.internal_name, attack);
		}

		ability_stats = new AbilityStats[100];
		ability_stats2 = new StringMap<>();
		for (int i = 0; i < ability_stats.length; i++) {
			AbilityStats ability = new AbilityStats();
			ability.id = i;
			if (i > 0) {
				ability.internal_name = ("ABILITY_" + i).toCharArray();
			} else {
				ability.internal_name = "NONE".toCharArray();
			}

			ability_stats[i] = ability;
			ability_stats2.put(ability.internal_name, ability);
		}
	}

	public static int getBagCount() {
		// TODO Auto-generated method stub
		int tmp = 7; // number of sub-bags
		return tmp;
	}

	/** Returns the experience type for the given pokemon. */
	public static XPType getXpType(int id, int form) {
		return base_stats.get(PokemonBaseStats.getUUID(id, form)).xp_type;
	}

	/** Returns the types for the given pokemon. */
	public static Type[] getTypes(int id, int form) {
		return base_stats.get(PokemonBaseStats.getUUID(id, form)).types;
	}

	/** Returns the base stats for the given pokemon. */
	public static int[] getBaseStats(int id, int form) {
		return base_stats.get(PokemonBaseStats.getUUID(id, form)).base_stats;
	}

	public static int[] getAbilities(int id, int form) {
		return base_stats.get(PokemonBaseStats.getUUID(id, form)).abbilities;
	}

	public static int getGenderThreshold(int id, int form) {
		return base_stats.get(PokemonBaseStats.getUUID(id, form)).gender_ration;
	}

	public static int getPokemonHeight(Pokemon p) {
		return base_stats.get(PokemonBaseStats.getUUID(p.id, p.form)).height;
	}

	public static char[] getInternalPokemonName(int id, int form) {
		return base_stats.get(PokemonBaseStats.getUUID(id, form)).internal_name;
	}

	/** Return the types for the given attacks. */
	public static Type[] getAttackTypes(int[] attackids) {
		Type[] tmp = new Type[4];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = attack_stats[attackids[i]].type;
		}
		return tmp;
	}

	public static int getPower(int id) {
		return attack_stats[id].power;
	}

	public static int getAccuracy(int id) {
		return attack_stats[id].accuracy;
	}

	public static ContactType getContactType(int id) {
		return attack_stats[id].contact_type;
	}

	public static boolean isFieldAttack(int id) {
		return attack_stats[id].is_field_attack;
	}

	public static int getBasePP(int id) {
		return attack_stats[id].base_pp;
	}

	public static char[] getInternalAttackName(int id) {
		return attack_stats[id].internal_name;
	}

	public static int getAttackId(char[] attack) {
		return attack_stats2.get(attack).id;
	}

	public static int getBagForItem(int id) {
		return item_stats[id].bag;
	}

	public static boolean[] getItemOptions(int id) {
		return item_stats[id].options;
	}

	public static char[] getInternalItemName(int id) {
		return item_stats[id].internal_name;
	}

	public static int getItemId(char[] item) {
		return item_stats2.get(item).id;
	}

	public static char[] getInternalAbilityName(int id) {
		return ability_stats[id].internal_name;
	}

	public static int getAbilityId(char[] ability) {
		return ability_stats2.get(ability).id;
	}

	/** Returns the name for the given region. */
	public static String getInternalRegionName(int id) {
		// TODO Auto-generated method stub
		String tmp = "KANTO"; // the name of the region
		return tmp;
	}

	public static int getRegionId(char[] region) {
		// TODO Auto-generated method stub
		int tmp = 0; // the id of the region
		return tmp;
	}

}
