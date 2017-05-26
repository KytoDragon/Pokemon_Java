package mapedit;

public class ConditionSwitch implements Condition {
	
	/** The index of the switch to be checked. */
	int nr;
	
	public ConditionSwitch(int switchNr) {
		nr = switchNr;
	}
	
	@Override
	public String getString() {
		return "0," + nr;
	}
	
}
