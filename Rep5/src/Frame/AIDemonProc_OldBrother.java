package Frame;

import java.util.ArrayList;
import java.util.Iterator;

public class AIDemonProc_OldBrother extends AIDemonProc {
	
	public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
			String inSlotName, Iterator inSlotValues, Object inOpts) {
		//
		int data1 = Integer.parseInt((String)inFrame.readSlotValue(inFrameSystem, "誕生日", false));
		String papa = (String)inFrame.readSlotValue(inFrameSystem, "親", false);
		AIFrame papaframe = inFrameSystem.get_Frame(papa);
		ArrayList<String> list = papaframe.get_leankers_Slot_names("親");
		for(int i=0;i<list.size();){
			AIFrame frame = inFrameSystem.get_Frame(list.get(i));
			if(Integer.parseInt((String)frame.readSlotValue(inFrameSystem, "誕生日", false))<data1 && frame.readSlotValue(inFrameSystem, "性別", false).equals("男")){
				i++;
			}else{
				list.remove(i);
			}
		}

		return AIFrame.makeEnum(new ArrayList<String>(list));
	}
	
}
