package mapedit;

import script.ByteCode;

public class Script {
	
	/** The type of the script, determining how it is activated. */
	byte type;
	/** Activated by pressing the action button in front of the event. */
	final static byte ACTION = 0;
	/** Activated by touching the event. */
	final static byte PLAYERTOUCH = 1;
	/** Activated by stepping on the event or being in a certain range in the viewed direction. */
	final static byte EVENTTOUCH = 2;
	/** Activated automatically once. */
	final static byte AUTOSCRIPT = 3;
	/** Activated automatically every tick. */
	final static byte PARALLEL = 4;
	/** Activated by a system message. */
	final static byte MESSAGE = 5;
	
	final static byte SIGN = 6;
	/** The message this script is waiting on. */
	String message;
	/** The range in tiles at which the script gets triggered. */
	byte range;
	
	byte moveType;
	
	final static byte FIXED = 0;
	
	final static byte CUSTOM = 1;
	
	final static byte LOOKAROUND = 2;
	
	final static byte RANDOM = 3;
	
	byte directions;
	
	/** The default move-route of this event. */
	String[] moveroutes;
	/** Whether the event is visible. */
	boolean invisible;
	/** Whether the event is animated. */
	boolean moveanimation;
	/** Whether the event is always displayed above other events. */
	boolean alwaysontop;
	/** Whether the event can rotate in different directions. */
	boolean fixeddirection;
	/** Whether the event ignores the terrain. */
	boolean noclip;
	
	/** The lines of the script */
	String lines;
	
	boolean isRaw;
	
	ByteCode code;
	
	Condition[] conditions;
	
	int tex;
	
	/* @Override public Script clone() { Script s = new Script(); s.type = type; s.message = message; s.range = range; s.moveType = moveType; s.directions =
	 * directions; if (moveroutes == null) { s.moveroutes = null; } else { s.moveroutes = moveroutes.clone(); } s.invisible = invisible; s.moveanimation =
	 * moveanimation; s.alwaysontop = alwaysontop; s.fixeddirection = fixeddirection; s.noclip = noclip; s.lines = lines.clone(); s.conditions = new
	 * Condition[conditions.length]; for (int i = 0; i < s.conditions.length; i++) { s.conditions[i] = conditions[i].clone(); } return s; } */
	
}
