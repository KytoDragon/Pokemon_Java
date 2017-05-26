package sequenze;

import static sequenze.Direction.*;

public class Moveroute {
	
	static final String FORWARD = "forward";
	static final String TURN = "turn ";
	static final String TURNLEFT = "turn left";
	static final String TURNRIGHT = "turn right";
	static final String TURNBACK = "turn back";
	static final String TURNNORTH = "turn north";
	static final String TURNSOUTH = "turn south";
	static final String TURNEAST = "turn east";
	static final String TURNWEST = "turn west";
	static final String MOVENORTH = "north";
	static final String MOVESOUTH = "south";
	static final String MOVEEAST = "east";
	static final String MOVEWEST = "west";
	
	static final String JUMP = "jump ";
	static final String JUMPTO = "jumpto ";
	static final String WAIT = "wait ";
	
	public static String getMovement(Direction direction) {
		if (direction == NORTH) {
			return MOVENORTH;
		} else if (direction == SOUTH) {
			return MOVESOUTH;
		} else if (direction == EAST) {
			return MOVEEAST;
		} else {
			return MOVEWEST;
		}
	}
	
	public static String getTurn(Direction direction) {
		if (direction == NORTH) {
			return TURNNORTH;
		} else if (direction == SOUTH) {
			return TURNSOUTH;
		} else if (direction == EAST) {
			return TURNEAST;
		} else {
			return TURNWEST;
		}
	}
	
	public static Direction getDirection(String moveroute) {
		if (moveroute == TURNNORTH || moveroute == MOVENORTH) {
			return NORTH;
		} else if (moveroute == TURNSOUTH || moveroute == MOVESOUTH) {
			return SOUTH;
		} else if (moveroute == TURNEAST || moveroute == MOVEEAST) {
			return EAST;
		} else if (moveroute == TURNWEST || moveroute == MOVEWEST) {
			return WEST;
		} else {
			assert(false) : ("Unknown moveroute command: " + moveroute);
			return null;
		}
	}

	public static char[] getMoveroute(char[] moveroute) {
		return getMoveroute(new String(moveroute)).toCharArray();
	}

	@Deprecated
	public static String getMoveroute(String moveroute) {
		switch (moveroute) {
		case FORWARD:
			return FORWARD;
		case TURNLEFT:
			return TURNLEFT;
		case TURNRIGHT:
			return TURNRIGHT;
		case TURNBACK:
			return TURNBACK;
		case TURNNORTH:
			return TURNNORTH;
		case TURNSOUTH:
			return TURNSOUTH;
		case TURNEAST:
			return TURNEAST;
		case TURNWEST:
			return TURNWEST;
		case MOVENORTH:
			return MOVENORTH;
		case MOVESOUTH:
			return MOVESOUTH;
		case MOVEEAST:
			return MOVEEAST;
		case MOVEWEST:
			return MOVEWEST;
		default:
			if (moveroute.startsWith(JUMP) || moveroute.startsWith(JUMPTO) || moveroute.startsWith(WAIT)) {
				return moveroute;
			}
			assert(false) : ("Unknown moveroute command: " + moveroute);
			return null;
		}
	}
}
