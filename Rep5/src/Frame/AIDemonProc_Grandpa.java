package Frame;

import java.util.ArrayList;
import java.util.Iterator;

public class AIDemonProc_Grandpa extends AIDemonProc {
	
	public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
			String inSlotName, Iterator inSlotValues, Object inOpts) {

		// 条件を満たすものを入れるリスト
		ArrayList<String> relist = new ArrayList<String>();
		
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
				
				if (parentframe.readSlotValue(inFrameSystem, "親", false) == null) {
					// 祖父母が登録されていないことを返す
					relist.add("親が登録されていないためわからない");
				} else {
				//祖父母の名前をとってくる
				 ArrayList<String> glist = parentframe.getmVals("親");
					// 祖父母の数だけループ
					for (int j = 0; j < glist.size(); j++) {
						// 祖父母の名前をとる
						String gparentname = (String) glist.get(j);
						// 祖父母の名前を元に祖父母のフレームをとってくる
						AIFrame gparentframe = inFrameSystem.get_Frame(gparentname);
						    //男なら祖父
							if(gparentframe.readSlotValue(inFrameSystem, "性別",false).equals("男")){
								relist.add(gparentname);
							}
						}
					}
				}
			}
		
		return AIFrame.makeEnum(new ArrayList<String>(relist));
	}
}
