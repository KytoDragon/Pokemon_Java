package sequenze;

public final class Localisation {

	private Localisation() {
	}

	public static char[] getPauseText() {
		// TODO Auto-generated method stub
		char[] tmp = "Game paused".toCharArray();
		return tmp;
	}

	public static char[][] getTeamOptionNames() {
		// TODO Auto-generated method stub
		char[][] tmp = {"SUMMARY".toCharArray(), "SWITCH".toCharArray(), "ITEM".toCharArray(), "GIVE".toCharArray(), "TAKE".toCharArray(), "USE".toCharArray()}; // names of the option fields
		return tmp;
	}

	public static char[] getTeamChoose() {
		// TODO Auto-generated method stub
		char[] tmp = "Choose a Pokémon.".toCharArray(); // text for the pokemon selection
		return tmp;
	}

	public static char[] getTeamMove() {
		// TODO Auto-generated method stub
		char[] tmp = "Move to where?".toCharArray(); // text for the pokemon selection
		return tmp;
	}

	public static char[] getTeamOptionInfo() {
		// TODO Auto-generated method stub
		char[] tmp = "Do what with\\n\\s0000?".toCharArray(); // text for the pokemon options
		return tmp;
	}

	public static char[] getOptionsTitle() {
		// TODO Auto-generated method stub
		char[] tmp = "OPTIONS".toCharArray();
		return tmp;
	}

	public static char[][] getCharacteristics() {
		// TODO Auto-generated method stub
		char[][] tmp = {"Loves to eat.".toCharArray(), "Proud of its power.".toCharArray(), "Sturdy body.".toCharArray(), "Highly curious.".toCharArray(), "Strong willed.".toCharArray(), "Likes to run.".toCharArray(),
			"Takes plenty of siestas.".toCharArray(), "Likes to thrash about.".toCharArray(), "Capable of taking hits.".toCharArray(), "Mischievous.".toCharArray(), "Somewhat vain.".toCharArray(), "Alert to sounds.".toCharArray(),
			"Nods off a lot.".toCharArray(), "A little quick tempered.".toCharArray(), "Highly persistent.".toCharArray(), "Thoroughly cunning.".toCharArray(), "Strongly defiant.".toCharArray(), "Impetuous and silly.".toCharArray(),
			"Scatters things often.".toCharArray(), "Likes to fight.".toCharArray(), "Good endurance.".toCharArray(), "Often lost in thought.".toCharArray(), "Hates to lose.".toCharArray(), "Somewhat of a clown.".toCharArray(),
			"Likes to relax.".toCharArray(), "Quick tempered.".toCharArray(), "Good perseverance.".toCharArray(), "Very finicky.".toCharArray(), "Somewhat stubborn.".toCharArray(), "Quick to flee.".toCharArray()};
		return tmp;
	}

	public static char[] getPokemonName(int idnr, int form) {
		// TODO Auto-generated method stub
		char[] tmp = InfoLoader.getInternalPokemonName(idnr, form); // name of the pokemon with the given id
		return tmp;
	}

	public static char[] getAttackName(int idnr) {
		// TODO Auto-generated method stub
		char[] tmp = InfoLoader.getInternalAttackName(idnr); // name of the attack with the given id
		return tmp;
	}

	public static char[] getAbilityName(int idnr) {
		// TODO Auto-generated method stub
		char[] tmp = InfoLoader.getInternalAbilityName(idnr); // name of the ability with the given id
		return tmp;
	}

	public static char[] getRibbonName(int idnr) {
		// TODO Auto-generated method stub
		char[] tmp = "Legend Ribbon".toCharArray(); // name of the ribbon with the given id
		return tmp;
	}

	public static char[] getRibbonText(int idnr) {
		// TODO Auto-generated method stub
		char[] tmp = "A Ribbon awarded for setting a\\nlegendary record.".toCharArray();
		return tmp;
	}

	public static char[] getAbilityText(int idnr) {
		// TODO Auto-generated method stub
		char[] tmp = "Powers up Water-type\\nmoves in a pinch.".toCharArray();
		return tmp;
	}

	public static char[] getAttackText(int idnr) {
		// TODO Auto-generated method stub
		char[] tmp = "The user cloaks\\nitself in fire and\\ncharges at the foe.\\nThe user sustains\\nserious damage, too.".toCharArray();
		return tmp;
	}

	public static char[][] getTeamInfoNames() {
		// TODO Auto-generated method stub
		char[][] tmp = {"Dex No.".toCharArray(), "Name".toCharArray(), "Type".toCharArray(), "OT".toCharArray(), "ID No.".toCharArray(), "Exp. Points".toCharArray(), "To Next Lv.".toCharArray()}; // names of the info fields
		return tmp;
	}

	public static char[][] getTeamInfoStatNames() {
		// TODO Auto-generated method stub
		char[][] tmp = {"Attack".toCharArray(), "Defense".toCharArray(), "Speed".toCharArray(), "Sp. Atk".toCharArray(), "Sp. Def.".toCharArray()};
		return tmp;
	}

	public static char[][] getMenuItems() {
		// TODO Auto-generated method stub
		char[][] tmp = {"POKéDEX".toCharArray(), "\\s1000".toCharArray(), "POKéMON".toCharArray(), "SAVE".toCharArray(), "BAG".toCharArray(), "OPTIONS".toCharArray(), "POKéGEAR".toCharArray(), "???".toCharArray()}; // list of names for the menu items
		return tmp;
	}

	public static char[] getMenuTitle() {
		// TODO Auto-generated method stub
		char[] tmp = "MENU".toCharArray(); // title on top of the menu
		return tmp;
	}

	public static char[][] getMenuABox() {
		// TODO Auto-generated method stub
		char[][] tmp = {"CHECK".toCharArray(), "NEXT".toCharArray(), "TALK".toCharArray()}; // list of texts displayed on the A button box
		return tmp;
	}

	public static char[] getItemName(int idnr) {
		// TODO Auto-generated method stub
		char[] tmp = InfoLoader.getInternalItemName(idnr); // Name of the given Item
		return tmp;
	}

	public static char[] getBagItemDesc(int i) {
		// TODO Auto-generated method stub
		if (i == 0) {
			return new char[0];
		}
		char[] tmp = "A spray-type medicin for wounds.\\nIt restores the HP of one Pokémon by\\njust 20 points.".toCharArray(); // description of the item with the given id
		return tmp;
	}

	public static char[] getBagSubDesc(int i) {
		// TODO Auto-generated method stub
		char[] tmp = "Item Pocket\\nFor items that will be useful during your\\nadventure.".toCharArray(); // description of the given sub-bag, 0 is the base bag
		return tmp;
	}

	public static char[] getEventText(int i) {
		// TODO Should not be here, store text with map like d/p/p/hg/ss
		char[] tmp = ("Translated text nr. " + i).toCharArray();
		return tmp;
	}

	public static char[][] getBagOptions() {
		// TODO Auto-generated method stub
		char[][] tmp = new char[][]{"USE".toCharArray(), "TRASH".toCharArray(), "DESELECT".toCharArray(), "REGISTER".toCharArray(), "GIVE".toCharArray(), "MOVE".toCharArray()};
		return tmp;
	}

	public static char[][] getOptionsText() {
		// TODO Auto-generated method stub
		char[][] tmp = new char[][]{"TEXT SPEED".toCharArray(), "BATTLE SCENE".toCharArray(), "BATTLE STYLE".toCharArray(), "SOUND".toCharArray(), "BUTTON MODE".toCharArray(), "FRAME".toCharArray(), "???".toCharArray(), "???".toCharArray()};
		return tmp;
	}

	public static char[][] getOptionsOptions() {
		// TODO Auto-generated method stub
		char[][] tmp = new char[][]{"SLOW".toCharArray(), "MID".toCharArray(), "FAST".toCharArray(),
			"ON".toCharArray(), "OFF".toCharArray(),
			"SHIFT".toCharArray(), "SET".toCharArray(),
			"STEREO".toCharArray(), "MONO".toCharArray(),
			"NORMAL".toCharArray(), "L=A".toCharArray()};
		return tmp;
	}

	public static char[] getOptionsFrameLabel(int i) {
		// TODO Auto-generated method stub
		char[] tmp = ("WINDOW TYPE " + (i + 1)).toCharArray();
		return tmp;
	}

	public static char[][] getOptionsBackButton() {
		// TODO Auto-generated method stub
		char[][] tmp = {"CONFIRM".toCharArray(), "QUIT".toCharArray()};
		return tmp;
	}

	public static char[] getNatureName(Nature nature) {
		// TODO Auto-generated method stub
		char[] tmp = nature.name().toCharArray();
		return tmp;
	}

	public static char[][] getMonths() {
		// TODO Auto-generated method stub
		char[][] tmp = {"Jan.".toCharArray(), "Feb.".toCharArray(), "Mar.".toCharArray(), "Apr.".toCharArray(), "May".toCharArray(), "Jun.".toCharArray(), "Jul.".toCharArray(), "Aug.".toCharArray(), "Sep.".toCharArray(), "Oct.".toCharArray(), "Nov.".toCharArray(), "Dec.".toCharArray()};
		return tmp;
	}

	public static char[] getInfoPageText(EncounterType enc) {
		// TODO Auto-generated method stub
		char[] tmp;
		switch (enc) {
			case Gift:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nRecieved at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			case Egg:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s000A\\c00\\nEgg recieved.\\n\\s0008 \\s0007, \\s0009\\n\\c04\\s0004\\c00\\nEgg hatched.\\n\\s0006".toCharArray();
				break;
			case Event:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nHad a fateful encounter\\nat Lv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case Outside:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nMet at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			case Cave:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nMet at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			case Water:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nMet at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			case Underwater:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nMet at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			case Sky:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nMet at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			case Building:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nMet at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			case Safari:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nMet at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			case Overworld:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nMet at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			case CryptoStolen:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nMet at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			case CryptoGift:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nMet at Lv. \\s0005.\\n\\n\\s0006".toCharArray();
				break;
			default:
				assert (false) : ("null");
				return null;

		}
		return tmp;
	}

	public static char[] getInfoPageTradedText(EncounterType enc) {
		// TODO Auto-generated method stub
		char[] tmp;
		switch (enc) {
			case Gift:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently recieved at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case Egg:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s000A\\c00\\nEgg recieved.\\n\\s0008 \\s0007, \\s0009\\n\\c04\\s0004\\c00\\nEgg apparently hatched.\\n\\s0006".toCharArray();
				break;
			case Event:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently had a\\nfateful encounter at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case Outside:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently met at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case Cave:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently met at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case Water:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently met at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case Underwater:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently met at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case Sky:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently met at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case Building:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently met at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case Safari:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently met at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case Overworld:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently met at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case CryptoStolen:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently met at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			case CryptoGift:
				tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\nApparently met at\\nLv. \\s0005.\\n\\s0006".toCharArray();
				break;
			default:
				assert (false) : ("null");
				return null;

		}
		return tmp;
	}

	public static char[][] getInfoPageEggText() {
		// TODO Auto-generated method stub
		// TODO there is a version (manaphy egg) that says recieved at instead of in
		char[][] tmp = new char[][]{"\\s0002 \\s0001, \\s0003\\nA mysterious Pokémon\\nEgg recieved in\\n\\c04\\s0004\\c00.\\n\\n“The Egg Watch”\\nSounds can be heard\\ncomming from inside!\\nIt will hatch soon!".toCharArray(),
			"\\s0002 \\s0001, \\s0003\\nA mysterious Pokémon\\nEgg recieved in\\n\\c04\\s0004\\c00.\\n\\n“The Egg Watch”\\nIt appears to move\\noccasionally. It may be\\nclose to hatching.".toCharArray(),
			"\\s0002 \\s0001, \\s0003\\nA mysterious Pokémon\\nEgg recieved in\\n\\c04\\s0004\\c00.\\n\\n“The Egg Watch”\\nWhat will hatch from\\nthis? It doesn't seem\\nclose to hatching.".toCharArray(),
			"\\s0002 \\s0001, \\s0003\\nA mysterious Pokémon\\nEgg recieved in\\n\\c04\\s0004\\c00.\\n\\n“The Egg Watch”\\nIt looks like this\\nEgg will take a long\\ntime to hatch.".toCharArray()};
		return tmp;
	}

	public static char[] getInfoPageFarAwayText() {
		// TODO Auto-generated method stub
		char[] tmp = "\\c04\\s0000\\c00 nature.\\n\\s0002 \\s0001, \\s0003\\n\\c04\\s0004\\c00\\Lv. \\s0005.\\n\\s0006".toCharArray();
		return tmp;
	}

	public static char[] getRegionName(int caughtregion) {
		// TODO Auto-generated method stub
		char[] tmp = "Far away".toCharArray();
		return tmp;
	}

	public static char[] getLocationName(int caughtlocation) {
		// TODO Auto-generated method stub
		char[] tmp = "Route 1337".toCharArray();
		return tmp;
	}

	public static char[] getTeamOptionsBackButton() {
		// TODO Auto-generated method stub
		char[] tmp = "QUIT".toCharArray();
		return tmp;
	}

	public static char[] getTeamBackButton() {
		// TODO Auto-generated method stub
		char[] tmp = "CANCEL".toCharArray();
		return tmp;
	}

	public static char[] getTeamInfoBackButton() {
		// TODO Auto-generated method stub
		char[] tmp = "Cancel".toCharArray();
		return tmp;
	}

	public static char[] getEggName() {
		// TODO Auto-generated method stub
		char[] tmp = "Egg".toCharArray();
		return tmp;
	}

	public static char[][] getTrashButtons() {
		// TODO Auto-generated method stub
		char[][] tmp = new char[][]{"TRASH".toCharArray(), "CANCEL".toCharArray()};
		return tmp;
	}

	public static char[] getBagCancel() {
		// TODO Auto-generated method stub
		char[] tmp = "CANCEL".toCharArray();
		return tmp;
	}

	public static char[] getBagSelection() {
		// TODO Auto-generated method stub
		char[] tmp = "The \\s0000 item is selected.".toCharArray();
		return tmp;
	}

	public static char[] getBagMove() {
		// TODO Auto-generated method stub
		char[] tmp = "Move the \\s0000.".toCharArray();
		return tmp;
	}

	public static char[] getTeamItemTitle() {
		// TODO Auto-generated method stub
		char[] tmp = "ITEM".toCharArray();
		return tmp;
	}

	public static char[][] getSummaryTitleL() {
		// TODO Auto-generated method stub
		char[][] tmp = new char[][]{"TRAINER-MEMO".toCharArray(), "SKILLS".toCharArray(), "PERFORMANCE".toCharArray()};
		return tmp;
	}

	public static char[][] getSummaryTitleR() {
		// TODO Auto-generated method stub
		char[][] tmp = new char[][]{"INFO".toCharArray(), "BATTLE MOVES".toCharArray(), "RIBBONS".toCharArray(), new char[0], new char[0]};
		return tmp;
	}

	public static char[][] getUseItemText() {
		// TODO Auto-generated method stub
		char[][] tmp = new char[][]{"ABLE!".toCharArray(), "UNABLE!".toCharArray(), "LEARNED".toCharArray()};
		return tmp;
	}

	public static char[] getBagAmount() {
		// TODO Auto-generated method stub
		char[] tmp = "x\\s0000".toCharArray();
		return tmp;
	}

	public static char[] getOptionsTextStyle(int i) {
		// TODO Auto-generated method stub
		char[] tmp = ("TYPE " + (i + 1)).toCharArray();
		return tmp;
	}

	public static char[] getSummaryMaxLevelEP() {
		// TODO Auto-generated method stub
		char[] tmp = "0".toCharArray();
		return tmp;
	}

	public static char[] getSummaryNoAttack() {
		// TODO Auto-generated method stub
		char[] tmp = "-".toCharArray();
		return tmp;
	}

	public static char[] getSummaryRibbonNumber() {
		// TODO Auto-generated method stub
		char[] tmp = "No. of Ribbons:    \\s0000".toCharArray();
		return tmp;
	}

	public static char[] getSummaryShinyLeafs() {
		// TODO Auto-generated method stub
		char[] tmp = "Shiny Leaf".toCharArray();
		return tmp;
	}

	public static char[] getSummaryEmptyMove() {
		// TODO Auto-generated method stub
		char[] tmp = "--".toCharArray();
		return tmp;
	}

	public static char[] getSummaryPPTitle() {
		// TODO Auto-generated method stub
		char[] tmp = "PP".toCharArray();
		return tmp;
	}

	public static char[] getSummaryPP() {
		// TODO Auto-generated method stub
		char[] tmp = "\\s0000/\\s0001".toCharArray();
		return tmp;
	}

	public static char[] getSummaryHPTitle() {
		// TODO Auto-generated method stub
		char[] tmp = "PP".toCharArray();
		return tmp;
	}

	public static char[] getSummaryHP() {
		// TODO Auto-generated method stub
		char[] tmp = "\\s0000/\\s0001".toCharArray();
		return tmp;
	}

	public static char[] getSummaryAbilityTitle() {
		// TODO Auto-generated method stub
		char[] tmp = "Ability".toCharArray();
		return tmp;
	}

	public static char[] getSummaryLevel() {
		// TODO Auto-generated method stub
		char[] tmp = "[Lv]․\\s0000".toCharArray();
		return tmp;
	}

	public static char[] getSummaryItemTitle() {
		// TODO Auto-generated method stub
		char[] tmp = "Item".toCharArray();
		return tmp;
	}

	public static char[][] getSummaryMoveTitles() {
		// TODO Auto-generated method stub
		char[][] tmp = {"CATEGORY".toCharArray(), "POWER".toCharArray(), "ACCURACY".toCharArray()};
		return tmp;
	}

	public static char[] getSummarySwitchMoves() {
		// TODO Auto-generated method stub
		char[] tmp = "Switch".toCharArray();
		return tmp;
	}

	public static char[] getSummaryEmptyMoveStat() {
		// TODO Auto-generated method stub
		char[] tmp = "---".toCharArray();
		return tmp;
	}

	public static char[] getTeamHP() {
		// TODO Auto-generated method stub
		char[] tmp = "\\s0000 ／\\s0001".toCharArray();
		return tmp;
	}

	public static char[] getTeamLevel() {
		// TODO Auto-generated method stub
		char[] tmp = "[Lv]․\\s0000".toCharArray();
		return tmp;
	}

	public static char[][] getCardTitles() {
		// TODO Auto-generated method stub
		char[][] tmp = {"ID No.".toCharArray(), "MONEY".toCharArray(), "POKEDEX".toCharArray(), "SCORE".toCharArray(), "TIME".toCharArray(), "ADVENTURE STARTED".toCharArray(), "NAME".toCharArray()};
		return tmp;
	}

	public static char[] getCardMoney() {
		// TODO Auto-generated method stub
		char[] tmp = "$\\s0000".toCharArray();
		return tmp;
	}

	public static char[] getCardTime() {
		// TODO Auto-generated method stub
		char[] tmp = "\\s0000:\\s0001\\s0002".toCharArray();
		return tmp;
	}

	public static char[] getCardDate() {
		// TODO Auto-generated method stub
		char[] tmp = "\\s0001 \\s0000, \\s0002".toCharArray();
		return tmp;
	}

	public static char[] getCardBackButton() {
		// TODO Auto-generated method stub
		char[] tmp = "Cancel".toCharArray();
		return tmp;
	}
}
