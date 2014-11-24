package ui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import SemanticNet.Link;
import SemanticNet.Node;

public class SemanticNetLayout extends MapLayout {
	
	private Object mLock = new Object();
	private Random random = new Random();
	// 各ノードの速度ベクトルとの対応
	private HashMap<UINode,Velocity> velocityMap = new HashMap<UINode,Velocity>();
	// レイアウトスレッド
	private Thread nodeLayoutThread;
	
	@Override
	void setMapPanel(MapPanel mapPanel) {
		if (!(mapPanel instanceof SemanticNetPanel)) {
			throw new ClassCastException("mapPanel must be "+SemanticNetPanel.class.getSimpleName());
		}
		super.setMapPanel(mapPanel);
		
		if (nodeLayoutThread == null) {
			nodeLayoutThread = new Thread(layoutNodes);
			nodeLayoutThread.start();
		}
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
		System.out.println("addLayoutComponent");
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

//	/**
//	 * UINode をレイアウトする
//	 * @param name
//	 * @param comp
//	 */
//	private void layoutUINode(String name, UINode comp) {
//		Node node = comp.getNode();
//		
//		// TODO ここにレイアウトするコードを書く
//		if (comp == getMapPanel().centerNode) {
//			// センターノードならセンターに描く
//			comp.setCenter(getMapPanel().getCenter());
//		} else {
//			// センターノード以外のノード
//			
//			//変更 ky
//			//
//			//
//			
//			
//			
//			// ノードから繋がっているリンクを列挙する
//			ArrayList<Link> depLinks = node.getDepartFromMeLinks();
//			
//			
//			for (Link link : depLinks) {
//				UINode head = getMapPanel().nodeMap.get(link.getHead());
//				
//				//
//				//ヘッドノードから出ているリンク先の、すでに配置されているノードを返す
//				// 
//				
//				//
//				//上で求めたノードからヘッドノードの配置を決める(もしくはUINODEにあるthetaを利用)
//				//
//				
//				//
//				//  ヘッドノードと他のノードの配置の仕方を別にする
//				//　UINodeのtFromCを利用して、　センターとは反対側に新しいノードを作るようにしたい
//				
//				// リンク先が MapPanel に入っていたら
//				if (head != null) {
//					ArrayList<Link> links = getConnectedLinks(head.getNode());
//					int size = links.size();
//					int index = links.indexOf(link);
//					if (0 <= index) {
//						double theta = 360 / (double)size * (double)index;
//						double r = 200 + (double) index * 60;
//						double dx = r*Math.cos(Math.toRadians(theta));
//						double dy = r*Math.sin(Math.toRadians(theta));
//						Point2D base = head.getCenter();
//						comp.setCenter(base.getX() + dx, base.getY() + dy);
//						
//						if(link.getInheritance() == false){
//							getMapPanel().add(new UILink(link, comp, head));
//						}
//						//break;
//					}
//				}
//				
//				
//				
//			}
//			
//			
//			
//			
//			// ノードに繋がっているリンクを列挙する
//			ArrayList<Link> arrLinks = node.getArriveAtMeLinks();
//			for (Link link : arrLinks) {
//				UINode tail = getMapPanel().nodeMap.get(link.getTail());
//				
//				// リンク元が MapPanel に入っていたら
//				if (tail != null) {
//					ArrayList<Link> links = getConnectedLinks(tail.getNode());
//					int size = links.size();
//					int index = links.indexOf(link);
//					if (0 <= index) {
//						double theta = 360 / (double)size * (double)index;
//						double r = 200 + (double) index * 60;
//						double dx = r*Math.cos(Math.toRadians(theta));
//						double dy = r*Math.sin(Math.toRadians(theta));
//						Point2D base = tail.getCenter();
//						comp.setCenter(base.getX() + dx, base.getY() + dy);
//						
//						getMapPanel().add(new UILink(link, tail, comp));
//						//break;
//					}
//				}
//			}
//		}
//	}
	private void layoutUINode(String name, UINode comp) {
		System.out.println("layoutUINode "+comp.getNode());

		synchronized(mLock) {
			// 速度ベクトルを作って map に追加
			velocityMap.put(comp, new Velocity(0,0));
			// この時点で今までに MapPanel に追加された UINode はすべて velocityMap に入っている

			// ノードの位置を、(乱数, 乱数) にする。 // 2 つのノードがまったく同じ位置におかれないようにする。
			comp.setCenter(random.nextDouble() * 100, random.nextDouble() * 100);
		}
		
		ArrayList<Link> connectedLinks = getConnectedLinks(comp.getNode());
		for (Link link : connectedLinks) {
			Node opposite = (comp.getNode() == link.getTail()) ? link.getTail() : link.getHead();
			UINode uiNode = getUINode(opposite);
			if (uiNode != null) {
				getMapPanel().addLink(link);
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
	
	/**
	 * Node に対応する UINode を返す
	 * @param node
	 * @return まだ MapPanel に追加されていない場合は null
	 */
	private UINode getUINode(Node node) {
		return getMapPanel().nodeMap.get(node);
	}
	
	/**
	 * ここ参照
	 * @see http://blog.ivank.net/force-based-graph-drawing-in-as3.html
	 */
	private Runnable layoutNodes = new Runnable() {
		@Override
		public void run() {
			final double E_THRESHOlD = 1.0;
			final double CONST = 200;
			final double CONST_SPRING = 0.06;
			final double CONST_ATTENUATION = 0.85;
			final double DELTA = 0.01;
			final long SLEEP = (long) (DELTA*100);
			
			// 運動エネルギーの合計
			double e = 0;
			Iterator<UINode> it1, it2;
			
			do {
				// 運動エネルギーの合計 := 0 // すべての粒子について、運動エネルギーの合計を計算する。
				e = 0;
		
				synchronized(mLock) {
					for (it1 = velocityMap.keySet().iterator(); it1.hasNext();) {
						UINode n1 = it1.next();
						Point2D p1 = n1.getCenter();
//						System.out.println("n1="+n1.hashCode()+" p1="+p1);
						
						// 力 := (0, 0) // この粒子について作用するすべての力の合成を計算する。
						double fx = 0;
						double fy = 0;
	
						for (it2 = velocityMap.keySet().iterator(); it2.hasNext();) {
							UINode n2 = it2.next();
							if (n1 == n2) {
								continue;
							}
							Point2D p2 = n2.getCenter();
//							System.out.println("  n2="+n2.hashCode()+" p2="+p2);
							
							// 距離の二乗
							final double sqD = p1.distanceSq(p2);
							
							// 力 := 力 + 定数 / 距離（ノード1, ノード2) ^ 2  // クーロン力
							fx += CONST * (p1.getX()-p2.getX()) / sqD;
							fy += CONST * (p1.getY()-p2.getY()) / sqD;
						}
	
						// ノード n1.getNode() につながっているリンクを取得
						ArrayList<Link> links = getConnectedLinks(n1.getNode());
						double w = 1.0;
						
						for (Link link : links) {
							final UINode head = getUINode(link.getHead());
							final UINode tail = getUINode(link.getTail());
							if (head == null || tail == null) {
								continue;
							}
							final UINode n2 = (n1 == tail) ? head : tail;
							final Point2D p2 = n2.getCenter();
							
							// 力 := 力 + バネ定数 * (距離 (ノード1, ノード2) - バネの自然長)  // フックの法則による力
							fx += CONST_SPRING * (p2.getX() - p1.getX());
							fy += CONST_SPRING * (p2.getY() - p1.getY());
							
							w *= 1.1;
						}
	
						// 内部摩擦が無ければ粒子は停止しないので、振動の減衰を計算する。
						// ノード１の速度 := (ノード1の速度 +　微小時間 * 力 / ノード1の質量) * 減衰定数
						Velocity v1 = velocityMap.get(n1);
						v1.vx += (DELTA * fx / w) * CONST_ATTENUATION;
						v1.vy += (DELTA * fy / w) * CONST_ATTENUATION;
						
						// ノード１の位置 := ノード1の位置 + 微小時間 * ノード1の速度
						n1.setCenter(
								p1.getX() + DELTA * v1.vx,
								p1.getY() + DELTA * v1.vy
						);
						
						// 運動エネルギーの合計 := 運動エネルギーの合計 + ノード1の質量 * ノード1の速度 ^ 2
						e = e + w * (v1.vx*v1.vx + v1.vy*v1.vy);
					}
					System.out.println("e = "+e);
				}

				try {
					System.out.println("sleep");
					Thread.sleep(SLEEP);
					System.out.println("slept");
					getMapPanel().repaint();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					break;
				}
			} while (true || E_THRESHOlD < e);
			
			synchronized(mLock) {
				nodeLayoutThread = null;
			}
		}
	};
	
	private class Velocity {
		double vx;
		double vy;
		
		public Velocity(double vx, double vy) {
			this.vx = vx;
			this.vy = vy;
		}
	}
}
