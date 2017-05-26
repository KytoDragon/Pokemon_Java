package script;

public class VariableObject extends Variable {
	
	/** The object contained in the variable. */
	private Object value;
	
	public VariableObject(Object value) {
		this.value = value;
	}
	
	@Override
	public int getInt() {
		return value == null ? 0 : new VariableString(value.toString()).getInt();
	}
	
	@Override
	public boolean getBool() {
		return value != null;
	}
	
	@Override
	public String getString() {
		return value == null ? "null" : value.toString();
	}
	
	@Override
	public char[] getCString() {
		return getString().toCharArray();
	}
	
	@Override
	public Object getObject() {
		return value;
	}
	
	@Override
	public Variable clone() {
		return new VariableObject(value);
	}
	
}
