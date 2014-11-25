package ui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.event.SwingPropertyChangeSupport;

import SemanticNet.Link;
import SemanticNet.Node;

public class SemanticNetLayout extends MapLayout {

	private static final double E_THRESHOlD = 1.0;
	private static final double CONST = 1000;
	private static final double CONST_SPRING = 0.06;
	private static final double CONST_MIN_ATTENUATION = 0.85;
	private static final double DELTA = 1;

	private Object mLock = new Object();
	private Random random = new Random();
	// 各ノードの速度ベクトルとの対応
	private HashMap<UINode,LayoutParams> paramMap = new HashMap<UINode,LayoutParams>();
	// レイアウトスレッド
	private Thread nodeLayoutThread;
	// 運動エネルギーの合計
	private double e = 1;
	
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
			paramMap.put(comp, new LayoutParams());
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
			final long SLEEP = (long) (10);
			
			Thread repaintThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							SwingUtilities.invokeAndWait(new Runnable() {
								@Override
								public void run() {
									getMapPanel().repaint();
								}
							});
						}
					} catch (InvocationTargetException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			});

//			repaintThread.start();
			
			try {
				for (;;) {
					updateVelocities();
					Thread.sleep(SLEEP);
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							getMapPanel().repaint();
						}
					});
				}
			} catch (InterruptedException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			try {
				repaintThread.interrupt();
				repaintThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			synchronized(mLock) {
				nodeLayoutThread = null;
			}
		}
	};
	
	private void updateVelocities() {
		final double attenuation = Math.min(CONST_MIN_ATTENUATION, 1.0/e);
		
		synchronized(mLock) {
			Set<UINode> uiNodeSet = paramMap.keySet();
			// 原点に戻ろうとする力の係数
			final double originFactor = uiNodeSet.size() * 0.001;
			// 運動エネルギーの合計 := 0 // すべての粒子について、運動エネルギーの合計を計算する。
			e = 0;
			
			for (UINode n1 : uiNodeSet) {
				LayoutParams lp1 = paramMap.get(n1);
				
				// ノードの位置とノードにつながっているリンクを取得
				Point2D p1 = n1.getCenter();
				ArrayList<Link> links = getConnectedLinks(n1.getNode());
				double w = 1.0;
				
				// 力 := (0, 0) // この粒子について作用するすべての力の合成を計算する。
				double fx = 0;
				double fy = 0;

				// 他のノードとの反発
				for (UINode n2 : uiNodeSet) {
					if (n1 == n2) {
						continue;
					}
					Point2D p2 = n2.getCenter();
					
					// 距離の二乗
					final double sqD = p1.distanceSq(p2);
					
					// 力 := 力 + 定数 / 距離（ノード1, ノード2) ^ 2  // クーロン力
					fx += CONST * (p1.getX()-p2.getX()) / sqD;
					fy += CONST * (p1.getY()-p2.getY()) / sqD;
				}

				// つながっているノードと近づく力
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
				
				// 原点 (0,0) に戻す力
				fx -= originFactor * p1.getX();
				fy -= originFactor * p1.getY();

				// 内部摩擦が無ければ粒子は停止しないので、振動の減衰を計算する。
				// ノード１の速度 := (ノード1の速度 +　微小時間 * 力 / ノード1の質量) * 減衰定数
				lp1.vx = (lp1.vx + DELTA * fx / w) * attenuation;
				lp1.vy = (lp1.vy + DELTA * fy / w) * attenuation;
				
				
				lp1.x = lp1.x + DELTA * lp1.vx;
				lp1.y = lp1.y + DELTA * lp1.vy;
				
				// 運動エネルギーの合計 := 運動エネルギーの合計 + ノード1の質量 * ノード1の速度 ^ 2
				e = e + w * (lp1.vx*lp1.vx + lp1.vy*lp1.vy);
			}
			
			for (UINode node : uiNodeSet) {
				LayoutParams lp = paramMap.get(node);
				
				// ノード１の位置 := ノード1の位置 + 微小時間 * ノード1の速度
				node.setCenter(lp.x, lp.y);
			}
		}
	}
	
	private class LayoutParams {
		double x;
		double y;
		double vx;
		double vy;
		
		public LayoutParams() {
			this.x = this.y = this.vx = this.vy = 0;
		}
	}
}
