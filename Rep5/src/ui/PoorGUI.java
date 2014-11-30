package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import ui.components.PoorPanel;
import ui.components.UIFrame;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import Frame.AIFrame;
import Frame.AIFrameSystem;
import Frame.OurFrameSystem;



public class PoorGUI extends JFrame implements MouseListener {
	
	private AIFrameSystem AIFrameSystem;
	private PoorPanel mapPanel;
	private String init = "名古屋市";
	private JTree tree;
	private DefaultTreeModel treeModel;
	private JScrollPane scrollPane;
	private DefaultMutableTreeNode rootNode;
	//ツリーとAIFrameの関係D
	protected HashMap<AIFrame,DefaultMutableTreeNode> frameMap = new HashMap<AIFrame,DefaultMutableTreeNode>();
	
	// コンストラクタ
	public PoorGUI(AIFrameSystem AIFrameSystem) throws HeadlessException {
	this.AIFrameSystem = AIFrameSystem;
	initialize();
	setVisible(true);
	}
	 
	// 初期化
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 1000, 800);
		setTitle("PoorGUI");
		
		//setupCommodPanel();
		setupMapPanel();
		setupTree();
	}
	
	/**
	 * MapPanel を初期化する
	 */
	private void setupMapPanel() {
		mapPanel = new PoorPanel();
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		add(mapPanel, BorderLayout.CENTER);
	}
	
	private void setupTree(){
		rootNode = new DefaultMutableTreeNode(init);
		frameMap.put(AIFrameSystem.getFrame(init),rootNode);
		FrameExpander(AIFrameSystem.getFrame(init));
		treeModel = new DefaultTreeModel(rootNode, true);
		
		tree = new JTree(treeModel);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.addMouseListener(this);	//マウスイベント
		scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(tree);
	    scrollPane.setPreferredSize(new Dimension(180, 120));
	    add(scrollPane);
	}
	
	public void FrameExpander(AIFrame AIFrame){
		DefaultMutableTreeNode frame = frameMap.get(AIFrame);
		DefaultMutableTreeNode Node;
		DefaultMutableTreeNode Node2;
		for (int i = 0; i < AIFrame.getSlotSize(); i++) {
			String slotName = AIFrame.getSlotkey(i);
			String inName = AIFrame.getName(); 
			if (slotName.equals("is-a") || slotName.equals("Ako")) {
				Node2 = new DefaultMutableTreeNode(slotName);
				frame.add(Node2);
				AIFrame x = (AIFrame) AIFrameSystem.readSlotValue(inName, slotName);
				Node = new DefaultMutableTreeNode(x.getName());
				if(AIFrameSystem.getFrame(x.getName())!=null){
				frameMap.put(AIFrameSystem.getFrame(x.getName()),Node);
				}
				Node2.add(Node);
			} else {
				ArrayList list = AIFrame.getmVals(slotName);
				Node2 = new DefaultMutableTreeNode(slotName);
				frame.add(Node2);
				for (int j = 0; j < list.size(); j++) {
				Node = new DefaultMutableTreeNode(list.get(j));
				if(AIFrameSystem.getFrame((String)list.get(j))!=null){
				frameMap.put(AIFrameSystem.getFrame((String)list.get(j)),Node);
				}
				Node2.add(Node);
				}
			}
		}
	}
	

	public static void main(String[] args){
		OurFrameSystem ofs = new OurFrameSystem();

		PoorGUI gui = new PoorGUI(ofs);
		gui.setVisible(true);
	}

	@Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 3) {
            TreePath path = tree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
            	String text = path.getLastPathComponent().toString();
        	if(AIFrameSystem.getFrame(text)!=null){
                System.out.println(text+"を展開しました");
        		FrameExpander(AIFrameSystem.getFrame(text));
        		DefaultMutableTreeNode selectNode = frameMap.get(text);
        		treeModel.reload(rootNode);
        		//repaint();
        	}else{
        		System.out.println(path.getLastPathComponent().toString()+"はフレームではありません");
        	}
            }
        }
    }

	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
