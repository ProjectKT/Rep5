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

public class AIFrameLayout2 extends MapLayout {

	@Override
	void setMapPanel(MapPanel mapPanel) {
		if (!(mapPanel instanceof AIFramePanel)) {
			throw new ClassCastException("mapPanel must be "+AIFramePanel.class.getSimpleName());
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
	/*
	private Runnable layoutFrames = new Runnable() {
	}
	*/

	
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
