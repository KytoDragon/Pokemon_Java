package sequenze;

public abstract class Entity {
	
	static final int MAXSWITCHNUM = 4;
	
	/** Name of the Entity. */
	public char[] name;
	/** X coordinate of the player. */
	public int x;
	/** Y coordinate of the player. */
	public int y;
	/** Z coordinate of the player. */
	public int z;
	/** Whether the entity is currently on a bridge. */
	public boolean bridge;
	/** The oriantation of this entity. */
	public Direction orientation = Direction.SOUTH;
	/** X coordinate of the entity on the screen in pixels. */
	public int xf;
	/** Y coordinate of the entity on the screen in pixels. */
	public int yf;
	/** Current speed on the bike. */
	byte speed = 0;
	/** The x coordinate a jump has started. */
	public int jumpfromx;
	/** The y coordinate a jump has started. */
	public int jumpfromy;
	/** Status of the entity. */
	EntityStatus status = EntityStatus.STANDING;
	/** Object that draws the entity to the screen. */
	EntityView ev;
	/** The current modified move-route. */
	String[] customMoveRoute;
	/** The index of the modified move-route. */
	int moveRouteIndex;
	
	boolean[] switches = new boolean[MAXSWITCHNUM];
	
	public abstract void setMoveRoute(String[] list);
	
	public abstract int[] getTex();
	
	public abstract void exclaim(int id);
	
	/** Moves the player in his current direction. */
	public void calcXY() {
		switch (orientation){
		case SOUTH:
			y += 1;
			break;
		case EAST:
			x += 1;
			break;
		case NORTH:
			y -= 1;
			break;
		case WEST:
			x -= 1;
			break;
		}
	}
}
