package mapedit;

import java.awt.EventQueue;

public class Main {
	
	public static void main(final String args[]) {
		System.getProperties().put("sun.java2d.noddraw", "true");
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new Editor(args);
			}
		});
	}
}
