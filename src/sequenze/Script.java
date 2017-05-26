package sequenze;

import script.Code;

public class Script {

	/** The type of the script, determining how it is activated. */
	public byte type;
	/** Activated by pressing the action button in front of the event. */
	public final static byte ACTION = 0;
	// TODO sort out Playertouch and Eventtouch so that trainer and boulder have something to use
	// TODO what to do with boulders, as they move and have different reactions to talking and bumping?
	/** Activated by touching the event. */
	public final static byte PLAYERTOUCH = 1;
	/** Activated by stepping on the event or being in a certain range in the viewed direction. */
	public final static byte EVENTTOUCH = 2;
	/** Activated automatically once. */
	public final static byte AUTOSCRIPT = 3;
	/** Activated automatically every tick. */
	public final static byte PARALLEL = 4;
	/** Activated by a system message. */
	public final static byte MESSAGE = 5;

	public final static byte SIGN = 6;
	/** The message this script is waiting on. */
	public char[] message;
	/** The range in tiles at which the script gets triggered. */
	public byte range;

	public byte moveType;

	public final static byte FIXED = 0;

	public final static byte CUSTOM = 1;

	public final static byte LOOKAROUND = 2;

	public final static byte RANDOM = 3;

	public byte directions;

	/** The default move-route of this event. */
	public char[][] moveroutes;
	/** The index of the default move-route. */
	public int moveroutePos;
	/** Whether the event is visible. */
	public boolean invisible;
	/** Whether the event is animated. */
	public boolean moveanimation;
	/** Whether the event is always displayed above other events. */
	public boolean alwaysontop;
	/** Whether the event can rotate in different directions. */
	public boolean fixeddirection;
	/** Whether the event ignores the terrain. */
	public boolean noclip;

	public Code code;

	public Condition[] conditions;

	public int[] tex;

	public boolean conditionTrue(Event event) {
		for (Condition condition : conditions) {
			if (!condition.isTrue(event)) {
				return false;
			}
		}
		return true;
	}

}
