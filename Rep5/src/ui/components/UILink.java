package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import SemanticNet.Link;

/**
 * SemanticNet の矢印を表すコンポーネント
 */
public class UILink extends UIArrow {

	private Link link;
	private UINode from;
	private UINode to;
	
	public UILink(Link link, UINode from, UINode to) {
		super((Point2D) from.getCenter().clone(), (Point2D) to.getCenter().clone());
		this.link = link;
	}
	
	public Link getLink() {
		return link;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// TODO 矢印の真ん中にラベルを貼る?
	}
}
