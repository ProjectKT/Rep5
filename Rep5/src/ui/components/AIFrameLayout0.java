package ui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

public class AIFrameLayout0 extends MapLayout {

	// レイアウトスレッド
	private Thread frameLayoutThread;
	
	//一番上の世代(祖父、祖母の世代)の数
	private int count1;
	
	//二番目に上の世代(親、叔父、叔母の世代)の数
	private int count2;
	
	//中心の世代(自分、従兄弟の世代)の数
	private int count3;
	
	//二番目に下の世代(子、甥、姪の世代)の数
	private int count4;
	
	//一番下の世代(孫の世代)の数
	private int count5;
	
	/**
	 * この LayoutManager が取り付けられている先の AIFramePanel を返すメソッド
	 */
	@Override
	protected AIFramePanel0 getMapPanel() {
		return (AIFramePanel0) super.getMapPanel();
	}
	

	@Override
	public void removeLayoutComponent(Component comp) {
		System.out.println("removeLayoutComponent");
		if (comp instanceof UIFrame) {
			getMapPanel().frameMap.remove(((UIFrame) comp).getFrame());
		}
	}
	
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		System.out.println("preferredLayoutSize");
		return super.preferredLayoutSize(parent);
	}
	
	@Override
	public void layoutContainer(Container parent) {
		System.out.println("layoutContainer");
		super.layoutContainer(parent);
	}


	@Override
	public void addLayoutComponent(String name, Component comp) {
		System.out.println("addLayoutComponent");
		if (comp instanceof UIFrame) {
			getMapPanel().frameMap.put(((UIFrame) comp).getFrame(), (UIFrame) comp);
			layoutUIFrame(name, (UIFrame) comp);

		} 
	}

	private void layoutUIFrame(String name, UIFrame comp) {
		switch (comp.get_down() - comp.get_up()){
		//２個下の世代
		case -2:	comp.setCenter(0.0,200.0);	break;
		//１個下の世代
		case -1:	comp.setCenter(0.0,100.0);	break;
		//同世代
		case 0:		comp.setCenter(0.0,0.0);	break;
		//１個上の世代
		case 1:		comp.setCenter(0.0,-100.0);	break;
		//２個上の世代
		case 2:		comp.setCenter(0.0,-200.0);	break;
		};
	}
	
	
}
