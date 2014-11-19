package ui.components;

import java.awt.Component;

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
	 * @param node
	 */
	private void layoutUINode(String name, UINode node) {
		
	}
	
	/**
	 * UIArrow をレイアウトする
	 * @param name
	 * @param arrow
	 */
	private void layoutUIArrow(String name, UIArrow arrow) {
		
	}

}
