package sequenze;

public class ConditionSwitch implements Condition {
	
	/** The index of the switch to be checked. */
	private int nr;
	
	public ConditionSwitch(int switchNr) {
		nr = switchNr;
	}
	
	@Override
	public boolean isTrue(Event e) {
		return e.switches[nr];
	} 
	
}
