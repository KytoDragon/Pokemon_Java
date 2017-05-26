package script;

public class VariableBool extends Variable {

	/** The boolean value of this variable. */
	private boolean value;

	public VariableBool(boolean value) {
		this.value = value;
	}

	@Override
	public int getInt() {
		return value ? 1 : 0;
	}

	@Override
	public boolean getBool() {
		return value;
	}

	@Override
	public String getString() {
		return value ? "true" : "false";
	}

	@Override
	public char[] getCString() {
		return value ? "true".toCharArray() : "false".toCharArray();
	}

	@Override
	public Boolean getObject() {
		return Boolean.valueOf(value);
	}

	@Override
	public Variable clone() {
		return new VariableBool(value);
	}

}
