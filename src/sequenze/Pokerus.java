package sequenze;

import util.ConV;
import util.Logger;

public enum Pokerus implements util.Enum {
	NONE(-1, false),
	HEALED(0, false),
	DAY1(1, true),
	DAY2(2, true),
	DAY3(3, true),
	DAY4(4, true);

	// TODO support different Pokerus strains

	private int id;
	private boolean isActive;

	Pokerus(int id, boolean isActive) {
		this.id = id;
		this.isActive = isActive;
	}

	Pokerus next() {
		if (isActive) {
			return getById(id - 1);
		}
		return this;
	}

	static Pokerus getById(int id) {
		for (Pokerus p : values()) {
			if (p.id == id) {
				return p;
			}
		}
		Logger.add(Logger.GAME, "Pokerus id not found: ", id);
		return null;
	}

	public boolean isActive() {
		return isActive;
	}

	@Override
	public int getID() {
		return id;
	}

	public static int length() {
		return values().length;
	}

}
