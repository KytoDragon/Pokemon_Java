package mapedit;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import sequenze.EventMethodLibrary;

public class ScriptPage extends JPanel implements ActionListener {
	
	private JPanel side;
	// private JTextArea lines;
	private CodeTextArea lines;
	private JComboBox<String> type, message, moveType;
	private JCheckBox invisible, moveanimation, alwaysontop, fixeddirection, noclip, compile;
	private MySpinner range;
	private ConditionEdit ce;
	private Editor e;
	
	public ScriptPage(Editor e, Script script) {
		this.e = e;
		this.setLayout(new BorderLayout());
		lines = new CodeTextArea();
		if(script.lines.length() == 0 && script.code.code.length > 0){
			String[] code_lines = new script.Decompiler(script.code.code).decompile();
			String text = code_lines[0];
			for (int i = 1; i < code_lines.length; i++) {
				text += "\n" + code_lines[i];
			}
			lines.append(text);
		}else{
			lines.append(script.lines);
		}
		this.add(new JScrollPane(lines), BorderLayout.CENTER);
		
		side = new JPanel(new GridBagLayout());
		FormUtility form = new FormUtility();
		
		type = new JComboBox<String>(new String[] { "ACTION", "PLAYERTOUCH", "EVENTTOUCH", "AUTOSCRIPT", "PARALLEL", "MESSAGE", "SIGN" });
		type.setSelectedIndex(script.type);
		form.addLabel("Type", side);
		form.addLastField(type, side);
		
		range = new MySpinner(15);
		range.setValue(script.range);
		form.addLabel(range, side);
		
		message = new JComboBox<String>(new String[] { script.message });
		message.setSelectedItem(script.message);
		form.addLastField(message, side);
		
		invisible = new JCheckBox("invisible");
		invisible.setSelected(script.invisible);
		form.addLastField(invisible, side);
		
		moveanimation = new JCheckBox("moveanimation");
		moveanimation.setSelected(script.moveanimation);
		form.addLastField(moveanimation, side);
		
		alwaysontop = new JCheckBox("alwaysontop");
		alwaysontop.setSelected(script.alwaysontop);
		form.addLastField(alwaysontop, side);
		
		fixeddirection = new JCheckBox("fixeddirection");
		fixeddirection.setSelected(script.fixeddirection);
		form.addLastField(fixeddirection, side);
		
		noclip = new JCheckBox("noclip");
		noclip.setSelected(script.noclip);
		form.addLastField(noclip, side);
		
		compile = new JCheckBox("compile");
		compile.setSelected(!script.isRaw);
		form.addLastField(compile, side);
		
		moveType = new JComboBox<String>(new String[] { "FIXED", "CUSTOM", "LOOKAROUND", "RANDOM" });
		moveType.setSelectedIndex(script.moveType);
		form.addLabel("Movetype", side);
		form.addLastField(moveType, side);
		
		ce = new ConditionEdit(script.conditions);
		form.addLastField(ce, side);
		
		JButton compileB = new JButton("Compile");
		compileB.addActionListener(this);
		compileB.setActionCommand("compile");
		form.addLastField(compileB, side);
		
		JButton exit = new JButton("Close");
		exit.addActionListener(this);
		exit.setActionCommand("exit");
		form.addLastField(exit, side);
		
		this.add(side, BorderLayout.WEST);
		
		// byte directions;
		// String[] moveroutes;
		// Condition[] conditions;
		// int tex;
	}
	
	public Script get() {
		Script s = new Script();
		s.type = (byte) type.getSelectedIndex();
		s.message = (String) message.getSelectedItem();
		s.range = (byte) ((Number) range.getValue()).intValue();
		s.moveType = (byte) moveType.getSelectedIndex();
		// byte directions;
		// String[] moveroutes;
		s.invisible = invisible.isSelected();
		s.moveanimation = moveanimation.isSelected();
		s.alwaysontop = alwaysontop.isSelected();
		s.fixeddirection = fixeddirection.isSelected();
		s.noclip = noclip.isSelected();
		s.isRaw = !compile.isSelected();
		s.lines = lines.getText();
		s.conditions = ce.getConditions();
		// int tex;
		return s;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		switch (ev.getActionCommand()) {
		case "exit":
			e.exitScripts();
			break;
		case "compile":
			script.ScriptException sc = new script.ScriptException();
			script.ByteCode bc = new script.Compiler().compile(lines.getText().split("[ \\t]*(\\r?[;\\n][ \\t]*)+"), sc, EventMethodLibrary.current());
			if(sc.isInitialized()) {
				JOptionPane.showMessageDialog(null, sc.getStackTraceAsString());
				return;
			}
			break;
		}
	}
	
}
