package ui.components;

import java.awt.geom.Point2D;

import Frame.AISlot;

/**AIFrameSystemのスロットを表すビュー
 * 
 */
public class UISlot extends MapComponent {
	private AISlot slot;
	
	private Point2D from;
	private Point2D to;
	
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

}
