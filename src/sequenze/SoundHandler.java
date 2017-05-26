package sequenze;

public abstract class SoundHandler {

	protected abstract void update();

	protected abstract void startBGM(String name);

	protected abstract void stopBGM();

	protected abstract void pauseBGM();

	protected abstract void unpauseBGM();

	protected abstract void playSE(String name, boolean active);

	protected abstract void close();

	protected abstract boolean isActive();

	protected abstract void fadeOutBGM(int frames);

}
