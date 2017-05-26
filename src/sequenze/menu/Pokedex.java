package sequenze.menu;

import sequenze.Game;
import sequenze.MessageHandler;

public class Pokedex extends GuiItem {

	boolean[] seen;
	boolean[] caught;
	int[] reverse_table;
	PartialDex[] dexs;

	int animation;
	private byte status = CLOSED; // current status of the pokedex
	final static byte STARTUP = 0; // starting up
	final static byte SHUTDOWN = 1; // shutting down
	final static byte ACTIVE = 2; // dummy
	final static byte CLOSED = 8; // closed

	boolean clickClose;

	public final static int NATIONAL_DEX = 0;
	public static int PokemonNum = 152;
	public static int PokedexNum = 2;

	// TODO save wich form/gender/shininess/language was already seen?
	// TODO wich form/gender/shininess of a pokémon to display?

	public Pokedex() {
		seen = new boolean[PokemonNum];
		caught = new boolean[PokemonNum];
		reverse_table = new int[PokemonNum];
		dexs = new PartialDex[PokedexNum];
		dexs[NATIONAL_DEX] = new PartialDex();
		dexs[NATIONAL_DEX].indices = new int[PokemonNum];
		for (int i = 1; i < dexs.length; i++) {
			dexs[i] = new PartialDex();
			dexs[i].indices = new int[0];
		}
		recalculateDex();
	}

	void recalculateDex() {
		for (int j = 0; j < PokemonNum; j++) {
			if (caught[j]) {
				dexs[NATIONAL_DEX].seen++;
				dexs[NATIONAL_DEX].caught++;
			} else if (seen[j]) {
				dexs[NATIONAL_DEX].seen++;
			}
			reverse_table[j] = 0x1;
		}
		for (int i = 1; i < PokedexNum; i++) {
			PartialDex pd = dexs[i];
			for (int j = 0; j < pd.indices.length; j++) {
				int p = pd.indices[j];
				if (caught[p]) {
					pd.seen++;
					pd.caught++;
				} else if (seen[p]) {
					pd.seen++;
				}
				reverse_table[p] |= (1 << i);
			}
		}
	}

	public boolean hasSeen(int id) {
		return seen[id];
	}

	public boolean hasCaught(int id) {
		return caught[id];
	}

	public void encountered(int id) {
		if (seen[id]) {
			return;
		}
		int dex_ids = reverse_table[id];
		for (int i = 0; i < dexs.length; i++) {
			if ((dex_ids & (1 << i)) != 0) {
				dexs[i].seen++;
			}
		}
		seen[id] = true;
	}

	public void caught(int id) {
		if (caught[id]) {
			return;
		}
		int dex_ids = reverse_table[id];
		for (int i = 0; i < dexs.length; i++) {
			if ((dex_ids & (1 << i)) != 0) {
				if (!seen[id]) {
					dexs[i].seen++;
				}
				dexs[i].caught++;
			}
		}
		seen[id] = true;
		caught[id] = true;
	}

	public int seenNum(int pokedexID) {
		return dexs[pokedexID].seen;
	}

	public int caughtNum(int pokedexID) {
		return dexs[pokedexID].caught;
	}

	@Override
	public void update(Game g) {
		// TODO
		switch (status) {
			case CLOSED:
				if (MessageHandler.hasMessage(MessageHandler.POKEDEX_OPEN)) {
					clickClose = false;
					animation = 17;
					status = STARTUP;
				}
				break;
			case STARTUP:
				if (animation == 32) {
					animation = 0;
					status = ACTIVE;
				} else {
					animation++;
				}
				break;
			case SHUTDOWN:
				if (animation == 32) {
					animation = 0;
					status = CLOSED;
					MessageHandler.add(MessageHandler.POKEDEX_CLOSE, clickClose);
				} else {
					animation++;
				}
				break;
			case ACTIVE:
				status = SHUTDOWN;
				break;
		}
	}

	@Override
	public void drawL() {
		// TODO
	}

	@Override
	public void drawR() {
		// TODO
	}

	@Override
	public boolean hasFocus() {
		return status != CLOSED;
	}

	@Override
	public boolean occupiesLeft() {
		return status != CLOSED;
	}

	@Override
	public boolean occupiesRight() {
		return status != CLOSED;
	}

	private class PartialDex {

		int[] indices;
		int seen;
		int caught;
	}
}
