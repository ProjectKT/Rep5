package ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.SwingUtilities;

import SemanticNet.Node;

/**
 * SemanticNet のノードを表すビュー
 */
public class UINode extends MapComponent {
	
	private interface Settings {
		/** フォントの設定 */
		interface Fonts {
			Font label = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
		}
		/** ストロークの設定 */
		interface Strokes {
			Stroke border = new BasicStroke(1.0f);
			Stroke borderSelected = new BasicStroke(1.0f);
		}
		/** 色の設定 */
		interface Colors {
			Color backgroundNormal = new Color(0xbb00bffb, true);
			Color backgroundSelected = new Color(0xee00eeff, true);
			Color borderNormal = new Color(0xbb000000, true);
			Color borderSelected = new Color(0xff000000, true);
			Color labelNormal = Color.black;
			Color labelSelected = Color.black;
		}
		/** パディング */
		interface Paddings {
			double left = 10;
			double top = 10;
			double right = 10;
			double bottom = 10;
		}
		int arcWidth = 10;
		int arcHeight = 10;
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
		int width = getFontMetrics(Settings.Fonts.label).stringWidth(node.getName());
		int height = getFontMetrics(Settings.Fonts.label).getHeight();
		setSize(
				Settings.Paddings.left + (double) width + Settings.Paddings.right,
				Settings.Paddings.top + (double) height + Settings.Paddings.bottom
		);
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
		g.setFont(Settings.Fonts.label.deriveFont((float) (Settings.Fonts.label.getSize2D() / panel.zoom)));
		
		// 描画
		g.setColor(isSelected ? Settings.Colors.backgroundSelected : Settings.Colors.backgroundNormal);
		g.fillRoundRect(0, 0, getWidth(), getHeight(),
				(int) (Settings.arcWidth / panel.zoom),
				(int) (Settings.arcHeight / panel.zoom)
		);
		((Graphics2D) g).setStroke(isSelected ? Settings.Strokes.borderSelected : Settings.Strokes.border);
		g.setColor(isSelected ? Settings.Colors.borderSelected : Settings.Colors.borderNormal);
		g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1,
			(int) (Settings.arcWidth / panel.zoom),
			(int) (Settings.arcHeight / panel.zoom)
		);
		g.setColor(isSelected ? Settings.Colors.labelSelected : Settings.Colors.labelNormal);
		g.drawString(
				node.getName(),
				(int) (Settings.Paddings.left / panel.zoom),
				(int) (getHeight() - Settings.Paddings.bottom / panel.zoom)
		);
		
	}

	
	
}
