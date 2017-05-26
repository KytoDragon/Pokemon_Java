package script;

import static script.OpCodes.*;

import util.*;

public class Decompiler {

	private byte[] code;

	private Stack<Variable> stack;
	private List<String> lines;
	private Stack<Integer> gotoLocation;
	private Stack<Integer> gotoPointer;
	private Map<Integer, Integer> line_pointer;
	int currentIndex;
	int lastLine;

	public Decompiler(byte[] code) {
		this.code = code;
		stack = new Stack<>();
		lines = new List<>();
		gotoLocation = new Stack<>();
		gotoPointer = new Stack<>();
		line_pointer = new Map<>();
	}

	public String[] decompile() {
		while (currentIndex < code.length) {
			processByte();
			currentIndex++;
		}
		String[] result = lines.toArray(new String[lines.size()]);
		while (!gotoLocation.isEmpty()) {
			int position = gotoLocation.pop().intValue();
			int pointer = gotoPointer.pop().intValue();
			int line_number = line_pointer.get(new Integer(pointer));
			result[position] += line_number;
		}
		return result;
	}

	public void processByte() {
		if (lines.size() != lastLine) {
			lastLine = lines.size();
			line_pointer.put(new Integer(currentIndex), new Integer(lastLine));
		}
		switch (code[currentIndex] & 0xFF) {
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
				String var_name = "var_".concat(stack.pop().getString());
				stack.push(new VariableDummy(var_name, true));
				break;
			}
			case ENUM: {
				String enum_group = stack.pop().getString();
				String enum_name = stack.pop().getString();
				String piece = enum_group + "." + enum_name;
				pushCode(piece);
				return;
			}
			case METHOD_VOID: {
				String methodName = stack.pop().getString();
				String[] parameters = new String[code[currentIndex + 1] & 0xFF];
				for (int i = parameters.length; i-- > 0; ) {
					parameters[i] = foo();
				}
				String object = foo();
				String piece = object + "." + methodName + "(";
				for (int i = 0; i < parameters.length; i++) {
					if (i != 0) {
						piece += ", ";
					}
					piece += parameters[i];
				}
				piece += ")";
				lines.add(piece);
				currentIndex++;
				break;
			}
			case METHOD_PUSH: {
				String methodName = stack.pop().getString();
				String[] parameters = new String[code[currentIndex + 1] & 0xFF];
				for (int i = parameters.length; i-- > 0; ) {
					parameters[i] = foo();
				}
				String object = foo();
				String piece = object + "." + methodName + "(";
				for (int i = 0; i < parameters.length; i++) {
					if (i != 0) {
						piece += ", ";
					}
					piece += parameters[i];
				}
				piece += ")";
				pushCode(piece);
				currentIndex++;
				break;
			}
			case MODULE_VOID: {
				String methodName = stack.pop().getString();
				String moduleName = stack.pop().getString();
				String[] parameters = new String[code[currentIndex + 1] & 0xFF];
				for (int i = parameters.length; i-- > 0; ) {
					parameters[i] = foo();
				}
				String piece = moduleName + "." + methodName + "(";
				for (int i = 0; i < parameters.length; i++) {
					if (i != 0) {
						piece += ", ";
					}
					piece += parameters[i];
				}
				piece += ")";
				lines.add(piece);
				currentIndex++;
				break;
			}
			case MODULE_PUSH: {
				String methodName = stack.pop().getString();
				String moduleName = stack.pop().getString();
				String[] parameters = new String[code[currentIndex + 1] & 0xFF];
				for (int i = parameters.length; i-- > 0; ) {
					parameters[i] = foo();
				}
				String piece = moduleName + "." + methodName + "(";
				for (int i = 0; i < parameters.length; i++) {
					if (i != 0) {
						piece += ", ";
					}
					piece += parameters[i];
				}
				piece += ")";
				pushCode(piece);
				currentIndex++;
				break;
			}
			case ASSIGN_VAR: {
				String variableName = "var_" + stack.pop().getString();
				String value = foo();
				String piece = variableName + " = " + value;
				lines.add(piece);
				break;
			}
			case ASSIGN_VAR_PUSH: {
				String variableName = "var_" + stack.pop().getString();
				String value = foo();
				String piece = variableName + " = " + value;
				pushCode(piece);
				break;
			}
			case ASSIGN_ARRAY: {
				String value = foo();
				String index = foo();
				String array = foo();
				String piece = array + "[" + index + "] = " + value;
				lines.add(piece);
				break;
			}
			case ASSIGN_ARRAY_PUSH: {
				String value = foo();
				String index = foo();
				String array = foo();
				String piece = array + "[" + index + "] = " + value;
				pushCode(piece);
				break;
			}
			case ACCSESS_ARRAY: {
				String index = foo();
				String array = foo();
				String piece = array + "[" + index + "]";
				pushCode(piece);
				break;
			}
			case AND: {
				String second = fooBracket();
				pushCode(fooBracket() + " && " + second);
				break;
			}
			case OR: {
				String second = fooBracket();
				pushCode(fooBracket() + " || " + second);
				break;
			}
			case EQUAL: {
				String second = fooBracket();
				pushCode(fooBracket() + " == " + second);
				break;
			}
			case NOT_EQUAL: {
				String second = fooBracket();
				pushCode(fooBracket() + " != " + second);
				break;
			}
			case GREATER: {
				String second = fooBracket();
				pushCode(fooBracket() + " > " + second);
				break;
			}
			case GREATER_EQUALS: {
				String second = fooBracket();
				pushCode(fooBracket() + " >= " + second);
				break;
			}
			case SMALLER: {
				String second = fooBracket();
				pushCode(fooBracket() + " < " + second);
				break;
			}
			case SMALLER_EQUALS: {
				String second = fooBracket();
				pushCode(fooBracket() + " <= " + second);
				break;
			}
			case CONCAT: {
				String second = fooBracket();
				pushCode(fooBracket() + " .. " + second);
				break;
			}
			case ADD: {
				String second = fooBracket();
				pushCode(fooBracket() + " + " + second);
				break;
			}
			case SUBTRACT: {
				String second = fooBracket();
				pushCode(fooBracket() + " - " + second);
				break;
			}
			case MULTIPLY: {
				String second = fooBracket();
				pushCode(fooBracket() + " * " + second);
				break;
			}
			case DIVIDE: {
				String second = fooBracket();
				pushCode(fooBracket() + " / " + second);
				break;
			}
			case MODULO: {
				String second = fooBracket();
				pushCode(fooBracket() + " % " + second);
				break;
			}
			case JUMP:
				gotoLocation.push(new Integer(lines.size()));
				gotoPointer.push(new Integer(ByteInterpreter.getPointer(code, currentIndex + 1)));
				lines.add("goto ");
				currentIndex += Compiler.POINTER_SIZE;
				break;
			case IF:
				gotoLocation.push(new Integer(lines.size()));
				gotoPointer.push(new Integer(ByteInterpreter.getPointer(code, currentIndex + 1)));
				lines.add("if " + fooBracket() + " else goto ");
				currentIndex += Compiler.POINTER_SIZE;
				break;
			case WAIT:
				lines.add("wait(" + foo() + ")");
				break;
			case SCRIPT: {
				String name = stack.pop().getString();
				String[] parameters = new String[code[currentIndex + 1] & 0xFF];
				for (int i = parameters.length; i-- > 0; ) {
					parameters[i] = foo();
				}
				String piece = "#" + name + "(";
				for (int i = 0; i < parameters.length; i++) {
					if (i != 0) {
						piece += ", ";
					}
					piece += parameters[i];
				}
				piece += ")";
				lines.add(piece);
				currentIndex++;
				break;
			}
			case ARRAY:
				pushCode("array()");
				break;
			case EXIT:
				lines.add("return");
				break;
			default:
				Logger.add(Logger.SCRIPT, "Illegal opcode: ", code[currentIndex] & 0xFF);
		}
	}

	private String foo() {
		Variable v = stack.pop();
		if (v.getClass() == VariableString.class) {
			return "\"" + v.getString() + "\"";
		} else {
			return v.getString();
		}
	}

	private String fooBracket() {
		Variable v = stack.pop();
		if (v.getClass() == VariableString.class) {
			return "\"" + v.getString() + "\"";
		} else if (v.getClass() == VariableDummy.class && !((VariableDummy) v).simple) {
			return "(" + v.getString() + ")";
		} else {
			return v.getString();
		}
	}

	private void pushCode(String piece) {
		stack.push(new VariableDummy(piece, false));
	}

	private class VariableDummy extends VariableObject {

		boolean simple = false;

		public VariableDummy(String value, boolean simple) {
			super(value);
			this.simple = simple;
		}
	}
}
