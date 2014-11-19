package ui.components;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

public class MapComponent extends JComponent {

	// マップビュー
	private MapPanel panel;
	// ワールド座標系での中心の位置
	private Point2D center;
	
	public MapComponent() {
		this(new Point2D.Double(0, 0));
	}
	public MapComponent(double x, double y) {
		this(new Point2D.Double(x, y));
	}
	public MapComponent(Point2D pos) {
		super();
		this.center = pos;
	}
	
	protected void setMapPanel(MapPanel mapPanel) {
		panel = mapPanel;
	}
	
	@Override
	public int getX() {
		if (panel != null) {
			return (int) ((center.getX() - (getWidth()/2) - panel.getCenter().getX()) / panel.getZoom());
		}
		return super.getX();
	}
	
	@Override
	public int getY() {
		if (panel != null) {
			return (int) ((center.getY() - (getHeight()/2) - panel.getCenter().getY()) / panel.getZoom());
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
		s += ",center="+center+",getX()="+getX()+",getY()="+getY();
		return s;
	}
	
	public Point2D getCenter() {
		return center;
	}
	
	public void setCenter(Point2D pos) {
		this.center = pos;
	}
	
	/**
	 * {@inheritDoc}
	 * <br />
	 * サブクラスはこのメソッドをオーバーライドして独自のUIコンポーネントを描画する
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Sub classes must override this method to paint itself.
	}
}
