package ui.components;

import java.awt.Graphics;

import SemanticNet.Link;

public class UIArrow extends MapComponent {

	private Link link;
	
	/**
	 * SemanticNet のリンクを受け取って初期化するコンストラクタ
	 * @param link
	 */
	public UIArrow(Link link) {
		this.link = link;
	}

	/**
	 * SemanticNet のリンクを返す
	 * @return
	 */
	public Link getLink() {
		return link;
	}

	/**
	 * {@inheritDoc}
	 * リンクを表す矢印を描画する
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// TODO ここに描画する部分を書く
	}
	
	

}
