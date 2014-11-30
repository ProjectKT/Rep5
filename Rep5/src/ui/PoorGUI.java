package ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import Frame.AIFrame;
import Frame.AIFrameSystem;
import Frame.OurFrameSystem;

public class PoorGUI extends JFrame implements MouseListener {

	AIFrameSystem aiFrameSystem;
	private String init = "名古屋市";
	private JTree tree;
	private JScrollPane scrollPane;
	private DefaultMutableTreeNode rootNode;
	// ツリーとAIFrameの関係D
	protected HashMap<AIFrame, DefaultMutableTreeNode> frameMap = new HashMap<AIFrame, DefaultMutableTreeNode>();
	
	
	abstract class Node {
		public abstract int getChildCount();
		public abstract Node getChild(int index);
		public abstract int getIndexOfChild(Object child);
		public abstract boolean isLeaf();
	}
	
	class AIFrameContainer extends Node {
		AIFrame frame;
		public AIFrameContainer(AIFrame frame) { this.frame = frame; }
		@Override public int getChildCount() { return frame.getSlotSize(); }
		@Override public Node getChild(int index) { return new AISlotContainer(frame, frame.getSlotkey(index)); }
		@Override public String toString() { return frame.getName(); }
		@Override public boolean isLeaf() { return (getChildCount() <= 0); }
		@Override
		public int getIndexOfChild(Object child) {
			if (child instanceof AISlotContainer) {
				for (int i = frame.getSlotSize()-1; i >= 0; --i) {
					if (frame.getSlotkey(i).equals(((AISlotContainer) child).slotName)) {
						return i;
					}
				}
			}
			return (-1);
		}
	}
	
	class AISlotContainer extends Node {
		AIFrame frame;
		String slotName;
		public AISlotContainer(AIFrame frame, String slotName) {
			this.frame = frame;
			this.slotName = slotName;
		}
		@Override public String toString() { return slotName; }
		@Override public int getChildCount() { return frame.getmVals(slotName).size(); }
		@Override public boolean isLeaf() { return (getChildCount() <= 0); }
		@Override
		public Node getChild(int index) {
			Object slotValue = frame.getmVals(slotName).get(index);
			if (slotValue instanceof AIFrame) {
				return new AIFrameContainer((AIFrame) slotValue);
			} else if (slotValue instanceof String) {
				AIFrame childFrame = aiFrameSystem.getFrame((String) slotValue);
				if (childFrame != null) {
					return new AIFrameContainer(childFrame);
				} else {
					return new LeafNode((String) slotValue);
				}
			}
			return null;
		}
		@Override
		public int getIndexOfChild(Object child) {
			if (child instanceof AIFrameContainer) {
				Iterator it = frame.getSlotValues(slotName);
				for (int i = 0; it.hasNext(); ++i) {
					if (it.next() == ((AIFrameContainer) child).frame) {
						return i;
					}
				}
			}
			return (-1);
		}
	}
	
	class RootNode extends Node {
		@Override public String toString() { return "root"; }
		@Override public int getChildCount() { return aiFrameSystem.getFrameMap().size(); }
		@Override public Node getChild(int index) { return new AIFrameContainer(aiFrameSystem.getFrames().get(index)); }
		@Override public int getIndexOfChild(Object child) { return aiFrameSystem.getFrames().indexOf(child); }
		@Override public boolean isLeaf() { return (getChildCount() <= 0); }
	}
	
	class LeafNode extends Node {
		String value;
		public LeafNode(String value) { this.value = value; }
		@Override public String toString() { return value; }
		@Override public int getChildCount() { return 0; }
		@Override public Node getChild(int index) { return null; }
		@Override public int getIndexOfChild(Object child) { return (-1); }
		@Override public boolean isLeaf() { return true; }
	}
	
	/** ツリーモデル */
	private TreeModel treeModel = new TreeModel() {
		@Override public void valueForPathChanged(TreePath path, Object newValue) { }
		@Override public void removeTreeModelListener(TreeModelListener l) { }
		@Override public boolean isLeaf(Object node) { return ((Node) node).isLeaf(); }
		@Override public Object getRoot() { return new RootNode(); }
		@Override public int getIndexOfChild(Object parent, Object child) { return ((Node) parent).getIndexOfChild(child); }
		@Override public int getChildCount(Object parent) { return ((Node) parent).getChildCount(); }
		@Override public Object getChild(Object parent, int index) { return ((Node) parent).getChild(index); }
		@Override public void addTreeModelListener(TreeModelListener l) { }
	};
	
	

	// コンストラクタ
	public PoorGUI(AIFrameSystem aiFrameSystem) throws HeadlessException {
		this.aiFrameSystem = aiFrameSystem;
		initialize();
		setVisible(true);
	}

	// 初期化
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 1000, 800);
		setTitle("PoorGUI");

		// setupCommodPanel();
		setupTree();
	}

	private void setupTree() {
		tree = new JTree(treeModel);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.addMouseListener(this); // マウスイベント
		scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(tree);
		scrollPane.setPreferredSize(new Dimension(180, 120));
		add(scrollPane);
	}

	public void FrameExpander(AIFrame AIFrame) {
		DefaultMutableTreeNode frame = frameMap.get(AIFrame);
		DefaultMutableTreeNode Node;
		DefaultMutableTreeNode Node2;
		for (int i = 0; i < AIFrame.getSlotSize(); i++) {
			String slotName = AIFrame.getSlotkey(i);
			String inName = AIFrame.getName();
			if (slotName.equals("is-a") || slotName.equals("Ako")) {
				Node2 = new DefaultMutableTreeNode(slotName);
				frame.add(Node2);
				AIFrame x = (AIFrame) aiFrameSystem.readSlotValue(inName,
						slotName);
				Node = new DefaultMutableTreeNode(x.getName());
				if (aiFrameSystem.getFrame(x.getName()) != null) {
					frameMap.put(aiFrameSystem.getFrame(x.getName()), Node);
				}
				Node2.add(Node);
			} else {
				ArrayList list = AIFrame.getmVals(slotName);
				Node2 = new DefaultMutableTreeNode(slotName);
				frame.add(Node2);
				for (int j = 0; j < list.size(); j++) {
					Node = new DefaultMutableTreeNode(list.get(j));
					if (aiFrameSystem.getFrame((String) list.get(j)) != null) {
						frameMap.put(
								aiFrameSystem.getFrame((String) list.get(j)),
								Node);
					}
					Node2.add(Node);
				}
			}
		}
	}

	public static void main(String[] args) {
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
				if (aiFrameSystem.getFrame(text) != null) {
					System.out.println(text + "を展開しました");
					FrameExpander(aiFrameSystem.getFrame(text));
					DefaultMutableTreeNode selectNode = frameMap.get(text);
//					treeModel.reload(rootNode); // FIXME
					// repaint();
				} else {
					System.out.println(path.getLastPathComponent().toString()
							+ "はフレームではありません");
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
