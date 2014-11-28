package ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import ui.AIFrameUI;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import Frame.AIFrame;
import Frame.AIFrameSystem;
import Frame.OurFrameSystem;

public class AIFrameGUIver0 extends JFrame implements ListSelectionListener, ActionListener {

	private AIFramePanelver0 mapPanel;
	
	private AIFrameSystem AIFramesystem;
	
	private String[] data = {"兄","姉","弟","妹"};
	
	//コンストラクタ
	public AIFrameGUIver0(AIFrameSystem AIFramesystem) throws HeadlessException {
		this.AIFramesystem = AIFramesystem;
		initialize();
		setVisible(true);
	}
	
	//初期化
	private void initialize(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 1000, 800);
		setTitle("AIFrameUI");
		setupCommodPanel();
	}
	
	private void setupMapPanel() {
		mapPanel = new AIFramePanelver0();
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		getContentPane().add(mapPanel);
	}
	
	//フレームを追加する部分
	//検索ボタンを押す度に呼び出す予定
	private void setupFrames() {
		ArrayList<AIFrame> frames = AIFramesystem.getFrames();
		for (AIFrame frame : frames) {
			addFrame(frame);
		}

	}
	
	/**
	 * ビューに AIFrameSystem のフレームを加える
	 * @param node
	 */
	public void addFrame(AIFrame aIFrame) {
		mapPanel.add(new UIFrame(aIFrame));
	}
	
	
	//コマンドパネルを追加する部分
	public void setupCommodPanel(){
		JPanel commondPanel = new JPanel();
		JTextField textfield = new JTextField();
		JList list = new JList(data);
		list.addListSelectionListener(this);
		JButton button = new JButton("検索");
		button.addActionListener(this);
		 JScrollPane sp = new JScrollPane();
		    sp.getViewport().setView(list);
		    sp.setPreferredSize(new Dimension(100, 50));
		commondPanel.setLayout(new GridLayout(1, 3));
		commondPanel.add(textfield);
		commondPanel.add(sp);
		commondPanel.add(button);
		setupMapPanel();
		textfield.setText("人名");
		add(commondPanel ,BorderLayout.SOUTH);
	}
	

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		JList lst = (JList)e.getSource();
		System.out.println(lst.getSelectedValue());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.print("a");
		
	}

	public static void main(String[] args) {
		OurFrameSystem ofs = new OurFrameSystem();

	AIFrameGUIver0 gui = new AIFrameGUIver0(ofs);
	gui.setVisible(true);
}




}
