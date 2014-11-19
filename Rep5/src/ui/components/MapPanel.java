package ui.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.beans.DesignMode;

import javax.swing.JPanel;

public class MapPanel extends JPanel implements DesignMode {
	
	private double zoom = 1.0f;
	private Point2D center = new Point2D.Double(0, 0);
	
	public MapPanel() {
		setLayout(new MapLayout(this));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!isDesignTime()) {
			doPaintComponent(g);
		}
		super.paintBorder(g);
	}
	
	private void doPaintComponent(Graphics g) {
		// 何か描く

		String s = "center=("+center.getX()+","+center.getY()+"), zoom="+zoom;
		g.drawString(s, 0, 10);
	}
	
	
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
	}

	@Override
	protected void addImpl(Component comp, Object constraints, int index) {
		if (comp instanceof MapComponent) {
			((MapComponent) comp).setMapPanel(this);
		}
		super.addImpl(comp, constraints, index);
	}

	@Override
	public void setDesignTime(boolean designTime) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isDesignTime() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public double getZoom() {
		return zoom;
	}
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	
	public Point2D getCenter() {
		return center;
	}
	public void setCenter(Point2D center) {
		this.center = center;
	}
	
//	protected double getLeft() {
//		final Insets insets = getInsets();
//		final int viewportWidth = getWidth() - insets.left - insets.right;
//		return (center.getX() - viewportWidth / 2);
//	}
//	protected double getTop() {
//		final Insets insets = getInsets();
//		final int viewportHeight = getHeight() - insets.top - insets.bottom;
//		return (center.getY() - viewportHeight / 2);
//	}
	
	/**
	 * ビューポートの範囲を返す
	 * @return
	 */
	public Rectangle getViewportBounds() {
		return calculateViewportBounds(getCenter());
	}

	private Rectangle calculateViewportBounds(Point2D center) {
		Insets insets = getInsets();
		// calculate the "visible" viewport area in pixels
		int viewportWidth = getWidth() - insets.left - insets.right;
		int viewportHeight = getHeight() - insets.top - insets.bottom;
		double viewportX = (center.getX() - viewportWidth / 2);
		double viewportY = (center.getY() - viewportHeight / 2);
		return new Rectangle((int) viewportX, (int) viewportY, viewportWidth, viewportHeight);
	}

	@Override
	protected String paramString() {
		String s = super.paramString();
		s += ",center="+center;
		return s;
	}

	
	
}
