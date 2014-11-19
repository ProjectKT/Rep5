package ui.components;

import java.awt.Color;
import java.awt.Graphics;

public class UIFrame extends MapComponent {
	
	public UIFrame() {
		setSize(200, 200);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.red);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.blue);
		g.drawString("Hello Java2D", 0, 0);
	}
	
	
	
}
