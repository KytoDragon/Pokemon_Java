package sequenze;

public enum Type implements util.Enum {

	NONE(0),
	NORMAL(1),
	FIGHTING(2),
	FLYING(3),
	POISON(4),
	GROUND(5),
	ROCK(6),
	BUG(7),
	GHOST(8),
	STEEL(9),
	FIRE(10),
	WATER(11),
	GRASS(12),
	ELECTRIC(13),
	PSYCHIC(14),
	ICE(15),
	DRAGON(16),
	DARK(17),
	SHADOW(18),
	FAIRY(19);

	private final int id;

	private Type(int id) {
		this.id = id;
	}

	@Override
	public int getID() {
		return id;
	}

	public static int length() {
		return values().length;
	}
}
