package ui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import SemanticNet.Link;
import SemanticNet.Node;

public class SemanticNetLayout extends MapLayout {
	
	@Override
	void setMapPanel(MapPanel mapPanel) {
		if (!(mapPanel instanceof SemanticNetPanel)) {
			throw new ClassCastException("mapPanel must be "+SemanticNetPanel.class.getSimpleName());
		}
		super.setMapPanel(mapPanel);
	}
	
	/**
	 * この LayoutManager が取り付けられている先の SemanticNetPanel を返すメソッド
	 */
	@Override
	protected SemanticNetPanel getMapPanel() {
		return (SemanticNetPanel) super.getMapPanel();
	}


	@Override
	public void addLayoutComponent(String name, Component comp) {
		System.out.println("addLayoutComponent: "+comp);
		if (comp instanceof UINode) {
			getMapPanel().nodeMap.put(((UINode) comp).getNode(), (UINode) comp);
			layoutUINode(name, (UINode) comp);
		} else if (comp instanceof UILink) {
			layoutUILink(name, (UILink) comp);
		}
	}
	
	@Override
	public void removeLayoutComponent(Component comp) {
		System.out.println("removeLayoutComponent");
		if (comp instanceof UINode) {
			getMapPanel().nodeMap.remove(((UINode) comp).getNode());
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
	 * UINode をレイアウトする
	 * @param name
	 * @param comp
	 */
	private void layoutUINode(String name, UINode comp) {
		Node node = comp.getNode();
		
		// TODO ここにレイアウトするコードを書く
		if (comp == getMapPanel().centerNode) {
			// センターノードならセンターに描く
			comp.setCenter(getMapPanel().getCenter());
		} else {
			// センターノード以外のノード
			
			// ノードから繋がっているリンクを列挙する
			ArrayList<Link> depLinks = node.getDepartFromMeLinks();
			for (Link link : depLinks) {
				UINode head = getMapPanel().nodeMap.get(link.getHead());
				
				// リンク先が MapPanel に入っていたら
				if (head != null) {
					ArrayList<Link> links = getConnectedLinks(head.getNode());
					int size = links.size();
					int index = links.indexOf(link);
					if (0 <= index) {
						double theta = 360 / (double)size * (double)index;
						double r = 100 + (double) index * 50;
						double dx = r*Math.cos(Math.toRadians(theta));
						double dy = r*Math.sin(Math.toRadians(theta));
						Point2D base = head.getCenter();
						comp.setCenter(base.getX() + dx, base.getY() + dy);
						
						getMapPanel().add(new UILink(link, comp, head));
						break;
					}
				}
			}
			
			// ノードに繋がっているリンクを列挙する
			ArrayList<Link> arrLinks = node.getArriveAtMeLinks();
			for (Link link : arrLinks) {
				UINode tail = getMapPanel().nodeMap.get(link.getTail());
				
				// リンク元が MapPanel に入っていたら
				if (tail != null) {
					ArrayList<Link> links = getConnectedLinks(tail.getNode());
					int size = links.size();
					int index = links.indexOf(link);
					if (0 <= index) {
						double theta = 360 / (double)size * (double)index;
						double r = 100 + (double) index * 50;
						double dx = r*Math.cos(Math.toRadians(theta));
						double dy = r*Math.sin(Math.toRadians(theta));
						Point2D base = tail.getCenter();
						comp.setCenter(base.getX() + dx, base.getY() + dy);
						
						getMapPanel().add(new UILink(link, tail, comp));
						break;
					}
				}
			}
		}
	}
	
	/**
	 * UIArrow をレイアウトする
	 * @param name
	 * @param comp
	 */
	private void layoutUILink(String name, UILink comp) {
		Link link = comp.getLink();
		
		// TODO ここにレイアウトするコードを書く
		Node head = link.getHead();
		Node tail = link.getTail();
		
	}

	/**
	 * 繋がってる全部のリンクを返す
	 * @param node
	 * @return
	 */
	private ArrayList<Link> getConnectedLinks(Node node) {
		ArrayList<Link> links = new ArrayList<Link>(node.getDepartFromMeLinks());
		links.addAll(node.getArriveAtMeLinks());
		return links;
	}
}
