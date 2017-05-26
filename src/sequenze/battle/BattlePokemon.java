package sequenze.battle;

import sequenze.ExtPokemon;
import sequenze.Statistic;

public class BattlePokemon extends ExtPokemon {

	int[] modstat;

	int[] statmod;

	public BattlePokemon(ExtPokemon extp){

	}

	void recalculateBattleStats() {
		for (int i = 0; i < BattleStatistic.length() - 2; i++) {
			if (statmod[i] >= 0) {
				modstat[i] = (statmod[i] + 2) * 100 / 2;
			} else {
				modstat[i] = 200 / (2 - statmod[i]);
			}
		}
		if (statmod[BattleStatistic.ACCURACY.getID()] >= 0) {
			modstat[BattleStatistic.ACCURACY.getID()] = (3 + statmod[BattleStatistic.ACCURACY.getID()]) * 100 / 3;
		} else {
			modstat[BattleStatistic.ACCURACY.getID()] = 300 / (3 - statmod[BattleStatistic.ACCURACY.getID()]);
		}
		if (statmod[BattleStatistic.EVASION.getID()] >= 0) {
			modstat[BattleStatistic.EVASION.getID()] = 300 / (3 + statmod[BattleStatistic.EVASION.getID()]);
		} else {
			modstat[BattleStatistic.EVASION.getID()] = (3 - statmod[BattleStatistic.EVASION.getID()]) * 100 / 3;
		}
	}

	void changeMod(BattleStatistic stat, int amount) {
		statmod[stat.getID()] += amount;
		if (statmod[stat.getID()] > 6) {
			statmod[stat.getID()] = 6;
		} else if (statmod[stat.getID()] < -6) {
			statmod[stat.getID()] = -6;
		}
		recalculateStats();
	}
}