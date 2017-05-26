package script;

public final class OpCodes {

	private OpCodes() {
	}

	public final static int BYTE_MAX = (2 << 8 - 1);

	public final static byte NOP = 0x00;
	public final static byte TRUE = 0x01; // PUSH: true as boolean
	public final static byte FALSE = 0x02; // PUSH: false as boolean
	public final static byte INTEGER = 0x03; // EXTRA: integer in 7-bit bytes, highest bit determines if next byte is used, PUSH: value as integer
	public final static byte STRING = 0x04; // EXTRA: 1 byte length, <length> bytes as string, PUSH: value as string
	public final static byte VARIABLE = 0x05; // POP: variable id as integer, PUSH: variable
	public final static byte ENUM = 0x06; // POP: group name as string, enum name as string, PUSH: enum value as variable
	public final static byte METHOD_VOID = 0x07; // EXTRA: 1 byte parameter count, POP: method name as string, <count> parameters as variables, object as variable
	public final static byte METHOD_PUSH = 0x08; // EXTRA: 1 byte parameter count,POP: method name as string, <count> parameters as variables, object as variable, PUSH: result variable
	public final static byte MODULE_VOID = 0x09; // EXTRA: 1 byte parameter count,POP: method name as string, module name as string, <count> parameters as variables
	public final static byte MODULE_PUSH = 0x0A; // EXTRA: 1 byte parameter count,POP: method name as string, module name as string, <count> parameters as variables, PUSH: result variable
	public final static byte ASSIGN_VAR = 0x0B; // POP: variable as Integer, value as variable,
	public final static byte ASSIGN_VAR_PUSH = 0x0C; // POP: variable name as integer, PEEK: value as variable
	public final static byte ASSIGN_ARRAY = 0x0D; // POP: value as variable, index as string, array as variable
	public final static byte ASSIGN_ARRAY_PUSH = 0x0E; // POP: value as variable, index as string, array as variable, PUSH: value as variable
	public final static byte ACCSESS_ARRAY = 0x0F; // POP: index as string, array as variable
	public final static byte AND = 0x10; // POP: operand 2 as boolean, operand 1 as boolean, PUSH: result as boolean
	public final static byte OR = 0x11; // POP: operand 2 as boolean, operand 1 as boolean, PUSH: result as boolean
	public final static byte EQUAL = 0x12; // POP: operand 2 as string, operand 1 as string, PUSH: result as boolean
	public final static byte NOT_EQUAL = 0x13; // POP: operand 2 as string, operand 1 as string, PUSH: result as boolean
	public final static byte GREATER = 0x14; // POP: operand 2 as integer, operand 1 as integer, PUSH: result as boolean
	public final static byte GREATER_EQUALS = 0x15; // POP: operand 2 as integer, operand 1 as integer, PUSH: result as boolean
	public final static byte SMALLER = 0x16; // POP: operand 2 as integer, operand 1 as integer, PUSH: result as boolean
	public final static byte SMALLER_EQUALS = 0x17; // POP: operand 2 as integer, operand 1 as integer, PUSH: result as boolean
	public final static byte CONCAT = 0x18; // POP: operand 2 as string, operand 1 as string, PUSH: result as string
	public final static byte ADD = 0x19; // POP: operand 2 as integer, operand 1 as integer, PUSH: result as integer
	public final static byte SUBTRACT = 0x1A; // POP: operand 2 as integer, operand 1 as integer, PUSH: result as integer
	public final static byte MULTIPLY = 0x1B; // POP: operand 2 as integer, operand 1 as integer, PUSH: result as integer
	public final static byte DIVIDE = 0x1C; // POP: operand 2 as integer, operand 1 as integer, PUSH: result as integer
	public final static byte MODULO = 0x1D; // POP: operand 2 as integer, operand 1 as integer, PUSH: result as integer
	public final static byte JUMP = 0x1E; // EXTRA: n-byte pointer
	public final static byte IF = 0x1F; // EXTRA: n-byte pointer, POP: condition as boolean
	public final static byte WAIT = 0x20; // POP: wait time as integer
	public final static byte SCRIPT = 0x21; // EXTRA: 1 byte parameter count, POP: script name as string, <count> parameters as variables
	public final static byte ARRAY = 0x22; // PUSH: new array as variable
	public final static byte EXIT = 0x23;
}
