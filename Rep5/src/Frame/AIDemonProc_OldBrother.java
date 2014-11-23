package Frame;

import java.util.ArrayList;
import java.util.Iterator;

public class AIDemonProc_OldBrother extends AIDemonProc {
	
	public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
			String inSlotName, Iterator inSlotValues, Object inOpts) {
		//
		int data1 = Integer.parseInt((String)inFrame.readSlotValue(inFrameSystem, "誕生日", false));
		System.out.println("a");
		String papa = (String)inFrame.readSlotValue(inFrameSystem, "親", false);
		System.out.println(papa);
		AIFrame papaframe = inFrameSystem.get_Frame(papa);
		ArrayList<String> list = papaframe.get_leankers_Slot_names("親");
		for(int i=0;i<list.size();){
			System.out.println(list.get(i));
			AIFrame frame = inFrameSystem.get_Frame(list.get(i));
			AIFrame x = (AIFrame)inFrameSystem.readSlotValue(list.get(i),"is-a");
			if(Integer.parseInt((String)frame.readSlotValue(inFrameSystem, "誕生日", false))<data1 && x.get_name().equals("男")){
				i++;
			}else{
				list.remove(i);
			}
		}

		return AIFrame.makeEnum(new ArrayList<String>(list));
	}
	
}
