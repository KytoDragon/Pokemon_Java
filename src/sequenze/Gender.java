package sequenze;

public enum Gender implements util.Enum {

	MALE(0),
	FEMALE(1),
	/** This is for Pokemon that do not have a gender */
	GENDERLESS(2),
	/** This is for objects, where the gender should not be displayed or is unknown */
	IRRELEVANT(3);

	private int id;

	Gender(int id) {
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
