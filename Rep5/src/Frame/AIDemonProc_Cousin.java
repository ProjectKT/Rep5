package Frame;

import java.util.ArrayList;
import java.util.Iterator;

public class AIDemonProc_Cousin  extends AIDemonProc {

	public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
			String inSlotName, Iterator inSlotValues, Object inOpts) {
		
		//条件を満たすものを入れるリスト
		ArrayList<String> relist = new ArrayList<String>();
		
		//自分の親が登録されているかチェック
		if (inFrame.readSlotValue(inFrameSystem, "親", false) == null) {
			//親が登録されていないことを返す
			relist.add("親が登録されていないためわからない");
		} else {
			
			//自分の親の名前をとってくる
			ArrayList<String> olist = inFrame.getmVals("親");
			
			//親の数だけループ
			for (int i = 0; i < olist.size(); i++) {
				
				//親の名前をとる
				String parentname = (String) olist.get(i);
				
				//親の名前を元に親のフレームをとってくる
				AIFrame parentframe = inFrameSystem.get_Frame(parentname);
				
				//とってきた親の親が登録されているかチェック
				if (parentframe.readSlotValue(inFrameSystem, "親", false) == null) {
					//祖父母が登録されていないことを返す
					relist.add("親の親が登録されていないためわからない");
				} else {
					
					//祖父母の名前をとってくる
					ArrayList<String> olist1 = inFrame.getmVals("親");
					
					//祖父母の数だけループ
					for (int j = 0; j < olist1.size(); j++) {
						
						//祖父母の名前をとる
						String parentname1 = (String) olist1.get(j);
						
						//祖父母の名前を元に祖父母のフレームをとってくる
						AIFrame parentframe1 = inFrameSystem.get_Frame(parentname1);
						
						//祖父母の逆リンクから子供の名前をとってくる
						//klistは親の兄弟のリスト
						ArrayList<String> klist = parentframe1.get_leankers_Slot_names("親");
						
						//祖父母の子供の数(親の兄弟の数)だけループ
						for (int k = 0; k < klist.size(); k++) {
							
							//親の兄弟の名前から親の兄弟のフレームをとってくる
							AIFrame frame = inFrameSystem.get_Frame(klist.get(k));
							
							//親の兄弟の逆リンクから親の兄弟の子供の名前をとってくる
							//klist1は親の兄弟の子供(従兄弟)のリスト
							ArrayList<String> klist1 = frame.get_leankers_Slot_names("親");
							
							//親の兄弟の子供の数(従兄弟の数)だけループ
							for (int l = 0; l < klist1.size(); l++) {
								
								//従兄弟の名前を従兄弟としてリストに入れる
								relist.add(klist1.get(l));
								AIFrame frame1 = inFrameSystem.get_Frame(klist1.get(l));
							}
						}
					}
				}
			}
		}
		//relistを返す
		return AIFrame.makeEnum(new ArrayList<String>(relist));
	}
}
