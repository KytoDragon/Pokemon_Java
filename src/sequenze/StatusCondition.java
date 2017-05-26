package sequenze;

public enum StatusCondition implements util.Enum {
	OK(0),
	KO(1),
	PARALYZED(2),
	SLEEPING(3),
	POISONED(4),
	BURNING(5),
	FROZEN(6);

	private final int id;

	private StatusCondition(int id) {
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
