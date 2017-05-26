package mapedit;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import script.ScriptException;
import util.ConV;

public class EventEdit extends JPanel implements ListSelectionListener, ActionListener {

	private Event e;
	private Editor editor;
	private JTextField name, x, y, z;
	private JCheckBox bridge;
	private JComboBox<String> orientation;
	private JList<String> scripts;

	public EventEdit(Editor e) {
		editor = e;
		this.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		this.setLayout(new GridBagLayout());
		FormUtility form = new FormUtility();
		DocumentFilter df = new NumberFilter();
		form.addLastField(new JLabel(" "), this);

		name = new JTextField(1);
		form.addLastField(new JLabel("Name"), this);
		form.addLastField(name, this);

		x = new JTextField(1);
		((PlainDocument) x.getDocument()).setDocumentFilter(df);
		form.addLabel("X", this);
		form.addLastField(x, this);

		y = new JTextField(1);
		((PlainDocument) y.getDocument()).setDocumentFilter(df);
		form.addLabel("Y", this);
		form.addLastField(y, this);

		z = new JTextField(1);
		((PlainDocument) z.getDocument()).setDocumentFilter(df);
		form.addLabel("Z", this);
		form.addLastField(z, this);

		bridge = new JCheckBox("bridge");
		form.addLabel("", this);
		form.addLastField(bridge, this);

		orientation = new JComboBox<String>(new String[]{"SOUTH", "EAST", "NORTH", "WEST"});
		form.addLabel("Orientation", this);
		form.addLastField(orientation, this);

		JButton add = new JButton("A");
		add.setActionCommand("add");
		add.addActionListener(this);
		form.addLabel3(add, this);

		JButton remove = new JButton("R");
		remove.setActionCommand("remove");
		remove.addActionListener(this);
		form.addLabel3(remove, this);

		JButton up = new JButton("U");
		up.setActionCommand("up");
		up.addActionListener(this);
		form.addLabel3(up, this);

		JButton down = new JButton("D");
		down.setActionCommand("down");
		down.addActionListener(this);
		form.addLabel3(down, this);

		form.addLastField(new JLabel(""), this);

		scripts = new JList<String>();
		scripts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scripts.setLayoutOrientation(JList.VERTICAL);
		scripts.addListSelectionListener(this);
		form.addLastField(scripts, this);

		JButton save = new JButton("Save");
		save.addActionListener(this);
		save.setActionCommand("save");
		form.addLastField(save, this);

		JButton newEvent = new JButton("New");
		newEvent.addActionListener(this);
		newEvent.setActionCommand("new");
		form.addLastField(newEvent, this);

		JButton delete = new JButton("Delete");
		delete.addActionListener(this);
		delete.setActionCommand("delete");
		form.addLabel2(delete, this);

		reset();
	}

	public void setEvent(Event event) {
		e = event;
		name.setText(e.name);
		name.setEnabled(true);
		x.setText(ConV.toString(e.x));
		x.setEnabled(true);
		y.setText(ConV.toString(e.y));
		y.setEnabled(true);
		z.setText(ConV.toString(e.z));
		z.setEnabled(true);
		bridge.setSelected(e.bridge);
		bridge.setEnabled(true);
		orientation.setSelectedIndex(e.orientation);
		orientation.setEnabled(true);
		setScriptNum(e.scripts.length);
		scripts.setEnabled(true);
		editor.setScripts(e.scripts);
	}

	public void reset() {
		e = null;
		name.setText("");
		name.setEnabled(false);
		x.setText("");
		x.setEnabled(false);
		y.setText("");
		y.setEnabled(false);
		z.setText("");
		z.setEnabled(false);
		bridge.setSelected(false);
		bridge.setEnabled(false);
		orientation.setSelectedIndex(0);
		orientation.setEnabled(false);
		setScriptNum(0);
		scripts.setEnabled(false);
		editor.setScripts(new Script[0]);
	}

	@Override
	public void valueChanged(ListSelectionEvent ev) {
		if (scripts.getSelectedIndex() != -1) {
			editor.setScriptIndex(scripts.getSelectedIndex());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (e != null && ev.getActionCommand().equals("save")) {
			e.name = name.getText();
			if (x.getText().equals("")) {
				x.setText("0");
			}
			e.x = ConV.getInt(x.getText(), 0);
			if (y.getText().equals("")) {
				y.setText("0");
			}
			e.y = ConV.getInt(y.getText(), 0);
			if (z.getText().equals("")) {
				z.setText("0");
			}
			e.z = ConV.getInt(z.getText(), 0);
			e.bridge = bridge.isSelected();
			e.orientation = (byte) orientation.getSelectedIndex();
			e.scripts = editor.getScripts();
			ScriptException sc = new ScriptException();
			e.compile(sc);
			if (sc.isInitialized()) {
				JOptionPane.showMessageDialog(null, sc.getStackTraceAsString());
				return;
			}
			editor.repaint();
		} else if (e != null && ev.getActionCommand().equals("add")) {
			Script[] s = editor.getScripts();
			if (s.length >= 16) {
				return;
			}
			Script[] s2 = new Script[s.length + 1];
			ConV.arrayCopy(s, s2);
			s2[s.length] = new Script();
			s2[s.length].lines = "";
			s2[s.length].conditions = new Condition[0];
			s2[s.length].isRaw = true;
			editor.setScripts(s2);
			setScriptNum(s2.length);

			setScriptIndex(s.length);
		} else if (e != null && ev.getActionCommand().equals("remove")) {
			int sel = scripts.getSelectedIndex();
			if (sel == -1) {
				return;
			}
			Script[] s = editor.getScripts();
			Script[] s2 = new Script[s.length - 1];
			for (int i = 0; i < s2.length; i++) {
				if (i < sel) {
					s2[i] = s[i];
				} else {
					s2[i] = s[i + 1];
				}
			}
			editor.setScripts(s2);
			setScriptNum(s2.length);

			setScriptIndex(sel < s2.length ? sel : sel - 1);
		} else if (e != null && ev.getActionCommand().equals("up")) {
			int sel = scripts.getSelectedIndex();
			if (sel == -1 || sel == 0) {
				return;
			}
			Script[] s = editor.getScripts();
			Script tmp = s[sel];
			s[sel] = s[sel - 1];
			s[sel - 1] = tmp;
			editor.setScripts(s);

			setScriptIndex(sel - 1);
		} else if (e != null && ev.getActionCommand().equals("down")) {
			int sel = scripts.getSelectedIndex();
			Script[] s = editor.getScripts();
			if (sel == -1 || sel == s.length - 1) {
				return;
			}

			Script tmp = s[sel];
			s[sel] = s[sel + 1];
			s[sel + 1] = tmp;
			editor.setScripts(s);

			setScriptIndex(sel + 1);
		} else if (ev.getActionCommand().equals("new")) {
			Event[] old = editor.getMap().events;
			Event[] newE = new Event[old.length + 1];
			ConV.arrayCopy(old, newE);
			newE[old.length] = Event.getNew();
			editor.getMap().events = newE;
			setEvent(newE[old.length]);
			editor.repaint();
		} else if (ev.getActionCommand().equals("delete")) {
			Event[] old = editor.getMap().events;
			Event[] newE = new Event[old.length - 1];
			int i = 0;
			for (; old[i] != e; i++) {
				newE[i] = old[i];
			}
			for (; i < newE.length; i++) {
				newE[i] = old[i + 1];
			}
			editor.getMap().events = newE;
			reset();
			editor.repaint();
		}
	}

	private void setScriptNum(int num) {
		String[] snames = new String[num];
		for (int i = 0; i < num; i++) {
			snames[i] = "Script " + i / 10 + "" + i % 10;
		}
		scripts.setListData(snames);
	}

	private void setScriptIndex(int i) {
		editor.setScriptIndex(i);
		scripts.setSelectedIndex(i);
	}

	public void unselect() {
		scripts.clearSelection();
	}

	public void updatePosition() {
		x.setText(ConV.toString(e.x));
		y.setText(ConV.toString(e.y));
		z.setText(ConV.toString(e.z));
	}
}
