package mapedit;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import script.ByteCode;
import script.ScriptException;
import sequenze.EventMethodLibrary;

public class Event {
	
	/** Name of this event. */
	String name;
	/** X coordinate of the event. */
	int x;
	/** Y coordinate of the event. */
	int y;
	
	int z;
	
	boolean bridge;
	/** Orientation of the event. */
	byte orientation;
	/** The scripts for this event. */
	Script[] scripts;
	
	int current;
	
	/** Draws the events part of the left screen. */
	public void draw(Graphics g, BufferedImage[] tex) {
		int currentTex = scripts[current].tex;
		g.drawImage(tex[currentTex], x * 32 - 16, y * 32 - 32, 64, 64, null);
	}
	
	public void compile(ScriptException sc) {
		for (Script s : scripts) {
			if (!s.isRaw) {
				s.code = new script.Compiler().compile(s.lines.split("[ \\t]*(\\r?[;\\n][ \\t]*)+"), sc, EventMethodLibrary.current());
				if(sc.isInitialized()){
					return;
				}
			}
		}
	}

	public static Event getNew() {
		Event event = new Event();
		event.name = "New Event";
		event.scripts = new Script[1];
		event.scripts[0] = new Script();
		event.scripts[0].lines = "";
		event.scripts[0].isRaw = true;
		event.scripts[0].code = new ByteCode(0, 0, new byte[0]);
		event.scripts[0].conditions = new Condition[0];

		return event;
	}
	
	/* @Override public Event clone(){ Event e = new Event(); e.name = name; e.x = x; e.y = y; e.z = z; e.bridge = bridge; e.orientation = orientation;
	 * e.scripts = new Script[scripts.length]; for (int i = 0; i < e.scripts.length; i++) { e.scripts[i] = (Script) scripts[i].clone(); } return e; } */
	
}