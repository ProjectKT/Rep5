package ui;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ui.components.SemanticNetPanel;
import ui.components.UINode;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import SemanticNet.Link;
import SemanticNet.Node;
import SemanticNet.OurSemanticNet;
import SemanticNet.SemanticNet;

public class SemanticUI extends JFrame {
	
	// --- ビューのメンバ ---
	private SemanticNetPanel mapPanel;
	
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
		setBounds(10, 10, 800, 600);
		setTitle("SemanticUI");
		
		setupMapPanel();
		setupNodes();
	}
	
	/**
	 * MapPanel を初期化する
	 */
	private void setupMapPanel() {
		mapPanel = new SemanticNetPanel();
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
		ArrayList<Node> nodes = new ArrayList<Node>(semanticNet.getHeadNodes());
		Node centerNode = semanticNet.getMostLink();
		nodes.remove(centerNode);
		nodes.add(0,centerNode);
		
		final ArrayList<Node> fnodes = nodes;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(;;){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					final Node node = fnodes.get(0);
					fnodes.remove(0);
					
					if(node.getDepartFromMeLinks().size() > 0){
						ArrayList<Link> link = node.getDepartFromMeLinks();
						for(Link linkedNode:link){
							fnodes.add(0,linkedNode.getHead());
						}
					}
					
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								System.out.println("adding node");
								addNode(node);
							}
						});
					} catch (InvocationTargetException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(fnodes.size() == 0)
						break;
				}
			}
		}).start();
		

		//  ノードセットの方針:リスト（OPENリスト)の先頭から展開していく
		//	リンクが入ってない（ヘッドノード)をOPENリストに、その中で一番リンクが出ているノードを初めに展開する。
		//	出てきたリンクをOPENリストの先頭に入れ、以上を繰り返す
		//
	}
	
	/**
	 * ビューに SemanticNet のノードを加える
	 * @param node
	 */
	public void addNode(Node node) {
		mapPanel.addNode(node);
	}

	
	public static void main(String[] args){
		OurSemanticNet osn = new OurSemanticNet();

		osn.printLinks();
		osn.printNodes();
		
		SemanticUI gui = new SemanticUI(osn);
		gui.setVisible(true);
	}
}
