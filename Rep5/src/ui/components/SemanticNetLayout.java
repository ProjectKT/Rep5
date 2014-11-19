package ui.components;

import java.awt.Component;
import java.util.HashMap;

import SemanticNet.Link;
import SemanticNet.Node;

public class SemanticNetLayout extends MapLayout {
	
	private HashMap<Node,UINode> nodeMap = new HashMap<Node,UINode>();
	
	public SemanticNetLayout(MapPanel mapPanel) {
		super(mapPanel);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		if (comp instanceof UINode) {
			nodeMap.put(((UINode) comp).getNode(), (UINode) comp);
			layoutUINode(name, (UINode) comp);
		} else if (comp instanceof UILink) {
			layoutUILink(name, (UILink) comp);
		}
	}
	
	@Override
	public void removeLayoutComponent(Component comp) {
		if (comp instanceof UINode) {
			nodeMap.remove(((UINode) comp).getNode());
		}
	}

	/**
	 * UINode をレイアウトする
	 * @param name
	 * @param comp
	 */
	private void layoutUINode(String name, UINode comp) {
		Node node = comp.getNode();
		
		// TODO ここにレイアウトするコードを書く
		
	}
	
	/**
	 * UIArrow をレイアウトする
	 * @param name
	 * @param comp
	 */
	private void layoutUILink(String name, UILink comp) {
		Link link = comp.getLink();
		
		// TODO ここにレイアウトするコードを書く
	}

}