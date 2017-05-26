package sequenze;

import static util.ConV.pow;

public enum XPType implements util.Enum {

	FAST(0),
	SEMIFAST(1),
	SEMISLOW(2),
	SLOW(3),
	ERRATIC(4),
	FLUCTUATING(5);

	private final int id;
	private static int[][] lookup;

	private XPType(int id) {
		this.id = id;
	}

	public static void initLoockUp() {

		int[][] lut = new int[XPType.length()][Pokemon.LEVELMAX + 1];
		for (XPType xpt : XPType.values()) {
			for (int j = 0; j <= Pokemon.LEVELMAX; j++) {
				lut[xpt.getID()][j] = xpt.getXpForLevel(j);
			}
		}
		XPType.lookup = lut;
	}

	@Override
	public int getID() {
		return id;
	}

	public int getXpForLevel(int level) {
		assert (level >= 0 && level <= Pokemon.LEVELMAX);
		if (lookup != null) {
			return lookup[this.getID()][level];
		}
		if (level <= 1) {
			return 0;
		}
		switch (this) {
			case FAST:
				return 4 * pow(level, 3) / 5;
			case SEMIFAST:
				return pow(level, 3);
			case SEMISLOW:
				return 6 * pow(level, 3) / 5 - 15 * pow(level, 2) + 100 * level - 140;
			case SLOW:
				return 5 * pow(level, 3) / 4;
			case ERRATIC:
				if (level <= 50) {
					return pow(level, 3) * (100 - level) / 50;
				} else if (level <= 68) {
					return pow(level, 3) * (150 - level) / 100;
				} else if (level <= 98) {
					return pow(level, 3) * ((1911 - 10 * level) / 3) / 500;
				} else {
					return pow(level, 3) * (160 - level) / 100;
				}
			case FLUCTUATING:
				if (level <= 15) {
					return pow(level, 3) * (24 + (level + 1) / 3) / 50;
				} else if (level <= 36) {
					return pow(level, 3) * (14 + level) / 50;
				} else {
					return pow(level, 3) * (32 + level / 2) / 50;
				}
			default:
				assert (false) : ("Unknown XPType");
				return 1;
		}
	}

	public int getXPDifference(int level) {
		return getXpForLevel(level + 1) - getXpForLevel(level);
	}

	public static int length() {
		return values().length;
	}
}
