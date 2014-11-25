package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
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
	 * コンポーネントを追加する
	 * UINode であればすでに追加されていない場合のみ追加する
	 */
	@Override
	public Component add(Component comp) {
		if (comp instanceof UINode) {
			UINode uiNode = nodeMap.get(((UINode) comp).getNode());
			if (uiNode != null) {
				return uiNode;
			}
			nodeMap.put(((UINode) comp).getNode(), (UINode) comp);
			if (centerNode == null) {
				centerNode = (UINode) comp;
			}
		}
		return super.add(comp);
	}
	
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
	
}
