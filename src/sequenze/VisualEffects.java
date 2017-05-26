package sequenze;

public class VisualEffects {

	private Direction direction;
	private boolean open;
	private int animation = -1;

	public int getCameraX(int playerX) {
		// TODO Auto-generated method stub

		return playerX;
	}

	public int getCameraY(int playerY) {
		// TODO Auto-generated method stub

		return playerY;
	}

	public void update() {
		// TODO Auto-generated method stub
		if (animation >= 0) {
			animation--;
		}
	}

	public void drawL() {
		// TODO Auto-generated method stub
		// NOTE we draw frame 0 in order to cover up the 1 frame gap between switching menu components (while the message is sent, but not recieved)
		if (animation == -1 || animation == 0 && open) {
			return;
		}
		if (open) {
			switch (direction) {
				case NORTH:
					GraphicHandler.translateStep(0, 16 - animation, 16);
					GraphicHandler.drawRectangleMax(0, 0, 0);
					GraphicHandler.translateStep(0, -16 + animation, 16);
					break;
			}
		} else {
			switch (direction) {
				case NORTH:
					GraphicHandler.translateStep(0, -animation, 16);
					GraphicHandler.drawRectangleMax(0, 0, 0);
					GraphicHandler.translateStep(0, animation, 16);
					break;
			}
		}
	}

	public void drawR() {
		// TODO Auto-generated method stub
		if (animation == -1 || animation == 0 && !open) {
			return;
		}
		if (open) {
			switch (direction) {
				case NORTH:
					GraphicHandler.translateStep(0, 16 - animation, 16);
					GraphicHandler.drawRectangleMax(0, 0, 0);
					GraphicHandler.translateStep(0, -16 + animation, 16);
					break;
			}
		} else {
			switch (direction) {
				case NORTH:
					GraphicHandler.translateStep(0, -animation, 16);
					GraphicHandler.drawRectangleMax(0, 0, 0);
					GraphicHandler.translateStep(0, animation, 16);
					break;
			}
		}
	}

	public void addShutter(Direction direction, boolean open) {
		this.open = open;
		this.direction = direction;
		switch (direction) {
			case NORTH:
			case SOUTH:
				animation = 16;
				break;
			case EAST:
			case WEST:
				animation = 32;
				break;
		}
	}

	public boolean shutterFinished() {
		return animation == 0;
	}

	public boolean active() {
		return animation <= 0;
	}
}
