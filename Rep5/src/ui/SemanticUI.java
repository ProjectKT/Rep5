package ui;

import java.awt.HeadlessException;
import java.util.ArrayList;

import javax.swing.JFrame;

import ui.components.MapPanel;
import ui.components.SemanticNetLayout;
import ui.components.UINode;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import SemanticNet.Node;
import SemanticNet.OurSemanticNet;
import SemanticNet.SemanticNet;

public class SemanticUI extends JFrame {
	
	// --- ビューのメンバ ---
	private MapPanel mapPanel;
	protected UINode centerNode;
	
	// --- ロジックのメンバ ---
	private SemanticNet semanticNet;

	public SemanticUI(SemanticNet semanticNet) throws HeadlessException {
		this.semanticNet =  semanticNet;
		initialize();
		setVisible(true);
	}
	
	/**
	 * ビューを初期化する
	 */
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 300, 200);
		setTitle("SemanticUI");
		
		setupMapPanel();
		setupNodes();
	}
	
	/**
	 * MapPanel を初期化する
	 */
	private void setupMapPanel() {
		mapPanel = new MapPanel(new SemanticNetLayout());
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		getContentPane().add(mapPanel);
	}
	
	/**
	 * SemanticNet 内のノードを addNode
	 */
	private void setupNodes() {
		ArrayList<Node> nodes = semanticNet.getNodes();
		for (Node node : nodes) {
			addNode(node);
		}
	}
	
	/**
	 * ビューに SemanticNet のノードを加える
	 * @param node
	 */
	public void addNode(Node node) {
		mapPanel.add(new UINode(node));
	}
	
	
	public static void main(String[] args){
		OurSemanticNet osn = new OurSemanticNet();

		osn.printLinks();
		osn.printNodes();
		
		SemanticUI gui = new SemanticUI(osn);
		gui.setVisible(true);
	}
}
