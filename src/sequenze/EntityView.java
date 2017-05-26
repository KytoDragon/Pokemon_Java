package sequenze;

public abstract class EntityView{
	
	Entity e;
	
	public EntityView(Entity e){
		this.e = e;
	}
	
	abstract boolean hasHardAnimation();
	
	abstract boolean hasAnimation();
	
	abstract void drawLDown();
	
	abstract void drawLUp();
	
	abstract void drawR();
	
	abstract void doAnimationStep();
	
	abstract void setAnimation(EntityStatus es);

	abstract void calcPosition();
	
	abstract void delayBridge();
	
	abstract void exclaim(int id);
}
