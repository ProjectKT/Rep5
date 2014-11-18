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
		g.fillRect(40, 20, 200, 120);

		g.setColor(Color.blue);
		g.drawString("Hello Java2D", 10, 50);
	}
	
	
	
}
