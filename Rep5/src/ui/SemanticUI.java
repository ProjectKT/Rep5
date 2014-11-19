package ui;

import java.awt.HeadlessException;

import javax.swing.JFrame;

import ui.components.MapPanel;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;

public class SemanticUI extends JFrame {
	
	private MapPanel mapPanel;

	public SemanticUI() throws HeadlessException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 300, 200);
		setTitle("SemanticUI");
		setVisible(true);

		mapPanel = new MapPanel();
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		getContentPane().add(mapPanel);
	}

	
	public static void main(String[] args) {
		new SemanticUI().setVisible(true);
	}
}
