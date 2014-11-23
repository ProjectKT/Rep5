package Frame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class FrameQASystem_prototype2 {
	static AIFrameSystem fs = new OurFrameSystem();
	
	public static void main(String[] args) {
		FrameQASystem_prototype2 test =new FrameQASystem_prototype2();
	}
	
	FrameQASystem_prototype2(){
		MainSystem();
	}
	
	public void MainSystem(){
	String buf;
		
		Pattern p = Pattern.compile(".*？");
		Matcher m;
		
		//？で終わる入力を受ける
		do{
			System.out.println("必須課題5-2の質疑応答システムです\n指定の形式で質問をお願いします\n形式：?x ?y？");
		InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        try{
                buf = br.readLine();
        }catch(Exception e){
                buf = "";
        }
        System.out.println("");
         m = p.matcher(buf);
		}while(!m.find());
		
		Pattern p2 = Pattern.compile("(.*) (.*)？");
		Matcher m2 = p2.matcher(buf);
		
		if(m2.find()){
		Answer(m2.group(1),m2.group(2));
		
		}else{
			//?xは？の場合
			
			Pattern p3 = Pattern.compile("(.*)？");
			Matcher m3 = p3.matcher(buf);
			if(m3.find()){
			if(PrintAllSlot(m3.group(1))){
				PrintAllLeankers(m3.group(1));
			}else{
				System.out.println(m3.group(1)+"というフレームは存在しません");
			}
			}
		}
		
	}
	
	public ArrayList<String> Answer(String s1,String s2){
		Pattern p4 = Pattern.compile("(.*) (.*)");
		Matcher m4 = p4.matcher(s1);
		if(m4.find()){
			ArrayList list = Answer(m4.group(1),m4.group(2));
			ArrayList<String> relist =new ArrayList<String>();
			
			for(int i=0;i<list.size();i++){
				AIFrame f =fs.get_Frame((String) list.get(i));
				System.out.print(list.get(i)+"の"+s2+"は");
				ArrayList list2 = new ArrayList<String>() ;
				if(fs.readSlotValue((String) list.get(i),s2,false).getClass().getName().equals("java.lang.String")){
					if(s2.equals("is-a")){
						AIFrame x = (AIFrame)fs.readSlotValue((String) list.get(i), s2);
						list2.add(x.get_name());
					}else{
							list2 = f.getmVals(s2);
					}
				}else{
					list2 = (ArrayList<String>) fs.readSlotValue((String) list.get(i), s2, false);
				}
				for(int i2=0;i2<list2.size();i2++){
					relist.add((String)list2.get(i2));
					if(i2==list2.size()-1){
						System.out.println(list2.get(i2)+"です");
					}else{
						System.out.print(list2.get(i2)+"、");
					}
				}
			}
			return relist;
		}else{
			System.out.print(s1+"の"+s2+"は");
			ArrayList<String> list = new ArrayList<String>();
			AIFrame f =fs.get_Frame(s1);
			
			if(fs.readSlotValue(s1, s2, false).getClass().getName().equals("java.lang.String")){

				if(s2.equals("is-a")){
					AIFrame x = (AIFrame)fs.readSlotValue(s1, s2);
					list.add(x.get_name());
				}else{
						list = f.getmVals(s2);
				}
			}else{
				list = (ArrayList<String>) fs.readSlotValue(s1, s2, false);
			}
				for(int i =0; i<list.size();i++){
					if(i==list.size()-1){
						System.out.println(list.get(i)+"です");
					}else{
						System.out.print(list.get(i)+"、");
					}
				}
			return list;
		}
	}
	
	//指定されたフレームのスロットを全て表示
	public boolean PrintAllSlot(String inName){
		AIFrame f =fs.get_Frame(inName);
		 if(f != null){
		for(int i = 0; i<f.get_Slot_size();i++){
			PrintSlot(f.get_Slot_key(i),inName);
		}
		return true;
		 }else{
			 return false;
		 }
	}
	
	//フレームの指定されたスロットを表示
	public void PrintSlot(String slotName,String inName){
		AIFrame f =fs.get_Frame(inName);
		if(slotName.equals("is-a")){
			AIFrame x = (AIFrame)fs.readSlotValue(inName, slotName);
			System.out.println(inName+"は"+x.get_name()+"のインスタンスです。");
		}else{
				ArrayList list = f.getmVals(slotName);
				for(int j = 0; j <list.size();j++){
					System.out.println(inName+"の"+slotName+"は"+list.get(j)+"です。");
				}
		}
	}
	
	//指定されたフレームの接続を全て表示
	public void PrintAllLeankers(String inName){
		AIFrame f =fs.get_Frame(inName);
		for(int i = 0; i<f.get_leankers_size();i++){
			System.out.println(f.get_leankers_Slot_key(i)+"が"+f.get_leankers_Slot_value(i)+"で接続しています。");
		}
	}
}
