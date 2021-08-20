package main.java.line;

import org.apache.http.util.TextUtils;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.java.utils.CommonUtil;

public class LineViewDialog extends JDialog implements KeyListener, FocusListener {
    private JPanel content;
    private JButton btnCancel, btnGenerate;
    private JTextField tfname;
    private JTextField tfRadius;
    private JTextField tfBgColor;
    private JTextField tfBorder;
    private JTextField tfBorderColor;
    private JTextField tfHeight;
    private JTextField tfMasory;
    private OnClickListener onClickListener;
    /**
     * 成员变量类型：private or public
     */
    private String memberType = "private";

    public LineViewDialog() {
        setContentPane(content);
        setModal(true);
        btnGenerate.addActionListener(e -> {
            if (onClickListener != null) {
                String name = CommonUtil.toUpperCase4Index(tfname.getText());
                onClickListener.onGenerate(name, tfRadius.getText(), tfBgColor.getText(), tfBorder.getText(), tfBorderColor.getText(), tfHeight.getText(),tfMasory.getText());
            }
            dispose();
        });
        btnCancel.addActionListener(e -> {
            if (onClickListener != null) {
                onClickListener.onCancel();
            }
            dispose();
        });


        tfname.addKeyListener(this);
        tfHeight.addKeyListener(this);
        tfBgColor.addKeyListener(this);
        tfRadius.addKeyListener(this);
        tfBorder.addKeyListener(this);
        tfBorderColor.addKeyListener(this);
        btnGenerate.addKeyListener(this);
        tfMasory.addKeyListener(this);

        tfMasory.addFocusListener(this);
        tfname.addFocusListener(this);
        tfHeight.addFocusListener(this);
        tfBgColor.addFocusListener(this);
        tfRadius.addFocusListener(this);
        tfBorder.addFocusListener(this);
        tfBorderColor.addFocusListener(this);
        btnGenerate.addFocusListener(this);

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        char keyChar = keyEvent.getKeyChar();
        if (keyChar == KeyEvent.VK_ENTER) {
            if (!TextUtils.isBlank(tfname.getText())) {
                if (onClickListener != null) {
                    String name = CommonUtil.toUpperCase4Index(tfname.getText());
                    onClickListener.onGenerate(name, tfRadius.getText(), tfBgColor.getText(), tfBorder.getText(), tfBorderColor.getText(), tfHeight.getText(), tfMasory.getText());
                    dispose();
                }
            } else {

            }
        } else if (keyChar == KeyEvent.VK_ESCAPE) {
            dispose();
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        JTextField component = (JTextField) focusEvent.getComponent();
        component.selectAll();
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {

    }

    public interface OnClickListener {
        void onGenerate(String nameStr, String radius, String bgcolor, String border, String border_color, String height, String masory);

        void onCancel();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
