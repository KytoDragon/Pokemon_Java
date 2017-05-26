package sequenze;

public enum ContactType implements util.Enum {

	PHYSICAL(0),
	SPECIAL(1),
	STATUS(2);

	private final int id;

	private ContactType(int id) {
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
