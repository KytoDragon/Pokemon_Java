package mapedit;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import sequenze.EventMethodLibrary;
import util.ConV;
import util.Logger;

public class Editor extends JFrame implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	private Image img;
	private int imgi = -1;
	private Map map;
	private EventEdit ee;
	private JScrollPane mp;
	private BufferedImage[] texs, btexs, etexs;
	private JScrollPane jsp, jspb;
	private JPanel sidepanel, right;
	private ScriptEdit sce;
	
	public Editor(String[] args) {
		this.setTitle("Pokemon Map Editor");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1350, 700));
		JPanel command = new JPanel(new FlowLayout());
		addCommands(command);
		this.add(command, BorderLayout.NORTH);
		JPanel tiles = new JPanel(new GridLayout(32, 8));
		JPanel behavs = new JPanel(new GridLayout(32, 8));
		sidepanel = new JPanel(new MyCardLayout());
		jsp = new JScrollPane(tiles, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sidepanel.add(jsp);
		jspb = new JScrollPane(behavs, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sidepanel.add(jspb);
		jsp.getVerticalScrollBar().setUnitIncrement(10);
		jspb.getVerticalScrollBar().setUnitIncrement(10);
		texs = loadBufs("tiles", 8, 32, 16, 16);
		btexs = loadBufs("Behavior", 16, 16, 16, 16);
		etexs = loadBufs("Hero", 8, 8, 32, 32);
		addTiles(tiles, texs);
		addTiles(behavs, btexs);
		map = loadMap(args);
		mp = new JScrollPane(map);
		mp.getVerticalScrollBar().setUnitIncrement(10);
		map.addMouseListener(map);
		map.addMouseMotionListener(map);
		map.setEditor(this);
		right = new JPanel(new CardLayout());
		right.add(mp);
		sce = new ScriptEdit();
		right.add(sce);
		this.add(right, BorderLayout.CENTER);
		ee = new EventEdit(this);
		map.ev = ee;
		sidepanel.add(ee);
		this.add(sidepanel, BorderLayout.WEST);
		this.pack();
		this.setVisible(true);
		
		EventMethodLibrary.init();
	}
	
	public Map loadMap(String[] args) {
		Map map;
		if (args.length > 0) {
			try {
				map = new Map(args[1]);
			} catch (RuntimeException e) {
				Logger.add(Logger.EDITOR, "Kartenname fehlerhaft!");
				return null;
			}
		} else {
			map = new Map("test");
		}
		int layer = 0;
		if (args.length > 2) {
			try {
				layer = ConV.parseInt(args[2]);
			} catch (ParseException nfe) {
				Logger.add(Logger.EDITOR, "Layerwert fehlerhaft!");
			}
		}
		map.setLock(layer);
		map.setTexs(new BufferedImage[][] { texs }, btexs, etexs);
		return map;
	}
	
	public void setMap(Map m) {
		mp.getViewport().remove(map);
		map.removeMouseListener(map);
		map.removeMouseMotionListener(map);
		map.setEditor(null);
		map = m;
		map.setEditor(this);
		map.addMouseMotionListener(map);
		map.addMouseListener(map);
		mp.getViewport().add(map);
		ee.reset();
		map.ev = ee;
		mp.revalidate();
		map.setTexs(new BufferedImage[][] { texs }, btexs, etexs);
		this.repaint();
	}
	
	public void addTiles(JPanel tilebase, BufferedImage[] t) {
		if (t == null) {
				Logger.add(Logger.EDITOR, "Tiles nicht gefunden!");
			return;
		}
		for (int i = 0; i < t.length; i++) {
			JLabel jl = new JLabel(new ImageIcon(t[i]));
			jl.setOpaque(true);
			jl.setName("" + i);
			jl.addMouseListener(this);
			jl.setBackground(new Color(255, 0, 255));
			tilebase.add(jl);
		}
	}
	
	private BufferedImage[] loadBufs(String name, int w, int h, int pw, int ph) {
		BufferedImage img;
		try {
			img = ImageIO.read(new File("lib/textures/" + name + ".png"));
		} catch (IOException e) {
			Logger.add(e);
			return null;
		}
		BufferedImage[] subs = new BufferedImage[w * h];
		BufferedImage resizedImg = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		ColorModel cm = resizedImg.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		for (int i = 0; i < subs.length; i++) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			g2.fillRect(0, 0, 32, 32);
			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g2.drawImage(img.getSubimage(i % w * pw, i / w * ph, pw, ph), 0, 0, 32, 32, null);
			WritableRaster raster = resizedImg.copyData(null);
			subs[i] = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
			// System.out.println("new Tile Nr.: " + i);
		}
		g2.dispose();
		return subs;
	}
	
	public void addCommands(JPanel command) {
		JButton layerall = new JButton(new ImageIcon("lib/res/A.png"));
		layerall.setActionCommand("a");
		layerall.addActionListener(this);
		JButton layer1 = new JButton(new ImageIcon("lib/res/1.png"));
		layer1.setActionCommand("1");
		layer1.addActionListener(this);
		JButton layer2 = new JButton(new ImageIcon("lib/res/2.png"));
		layer2.setActionCommand("2");
		layer2.addActionListener(this);
		JButton layer3 = new JButton(new ImageIcon("lib/res/3.png"));
		layer3.setActionCommand("3");
		layer3.addActionListener(this);
		JButton layerp = new JButton(new ImageIcon("lib/res/P.png"));
		layerp.setActionCommand("p");
		layerp.addActionListener(this);
		JButton event = new JButton(new ImageIcon("lib/res/E.png"));
		event.setActionCommand("e");
		event.addActionListener(this);
		JButton reload = new JButton(new ImageIcon("lib/res/R.png"));
		reload.setActionCommand("r");
		reload.addActionListener(this);
		JButton save = new JButton("Save Map");
		save.setActionCommand("s");
		save.addActionListener(this);
		JButton load = new JButton("Load Map");
		load.setActionCommand("l");
		load.addActionListener(this);
		JButton newm = new JButton("New Map");
		newm.setActionCommand("n");
		newm.addActionListener(this);
		JButton del = new JButton("Delete");
		del.setActionCommand("d");
		del.addActionListener(this);
		JButton resize = new JButton("Resize");
		resize.setActionCommand("z");
		resize.addActionListener(this);
		command.add(layerall);
		command.add(layer1);
		command.add(layer2);
		command.add(layer3);
		command.add(layerp);
		command.add(event);
		command.add(reload);
		command.add(save);
		command.add(load);
		command.add(newm);
		command.add(del);
		command.add(resize);
	}
	
	public Map getMap() {
		return map;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String ac = arg0.getActionCommand();
		switch (ac) {
		case "a": {
			map.setLock(0);
			CardLayout cl = (CardLayout) sidepanel.getLayout();
			cl.first(sidepanel);
			break;
		}
		case "1": {
			map.setLock(1);
			CardLayout cl = (CardLayout) sidepanel.getLayout();
			cl.first(sidepanel);
			break;
		}
		case "2": {
			map.setLock(2);
			CardLayout cl = (CardLayout) sidepanel.getLayout();
			cl.first(sidepanel);
			break;
		}
		case "3": {
			map.setLock(3);
			CardLayout cl = (CardLayout) sidepanel.getLayout();
			cl.first(sidepanel);
			break;
		}
		case "p": {
			map.setLock(4);
			this.remove(jsp);
			CardLayout cl = (CardLayout) sidepanel.getLayout();
			cl.first(sidepanel);
			cl.next(sidepanel);
			break;
		}
		case "e": {
				map.setLock(5);
				CardLayout cl = (CardLayout) sidepanel.getLayout();
				cl.last(sidepanel);
				break;
		}
		case "r":
			texs = loadBufs("tiles", 8, 32, 16, 16);
			btexs = loadBufs("Behavior", 16, 16, 16, 16);
			etexs = loadBufs("Hero", 8, 8, 32, 32);
			map.setTexs(new BufferedImage[][] { texs }, btexs, etexs);
			break;
		case "s":
			map.saveMap();
			break;
		case "l":
			new MiniWindow('l', this);
			break;
		case "n":
			new MiniWindow('n', this);
			break;
		case "d":
			imgi = -1;
			img = null;
			this.setIconImage(null);
			break;
		case "z":
			new MiniWindow('z', this);
			break;
		}
		repaint();
	}
	
	public BufferedImage getImg() {
		return (BufferedImage) img;
	}
	
	public int getImgi() {
		return imgi;
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		ImageIcon a = (ImageIcon) (((JLabel) (arg0.getComponent())).getIcon());
		img = a.getImage();
		this.setIconImage(img);
		imgi = ConV.getInt(arg0.getComponent().getName(), 0);
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	public void setEvent(Event event) {
		if (event != null) {
			ee.setEvent(event);
		} else {
			ee.reset();
		}
	}
	
	public void setScripts(Script[] scripts) {
		sce.setScripts(this, scripts);
	}
	
	public void setScriptIndex(int index) {
		((CardLayout) right.getLayout()).last(right);
		sce.setSelectedIndex(index);
	}
	
	public void exitScripts() {
		((CardLayout) right.getLayout()).first(right);
		ee.unselect();
	}
	
	public Script[] getScripts() {
		return sce.getScripts();
	}
}