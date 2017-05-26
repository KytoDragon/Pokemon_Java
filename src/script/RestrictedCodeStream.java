package script;

public class RestrictedCodeStream extends CodeStream {

	private int breakPoint;

	public RestrictedCodeStream(int breakPoint){
		this.breakPoint = breakPoint;
	}
	
	@Override
	public void write(byte b, ScriptException s){
		if(breakPoint == size()){
			s.initMessage("Brakepoint reached");
			return;
		}
		super.write(b, s);
	}
}
