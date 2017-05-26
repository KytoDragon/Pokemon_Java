package mapedit;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import util.Map;
import util.List;
import util.Logger;

public class CodeTextArea extends JTextPane {
	
	private CodeTextDocument doc;
	
	public CodeTextArea() {
		doc = new CodeTextDocument();
		Map<String, Color> keywords = new Map<String, Color>();
		
		Color defColor = new Color(127, 0, 85);
		keywords.put("break", defColor);
		keywords.put("continue", defColor);
		keywords.put("do", defColor);
		keywords.put("done", defColor);
		keywords.put("to", defColor);
		keywords.put("else", defColor);
		keywords.put("then", defColor);
		keywords.put("for", defColor);
		keywords.put("if", defColor);
		keywords.put("fi", defColor);
		keywords.put("return", defColor);
		keywords.put("this", defColor);
		keywords.put("while", defColor);
		doc.setKeywords(keywords);
		this.setDocument(doc);
		setParagraphAttributes(StyleContext.getDefaultStyleContext().addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, new MyTabSet()), false);
	}
	
	private class MyTabSet extends TabSet {
		
		private int TABSIZE = 4 * 4 * 2;
		private List<TabStop> list = new List<>();
		
		public MyTabSet() {
			super(null);
		}
		
		@Override
		public TabStop getTab(int index) {
			if (list.size() <= index) {
				while (list.size() <= index) {
					list.add(null);
				}
				list.set(index, new TabStop(TABSIZE * index));
			}
			return list.get(index);
		}
		
		@Override
		public TabStop getTabAfter(float location) {
			return getTab(getTabIndexAfter(location));
		}
		
		@Override
		public int getTabCount() {
			return -1;
		}
		
		@Override
		public int getTabIndex(TabStop tab) {
			return list.indexOf(tab);
		}
		
		@Override
		public int getTabIndexAfter(float location) {
			return (int) (location / TABSIZE) + 1;
		}
		
	}
	
	public void append(String string) {
		try {
			doc.insertString(doc.getLength(), string, null);
		} catch (BadLocationException e) {
			Logger.add(e);
		}
	}
}
