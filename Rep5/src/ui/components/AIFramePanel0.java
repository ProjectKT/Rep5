package ui.components;

import java.awt.Component;
import java.util.HashMap;

import Frame.AIFrame;

public class AIFramePanel0 extends MapPanel {

	public AIFramePanel0() {
		super(new AIFrameLayout0());
	}

	// AIFrameSystem のフレームと UIFrame との対応
	protected HashMap<AIFrame, UIFrame> frameMap = new HashMap<AIFrame, UIFrame>();
	// 現在のパネル中心の UINode
	// 名前を指定されているフレームの予定
	protected UIFrame centerFrame;

	/**
	 * コンポーネントを追加する UIFrame であればすでに追加されていない場合のみ追加する
	 */
	@Override
	public Component add(Component comp) {
		if (comp instanceof UIFrame) {
			UIFrame uiFrame = frameMap.get(((UIFrame) comp).getFrame());
			if (uiFrame != null) {
				return uiFrame;
			}
			frameMap.put(((UIFrame) comp).getFrame(), (UIFrame) comp);
			if (centerFrame == null) {
				centerFrame = (UIFrame) comp;
			}
		}
		return super.add(comp);
	}

}
