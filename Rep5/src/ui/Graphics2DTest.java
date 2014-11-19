package ui;

import java.awt.geom.Point2D;

import javax.swing.JFrame;

import ui.components.MapComponent;
import ui.components.MapPanel;
import ui.components.UIFrame;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;

public class Graphics2DTest extends JFrame {
	
	public MapPanel mapPanel;
	public MapComponent comp1;
	public MapComponent comp2;

	public Graphics2DTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 300, 200);
		setTitle("タイトル");
		setVisible(true);

		mapPanel = new MapPanel();
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		getContentPane().add(mapPanel);

		comp1 = new UIFrame();
		comp2 = new UIFrame();
		comp2.setPos(move(comp2.getPos(), -200, -200));

		mapPanel.add(comp1);
		mapPanel.add(comp2);
	}
	
	private Point2D move(Point2D pos, double dx, double dy) {
		pos.setLocation(pos.getX()+dx, pos.getY()+dy);
		return pos;
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
					System.out.println(f.comp1);
					System.out.println(f.comp2);
				}
			}
		}).start();
	}
}
