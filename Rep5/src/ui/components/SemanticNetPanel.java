package ui.components;

import java.awt.Component;
import java.util.HashMap;

import SemanticNet.Node;

public class SemanticNetPanel extends MapPanel {

	// SemanticNet のノードと UINode との対応
	protected HashMap<Node,UINode> nodeMap = new HashMap<Node,UINode>();
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

	
}
