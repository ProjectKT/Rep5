package ui;

import java.awt.HeadlessException;
import java.util.ArrayList;

import javax.swing.JFrame;

import Frame.*;
import SemanticNet.Node;
import ui.components.AIFrameLayout;
import ui.components.AIFramePanel;
import ui.components.MapPanel;
import ui.components.SemanticNetPanel;
import ui.components.UIFrame;
import ui.components.UINode;
import ui.components.input.MapDragListener;
import ui.components.input.MapZoomListener;
import Frame.*;

public class AIFrameUI extends JFrame {
	
	// --- ビューのメンバ ---
	private AIFramePanel mapPanel;
	
	// --- ロジックのメンバ ---
	private AIFrameSystem aIFramesystem;

	public AIFrameUI(AIFrameSystem aIFramesystem) throws HeadlessException {
		this.aIFramesystem = aIFramesystem;
		initialize();
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 300, 200);
		setTitle("AIFrameUI");
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
<<<<<<< HEAD
		ArrayList<AIFrame> frames = aIFramesystem.getFrames();
		for (AIFrame frame : frames) {
			addFrame(frame);
		}
=======
//		ArrayList<AIFrame> frames = AIFrameSystem.getFrames();
//		for (AIFrame frame : frames) {
//			addFrame(frame);
//		}
>>>>>>> branch 'master' of https://github.com/ProjectKT/Rep5.git
	}
	
	/**
	 * ビューに AIFrameSystem のフレームを加える
	 * @param node
	 */
	public void addFrame(AIFrame aIFrame) {
		mapPanel.add(new UIFrame(aIFrame));
	}
	
	public static void main(String[] args) {
		OurFrameSystem ofs = new OurFrameSystem();
		
		AIFrameUI gui = new AIFrameUI(ofs);
		gui.setVisible(true);
	}
}
