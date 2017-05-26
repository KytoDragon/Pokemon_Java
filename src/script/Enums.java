package script;

import util.StringMap;

public class Enums {
	
	char[] name;
	/** The map containing the variables. */
	private StringMap<Variable> map;
	
	public Enums(char[] name) {
		this.name = name;
		map = new StringMap<Variable>();
	}
	
	public <t extends Enum<t> & util.Enum> Enums(Class<t> clazz) {
		this(clazz.getCanonicalName().toCharArray());
		insertAll(clazz);
	}
	
	/** Adds the given variable to the list. */
	public void insert(char[] name, Variable v) {
		map.put(name, v);
	}
	
	public void insert(char[] name, Object e) {
		map.put(name, new VariableObject(e));
	}
	
	public <t extends Enum<t> & util.Enum> void insertAll(Class<t> clazz){
		t[] enums = clazz.getEnumConstants();
		for (t elem : enums) {
			insert(elem.name().toCharArray(), new VariableObject(elem));
		}
	}
	
	/** Returns the variable with the given name. */
	Variable getVar(char[] name, ScriptException s) {
		Variable v = map.get(name);
		if(v != null){
			return v;
		}
		s.initMessage("Could not find value of ", name, " in enum ", this.name);
		return null;
	}
}
