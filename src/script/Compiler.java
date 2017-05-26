package script;

import static script.OpCodes.*;

import util.ConV;
import util.Encoding;
import util.Stack;
import util.StringMap;

public class Compiler {

	private String[] lines;

	private CodeStream stream;

	private Stack<Boolean> loops;
	private static final Boolean STACK_WHILE = Boolean.TRUE;
	private static final Boolean STACK_BREAK = Boolean.FALSE;
	private Stack<Integer> loopPointers;

	private Stack<Boolean> ifs;
	private static final Boolean STACK_IF = Boolean.TRUE;
	private static final Boolean STACK_ELSE = Boolean.FALSE;
	private Stack<Integer> ifPointers;

	private Stack<Integer> jumpLocation;
	private Stack<Integer> jumpPointer;

	private int[] line_pointers;
	private Stack<Integer> gotoLocation;
	private Stack<Integer> gotoPointer;

	private StringMap<Integer> vars;
	static final int THIS_id = 1;
	private int var_id = THIS_id;
	static final int PAR_id = 1 << 16;
	private int par_num = 0;

	static final int POINTER_SIZE = 2;

	private MethodLibrary library;

	static final char[] IF_START = "if ".toCharArray();
	static final char[] IF_END = " then".toCharArray();
	static final char[] IF_EXIT = "fi".toCharArray();
	static final char[] ELSE = "else".toCharArray();
	static final char[] ELSE_IF = "else if ".toCharArray();
	static final char[] WHILE_START = "while ".toCharArray();
	static final char[] WHILE_END = " do".toCharArray();
	static final char[] WHILE_EXIT = "done".toCharArray();
	static final char[] FOR_START = "for ".toCharArray();
	static final char[] FOR_END = " do".toCharArray();
	static final char[] FOR_MIDDLE = " to ".toCharArray();
	static final char[] CONTINUE = "continue".toCharArray();
	static final char[] BREAK = "break".toCharArray();
	static final char[] THIS = "this".toCharArray();
	static final char[] RETURN = "return".toCharArray();
	static final char[] GOTO = "goto ".toCharArray();
	static final char[] IF_GOTO = " else goto ".toCharArray();
	static final char[] WAIT_START = "wait(".toCharArray();
	static final char[] WAIT_END = ")".toCharArray();
	static final char[] PAR = "par".toCharArray();

	public Compiler() {
		vars = new StringMap<>();
		loops = new Stack<>();
		loopPointers = new Stack<>();
		ifs = new Stack<>();
		ifPointers = new Stack<>();
		jumpLocation = new Stack<>();
		jumpPointer = new Stack<>();
		gotoLocation = new Stack<>();
		gotoPointer = new Stack<>();
	}

	public void reset() {
		vars.clear();
		loops.clear();
		loopPointers.clear();
		ifs.clear();
		ifPointers.clear();
		jumpLocation.clear();
		jumpPointer.clear();
		gotoLocation.clear();
		gotoPointer.clear();
		stream = null;
	}

	public void setBreakpoint(int breakPoint) {
		stream = new RestrictedCodeStream(breakPoint);
	}

	public ByteCode compile(String[] lines, ScriptException s, MethodLibrary ml) {
		this.lines = lines;
		if (stream == null) {
			stream = new CodeStream();
		}
		library = ml;
		vars.put(THIS, THIS_id);
		line_pointers = new int[lines.length];
		for (int i = 0; i < lines.length; i++) {
			line_pointers[i] = stream.size();
			evaluateLine(lines[i].toCharArray(), s);
			if (s.isInitialized()) {
				s.addStackElement("Expression ", lines[i]);
				s.addStackElement("Line ", i + 1, " out of ", lines.length);
				return null;
			}
		}
		if (!ifs.isEmpty()) {
			s.initMessage("Open if at end of code");
			return null;
		} else if (!loops.isEmpty()) {
			s.initMessage("Open loop at end of code");
			return null;
		}
		byte[] result = stream.getResult();
		while (!jumpLocation.isEmpty()) {
			int position = jumpLocation.pop().intValue();
			int pointer = jumpPointer.pop().intValue();
			if (pointer >= ConV.pow(2, 8 * POINTER_SIZE)) {
				s.initMessage("Pointer points outside address space: ", pointer, ", current pointer size is ", POINTER_SIZE, " bytes");
				return null;
			}
			for (int i = 0; i < POINTER_SIZE; i++) {
				result[position + i] = (byte) ((pointer >> (8 * (POINTER_SIZE - i - 1))) & 0xFF);
			}
		}
		while (!gotoLocation.isEmpty()) {
			int position = gotoLocation.pop().intValue();
			int line_number = gotoPointer.pop().intValue();
			int pointer = line_pointers[line_number];
			for (int i = 0; i < POINTER_SIZE; i++) {
				result[position + i] = (byte) ((pointer >> (8 * (POINTER_SIZE - i - 1))) & 0xFF);
			}
		}
		return new ByteCode(var_id, par_num, result);
	}

	private void evaluateLine(char[] lineC, ScriptException s) {
		int end = lineC.length;
		if (ConV.startsWith(lineC, IF_START)) {
			if (!ConV.endsWith(lineC, IF_END)) {
				int index = ConV.indexOf(lineC, IF_GOTO);
				if (index == -1) {
					s.initMessage("Missing \"", IF_END, "\" for ", IF_START);
					return;
				}
				pushExpression(lineC, IF_START.length, index, true, s);
				if (!isNumber(lineC, index + IF_GOTO.length, end)) {
					s.initMessage("Unparsable jump mark: ", ConV.subCstring(lineC, index + IF_GOTO.length, end));
					return;
				}
				int line_number = ConV.getInt(lineC, index + IF_GOTO.length);
				if (line_number < 0 || line_number >= lines.length) {
					s.initMessage("Jump mark out of bounds: ", ConV.subCstring(lineC, index + IF_GOTO.length, end));
					return;
				}
				stream.write(IF, s);
				gotoLocation.push(new Integer(stream.size()));
				gotoPointer.push(new Integer(line_number));
				stream.writeEmptyPointer(s);
				return;
			}
			pushExpression(lineC, IF_START.length, end - IF_END.length, true, s);
			stream.write(IF, s);
			ifs.push(STACK_IF);
			ifPointers.push(new Integer(stream.size()));
			stream.writeEmptyPointer(s);
		} else if (ConV.startsWith(lineC, ELSE_IF)) {
			if (!ConV.endsWith(lineC, IF_END)) {
				s.initMessage("Missing \"", IF_END, "\" for ", ELSE_IF);
				return;
			}
			if (STACK_IF != ifs.pop()) {
				s.initMessage("Missing \"", IF_START, "\" for ", ELSE_IF);
				return;
			}
			int pointer = ifPointers.pop().intValue();

			stream.write(JUMP, s);
			ifs.push(STACK_ELSE);
			ifPointers.push(new Integer(stream.size()));
			stream.writeEmptyPointer(s);

			if (s.isInitialized()) {
				return;
			}

			jumpLocation.push(new Integer(pointer));
			jumpPointer.push(new Integer(stream.size()));
			pushExpression(lineC, ELSE_IF.length, end - IF_END.length, true, s);
			stream.write(IF, s);
			ifs.push(STACK_IF);
			ifPointers.push(new Integer(stream.size()));
			stream.writeEmptyPointer(s);
		} else if (ConV.equals(lineC, ELSE)) {
			if (STACK_IF != ifs.pop()) {
				s.initMessage("Missing \"", IF_START, "\" for ", ELSE);
				return;
			}
			int pointer = ifPointers.pop().intValue();
			jumpLocation.push(new Integer(pointer));
			jumpPointer.push(new Integer(stream.size() + 3));
			stream.write(JUMP, s);
			ifs.push(STACK_IF);
			ifPointers.push(new Integer(stream.size()));
			stream.writeEmptyPointer(s);
		} else if (ConV.equals(lineC, IF_EXIT)) {
			if (STACK_IF != ifs.pop()) {
				s.initMessage("Missing \"", IF_START, "\" for ", IF_EXIT);
				return;
			}
			int pointer = ifPointers.pop().intValue();
			jumpLocation.push(new Integer(pointer));
			jumpPointer.push(new Integer(stream.size()));

			while (STACK_ELSE == ifs.peek()) {
				ifs.pop();
				pointer = ifPointers.pop().intValue();
				jumpLocation.push(new Integer(pointer));
				jumpPointer.push(new Integer(stream.size()));
			}
		} else if (ConV.startsWith(lineC, WHILE_START)) {
			if (!ConV.endsWith(lineC, WHILE_END)) {
				s.initMessage("Missing \"", WHILE_END, "\" for ", WHILE_START);
				return;
			}
			loopPointers.push(new Integer(stream.size()));
			pushExpression(lineC, WHILE_START.length, end - WHILE_END.length, true, s);
			stream.write(IF, s);
			loops.push(STACK_WHILE);
			loopPointers.push(new Integer(stream.size()));
			stream.writeEmptyPointer(s);
		} else if (ConV.startsWith(lineC, FOR_START)) {
			if (!ConV.endsWith(lineC, FOR_END)) {
				s.initMessage("Missing \"", FOR_END, "\" for ", FOR_START);
				return;
			} else if (!ConV.contains(lineC, FOR_MIDDLE)) {
				s.initMessage("Missing \"", FOR_MIDDLE, "\" for ", FOR_START);
				return;
			}
			int middleIndex = ConV.indexOf(lineC, FOR_MIDDLE);
			char[] name;
			if (ConV.contains(lineC, '=') && ConV.indexOf(lineC, '=') < middleIndex) {
				pushExpression(lineC, FOR_START.length, middleIndex, false, s);
				name = ConV.trimToCString(lineC, FOR_START.length, ConV.indexOf(lineC, 0, end, '='));
			} else {
				name = ConV.trimToCString(lineC, FOR_START.length, middleIndex);
			}
			if (!isVarName(name, 0, name.length)) {
				s.initMessage("Malformed variable name in for loop: ", name);
				return;
			}

			stream.write(JUMP, s);
			jumpLocation.push(new Integer(stream.size()));
			stream.writeEmptyPointer(s);

			loopPointers.push(new Integer(stream.size()));

			pushVariable(name, s);
			stream.write(VARIABLE, s);
			pushInteger(1, s);
			stream.write(ADD, s);
			pushVariable(name, s);
			stream.write(ASSIGN_VAR, s);

			jumpPointer.push(new Integer(stream.size()));

			pushVariable(name, s);
			stream.write(VARIABLE, s);
			pushExpression(lineC, middleIndex + FOR_MIDDLE.length, end - FOR_END.length, true, s);
			stream.write(SMALLER, s);

			stream.write(IF, s);
			loops.push(STACK_WHILE);
			loopPointers.push(new Integer(stream.size()));
			stream.writeEmptyPointer(s);
		} else if (ConV.equals(lineC, WHILE_EXIT)) {
			stream.write(JUMP, s);
			while (STACK_BREAK == loops.peek()) {
				loops.pop();
				int pointer = loopPointers.pop().intValue();
				jumpLocation.push(new Integer(pointer));
				jumpPointer.push(new Integer(stream.size() + 2));
			}
			if (STACK_WHILE != loops.pop()) {
				s.initMessage("Missing loop for ", WHILE_EXIT);
				return;
			}
			int pointer = loopPointers.pop().intValue();
			int position = loopPointers.pop().intValue();
			jumpLocation.push(new Integer(stream.size()));
			jumpPointer.push(new Integer(position));
			stream.writeEmptyPointer(s);
			jumpLocation.push(new Integer(pointer));
			jumpPointer.push(new Integer(stream.size()));
		} else if (ConV.equals(lineC, CONTINUE)) {
			stream.write(JUMP, s);
			if (STACK_WHILE != loops.peek()) {
				s.initMessage("Missing loop for ", CONTINUE);
				return;
			}
			int pointer = loopPointers.pop().intValue();
			int position = loopPointers.peek().intValue();

			jumpLocation.push(new Integer(stream.size()));
			jumpPointer.push(new Integer(position));
			stream.writeEmptyPointer(s);
			loopPointers.push(pointer);
		} else if (ConV.equals(lineC, BREAK)) {
			stream.write(JUMP, s);
			loops.push(STACK_BREAK);
			loopPointers.push(new Integer(stream.size()));
			stream.writeEmptyPointer(s);
		} else if (ConV.equals(lineC, RETURN)) {
			stream.write(EXIT, s);
		} else if (ConV.startsWith(lineC, WAIT_START) && ConV.endsWith(lineC, WAIT_END)) {
			pushExpression(lineC, WAIT_START.length, end - WAIT_END.length, true, s);
			stream.write(WAIT, s);
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
			stream.write(JUMP, s);
			gotoLocation.push(new Integer(stream.size()));
			gotoPointer.push(new Integer(line_number));
			stream.writeEmptyPointer(s);
		} else if (end > 0 && lineC[0] == '#') {
			int index = ConV.indexOf(lineC, '(');
			if (index == -1 || getBracketIndex(lineC, index + 1, end, s) != end - 1) {
				s.initMessage("Mismatched or missing brackets for script call");
				return;
			} else if (!isVarName(lineC, 1, index)) {
				s.initMessage("Malformed script name: ", ConV.subCstring(lineC, 0, index));
				return;
			}
			int count = pushParameter(lineC, index + 1, end - 1, s);
			pushString(lineC, 1, index, s);
			stream.write(SCRIPT, s);
			stream.write(count, s);
		} else {
			pushExpression(lineC, 0, end, false, s);
		}
	}

	private void pushExpression(char[] exp, int start, int end, boolean returnResult, ScriptException s) {
		if (s.isInitialized()) {
			return;
		}
		while (exp[start] == ' ' || exp[start] == '\t') {
			start++;
		}
		while (exp[end - 1] == ' ' || exp[end - 1] == '\t') {
			end--;
		}
		if (start >= end) {
			s.initMessage("Empty Expression");
			return;
		}
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
				} else if (priority <= 3 && c == '.' && i - 1 >= start && exp[i - 1] == '.') {
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
					}
				} else if (c == '(') {
					i = getBracketIndex(exp, i + 1, end, s);
				} else if (c == '[') {
					i = getSquareBracketIndex(exp, i + 1, end, s);
				} else if (c == '"') {
					i = getStringIndex(exp, i + 1, end, s);
				}
				if (s.isInitialized()) {
					return;
				}
			}
			if (priority > 0) {
				char c = exp[index];
				if (priority >= 3 && priority != 6) {
					if (c == '=' || c == '|' || c == '&' || c == '.') {
						pushExpression(exp, start, index - 1, returnResult, s);
					} else {
						pushExpression(exp, start, index, returnResult, s);
					}
					pushExpression(exp, index + 1, end, returnResult, s);
				} else if (priority != 6) {
					pushExpression(exp, start, index, returnResult, s);
					pushExpression(exp, index + 1, end, returnResult, s);
				} else {
					int i = index;
					while (i > start && (exp[i - 1] == ' ' || exp[i - 1] == '\t')) {
						i--;
					}
					if (i == start) {
						s.initMessage("Can not assign to empty expression: ", ConV.subCstring(exp, start, index));
						return;
					}
					if (isVarName(exp, start, i)) {
						pushExpression(exp, index + 1, end, true, s);
						pushVariable(ConV.subCstring(exp, start, i), s);
						if (returnResult) {
							stream.write(ASSIGN_VAR_PUSH, s);
						} else {
							stream.write(ASSIGN_VAR, s);
						}
					} else {
						int aIndex = ConV.indexOf(exp, start, end, '[');
						if (aIndex == -1 || getSquareBracketIndex(exp, aIndex + 1, end, s) != i - 1) {
							s.initMessage("Left side of assignment is not a variable: ", ConV.subCstring(exp, start, i));
							return;
						}
						pushExpression(exp, start, aIndex, true, s);
						pushExpression(exp, aIndex + 1, i - 1, true, s);
						pushExpression(exp, index + 1, end, true, s);
						if (returnResult) {
							stream.write(ASSIGN_ARRAY_PUSH, s);
						} else {
							stream.write(ASSIGN_ARRAY, s);
						}
					}
				}
				if (returnResult) {
					switch (c) {
						case '+':
							stream.write(ADD, s);
							break;
						case '-':
							stream.write(SUBTRACT, s);
							break;
						case '*':
							stream.write(MULTIPLY, s);
							break;
						case '/':
							stream.write(DIVIDE, s);
							break;
						case '%':
							stream.write(MODULO, s);
							break;
						case '|':
							stream.write(OR, s);
							break;
						case '&':
							stream.write(AND, s);
							break;
						case '=':
							char c2 = exp[index - 1];
							switch (c2) {
								case '=':
									stream.write(EQUAL, s);
									break;
								case '<':
									stream.write(SMALLER_EQUALS, s);
									break;
								case '>':
									stream.write(GREATER_EQUALS, s);
									break;
								case '!':
									stream.write(NOT_EQUAL, s);
									break;
							}
							break;
						case '<':
							stream.write(SMALLER, s);
							break;
						case '>':
							stream.write(GREATER, s);
							break;
						case '.':
							stream.write(CONCAT, s);
							break;
					}
				}
			} else if (priority == 0) {
				if (exp[index] == '.') {
					int bracket = ConV.indexOf(exp, index + 1, end, '(');
					if (bracket == -1) {
						if (isModuleName(exp, start, index) && isVarName(exp, index + 1, end)) {
							if (returnResult) {
								pushString(exp, index + 1, end, s);
								pushString(exp, start, index, s);
								stream.write(ENUM, s);
							}
						} else {
							s.initMessage("Failed to access enum: malformed names");
						}
					} else {
						if (getBracketIndex(exp, bracket + 1, end, s) != end - 1) {
							if (exp[end - 1] == ')') {
								s.initMessage("Mismatched brackets or missing operator for function call");
								return;
							} else if (getBracketIndex(exp, bracket + 1, end, s) == -1) {
								s.initMessage("Missing closing bracket for function call");
								return;
							} else {
								s.initMessage("Trailing characters in function call");
								return;
							}
						} else if (!isVarName(exp, index + 1, bracket)) {
							s.initMessage("Malformed function name");
							return;
						}
						if (isModuleName(exp, start, index)) {
							int count = pushParameter(exp, bracket + 1, end - 1, s);
							char[] module_name = ConV.subCstring(exp, start, index);
							int module = library.getModuleId(module_name);
							if (module == -1) {
								s.initMessage("Unknown module: ", module_name);
								return;
							}
							char[] method_name = ConV.subCstring(exp, index + 1, bracket);
							if (!library.hasMethod(module, method_name)) {
								s.initMessage("Unknown method: ", method_name, " in module ", module_name);
								return;
							}
							pushString(exp, start, index, s);
							pushString(exp, index + 1, bracket, s);
							if (returnResult) {
								stream.write(MODULE_PUSH, s);
							} else {
								stream.write(MODULE_VOID, s);
							}
							stream.write(count, s);
						} else {
							pushExpression(exp, start, index, true, s);
							int count = pushParameter(exp, bracket + 1, end - 1, s);
							pushString(exp, index + 1, bracket, s);
							if (returnResult) {
								stream.write(METHOD_PUSH, s);
							} else {
								stream.write(METHOD_VOID, s);
							}
							stream.write(count, s);
						}
					}
				} else {
					if (getSquareBracketIndex(exp, index + 1, end, s) != end - 1) {
						if (exp[end - 1] == ']') {
							s.initMessage("Mismatched square brackets or missing operator: ", ConV.subCstring(exp, index, end));
							return;
						} else if (getBracketIndex(exp, index + 1, end, s) == -1) {
							s.initMessage("Missing closing square bracket: ", ConV.subCstring(exp, index, end));
							return;
						} else {
							s.initMessage("Trailing characters after square bracket: ", ConV.subCstring(exp, index, end));
							return;
						}
					}
					pushExpression(exp, start, index, returnResult, s);
					if (s.isInitialized()) {
						return;
					}
					pushExpression(exp, index + 1, end - 1, returnResult, s);
					if (returnResult) {
						stream.write(ACCSESS_ARRAY, s);
					}
				}
			} else {
				if (exp[start] == '(' && exp[end - 1] == ')') {
					if (getBracketIndex(exp, start + 1, end, s) != end - 1) {
						s.initMessage("Mismatched brackets or missing operator");
						return;
					}
					pushExpression(exp, start + 1, end - 1, returnResult, s);
				} else if (isNumber(exp, start, end)) {
					if (returnResult) {
						int number = ConV.getInt(exp, start);
						pushInteger(number, s);
					}
				} else if (exp[start] == '\"' && exp[end - 1] == '\"') {
					if (getStringIndex(exp, start + 1, end, s) != end - 1) {
						s.initMessage("Mismatched quotation marks or missing operator");
						return;
					}
					if (returnResult) {
						pushString(exp, start + 1, end - 1, s);
					}
				} else if (ConV.equals(exp, start, end, "true")) {
					if (returnResult) {
						stream.write(TRUE, s);
					}
				} else if (ConV.equals(exp, start, end, "false")) {
					if (returnResult) {
						stream.write(FALSE, s);
					}
				} else if (isVarName(exp, start, end)) {
					if (returnResult) {
						pushVariable(ConV.subCstring(exp, start, end), s);
						stream.write(VARIABLE, s);
					}
				} else {
					int begin = ConV.indexOf(exp, start, end, '(');
					if (begin == -1 || !isVarName(exp, start, begin)) {
						s.initMessage("Could not parse expression, unknown content");
						return;
					}
					if (getBracketIndex(exp, begin + 1, end, s) != end - 1) {
						s.initMessage("Mismatched brackets or missing operator: ", ConV.subCstring(exp, begin, end));
						return;
					}
					if (ConV.equals(exp, start, begin, "array")) {
						if (end != begin + 2) {
							s.initMessage("Array creation does not take aruments: ", ConV.subCstring(exp, begin, end));
							return;
						}
						stream.write(ARRAY, s);
					} else {
						pushVariable(THIS, s);
						stream.write(VARIABLE, s);
						if (s.isInitialized()) {
							return;
						}
						int count = pushParameter(exp, begin + 1, end - 1, s);
						pushString(exp, start, begin, s);
						if (returnResult) {
							stream.write(METHOD_PUSH, s);
						} else {
							stream.write(METHOD_VOID, s);
						}
						stream.write(count, s);
					}
				}
			}
		} finally {
			if (s.isInitialized()) {
				s.addStackElement("Expression: ", ConV.substring(exp, start, end));
			}
		}
	}

	private void pushString(char[] s, int start, int end, ScriptException sc) {
		stream.write(STRING, sc);
		byte[] b = Encoding.convertUTF8(s, start, end);
		if (b == null) {
			sc.initMessage("String contains invalid UTF16 characters: ", s);
			return;
		} else if (b.length > BYTE_MAX) {
			sc.initMessage("String is too large: ", s);
			return;
		}
		stream.write(b.length, sc);
		stream.write(b, sc);
	}

	private void pushVariable(char[] s, ScriptException sc) {
		if (!vars.containsKey(s)) {
			if (isParName(s)) {
				int i = ConV.getInt(s, 3);
				par_num = ConV.max(par_num, i + 1);
				vars.put(s, new Integer(PAR_id + i));
			} else {
				var_id++;
				vars.put(s, new Integer(var_id));
			}
		}
		int id = vars.get(s).intValue();
		pushInteger(id, sc);
	}

	private boolean isParName(char[] s) {
		return s.length > 3 && ConV.startsWith(s, PAR) && isNumber(s, 0, s.length) && s[3] != '+' && s[3] != '-';
	}

	private int pushParameter(char[] exp, int start, int end, ScriptException sc) {
		if (sc.isInitialized()) {
			return 0;
		}
		if (start == end) {
			return 0;
		}
		int comma = 0;
		for (int i = start; i < end; i++) {
			char c = exp[i];
			if (c == '"') {
				i = getStringIndex(exp, i + 1, end, sc);
			} else if (c == '(') {
				i = getBracketIndex(exp, i, end, sc);
			} else if (c == ',') {
				comma++;
			}
			if (sc.isInitialized()) {
				return 0;
			}
		}
		int result = comma + 1;
		if (result > BYTE_MAX) {
			sc.initMessage("Too many parameters!");
			return 0;
		}
		int last = start;
		for (int i = start; i < end && comma >= 0; i++) {
			char c = exp[i];
			if (c == '"') {
				i = getStringIndex(exp, i + 1, end, sc);
			} else if (c == '(') {
				i = getBracketIndex(exp, i, end, sc);
			} else if (c == ',') {
				pushExpression(exp, last, i, true, sc);
				last = i + 1;
				comma--;
			}
			if (sc.isInitialized()) {
				return 0;
			}
		}
		pushExpression(exp, last, end, true, sc);
		if (sc.isInitialized()) {
			return 0;
		}
		return result;
	}

	/** Returns the index of the matching bracket starting at start. */
	private int getBracketIndex(char[] s, int start, int end, ScriptException sc) {
		int index = start;
		int depth = 1;
		while (depth > 0) {
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
				depth++;
			} else if (c == ')') {
				depth--;
			}
			index++;
		}
		return index - 1;
	}

	/** Returns the index of the matching square bracket starting at start. */
	private int getSquareBracketIndex(char[] s, int start, int end, ScriptException sc) {
		int index = start;
		int depth = 1;
		while (depth > 0) {
			if (index >= end) {
				sc.initMessage("Missing closing square bracket");
				return 0;
			}
			char c = s[index];
			if (c == '"') {
				index = getStringIndex(s, index + 1, end, sc);
			} else if (c == '(') {
				index = getBracketIndex(s, index + 1, end, sc);
			} else if (c == '[') {
				depth++;
			} else if (c == ']') {
				depth--;
			}
			if (sc.isInitialized()) {
				return 0;
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

	private void pushInteger(int number, ScriptException sc) {
		stream.write(INTEGER, sc);
		boolean isNegativ = number < 0;
		if (isNegativ) {
			number = -number;
		}
		boolean signDown = false;
		int base = 27;
		while (base >= -1) {
			if (signDown) {
				stream.write(((number >> base) & 0x7F) | (base > 0 ? 0x80 : 0), sc);
			} else {
				if (base == -1) {
					stream.write(number & 0x7F, sc);
				} else if (number >> base != 0) {
					stream.write((number >> base) | (base > 0 ? 0x80 : 0) | (isNegativ ? 0x40 : 0), sc);
					signDown = true;
					base += 1;
				}
			}
			base -= 7;
		}
	}

}
