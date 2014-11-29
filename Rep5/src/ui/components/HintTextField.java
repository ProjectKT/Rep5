package ui.components;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

public class HintTextField extends JTextField implements FocusListener {
	private String hint;
	private Color hintForeground;
	private Color textForeground;
	private boolean hintShown;
	
	public HintTextField() {
		this("");
	}
	public HintTextField(String hint) {
		this(hint, null);
	}
	public HintTextField(String hint, Color hintForeground) {
		super(hint);
		setHint(hint);
		setHintForeground(hintForeground);
		hintShown = true;
		
		addFocusListener(this);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		hideHint();
	}

	@Override
	public void focusLost(FocusEvent e) {
		showHint();
	}
	
	@Override
	public void setForeground(Color color) {
		super.setForeground(color);
		textForeground = color;
	}
	@Override
	public Color getForeground() {
		return textForeground;
	}
	
	@Override
	public String getText(int offs, int len) throws BadLocationException {
		return hintShown ? "" : super.getText(offs, len);
	}
	
	@Override
	public String getText() {
		return hintShown ? "" : super.getText();
	}
	
	public void setHint(String hint) {
		this.hint = hint;
	}
	public String getHint() {
		return hint;
	}
	
	public void setHintForeground(Color color) {
		hintForeground = color == null ? getForeground() : color;
	}
	public Color getHintForeground() {
		return hintForeground;
	}
	
	private void showHint() {
		if (!hintShown && getText().isEmpty()) {
			hintShown = true;
			super.setText(hint);
			super.setForeground(hintForeground);
		}
	}
	private void hideHint() {
		if (hintShown) {
			super.setText("");
			super.setForeground(textForeground);
			hintShown = false;
		}
	}

}
