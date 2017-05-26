package mapedit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ConditionEdit extends JPanel {
	
	private ConditionPiece[] cp;
	
	public ConditionEdit(Condition[] c) {
		cp = new ConditionPiece[c.length];
		for (int i = 0; i < c.length; i++) {
			cp[i] = new ConditionPiece(c[i]);
			this.add(cp[i]);
		}
	}
	
	public Condition[] getConditions() {
		Condition[] c = new Condition[cp.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = cp[i].getCondition();
		}
		return c;
	}
	
	private class ConditionPiece extends JPanel implements ActionListener {
		
		private JComboBox<String> type;
		private MySpinner nr, min;
		
		ConditionPiece(Condition c) {
			type = new JComboBox<String>(new String[] { "Switch", "Flag", "Variable" });
			type.addActionListener(this);
			type.setActionCommand("type");
			this.add(type);
			nr = new MySpinner(255);
			this.add(nr);
			min = new MySpinner(255);
			this.add(min);
			
			if (c instanceof ConditionSwitch) {
				type.setSelectedIndex(0);
				nr.setValue(((ConditionSwitch) c).nr);
				nr.setMax(3);
				min.setEnabled(false);
			} else if (c instanceof ConditionFlag) {
				type.setSelectedIndex(1);
				nr.setValue(((ConditionFlag) c).nr);
				min.setEnabled(false);
			} else if (c instanceof ConditionVariable) {
				type.setSelectedIndex(2);
				nr.setValue(((ConditionVariable) c).nr);
				min.setValue(((ConditionVariable) c).min);
			}
		}
		
		Condition getCondition() {
			if (type.getSelectedIndex() == 0) {
				return new ConditionSwitch(nr.getValue().intValue());
			} else if (type.getSelectedIndex() == 1) {
				return new ConditionFlag(nr.getValue().intValue());
			} else {
				return new ConditionVariable(nr.getValue().intValue(), min.getValue().intValue());
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("type")) {
				if (type.getSelectedIndex() == 0) {
					min.setEnabled(false);
					nr.setMax(3);
					if (nr.getValue().intValue() > 3) {
						nr.setValue(0);
					}
				} else if (type.getSelectedIndex() == 1) {
					min.setEnabled(false);
					nr.setMax(255);
				} else {
					min.setEnabled(true);
					nr.setMax(255);
				}
			}
		}
		
	}
}
