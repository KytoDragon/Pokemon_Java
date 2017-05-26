package sequenze.overlay;

import sequenze.Direction;
import sequenze.Game;
import static sequenze.InstancePackage.bag;
import static sequenze.InstancePackage.vfx;

public class ItemSelector extends MenuObject {

	// TODO combine with PokemonSelector via Interface?
	private boolean finished;
	private int status = SHUTDOWNOUTSIDE;
	private final static int SHUTDOWNOUTSIDE = 0;
	private final static int STARTUPINSIDE = 1;
	private final static int SELECTION = 2;
	private final static int SHUTDOWNINSIDE = 3;
	private final static int STARTUPOUTSIDE = 4;

	public ItemSelector() {
		bag.initSelection();
		vfx.addShutter(Direction.NORTH, false);
	}

	@Override
	void drawL() {
		switch (status) {
			case STARTUPINSIDE:
			case SELECTION:
			case SHUTDOWNINSIDE:
				bag.drawLSelection();
		}
	}

	@Override
	void drawR() {
		switch (status) {
			case STARTUPINSIDE:
			case SELECTION:
			case SHUTDOWNINSIDE:
				bag.drawRSelection();
		}
	}

	@Override
	void update(Game g, MenuObjectHandler m) {
		switch (status) {
			case SHUTDOWNOUTSIDE:
				if (vfx.shutterFinished()) {
					status = STARTUPINSIDE;
					vfx.addShutter(Direction.NORTH, true);
				}
				break;
			case STARTUPINSIDE:
				if (vfx.shutterFinished()) {
					status = SELECTION;
				}
				break;
			case SELECTION:
				if (!finished && bag.updateSelection(g)) {
					finished = true;
				}
				break;
			case SHUTDOWNINSIDE:
				if (vfx.shutterFinished()) {
					status = STARTUPOUTSIDE;
					vfx.addShutter(Direction.NORTH, true);
				}
				break;
			case STARTUPOUTSIDE:
				if (vfx.shutterFinished()) {
					m.deleteObject();
				}
				break;
		}
	}

	@Override
	boolean getsInput() {
		return !finished;
	}

	@Override
	void close(MenuObjectHandler m) {
		status = SHUTDOWNINSIDE;
		vfx.addShutter(Direction.NORTH, false);
		return;
	}
}
