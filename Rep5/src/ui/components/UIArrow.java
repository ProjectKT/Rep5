package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import SemanticNet.Link;

public class UIArrow extends MapComponent {

	private Point2D from;
	private Point2D to;
	
	/**
	 * SemanticNet のリンクを受け取って初期化するコンストラクタ
	 * @param link
	 */
	public UIArrow(Point2D from, Point2D to) {
		this.from = from;
		this.to = to;
		double width = Math.max(1, Math.abs(from.getX()-to.getX()));
		double height = Math.max(1, Math.abs(from.getY()-to.getY()));
		setSize(width, height);
		setCenter((from.getX()+to.getX())/2.0, (from.getY()+to.getY())/2.0);
	}

	public Point2D getFrom() {
		return from;
	}
	public void setFrom(Point2D from) {
		this.from = from;
	}
	public Point2D getTo() {
		return to;
	}
	public void setTo(Point2D to) {
		this.to = to;
	}

	/**
	 * {@inheritDoc}
	 * リンクを表す矢印を描画する
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// TODO ここに描画する部分を書く
		g.setColor(Color.red);
		final int width = getWidth();
		final int height = getHeight();
		if (width <= 1 || height <= 1) {
			g.fillRect(0, 0, width, height);
		} else {
			final double fx = from.getX();
			final double fy = from.getY();
			final double tx = to.getX();
			final double ty = to.getY();
			if (fx < tx) {
				if (fy < ty) {
					// +-+
					// |\| to bottom right
					// +-+
					g.drawLine(0, 0, width, height);
				} else {
					// +-+
					// |/| to top right
					// +-+
					g.drawLine(0, height, width, 0);
				}
			} else {
				if (fy < ty) {
					// +-+
					// |/| to bottom left
					// +-+
					g.drawLine(width, 0, 0, height);
				} else {
					// +-+
					// |\| to top left
					// +-+
					g.drawLine(width, height, 0, 0);
				}
			}
		}
	}
	

}
