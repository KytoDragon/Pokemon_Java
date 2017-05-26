package sequenze;

public enum Direction implements util.Enum {

	SOUTH(0),
	EAST(1),
	NORTH(2),
	WEST(3),
	MIDDLE(4),
	NORTHSOUTH(5),
	EASTWEST(6);

	private final int id;

	private Direction(int id) {
		this.id = id;
	}

	/** Returns the direction to the left of a. */
	public static Direction left(Direction a) {
		switch (a) {
			case SOUTH:
				return EAST;
			case EAST:
				return NORTH;
			case NORTH:
				return WEST;
			case WEST:
				return SOUTH;
			case NORTHSOUTH:
				return EASTWEST;
			case EASTWEST:
				return NORTHSOUTH;
			case MIDDLE:
				return MIDDLE;
			default:
				throw null;
		}
	}

	/** Returns the direction inverse to a. */
	public static Direction back(Direction a) {
		switch (a) {
			case SOUTH:
				return NORTH;
			case EAST:
				return WEST;
			case NORTH:
				return SOUTH;
			case WEST:
				return EAST;
			case NORTHSOUTH:
				return NORTHSOUTH;
			case EASTWEST:
				return EASTWEST;
			case MIDDLE:
				return MIDDLE;
			default:
				throw null;
		}
	}

	/** Returns the direction to the right of a. */
	public static Direction right(Direction a) {
		switch (a) {
			case SOUTH:
				return WEST;
			case EAST:
				return SOUTH;
			case NORTH:
				return EAST;
			case WEST:
				return NORTH;
			case NORTHSOUTH:
				return EASTWEST;
			case EASTWEST:
				return NORTHSOUTH;
			case MIDDLE:
				return MIDDLE;
			default:
				throw null;
		}
	}

	/** Returns the direction containing a and its inverse. */
	public static Direction mirror(Direction a) {
		switch (a) {
			case SOUTH:
			case NORTH:
			case NORTHSOUTH:
				return NORTHSOUTH;
			case EAST:
			case WEST:
			case EASTWEST:
				return EASTWEST;
			case MIDDLE:
				return MIDDLE;
			default:
				throw null;
		}
	}

	/** Returns the direction start has to face to look at the target. */
	public static Direction getDirection(int startX, int startY, int targetX, int targetY) {
		int dx = targetX - startX;
		int dy = targetY - startY;
		if (dx > dy || (dx > 0 && dx == dy)) {
			if (-dy >= dx) {
				return WEST;
			} else {
				return NORTH;
			}
		} else {
			if (-dx >= dy) {
				return EAST;
			} else {
				return SOUTH;
			}
		}
	}

	/** Returns the direction start has to face to look at a target in a straight line. */
	public static Direction getSimpleDirection(int startX, int startY, int targetX, int targetY) {
		if (startX == targetX) {
			if (startY < targetY) {
				return SOUTH;
			} else {
				return NORTH;
			}
		} else {
			if (startX > targetX) {
				return WEST;
			} else {
				return EAST;
			}
		}
	}

	@Override
	public int getID() {
		return id;
	}

	public static Direction getByID(byte parseByte) {
		for (Direction e : values()) {
			if (e.getID() == parseByte) {
				return e;
			}
		}
		assert(false) : ("Direction id " + parseByte + " not found");
		return null;
	}

	public static int length() {
		return values().length;
	}

}
