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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import semanticnet.Link;
import semanticnet.Node;
import semanticnet.OurSemanticNet;
import semanticnet.SemanticNet;
import ui.components.HintTextField;
import ui.components.SemanticNetPanel;
import ui.components.UINode;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;

public class SemanticUI extends JFrame implements SemanticNetPanel.Callbacks {
	
	private static final String[] tableColumnNames = {"link"};
	
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
				return semanticNet.getLinks().get(rowIndex);
			default:
				return null;
			}
		}
		@Override
		public int getRowCount() {
			return semanticNet.getLinks().size();
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
			case 0: return Link.class;
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
	}
	
	/**
	 * MapPanel を初期化する
	 */
	private void setupMapPanel() {
		mapPanel = new SemanticNetPanel(semanticNet);
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		mapPanel.setCallbacks(this);
	}

	@Override
	public void onSelectUINodes(UINode[] uiNodes) {
		tblDataView.clearSelection();
		for (int i = tableModel.getRowCount()-1; i >= 0; --i) {
			Link link = (Link) tableModel.getValueAt(i, 0);
			for (UINode uiNode : uiNodes) {
				if (link.getTail().equals(uiNode.getNode())) {
					tblDataView.addRowSelectionInterval(i, i);
					break;
				}
			}
		}
	}
	
	public static void main(String[] args){
		OurSemanticNet osn = new OurSemanticNet();

		osn.printLinks();
		osn.printNodes();
		
		SemanticUI gui = new SemanticUI(osn);
		gui.setVisible(true);
	}
}
