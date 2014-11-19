package ui.components;

import java.awt.Color;
import java.awt.Graphics;

public class UIFrame extends MapComponent {
	
	public UIFrame() {
		setSize(100, 100);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println("paintComponent(): "+this);
		g.setColor(Color.red);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	
	
}