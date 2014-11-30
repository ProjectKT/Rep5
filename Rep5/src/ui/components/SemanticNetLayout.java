package ui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import javax.swing.SwingUtilities;

import semanticnet.Link;
import semanticnet.Node;
import util.SemanticNetUtils;

public class SemanticNetLayout extends MapLayout {

	private static final double E_THRESHOlD = 1.0;
	private static final double CONST = 1000;
	private static final double CONST_SPRING = 0.06;
	private static final double CONST_MIN_ATTENUATION = 0.85;
	private static final double DELTA = 1.0;

	private Object mLock = new Object();
	private Random random = new Random();
	// 各ノードの速度ベクトルとの対応
	private HashMap<UINode,LayoutParam> paramMap = new HashMap<UINode,LayoutParam>();
	// レイアウトスレッド
	private Thread nodeLayoutThread;
	// 運動エネルギーの合計
	private double e = 0;
	
	@Override
	void setMapPanel(MapPanel mapPanel) {
		if (!(mapPanel instanceof SemanticNetPanel)) {
			throw new ClassCastException("mapPanel must be "+SemanticNetPanel.class.getSimpleName());
		}
		super.setMapPanel(mapPanel);
	}
	
	public void startLayoutThread() throws InterruptedException {
		if (nodeLayoutThread != null) {
			nodeLayoutThread.interrupt();
			nodeLayoutThread.join();
		}

		nodeLayoutThread = new Thread(layoutRunnable);
		nodeLayoutThread.start();
	}
	
	public void clear() throws InterruptedException {
		nodeLayoutThread.interrupt();
		nodeLayoutThread.join();
		paramMap.clear();
		e = 0;
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

	/**
	 * コンテナをレイアウトする
	 * コンテナ (MapPanel) の大きさが変えられたとか
	 */
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
		System.out.println("layoutUINode "+comp.getNode());

		synchronized(mLock) {
			// LayoutParams を作って map に追加
			LayoutParam lp = new LayoutParam();
			paramMap.put(comp, lp);
			// この時点で今までに MapPanel に追加された UINode はすべて velocityMap に入っている

			final int n = paramMap.size();
			final double r = n * 10;
			final double t = (Math.PI / 6.0) * (double) n;
			
			// ノードの初期位置を設定. 2 つのノードがまったく同じ位置におかれないようにする。
			lp.x = r * Math.cos(t);
			lp.y = r * Math.sin(t);
			comp.setCenter(lp.x, lp.y);
			getMapPanel().repaint();
		}
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
	 * 別スレッドで UINode の位置を更新するための Runnable
	 */
	private Runnable layoutRunnable = new Runnable() {
		@Override
		public void run() {
			final long SLEEP = (long) (1000.0/60.0);
			
			try {
				for (;;) {
					updateUINodes();
					Thread.sleep(SLEEP);
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							getMapPanel().repaint();
						}
					});
				}
			} catch (InterruptedException | InvocationTargetException e) { }
			
			synchronized(mLock) {
				nodeLayoutThread = null;
			}
		}
	};
	
	/**
	 * UINode の位置を更新する
	 * ここ参照
	 * @see http://blog.ivank.net/force-based-graph-drawing-in-as3.html
	 */
	private void updateUINodes() {
		final double attenuation = Math.min(CONST_MIN_ATTENUATION, 1.0/e);
		
		synchronized(mLock) {
			Set<UINode> uiNodeSet = paramMap.keySet();
			// 原点に戻ろうとする力の係数
			final double originFactor = uiNodeSet.size() * 0.001;
			// 運動エネルギーの合計 := 0 // すべての粒子について、運動エネルギーの合計を計算する。
			e = 0;
			
			for (UINode n1 : uiNodeSet) {
				LayoutParam lp1 = paramMap.get(n1);
				
				// ノードの位置とノードにつながっているリンクを取得
				Point2D p1 = n1.getCenter();
				ArrayList<Link> links = SemanticNetUtils.getConnectedLinks(n1.getNode());
				double w = 1.0;
				
				// 力 := (0, 0) // この粒子について作用するすべての力の合成を計算する。
				double fx = 0;
				double fy = 0;

				// 他のノードとの反発
				for (UINode n2 : uiNodeSet) {
					if (n1 == n2) {
						continue;
					}
					final Point2D p2 = n2.getCenter();
					
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
					
//					w *= 1.1;
					w += 1.0;
				}
				
				// 原点 (0,0) に戻す力
				fx -= originFactor * p1.getX();
				fy -= originFactor * p1.getY();

				// 内部摩擦が無ければ粒子は停止しないので、振動の減衰を計算する。
				// ノード１の速度 := (ノード1の速度 +　微小時間 * 力 / ノード1の質量) * 減衰定数
				lp1.vx = (lp1.vx + DELTA * fx / w) * attenuation;
				lp1.vy = (lp1.vy + DELTA * fy / w) * attenuation;

				// ノード１の位置 := ノード1の位置 + 微小時間 * ノード1の速度
				lp1.x = lp1.x + DELTA * lp1.vx;
				lp1.y = lp1.y + DELTA * lp1.vy;
				
				// 運動エネルギーの合計 := 運動エネルギーの合計 + ノード1の質量 * ノード1の速度 ^ 2
				e = e + w * (lp1.vx*lp1.vx + lp1.vy*lp1.vy);
			}
			
			for (UINode node : uiNodeSet) {
				LayoutParam lp = paramMap.get(node);
				if (node.isDragged) {
					lp.x = node.center.getX();
					lp.y = node.center.getY();
				} else {
					node.setCenter(lp.x, lp.y);
				}
			}
		}
	}
	
	private class LayoutParam {
		double x = 0;
		double y = 0;
		double vx = 0;
		double vy = 0;
	}
}
