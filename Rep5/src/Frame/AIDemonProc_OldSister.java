package Frame;

import java.util.ArrayList;
import java.util.Iterator;

public class AIDemonProc_OldBrother extends AIDemonProc {

	public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
			String inSlotName, Iterator inSlotValues, Object inOpts) {

		// 条件を満たすものを入れるリスト
		ArrayList<String> relist = new ArrayList<String>();

		// 自分の年齢が登録されているかチェック
		if (inFrame.readSlotValue(inFrameSystem, "誕生日", false) == null) {
			// 誕生日が登録されていないことを返す
			relist.add("誕生日が登録されていないためわからない");
		} else {

			// 自分の年齢(data1)をint型でとってくる
			int data1 = Integer.parseInt((String) inFrame.readSlotValue(
					inFrameSystem, "誕生日", false));

			//自分の親が登録されているかチェック
			if (inFrame.readSlotValue(inFrameSystem, "親", false) == null) {
				// 親が登録されていないことを返す
				relist.add("親が登録されていないためわからない");
			} else {
				
				// 自分の親の名前をとってくる
				ArrayList<String> olist = inFrame.getmVals("親");

				// 親の数だけループ
				for (int i = 0; i < olist.size(); i++) {

					// 親の名前をとる
					String parentname = (String) olist.get(i);

					// 親の名前を元に親のフレームをとってくる
					AIFrame parentframe = inFrameSystem.get_Frame(parentname);

					// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
					// klistは兄弟の名前のリスト
					ArrayList<String> klist = parentframe
							.get_leankers_Slot_names("親");


					// 親の子供の数(兄弟の数)だけループ
					for (int j = 0; j < klist.size(); j++) {

						// 兄弟の名前から兄弟のフレームをとってくる
						AIFrame frame = inFrameSystem.get_Frame(klist.get(j));

						// 兄弟フレームの誕生日スロットの値が自分より若く、かつ、性別スロットの値が男なら兄
						if (Integer.parseInt((String) frame.readSlotValue(
								inFrameSystem, "誕生日", false))  < data1
								&& frame.readSlotValue(inFrameSystem, "性別",
										false).equals("女")) {

							// 親は基本二人なので二回目以降のループ時すでに前のループで登録されてないか調べる
							if (!relist.contains(klist.get(j))) {
								// 初めての名前なら姉としてリストに入れる
								relist.add(klist.get(j));
							}
						}
					}
				}
			}
		}
		// relistを返す
		return AIFrame.makeEnum(new ArrayList<String>(relist));
	}

}
