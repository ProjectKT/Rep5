package ui;

import java.awt.Component;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import ui.components.MapPanel;
import ui.components.SemanticNetLayout;
import ui.components.UINode;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import SemanticNet.Node;

public class SemanticUI extends JFrame {
	
	private MapPanel mapPanel;

	public SemanticUI() throws HeadlessException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 300, 200);
		setTitle("SemanticUI");
		setVisible(true);

		mapPanel = new MapPanel(new SemanticNetLayout(mapPanel));
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		getContentPane().add(mapPanel);
	}
	
	public void addNode(Node node) {
		mapPanel.add(new UINode(node));
	}

	
	public static void main(String[] args) {
		new SemanticUI().setVisible(true);
	}
}
