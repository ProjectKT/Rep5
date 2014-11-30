package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ui.components.AIFramePanel0;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import Frame.AIFrameSystem;
import Frame.OurFrameSystem;

public class AIFrameGUI0 extends JFrame implements ListSelectionListener,
		ActionListener {

	private AIFrameSystem aIFrameSystem;
	private AIFramePanel0 mapPanel;
	
	private String[] data = { "兄", "姉", "弟", "妹", "祖父", "祖母", "息子", "娘", "叔父",
			"伯父", "叔母", "伯母", "従兄弟", "孫", "甥", "姪" };
	private JTextField textField;
	private JPanel commondPanel;
	private JList list;
	private JButton button;

	/** コンストラクタ */
	public AIFrameGUI0(AIFrameSystem AIFramesystem) throws HeadlessException {
		this.aIFrameSystem = AIFramesystem;
		initialize();
		setVisible(true);
	}

	/** 初期化 */
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 1000, 800);
		setTitle("AIFrameUI");
		
		setupCommodPanel();
		setupMapPanel();
	}

	private void setupMapPanel() {
		mapPanel = new AIFramePanel0(aIFrameSystem);
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		add(mapPanel, BorderLayout.CENTER);
	}

	
	// コマンドパネルを追加する部分
	public void setupCommodPanel() {
		commondPanel = new JPanel();
		list = new JList(data);
		list.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 25));
		textField = new JTextField("人名");
		textField.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 50));
		list.addListSelectionListener(this);
		button = new JButton("検索");
		button.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 50));
		button.addActionListener(this);
		JScrollPane sp = new JScrollPane();
		sp.getViewport().setView(list);
		sp.setPreferredSize(new Dimension(100, 100));
		commondPanel.setLayout(new GridLayout(1, 3));
		commondPanel.add(textField);
		commondPanel.add(sp);
		commondPanel.add(button);
		add(commondPanel, BorderLayout.SOUTH);
	}



	// 登録されている人の名前をコンソールに列挙
	public void printHumanName() {
		ArrayList<String> list = aIFrameSystem.getFrame("人間")
				.getLeankersSlotNames("is-a");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList lst = (JList) e.getSource();
		mapPanel.highlightRelatedFrames((String) lst.getSelectedValue());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// いま出ているフレームをリセット
		if (aIFrameSystem.getFrame((String) textField.getText()) == null) {
			System.out.println("そのような名前のフレームは登録されていません\n以下の名前のフレームが存在します");
			printHumanName();
		} else {
			mapPanel.showFramesFor(textField.getText());
		}
	}

	public static void main(String[] args) {
		OurFrameSystem ofs = new OurFrameSystem();

		AIFrameGUI0 gui = new AIFrameGUI0(ofs);
		gui.setVisible(true);
	}

}
