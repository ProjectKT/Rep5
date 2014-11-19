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
		setSize((int) width, (int) height);
		if (from.getX() < to.getX()) {
			if (from.getY() < to.getY()) {
				setCenter(from.getX()+width/2, from.getY()+height/2);
			} else {
				setCenter(from.getX()+width/2, from.getY()-height/2);
			}
		} else {
			if (from.getY() < to.getY()) {
				setCenter(from.getX()-width/2, from.getY()+height/2);
			} else {
				setCenter(from.getX()-width/2, from.getY()-height/2);
			}
		}
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
		if (from.getX() < to.getX()) {
			if (from.getY() < to.getY()) {
				g.drawLine(0, 0, getWidth(), getHeight());
			} else {
				g.drawLine(0, getHeight(), getWidth(), 0);
			}
		} else {
			if (from.getY() < to.getY()) {
				g.drawLine(getWidth(), 0, 0, getHeight());
			} else {
				g.drawLine(getWidth(), getHeight(), 0, 0);
			}
		}
	}
	

}
