package mapedit;

import javax.swing.JTabbedPane;
import util.ConV;

public class ScriptEdit extends JTabbedPane {
	
	private ScriptPage[] s;
	
	public ScriptEdit() {
	}
	
	public void setScripts(Editor e, Script[] scripts) {
		this.removeAll();
		s = new ScriptPage[scripts.length];
		for (int i = 0; i < scripts.length; i++) {
			s[i] = new ScriptPage(e, scripts[i]);
			this.addTab(ConV.toString(i), s[i]);
		}
		this.repaint();
	}
	
	public void setIndex(int index) {
		this.setSelectedIndex(index);
	}
	
	public Script[] getScripts() {
		Script[] res = new Script[s.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = s[i].get();
		}
		return res;
	}
	
}
