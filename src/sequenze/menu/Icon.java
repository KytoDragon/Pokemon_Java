package sequenze.menu;

import sequenze.GraphicHandler;
//-Djava.library.path="C:\Users\Ulki99\Entwicklung\Java\lwjgl-2.7.1\native\windows;C:\Users\Ulki99\Entwicklung\Java\libraries\dll"  -ea -XXaltjvm=dcevm -javaagent:"C:\Users\Ulki99\Entwicklung\Java\libraries\hotswap-agent.jar"

public class Icon {

	public DTD[] normal;
	public DTD[] sel;
	public DTD[][] anim;
	public int[] frames_a;
	public DTD[][] sel_anim;
	public int[] frames_sa;
	public int duration;
	public int sel_duration;

	public Icon() {
	}

	public Icon(int t, int st, int at) {
		set(t, st, at);
	}

	void drawSelectedAnimation(int animation) {
		if (sel_anim == null) {
			drawSelectedTexture();
			return;
		}
		int current = frames_sa.length;
		while (current > 0 && frames_sa[current - 1] > animation) {
			current--;
		}
		for (DTD dtd : sel_anim[current]) {
			dtd.draw();
		}
	}

	void drawAnimation(int animation) {
		if (anim == null) {
			drawTexture();
			return;
		}
		int current = frames_a.length;
		while (current > 0 && frames_a[current - 1] > animation) {
			current--;
		}
		for (DTD dtd : anim[current]) {
			dtd.draw();
		}
	}

	void drawSelectedTexture() {
		if (sel == null) {
			drawTexture();
			return;
		}
		for (DTD dtd : sel) {
			dtd.draw();
		}
	}

	void drawTexture() {
		if (normal == null) {
			return;
		}
		for (DTD dtd : normal) {
			dtd.draw();
		}
	}

	int getAnimationDuration(boolean selected) {
		if (selected) {
			return sel_duration;
		} else {
			return duration;
		}
	}

	public void set(int t, int st, int at) {
		duration = 16;
		sel_duration = 16;
		if (t != 0) {
			normal = new DTD[]{new DTD(t)};
		}

		if (t == 0 && st != 0) {
			sel = new DTD[]{new DTD(st)};
		} else if (t != 0 && st != 0) {
			sel = new DTD[]{new DTD(t), new DTD(st)};
		}

		if (at != 0) {
			frames_a = new int[]{5, 13};
			anim = new DTD[][]{{new DTD(t)}, {new DTD(at)}, {new DTD(t)}};
		}

		if (at != 0 && st != 0) {
			frames_sa = new int[]{5, 13};
			sel_anim = new DTD[][]{{new DTD(t), new DTD(st)}, {new DTD(at), new DTD(st)}, {new DTD(t), new DTD(st)}};
		} else if (at == 0 && st != 0) {
			frames_sa = new int[]{3, 7, 11, 15};
			sel_anim = new DTD[][]{{new DTD(t), new DTD(st)}, {new DTD(t)}, {new DTD(t), new DTD(st)}, {new DTD(t)}, {new DTD(t), new DTD(st)}};
		}
	}

	public Icon moveSel(int x, int y) {
		sel[sel.length - 1].x = x;
		sel[sel.length - 1].y = y;
		for (DTD[] dtds : sel_anim) {
			if(dtds.length == 2){
				dtds[1].x = x;
				dtds[1].y = y;
			}
		}
		return this;
	}

	public void setReplacement(int t) {
		if (normal == null) {
			normal = new DTD[1];
			normal[0] = new DTD(0);
		}
		normal[0].t = t;
	}

	public static class DTD {

		int t;
		int x;
		int y;

		public DTD(int texture) {
			this(texture, 0, 0);
		}

		public DTD(int texture, int x, int y) {
			this.t = texture;
			this.x = x;
			this.y = y;
		}

		public void draw() {
			GraphicHandler.drawBox(x, y, t);
		}
	}

}
