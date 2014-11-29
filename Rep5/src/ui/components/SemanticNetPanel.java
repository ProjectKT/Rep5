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
				final int hhWidth = head.getWidth()/2;
				final int hhHeight = head.getHeight()/2;
				final int htWidth = tail.getWidth()/2;
				final int htHeight = tail.getHeight()/2;
				
				// from, to はそれぞれ head, tail の中心の座標
				int fromX = tail.getX() + htWidth;
				int fromY = tail.getY() + htHeight;
				int toX = head.getX() + hhWidth;
				int toY = head.getY() + hhHeight;
				
				// 枠外に線を引くよう調整
				if (fromX != toX && fromY != toY) {
					final int coeffX = (toX < fromX) ? (-1) : 1;
					final int coeffY = (toY < fromY) ? (-1) : 1;
					// 単位ベクトル [1 0] となす角度の tan
					final double tan = (double) (toY-fromY) / (double) (toX-fromX);
					// 単位ベクトル [0 1] となす角度の tan = 1 / tan
					final double itan = (tan == 0) ? 0 : -(1.0/tan);
	
					// head, tail の中心の座標から枠外の座標を計算
					fromX += absMin(coeffX * htWidth, (int)(-coeffY * htHeight*itan));
					fromY += absMin(coeffY * htHeight, (int)(coeffX * htWidth*tan));
					toX -= absMin(coeffX * hhWidth, (int)(-coeffY * hhHeight*itan));
					toY -= absMin(coeffY * hhHeight, (int)(coeffX * hhWidth*tan));
				} else if (fromX == toX) {
					// tan が求まらないので適宜調整
					final int coeff = (toY < fromY) ? (-1) : 1;
					fromY += coeff * htHeight;
					toY -= coeff * hhHeight;
				} else {
					// tan が 0 になるので適宜調整
					final int coeff = (toX < fromX) ? (-1) : 1;
					fromX += coeff * htWidth;
					toX -= coeff * hhWidth;
				}
				
				// TODO 矢印を描く
				
				g.setColor(Color.red);
				g.drawLine(fromX, fromY, toX, toY);
			}
		}
	}
	
	/**
	 * 絶対値が小さい方を返す
	 * @param i1
	 * @param i2
	 * @return
	 */
	private int absMin(int i1, int i2) {
		return (Math.abs(i1) <= Math.abs(i2)) ? i1 : i2;
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
