package ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graphics2DTest extends JFrame {

	public Graphics2DTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 300, 200);
		setTitle("タイトル");
		setVisible(true);

		MyPanel panel = new MyPanel();
		getContentPane().add(panel);
	}

	class MyPanel extends JPanel {

		public MyPanel() {

		}
		
		@Override
		protected void paintComponent(Graphics g) {

			// 何か描く

			g.setColor(Color.red);
			g.fillRect(40, 20, 200, 120);

			g.setColor(Color.blue);
			g.drawString("Hello Java2D", 10, 50);

		}
	}
	
	
	public static void main(String[] args) {
		new Graphics2DTest().setVisible(true);
	}
}
