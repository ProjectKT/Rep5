package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ui.components.AIFramePanel0;
import ui.components.UIFrame;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import Frame.AIFrame;
import Frame.AIFrameSystem;
import Frame.OurFrameSystem;

public class AIFrameGUI0 extends JFrame implements ListSelectionListener,
		ActionListener {
	// AIFrameSystem のフレームと UIFrame との対応
	protected HashMap<AIFrame, UIFrame> frameMap = new HashMap<AIFrame, UIFrame>();

	private AIFramePanel0 mapPanel;

	private AIFrame center;

	private AIFrameSystem aIFrameSystem;

	private ArrayList<String> demonlist = new ArrayList<String>();

	private String[] data = { "兄", "姉", "弟", "妹", "祖父", "祖母", "息子", "娘", "叔父",
			"伯父", "叔母", "伯母", "従兄弟", "孫", "甥", "姪" };
	JTextField textField;
	JPanel commondPanel;
	JList list;
	JButton button;

	private ArrayList<AIFrame> openList = new ArrayList<AIFrame>();
	private ArrayList<AIFrame> closedList = new ArrayList<AIFrame>();

	// コンストラクタ
	public AIFrameGUI0(AIFrameSystem AIFramesystem) throws HeadlessException {
		this.aIFrameSystem = AIFramesystem;
		initialize();
		setVisible(true);
	}

	// 初期化
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 1000, 800);
		setTitle("AIFrameUI");
		
		setupCommodPanel();
		setupMapPanel();
		
	}

	private void setupMapPanel() {
		mapPanel = new AIFramePanel0();
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		add(mapPanel, BorderLayout.CENTER);
	}

	// フレームを全部消す
	private void resetFrames() {
		for (Iterator it = frameMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			AIFrame key = (AIFrame) entry.getKey();
			UIFrame value = (UIFrame) entry.getValue();
			value.setVisible(false);
			mapPanel.remove(value);
		}
		frameMap.clear();
		frameMap = new HashMap<AIFrame, UIFrame>();
		openList.clear();
		closedList.clear();

	}

	// フレームを追加する部分
	// 検索ボタンを押す度に呼び出す予定
	private void setupFrames(String name) {
		AIFrame frame = aIFrameSystem.getFrame(name);
		center = frame;
		int up;
		int down;
		addFrame(frame, 2, 2);
		openList.add(frame);
		while (openList.size() > 0) {
			frame = openList.get(0);

			up = frameMap.get(frame).getUp();
			down = frameMap.get(frame).getDown();
			if (up > 0) {
				searchUp(frame, up - 1, down);
			}
			if (down > 0) {
				searchDown(frame, up, down - 1);
			}
			openList.remove(frame);
			closedList.add(frame);

		}
		for (int i = 0; i < closedList.size(); i++) {
			System.out.println(closedList.get(i).getName());
			up = frameMap.get(closedList.get(i)).getUp();
			down = frameMap.get(closedList.get(i)).getDown();
			System.out.println("up:" + up + ", down:" + down);
		}
	}

	private void searchUp(AIFrame frame, int up, int down) {
		// 自分の親が登録されているかチェック
		if (!(frame.readSlotValue(aIFrameSystem, "親", false) == null)) {
			// 自分の親の名前をとってくる
			ArrayList<String> parentlist = frame.getmVals("親");
			for (int i = 0; i < parentlist.size(); i++) {
				AIFrame parentframe = aIFrameSystem.getFrame(parentlist.get(i));
				if (!openList.contains(parentframe)
						& !closedList.contains(parentframe)) {
					openList.add(parentframe);
					addFrame(parentframe, up, down);
				}
			}
		}
	}

	private void searchDown(AIFrame frame, int up, int down) {
		ArrayList<String> childlist = frame.getLeankersSlotNames("親");
		for (int i = 0; i < childlist.size(); i++) {
			AIFrame childframe = aIFrameSystem.getFrame(childlist.get(i));
			if (!openList.contains(childframe)
					& !closedList.contains(childframe)) {
				openList.add(childframe);
				addFrame(childframe, up, down);
			}
		}
	}

	/**
	 * ビューに AIFrameSystem のフレームを加える
	 * 
	 * @param node
	 */
	public void addFrame(AIFrame aIFrame, int up, int down) {
		UIFrame uIFrame = new UIFrame(aIFrame, up, down);
		if (aIFrame == center) {
			uIFrame.setColor(Color.GREEN);
		}
		uIFrame.setVisible(true);
		mapPanel.add(uIFrame);
		frameMap.put(aIFrame, uIFrame);
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

	// デモン手続きで親族を見つけその色を変える
	public void searchDemon(String demon) {
		for (int i = 0; i < demonlist.size(); i++) {
			if (aIFrameSystem.getFrame(demonlist.get(i)) != null) {
				AIFrame changeframe = aIFrameSystem.getFrame(demonlist.get(i));
				frameMap.get(changeframe).setColor(Color.BLUE);
				;
			}
		}
		if (center != null) {
			if (aIFrameSystem.getFrame(center.getName()) != null) {
				demonlist = (ArrayList<String>) aIFrameSystem.readSlotValue(
						center.getName(), demon);
			}
		}
		for (int i = 0; i < demonlist.size(); i++) {
			if (aIFrameSystem.getFrame(demonlist.get(i)) != null) {
				AIFrame changeframe = aIFrameSystem.getFrame(demonlist.get(i));
				frameMap.get(changeframe).setColor(Color.RED);
				;
			}
		}
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
		// TODO Auto-generated method stub
		JList lst = (JList) e.getSource();
		// System.out.println(lst.getSelectedValue());
		searchDemon((String) lst.getSelectedValue());
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// いま出ているフレームをリセット
		if (aIFrameSystem.getFrame((String) textField.getText()) == null) {
			System.out.println("そのような名前のフレームは登録されていません\n以下の名前のフレームが存在します");
			printHumanName();
		} else {
			resetFrames();
			// 新しいフレームをセット

			setupFrames(textField.getText());
			repaint();
		}
	}

	public static void main(String[] args) {
		OurFrameSystem ofs = new OurFrameSystem();

		AIFrameGUI0 gui = new AIFrameGUI0(ofs);
		gui.setVisible(true);
	}

}
