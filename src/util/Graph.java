package util;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Graph extends JFrame {

	private int[] data;
	private int current;
	private int width;
	private boolean closing;

	public Graph(int width, int height) {
		super("Graph");
		data = new int[width];
		this.width = width;
		try {
			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					getContentPane().setPreferredSize(new Dimension(width, height));
					setResizable(false);
					setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					addWindowListener(new WindowListener() {

						@Override
						public void windowOpened(WindowEvent arg0) {
						}

						@Override
						public void windowIconified(WindowEvent arg0) {
						}

						@Override
						public void windowDeiconified(WindowEvent arg0) {
						}

						@Override
						public void windowDeactivated(WindowEvent arg0) {
						}

						@Override
						public void windowClosing(WindowEvent arg0) {
							closing = true;
						}

						@Override
						public void windowClosed(WindowEvent arg0) {
							closing = true;
						}

						@Override
						public void windowActivated(WindowEvent arg0) {
						}
					});
					add(new JPanel() {

						@Override
						protected void paintComponent(Graphics g) {
							super.paintComponent(g);
							g.clearRect(0, 0, width, height);
							for (int i = 0; i < width; i++) {
								g.drawLine(i, height, i, height - data[(current + i) % width]);
							}
							g.dispose();
						}

					});
					pack();
					setVisible(true);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.add(e);
			dispose();
			closing = true;
		}
	}

	public void draw(int point) {
		if (closing) {
			return;
		}
		data[current] = point;
		current = (current + 1) % width;
		repaint();
	}

	public void close() {
		super.dispose();
	}
}
