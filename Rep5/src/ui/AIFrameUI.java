package ui;

import java.util.*;
import java.awt.HeadlessException;
import java.util.ArrayList;

import javax.swing.JFrame;

import ui.components.AIFramePanel;
import ui.components.UIFrame;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import Frame.AIFrame;
import Frame.AIFrameSystem;
import Frame.OurFrameSystem;

public class AIFrameUI extends JFrame {
	
	// --- ビューのメンバ ---
	private AIFramePanel mapPanel;
	
	// --- ロジックのメンバ ---
	private AIFrameSystem aIFramesystem;

	public AIFrameUI(AIFrameSystem aIFramesystem) throws HeadlessException {
		this.aIFramesystem = aIFramesystem;
		initialize();
		setVisible(true);
	}

	/**
	 * ビューを初期化する
	 */
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 300, 200);
		setTitle("AIFrameUI");
		
		setupMapPanel();
		setupFrames();
	}
	/**
	 * MapPanel を初期化する
	 */
	private void setupMapPanel() {
		mapPanel = new AIFramePanel();
		MapDragListener dl = new MapDragListener(mapPanel);
		mapPanel.addMouseListener(dl);
		mapPanel.addMouseMotionListener(dl);
		MapZoomListener zl = new MapZoomListener(mapPanel);
		mapPanel.addMouseWheelListener(zl);
		getContentPane().add(mapPanel);
	}
	
	/**
	 * AIFrameSystem 内のフレームを addFrame
	 */
	private void setupFrames() {
		ArrayList<AIFrame> frames = aIFramesystem.getFrames();
		HashMap<String,AIFrame> dic = aIFramesystem.getmFrames();
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
	
	public ArrayList<AIFrame> getFirstGene(ArrayList<AIFrame> list){
		ArrayList<AIFrame> result = new ArrayList<AIFrame>();
		for(AIFrame frame: list){
			if (frame.readSlotValue(this.aIFramesystem, "親", false) == null) {
				result.add(frame);
			}
		}
		
		return(result);
	}
	
	
	public static void main(String[] args) {
		OurFrameSystem ofs = new OurFrameSystem();
		
		
		AIFrameUI gui = new AIFrameUI(ofs);
		gui.setVisible(true);
	}
}
