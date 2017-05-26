package mapedit;

public class ConditionVariable implements Condition {
	
	/** The index of the variable to be checked. */
	int nr;
	/** The minimum value the variables needs to have. */
	int min;
	
	public ConditionVariable(int varNr, int minimum) {
		nr = varNr;
		min = minimum;
	}
	
	@Override
	public String getString() {
		return "2," + nr + ',' + min;
	}
	
}
