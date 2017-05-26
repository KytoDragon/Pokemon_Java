package sequenze;

public enum EncounterType implements util.Enum {
	Gift(0),
	Egg(1),
	Event(2),
	Outside(3),
	Cave(4),
	Water(5),
	Underwater(6),
	Sky(7),
	Building(8),
	Safari(9),
	Overworld(10),
	CryptoStolen(11),
	CryptoGift(12);
	
	
	private final int id;
	
	EncounterType(int id) {
		this.id = id;
	}
	
	@Override
	public int getID(){
		return id;
	}
	
	static EncounterType getById(int id) {
		for (EncounterType e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		assert(false) : ("EncounterType id " + id + " not found");
		return null;
	}
	
	public static int length(){
		return values().length;
	}
}
