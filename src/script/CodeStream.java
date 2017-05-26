package script;

import java.io.ByteArrayOutputStream;

public class CodeStream {
	
	private ByteArrayOutputStream baos;
	
	public CodeStream() {
		baos = new ByteArrayOutputStream();
	}
	
	public void write(byte b, ScriptException s) {
		baos.write(b);
	}
	
	public void write(int b, ScriptException s) {
		write((byte)b, s);
	}
	
	public void write(byte[] b, ScriptException s){
		for (byte aB : b) {
			write(aB, s);
		}
	}
	
	public void writeEmptyPointer(ScriptException s){
		for(int i = 0; i < Compiler.POINTER_SIZE; i++){
			write((byte)0, s);
		}
	}
	
	public byte[] getResult(){
		return baos.toByteArray();
	}

	public int size() {
		return baos.size();
	}
}
