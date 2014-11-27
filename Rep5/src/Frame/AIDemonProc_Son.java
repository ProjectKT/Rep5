package Frame;

import java.util.ArrayList;
import java.util.Iterator;

public class AIDemonProc_Son {

	public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
			String inSlotName, Iterator inSlotValues, Object inOpts) {

		// 条件を満たすものを入れるリスト
		ArrayList<String> relist = new ArrayList<String>();

		// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
		// klistは息子の名前のリスト
			ArrayList<String> klist = inFrame
					.get_leankers_Slot_names("親");

		// 子供の数だけループ
			for (int j = 0; j < klist.size(); j++) {

				//子供の名前から子供のフレームをとってくる
				AIFrame frame = inFrameSystem.get_Frame(klist.get(j));

				// 性別スロットの値が男なら息子
				if (frame.readSlotValue(inFrameSystem, "性別",
								false).equals("男")) {

					// 二回目以降のループ時すでに前のループで登録されてないか調べる
					if (!relist.contains(klist.get(j))) {
						// 初めての名前なら息子としてリストに入れる
						relist.add(klist.get(j));
					}
				}
			}
		// relistを返す
		return AIFrame.makeEnum(new ArrayList<String>(relist));
	}
	
}
