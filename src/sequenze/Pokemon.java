package sequenze;

import util.ConV;
import util.Random;

public class Pokemon {

	public static final int EVMAX = 1 << 8;
	public static final int EVOVERALLMAX = EVMAX * 2;
	public static final int LEVELMAX = 100;
	public static final int TRAINERIDMAX = 1 << 16;
	public static final int IVMAX = 1 << 5;
	public static final int PPUPMAX = 3;
	public static final int PPMAX = 1 << 6;
	public static final int ATTACKS = 4;
	/** Number of characteritics per stat */
	public static final int CHARACTERISTIC_NUM = 5;
	public static final int MARKINGS_NUM = 6;

	public int id; // pokemon id
	public int form; // special form
	public boolean shiny; // whether the pokemon is shiny
	public Gender gender = Gender.IRRELEVANT; // gender of the pokemon
	public int level = 1; // pokemon level
	public int currentep; // current experience
	public Pokerus pokerus = Pokerus.NONE; // stage of the pokerus
	public int trainerid; // owner id
	public int strainerid; // owner id second part
	public char[] trainer_name; // name of the owner
	public boolean trainer_male; // gender of the owner
	public int itemid; // hold item id
	public int ability; // the id of the ability
	public Nature nature = Nature.HARDY; // the nature
	public final int[] attackids; // ids of the attacks
	public final int[] ppups; // number of times, a pp-up has been used
	public final int[] evs; // the evs for each stat
	public final int[] ivs; // the dvs for each stat
	public char[] name; // the custom name for this pokemon
	public int caught_level; // the level the pokemon was caught with
	public int caught_region; // the region the pokemon was caught in
	public int caught_location; // the location the pokemon was caught at
	public long caught_time; // the datetime the pokemon was caught at
	public int[] ribbons; // list of ribbon ids owned by this pokemon
	public int personality; // personality value for this pokemon
	public int pokeball; // the pokeball the pokemon was caught in
	public final boolean[] markings; // the markings used for box sorting
	public int friendship; // the friendship value or the amount of egg cycles left or the crypto level left
	public EncounterType encounter_type = EncounterType.Gift; // how the pokemon was recieved
	public long hatch_time; // the datetime the pokemon was hatched at or the datetime the crypto pokemon was freed at
	public int egg_location; // the location the pokemon egg was recieved at or the location the crypto pokemon was stolen at
	public boolean crypto; // whether the pokkemon is in its crypto stage
	public int shiny_leaf; // TODO actually needs to be a boolean field, since acquiring a shiny leaf can only be done once per area per pokemon
	public int size; // the size of the pokemon in tenth of meters

	public Pokemon() {
		attackids = new int[ATTACKS];
		ppups = new int[ATTACKS];
		evs = new int[Statistic.length()];
		ivs = new int[Statistic.length()];
		ribbons = new int[0];
		markings = new boolean[MARKINGS_NUM];
	}

	private static final int[] size_size = {1, 1, 2, 4, 20, 50, 100, 150, 150, 100, 50, 20, 5, 2, 1};
	private static final int[] size_base = {290, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1700};
	private static final int[] size_min = {0, 10, 110, 310, 710, 2710, 7710, 17710, 32710, 47710, 57710, 62710, 64710, 65210, 65510};

	public void generatePersonality() {
		personality = Random.nextInt();
		int gender_threshold = InfoLoader.getGenderThreshold(id, form);
		if (gender_threshold == 0XFF) {
			gender = Gender.GENDERLESS;
		} else if (gender_threshold == 0XFE || gender_threshold > (personality & 0xFF)) {
			gender = Gender.FEMALE;
		} else {
			gender = Gender.MALE;
		}

		int[] abilities = InfoLoader.getAbilities(id, form);
		ability = abilities[personality & 0x1];

		nature = Nature.getById(ConV.modulo(personality, Nature.length()));

		shiny = (trainerid ^ strainerid ^ (personality & 0xFFFF) ^ (personality >>> 16)) < 8;

		for (int i = 0; i < Statistic.length(); i++) {
			ivs[i] = Random.nextInt(IVMAX);
		}

		int s = (((ivs[Statistic.ATTACK.getID()] % 16 ^ ivs[Statistic.DEFENCE.getID()] % 16) * (ivs[Statistic.HEALTH.getID()] % 16)) ^ (personality % 256)) * 256 + (((ivs[Statistic.SPATTACK.getID()] % 16 ^ ivs[Statistic.SPDEFENCE.getID()] % 16) * (ivs[Statistic.SPEED.getID()] % 16)) ^ (personality / 256) % 256);
		int size_class = 0;
		for (; size_class < size_min.length; size_class++) {
			if (size_min[size_class] >= s) {
				size_class++;
				break;
			}
		}
		size_class--;
		size = ((s - size_min[size_class]) / size_size[size_class] + size_base[size_class]) * InfoLoader.getPokemonHeight(this) / 10;
	}

	/** Returns whether this pokemon is still an egg. */
	public boolean isEgg() {
		return level == 0;
	}

	public char[] getName() {
		if (isEgg()) {
			return Localisation.getEggName();
		} else if (name != null) {
			return name;
		} else {
			return Localisation.getPokemonName(id, form);
		}
	}

	/** Returns an extended version of this pokemon. */
	public ExtPokemon extend() {
		if (this instanceof ExtPokemon) {
			return (ExtPokemon) this;
		}
		return new ExtPokemon(this);
	}
}
