package ui.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.peer.LightweightPeer;
import java.beans.DesignMode;

import javax.swing.JPanel;

public class MapPanel extends JPanel implements DesignMode {
	
	double zoom = 1.0f;
	Point2D center = new Point2D.Double(0, 0);
	
	public MapPanel(MapLayout layoutManager) {
		layoutManager.setMapPanel(this);
		setLayout(layoutManager);
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
	public Component add(Component comp) {
		// LayoutManager.addLayoutComponent を呼ぶ為に constraint を付ける
		return super.add("map_content", comp);
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
	
	public Point2D toRelativePosition(double ax, double ay) {
		return toRelativePosition(new Point2D.Double(ax,ay));
	}
	
	public Point2D toRelativePosition(Point2D p) {
		final double rx = getViewportWidth()/2 + (p.getX() - center.getX()) / zoom;
		final double ry = getViewportHeight()/2 + (p.getY() - center.getY()) / zoom;
		p.setLocation(rx, ry);
		return p;
	}
	
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
		int viewportX = (int) (center.getX() - viewportWidth / 2);
		int viewportY = (int) (center.getY() - viewportHeight / 2);
		return new Rectangle(viewportX, viewportY, viewportWidth, viewportHeight);
	}
	
	protected int getViewportWidth() {
		Insets insets = getInsets();
		return (getWidth() - insets.left - insets.right);
	}
	
	protected int getViewportHeight() {
		Insets insets = getInsets();
		return (getHeight() - insets.top - insets.bottom);
	}

	@Override
	protected String paramString() {
		String s = super.paramString();
		s += ",center="+center;
		return s;
	}
	
	
}
