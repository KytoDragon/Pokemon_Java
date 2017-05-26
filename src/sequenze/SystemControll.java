package sequenze;

public final class SystemControll {

	private static boolean[] sysFlags = new boolean[50];
	private static int[] sysVariables = new int[50];
	private static char[][] sysStrings = new char[50][];

	// Flags
	/** Whether the game can be paused. */
	public final static int Pausable = 0;
	/** Whether the game screen will be drawn while paused. */
	public final static int HideWhilePause = 1;
	/** Whether the pokedex is unlocked. */
	public final static int PokedexUnlocked = 2;
	/** Whether the trainer id is unlocked. */
	public final static int IDUnlocked = 3;
	/** Whether the pokemon team is unlocked. */
	public final static int TeamUnlocked = 4;
	/** Whether the save option is unlocked. */
	public final static int SaveUnlocked = 5;
	/** Whether the item bag is unlocked. */
	public final static int BagUnlocked = 6;
	/** Whether the options page is unlocked. */
	public final static int OptionsUnlocked = 7;
	/** Whether the gadget is unlocked. */
	public final static int GadgetUnlocked = 8;
	/** Whether the XXX (last menu option) is unlocked. */
	public final static int XXXUnlocked = 9;
	/** Whether a event awaits the completion of a move. */
	public final static int CompleteMoveRoutes = 10;
	/** The answer to the last yes-no question. */
	public final static int QuestionAnswer = 11;
	/** Whether an empty spot in a multiple trainer battle gets transfered to a team member. */
	public final static int TransferBattleSpot = 12;
	/** Whether the player has won their last battle. */
	public final static int BattleWon = 13;
	/** Whether the player has caught a pokemon in their last battle. */
	public final static int PokemonCaught = 14;
	/** Whether the player can surf onto whirlpool tiles (and get rejected). */
	public final static int WaterSpin = 15;
	/** Whether the game is currently waiting for a sound to complete. */
	public final static int WaitForSound = 16;
	/** Whether the player has recieved the running shoes. */
	public final static int ShoesUnlocked = 17;
	/** Whether the running shoes are enabled by default. */
	public final static int ShoeOverride = 18;
	/** Whether the player can access the menu. */
	public final static int MenuAvailable = 19;
	/** Whether animations are displayed in battle. */
	public final static int BattleScene = 20;
	/** Whether the player pokemon can be changed on a enemy KO. */
	public final static int BattleStyle = 21;
	/** Whether stereo sound is enabled. */
	public final static int StereoSound = 22;
	/** Whether the L button schould act as the A button. */
	public final static int LButtonIsA = 23;
	/** Whether the performance stats are visible. */
	public final static int PerformanceHidden = 24;

	// Variables
	/** The number of pokemon in the team. */
	public final static int TeamSize = 0;
	/** The maximum number of items that can be registered. */
	public final static int RegisteredNum = 1;
	/** The registered item in slot 1. */
	public final static int Registered1 = 2;
	/** The registered item in slot 2. */
	public final static int Registered2 = 3;
	/** The registered item in slot 3. */
	public final static int Registered3 = 4;
	/** The registered item in slot 4. */
	public final static int Registered4 = 5;
	/** The registered item in slot 5. */
	public final static int Registered5 = 6;
	/** The registered item in slot 6. */
	public final static int Registered6 = 7;
	/** The registered item in slot 7. */
	public final static int Registered7 = 8;
	/** The registered item in slot 8. */
	public final static int Registered8 = 9;
	/** The answer of the last multiple choice question. */
	public final static int MCQuestionAnswer = 10;
	/** The display speed of text in a text box. */
	public final static int DefaultTextSpeed = 11;
	/** The window style for text boxes. */
	public final static int DefaultTextboxStyle = 12;
	/** The level to wich pokemon obey the player. */
	public final static int PokemonObeyLv = 13;
	/** The score of the player. */
	public final static int PlayerScore = 14;
	/** The amount of trainer card stars the player has. */
	public final static int TrainerCardStars = 15;
	/** The id of the current region. */
	public final static int RegionID = 16;
	/** The players ID number. */
	public final static int TrainerID = 17;
	/** The players secret ID number. */
	public final static int STrainerID = 18;
	/** The number of badges the player has. */
	public final static int BadgeNum = 19;
	/** Bitfield of obtained badges. */
	public final static int Badges1 = 20;
	/** Amount of money the player has. */
	public final static int Money = 21;
	/** Amount of time the player was playing. */
	public final static int TimePlayed = 22;
	/** Moment the player started playing. */
	public final static int TimeStart = 23;

	// Strings
	/** The name of the player. */
	public final static int PlayerName = 0;
	/** The text of the last text input. */
	public final static int InputText = 1;

	private SystemControll() {
	}

	public static void init() {
		// TODO load vars

		sysVariables[TeamSize] = 6;
		sysVariables[RegisteredNum] = 2;
		sysVariables[Registered1] = 1;
		sysVariables[Registered2] = 2;
		sysVariables[PokemonObeyLv] = 10;

		sysFlags[Pausable] = true;
		sysFlags[PokedexUnlocked] = true;
		sysFlags[IDUnlocked] = true;
		sysFlags[TeamUnlocked] = true;
		sysFlags[SaveUnlocked] = true;
		sysFlags[BagUnlocked] = true;
		sysFlags[OptionsUnlocked] = true;
		sysFlags[GadgetUnlocked] = true;
		sysFlags[MenuAvailable] = true;
		sysFlags[ShoesUnlocked] = true;
	}

	public static boolean getFlag(int flagNr) {
		return sysFlags[flagNr];
	}

	public static int getVariable(int variableNr) {
		return sysVariables[variableNr];
	}

	public static char[] getString(int variableNr) {
		return sysStrings[variableNr];
	}

	public static void setFlag(int flagNr, boolean value) {
		sysFlags[flagNr] = value;
	}

	public static void setVariable(int variableNr, int value) {
		sysVariables[variableNr] = value;
	}

	public static void setString(int variableNr, char[] value) {
		sysStrings[variableNr] = value;
	}
}
