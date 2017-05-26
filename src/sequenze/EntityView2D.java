package sequenze;

import static sequenze.Direction.*;
import static sequenze.EntityStatus.*;
import util.ConV;

public class EntityView2D extends EntityView {
	
	int animationmax = 0;
	int runStep = 0; // current animation for running
	int animation = 0; // animation index
	int cameraOffsetX = 0;
	int cameraOffsetY = 0;
	int currentTex = 0;
	boolean delayedBridge = false;
	
	static final int walking_max = 11;
	static final int running_max = 6;
	static final int bike_on_max = 4;
	static final int bike_off_max = 4;
	static final int bike_drive_max = 12;
	static final int bike_jump_max = 8;
	static final int bike_bump_max = 8;
	static final int bike_sliding_max = 12;
	static final int falling_on_max = 8;
	static final int falling_off_max = 8;
	static final int sliding_max = 8;
	static final int rolling_max = 16;
	static final int jumping_max = 16;
	static final int surfer_on_max = 8;
	static final int surfer_off_max = 8;
	static final int surfer_max = 4;
	static final int bump_max = 16;
	static final int climbing_on_max = 16;
	static final int climbing_off_max = 16;
	static final int climbing_up_max = 16;
	static final int climbing_down_max = 16;
	static final int kaskade_on_max = 16;
	static final int kaskade_off_max = 16;
	static final int kaskade_up_max = 8;
	static final int kaskade_down_max = 8;
	static final int fly_on_max = 16;
	static final int fly_off_max = 16;
	static final int dive_on_max = 16;
	static final int dive_off_max = 16;
	static final int dive_deeper_max = 16;
	static final int dive_max = 16;

	public EntityView2D(Entity e) {
		super(e);
	}
	
	@Override
	boolean hasHardAnimation() {
		return animationmax > 0 && e.status != BUMP && e.status != BIKE_BUMP;
	}
	
	@Override
	boolean hasAnimation() {
		return animationmax > 0;
	}
	
	@Override
	void doAnimationStep() {
		animation++;
		if (animation == animationmax) {
			animationmax = 0;
			delayedBridge = false;
		}
	}
	
	@Override
	void calcPosition() {
		int x = e.x;
		int y = e.y;
		int xf = x * 16 - 8;
		int yf = y * 16 - 16;
		cameraOffsetX = 0;
		cameraOffsetY = 0;
		Direction orientation = e.orientation;
		int[] tex = e.getTex();
		int delta = 0;
		if(animationmax > 0){
			delta = 16 - animation * 16 / animationmax;
		}
		switch (e.status) {
		case RUNNING:
			if(animation >= 5){
				runStep++;
				runStep %= 4;
			}
			if (runStep % 2 == 1) {
				currentTex = tex[orientation.getID() * 6 + 3];
			} else {
				currentTex = tex[orientation.getID() * 6 + 4 + runStep / 2];
			}
			if(animation >= 5){
				runStep += 3;
				runStep %= 4;
			}
			
			if (orientation == SOUTH) {
				yf -= delta;
			} else if (orientation == EAST) {
				xf -= delta;
			} else if (orientation == NORTH) {
				yf += delta;
			} else if (orientation == WEST) {
				xf += delta;
			}
			break;
		case STANDING:
			currentTex = tex[orientation.getID() * 6];
			break;
		case WALKING:
			if (animation < 3 || animation > 7) {
				currentTex = tex[orientation.getID() * 6];
			} else {
				currentTex = tex[orientation.getID() * 6 + 1 + (x + y) % 2];
			}
			if (orientation == SOUTH) {
				yf -= delta;
			} else if (orientation == EAST) {
				xf -= delta;
			} else if (orientation == NORTH) {
				yf += delta;
			} else if (orientation == WEST) {
				xf += delta;
			}
			break;
		case BIKE_IDLE:
			currentTex = tex[orientation.getID() * 6 + 24];
			break;
		case BIKE_DRIVE:
			//currentTex = tex[orientation * 6 + 24];
			if (animation * e.speed < 3 || animation * e.speed > 9) {
				if(animation * e.speed < 3 != ((x + y) % 2 == 1)){
					currentTex = tex[orientation.getID() * 6 + 24 + 3];
				}else{
					currentTex = tex[orientation.getID() * 6 + 24 + 1];
				}
			} else {
				currentTex = tex[orientation.getID() * 6 + 2 + ((x + y) % 2) * 2 + 24];
			}
			if (animation != 0) {
				if (orientation == SOUTH) {
					yf -= 12 - animation * e.speed;
				} else if (orientation == EAST) {
					xf -= 12 - animation * e.speed;
				} else if (orientation == NORTH) {
					yf += 12 - animation * e.speed;
				} else if (orientation == WEST) {
					xf += 12 - animation * e.speed;
				}
			}
			break;
		case BIKE_ON:
			if (animation < 8) {
				currentTex = tex[orientation.getID() * 6];
			} else {
				currentTex = tex[orientation.getID() * 6 + 24];
			}
			break;
		case BIKE_OFF:
			if (animation < 8) {
				currentTex = tex[orientation.getID() * 6 + 24];
			} else {
				currentTex = tex[orientation.getID() * 6];
			}
			break;
		case BIKE_SLIDING:
			currentTex = tex[orientation.getID() * 6 + 24];
			if (animation != 0) {
				if (orientation == SOUTH) {
					yf -= delta;
				} else if (orientation == EAST) {
					xf -= delta;
				} else if (orientation == NORTH) {
					yf += delta;
				} else if (orientation == WEST) {
					xf += delta;
				}
			}
			break;
		case BIKE_JUMP:
			break;
		case BIKE_BUMP:
			break;
		case FALLING_ON:
			break;
		case FALLING_OFF:
			break;
		case SLIDING:
			currentTex = tex[orientation.getID() * 6];
			if (orientation == SOUTH) {
				yf -= delta;
			} else if (orientation == EAST) {
				xf -= delta;
			} else if (orientation == NORTH) {
				yf += delta;
			} else if (orientation == WEST) {
				xf += delta;
			}
			break;
		case ROLLING:
			if (animation < 4) {
				currentTex = tex[orientation.getID() * 6];
			} else if (animation < 8) {
				currentTex = tex[left(orientation).getID() * 6];
			} else if (animation < 12) {
				currentTex = tex[back(orientation).getID() * 6];
			} else {
				currentTex = tex[right(orientation).getID() * 6];
			}
			if (orientation == SOUTH) {
				yf -= delta;
			} else if (orientation == EAST) {
				xf -= delta;
			} else if (orientation == NORTH) {
				yf += delta;
			} else if (orientation == WEST) {
				xf += delta;
			}
			break;
		case JUMPING:
			if (orientation == SOUTH) {
				yf -= delta * 2;
				cameraOffsetY = 4 - ConV.abs((animation - 8) / 2);
			} else if (orientation == EAST) {
				xf -= delta * 2;
				cameraOffsetY = 4 - ConV.abs((animation - 8) / 2);
			} else if (orientation == NORTH) {
				yf += delta * 2;
				cameraOffsetY = 4 - ConV.abs((animation - 8) / 2);
			} else if (orientation == WEST) {
				xf += delta * 2;
				cameraOffsetY = 4 - ConV.abs((animation - 8) / 2);
			}
			break;
		case SURFER_ON:
			break;
		case SURFER_OFF:
			break;
		case SURFER_IDLE:
			currentTex = tex[orientation.getID() * 6 + 29];
			break;
		case SURFER:
			currentTex = tex[orientation.getID() * 6 + 29];
			if (orientation == SOUTH) {
				yf -= delta;
			} else if (orientation == EAST) {
				xf -= delta;
			} else if (orientation == NORTH) {
				yf += delta;
			} else if (orientation == WEST) {
				xf += delta;
			}
			break;
		case BUMP:
			if (animation < 4 || animation > 12) {
				currentTex = tex[orientation.getID() * 6];
			} else {
				currentTex = tex[orientation.getID() * 6 + 1 + runStep % 2];
			}
			break;
		case CLIMBING_ON:
			break;
		case CLIMBING_OFF:
			break;
		case CLIMBING_UP:
			break;
		case CLIMBING_DOWN:
			break;
		case KASKADE_ON:
			break;
		case KASKADE_OFF:
			break;
		case KASKADE_UP:
			break;
		case KASKADE_DOWN:
			break;
		case FLY_ON:
			break;
		case FLY_OFF:
			break;
		case DIVE_ON:
			break;
		case DIVE_OFF:
			break;
		case DIVE_DEEPER:
			break;
		case DIVE_IDLE:
			break;
		case DIVE:
			break;
		}
		e.xf = xf;
		e.yf = yf;
	}
	
	@Override
	void drawR() {
	}
	
	@Override
	void setAnimation(EntityStatus ps) {
		if (animationmax == 0) {
			animation = 0;
		}
		switch (ps) {
		case BIKE_BUMP:
			animationmax = bike_bump_max;
			break;
		case BIKE_DRIVE:
			animationmax = bike_drive_max / e.speed; // TODO figure out speed
			break;
		case BIKE_IDLE:
			animationmax = 0;
			break;
		case BIKE_JUMP:
			animationmax = bike_jump_max;
			break;
		case BIKE_OFF:
			animationmax = bike_off_max;
			break;
		case BIKE_ON:
			animationmax = bike_on_max;
			break;
		case BIKE_SLIDING:
			animationmax = bike_sliding_max;
			break;
		case BUMP:
			if (animationmax == 0) {
				animationmax = bump_max;
				runStep++;
				runStep %= 2;
			}
			break;
		case CLIMBING_DOWN:
			animationmax = climbing_down_max;
			break;
		case CLIMBING_OFF:
			animationmax = climbing_off_max;
			break;
		case CLIMBING_ON:
			animationmax = climbing_on_max;
			break;
		case CLIMBING_UP:
			animationmax = climbing_up_max;
			break;
		case DIVE:
			animationmax = dive_max;
			break;
		case DIVE_DEEPER:
			animationmax = dive_deeper_max;
			break;
		case DIVE_IDLE:
			animationmax = 0;
			break;
		case DIVE_OFF:
			animationmax = dive_off_max;
			break;
		case DIVE_ON:
			animationmax = dive_on_max;
			break;
		case FALLING_OFF:
			animationmax = falling_off_max;
			break;
		case FALLING_ON:
			animationmax = falling_on_max;
			break;
		case FLY_OFF:
			animationmax = fly_off_max;
			break;
		case FLY_ON:
			animationmax = fly_on_max;
			break;
		case JUMPING:
			animationmax = jumping_max;
			break;
		case KASKADE_DOWN:
			animationmax = kaskade_down_max;
			break;
		case KASKADE_OFF:
			animationmax = kaskade_off_max;
			break;
		case KASKADE_ON:
			animationmax = kaskade_on_max;
			break;
		case KASKADE_UP:
			animationmax = kaskade_up_max;
			break;
		case ROLLING:
			animationmax = rolling_max;
			break;
		case RUNNING:
			animationmax = running_max;
			runStep++;
			runStep %= 4;
			break;
		case SLIDING:
			animationmax = sliding_max;
			break;
		case STANDING:
			animationmax = 0;
			if (runStep % 2 == 1) {
				runStep += 3;
				runStep %= 4;
			}
			break;
		case SURFER:
			animationmax = surfer_max;
			break;
		case SURFER_IDLE:
			animationmax = 0;
			break;
		case SURFER_OFF:
			animationmax = surfer_off_max;
			break;
		case SURFER_ON:
			animationmax = surfer_on_max;
			break;
		case WALKING:
			animationmax = walking_max;
			break;
		}
	}

	@Override
	void drawLDown() {
		if(e.bridge == delayedBridge){
			GraphicHandler.drawRectangle(e.x * 16, e.y * 16, 16, 16, 255, 0, 255, 64);
			GraphicHandler.drawBox(e.xf - cameraOffsetX, e.yf - cameraOffsetY, currentTex);
		}
	}
	
	@Override
	void drawLUp(){
		if(e.bridge != delayedBridge){
			GraphicHandler.drawRectangle(e.x * 16, e.y * 16, 16, 16, 255, 0, 255, 64);
			GraphicHandler.drawBox(e.xf - cameraOffsetX, e.yf - cameraOffsetY, currentTex);
		}
	}
	
	@Override
	void delayBridge(){
		delayedBridge = true;
	}

	@Override
	void exclaim(int id) {
		// TODO
	}
	
}
