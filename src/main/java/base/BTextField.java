package main.java.base;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class BTextField extends JTextField implements FocusListener {

    public BTextField() {
    }

    public BTextField(String text) {
        super(text);
        init();
    }


    public BTextField(int columns) {
        super(columns);
        init();
    }

    public BTextField(String text, int columns) {
        super(text, columns);
        init();
    }

    public BTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        init();
    }

    private void init() {
//        addFocusListener(this);

    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        this.selectAll();
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {

    }
}
