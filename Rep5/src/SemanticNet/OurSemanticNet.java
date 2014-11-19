package SemanticNet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OurSemanticNet extends SemanticNet {
	//各グループメンバーのデータセット
	private static final String[] FILES = {
		"Semantic067.txt",
		"Semantic088.txt",
		"Semantic109.txt",
		"Semantic110.txt",
		"Semantic113.txt",
	};

	public OurSemanticNet() {
		setupSemanticNet();
	}
	
	private void setupSemanticNet() {
		for (String filename : FILES) {
			try {
				loadFromFile(new File(filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadFromFile(File file) throws IOException {
		BufferedReader in = null;
		Pattern p = Pattern.compile("(.*) (.*) (.*)");
		Matcher m;

		try {
			// 文字コードを指定してBufferedReaderオブジェクトを作る
			in = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));

			// 変数lineに1行ずつ読み込むfor文
			for (String line = in.readLine(); line != null; line = in
					.readLine()) {
				m = p.matcher(line);
				if (m.find()) {
					addLink(new Link(m.group(2), m.group(1), m.group(3), this));
				}
			}

		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
	
	public static void main(String[] args){
		OurSemanticNet osn = new OurSemanticNet();

		osn.printLinks();
		osn.printNodes();
	}
}
