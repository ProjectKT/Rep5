package ui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import Frame.*;

public class AIFrameLayout extends MapLayout {

	@Override
	void setMapPanel(MapPanel mapPanel) {
		if (!(mapPanel instanceof SemanticNetPanel)) {
			throw new ClassCastException("mapPanel must be "+SemanticNetPanel.class.getSimpleName());
		}
		super.setMapPanel(mapPanel);
	}
	
	/**
	 * この LayoutManager が取り付けられている先の AIFramePanel を返すメソッド
	 */
	@Override
	protected AIFramePanel getMapPanel() {
		return (AIFramePanel) super.getMapPanel();
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		System.out.println("addLayoutComponent");
		if (comp instanceof UINode) {
			getMapPanel().frameMap.put(((UIFrame) comp).getFrame(), (UIFrame) comp);
			layoutUIFrame(name, (UIFrame) comp);
		}

	}

	@Override
	public void removeLayoutComponent(Component comp) {
		System.out.println("removeLayoutComponent");
		if (comp instanceof UIFrame) {
			getMapPanel().frameMap.remove(((UIFrame) comp).getFrame());
		}
	}
	
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		System.out.println("preferredLayoutSize");
		return super.preferredLayoutSize(parent);
	}
	
	@Override
	public void layoutContainer(Container parent) {
		System.out.println("layoutContainer");
		super.layoutContainer(parent);
	}
	
	/**
	 * UIFrame をレイアウトする
	 * @param name
	 * @param comp
	 */
	private void layoutUIFrame(String name, UIFrame comp) {
		AIFrame frame = comp.getFrame();
		
		// TODO ここにレイアウトするコードを書く
		if (comp == getMapPanel().centerFrame) {
			// センターノードならセンターに描く
			comp.setCenter(getMapPanel().getCenter());
		} else {
			// センターノード以外のノード
		}
	}
	
//	/**
//	 * UIArrow をレイアウトする
//	 * @param name
//	 * @param comp
//	 */
//	private void layoutUILink(String name, UILink comp) {
//		Link link = comp.getLink();
//		
//		// TODO ここにレイアウトするコードを書く
//		Node head = link.getHead();
//		Node tail = link.getTail();
//		
//	}
}
