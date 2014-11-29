package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import ui.components.HintTextField;
import ui.components.SemanticNetPanel;
import ui.components.UINode;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import SemanticNet.Link;
import SemanticNet.Node;
import SemanticNet.OurSemanticNet;
import SemanticNet.SemanticNet;

public class SemanticUI extends JFrame implements SemanticNetPanel.Callbacks {
	
	private static final String[] tableColumnNames = {"data"};
	
	// --- ビューのメンバ ---
	private JLabel lblDataView;
	private HintTextField tfFilter;
	private JTable tblDataView;
	private SemanticNetPanel mapPanel;
	
	// --- ロジックのメンバ ---
	private SemanticNet semanticNet;
	
	private TableModel tableModel = new TableModel() {
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
//				semanticNet.getNodes().get(rowIndex).setName((String) aValue);
			}
		}
		@Override
		public void removeTableModelListener(TableModelListener l) {
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return semanticNet.getNodes().get(rowIndex);
			default:
				return null;
			}
		}
		@Override
		public int getRowCount() {
			return semanticNet.getNodes().size();
		}
		@Override
		public String getColumnName(int columnIndex) {
			return tableColumnNames[columnIndex];
		}
		@Override
		public int getColumnCount() {
			return 1;
		}
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0: return String.class;
			default: return null;
			}
		}
		@Override
		public void addTableModelListener(TableModelListener l) {
		}
	};

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
		
		/* --- MENU --- */
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadDataset = new JMenuItem("Load DataSet");
		mnFile.add(mntmLoadDataset);
		
		JMenuItem mntmSaveDataset = new JMenuItem("Save DataSet");
		mnFile.add(mntmSaveDataset);
		
		JMenuItem mntmSaveDatasetAs = new JMenuItem("Save DataSet As ...");
		mnFile.add(mntmSaveDatasetAs);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnDataSet = new JMenu("DataSet");
		menuBar.add(mnDataSet);
		
		JMenuItem mntmRefresh = new JMenuItem("Reload");
		mnDataSet.add(mntmRefresh);
		
		JMenuItem mntmClear = new JMenuItem("Clear");
		mnDataSet.add(mntmClear);
		
		/* --- Content --- */
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0.3);
		getContentPane().add(splitPane);
		
		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		lblDataView = new JLabel("DataView");
		panel_1.add(lblDataView, BorderLayout.NORTH);
		
		JPanel panel_1c = new JPanel();
		panel_1c.setLayout(new BorderLayout(0, 0));
		panel_1.add(panel_1c, BorderLayout.CENTER);
		
		tfFilter = new HintTextField("Filter", Color.GRAY);
		tfFilter.setBackground(new Color(250, 250, 250));
		tfFilter.setBorder(new EmptyBorder(2, 2, 2, 2));
//		tfFilter.getDocument().addDocumentListener(filterChangeListener);
		panel_1c.add(tfFilter, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		panel_1c.add(scrollPane, BorderLayout.CENTER);
		
		tblDataView = new JTable(tableModel);
		scrollPane.setViewportView(tblDataView);
		
		setupMapPanel();
		splitPane.setRightComponent(mapPanel);
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
		mapPanel.setCallbacks(this);
//		getContentPane().add(mapPanel);
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
						Thread.sleep(100);
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

	@Override
	public void onSelectUINodes(UINode[] uiNodes) {
		System.out.println(uiNodes);
	}
	
	public static void main(String[] args){
		OurSemanticNet osn = new OurSemanticNet();

		osn.printLinks();
		osn.printNodes();
		
		SemanticUI gui = new SemanticUI(osn);
		gui.setVisible(true);
	}
}
