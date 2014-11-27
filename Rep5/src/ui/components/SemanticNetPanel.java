package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import SemanticNet.Link;
import SemanticNet.Node;

public class SemanticNetPanel extends MapPanel {

	// SemanticNet のノードと UINode との対応
	protected HashMap<Node,UINode> nodeMap = new HashMap<Node,UINode>();
	// ノード間のリンク
	protected ArrayList<Link> links = new ArrayList<Link>();
	// 現在のパネル中心の UINode
	protected UINode centerNode;
	
	public SemanticNetPanel() {
		super(new SemanticNetLayout());
	}
	
	/**
	 * SemanticNet のノードを追加する
	 * @param node
	 * @return
	 */
	public UINode addNode(Node node) {
		UINode uiNode = nodeMap.get(node);
		if (uiNode != null) {
			return uiNode;
		}
		
		uiNode = new UINode(node);
		uiNode.addMouseListener(uiNodeMouseAdapter);
		uiNode.addMouseMotionListener(uiNodeMouseAdapter);
		nodeMap.put(node, uiNode);
		if (centerNode == null) {
			centerNode = uiNode;
		}
		
		return (UINode) add(uiNode);
	}
	
	/**
	 * SemanticNet のリンクを追加する
	 * @param link
	 */
	public void addLink(Link link) {
		if (!links.contains(link)) {
			links.add(link);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Link は別途ここで描画する
		drawLinks(g);
	}

	/**
	 * UINode 間のリンクを描画する
	 */
	private void drawLinks(Graphics g) {
		g.setColor(Color.red);
		for (Link link : links) {
			UINode head = nodeMap.get(link.getHead());
			UINode tail = nodeMap.get(link.getTail());
			if (head != null && tail != null) {
				g.drawLine(head.getX() + head.getWidth()/2,
							head.getY() + head.getHeight()/2,
							tail.getX() + tail.getWidth()/2,
							tail.getY() + tail.getHeight()/2);
			}
		}
	}
	
	/**
	 * UINode 用のマウスアダプタ
	 */
	private MouseAdapter uiNodeMouseAdapter = new MouseAdapter() {
		private Point prevPoint;
		@Override
		public void mouseClicked(MouseEvent e) {
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getComponent() instanceof UINode) {
				prevPoint = e.getPoint();
				((UINode) e.getComponent()).isDragged = true;
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getComponent() instanceof UINode) {
				((UINode) e.getComponent()).isDragged = false;
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			if (e.getComponent() instanceof UINode) {
				((UINode) e.getComponent()).color = Color.blue;
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getComponent() instanceof UINode) {
				((UINode) e.getComponent()).color = Color.green;
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (e.getComponent() instanceof UINode) {
				final Point point = e.getPoint();
				final double x = ((UINode) e.getComponent()).center.getX() + ((point.x - prevPoint.x) * zoom);
				final double y = ((UINode) e.getComponent()).center.getY() + ((point.y - prevPoint.y) * zoom);
				((UINode) e.getComponent()).setCenter(x,y);
				prevPoint = point;
			}
		}
	};
}
