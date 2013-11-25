package graphicInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
 
@SuppressWarnings("serial")
public class HintTextField extends JTextField implements FocusListener {
 
    private String hint;
    Font hintFont;
    Font normalFont;
 
    HintTextField() {
        super(0);
    }
 
    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        this.hintFont = new Font("Segoe UI", Font.ITALIC, 12);
        this.normalFont = new Font("Segoe UI", Font.PLAIN, 12);
        super.addFocusListener(this);
        super.setForeground(Color.GRAY);
        super.setFont(hintFont);
    }
 
    @Override
    public void focusGained(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText("");
            super.setForeground(Color.BLACK);
            super.setFont(normalFont);
        }
    }
 
    @Override
    public void focusLost(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText(hint);
            Font hintFont = new Font("Segoe UI", Font.ITALIC, 12);
            super.setForeground(Color.GRAY);
            super.setFont(hintFont);
        }
    }
 
    @Override
    public String getText() {
        String typed = super.getText();
        return typed.equals(hint) ? "" : typed;
    }
}