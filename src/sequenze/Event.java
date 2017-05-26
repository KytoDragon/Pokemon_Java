package sequenze;

import sequenze.world.World;
import util.ConV;
import script.Interpreter;

import static sequenze.Moveroute.*;
import static sequenze.TerrainTag.*;
import static sequenze.Direction.*;
import static sequenze.EntityStatus.*;

public class Event extends Entity {

	/** The scripts for this event. */
	public Script[] scripts;
	/** Script interpreter used by this event. */
	private Interpreter interpreter;
	/** Index of the currently active script. */
	private int active;

	int timer;

	public Event() {
		switches = new boolean[MAXSWITCHNUM];
		// TODO load switches
		ev = new EntityView2D(this);
	}

	/** Checks the conditions of all script to choose the currently active one. */
	public void checkActive() {
		if (interpreter != null && interpreter.isActive()) {
			return;
		}
		for (byte b = 0; b < scripts.length; b++) {
			if (scripts[b].conditionTrue(this)) {
				active = b;
				return;
			}
		}
		active = -1;
	}

	/** Moves the event according to its move routes. */
	public void update(Game game, World world, Event[] es) {
		if (ev.hasHardAnimation()) {
			ev.doAnimationStep();
			return;
		}
		if (timer > 0) {
			timer--;
			return;
		}
		if (hasToMove()) {
			move(game, world);
			return;
		}

		Event ev1 = null;
		for (Event e : es) {
			if (e.x == x - (orientation.getID() - 2) % 2 && e.y == y - (orientation.getID() - 1) % 2) {
				ev1 = e;
			}
		}

		// TODO footprints + floor crack

		TerrainTag[] b = world.getBAround(x, y);
		TerrainTag bdd = b[orientation.getID()];
		TerrainTag bmm = b[MIDDLE.getID()];
		switch (status) {
			case RUNNING:
			case STANDING:
				if (status == RUNNING && bdd == ICE) {
					setStatus(SLIDING);
					break;
				}
				if (bmm.isA(FLING)) {
					if (bmm != FLING) {
						orientation = bmm.getOrientation();
					}
					calcXY();
					setStatus(ROLLING);
				} else if (bmm.isA(ICE)) {
					orientation = bmm.getOrientation();
					calcXY();
					setStatus(SLIDING);
				} else {
					setStatus(STANDING);
				}
				break;
			case WALKING:
				if (bmm == ICE) {
					setStatus(SLIDING);
				} else {
					setStatus(STANDING);
				}
				break;
			case BIKE_DRIVE:
			case BIKE_IDLE:
				if (status == BIKE_DRIVE && bmm == ICE) {
					setStatus(BIKE_SLIDING);
					break;
				}
				if (bmm.isA(FLING)) {
					if (bmm != FLING) {
						orientation = bmm.getOrientation();
					}
					setStatus(STANDING);
				} else if (bmm.isA(ICE)) {
					orientation = bmm.getOrientation();
					calcXY();
					setStatus(BIKE_SLIDING);
				} else {
					setStatus(BIKE_IDLE);
				}
				break;
			case BIKE_SLIDING:
				if (bmm != ICE || bdd == BLOCKED || bdd.isA(WATER) || bdd.isA(CLIMB) || bdd == TREE || (bdd.isA(LEDGE) && bdd.getOrientation() == orientation)) {
					setStatus(BIKE_IDLE);
					break;
				} else if (bdd.getOrientation() == orientation || bdd == LEDGE) {
					calcXY();
					setStatus(BIKE_JUMP);
					break;
				} else {
					calcXY();
					setStatus(BIKE_SLIDING);
				}
				break;
			case BIKE_JUMP:
				if (bmm == ICE) {
					setStatus(BIKE_SLIDING);
				} else {
					setStatus(BIKE_IDLE);
				}

				break;
			case SLIDING:
				if (bmm != ICE || bdd == BLOCKED || bdd.isA(WATER) || bdd.isA(CLIMB) || bdd == TREE || (bdd.isA(LEDGE) && bdd.getOrientation() != orientation)) {
					setStatus(STANDING);
					break;
				} else if (bdd.isA(LEDGE) && bdd.getOrientation() == orientation || bdd == LEDGE) {
					calcXY();
					calcXY();
					setStatus(JUMPING);
					break;
				} else {
					setStatus(SLIDING);
					calcXY();
				}
				break;
			case ROLLING:
				if (bmm.isA(FLING)) {
					if (bmm != FLING) {
						orientation = bmm.getOrientation();
					}
					calcXY();
					setStatus(ROLLING);
				} else if (bmm.isA(ICE)) {
					orientation = bmm.getOrientation();
					calcXY();
					setStatus(SLIDING);
				} else if (ev1 != null && !ev1.noclip()) {
					setStatus(STANDING);
				} else if (bdd == BLOCKED || bdd.isA(CLIMB) || bdd.isA(WATER) || bdd == TREE || (bdd.isA(LEDGE) && bdd.getOrientation() != orientation)) {
					setStatus(STANDING);
				} else if (bdd.isA(LEDGE) && bdd.getOrientation() == orientation || bdd == LEDGE) {
					calcXY();
					calcXY();
					setStatus(JUMPING);
				} else {
					calcXY();
					setStatus(ROLLING);
				}
				break;
			case JUMPING:
				if (bmm == ICE) {
					setStatus(SLIDING);
				} else {
					setStatus(STANDING);
				}
				break;
			case SURFER_IDLE:
				if (bmm.isA(WHIRLPOOL)) {
					orientation = bmm.getOrientation();
					calcXY();
					setStatus(SURFER);
				}
				break;
			case SURFER:
				setStatus(SURFER_IDLE);
				break;
			case DIVE_IDLE:

				break;
			case DIVE:
				setStatus(DIVE_IDLE);
				break;
		}
		if (ev.hasAnimation()) {
			ev.doAnimationStep();
		}
	}

	/** Draws the events part of the left screen. */
	public void drawLDown() {
		ev.calcPosition();
		if (!scripts[active].invisible) {
			ev.drawLDown();
		}
	}

	/** Draws the events part of the left screen. */
	public void drawLUp() {
		if (!scripts[active].invisible) {
			ev.drawLUp();
		}
	}

	/** Draws the events part of the right screen. */
	public void drawR() {
		if (!scripts[active].invisible) {
			ev.drawR();
		}
	}

	public void setStatus(EntityStatus es) {
		status = es;
		ev.setAnimation(es);
	}

	/** Returns whether a custom move route is set. */
	public boolean hasToMove() {
		return customMoveRoute != null;
	}

	/** Moves according to the current custom move route. */
	public void move(Game game, World world) {
		String s = customMoveRoute[moveRouteIndex];
		if (s == FORWARD) {
			calcXY();
			setStatus(WALKING);
		} else if (s == TURNLEFT) {
			orientation = left(orientation);
		} else if (s == TURNRIGHT) {
			orientation = right(orientation);
		} else if (s == TURNBACK) {
			orientation = back(orientation);
		} else if (s.startsWith(TURN)) {
			orientation = getDirection(s);
		} else if (s.startsWith(JUMP)) {
			jumpfromx = x;
			jumpfromy = y;
			x += ConV.getInt(s, JUMP.length());
			y += ConV.getInt(s, s.indexOf(' ', JUMP.length()));
			setStatus(JUMPING);
		} else if (s.startsWith(JUMPTO)) {
			jumpfromx = x;
			jumpfromy = y;
			x = ConV.getInt(s, JUMP.length());
			y = ConV.getInt(s, s.indexOf(' ', JUMP.length()));
			setStatus(JUMPING);
		} else if (s.startsWith(WAIT)) {
			timer = ConV.getInt(s, WAIT.length());
		} else {
			orientation = getDirection(s);
			calcXY();
			setStatus(WALKING);
		}
		moveRouteIndex++;
		if (moveRouteIndex == customMoveRoute.length) {
			customMoveRoute = null;
		}
	}

	/** Returns whether the event is currently solid. */
	public boolean noclip() {
		return scripts[active].noclip;
	}

	/** Starts the script and returns the running interpreter. */
	public Interpreter startScript() {
		if (interpreter == null) {
			interpreter = Interpreter.getNewInterpreter(scripts[active].code, EventMethodLibrary.current(), this);
		} else if (scripts[active].type != Script.PARALLEL || !interpreter.isActive()) {
			interpreter = Interpreter.setCode(interpreter, scripts[active].code);
		}
		return interpreter;
	}

	/** Returns the activation range of the current script. */
	public byte getRange() {
		return scripts[active].range;
	}

	/** Returns the activation type of the current script. */
	public byte getType() {
		return scripts[active].type;
	}

	public char[] getMessage() {
		return scripts[active].message;
	}

	/** Returns the textures of the current script. */
	@Override
	public int[] getTex() {
		return scripts[active].tex;
	}

	@Override
	public void setMoveRoute(String[] list) {
		customMoveRoute = list;
		moveRouteIndex = 0;
	}

	@Override
	public String toString() {
		return "Event, " + name;
	}

	@Override
	public void exclaim(int id) {
		ev.exclaim(id);
	}

}