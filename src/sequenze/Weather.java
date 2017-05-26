package sequenze;

public enum Weather implements util.Enum {
	CLEAR(0),
	SUN(1),
	RAIN(2),
	SANDSTORM(3),
	HAIL(4),
	FOG(5),
	SHADOWAURA(6);

	private final int id;

	private Weather(int id) {
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
