package ui;

import javax.swing.JFrame;

import ui.components.MapComponent;
import ui.components.MapPanel;
import ui.components.UIFrame;
import ui.components.input.MapDragListener;

public class Graphics2DTest extends JFrame {
	
	public MapPanel mapPanel;
	public MapComponent comp;

	public Graphics2DTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 300, 200);
		setTitle("タイトル");
		setVisible(true);

		mapPanel = new MapPanel();
		MapDragListener l = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(l);
		mapPanel.addMouseMotionListener(l);
		getContentPane().add(mapPanel);
		
		comp = new UIFrame();
		
		mapPanel.add(comp);
	}
	
	
	public static void main(String[] args) {
		final Graphics2DTest f = new Graphics2DTest();
		f.setVisible(true);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(f.mapPanel);
					System.out.println(f.comp);
				}
			}
		}).start();
	}
}
