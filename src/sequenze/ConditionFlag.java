package sequenze;

public class ConditionFlag implements Condition {
	
	/** The index of the flag to be checked. */
	private int nr;
	
	public ConditionFlag(int flagNr) {
		nr = flagNr;
	}
	
	@Override
	public boolean isTrue(Event e) {
		return EventControll.getFlag(nr);
	} 
	
}
