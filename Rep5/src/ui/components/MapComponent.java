package ui.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

public class MapComponent extends JComponent {

	// マップビュー
	MapPanel panel;
	// ワールド座標系での中心の位置
	Point2D.Double center;
	// 幅
	double width;
	// 高さ
	double height;
	
	public MapComponent() {
		this(new Point2D.Double(0, 0));
	}
	public MapComponent(double x, double y) {
		this(new Point2D.Double(x, y));
	}
	public MapComponent(Point2D.Double pos) {
		super();
		this.center = pos;
	}
	
	protected void setMapPanel(MapPanel mapPanel) {
		panel = mapPanel;
	}
	
	@Override
	public int getX() {
		if (panel != null) {
			return (int) panel.toRelativeX(center.x - width/2.0);
		}
		return super.getX();
	}
	
	@Override
	public int getY() {
		if (panel != null) {
			return (int) panel.toRelativeY(center.y - height/2.0);
		}
		return super.getY();
	}
	
	@Override
	public int getWidth() {
		if (panel != null) {
			return (int) panel.toRelativeSize(width);
		}
		return (int) width;
	}
	
	@Override
	public int getHeight() {
		if (panel != null) {
			return (int) panel.toRelativeSize(height);
		}
		return (int) height;
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
	public void setCenter(Point2D center) {
		this.center.setLocation(center);
	}
	public void setCenter(double x, double y) {
		center.setLocation(x, y);
	}
	
	/**
	 * double 値を設定する setSize(double,double) を使ってください
	 */
	@Override @Deprecated
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		super.setSize(width, height);
	}
	/**
	 * double 値を設定する setSize(double,double) を使ってください
	 */
	@Override
	public void setSize(Dimension d) {
		this.width = d.getWidth();
		this.height = d.getHeight();
		super.setSize(d);
	}
	/**
	 * 幅と高さを設定する
	 * @param width
	 * @param height
	 */
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
		super.setSize((int) width, (int) height);
	}

	@Override
	public boolean contains(int x, int y) {
		if (panel == null) {
			return super.contains(x, y);
		}
		return contains(panel.toAbsoluteX(x) - center.x, panel.toAbsoluteY(y) - center.y);
	}
	
	public boolean contains(double x, double y) {
		return (-width/2.0 <= x && x < width/2.0) && (-height/2.0 <= y && y < height/2.0);
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
