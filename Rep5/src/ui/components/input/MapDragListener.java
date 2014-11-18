package ui.components.input;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import ui.components.MapPanel;

public class MapDragListener extends MouseInputAdapter {

	private Point prev;
	private MapPanel panel;
	private Cursor priorCursor;
	
	public MapDragListener(MapPanel mapPanel) {
		panel = mapPanel;
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		prev = evt.getPoint();
		priorCursor = panel.getCursor();
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if (!SwingUtilities.isLeftMouseButton(evt))
			return;

		Point current = evt.getPoint();
		double x = panel.getCenter().getX();
		double y = panel.getCenter().getY();

		if (prev != null) {
			x += prev.x - current.x;
			y += prev.y - current.y;
		}

//		if (!panel.isNegativeYAllowed()) {
//			if (y < 0) {
//				y = 0;
//			}
//		}
//
//		int maxHeight = (int) (panel.getTileFactory()
//				.getMapSize(panel.getZoom()).getHeight() * panel
//				.getTileFactory().getTileSize(panel.getZoom()));
//		if (y > maxHeight) {
//			y = maxHeight;
//		}

		prev = current;
		panel.setCenter(new Point2D.Double(x, y));
		panel.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if (!SwingUtilities.isLeftMouseButton(evt)) {
			return;
		}

		prev = null;
		panel.setCursor(priorCursor);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				panel.requestFocusInWindow();
			}
		});
	}

}
