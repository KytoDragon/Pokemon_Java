package script;

public class ByteCode extends Code {
	
	public byte[] code;
	public int vars_num;
	public int pars_num;
	
	public ByteCode(int vars_num, int pars_num, byte[] code) {
		this.code = code;
		this.vars_num = vars_num;
		this.pars_num = pars_num;
	}
}
