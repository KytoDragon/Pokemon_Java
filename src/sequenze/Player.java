package sequenze;

import sequenze.world.World;
import static sequenze.TerrainTag.*;
import static sequenze.Direction.*;
import static sequenze.EntityStatus.*;
import script.ByteCode;
import script.Interpreter;

public class Player extends Entity {

	/** List of Texture indices. */
	int[] tex;
	
	Interpreter interpreter;
	
	boolean moved = false;
	
	public Player() {
		tex = GraphicHandler.giveTextures("Hero");
		ev = new EntityView2D(this);
		interpreter = Interpreter.getNewInterpreter(new ByteCode(1, 0, new byte[0]), EventMethodLibrary.current(), this);
	}
	
	/** Draws to the right Screen, currently unused. */
	public void drawR() {
		ev.drawR();
	}
	
	/** Draws the player model to the left screen inbetween the world. */
	public void drawLDown() {
		ev.drawLDown();
	}
	
	/** Draws the player model to the left screen above the world. */
	public void drawLUp() {
		ev.drawLUp();
	}
	
	/** Updates the player with the existing key presses. */
	public void update(Game g, World world) {
		if (ev.hasHardAnimation()) {
			ev.doAnimationStep();
			return;
		}
		TerrainTag[] b = world.getBAround(x, y);
		short[] h = world.getHAt(x, y);
		Event[] es = world.getEvents(x, y);
		int[] pos = world.toMapCoord(x, y);
		int map_x = pos[0];
		int map_y = pos[1];
		Event ev0 = null;
		Event ev1 = null;
		Event ev2 = null;
		for (Event e : es) {
			int dx = (orientation.getID() - 2) % 2;
			int dy = map_y - (orientation.getID() - 1) % 2;
			if (e.x == map_x && e.y == map_y) {
				ev0 = e;
			} else if (e.x == map_x - dx && e.y == dy) {
				ev1 = e;
			} else if (e.x == map_x - dx * 2 && e.y == map_y - dy * 2) {
				ev2 = e;
			}
		}
		if (moved) {
			moved = false;
			if (ev0 != null && ev0.getType() == Script.EVENTTOUCH && ev0.getRange() == 0) {
				stopMovement();
				MessageHandler.add(MessageHandler.EVENT, ev0.startScript());
				return;
			}
		}
		
		for (Event e : es) {
			if (e.getType() == Script.EVENTTOUCH) {
				if (e.x == map_x) {
					int dy = map_y - e.y;
					if (e.orientation == SOUTH && dy > 0 && e.getRange() >= dy) {
						// TODO check if path is clear
						stopMovement();
						MessageHandler.add(MessageHandler.EVENT, e.startScript());
						return;
					} else if (e.orientation == NORTH && dy < 0 && e.getRange() <= -dy) {
						stopMovement();
						MessageHandler.add(MessageHandler.EVENT, e.startScript());
						return;
					}
				} else if (e.y == map_y) {
					int dx = map_x - e.x;
					if (e.orientation == WEST && dx > 0 && e.getRange() >= dx) {
						stopMovement();
						MessageHandler.add(MessageHandler.EVENT, e.startScript());
						return;
					} else if (e.orientation == EAST && dx < 0 && e.getRange() <= -dx) {
						stopMovement();
						MessageHandler.add(MessageHandler.EVENT, e.startScript());
						return;
					}
				}
			}
		}

		// TODO footprints + floor crack

		TerrainTag bdd = b[orientation.getID()];
		TerrainTag bmm = b[MIDDLE.getID()];
		switch (status) {
		case WALKING:
		case RUNNING:
		case STANDING:
            if (status != STANDING && bmm == ICE) {
				calcXY();
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
			} else if (g.bike) {
				setStatus(BIKE_ON);
			} else if (g.wasPressed(Button.DOWN) && orientation != SOUTH) {
				g.process(Button.DOWN);
				orientation = SOUTH;
			} else if (g.wasPressed(Button.RIGHT) && orientation != EAST) {
				g.process(Button.RIGHT);
				orientation = EAST;
			} else if (g.wasPressed(Button.UP) && orientation != NORTH) {
				g.process(Button.UP);
				orientation = NORTH;
			} else if (g.wasPressed(Button.LEFT) && orientation != WEST) {
				g.process(Button.LEFT);
				orientation = WEST;
			} else if (g.wasPressed(Button.A)
					&& ((ev1 != null && (ev1.getType() == Script.ACTION || ev1.getType() == Script.SIGN)) || (bdd.isA(WATER) && !bridge) || bdd.isA(CLIMB) || bdd.isA(COUNTER)
							&& bdd != COUNTER || (bdd == COUNTER && ev2 != null && ev2.getType() == Script.ACTION))) {
				g.process(Button.A);
				if (ev1 != null && (ev1.getType() == Script.ACTION || ev1.getType() == Script.SIGN)) {
					MessageHandler.add(MessageHandler.EVENT, ev1.startScript());
				} else if (bdd.isA(WATER) && !bridge) {
					interpreter = Interpreter.setScript(interpreter, "tiles/water");
					MessageHandler.add(MessageHandler.EVENT, interpreter);
				} else if (bdd.isA(CLIMB)) {
					interpreter = Interpreter.setScript(interpreter, "tiles/climb");
					MessageHandler.add(MessageHandler.EVENT, interpreter);
				} else {
					switch (bdd) {
					case TREE:
						interpreter = Interpreter.setScript(interpreter, "tiles/tree");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case COUNTER:
						if (ev2 != null) {
							MessageHandler.add(MessageHandler.EVENT, ev2.startScript());
						}
						break;
					case PC:
						interpreter = Interpreter.setScript(interpreter, "tiles/pc");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case MAP:
						interpreter = Interpreter.setScript(interpreter, "tiles/map");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case TV:
						interpreter = Interpreter.setScript(interpreter, "tiles/tv");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case CYCLESTAND:
						interpreter = Interpreter.setScript(interpreter, "tiles/cyclestand");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case BOOKCASE1:
						interpreter = Interpreter.setScript(interpreter, "tiles/bookcase1");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case BOOKCASE2:
						interpreter = Interpreter.setScript(interpreter, "tiles/bookcase2");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case TRASHCAN:
						interpreter = Interpreter.setScript(interpreter, "tiles/trashcan");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case GOODSSHELF1:
						interpreter = Interpreter.setScript(interpreter, "tiles/goodsshelf1");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case GOODSSHELF2:
						interpreter = Interpreter.setScript(interpreter, "tiles/goodsshelf2");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					default:
						assert(false) : ("Terrain tag " + bdd + " is not correctly defined!");
						return;
					}
				}
			} else if (g.keyDown(Button.getDirection(orientation))) {
				g.process(Button.getDirection(orientation));
				if (ev1 != null && !ev1.noclip()) {
					if (ev1.getType() == Script.PLAYERTOUCH || ev1.getType() == Script.SIGN) {
						MessageHandler.add(MessageHandler.EVENT, ev1.startScript());
						setStatus(STANDING);
					} else {
						setStatus(BUMP);
					}
				} else if (bdd == BLOCKED || (bdd.isA(WATER) && !bdd.isA(BRIDGE)) || bdd.isA(CLIMB) || bdd == TREE || (bdd.isA(LEDGE) && bdd.getOrientation() != orientation)
						|| (bmm.isA(FREE) && (bmm.getOrientation() == orientation || bmm.getOrientation() == Direction.mirror(orientation)))
						|| (bdd.isA(FREE) && (bdd.getOrientation() == Direction.back(orientation) || bdd.getOrientation() == Direction.mirror(orientation)))
						|| !bridge && (bdd == BRIDGEOVERWATER || bdd == BRIDGEBLOCKED || bdd == BEAMLEFTRIGHTBLOCKED || bdd == BEAMLEFTRIGHTWATER || bdd == BEAMUPDOWNBLOCKED || bdd == BEAMUPDOWNWATER)
						|| bridge  && bmm != BRIDGESTART && (!bdd.isA(BRIDGE) || bdd.isA(BEAM))) {
					setStatus(BUMP);
				} else if (bdd.isA(LEDGE) && bdd.getOrientation() == orientation || bdd == LEDGE) {
					jumpfromx = x;
					jumpfromy = y;
					calcXY();
					calcXY();
					setStatus(JUMPING);
				} else {
					calcXY();
					if(!bridge && bdd == BRIDGESTART){
						bridge = true;
					}else if(bridge && !bdd.isA(BRIDGE)){
						bridge = false;
						ev.delayBridge();
					}
					if (SystemControll.getFlag(SystemControll.ShoeOverride) || SystemControll.getFlag(SystemControll.ShoesUnlocked) && g.keyDown(Button.B)) {
						setStatus(RUNNING);
					} else {
						setStatus(WALKING);
					}
				}
			} else if (g.keyDown(Button.DOWN)) {
				orientation = SOUTH;
			} else if (g.keyDown(Button.RIGHT)) {
				orientation = EAST;
			} else if (g.keyDown(Button.UP)) {
				orientation = NORTH;
			} else if (g.keyDown(Button.LEFT)) {
				orientation = WEST;
			} else {
				setIdleStatus(STANDING);
			}
			break;
		case BIKE_DRIVE:
		case BIKE_IDLE:
            if (status == BIKE_DRIVE && bmm == ICE) {
				calcXY();
				setStatus(BIKE_SLIDING);
				break;
			}
			if (bmm.isA(FLING)) {
				if (bmm != FLING) {
					orientation = bmm.getOrientation();
				}
				g.bike = false;
				speed = 0;
				setStatus(BIKE_OFF);
			} else if (bmm.isA(ICE)) {
				orientation = bmm.getOrientation();
				calcXY();
				speed = 2;
				setStatus(BIKE_SLIDING);
			} else if (!g.bike) {
				speed = 0;
				setStatus(BIKE_OFF);
			} else if (g.wasPressed(Button.DOWN) && orientation != SOUTH) {
				g.process(Button.DOWN);
				orientation = SOUTH;
			} else if (g.wasPressed(Button.RIGHT) && orientation != EAST) {
				g.process(Button.RIGHT);
				orientation = EAST;
			} else if (g.wasPressed(Button.UP) && orientation != NORTH) {
				g.process(Button.UP);
				orientation = NORTH;
			} else if (g.wasPressed(Button.LEFT) && orientation != WEST) {
				g.process(Button.LEFT);
				orientation = WEST;
			} else if (g.keyDown(Button.getDirection(orientation))) {
				if (ev1 != null && !ev1.noclip()) {
					if (ev1.getType() == Script.PLAYERTOUCH || ev1.getType() == Script.SIGN) {
						MessageHandler.add(MessageHandler.EVENT, ev1.startScript());
					} else {
						setStatus(BIKE_IDLE);
						speed = 0;
					}
				} else if (bdd == BLOCKED || bdd.isA(WATER) || bdd.isA(CLIMB) || bdd == TREE || bdd == HIGHGRASS
						|| (bdd.isA(LEDGE) && bdd.getOrientation() != orientation)
						|| (bmm.isA(FREE) && (bmm.getOrientation() == orientation || bmm.getOrientation() == Direction.mirror(orientation)))
						|| (bdd.isA(FREE) && (bdd.getOrientation() == Direction.back(orientation) || bdd.getOrientation() == Direction.mirror(orientation)))) {
					speed = 0;
					setStatus(BIKE_IDLE);
				} else if (bdd.isA(LEDGE) && bdd.getOrientation() == orientation || bdd == LEDGE) {
					jumpfromx = x;
					jumpfromy = y;
					calcXY();
					calcXY();
					setStatus(BIKE_JUMP);
				} else {
					calcXY();
					if (speed < g.maxBikeSpeed) {
						speed++;
					}
					setStatus(BIKE_DRIVE);
				}
			} else if (g.keyDown(Button.DOWN)) {
				orientation = SOUTH;
			} else if (g.keyDown(Button.RIGHT)) {
				orientation = EAST;
			} else if (g.keyDown(Button.UP)) {
				orientation = NORTH;
			} else if (g.keyDown(Button.LEFT)) {
				orientation = WEST;
			} else {
				speed = 0;
				setIdleStatus(BIKE_IDLE);
			}
			break;
		case BIKE_ON:
			setIdleStatus(BIKE_IDLE);
			break;
		case BIKE_OFF:
			setIdleStatus(STANDING);
			break;
		case BIKE_SLIDING:
			if (bmm != ICE || bdd == BLOCKED || bdd.isA(WATER) || bdd.isA(CLIMB) || bdd == TREE || (bdd.isA(LEDGE) && bdd.getOrientation() != orientation)) {
				setIdleStatus(BIKE_IDLE);
				break;
			} else if (bdd.getOrientation() == orientation || bdd == LEDGE) {
				jumpfromx = x;
				jumpfromy = y;
				calcXY();
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
				setIdleStatus(BIKE_IDLE);
			}

			break;
		case BIKE_BUMP:
			setStatus(BIKE_IDLE);
			break;
		case FALLING_ON:
			// TODO
			setStatus(FALLING_OFF);
			break;
		case FALLING_OFF:
			if (g.bike) {
				setIdleStatus(BIKE_IDLE);
			} else {
				setIdleStatus(STANDING);
			}
			break;
		case SLIDING:
			if ((bmm != ICE && (!bmm.isA(ICE) || bmm.getOrientation() != orientation)) || bdd == BLOCKED || bdd.isA(WATER) || bdd.isA(CLIMB) || bdd == TREE || (bdd.isA(LEDGE) && bdd.getOrientation() != orientation)) {
				setIdleStatus(STANDING);
				break;
			} else if (bdd.isA(LEDGE) && bdd.getOrientation() == orientation || bdd == LEDGE) {
				jumpfromx = x;
				jumpfromy = y;
				calcXY();
				calcXY();
				setStatus(JUMPING);
				break;
			} else {
				calcXY();
				setStatus(SLIDING);
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
				setIdleStatus(STANDING);
				if (ev1.getType() == Script.PLAYERTOUCH || ev1.getType() == Script.SIGN) {
					MessageHandler.add(MessageHandler.EVENT, ev1.startScript());
				}
			} else if (bdd == BLOCKED || bdd.isA(CLIMB) || bdd.isA(WATER) || bdd == TREE || (bdd.isA(LEDGE) && bdd.getOrientation() != orientation)) {
				setIdleStatus(STANDING);
			} else if (bdd.isA(LEDGE) && bdd.getOrientation() == orientation || bdd == LEDGE) {
				jumpfromx = x;
				jumpfromy = y;
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
				calcXY();
				setStatus(SLIDING);
			} else {
				setIdleStatus(STANDING);
			}
			break;
		case SURFER_ON:
			setIdleStatus(SURFER_IDLE);
			break;
		case SURFER_OFF:
			if (bmm == ICE) {
				calcXY();
				setStatus(SLIDING);
			} else {
				setIdleStatus(STANDING);
			}
			break;
		case WATER_SPIN:
			orientation = Direction.back(orientation);
			calcXY();
			setStatus(SURFER);
			break;
		case SURFER:
		case SURFER_IDLE:
			if (bmm.isA(WHIRLPOOL)) {
				orientation = bmm.getOrientation();
				calcXY();
				setStatus(SURFER);
			} else if (bmm == WHIRLPOOL){
				setStatus(WATER_SPIN);
			} else if (bmm.isA(WATERFALL)) {
				if (bmm != WATERFALL) {
					orientation = bmm.getOrientation();
				} else {
					if ((b[SOUTH.getID()] == WATER || b[SOUTH.getID()] == WATERFALL) && (b[NORTH.getID()] == WATER || b[NORTH.getID()] == WATERFALL)) {
						if (h[SOUTH.getID()] < h[NORTH.getID()]) {
							orientation = SOUTH;
						} else {
							orientation = NORTH;
						}
					} else {
						if (h[EAST.getID()] < h[WEST.getID()]) {
							orientation = EAST;
						} else {
							orientation = WEST;
						}
					}
				}
				calcXY();
				setStatus(KASKADE_DOWN);
			} else if (g.wasPressed(Button.DOWN) && orientation != SOUTH) {
				g.process(Button.DOWN);
				orientation = SOUTH;
			} else if (g.wasPressed(Button.RIGHT) && orientation != EAST) {
				g.process(Button.RIGHT);
				orientation = EAST;
			} else if (g.wasPressed(Button.UP) && orientation != NORTH) {
				g.process(Button.UP);
				orientation = NORTH;
			} else if (g.wasPressed(Button.LEFT) && orientation != WEST) {
				g.process(Button.LEFT);
				orientation = WEST;
			} else if (g.wasPressed(Button.A)
					&& ((ev1 != null && (ev1.getType() == Script.ACTION || ev1.getType() == Script.SIGN)) || bdd.isA(WATERFALL) || bmm == UNDERWATERENTRY || bdd == WHIRLPOOL || bdd.isA(COUNTER)
							&& bdd != COUNTER || (bdd == COUNTER && ev2 != null && ev2.getType() == Script.ACTION))) {
				g.process(Button.A);
				if (ev1 != null && (ev1.getType() == Script.ACTION || ev1.getType() == Script.SIGN)) {
					MessageHandler.add(MessageHandler.EVENT, ev1.startScript());
				} else if (bdd.isA(WATERFALL)) {
					interpreter = Interpreter.setScript(interpreter, "tiles/waterfall");
					MessageHandler.add(MessageHandler.EVENT, interpreter);
				} else if (bmm == UNDERWATERENTRY) {
					interpreter = Interpreter.setScript(interpreter, "tiles/dive");
					MessageHandler.add(MessageHandler.EVENT, interpreter);
				} else if (bmm == WHIRLPOOL) {
					interpreter = Interpreter.setScript(interpreter, "tiles/whirlpool");
					MessageHandler.add(MessageHandler.EVENT, interpreter);
				} else {
					switch (bdd) {
					case TREE:
						interpreter = Interpreter.setScript(interpreter, "tiles/tree");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case COUNTER:
						if (ev2 != null) {
							MessageHandler.add(MessageHandler.EVENT, ev2.startScript());
						}
						break;
					case PC:
						interpreter = Interpreter.setScript(interpreter, "tiles/pc");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case MAP:
						interpreter = Interpreter.setScript(interpreter, "tiles/map");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case TV:
						interpreter = Interpreter.setScript(interpreter, "tiles/tv");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case BOOKCASE1:
						interpreter = Interpreter.setScript(interpreter, "tiles/bookcase1");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case BOOKCASE2:
						interpreter = Interpreter.setScript(interpreter, "tiles/bookcase2");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case TRASHCAN:
						interpreter = Interpreter.setScript(interpreter, "tiles/trashcan");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case GOODSSHELF1:
						interpreter = Interpreter.setScript(interpreter, "tiles/goodsshelf1");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					case GOODSSHELF2:
						interpreter = Interpreter.setScript(interpreter, "tiles/goodsshelf2");
						MessageHandler.add(MessageHandler.EVENT, interpreter);
						break;
					default:
						assert(false) : ("Terrain tag " + bdd + " is not correctly defined!");
						return;
					}
				}
			} else if (g.keyDown(Button.getDirection(orientation))) {
				if (ev1 != null && !ev1.noclip()) {
					if (ev1.getType() == Script.PLAYERTOUCH || ev1.getType() == Script.SIGN) {
						MessageHandler.add(MessageHandler.EVENT, ev1.startScript());
					} else {
						setIdleStatus(SURFER_IDLE);
					}
				} else if (bdd.isA(WATER) && (bdd != WHIRLPOOL || SystemControll.getFlag(SystemControll.WaterSpin))) {
					calcXY();
					setStatus(SURFER);
				} else if (!bdd.isA(BLOCKED) && !bdd.isA(BRIDGE) && (!bdd.isA(FREE) || bdd.getOrientation() != mirror(orientation) || bdd.getOrientation() != back(orientation))
						&& !bdd.isA(CLIMB)) {
					calcXY();
					setStatus(SURFER_OFF);
				}
			} else if (g.keyDown(Button.DOWN)) {
				orientation = SOUTH;
			} else if (g.keyDown(Button.RIGHT)) {
				orientation = EAST;
			} else if (g.keyDown(Button.UP)) {
				orientation = NORTH;
			} else if (g.keyDown(Button.LEFT)) {
				orientation = WEST;
			}else{
				setIdleStatus(SURFER_IDLE);
			}
			break;
		case BUMP:
			if (!g.keyDown(Button.getDirection(orientation)) || g.wasPressed(Button.DOWN) || g.wasPressed(Button.RIGHT) || g.wasPressed(Button.UP)
					|| g.wasPressed(Button.LEFT) || (ev1 == null || ev1.noclip()) && bdd != BLOCKED && !bdd.isA(WATER) && !bdd.isA(CLIMB) && bdd != TREE
					&& (!bdd.isA(LEDGE) || bdd.getOrientation() == orientation)) {
				// TODO clean up condition
				setIdleStatus(STANDING);
			} else {
				setStatus(BUMP);
			}
			break;
		case CLIMBING_ON:
			if (bmm == CLIMB) {
				calcXY();
				if (h[MIDDLE.getID()] > h[orientation.getID()]) {
					setStatus(CLIMBING_UP);
				} else {
					setStatus(CLIMBING_DOWN);
				}
			} else if (bmm.isA(CLIMB) && bmm.getOrientation() == orientation) {
				calcXY();
				setStatus(CLIMBING_UP);
			} else if (bmm.isA(CLIMB) && bmm.getOrientation() == back(orientation)) {
				calcXY();
				setStatus(CLIMBING_DOWN);
			}
			break;
		case CLIMBING_OFF:
			setStatus(STANDING);
			break;
		case CLIMBING_UP:
			if (bmm != bdd) {
				setStatus(CLIMBING_OFF);
			} else {
				setStatus(CLIMBING_UP);
			}
			calcXY();
			break;
		case CLIMBING_DOWN:
			if (bmm != bdd) {
				setStatus(CLIMBING_OFF);
			} else {
				setStatus(CLIMBING_DOWN);
			}
			calcXY();
			break;
		case KASKADE_ON:
			calcXY();
			setStatus(KASKADE_UP);
			break;
		case KASKADE_OFF:
			setStatus(SURFER_IDLE);
			break;
		case KASKADE_UP:
			if (bdd != bmm) {
				calcXY();
				setStatus(KASKADE_OFF);
			} else {
				calcXY();
				setStatus(KASKADE_UP);
			}
			break;
		case KASKADE_DOWN:
			if (bdd != bmm) {
				calcXY();
				setStatus(KASKADE_OFF);
			} else {
				setStatus(KASKADE_DOWN);
			}
			break;
		case FLY_ON:
			setStatus(FLY_OFF);
			break;
		case FLY_OFF:
			setStatus(STANDING);
			break;
		case DIVE_ON:
			setStatus(DIVE_IDLE);
			break;
		case DIVE_OFF:
			setStatus(DIVE_IDLE);
			break;
		case DIVE_DEEPER:
			setStatus(DIVE_IDLE);
			break;
		case DIVE_IDLE:
			if (g.wasPressed(Button.DOWN)) {
				g.process(Button.DOWN);
				orientation = SOUTH;
			} else if (g.wasPressed(Button.RIGHT)) {
				g.process(Button.RIGHT);
				orientation = EAST;
			} else if (g.wasPressed(Button.UP)) {
				g.process(Button.UP);
				orientation = NORTH;
			} else if (g.wasPressed(Button.LEFT)) {
				g.process(Button.LEFT);
				orientation = WEST;
			} else if (g.keyDown(Button.getDirection(orientation))) {
				if (ev1 != null && !ev1.noclip()) {
					if (ev1.getType() == Script.PLAYERTOUCH || ev1.getType() == Script.SIGN) {
						MessageHandler.add(MessageHandler.EVENT, ev1.startScript());
					} else {
						setIdleStatus(DIVE_IDLE);
					}
				} else if (bdd.isA(UNDERWATER)) {
					calcXY();
					setStatus(DIVE);
				}
			} else if (g.keyDown(Button.DOWN)) {
				orientation = SOUTH;
			} else if (g.keyDown(Button.RIGHT)) {
				orientation = EAST;
			} else if (g.keyDown(Button.UP)) {
				orientation = NORTH;
			} else if (g.keyDown(Button.LEFT)) {
				orientation = WEST;
			}
			break;
		case DIVE:
			setIdleStatus(DIVE_IDLE);
			break;
		}
		if (ev.hasAnimation()) {
			ev.doAnimationStep();
		}
	}
	
	public void stopMovement() {
		switch (status) {
		case BIKE_BUMP:
		case BIKE_DRIVE:
		case BIKE_IDLE:
		case BIKE_JUMP:
		case BIKE_ON:
		case BIKE_SLIDING:
			setIdleStatus(BIKE_IDLE);
			break;
		case BIKE_OFF:
		case BUMP:
		case CLIMBING_DOWN:
		case CLIMBING_OFF:
		case CLIMBING_ON:
		case CLIMBING_UP:
		case FALLING_OFF:
		case FALLING_ON:
		case FLY_OFF:
		case FLY_ON:
		case JUMPING:
		case ROLLING:
		case RUNNING:
		case SLIDING:
		case STANDING:
		case SURFER_OFF:
		case WALKING:
			setIdleStatus(STANDING);
			break;
		case DIVE:
		case DIVE_DEEPER:
		case DIVE_IDLE:
		case DIVE_ON:
			setIdleStatus(DIVE_IDLE);
			break;
		case DIVE_OFF:
		case KASKADE_DOWN:
		case KASKADE_OFF:
		case KASKADE_ON:
		case KASKADE_UP:
		case SURFER:
		case SURFER_IDLE:
		case SURFER_ON:
			setIdleStatus(SURFER_IDLE);
			break;
		}
	}
	
	public void setStatus(EntityStatus ps) {
		status = ps;
		ev.setAnimation(ps);
		moved = true;
	}
	
	public void setIdleStatus(EntityStatus ps) {
		status = ps;
		ev.setAnimation(ps);
	}
	
	/** Returns whether a custom move route is set. */
	public boolean hasToMove() {
		return customMoveRoute != null;
	}
	
	@Override
	public void setMoveRoute(String[] list) {
		customMoveRoute = list;
		moveRouteIndex = 0;
	}
	
	/** Moves according to the current custom move route. */
	public void move(Game game, World world) {
		// TODO move using custom move route
	}
	
	public void calcPosition() {
		ev.calcPosition();
	}

	@Override
	public int[] getTex() {
		return tex;
	}

	@Override
	public void exclaim(int id) {
		ev.exclaim(id);
	}
}