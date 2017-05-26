package script;

import util.ConV;
import util.StringMap;

public class VariableArray extends Variable {
	
	/** Map where the values of this "array" are stored. */
	private StringMap<Variable> map;

	public VariableArray(Object[] array) {
		this();
		int i = 0;
		for (Object object : array) {
			setElement(ConV.toCString(i), new VariableObject(object));
			i++;
		}
	}
	
	public VariableArray() {
		map = new StringMap<>();
	}
	
	/** Returns the Variable stored with the given index. */
	public Variable getElement(char[] index, ScriptException s) {
		if (!map.containsKey(index)) {
			s.initMessage("Can not find element with index \"", index, "\" in array variable");
			return null;
		}
		return map.get(index);
	}
	
	/** Stores the given variable with the given index in the array. */
	public void setElement(char[] index, Variable value) {
		map.put(index, value);
	}
	
	/** Removes the variable with the given index from the array. */
	public void removeElement(char[] index) {
		map.remove(index);
	}
	
	@Override
	public int getInt() {
		return map.size();
	}
	
	@Override
	public boolean getBool() {
		return !map.isEmpty();
	}
	
	@Override
	public String getString() {
		return ConV.toString(map.size());
	}
	
	@Override
	public char[] getCString() {
		return ConV.toCString(map.size());
	}
	
	@Override
	public StringMap<Variable> getObject() {
		return map;
	}
	
	@Override
	public Variable clone() {
		VariableArray result = new VariableArray();
		result.map.putAll(map);
		return result;
	}
	
}
