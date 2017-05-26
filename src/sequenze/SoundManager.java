package sequenze;

public class SoundManager {

	private static SoundHandler sh;
	private static final String AUDIOPATH = "lib/sounds/";

	public static void update() {
		sh.update();
	}

	public static void freeze() {
		pauseBGM();
	}

	public static void unfreeze() {
		unpauseBGM();
	}

	public static void startBGM(String name) {
		sh.startBGM(AUDIOPATH + name);
	}

	public static void stopBGM() {
		sh.stopBGM();
	}

	public static void pauseBGM() {
		sh.pauseBGM();
	}

	public static void unpauseBGM() {
		sh.unpauseBGM();
	}

	public static void fadeOutBGM(int frames) {
		sh.fadeOutBGM(frames);
	}

	public static void playSE(String name, boolean active) {
		sh.playSE(AUDIOPATH + name, active);
	}

	public static boolean isActive() {
		return sh.isActive();
	}

	/** Sets the given instance of this class. */
	public static void set(SoundHandler soundHandler) {
		sh = soundHandler;
	}

	public static void close() {
		if (sh != null) {
			sh.close();
		}
	}
}
