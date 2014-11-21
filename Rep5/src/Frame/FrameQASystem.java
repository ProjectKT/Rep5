package Frame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class FrameQASystem {
	static AIFrameSystem fs = new OurFrameSystem();
	public static void main(String[] args) {
		if(args.length>0){
		 AIFrame f =fs.get_Frame(args[0]);
		 if(f != null){
		for(int i = 0; i<f.get_Slot_size();i++){
			String fn = f.get_Slot_key(i);
			if(fn.equals("is-a")){
				AIFrame x = (AIFrame)fs.readSlotValue(args[0], f.get_Slot_key(i));
				System.out.println(args[0]+"の"+fn+"は"+x.get_name()+"です。");
			}else{
					ArrayList list = f.getmVals(fn);
					for(int j = 0; j <list.size();j++){
						System.out.println(args[0]+"の"+fn+"は"+list.get(j)+"です。");
					}
			}
		}
		 
		for(int i = 0; i<f.get_leankers_size();i++){
			System.out.println(f.get_leankers_Slot_key(i)+"が"+f.get_leankers_Slot_value(i)+"で接続しています。");
		}
		 }
		 System.out.println(fs.readSlotValue("Kiyootouto", "兄", false));
		}
	}
}
