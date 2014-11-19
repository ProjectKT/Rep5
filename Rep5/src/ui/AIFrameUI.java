package ui;

import java.awt.HeadlessException;

import javax.swing.JFrame;

import ui.components.AIFrameLayout;
import ui.components.MapPanel;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;

public class AIFrameUI extends JFrame {
	
	private MapPanel mapPanel;

	public AIFrameUI() throws HeadlessException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 300, 200);
		setTitle("AIFrameUI");
		setVisible(true);

		mapPanel = new MapPanel(new AIFrameLayout());
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		getContentPane().add(mapPanel);
	}

	
	public static void main(String[] args) {
		new AIFrameUI().setVisible(true);
	}
}
