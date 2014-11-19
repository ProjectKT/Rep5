package ui.components;

import java.awt.Component;

import SemanticNet.Link;
import SemanticNet.Node;

public class SemanticNetLayout extends MapLayout {

	public SemanticNetLayout(MapPanel mapPanel) {
		super(mapPanel);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		if (comp instanceof UINode) {
			layoutUINode(name, (UINode) comp);
		} else if (comp instanceof UIArrow) {
			layoutUIArrow(name, (UIArrow) comp);
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
	private void layoutUIArrow(String name, UIArrow comp) {
		Link link = comp.getLink();
		
		// TODO ここにレイアウトするコードを書く
	}

}
