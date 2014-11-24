package ui.components;

import java.awt.Color;
import java.awt.Graphics;

import Frame.*;
import SemanticNet.Node;

public class UIFrame extends MapComponent {
	private AIFrame frame;
	
	/**
	 * AIFrameSystem のフレームを受け取って初期化するコンストラクタ
	 * @param frame
	 */
	public UIFrame(AIFrame frame) {
		this.frame = frame;
		int len = frame.get_name().length();
		int hight = frame.get_Slot_size();
		setSize(len*10, (hight+1)*10);
	}
	
	/**
	 * AIFrameSystem のフレームを返す
	 * @return
	 */
	public AIFrame getFrame() {
		return frame;
	}
	
	/**
	 * {@inheritDoc}
	 * ノードを描画する
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// TODO ここに描画する部分を書く
		g.setColor(Color.blue);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.drawString(frame.get_name(), 0, (getHeight()/2)-10);
	}
	
	
}
