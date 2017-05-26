package sequenze.battle;

import sequenze.Game;

public interface BattleActor {
	
	public void chooseAction(BattleMenu bh, Game g);
	
	public void chooseAttack(BattleMenu bh, Game g);
	
	public void choosePokemon(BattleMenu bh, Game g);
	
	public void drawL(BattleMenu bh);
	
	public void drawR(BattleMenu bh);
}
