package main.java.button;

import org.apache.http.util.TextUtils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.java.utils.CommonUtil;

public class UIButtonDialog extends JDialog implements KeyListener {
    private JPanel content;
    private JButton btnCancel, btnGenerate;
    private JTextField tfname;
    private JTextField tfFont;
    private JTextField tfColor;
    private JTextField tfText;
    private JTextField tfRadius;
    private JTextField tfBgColor;
    private JTextField tfBorder;
    private JTextField tfBorderColor;
    private OnClickListener onClickListener;
    /**
     * 成员变量类型：private or public
     */
    private String memberType = "private";

    public UIButtonDialog() {
        setContentPane(content);
        setModal(true);
        btnGenerate.addActionListener(e -> {
            if (onClickListener != null) {
                String name = CommonUtil.toUpperCase4Index(tfname.getText());
                onClickListener.onGenerate(name, tfFont.getText(), tfColor.getText(), tfText.getText(), tfRadius.getText(), tfBgColor.getText(), tfBorder.getText(), tfBorderColor.getText());
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
        tfFont.addKeyListener(this);
        tfColor.addKeyListener(this);
        tfText.addKeyListener(this);
        tfBgColor.addKeyListener(this);
        tfRadius.addKeyListener(this);
        tfBorder.addKeyListener(this);
        tfBorderColor.addKeyListener(this);
        btnGenerate.addKeyListener(this);


    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        char keyChar = keyEvent.getKeyChar();
        if (keyChar == KeyEvent.VK_ENTER) {
            if (!TextUtils.isBlank(tfname.getText())) {
                if (onClickListener != null) {
                    String name = CommonUtil.toUpperCase4Index(tfname.getText());
                    onClickListener.onGenerate(name, tfFont.getText(), tfColor.getText(), tfText.getText(), tfRadius.getText(), tfBgColor.getText(), tfBorder.getText(), tfBorderColor.getText());
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


    public interface OnClickListener {
        void onGenerate(String name, String font, String color, String text, String radius, String bgcolor, String border, String border_color);

        void onCancel();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
