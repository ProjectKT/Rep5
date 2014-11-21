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
		g.drawLine((int) from.getX(), (int) from.getY(), (int) to.getX(), (int) to.getY());
	}
	

}
