package ui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

public class AIFrameLayoutver0 extends MapLayout {

	// レイアウトスレッド
	private Thread frameLayoutThread;
	
	
	/**
	 * この LayoutManager が取り付けられている先の AIFramePanel を返すメソッド
	 */
	@Override
	protected AIFramePanelver0 getMapPanel() {
		return (AIFramePanelver0) super.getMapPanel();
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
