package Frame;

import java.util.ArrayList;

public class FrameQASystem1 {
	static AIFrameSystem fs = new OurFrameSystem();
	public static void main(String[] args) {
		if(args.length>0){
		 AIFrame f =fs.getFrame(args[0]);
		 if(f != null){
		for(int i = 0; i<f.getSlotSize();i++){
			String fn = f.getSlotkey(i);
			if(fn.equals("is-a")){
				AIFrame x = (AIFrame)fs.readSlotValue(args[0], f.getSlotkey(i));
				System.out.println(args[0]+"の"+fn+"は"+x.getName()+"です。");
			}else{
					ArrayList list = f.getmVals(fn);
					for(int j = 0; j <list.size();j++){
						System.out.println(args[0]+"の"+fn+"は"+list.get(j)+"です。");
					}
			}
		}
		 
		System.out.println(f.getLeankersSize());
		for(int i = 0; i<f.getLeankersSize();i++){
			System.out.println(f.getLeankersSlotKey(i)+"が"+f.getLeankersSlotValue(i)+"で接続しています。");
		}
		 }
		 System.out.println(fs.readSlotValue("Kiyojiro", "兄", false));
		}
	}
}
