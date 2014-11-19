package ui.components;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

public class MapComponent extends JComponent {

	// マップビュー
	private MapPanel panel;
	// ワールド座標系での位置
	private Point2D pos;
	
	public MapComponent() {
		this(new Point2D.Double(0, 0));
	}
	public MapComponent(double x, double y) {
		this(new Point2D.Double(x, y));
	}
	public MapComponent(Point2D pos) {
		super();
		this.pos = pos;
	}
	
	protected void setMapPanel(MapPanel mapPanel) {
		panel = mapPanel;
	}
	
	@Override
	public int getX() {
		if (panel != null) {
			return (int) ((pos.getX() + (getWidth()/2) - panel.getCenter().getX()) / panel.getZoom());
		}
		return super.getX();
	}
	
	@Override
	public int getY() {
		if (panel != null) {
			return (int) ((pos.getY() + (getHeight()/2) - panel.getCenter().getY()) / panel.getZoom());
		}
		return super.getY();
	}
	
	@Override
	public int getWidth() {
		if (panel != null) {
			return (int) (super.getWidth() / panel.getZoom());
		}
		return super.getWidth();
	}
	
	@Override
	public int getHeight() {
		if (panel != null) {
			return (int) (super.getHeight() / panel.getZoom());
		}
		return super.getHeight();
	}
	
	@Override
	protected String paramString() {
		String s = super.paramString();
		s += ",pos="+pos+",getX()="+getX()+",getY()="+getY();
		return s;
	}
	
	public Point2D getPos() {
		return pos;
	}
	
	public void setPos(Point2D pos) {
		this.pos = pos;
	}
	
	
}
