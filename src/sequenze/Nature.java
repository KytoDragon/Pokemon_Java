package sequenze;

import util.ConV;
import util.Logger;

public enum Nature implements util.Enum {
	HARDY(0),
	LONELY(1),
	BRAVE(2),
	ADAMANT(3),
	NAUGHTY(4),
	BOLD(5),
	DOCILE(6),
	RELAXED(7),
	IMPISH(8),
	LAX(9),
	TIMID(10),
	HASTY(11),
	SERIOUS(12),
	JOLLY(13),
	NAIVE(14),
	MODEST(15),
	MILD(16),
	QUIET(17),
	BASHFUL(18),
	RASH(19),
	CALM(20),
	GENTLE(21),
	SASSY(22),
	CAREFUL(23),
	QUIRKY(24);

	private final int id;

	Nature(int id) {
		this.id = id;
	}

	@Override
	public int getID() {
		return id;
	}

	static Nature getById(int id) {
		for (Nature n : values()) {
			if (n.id == id) {
				return n;
			}
		}
		Logger.add(Logger.GAME, "Nature id not found: ", id);
		return null;
	}

	public static int length() {
		return values().length;
	}

}
