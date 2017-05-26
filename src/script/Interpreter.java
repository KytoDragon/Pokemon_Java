package script;

import util.List;

public abstract class Interpreter {
	
	/** A list of variables that can be called by a given name. */
	public static List<Enums> enums = new List<Enums>();
	/** Whether it is currently waiting on something to finish. */
	boolean waiting;
	/** A Interpreter that this Interpreter is waiting on. */
	Interpreter forward;
	/** A timer used by various actions. */
	int timer;
	/** The current code index. */
	int currentIndex;
	/** The current library for method calls. */
	MethodLibrary library;
	/** The Object that is used as variable with name "THIS". */
	Object calling;
	/** The calling depth of this interpreter. */
	int depth;
	
	public static final int MAX_CALL_DEPTH = 50;
	
	public abstract boolean doStep(ScriptException s);
	
	/** Returns whether the interpreter is currently running. */
	public boolean isActive() {
		return currentIndex != 0;
	}
	
	/** Freezes the interpreter until unfreeze() is called, all wait() timers get reset. */
	public void freeze() {
		waiting = true;
		timer = 0;
	}
	
	/** Unfreezes the interpreter, will skip the waiting at a wait(). */
	public void unfreeze() {
		waiting = false;
	}
	
	public static Interpreter getNewInterpreter(Code code, MethodLibrary ml, Object caller) {
		if (code.getClass() == RawCode.class) {
			return new DirectInterpreter((RawCode) code, ml, caller);
		} else {
			return new ByteInterpreter((ByteCode) code, ml, caller);
		}
	}
	
	static Interpreter getNewInterpreter(Code code, MethodLibrary ml, Object caller, Variable[] paras, int depth) {
		if (code.getClass() == RawCode.class) {
			return new DirectInterpreter((RawCode) code, ml, caller, paras, depth);
		} else {
			return new ByteInterpreter((ByteCode) code, ml, caller, paras, depth);
		}
	}
	
	public static Interpreter setCode(Interpreter interpreter, Code code) {
		if (code.getClass() == RawCode.class && interpreter.getClass() == DirectInterpreter.class) {
			((DirectInterpreter) interpreter).setRawCode((RawCode) code);
			return interpreter;
		} else if (code.getClass() == ByteCode.class && interpreter.getClass() == ByteInterpreter.class) {
			((ByteInterpreter) interpreter).setByteCode((ByteCode) code);
			return interpreter;
		}
		return getNewInterpreter(code, interpreter.library, interpreter.calling);
	}
	
	public static Interpreter setScript(Interpreter interpreter, String name) {
		ScriptException s = new ScriptException();
		Code code = interpreter.library.callScript(name, s);
		if (s.isInitialized()) {
			code = new RawCode(new char[][] { ("#" + name + "()").toCharArray() });
		}
		return setCode(interpreter, code);
	}
}
