package Frame;

public class FrameQASystem {
	static AIFrameSystem fs = new OurFrameSystem();
	public static void main(String[] args) {
		if(args.length>0){
		 AIFrame f =fs.get_Frame(args[0]);
		 if(f != null){
		for(int i = 0; i<f.get_Slot_size();i++){
			String fn = f.get_Slot_key(i);
			if(fn == "is-a"){
				AIFrame x = (AIFrame)fs.readSlotValue(args[0], f.get_Slot_key(i));
				System.out.println(args[0]+"の"+fn+"は"+x.get_name()+"です。");
			}else{
			System.out.println(args[0]+"の"+fn+"は"+fs.readSlotValue(args[0], f.get_Slot_key(i))+"です。");
			}
		}
		 
		for(int i = 0; i<f.get_leankers_size();i++){
			System.out.println(f.get_leankers_Slot_key(i)+"が"+f.get_leankers_Slot_value(i)+"で接続しています。");
		}
		 }
		}
	}
}
