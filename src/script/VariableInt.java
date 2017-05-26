package script;

import util.ConV;

public class VariableInt extends Variable {
	
	/** The integer value of this variable. */
	private int value;
	
	public VariableInt(int value) {
		this.value = value;
	}
	
	@Override
	public int getInt() {
		return value;
	}
	
	@Override
	public boolean getBool() {
		return value != 0;
	}
	
	@Override
	public String getString() {
		return ConV.toString(value);
	}
	
	@Override
	public char[] getCString() {
		return ConV.toCString(value);
	}
	
	@Override
	public Integer getObject() {
		return new Integer(value);
	}
	
	@Override
	public Variable clone() {
		return new VariableInt(value);
	}
	
}
