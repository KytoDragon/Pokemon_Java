package mapedit;

public class ConditionFlag implements Condition {
	
	/** The index of the flag to be checked. */
	int nr;
	
	public ConditionFlag(int flagNr) {
		nr = flagNr;
	}
	
	@Override
	public String getString() {
		return "1," + nr;
	}
	
}
