package sequenze;

public final class EventControll {
	
	private static boolean[] flags = new boolean[256];
	private static int[] variables = new int[256];
	private static char[][] strings = new char[256][];
	private static Pokemon[] pokemon = new Pokemon[256];
	
	private EventControll() {
	}
	
	// TODO load vars
	// TODO range check
	
	public static void init() {
	}
	
	/** Returns whether the flag with the given index is set. */
	public static boolean getFlag(int flagNr) {
		return flags[flagNr];
	}
	
	/** Returns the value of the variable with the given index. */
	public static int getVariable(int variableNr) {
		return variables[variableNr];
	}
	
	public static char[] getString(int stringNr) {
		return strings[stringNr];
	}
	
	public static Pokemon getPokemon(int pokemonNr) {
		return pokemon[pokemonNr];
	}
	
	public static void setFlag(int flagNr, boolean value) {
		flags[flagNr] = value;
	}
	
	public static void setVariable(int variableNr, int value) {
		variables[variableNr] = value;
	}
	
	public static void setString(int stringNr, char[] value) {
		strings[stringNr] = value;
	}
	
	public static void setPokemon(int pokemonNr, Pokemon value) {
		pokemon[pokemonNr] = value;
	}
}
