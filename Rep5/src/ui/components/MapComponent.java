package ui.components;

import java.awt.Container;
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
			return (int) (pos.getX() - panel.getLeft());
		}
		return super.getX();
	}
	
	@Override
	public int getY() {
		if (panel != null) {
			return (int) (pos.getY() - panel.getTop());
		}
		return super.getY();
	}
	
	@Override
	protected String paramString() {
		String s = super.paramString();
		s += ",pos="+pos+",getX()="+getX()+",getY()="+getY();
		return s;
	}
	
	
	
}
