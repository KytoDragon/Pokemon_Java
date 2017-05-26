package script;

import static script.Compiler.PAR_id;

import util.ConV;

class VariablesC {

	private Variable[] vars;
	private Variable[] pars;

	VariablesC(int vars_num, int pars_num) {
		vars = new Variable[vars_num];
		pars = new Variable[pars_num];
	}

	boolean exists(int ident, ScriptException s) {
		if (ident <= 0) {
			s.initMessage("Invalid variable id: ", ident);
			return false;
		} else if (ident < PAR_id) {
			if (ident >= vars.length) {
				s.initMessage("Variable id outside range: ", ident, " >= ", vars.length);
				return false;
			} else if (vars[ident] == null) {
				s.initMessage("Variable does not exist: ", ident);
				return false;
			}
		} else {
			ident -= PAR_id;
			if (ident >= pars.length) {
				s.initMessage("Parameter id outside range: ", ident, " >= ", pars.length);
				return false;
			} else if (pars[ident] == null) {
				s.initMessage("Parameter does not exist: ", ident);
				return false;
			}
		}
		return true;
	}

	void set(int ident, Variable v, ScriptException s) {
		if (ident <= 0) {
			s.initMessage("Invalid variable id: ", ident);
		}else if (ident < PAR_id) {
			if (ident - 1 >= vars.length) {
				s.initMessage("Variable id outside range: ", ident - 1, " >= ", vars.length);
				return;
			}
			vars[ident - 1] = v;
		} else {
			ident -= PAR_id;
			if (ident >= pars.length) {
				s.initMessage("Parameter id outside range: ", ident, " >= ", pars.length);
				return;
			}
			pars[ident] = v;
		}
	}

	void create(int ident, Variable v) {
		set(ident, v, null);
	}

	void reset() {
		vars = new Variable[vars.length];
		pars = new Variable[pars.length];
	}

	/** Returns the variable with the given name. */
	Variable getVar(int ident) {
		if (ident < PAR_id) {
			return vars[ident - 1];
		} else {
			return pars[ident - PAR_id];
		}
	}

}
