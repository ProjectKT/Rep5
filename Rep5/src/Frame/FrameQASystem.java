package Frame;

public class FrameQASystem {
	static AIFrameSystem fs = new OurFrameSystem();
	public static void main(String[] args) {
		if(args.length>1){
		 AIFrame f =fs.get_Frame(args[0]);
		System.out.println(f.get_Slot_size());
		for(int i = 0; i<f.get_Slot_size();i++){
			System.out.println(args[0]+"の"+f.get_Slot_name(i)+"は"+fs.readSlotValue(args[0],f.get_Slot_name(i),false)+"です。");
		}
		for(int i = 0; i<f.get_leankers_size();i++){
			System.out.println(args[0]+"de"+f.get_leankers_Slot_name(i)+"は"+fs.readSlotValue(args[0],f.get_leankers_Slot_name(i),false)+"です。");
		}
		}
	}
}
