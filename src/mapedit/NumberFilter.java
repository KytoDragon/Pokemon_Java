package mapedit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import util.ConV;

public class NumberFilter extends DocumentFilter {
	
	private int max;
	
	public NumberFilter(int max) {
		this.max = max;
	}
	
	public NumberFilter() {
	}
	
	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		if (max == 0 && test(string)) {
			super.insertString(fb, offset, string, attr);
			return;
		}
		int len = fb.getDocument().getLength();
		String text = fb.getDocument().getText(0, len);
		String newS = (offset > 0 ? text.substring(0, offset) : "") + string + (offset < len ? text.substring(offset) : "");
		if (testExt(newS)) {
			super.insertString(fb, offset, string, attr);
			return;
		}
	}
	
	private boolean testExt(String text) {
		return test(text) && (text.isEmpty() || ConV.getInt(text, 0) <= max);
	}
	
	private boolean test(String text) {
		return text.matches("[0-9]*");
	}
	
	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		if (max == 0 && test(text)) {
			super.replace(fb, offset, length, text, attrs);
			return;
		}
		
		int len = fb.getDocument().getLength();
		String text2 = fb.getDocument().getText(0, len);
		String newS = (offset > 0 ? text2.substring(0, offset) : "") + text + (offset + length < len ? text2.substring(offset + length) : "");
		if (testExt(newS)) {
			super.replace(fb, offset, length, text, attrs);
			return;
		}
	}
}