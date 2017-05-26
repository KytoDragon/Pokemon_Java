package script;

import util.ConV;
import util.FileReader;
import util.Logger;

public abstract class Code {
	
	public static Code getCode(int lineNum, boolean isRaw, int codeCount, FileReader fr) {
		if (isRaw) {
			char[] lines = fr.readCLines(lineNum);
			fr.skipBytes(codeCount);
			return new RawCode(split(lines));
		} else {
			fr.skipLines(lineNum);
			int vars_num = fr.readByte();
			int pars_num = fr.readByte();
			if(vars_num <= 0 || pars_num < 0){
				Logger.add(Logger.FILE, "Missing varaible number in byte code");
				return null;
			}
			return new ByteCode(vars_num, pars_num, fr.readByteArray(codeCount - 2));
		}
	}
	
	public static char[][] split(char[] lines) {
		int line_num = 1;
		for (int i = 0; i < lines.length; i++) {
			char c = lines[i];
			if (c == '\n' || c == ';') {
				line_num++;
				do {
					c = lines[++i];
				} while (c == '\t' || c == ' ' || c == '\n' || c == ';');
			}
		}
		char[][] result = new char[line_num][];
		line_num = 0;
		int start = -1;
		int end = -1;
		for (int i = 0; i < lines.length; i++) {
			char c = lines[i];
			if (c == '\n' || c == ';') {
				if(start == -1){
					continue;
				} else if (end == -1) {
					end = i;
				}
				result[line_num++] = ConV.subCstring(lines, start, end);
				start = -1;
				end = -1;
			} else if (c == ' ' || c == '\t') {
				if(end == -1){
					end = i;
				}
			} else {
				if(start == -1){
					start = i;
				}
				end = -1;
			}
		}
		if(start != -1){
			if(end == -1){
				end = lines.length;
			}
			result[line_num++] = ConV.subCstring(lines, start, end);
		}
		return result;
	}
}
