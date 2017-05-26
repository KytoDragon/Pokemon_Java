package script;

public interface MethodLibrary {

	/** Is called when a method is called on a module. */
	public Variable callMethod(int module, char[] method, Variable[] paras, ScriptException s);
	
	/** Is called when a method is called on a object. */
	public Variable callObject(Variable object, char[] method, Variable[] paras, ScriptException s);
	
	/** Is called when an external script is called. */
	public Code callScript(String name, ScriptException s);
	
	public int getModuleId(char[] name);

	public boolean hasMethod(int module, char[] name);
}
