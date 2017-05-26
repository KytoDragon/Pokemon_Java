package script;

import util.ConV;

public abstract class Variable implements Cloneable {

	/** Return the integer value of this variable. */
	public abstract int getInt();

	/** Return the boolean value of this variable. */
	public abstract boolean getBool();

	/** Return the string value of this variable. */
	@Deprecated
	public abstract String getString();

	/** Return the string value of this variable. */
	public abstract char[] getCString();

	/** Return an equivalent object of this variables value. */
	public abstract Object getObject();

	/** Returns a nameless of this variable, containing the same value. */
	@Override
	public abstract Variable clone();

	/** Returns a string containing name and string value of this variable. */
	@Override
	public String toString() {
		return "Variable, value = " + getString();
	}

	/** Returns whether the given object is a variable and has the same name as this variable. */
	@Override
	public boolean equals(Object o) {
		assert(o instanceof Variable);
		return ConV.equals(((Variable) o).getCString(), getCString());
	}

	@Override
	public int hashCode() {
		// TODO make myself
		return this.getObject().hashCode();
	}

}
