package script;

import util.ConV;

public class VariableString extends Variable {

	/** The string value of the variable. */
	String value;

	public VariableString(String value) {
		this.value = value;
	}

	public VariableString(char[] value) {
		this.value = new String(value);
	}

	@Override
	public int getInt() {
		if (ConV.isInteger(value)) {
			return ConV.parseInteger(value);
		} else {
			if (getBool()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public boolean getBool() {
		return "false".equals(value) || "0".equals(value);
	}

	@Override
	public String getString() {
		return value;
	}

	@Override
	public char[] getCString() {
		return value.toCharArray();
	}

	@Override
	public String getObject() {
		return getString();
	}

	@Override
	public Variable clone() {
		return new VariableString(value);
	}
}
