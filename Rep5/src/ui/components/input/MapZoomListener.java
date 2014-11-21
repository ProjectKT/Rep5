package ui.components.input;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import ui.components.MapPanel;

public class MapZoomListener extends MouseAdapter {
	
	private MapPanel panel;
	private double zoomCoeff = 1.0;
	
	public MapZoomListener(MapPanel mapPanel) {
		panel = mapPanel;
	}
	
	public void setZoomCoeff(double coeff) {
		zoomCoeff = coeff;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Point current = e.getPoint();
		Rectangle bound = panel.getViewportBounds();
		
//		double dx = current.x - bound.width / 2;
//		double dy = current.y - bound.height / 2;
//		
//		Dimension oldMapSize = panel.getTileFactory().getMapSize(panel.getZoom());
//
		panel.setZoom(panel.getZoom() + (double)e.getWheelRotation() * zoomCoeff);
//		
//		Dimension mapSize = panel.getTileFactory().getMapSize(panel.getZoom());
//
		Point2D center = panel.getCenter();
//
//		double dzw = (mapSize.getWidth() / oldMapSize.getWidth());
//		double dzh = (mapSize.getHeight() / oldMapSize.getHeight());
//
//		double x = center.getX() + dx * (dzw - 1);
//		double y = center.getY() + dy * (dzh - 1);
		double x = center.getX();
		double y = center.getY();

		panel.setCenter(new Point2D.Double(x, y));
		panel.repaint();
	}
	
	
	
}
