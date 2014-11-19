package ui.components;

import java.awt.Component;
import java.util.HashMap;

import SemanticNet.Link;
import SemanticNet.Node;

public class SemanticNetPanel extends MapPanel {

	// SemanticNet のノードと UINode との対応
	protected HashMap<Node,UINode> nodeMap = new HashMap<Node,UINode>();
	// SemanticNet のノードと UINode との対応
	protected HashMap<Link,UILink> linkMap = new HashMap<Link,UILink>();
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
		} else if (comp instanceof UILink) {
			UILink uiLink = linkMap.get(((UILink) comp).getLink());
			if (uiLink != null) {
				return uiLink;
			}
			linkMap.put(((UILink) comp).getLink(), (UILink) comp);
		}
		return super.add(comp);
	}

	
}
