package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import ui.components.AIFramePanelver0;
import ui.components.UIFrame;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import Frame.AIFrame;
import Frame.AIFrameSystem;
import Frame.OurFrameSystem;

public class AIFrameGUIver0 extends JFrame implements ListSelectionListener, ActionListener {
	// AIFrameSystem のフレームと UIFrame との対応
	protected HashMap<AIFrame,UIFrame> frameMap = new HashMap<AIFrame,UIFrame>();
	
	private AIFramePanelver0 mapPanel;
	
	private AIFrame center;
	
	private AIFrameSystem AIFramesystem;
	
	private ArrayList<String> demonlist= new ArrayList<String>();
	
	private String[] data = {"兄","姉","弟","妹","祖父","祖母","息子","娘","叔父","伯父","叔母","伯母","従兄弟","孫","甥","姪"};
	JTextField textfield;
	JPanel commondPanel;
	JList list;
	JButton button;
	
	private ArrayList<AIFrame> openlist = new ArrayList<AIFrame>();
	private ArrayList<AIFrame> closedlist = new ArrayList<AIFrame>();
	
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
	
	//フレームを全部消す
	private void resetFrames(){
		for (Iterator it = frameMap.entrySet().iterator(); it.hasNext();) {
		    Map.Entry entry = (Map.Entry)it.next();
		    AIFrame key = (AIFrame) entry.getKey();
		    UIFrame value = (UIFrame) entry.getValue();
		    value.setVisible(false);
		     mapPanel.remove(value);
		    		}
		frameMap.clear();
		frameMap = new HashMap<AIFrame,UIFrame>();
		openlist.clear();
		closedlist.clear();

	}
	
	//フレームを追加する部分
	//検索ボタンを押す度に呼び出す予定
	private void setupFrames(String name) {
		AIFrame frame = AIFramesystem.get_Frame(name);
		center = frame;
		int up;
		int down;
		addFrame(frame, 2, 2);
		openlist.add(frame);
		while(openlist.size()>0){
			frame = openlist.get(0);
			
			up = frameMap.get(frame).get_up();
			down = frameMap.get(frame).get_down();
			if(up>0){
			search_up(frame,up-1,down);
			}
			if(down>0){
			search_down(frame,up,down-1);
			}
			openlist.remove(frame);
			closedlist.add(frame);

		}
		for(int i =0;i<closedlist.size();i++){
			System.out.println(closedlist.get(i).get_name());
			up = frameMap.get(closedlist.get(i)).get_up();
			down = frameMap.get(closedlist.get(i)).get_down();
			System.out.println("up:"+up+", down:"+down);
		}
	}
	
	private void search_up(AIFrame frame,int up,int down){
		//自分の親が登録されているかチェック
		if (!(frame.readSlotValue(AIFramesystem, "親", false) == null)){
			// 自分の親の名前をとってくる
		ArrayList<String> parentlist = frame.getmVals("親");
		for(int i=0; i < parentlist.size(); i++){
			AIFrame parentframe = AIFramesystem.get_Frame(parentlist.get(i));
			if(!openlist.contains(parentframe) & !closedlist.contains(parentframe)){
				openlist.add(parentframe);
				addFrame(parentframe,up,down);
			}
		}
		}
	}
	
	private void search_down(AIFrame frame,int up,int down){
		ArrayList<String> childlist = frame.get_leankers_Slot_names("親");
		for(int i=0; i < childlist.size(); i++){
			AIFrame childframe = AIFramesystem.get_Frame(childlist.get(i));
			if(!openlist.contains(childframe) & !closedlist.contains(childframe)){
				openlist.add(childframe);
				addFrame(childframe,up,down);
			}
		}
	}
	
	/**
	 * ビューに AIFrameSystem のフレームを加える
	 * @param node
	 */
	public void addFrame(AIFrame aIFrame,int up,int down) {
		UIFrame uIFrame = new UIFrame(aIFrame,up,down);
		if(aIFrame == center){
			uIFrame.setColor(Color.GREEN);
		}
		uIFrame.setVisible(true);
		mapPanel.add(uIFrame);
		frameMap.put(aIFrame, uIFrame);
	}
	
	
	//コマンドパネルを追加する部分
	public void setupCommodPanel(){
		commondPanel = new JPanel();
		list = new JList(data);
		textfield = new JTextField("人名");
		list.addListSelectionListener(this);
		button = new JButton("検索");
		button.addActionListener(this);
		 JScrollPane sp = new JScrollPane();
		    sp.getViewport().setView(list);
		    sp.setPreferredSize(new Dimension(100, 50));
		commondPanel.setLayout(new GridLayout(1, 3));
		commondPanel.add(textfield);
		commondPanel.add(sp);
		commondPanel.add(button);
		setupMapPanel();
		add(commondPanel ,BorderLayout.SOUTH);
	}
	
	//デモン手続きで親族を見つけその色を変える
	 public void search_demon(String demon){
		 for(int i=0; i<demonlist.size();i++){
			 if(AIFramesystem.get_Frame(demonlist.get(i))!=null){
			 AIFrame changeframe = AIFramesystem.get_Frame(demonlist.get(i));
			 frameMap.get(changeframe).setColor(Color.BLUE);;
			 }
		 }
		 demonlist = (ArrayList<String>) AIFramesystem.readSlotValue(center.get_name(), demon);
		 for(int i=0; i<demonlist.size();i++){
			 if(AIFramesystem.get_Frame(demonlist.get(i))!=null){
			 AIFrame changeframe = AIFramesystem.get_Frame(demonlist.get(i));
			 frameMap.get(changeframe).setColor(Color.RED);;
			 }
		 }
	 }

	 //登録されている人の名前をコンソールに列挙
	 public void print_human_name(){
		 ArrayList<String> list = AIFramesystem.get_Frame("人間").get_leankers_Slot_names("is-a");
		 for(int i =0; i<list.size();i++){
			 System.out.println(list.get(i));
		 }
	 }
	 
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		JList lst = (JList)e.getSource();
		//System.out.println(lst.getSelectedValue());
		search_demon((String) lst.getSelectedValue());
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//いま出ているフレームをリセット
		if(AIFramesystem.get_Frame((String)textfield.getText())==null){
			System.out.println("そのような名前のフレームは登録されていません\n以下の名前のフレームが存在します");
			print_human_name();
		}else{
		resetFrames();
		//新しいフレームをセット
		
		setupFrames(textfield.getText());
		repaint();
		}
	}

	public static void main(String[] args) {
		OurFrameSystem ofs = new OurFrameSystem();

	AIFrameGUIver0 gui = new AIFrameGUIver0(ofs);
	gui.setVisible(true);
}




}
