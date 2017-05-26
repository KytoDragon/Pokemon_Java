package script;

import util.ConV;
import util.Stack;

import static script.Compiler.THIS_id;
import static script.OpCodes.*;

import util.Encoding;
import util.Logger;

public class ByteInterpreter extends Interpreter {

	/** The lines to be interpreted */
	private byte[] code;

	private Stack<Variable> stack = new Stack<>();
	/** The maximum number of operations executed at a single time. */
	public static final int maxBytesPerRun = 0;
	/** Storage for variables. */
	VariablesC variables;

	/** Creates a new interpreter using the given object as THIS object. */
	ByteInterpreter(ByteCode byteCode, MethodLibrary ml, Object caller) {
		this(byteCode, ml, caller, null, 0);
	}

	/** Creates a new interpreter with the given parameters and recursion depth. */
	ByteInterpreter(ByteCode byteCode, MethodLibrary ml, Object caller, Variable[] parameter, int depth) {
		this.code = byteCode.code;
		this.depth = depth;
		waiting = false;
		currentIndex = 0;
		timer = 0;
		forward = null;
		variables = new VariablesC(byteCode.vars_num, byteCode.pars_num);
		library = ml;
		calling = caller;
		if (caller != null) {
			variables.create(THIS_id, new VariableObject(caller));
		}
		if (parameter != null) {
			for (int i = 0; i < parameter.length && i < byteCode.pars_num; i++) {
				if (parameter[i] == null) {
					parameter[i] = new VariableObject(null);
				}
				variables.create(Compiler.PAR_id + i, parameter[i]);
			}
			if (byteCode.pars_num < parameter.length) {
				Logger.add(Logger.SCRIPT, "Script passed more parameters than accepted: ", byteCode.pars_num, " < ", parameter.length);
			}
		}
	}

	@Override
	public boolean doStep(ScriptException s) {
		if (timer > 0 && waiting) {
			timer--;
			if (timer == 0) {
				waiting = false;
			} else {
				return true;
			}
		}
		if (forward != null) {
			if (forward.doStep(s)) {
				if (s.isInitialized()) {
					forward.freeze();
					s.addStackElement("Code: ", (currentIndex < code.length && currentIndex >= 0 ? code[currentIndex] & 0xFF : "null"), ", Index: "
							, (currentIndex + 1), " of ", code.length);
					if (depth == 0) {
						s.addStackElement(calling == null ? "null" : calling.toString());
					}
				}
				return true;
			} else {
				forward = null;
				waiting = false;
			}
		}
		int lineNum = 0;
		while (!waiting && currentIndex < code.length && forward == null) {
			evaluateByte(s);
			if (s.isInitialized()) {
				freeze();
				if (currentIndex >= 0 && currentIndex < code.length) {
					s.addStackElement("Code: \"", code[currentIndex] & 0xFF, "\", Index.: ", (currentIndex + 1), " of ", code.length);
				} else {
					s.addStackElement("Index.: ", (currentIndex + 1), " of ", code.length);
				}
				if (depth == 0) {
					s.addStackElement(calling == null ? "null" : calling.toString());
				}
				stack.clear();
				return true;
			}
			currentIndex++;
			lineNum++;
			if (maxBytesPerRun > 0 && lineNum >= maxBytesPerRun && stack.isEmpty()) {
				break;
			}
		}
		assert (stack.isEmpty()) : "Stack did not empty, index is: " + ConV.toString(currentIndex);
		if (currentIndex >= code.length && forward == null && !waiting) {
			currentIndex = 0;
			variables.reset();
			variables.create(THIS_id, new VariableObject(calling));
		}
		return currentIndex != 0;
	}

	/** Evaluates one line of the script. */
	private void evaluateByte(ScriptException s) {
		S:
		switch (code[currentIndex] & 0xFF) {
			case NOP:
				break;
			case TRUE:
				stack.push(new VariableBool(true));
				break;
			case FALSE:
				stack.push(new VariableBool(false));
				break;
			case INTEGER: {
				int number = 0;
				boolean isNegativ = (code[currentIndex + 1] & 0x40) == 0x40;
				int base = 6;
				do {
					number = (number << base) | ((0xFF >> (8 - base)) & code[++currentIndex]);
					base = 7;
				} while ((code[currentIndex] & 0x80) == 0x80);
				if (isNegativ) {
					number = -number;
				}
				stack.push(new VariableInt(number));
				break;
			}
			case STRING:
				stack.push(new VariableString(Encoding.convertUTF16(code, currentIndex + 2, currentIndex + 2 + code[currentIndex + 1] & 0xFF)));
				currentIndex += (code[currentIndex + 1] & 0xFF) + 1;
				break;
			case VARIABLE: {
				int variableName = stack.pop().getInt();
				if (!variables.exists(variableName, s)) {
					return;
				}
				stack.push(variables.getVar(variableName));
				if (s.isInitialized()) {
					return;
				}
				break;
			}
			case ENUM: {
				char[] group = stack.pop().getCString();
				for (Enums enumI : enums) {
					if (ConV.equals(enumI.name, group)) {
						stack.push(enumI.getVar(stack.pop().getCString(), s));
						if (s.isInitialized()) {
							return;
						}
						break S;
					}
				}
				s.initMessage("Could not find enum group: ", group);
				return;
			}
			case METHOD_VOID: {
				char[] method_name = stack.pop().getCString();
				Variable[] parameters = new Variable[code[currentIndex + 1] & 0xFF];
				for (int i = parameters.length; i-- > 0; ) {
					parameters[i] = stack.pop();
				}
				library.callObject(stack.pop(), method_name, parameters, s);
				if (s.isInitialized()) {
					s.addStackElement("Library call returned exception");
					s.addStackElement("Method name: ", method_name);
					s.addStackElement("Parameter count: ", parameters.length);
					return;
				}
				// free parameters
				currentIndex++;
				break;
			}
			case METHOD_PUSH: {
				char[] method_name = stack.pop().getCString();
				Variable[] parameters = new Variable[code[currentIndex + 1] & 0xFF];
				for (int i = parameters.length; i-- > 0; ) {
					parameters[i] = stack.pop();
				}
				Variable result = library.callObject(stack.pop(), method_name, parameters, s);
				stack.push(result);
				if (s.isInitialized()) {
					s.addStackElement("Library call returned exception");
					s.addStackElement("Method name: ", method_name);
					s.addStackElement("Parameter count: ", parameters.length);
					return;
				} else if (result == null) {
					s.initMessage("Library call returned nothing, value expected");
					s.addStackElement("Method name: ", method_name);
					s.addStackElement("Parameter count: ", parameters.length);
					return;
				}
				// free parameters
				currentIndex++;
				break;
			}
			case MODULE_VOID: {
				Variable[] parameters = new Variable[code[currentIndex + 1] & 0xFF];
				char[] method_name = stack.pop().getCString();
				char[] module_name = stack.pop().getCString();
				for (int i = parameters.length; i-- > 0; ) {
					parameters[i] = stack.pop();
				}
				int module = library.getModuleId(module_name);
				if (module == -1) {
					s.initMessage("Unknown module name: ", module_name);
					return;
				}
				if (!library.hasMethod(module, method_name)) {
					s.initMessage("Unknown method ", module_name, " in module ", module_name);
					return;
				}
				library.callMethod(module, method_name, parameters, s);
				if (s.isInitialized()) {
					s.addStackElement("Library call returned exception");
					s.addStackElement("Module name: ", module_name);
					s.addStackElement("Method name: ", method_name);
					s.addStackElement("Parameter count: ", parameters.length);
					return;
				}
				// free parameters
				currentIndex++;
				break;
			}
			case MODULE_PUSH: {
				Variable[] parameters = new Variable[code[currentIndex + 1] & 0xFF];
				char[] method_name = stack.pop().getCString();
				char[] module_name = stack.pop().getCString();
				for (int i = parameters.length; i-- > 0; ) {
					parameters[i] = stack.pop();
				}
				int module = library.getModuleId(module_name);
				if (module == -1) {
					s.initMessage("Unknown module name: ", module_name);
					return;
				}
				if (!library.hasMethod(module, method_name)) {
					s.initMessage("Unknown method ", module_name, " in module ", module_name);
					return;
				}
				Variable result = library.callMethod(module, method_name, parameters, s);
				stack.push(result);
				if (s.isInitialized()) {
					s.addStackElement("Library call returned exception");
					s.addStackElement("Module name: ", module_name);
					s.addStackElement("Method name: ", method_name);
					s.addStackElement("Parameter count: ", parameters.length);
					return;
				} else if (result == null) {
					s.initMessage("Library call returned nothing, value expected");
					s.addStackElement("Module name: ", module_name);
					s.addStackElement("Method name: ", method_name);
					s.addStackElement("Parameter count: ", parameters.length);
					return;
				}
				// free parameters
				currentIndex++;
				break;
			}
			case ASSIGN_VAR: {
				int variableName = stack.pop().getInt();
				variables.set(variableName, stack.pop().clone(), s);
				break;
			}
			case ASSIGN_VAR_PUSH: {
				int variableName = stack.pop().getInt();
				variables.set(variableName, stack.peek().clone(), s);
				break;
			}
			case ASSIGN_ARRAY: {
				Variable value = stack.pop();
				char[] index = stack.pop().getCString();
				Variable array = stack.pop();
				if (array.getClass() != VariableArray.class) {
					s.initMessage("Can not index a non array");
					return;
				}
				((VariableArray) array).setElement(index, value);
				break;
			}
			case ASSIGN_ARRAY_PUSH: {
				Variable value = stack.pop();
				char[] index = stack.pop().getCString();
				Variable array = stack.pop();
				if (array.getClass() != VariableArray.class) {
					s.initMessage("Can not index a non array");
					return;
				}
				((VariableArray) array).setElement(index, value);
				stack.push(value);
				break;
			}
			case ACCSESS_ARRAY: {
				char[] index = stack.pop().getCString();
				Variable array = stack.pop();
				if (array.getClass() != VariableArray.class) {
					s.initMessage("Can not index a non array");
					return;
				}
				stack.push(((VariableArray) array).getElement(index, s));
				if (s.isInitialized()) {
					return;
				}
				break;
			}
			case AND:
				stack.push(new VariableBool(stack.pop().getBool() && stack.pop().getBool()));
				break;
			case OR:
				stack.push(new VariableBool(stack.pop().getBool() || stack.pop().getBool()));
				break;
			case EQUAL:
				stack.push(new VariableBool(ConV.endsWith(stack.pop().getCString(), stack.pop().getCString())));
				break;
			case NOT_EQUAL:
				stack.push(new VariableBool(!ConV.endsWith(stack.pop().getCString(), stack.pop().getCString())));
				break;
			case GREATER:
				stack.push(new VariableBool(stack.pop().getInt() < stack.pop().getInt()));
				break;
			case GREATER_EQUALS:
				stack.push(new VariableBool(stack.pop().getInt() <= stack.pop().getInt()));
				break;
			case SMALLER:
				stack.push(new VariableBool(stack.pop().getInt() > stack.pop().getInt()));
				break;
			case SMALLER_EQUALS:
				stack.push(new VariableBool(stack.pop().getInt() >= stack.pop().getInt()));
				break;
			case CONCAT:
				stack.push(new VariableString(ConV.concat(stack.pop().getCString(), stack.pop().getCString())));
				break;
			case ADD:
				stack.push(new VariableInt(stack.pop().getInt() + stack.pop().getInt()));
				break;
			case SUBTRACT:
				stack.push(new VariableInt(-stack.pop().getInt() + stack.pop().getInt()));
				break;
			case MULTIPLY:
				stack.push(new VariableInt(stack.pop().getInt() * stack.pop().getInt()));
				break;
			case DIVIDE: {
				int div = stack.pop().getInt();
				if (div == 0) {
					s.initMessage("Can not devide by zero");
					return;
				}
				stack.push(new VariableInt(stack.pop().getInt() / div));
				break;
			}
			case MODULO: {
				int mod = stack.pop().getInt();
				if (mod == 0) {
					s.initMessage("Can not modulo by zero");
					return;
				}
				stack.push(new VariableInt(stack.pop().getInt() % mod));
				break;
			}
			case JUMP:
				currentIndex = getPointer(code, currentIndex + 1) - 1;
				break;
			case IF:
				if (stack.pop().getBool()) {
					currentIndex += Compiler.POINTER_SIZE;
				} else {
					currentIndex = getPointer(code, currentIndex + 1) - 1;
				}
				break;
			case WAIT:
				timer = stack.pop().getInt();
				if (timer > 0) {
					waiting = true;
				} else if (timer < 0) {
					timer = 0;
					s.initMessage("Negativ values on a wait() call");
					return;
				}
				break;
			case SCRIPT: {
				String name = stack.pop().getString();
				Variable[] parameters = new Variable[code[currentIndex + 1] & 0xFF];
				for (int i = parameters.length; i-- > 0; ) {
					parameters[i] = stack.pop();
				}
				Code newCode;
				newCode = library.callScript(name, s);
				if (s.isInitialized()) {
					s.addStackElement("Library script call returned exception");
					s.addStackElement("Script name: ", name);
					return;
				}
				if (depth == Interpreter.MAX_CALL_DEPTH) {
					s.initMessage("Script recursion to deep, exceeded ", Interpreter.MAX_CALL_DEPTH, " steps");
					return;
				}
				forward = Interpreter.getNewInterpreter(newCode, library, calling, parameters, depth + 1);
				if (!forward.doStep(s)) {
					if (s.isInitialized()) {
						return;
					}
					forward = null;
				}
				currentIndex++;
				break;
			}
			case ARRAY:
				stack.push(new VariableArray());
				break;
			case EXIT:
				currentIndex = code.length - 1;
				break;
			default:
				s.initMessage("Illegal opcode: ", code[currentIndex] & 0xFF);
		}
	}

	static int getPointer(byte[] code, int index) {
		int pointer = 0;
		for (int i = 0; i < Compiler.POINTER_SIZE; i++) {
			pointer = (pointer << 8) | code[index + i];
		}
		return pointer;
	}

	void setByteCode(ByteCode code) {
		this.code = code.code;
		currentIndex = 0;
		waiting = false;
		timer = 0;
		forward = null;
		variables = new VariablesC(code.vars_num, code.pars_num);
		variables.create(THIS_id, new VariableObject(calling));
	}
}
