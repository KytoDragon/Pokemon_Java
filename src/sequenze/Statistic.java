package sequenze;

public enum Statistic implements util.Enum {
	ATTACK(0),
	DEFENCE(1),
	SPEED(2),
	SPATTACK(3),
	SPDEFENCE(4),
	HEALTH(5);
	//ACCURACY(6),
	//EVASION(7),
	//CRITICAL(8);

	private final int id;

	private Statistic(int id) {
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
