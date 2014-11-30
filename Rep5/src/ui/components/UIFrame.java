package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import Frame.AIFrame;
import Frame.AISlot;

public class UIFrame extends MapComponent {
	private AIFrame frame;
	// あと何世代上に移動できるか
	private int up;
	// あと何世代下に移動できるか
	private int down;

	// private int groupnumber;

	Color color = Color.blue;

	// AIFrame のAISlotと UISlot との対応
	protected HashMap<AISlot, UISlot> slotMap = new HashMap<AISlot, UISlot>();
	protected Map<String, AISlot> mSlots = new HashMap<String, AISlot>();
	protected Map<String, String> leankers = new HashMap<String, String>();

	/**
	 * AIFrameSystem のフレームを受け取って初期化するコンストラクタ
	 * 
	 * @param frame
	 */
	public UIFrame(AIFrame frame, int up, int down) {
		this.frame = frame;
		this.up = up;
		this.down = down;
		// int len = frame.get_name().length();
		// int hight = frame.get_Slot_size();
		setSize(50, 50);
	}

	/*
	 * public void set_groupnumber(int x){ groupnumber = x; }
	 * 
	 * public int get_groupnumber(){ return groupnumber; }
	 */

	public int getUp() {
		return up;
	}

	public int getDown() {
		return down;
	}

	/**
	 * AIFrameSystem のフレームを返す
	 * 
	 * @return
	 */
	public AIFrame getFrame() {
		return frame;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * {@inheritDoc} ノードを描画する
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// TODO ここに描画する部分を書く
		g.setColor(color);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.drawString(frame.getName(), 0, 20);
	}

}
