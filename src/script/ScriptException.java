package script;

import java.io.PrintStream;

import util.ConV;
import util.List;

public class ScriptException {

	private char[] message;
	private List<char[]> stack;
	private int capacity;

	public ScriptException() {
		capacity = 4;
		stack = new List<>();
	}

	public void initMessage(Object... parts) {
		initMessage(ConV.concat(parts));
	}

	public void initMessage(char[] message) {
		if (this.message == null) {
			this.message = message;
			if (message != null) {
				capacity += message.length - 4;
			}
		}
	}

	public void addStackElement(Object... parts) {
		addStackElement(ConV.concat(parts));
	}

	public void addStackElement(char[] name) {
		if (name != null) {
			stack.add(name);
			capacity += name.length + 5;
		} else {
			stack.add(null);
			capacity += 4 + 5;
		}
	}

	public boolean isInitialized() {
		return message != null;
	}

	public void printStackTrace(PrintStream ps) {
		ps.println(message);
		for (char[] s : stack) {
			ps.print("at: ");
			ps.println(s);
		}
	}

	public String getStackTraceAsString() {
		StringBuilder res = new StringBuilder(capacity);
		res.append(message);
		for (char[] s : stack) {
			res.append("\nat: ");
			res.append(s);
		}
		assert (res.length() == capacity);
		return res.toString();
	}

}
