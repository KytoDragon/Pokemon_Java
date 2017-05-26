package mapedit;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import util.ConV;
import util.Logger;

public class MiniWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private char status;
	private Editor editor;
	private JTextField tf1, tf2, tf3, tf4;
	private JLabel error;
	private JComboBox<String> cbx;

	public MiniWindow(char c, Editor edit) {
		status = c;
		editor = edit;
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		if (c == 'l') {
			this.setTitle("Select Map");
			this.setPreferredSize(new Dimension(200, 200));
			this.setLayout(new GridLayout(3, 2));
			addLoadButtons();
		} else if (c == 'n') {
			this.setTitle("New Map");
			this.setPreferredSize(new Dimension(200, 250));
			this.setLayout(new GridLayout(5, 2));
			addNewButtons();
		} else if (c == 'z') {
			this.setTitle("Resize");
			this.setPreferredSize(new Dimension(400, 100));
			this.setLayout(new GridLayout(2, 3));
			addResizeButtons();
		}
		this.pack();
		this.setVisible(true);
	}

	private void addLoadButtons() {
		this.add(new JLabel("Map Name"));
		File folder = new File("lib/mapdata");
		String[] maps = folder.list();
		cbx = new JComboBox<>(maps);
		this.add(cbx);
		this.add(new JLabel("Layer"));
		tf3 = new JTextField();
		this.add(tf3);
		error = new JLabel();
		this.add(error);
		JButton ok = new JButton();
		ok.addActionListener(this);
		this.add(ok);
	}

	private void addNewButtons() {
		this.add(new JLabel("Worldname"));
		tf1 = new JTextField();
		this.add(tf1);
		this.add(new JLabel("Mapname"));
		tf2 = new JTextField();
		this.add(tf2);
		this.add(new JLabel("Width"));
		tf3 = new JTextField();
		this.add(tf3);
		this.add(new JLabel("Height"));
		tf4 = new JTextField();
		this.add(tf4);
		error = new JLabel();
		this.add(error);
		JButton ok = new JButton();
		ok.addActionListener(this);
		this.add(ok);
	}

	public void addResizeButtons() {
		this.add(new JLabel("Lines"));
		JButton up = new JButton();
		up.setText("Add Above");
		up.setActionCommand("u");
		up.addActionListener(this);
		this.add(up);
		JButton down = new JButton();
		down.setText("Add Below");
		down.setActionCommand("d");
		down.addActionListener(this);
		this.add(down);
		tf1 = new JTextField();
		this.add(tf1);
		JButton left = new JButton();
		left.setText("Add Left");
		left.setActionCommand("l");
		left.addActionListener(this);
		this.add(left);
		JButton right = new JButton();
		right.setText("Add Right");
		right.setActionCommand("r");
		right.addActionListener(this);
		this.add(right);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (status == 'l') {
			Map map;
			try {
				String mapname = (String) cbx.getSelectedItem();
				map = new Map(mapname);
			} catch (RuntimeException ex) {
				Logger.add(ex);
				error.setText("Map Error!");
				return;
			}
			int layer = 0;
			try {
				layer = ConV.parseInt(tf3.getText().toCharArray());
			} catch (ParseException nfe) {
			}
			if (layer < 0 || layer > 4) {
				layer = 0;
			}
			map.setLock(layer);
			editor.setMap(map);
			close();
		} else if (status == 'n') {
			error.setText("");
			String mapname = tf2.getText();
			int width;
			int height;
			try {
				width = ConV.parseInt(tf3.getText().toCharArray());
				height = ConV.parseInt(tf4.getText().toCharArray());
			} catch (ParseException e1) {
				close();
				return;
			}
			Map map = new Map(mapname, width, height);
			map.setLock(0);
			editor.setMap(map);
			close();
		} else if (status == 'z') {
			String s = e.getActionCommand();
			int lines;
			try {
				lines = ConV.parseInt(tf1.getText().toCharArray());
			} catch (ParseException nfe) {
				return;
			}
			switch (s) {
				case "u":
					editor.getMap().addUp(lines);
					break;
				case "d":
					editor.getMap().addDown(lines);
					break;
				case "l":
					editor.getMap().addLeft(lines);
					break;
				case "r":
					editor.getMap().addRight(lines);
					break;
			}
			editor.repaint();
			// close();
		}
	}

	public void close() {
		this.dispose();
	}
}
