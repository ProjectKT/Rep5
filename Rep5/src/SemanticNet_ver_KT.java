import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//必須課題5−1で使うグループメンバーに関するセマンティックネット(仮)
public class SemanticNet_ver_KT {
	static SemanticNet sn;

	//外部から呼ばれることを想定したコンストラクタ
	SemanticNet_ver_KT(){
		String[] z = null;
		main(z);
	}
	
	public static void main(String[] args){
		sn = new SemanticNet();
		//String[] file = {"SemanticSample.txt"};
		
		//各グループメンバーのデータセット
		String[] file = {"Semantic067.txt",
				"Semantic088.txt",
				"Semantic109.txt",
				"Semantic110.txt",
				"Semantic113.txt",};
				
		BufferedReader in;
		Pattern p = Pattern.compile("(.*) (.*) (.*)");
		Matcher m;
		
		for(int i = 0;i< file.length; i++){
			try {    // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文

	            // 文字コードを指定してBufferedReaderオブジェクトを作る
	            in = new BufferedReader(new InputStreamReader(new FileInputStream(file[i]), "UTF-8"));

	         // 変数lineに1行ずつ読み込むfor文
	            for (String line = in.readLine(); line != null; line = in.readLine()) {
	            	m = p.matcher(line);
	            	if(m.find()){
	            		//リンクを登録
	            		sn.addLink(new Link(m.group(2),m.group(1),m.group(3),sn));
	            	}
	            }

	        } catch (IOException e) {
	            e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
	        }
		}
		
		sn.printLinks();
		sn.printNodes();
		
	}		
	
	static SemanticNet get_Net(){
		return sn;
	}
}
