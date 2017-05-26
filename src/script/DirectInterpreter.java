package script;

import util.ConV;

import static script.Compiler.*;

public class DirectInterpreter extends Interpreter {

	/** The lines to be interpreted */
	private char[][] lines;
	/** Whether the condition in the current line needs to be rechecked. (for loops and else if) */
	private boolean check;
	/** The maximum number of lines executed at a single time. */
	public static final int maxLinesPerRun = 0;
	/** Storage for variables. */
	Variables variables;

	/** Creates a new interpreter using the given object as THIS object. */
	DirectInterpreter(RawCode code, MethodLibrary ml, Object caller) {
		this(code, ml, caller, null, 0);
	}

	/** Creates a new interpreter with the given parameters and recursion depth. */
	DirectInterpreter(RawCode code, MethodLibrary ml, Object caller, Variable[] parameter, int depth) {
		this.lines = code.lines;
		this.depth = depth;
		waiting = false;
		currentIndex = 0;
		timer = 0;
		forward = null;
		check = false;
		variables = new Variables();
		library = ml;
		calling = caller;
		if (caller != null) {
			variables.create(THIS, new VariableObject(caller));
		}
		if (parameter != null) {
			for (int i = 0; i < parameter.length; i++) {
				if (parameter[i] == null) {
					variables.create(("par" + i).toCharArray(), new VariableObject(null));
				} else {
					variables.create(("par" + i).toCharArray(), parameter[i]);
				}
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
					if (currentIndex >= 0 && currentIndex < lines.length) {
						s.addStackElement("Line: \"", lines[currentIndex], "\", LineNr.: ", (currentIndex + 1), " of ", lines.length);
					} else {
						s.addStackElement("LineNr.: ", (currentIndex + 1), " of ", lines.length);
					}
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
		while (!waiting && currentIndex < lines.length && forward == null) {
			evaluateLine(s);
			if (s.isInitialized()) {
				freeze();
				if (currentIndex >= 0 && currentIndex < lines.length) {
					s.addStackElement("Line: \"", lines[currentIndex], "\", LineNr.: ", (currentIndex + 1), " of ", lines.length);
				} else {
					s.addStackElement("LineNr.: ", (currentIndex + 1), " of ", lines.length);
				}
				if (depth == 0) {
					s.addStackElement(calling == null ? "null" : calling.toString());
				}
				return true;
			}
			currentIndex++;
			lineNum++;
			if (maxLinesPerRun > 0 && lineNum >= maxLinesPerRun) {
				break;
			}
		}
		if (currentIndex >= lines.length && forward == null && !waiting) {
			currentIndex = 0;
			check = false;
			variables.reset();
			variables.create(THIS, new VariableObject(calling));
		}
		return currentIndex != 0;
	}

	/** Evaluates one line of the script. */
	@SuppressWarnings("ConstantConditions")
	private void evaluateLine(ScriptException s) {
		char[] lineC = lines[currentIndex];
		int end = lineC.length;
		if (ConV.startsWith(lineC, 0, end, IF_START)) {
			if (!ConV.endsWith(lineC, 0, end, IF_END)) {
				int index = ConV.indexOf(lineC, IF_GOTO);
				if (index == -1) {
					s.initMessage("Missing \"", IF_END, "\" for ", IF_START);
					return;
				}
				Variable var = evalExpression(lineC, IF_START.length, end - IF_END.length, true, s);
				if (s.isInitialized()) {
					return;
				}
				if (!isNumber(lineC, index + IF_GOTO.length, end)) {
					s.initMessage("Unparsable jump mark: ", ConV.subCstring(lineC, index + IF_GOTO.length, end));
					return;
				}
				int line_number = ConV.getInt(lineC, index + IF_GOTO.length);
				if (line_number < 0 || line_number >= lines.length) {
					s.initMessage("Jump mark out of bounds: ", ConV.subCstring(lineC, index + IF_GOTO.length, end));
					return;
				}
				if (!var.getBool()) {
					currentIndex = line_number - 1;
				}
				return;
			}
			Variable var = evalExpression(lineC, IF_START.length, end - IF_END.length, true, s);
			if (s.isInitialized()) {
				return;
			}
			if (!var.getBool()) {
				int if_depth = 1;
				while (if_depth > 0) {
					currentIndex++;
					if (currentIndex >= lines.length) {
						s.initMessage("Missing \"", ELSE, "\"/\"", ELSE_IF, "\"/\"", IF_EXIT, "\" for ", IF_START);
						return;
					}
					lineC = lines[currentIndex];
					end = lineC.length;
					if (ConV.startsWith(lineC, 0, end, IF_START)) {
						if_depth++;
					} else if (ConV.equals(lineC, 0, end, IF_EXIT)) {
						if_depth--;
					} else if (ConV.equals(lineC, 0, end, ELSE)) {
						if (if_depth == 1) {
							if_depth--;
						}
					} else if (ConV.startsWith(lineC, 0, end, ELSE_IF)) {
						if (if_depth == 1) {
							if_depth--;
							check = true;
							currentIndex--;
						}
					}
				}
			}
		} else if (ConV.startsWith(lineC, 0, end, ELSE_IF)) {
			if (!ConV.endsWith(lineC, 0, end, IF_END)) {
				s.initMessage("Missing \"", IF_END, "\" for ", ELSE_IF);
				return;
			}
			if (!check) {
				int if_depth = 1;
				while (if_depth > 0) {
					currentIndex++;
					if (currentIndex >= lines.length) {
						s.initMessage("Missing \"", IF_EXIT, "\" for ", ELSE_IF);
						return;
					}
					lineC = lines[currentIndex];
					end = lineC.length;
					if (ConV.startsWith(lineC, 0, end, IF_START)) {
						if_depth++;
					} else if (ConV.equals(lineC, 0, end, IF_EXIT)) {
						if_depth--;
					}
				}
			} else {
				Variable var = evalExpression(lineC, ELSE_IF.length, end - IF_END.length, true, s);
				if (s.isInitialized()) {
					return;
				}
				if (!var.getBool()) {
					check = false;
					int if_depth = 1;
					while (if_depth > 0) {
						currentIndex++;
						if (currentIndex >= lines.length) {
							s.initMessage("Missing \"", ELSE, "\"/\"", ELSE_IF, "\"/\"", IF_EXIT, "\" for ", ELSE_IF);
							return;
						}
						lineC = lines[currentIndex];
						end = lineC.length;
						if (ConV.startsWith(lineC, 0, end, IF_START)) {
							if_depth++;
						} else if (ConV.equals(lineC, 0, end, IF_EXIT)) {
							if_depth--;
						} else if (ConV.equals(lineC, 0, end, ELSE)) {
							if (if_depth == 1) {
								if_depth--;
							}
						} else if (ConV.startsWith(lineC, 0, end, ELSE_IF)) {
							if (if_depth == 1) {
								if_depth--;
								check = true;
								currentIndex--;
							}
						}
					}
				} else {
					check = false;
				}
			}
		} else if (ConV.equals(lineC, 0, end, ELSE)) {
			int if_depth = 1;
			while (if_depth > 0) {
				currentIndex++;
				if (currentIndex >= lines.length) {
					s.initMessage("Missing \"", IF_EXIT, "\" for ", ELSE);
					return;
				}
				lineC = lines[currentIndex];
				end = lineC.length;
				if (ConV.startsWith(lineC, 0, end, IF_START)) {
					if_depth++;
				} else if (ConV.equals(lineC, 0, end, IF_EXIT)) {
					if_depth--;
				}
			}
		} else if (ConV.equals(lineC, 0, end, IF_EXIT)) {
		} else if (ConV.startsWith(lineC, 0, end, WHILE_START)) {
			if (!ConV.endsWith(lineC, 0, end, WHILE_END)) {
				s.initMessage("Missing \"", WHILE_END, "\" for ", WHILE_START);
				return;
			}
			Variable var = evalExpression(lineC, WHILE_START.length, end - WHILE_END.length, true, s);
			if (s.isInitialized()) {
				return;
			}
			if (!var.getBool()) {
				int loop_depth = 1;
				while (loop_depth > 0) {
					currentIndex++;
					if (currentIndex >= lines.length) {
						s.initMessage("Missing \"", WHILE_EXIT, "\" for ", WHILE_START);
						return;
					}
					lineC = lines[currentIndex];
					end = lineC.length;
					if (ConV.startsWith(lineC, 0, end, WHILE_START)) {
						loop_depth++;
					} else if (ConV.startsWith(lineC, 0, end, FOR_START)) {
						loop_depth++;
					} else if (ConV.equals(lineC, 0, end, WHILE_EXIT)) {
						loop_depth--;
					}
				}
			}
		} else if (ConV.startsWith(lineC, 0, end, FOR_START)) {
			if (!ConV.endsWith(lineC, 0, end, FOR_END)) {
				s.initMessage("Missing \"", FOR_END, "\" for ", FOR_START);
				return;
			} else if (!ConV.contains(lineC, 0, end, FOR_MIDDLE)) {
				s.initMessage("Missing \"", FOR_MIDDLE, "\" for ", FOR_START);
				return;
			}
			int middleIndex = ConV.indexOf(lineC, 0, end, FOR_MIDDLE);
			char[] name;
			if (ConV.contains(lineC, 0, end, '=') && ConV.indexOf(lineC, 0, end, '=') < middleIndex) {
				name = ConV.trimToCString(lineC, FOR_START.length, ConV.indexOf(lineC, 0, end, '='));
			} else {
				name = ConV.trimToCString(lineC, FOR_START.length, middleIndex);
			}
			Variable var;
			if (check) {
				var = variables.getVar(name, s);
				if (s.isInitialized()) {
					return;
				}
				var = new VariableInt(var.getInt() + 1);
				variables.set(name, var);
				check = false;
			} else if (ConV.contains(lineC, 0, end, '=') && ConV.indexOf(lineC, 0, end, '=') < middleIndex) {
				var = evalExpression(lineC, ConV.indexOf(lineC, 0, end, '=') + 1, middleIndex, true, s);
				if (s.isInitialized()) {
					return;
				}
				variables.set(name, var);
			} else {
				var = variables.getVar(name, s);
				if (s.isInitialized()) {
					return;
				}
			}
			Variable bounds = evalExpression(lineC, middleIndex + FOR_MIDDLE.length, end - FOR_END.length, true, s);
			if (s.isInitialized()) {
				return;
			}
			if (var.getInt() >= bounds.getInt()) {
				int loop_depth = 1;
				while (loop_depth > 0) {
					currentIndex++;
					if (currentIndex >= lines.length) {
						s.initMessage("Missing \"", WHILE_EXIT, "\" for ", FOR_START);
						return;
					}
					lineC = lines[currentIndex];
					end = lineC.length;
					if (ConV.startsWith(lineC, 0, end, WHILE_START)) {
						loop_depth++;
					} else if (ConV.startsWith(lineC, 0, end, FOR_START)) {
						loop_depth++;
					} else if (ConV.equals(lineC, 0, end, WHILE_EXIT)) {
						loop_depth--;
					}
				}
			}
		} else if (ConV.equals(lineC, 0, end, WHILE_EXIT) || ConV.equals(lineC, 0, end, CONTINUE)) {
			int loop_depth = 1;
			currentIndex--;
			while (loop_depth > 0) {
				if (currentIndex < 0) {
					s.initMessage("Missing loop for ", WHILE_EXIT, "/", CONTINUE);
					return;
				}
				lineC = lines[currentIndex];
				end = lineC.length;
				if (ConV.startsWith(lineC, 0, end, WHILE_START)) {
					loop_depth--;
				} else if (ConV.startsWith(lineC, 0, end, FOR_START)) {
					loop_depth--;
					if (loop_depth == 0) {
						check = true;
					}
				} else if (ConV.equals(lineC, 0, end, WHILE_EXIT)) {
					loop_depth++;
				}
				currentIndex--;
			}
		} else if (ConV.equals(lineC, 0, end, BREAK)) {
			int loop_depth = 1;
			while (loop_depth > 0) {
				currentIndex++;
				if (currentIndex >= lines.length) {
					s.initMessage("Missing loop for ", BREAK);
					return;
				}
				lineC = lines[currentIndex];
				end = lineC.length;
				if (ConV.startsWith(lineC, 0, end, WHILE_START)) {
					loop_depth++;
				} else if (ConV.startsWith(lineC, 0, end, FOR_START)) {
					loop_depth++;
				} else if (ConV.equals(lineC, 0, end, WHILE_EXIT)) {
					loop_depth--;
				}
			}
		} else if (ConV.equals(lineC, 0, end, RETURN)) {
			currentIndex = lines.length - 1;
		} else if (ConV.startsWith(lineC, 0, end, WAIT_START) && ConV.endsWith(lineC, 0, end, WAIT_END)) {
			Variable var = evalExpression(lineC, WAIT_START.length, end - WAIT_END.length, true, s);
			if (s.isInitialized()) {
				return;
			}
			timer = var.getInt();
			if (timer > 0) {
				waiting = true;
			} else if (timer < 0) {
				timer = 0;
				s.initMessage("Negativ values on a wait() call");
			}
		} else if (end > 0 && lineC[0] == '#') {
			int index = ConV.indexOf(lineC, 0, end, '(');
			if (index == -1 || getBracketIndex(lineC, index + 1, end, s) != end - 1) {
				s.initMessage("Mismatched or missing brackets for script call");
				return;
			} else if (!isVarName(lineC, 1, index)) {
				s.initMessage("Malformed script name: ", ConV.subCstring(lineC, 0, index));
				return;
			}
			String name = ConV.substring(lineC, 1, index);
			Variable[] paras = getParameter(lineC, index + 1, end - 1, s);
			if (s.isInitialized()) {
				return;
			}
			Code newCode = library.callScript(name, s);
			if (s.isInitialized()) {
				s.addStackElement("Library object call returned exception");
				return;
			}
			if (depth == Interpreter.MAX_CALL_DEPTH) {
				s.initMessage("Script recursion to deep, exceeded ", Interpreter.MAX_CALL_DEPTH, " steps");
				return;
			}
			forward = Interpreter.getNewInterpreter(newCode, library, calling, paras, depth + 1);
			if (!forward.doStep(s)) {
				forward = null;
			}
		} else if (ConV.startsWith(lineC, GOTO)) {
			if (!isNumber(lineC, GOTO.length, end)) {
				s.initMessage("Unparsable jump mark: ", ConV.subCstring(lineC, GOTO.length, end));
				return;
			}
			int line_number = ConV.getInt(lineC, GOTO.length);
			if (line_number < 0 || line_number >= lines.length) {
				s.initMessage("Jump mark out of bounds: ", ConV.subCstring(lineC, GOTO.length, end));
				return;
			}
			currentIndex = line_number - 1;
		} else {
			evalExpression(lineC, 0, end, false, s);
		}
	}

	/** Evaluates the given expression and return the resulting variable. */
	@SuppressWarnings("ConstantConditions")
	private Variable evalExpression(char[] exp, int start, int end, boolean returnResult, ScriptException s) {
		while (exp[start] == ' ' || exp[start] == '\t') {
			start++;
		}
		while (exp[end - 1] == ' ' || exp[end - 1] == '\t') {
			end--;
		}
		if (start >= end) {
			s.initMessage("Empty Expression");
		}
		Variable first;
		Variable second;
		int index = end;
		int priority = -1; // 0 .[, 1 */%, 2 +-, 3 .., 4 <>==>=<=!=, 5 ||&&, 6 =,
		try {
			for (int i = start; i < end; i++) {
				char c = exp[i];
				if (c == '=' && i - 1 >= start && exp[i - 1] != '<' && exp[i - 1] != '>' && exp[i - 1] != '=' && i + 1 < end && exp[i + 1] != '=') {
					index = i;
					priority = 6;
					break;
				} else if (c == '|' || c == '&') {
					index = i;
					priority = 5;
				} else if (priority <= 4 && (c == '<' || c == '>' || c == '=')) {
					index = i;
					priority = 4;
				} else if (priority <= 3 && c == '.' && i - 1 >= 0 && exp[i - 1] == '.') {
					index = i;
					priority = 3;
				} else if (priority <= 2 && (c == '+' || c == '-')) {
					index = i;
					priority = 2;
				} else if (priority <= 1 && (c == '*' || c == '/' || c == '%')) {
					index = i;
					priority = 1;
				} else if (priority <= 0 && (c == '.' || c == '[')) {
					index = i;
					priority = 0;
					if (c == '[') {
						i = getSquareBracketIndex(exp, i + 1, end, s);
						if (s.isInitialized()) {
							return null;
						}
					}
				} else if (c == '(') {
					i = getBracketIndex(exp, i + 1, end, s);
					if (s.isInitialized()) {
						return null;
					}
				} else if (c == '"') {
					i = getStringIndex(exp, i + 1, end, s);
					if (s.isInitialized()) {
						return null;
					}
				}
			}
			if (priority > 0) {
				second = evalExpression(exp, index + 1, end, true, s);
				if (s.isInitialized()) {
					return null;
				}
				char c = exp[index];
				if (priority >= 3 && priority != 6) {
					if (c == '=' || c == '|' || c == '&' || c == '.') {
						first = evalExpression(exp, start, index - 1, true, s);
						if (s.isInitialized()) {
							return null;
						}
					} else {
						first = evalExpression(exp, start, index, true, s);
						if (s.isInitialized()) {
							return null;
						}
					}
				} else if (priority != 6) {
					first = evalExpression(exp, start, index, true, s);
					if (s.isInitialized()) {
						return null;
					}
				} else {
					int i = index;
					while (i > start && (exp[i - 1] == ' ' || exp[i - 1] == '\t')) {
						i--;
					}
					if (i == start) {
						s.initMessage("Can not assign to empty expression: ", ConV.subCstring(exp, start, index));
						return null;
					}
					second = second.clone();
					if (isVarName(exp, start, i)) {
						variables.set(ConV.subCstring(exp, start, i), second);
					} else {
						int aIndex = ConV.indexOf(exp, start, end, '[');
						if (aIndex == -1 || getSquareBracketIndex(exp, aIndex + 1, end, s) != i - 1) {
							s.initMessage("Left side of assignment is not a variable: ", ConV.subCstring(exp, start, i));
							return null;
						}
						first = evalExpression(exp, aIndex + 1, i - 1, true, s);
						if (s.isInitialized()) {
							return null;
						}
						Variable varr = evalExpression(exp, start, aIndex, true, s);
						if (s.isInitialized()) {
							return null;
						}
						if (varr.getClass() != VariableArray.class) {
							s.initMessage("Can not index a non array expression");
							return null;
						}
						((VariableArray) varr).setElement(first.getCString(), second);
					}
					assert (second != null);
					return second;
				}
				switch (c) {
					case '+':
						first = new VariableInt(first.getInt() + second.getInt());
						break;
					case '-':
						first = new VariableInt(first.getInt() - second.getInt());
						break;
					case '*':
						first = new VariableInt(first.getInt() * second.getInt());
						break;
					case '/':
						if (second.getInt() == 0) {
							s.initMessage("Can not devide by zero");
							return null;
						}
						first = new VariableInt(first.getInt() / second.getInt());
						break;
					case '%':
						if (second.getInt() == 0) {
							s.initMessage("Can not modulo by zero");
							return null;
						}
						first = new VariableInt(first.getInt() % second.getInt());
						break;
					case '|':
						first = new VariableBool(first.getBool() || second.getBool());
						break;
					case '&':
						first = new VariableBool(first.getBool() && second.getBool());
						break;
					case '=':
						char c2 = exp[index - 1];
						switch (c2) {
							case '=':
								first = new VariableBool(ConV.equals(first.getCString(), second.getCString()));
								break;
							case '<':
								first = new VariableBool(first.getInt() <= second.getInt());
								break;
							case '>':
								first = new VariableBool(first.getInt() >= second.getInt());
								break;
							case '!':
								first = new VariableBool(!ConV.equals(first.getCString(), second.getCString()));
								break;
							default:
								assert (false) : "not ==";
						}
						break;
					case '<':
						first = new VariableBool(first.getInt() < second.getInt());
						break;
					case '>':
						first = new VariableBool(first.getInt() > second.getInt());
						break;
					case '.':
						first = new VariableString(ConV.concat(first.getCString(), second.getCString()));
						break;
					default:
						assert (false) : "not operant";
				}
			} else if (priority == 0) {
				if (exp[index] == '.') {
					int bracket = ConV.indexOf(exp, index + 1, end, '(');
					if (bracket == -1) {
						if (isModuleName(exp, start, index) && isVarName(exp, index + 1, end)) {
							for (Enums enumI : enums) {
								if (ConV.endsWith(exp, start, index, enumI.name)) {
									first = enumI.getVar(ConV.subCstring(exp, index + 1, end), s);
									if (s.isInitialized()) {
										s.initMessage("Could not find enum: ", ConV.subCstring(exp, start, end));
										return null;
									}
									assert (first != null);
									return first;
								}
							}
							s.initMessage("Could not find enum group: ", ConV.subCstring(exp, start, index));
							return null;
						} else {
							s.initMessage("Failed to access enum: malformed names");
							return null;
						}
					} else {
						if (getBracketIndex(exp, bracket + 1, end, s) != end - 1) {
							s.initMessage("Mismatched or missing brackets for function call");
							return null;
						} else if (!isVarName(exp, index + 1, bracket)) {
							s.initMessage("Malformed function call");
							return null;
						}
						Variable[] paras = getParameter(exp, bracket + 1, end - 1, s);
						if (s.isInitialized()) {
							s.addStackElement("Reading parameter list");
							return null;
						}
						if (isModuleName(exp, start, index)) {
							char[] module_name = ConV.subCstring(exp, start, index);
							int module = library.getModuleId(module_name);
							if (module == -1) {
								s.initMessage("Unknown module name: ", module_name);
								return null;
							}
							char[] method = ConV.subCstring(exp, index + 1, bracket);
							if (!library.hasMethod(module, method)) {
								s.initMessage("Unknown method ", module_name, " in module ", module_name);
								return null;
							}
							first = library.callMethod(module, method, paras, s);
							if (s.isInitialized()) {
								return null;
							}
						} else {
							Variable object = evalExpression(exp, start, index, true, s);
							if (s.isInitialized()) {
								return null;
							}
							first = library.callObject(object, ConV.subCstring(exp, index + 1, bracket), paras, s);
						}
						if (s.isInitialized()) {
							s.addStackElement("Method call: ", ConV.substring(exp, index + 1, bracket));
							return null;
						}
					}
				} else {
					if (getSquareBracketIndex(exp, index + 1, end, s) != end - 1) {
						s.initMessage("Mismatched square brackets or missing operator");
						return null;
					}
					first = evalExpression(exp, start, index, true, s);
					if (s.isInitialized()) {
						return null;
					}
					if (first.getClass() != VariableArray.class) {
						s.initMessage("Can not index a non array expression");
						return null;
					}
					second = evalExpression(exp, index + 1, end - 1, true, s);
					if (s.isInitialized()) {
						return null;
					}
					first = ((VariableArray) first).getElement(second.getCString(), s);
					if (s.isInitialized()) {
						return null;
					}
				}
			} else {
				if (exp[start] == '(' && exp[end - 1] == ')') {
					if (getBracketIndex(exp, start + 1, end, s) != end - 1) {
						s.initMessage("Mismatched brackets or missing operator");
						return null;
					}
					first = evalExpression(exp, start + 1, end - 1, returnResult, s);
					if (s.isInitialized()) {
						return null;
					}
				} else if (isNumber(exp, start, end)) {
					first = new VariableInt(ConV.getInt(exp, start));
				} else if (exp[start] == '\"' && exp[end - 1] == '\"') {
					if (getStringIndex(exp, start + 1, end, s) != end - 1) {
						s.initMessage("Mismatched quotation marks or missing operator");
						return null;
					}
					first = new VariableString(ConV.substring(exp, start + 1, end - 1));
				} else if (ConV.equals(exp, start, end, "true")) {
					first = new VariableBool(true);
				} else if (ConV.equals(exp, start, end, "false")) {
					first = new VariableBool(false);
				} else if (isVarName(exp, start, end)) {
					first = variables.getVar(ConV.subCstring(exp, start, end), s);
					if (s.isInitialized()) {
						return null;
					}
				} else {
					int begin = ConV.indexOf(exp, start, end, '(');
					if (begin == -1 || !isVarName(exp, start, begin)) {
						s.initMessage("Could not parse expression, unknown content: ", ConV.subCstring(exp, start, end));
						return null;
					}
					if (getBracketIndex(exp, begin + 1, end, s) != end - 1) {
						s.initMessage("Mismatched brackets or missing operator: ", ConV.subCstring(exp, begin, end));
						return null;
					}
					char[] method_name = ConV.subCstring(exp, start, begin);
					if (ConV.equals(method_name, "array")) {
						if (end != begin + 7) {
							s.initMessage("Array creation does not take aruments: ", ConV.subCstring(exp, begin, end));
							return null;
						}
						first = new VariableArray();
					} else {
						Variable[] paras = getParameter(exp, begin + 1, end - 1, s);
						if (s.isInitialized()) {
							return null;
						}
						first = library.callObject(variables.getVar(THIS, s), method_name, paras, s);
						if (s.isInitialized()) {
							s.addStackElement("method call: ", method_name);
							return null;
						}
						if (returnResult && first == null) {
							s.initMessage("Required value, but expression returned nothing");
							return null;
						}
					}
				}
			}
			return first;
		} finally {
			if (s.isInitialized()) {
				s.addStackElement("Expression: ", ConV.substring(exp, start, end));
			}
		}
	}

	/** Evaluates the given expression and returns a list of variables contained in the expression. */
	private Variable[] getParameter(char[] exp, int start, int end, ScriptException s) {
		if (start == end) {
			return new Variable[0];
		}
		int comma = 0;
		for (int i = start; i < end; i++) {
			char c = exp[i];
			if (c == '"') {
				i = getStringIndex(exp, i + 1, end, s);
				if (s.isInitialized()) {
					return null;
				}
			} else if (c == '(') {
				i = getBracketIndex(exp, i + 1, end, s);
				if (s.isInitialized()) {
					return null;
				}
			} else if (c == ',') {
				comma++;
			}
		}
		Variable[] paras = new Variable[comma + 1];
		int last = start;
		for (int i = start; i < end && comma >= 0; i++) {
			char c = exp[i];
			if (c == '"') {
				i = getStringIndex(exp, i + 1, end, s);
				if (s.isInitialized()) {
					return null;
				}
			} else if (c == '(') {
				i = getBracketIndex(exp, i + 1, end, s);
				if (s.isInitialized()) {
					return null;
				}
			} else if (c == ',') {
				paras[paras.length - comma - 1] = evalExpression(exp, last, i, true, s);
				if (s.isInitialized()) {
					return null;
				}
				last = i + 1;
				comma--;
			}
		}
		paras[paras.length - 1] = evalExpression(exp, last, end, true, s);
		if (s.isInitialized()) {
			return null;
		}
		return paras;
	}

	/** Returns the index of the matching bracket starting at start. */
	private int getBracketIndex(char[] s, int start, int end, ScriptException sc) {
		int index = start;
		int br_depth = 1;
		while (br_depth > 0) {
			if (index >= end) {
				sc.initMessage("Missing closing bracket");
				return 0;
			}
			char c = s[index];
			if (c == '"') {
				index = getStringIndex(s, index + 1, end, sc);
				if (sc.isInitialized()) {
					return 0;
				}
			} else if (c == '(') {
				br_depth++;
			} else if (c == ')') {
				br_depth--;
			}
			index++;
		}
		return index - 1;
	}

	/** Returns the index of the matching square bracket starting at start. */
	private int getSquareBracketIndex(char[] s, int start, int end, ScriptException sc) {
		int index = start;
		int br_depth = 1;
		while (br_depth > 0) {
			if (index >= end) {
				sc.initMessage("Missing closing square bracket");
				return 0;
			}
			char c = s[index];
			if (c == '"') {
				index = getStringIndex(s, index + 1, end, sc);
				if (sc.isInitialized()) {
					return 0;
				}
			} else if (c == '(') {
				index = getBracketIndex(s, index + 1, end, sc);
				if (sc.isInitialized()) {
					return 0;
				}
			} else if (c == '[') {
				br_depth++;
			} else if (c == ']') {
				br_depth--;
			}
			index++;
		}
		return index - 1;
	}

	/** Returns the index of the matching quote mark starting at start */
	private int getStringIndex(char[] s, int start, int end, ScriptException sc) {
		for (int i = start; ; i++) {
			if (i >= end) {
				sc.initMessage("Missing closing quotation mark");
				return 0;
			}
			if (s[i] == '"' && (i == start || s[i - 1] != '\\')) {
				return i;
			}
		}
	}

	void setRawCode(RawCode code) {
		this.lines = code.lines;
		currentIndex = 0;
		waiting = false;
		timer = 0;
		check = false;
		forward = null;
		variables.reset();
		variables.create(THIS, new VariableObject(calling));
	}

	private boolean isNumber(char[] exp, int start, int end) {
		if (exp[start] == '-' || exp[start] == '+') {
			start++;
		}
		if (start >= end) {
			return false;
		}
		for (int i = start; i < end; i++) {
			if (exp[i] < '0' || exp[i] > '9') {
				return false;
			}
		}
		return true;
	}

	private boolean isVarName(char[] exp, int start, int end) {
		if (exp[start] < 'a' || exp[start] > 'z') {
			return false;
		}
		for (int i = start + 1; i < end; i++) {
			if ((exp[i] < 'a' || exp[i] > 'z') && (exp[i] < 'A' || exp[i] > 'Z') && (exp[i] < '0' || exp[i] > '9') && exp[i] != '_') {
				return false;
			}
		}
		return true;
	}

	private boolean isModuleName(char[] exp, int start, int end) {
		if (exp[start] < 'A' || exp[start] > 'Z') {
			return false;
		}
		for (int i = start + 1; i < end; i++) {
			if ((exp[i] < 'a' || exp[i] > 'z') && (exp[i] < 'A' || exp[i] > 'Z') && (exp[i] < '0' || exp[i] > '9') && exp[i] != '_') {
				return false;
			}
		}
		return true;
	}

}
