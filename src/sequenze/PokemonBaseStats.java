package sequenze;

public class PokemonBaseStats {

	int id;
	int form;
	int gender_ration; // 0-255
	int[/*2*/] abbilities;
	int catch_rate; // 0-255
	int height; // in 0.1 meters
	int weight; // in 0.1 kg
	char[] internal_name; // english all caps with underscores
	XPType xp_type;
	Type[/*2*/] types;
	int[/*6*/] base_stats;
	int[/*6*/] effort_yield;
	int[/*2*/] held_item;
	int[/*2*/] egg_group;
	int escape_rate; // 0-255, for safari zone
	int search_color; // for pokedex
	int body_style; // for pokedex
	boolean asymmtric;
	int base_happiness;
	int egg_cycles;

	// level_up_attacks
	// tm_hm_attacks
	// evolutions
	// egg_moves
	public int getUUID() {
		assert (form <= 0xFF);
		return (id << 8) + form;
	}

	public static int getUUID(int id, int form) {
		assert (form <= 0xFF);
		return (id << 8) + form;
	}
}
