package ui.components;

import java.awt.Graphics;

import SemanticNet.Node;

/**
 * SemanticNet のノードを表すビュー
 */
public class UINode extends MapComponent {
	private Node node;
	
	/**
	 * SemanticNet のノードを受け取って初期化するコンストラクタ
	 * @param node
	 */
	public UINode(Node node) {
		this.node = node;
	}

	/**
	 * SemanticNet のノードを返す
	 * @return
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * {@inheritDoc}
	 * ノードを描画する
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// TODO ここに描画する部分を書く
	}
	
	
}
