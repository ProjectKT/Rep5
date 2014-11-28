package ui.components;

import java.awt.Component;
import java.util.HashMap;

import Frame.*;

public class AIFramePanel2 extends MapPanel {
	// AIFrameSystem のフレームと UIFrame との対応
	protected HashMap<AIFrame,UIFrame> frameMap = new HashMap<AIFrame,UIFrame>();
	// 現在のパネル中心の UINode
	protected UIFrame centerFrame;
	
	public AIFramePanel2() {
		super(new AIFrameLayout2());
	}

	/**
	 * コンポーネントを追加する
	 * UIFrame であればすでに追加されていない場合のみ追加する
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