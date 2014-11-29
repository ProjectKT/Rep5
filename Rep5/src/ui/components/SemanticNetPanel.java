package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import SemanticNet.Link;
import SemanticNet.Node;

public class SemanticNetPanel extends MapPanel {

	/** SemanticNet のノードと UINode との対応 */
	protected HashMap<Node,UINode> nodeMap = new HashMap<Node,UINode>();
	/** ノード間のリンク */
	protected ArrayList<Link> links = new ArrayList<Link>();
	/** 現在のパネル中心の UINode */
	protected UINode centerNode;
	/** 現在選択されているノード */
	protected ArrayList<UINode> selectedNodes = new ArrayList<UINode>();

	/** コールバック */
	public interface Callbacks {
		public void onSelectUINodes(UINode[] uiNodes);
	}
	private static final Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onSelectUINodes(UINode[] uiNodes) { }
	};
	protected Callbacks callbacks = sDummyCallbacks;
	
	/** 矢印の描画に一時的に使う変数 */
	private interface Arrow {
		interface Default {
			// 矢印の形を作る 2 点の座標
			final Point2D.Double p1 = new Point2D.Double(-10.0, -5.0);
			final Point2D.Double p2 = new Point2D.Double(-10.0,  5.0);
		}
		// 直線の始点(pf)と終点(pt)
		final Point pf = new Point();
		final Point pt = new Point();
		// 変換後の矢印の先端の座標
		final Point2D.Double p1 = new Point2D.Double();
		final Point2D.Double p2 = new Point2D.Double();
	}
	
	/** 色の設定 */
	private interface ColorSetting {
		Color link = Color.red;
		Color inheritedLink = Color.gray;
		Color linkLabel = Color.darkGray;
		Color inheritedLinkLabel = Color.gray;
	}
	
	public SemanticNetPanel() {
		super(new SemanticNetLayout());
		addMouseListener(panelMouseAdapter);
	}
	
	public void setCallbacks(Callbacks callbacks) {
		this.callbacks = callbacks;
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
		synchronized(links) {
			if (!links.contains(link)) {
				links.add(link);
			}
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
		synchronized(links) {
			// inheritance リンクを初めに描画
			for (Link link : links) {
				UINode head = nodeMap.get(link.getHead());
				UINode tail = nodeMap.get(link.getTail());
				if (head != null && tail != null && link.getInheritance()) {
					// 矢印を描く
					g.setColor(ColorSetting.inheritedLink);
					drawArrow(g, head, tail);
					
					// ラベルを描く
					g.setColor(ColorSetting.inheritedLinkLabel);
					g.drawString(link.getLabel(), (Arrow.pt.x+Arrow.pf.x)/2, (Arrow.pt.y+Arrow.pf.y)/2);
				}
			}
			
			// 明示的なリンクを上から描画
			for (Link link : links) {
				UINode head = nodeMap.get(link.getHead());
				UINode tail = nodeMap.get(link.getTail());
				if (head != null && tail != null && !link.getInheritance()) {
					// 矢印を描く
					g.setColor(ColorSetting.link);
					drawArrow(g, head, tail);
					
					// ラベルを描く
					g.setColor(ColorSetting.linkLabel);
					g.drawString(link.getLabel(), (Arrow.pt.x+Arrow.pf.x)/2, (Arrow.pt.y+Arrow.pf.y)/2);
				}
			}
		}
	}
	
	/**
	 * 矢印を描く
	 * @param g
	 * @param head 矢印の先端
	 * @param tail 矢印の出元
	 */
	private void drawArrow(Graphics g, UINode head, UINode tail) {	
		// 各ノードの幅,高さの半分を求める
		final int hhWidth = head.getWidth()/2;
		final int hhHeight = head.getHeight()/2;
		final int htWidth = tail.getWidth()/2;
		final int htHeight = tail.getHeight()/2;
		
		// from, to はそれぞれ head, tail の中心の座標
		int fromX = tail.getX() + htWidth;
		int fromY = tail.getY() + htHeight;
		int toX = head.getX() + hhWidth;
		int toY = head.getY() + hhHeight;
		
		// ベクトルの長さ
		final double len = head.center.distance(tail.center);
		// cos, sin
		final double cos = (double) (toX-fromX) / len;
		final double sin = (double) (toY-fromY) / len;
		
		// 枠外に線を引くよう調整
		if (fromX != toX && fromY != toY) {
			final int coeffX = (toX < fromX) ? (-1) : 1;
			final int coeffY = (toY < fromY) ? (-1) : 1;
			// 単位ベクトル [1 0] となす角度の tan
			final double tan = sin / cos;
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

		Arrow.pf.x = fromX;
		Arrow.pf.y = fromY;
		Arrow.pt.x = toX;
		Arrow.pt.y = toY;
		
		// 矢印の形を作る 2 点の座標を計算
		Arrow.p1.x = Arrow.Default.p1.x * cos - Arrow.Default.p1.y * sin + toX;
		Arrow.p1.y = Arrow.Default.p1.x * sin + Arrow.Default.p1.y * cos + toY;
		Arrow.p2.x = Arrow.Default.p2.x * cos - Arrow.Default.p2.y * sin + toX;
		Arrow.p2.y = Arrow.Default.p2.x * sin + Arrow.Default.p2.y * cos + toY;
		
		// 線を描く
		g.drawLine(fromX, fromY, toX, toY);
		g.drawLine((int) Arrow.p1.x, (int) Arrow.p1.y, toX, toY);
		g.drawLine((int) Arrow.p2.x, (int) Arrow.p2.y, toX, toY);
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
	 * SemanticNetPanel のマウスアダプタ
	 */
	private MouseAdapter panelMouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!e.isControlDown() && !e.isMetaDown() && !e.isShiftDown()) {
				for (Iterator<UINode> it = selectedNodes.iterator(); it.hasNext();) {
					it.next().isSelected = false;
					it.remove();
				}
			}
		}
	};
	
	/**
	 * UINode 用のマウスアダプタ
	 */
	private MouseAdapter uiNodeMouseAdapter = new MouseAdapter() {
		private Point prevPoint;
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!e.isControlDown() && !e.isMetaDown() && !e.isShiftDown()) {
				for (Iterator<UINode> it = selectedNodes.iterator(); it.hasNext();) {
					it.next().isSelected = false;
					it.remove();
				}
			}
			if (e.getComponent() instanceof UINode) {
				((UINode) e.getComponent()).isSelected = true;
				selectedNodes.add((UINode) e.getComponent());
				callbacks.onSelectUINodes(selectedNodes.toArray(new UINode[selectedNodes.size()]));
			}
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
		public void mouseDragged(MouseEvent e) {
			if (e.getComponent() instanceof UINode) {
				final Point point = e.getPoint();
				final double x = ((UINode) e.getComponent()).center.x + ((point.x - prevPoint.x) * zoom);
				final double y = ((UINode) e.getComponent()).center.y + ((point.y - prevPoint.y) * zoom);
				((UINode) e.getComponent()).setCenter(x,y);
				prevPoint = point;
			}
		}
	};
}
