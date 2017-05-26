package mapedit;

import java.awt.Color;
import util.ConV;
import util.Map;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import util.Logger;

public class CodeTextDocument extends DefaultStyledDocument {
	
	private SimpleAttributeSet normal = new SimpleAttributeSet();
	private SimpleAttributeSet keyword = new SimpleAttributeSet();
	private SimpleAttributeSet string = new SimpleAttributeSet();
	private SimpleAttributeSet literal = new SimpleAttributeSet();
	private SimpleAttributeSet operator = new SimpleAttributeSet();
	private Map<String, SimpleAttributeSet> keywordSets = new Map<String, SimpleAttributeSet>();
	
	public CodeTextDocument() {
		StyleConstants.setBold(keyword, true);
		StyleConstants.setForeground(keyword, new Color(0xAF007F));
		StyleConstants.setForeground(string, new Color(0x008F00));
		StyleConstants.setForeground(literal, Color.blue);
		StyleConstants.setForeground(operator, Color.gray);
		this.putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
	}
	
	public void highlightText() {
		char[] text;
		try {
			text = this.getText(0, this.getLength()).toCharArray();
		} catch (BadLocationException e) {
			Logger.add(e);
			return;
		}
		this.setCharacterAttributes(0, text.length, normal, true);
		int last = 0;
		for (int i = 0; i < text.length; i++) {
			if (text[last] == '"') {
				if (text[i] == '"' && last != i) {
					this.setCharacterAttributes(last, i - last + 1, string, true);
					last = i + 1;
				}
			} else if (text[i] == '+' || text[i] == '-' || text[i] == '*' || text[i] == '/' || text[i] == '.') {
				if (last != i) {
					proccessWord(text, last, i);
				}
				this.setCharacterAttributes(i, 1, operator, true);
				last = i + 1;
			} else if (text[i] == '&' || text[i] == '|') {
				if (last == i - 1 && text[last] == '&') {
					this.setCharacterAttributes(last, 2, operator, true);
					last = i + 1;
				} else if (last != i) {
					proccessWord(text, last, i);
					last = i;
				}
			} else if (text[i] >= 'a' && text[i] <= 'z' || text[i] >= 'A' && text[i] <= 'Z' || text[i] >= '0' && text[i] <= '9' || text[i] == '_') {
			} else if (last == i) {
				last = i + 1;
			} else {
				proccessWord(text, last, i);
				last = i + 1;
			}
		}
		if (last != text.length) {
			proccessWord(text, last, text.length);
		}
	}
	
	public void proccessWord(char[] text, int last, int i) {
		if (text[last] >= '0' && text[last] <= '9') {
			boolean isNumber = true;
			for (int j = last; j < i; j++) {
				if (text[j] < '0' || text[j] > '9') {
					isNumber = false;
					break;
				}
			}
			if (isNumber) {
				this.setCharacterAttributes(last, i - last, literal, true);
			}
		} else if (text[last] >= 'a' && text[last] <= 'z' || text[last] >= 'A' && text[last] <= 'Z' || text[last] == '_') {
			if (keywordSets.containsKey(new String(text, last, i - last))) {
				this.setCharacterAttributes(last, i - last, keyword, true);
			} else if (ConV.equals(text, last, i, "true") || ConV.equals(text, last, i, "false") || ConV.equals(text, last, i, "null")) {
				this.setCharacterAttributes(last, i - last, literal, true);
			}
		}
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offs, str, normal);
		highlightText();
	}
	
	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		highlightText();
	}
	
	public void setKeywords(Map<String, Color> aKeywordList) {
		if (aKeywordList != null) {
			for (util.Map.Entry<String, Color> entry : aKeywordList) {
				SimpleAttributeSet temp = new SimpleAttributeSet();
				StyleConstants.setForeground(temp, entry.getValue());
				StyleConstants.setBold(temp, true);
				this.keywordSets.put(entry.getKey(), temp);
			}
		}
	}
}
