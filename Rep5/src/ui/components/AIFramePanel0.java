package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import Frame.AIFrame;
import Frame.AIFrameSystem;

public class AIFramePanel0 extends MapPanel {
	
	private static interface ColorPalette {
		Color skyblue = new Color(0xffadd8e6);
	}

	// AIFrameSystem
	protected AIFrameSystem aIFrameSystem;
	// AIFrameSystem のフレームと UIFrame との対応
	protected HashMap<AIFrame, UIFrame> frameMap = new HashMap<AIFrame, UIFrame>();
	// 現在のパネル中心の UINode
	// 名前を指定されているフレームの予定
	protected UIFrame centerFrame;

	private ArrayList<String> demonList = new ArrayList<String>();
	private ArrayList<AIFrame> openList = new ArrayList<AIFrame>();
	private ArrayList<AIFrame> closedList = new ArrayList<AIFrame>();

	public AIFramePanel0(AIFrameSystem aiFrameSystem) {
		super(new AIFrameLayout0());
		this.aIFrameSystem = aiFrameSystem;
	}
	
	/**
	 * 中心のフレームを指定して GUI を描画する
	 * @param name
	 */
	public void showFramesFor(String centerFrameName) {
		clear();
		
		AIFrame frame = aIFrameSystem.getFrame(centerFrameName);
		centerFrame = addFrame(frame, 2, 2);
		centerFrame.isCenter = true;
		int up;
		int down;
		
		openList.add(frame);
		while (openList.size() > 0) {
			frame = openList.remove(0);
			closedList.add(frame);

			up = frameMap.get(frame).getUp();
			down = frameMap.get(frame).getDown();
			if (up > 0) {
				searchUp(frame, up - 1, down);
			}
			if (down > 0) {
				searchDown(frame, up, down - 1);
			}
		}
		for (int i = 0; i < closedList.size(); i++) {
			System.out.println(closedList.get(i).getName());
			up = frameMap.get(closedList.get(i)).getUp();
			down = frameMap.get(closedList.get(i)).getDown();
			System.out.println("up:" + up + ", down:" + down);
		}
		
		repaint();
	}

	/**
	 * ビューに AIFrameSystem のフレームを加える
	 * 
	 * @param node
	 */
	public UIFrame addFrame(AIFrame aiFrame, int up, int down) {
		UIFrame uiFrame = frameMap.get(aiFrame);
		if (uiFrame != null) {
			return uiFrame;
		}
		
		uiFrame = new UIFrame(aiFrame, up, down);
		add(uiFrame);
		frameMap.put(aiFrame, uiFrame);
		return uiFrame;
	}
	
	/**
	 * フレームを全部消す
	 */
	public void clear() {
		for (Iterator it = frameMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			AIFrame key = (AIFrame) entry.getKey();
			UIFrame value = (UIFrame) entry.getValue();
			value.setVisible(false);
			remove(value);
		}
		if (centerFrame != null) {
			centerFrame.isCenter = false;
		}
		frameMap.clear();
		frameMap = new HashMap<AIFrame, UIFrame>();
		openList.clear();
		closedList.clear();
	}

	/**
	 * 
	 * @param frame
	 * @param up
	 * @param down
	 */
	private void searchUp(AIFrame frame, int up, int down) {
		// 自分の親が登録されているかチェック
		if (!(frame.readSlotValue(aIFrameSystem, "親", false) == null)) {
			// 自分の親の名前をとってくる
			ArrayList<String> parentlist = frame.getmVals("親");
			for (int i = 0; i < parentlist.size(); i++) {
				AIFrame parentframe = aIFrameSystem.getFrame(parentlist.get(i));
				if (!openList.contains(parentframe)
						& !closedList.contains(parentframe)) {
					openList.add(0,parentframe);
					addFrame(parentframe, up, down);
					
				}
			}
		}
	}

	/**
	 * 
	 * @param frame
	 * @param up
	 * @param down
	 */
	private void searchDown(AIFrame frame, int up, int down) {
		ArrayList<String> childlist = frame.getLeankersSlotNames("親");
		for (int i = 0; i < childlist.size(); i++) {
			AIFrame childframe = aIFrameSystem.getFrame(childlist.get(i));
			if (!openList.contains(childframe)
					& !closedList.contains(childframe)) {
				openList.add(0,childframe);
				addFrame(childframe, up, down);
			}
		}
	}
	
	/**
	 * デモン手続きで親族を見つけその色を変える
	 * @param demon
	 */
	public void highlightRelatedFrames(String demon) {
		for (int i = 0; i < demonList.size(); i++) {
			if (aIFrameSystem.getFrame(demonList.get(i)) != null) {
				AIFrame changeframe = aIFrameSystem.getFrame(demonList.get(i));
				frameMap.get(changeframe).isHighlighted = false;
			}
		}
		if (centerFrame != null) {
			if (aIFrameSystem.getFrame(centerFrame.getFrame().getName()) != null) {
				demonList = (ArrayList<String>) aIFrameSystem.readSlotValue(centerFrame.getFrame().getName(), demon);
			}
		}
		for (int i = 0; i < demonList.size(); i++) {
			if (aIFrameSystem.getFrame(demonList.get(i)) != null) {
				AIFrame changeframe = aIFrameSystem.getFrame(demonList.get(i));
				frameMap.get(changeframe).isHighlighted = true;
			}
		}
		repaint();
	}
	
	@Override
	protected void doPaintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.doPaintComponent(g);
		
		drawline(g);
	}



	private void drawline(Graphics g){
		for(int i = 0; i< closedList.size();i++){
			AIFrame frame = closedList.get(i);
			if(!(frame.readSlotValue(aIFrameSystem, "親", false) == null)){
				ArrayList<String> parents = frame.getmVals("親");
				UIFrame to = frameMap.get(frame);
				for(int j = 0; j < parents.size();j++){
					AIFrame parent = aIFrameSystem.getFrame(parents.get(j));
					UIFrame from = frameMap.get(parent);
					if(frameMap.containsKey(parent)){
						final double fromX = toRelativeX(from.center.x);
						final double fromY = toRelativeY(from.center.y);
						final double toX = toRelativeX(to.center.x);
						final double toY = toRelativeY(to.center.y);
						
						g.setColor(Color.black);
						g.drawLine((int) fromX, (int) fromY, (int) toX, (int) toY);
					}
				}
			}
		}
	}
	
}
