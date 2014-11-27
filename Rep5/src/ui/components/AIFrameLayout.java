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

import Frame.AIFrame;
import Frame.AISlot;

public class AIFrameLayout extends MapLayout {

	private static final double E_THRESHOlD = 1.0;
	private static final double CONST = 1000;
	private static final double CONST_SPRING = 0.06;
	private static final double CONST_MIN_ATTENUATION = 0.85;
	private static final double DELTA = 1;
	

	private Object mLock = new Object();
	private Random random = new Random();
	// 各ノードの速度ベクトルとの対応
	private HashMap<UIFrame,LayoutParams> paramMap = new HashMap<UIFrame,LayoutParams>();
	// レイアウトスレッド
	private Thread frameLayoutThread;
	// 運動エネルギーの合計
	private double e = 1;
	
	@Override
	void setMapPanel(MapPanel mapPanel) {
		if (!(mapPanel instanceof AIFramePanel)) {
			throw new ClassCastException("mapPanel must be "+AIFramePanel.class.getSimpleName());
		}
		super.setMapPanel(mapPanel);
		
		if (frameLayoutThread == null) {
			frameLayoutThread = new Thread(layoutFrames);
			frameLayoutThread.start();
		}
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
		if (comp instanceof UIFrame) {
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
	
	
	private void layoutUIFrame(String name, UIFrame comp) {
		System.out.println("layoutUIFrame "+comp.getFrame());

		synchronized(mLock) {
			// 速度ベクトルを作って map に追加
			paramMap.put(comp, new LayoutParams());
			// この時点で今までに MapPanel に追加された UINode はすべて velocityMap に入っている

			// ノードの位置を、(乱数, 乱数) にする。 // 2 つのノードがまったく同じ位置におかれないようにする。
			comp.setCenter(random.nextDouble() * 100, random.nextDouble() * 100);
		}
		
		AIFrame aiFrame = comp.getFrame();
		for(int i = 0; i < aiFrame.get_Slot_size(); i++){
		//ArrayList<AISlot> connectedSlots = getConnectedSlots(comp.getFrame());
			
			ArrayList<String> list;
			//スロットの値をとってくる
			if((aiFrame.get_Slot_key(i) == "is-a")||(aiFrame.get_Slot_key(i) == "ako")){
				list = aiFrame.get_supers();
			}else{
				list = aiFrame.getmVals(aiFrame.get_Slot_key(i));
			}
			
			for(int j=0;j<list.size();j++){
				getUIFrame(list.get(j));
			}
			comp.getFrame
		for (AISlot slot : connectedSlots) {
			if(comp.getFrame().)
			AIFrame opposite = (comp.getFrame() == slot.getTail()) ? slot.getTail() : slot.getHead();
			UIFrame uiFrame = getUIFrame(opposite);
			if (uiFrame != null) {
				getMapPanel().addSlot(slot);
			}
		}
	}
	
	

	
	/**
	 * 繋がってる全部のリンクを返す
	 * @param node
	 * @return
	 */
	/*
	private ArrayList<AISlot> getConnectedSlots(AIFrame frame) {
		ArrayList<AISlot> slots = new ArrayList<AISlot>(frame.getDepartFromMeSlots());
		slots.addAll(frame.getArriveAtMeSlots());
		return slots;
	}
	*/
	
	/**
	 * Node に対応する UINode を返す
	 * @param node
	 * @return まだ MapPanel に追加されていない場合は null
	 */
	private UIFrame getUIFrame(AIFrame frame) {
		return getMapPanel().frameMap.get(frame);
	}
	
	/**
	 * ここ参照
	 * @see http://blog.ivank.net/force-based-graph-drawing-in-as3.html
	 */
	private Runnable layoutFrames = new Runnable() {
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
				frameLayoutThread = null;
			}
		}
	};
	
	private void updateVelocities() {
		final double attenuation = Math.min(CONST_MIN_ATTENUATION, 1.0/e);
		
		synchronized(mLock) {
			Set<UIFrame> uiFrameSet = paramMap.keySet();
			// 原点に戻ろうとする力の係数
			final double originFactor = uiFrameSet.size() * 0.001;
			// 運動エネルギーの合計 := 0 // すべての粒子について、運動エネルギーの合計を計算する。
			e = 0;
			
			for (UIFrame n1 : uiFrameSet) {
				LayoutParams lp1 = paramMap.get(n1);
				
				// ノードの位置とノードにつながっているリンクを取得
				Point2D p1 = n1.getCenter();
				ArrayList<AISlot> slots = getConnectedSlots(n1.getFrame());
				double w = 1.0;
				
				// 力 := (0, 0) // この粒子について作用するすべての力の合成を計算する。
				double fx = 0;
				double fy = 0;

				// 他のノードとの反発
				for (UIFrame n2 : uiFrameSet) {
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
				for (AISlot slot : slots) {
					final UIFrame head = getUIFrame(slot.getHead());
					final UIFrame tail = getUIFrame(slot.getTail());
					if (head == null || tail == null) {
						continue;
					}
					final UIFrame n2 = (n1 == tail) ? head : tail;
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
			
			for (UIFrame frame : uiFrameSet) {
				LayoutParams lp = paramMap.get(frame);
				
				// ノード１の位置 := ノード1の位置 + 微小時間 * ノード1の速度
				frame.setCenter(lp.x, lp.y);
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
