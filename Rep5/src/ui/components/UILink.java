package ui.components;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import SemanticNet.Link;

/**
 * SemanticNet の矢印を表すコンポーネント
 */
public class UILink extends UIArrow {

	private Link link;
	
	public UILink(Link link) {
		super(new Point2D.Double(0,0), new Point2D.Double(0,0));
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
