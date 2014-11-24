package ui.components;

import java.awt.Color;
import java.awt.Graphics;

import SemanticNet.Node;

/**
 * SemanticNet のノードを表すビュー
 */
public class UINode extends MapComponent {
	private Node node;
	
	
	//変更 ky 11/21
	public double tFromC;  //centerとの角度
	
	
	
	/**
	 * SemanticNet のノードを受け取って初期化するコンストラクタ
	 * @param node
	 */
	public UINode(Node node) {
		this.node = node;
		int len = node.getName().length();
		setSize(len*8.0, 20.0);
	}

	/**
	 * SemanticNet のノードを返す
	 * @return
	 */
	public Node getNode() {
		return node;
	}

	//変更 ky 11/21
	public double getTheta(){
		return tFromC;
	}
	
	//変更 ky 11/21
	public void putTheta(double theta){
		this.tFromC = theta;
	}
	
	
	/**
	 * {@inheritDoc}
	 * ノードを描画する
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// TODO ここに描画する部分を書く
		g.setColor(Color.green);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.drawString(node.getName(), 0, 0);
	}
	
	
}
