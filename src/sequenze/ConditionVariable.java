package sequenze;

public class ConditionVariable implements Condition {
	
	/** The index of the variable to be checked. */
	private int nr;
	/** The minimum value the variables needs to have. */
	private int min;
	
	public ConditionVariable(int varNr, int minimum) {
		nr = varNr;
		min = minimum;
	}
	
	@Override
	public boolean isTrue(Event e) {
		return EventControll.getVariable(nr) >= min;
	}
	
}
