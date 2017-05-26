package sequenze;

public enum Button implements util.Enum {
	
	PAUSE(0),
	A(1),
	B(2),
	START(3),
	SELECT(4),
	DOWN(5),
	RIGHT(6),
	UP(7),
	LEFT(8),
	L(9),
	R(10),
	DEBUG(11),
	EXIT(12);
	
	private final int id;
	
	private Button(int id){
		this.id = id;
	}
	
	@Override
	public int getID(){
		return id;
	}
	
	public static Button getDirection(Direction orientation){
		switch(orientation){
			case SOUTH:
				return DOWN;
			case EAST:
				return RIGHT;
			case NORTH:
				return UP;
			case WEST:
				return LEFT;
			default:
				assert(false) : ("No button for given direction: " + orientation.name());
				return null;
		}
	}
	
	public static int length(){
		return values().length;
	}
}
