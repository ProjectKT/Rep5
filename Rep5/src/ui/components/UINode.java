package ui.components;

import java.awt.Color;
import java.awt.Graphics;

import SemanticNet.Node;

/**
 * SemanticNet のノードを表すビュー
 */
public class UINode extends MapComponent {
	
	private interface ColorSetting {
		Color normal = Color.green;
		Color selected = Color.blue;
	}
	
	/** SemanticNet のノード */
	Node node;
	/** 選択されているフラグ */
	boolean isSelected = false;
	/** ドラッグされているフラグ */
	boolean isDragged = false;
	
	//変更 ky 11/21
	public double tFromC;  //centerとの角度
	
	
	
	/**
	 * SemanticNet のノードを受け取って初期化するコンストラクタ
	 * @param node
	 */
	public UINode(Node node) {
		this.node = node;
		int len = node.getName().length();
		setSize(len*10.0, 50.0);
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
		
		// 描画
		g.setColor(isSelected ? ColorSetting.selected : ColorSetting.normal);
//		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		g.setColor(Color.black);
		g.drawString(node.getName(), 0, 20);
	}

	
	
}
