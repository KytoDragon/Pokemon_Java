package script;

import util.StringMap;

class Variables {

	/** The map containing the variables. */
	private StringMap<Variable> map;

	Variables() {
		map = new StringMap<>();
	}

	/** Inserts the given variable in the map, overwriting existing variables with the same identifier. */
	void set(char[] ident, Variable v) {
		create(ident, v);
	}

	/** Adds the given variable to the map. */
	void create(char[] ident, Variable v) {
		map.put(ident, v);
	}

	/** Deletes all variables. */
	void reset() {
		map.clear();
	}

	/** Returns the variable with the given name. */
	Variable getVar(char[] ident, ScriptException s) {
		Variable v = map.get(ident);
		if (v != null) {
			return v;
		}
		s.initMessage("Variable not found: ", ident);
		return null;
	}

}
