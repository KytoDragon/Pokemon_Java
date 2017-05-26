package sequenze.awt;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;
import sequenze.SoundHandler;
import util.Logger;

import java.io.File;

public class SoundHandlerAWT extends SoundHandler {

	private boolean waiting;
	private int fade_frames;
	private int fade_current;
	private ReportClipWhenDone reportWhenDone;
	private AudioContext ac;
	private SamplePlayer bgm;
	private Gain g;

	public SoundHandlerAWT() {
		reportWhenDone = new ReportClipWhenDone();
		ac = new AudioContext(new JavaSoundAudioIO());
		g = new Gain(ac, 2, 1f);
		ac.out.addInput(g);
		ac.start();
	}

	@Override
	protected void update() {
		if (fade_frames == 0 || bgm == null) {
			return;
		}
		fade_current++;
		if (fade_current >= fade_frames) {
			pauseBGM();
			g.setGain(1.0f);
			fade_frames = 0;
			return;
		}
		float dB = 1.0f - (float) (fade_current) / (float) fade_frames;
		g.setGain(dB);
	}

	@Override
	protected void startBGM(String name) {
		if (bgm != null) {
			stopBGM();
		}
		Sample sample = getAudioFile(name);
		if (sample == null) {
			return;
		}
		bgm = new SamplePlayer(ac, sample);
		g.addInput(bgm);

		bgm.setLoopType(SamplePlayer.LoopType.LOOP_ALTERNATING);
		bgm.start();
	}

	@Override
	protected void stopBGM() {
		if (bgm != null) {
			bgm.kill();
			bgm = null;
			fade_frames = 0;
		}
	}

	@Override
	protected void pauseBGM() {
		if (bgm != null) {
			bgm.pause(true);
		}
	}

	@Override
	protected void unpauseBGM() {
		if (bgm != null) {
			bgm.pause(false);
		}
	}

	@Override
	protected void fadeOutBGM(int frames) {
		if (frames == 0) {
			pauseBGM();
		}
		fade_current = 0;
		fade_frames = frames;
	}

	@Override
	protected void playSE(String name, boolean active) {
		Sample sample = getAudioFile(name);
		if (sample == null) {
			return;
		}
		SamplePlayer player = new SamplePlayer(ac, sample);
		if (active) {
			waiting = true;
			player.setKillListener(reportWhenDone);
		}
		player.setKillOnEnd(true);
		ac.out.addInput(player);
	}

	@Override
	protected boolean isActive() {
		return waiting;
	}

	private static Sample getAudioFile(String fileName) {
		try {
			return SampleManager.sample(fileName);
		} catch (IllegalArgumentException e) {
			File f = new File(fileName);
			if (f.exists()) {
				Logger.add(Logger.AUDIO, "Could not read audio file: ", fileName);
				//Logger.add(Logger.AUDIO, e.getCause().getCause().getMessage());
			} else {
				Logger.add(Logger.FILE, "Could not find audio file: ", fileName);
			}
			return null;
		}
	}

	private class ReportClipWhenDone extends Bead {
		@Override
		public void messageReceived(Bead message) {
			waiting = false;
		}
	}

	@Override
	public void close() {
		if (bgm != null) {
			stopBGM();
		}
		ac.stop();
	}

}
