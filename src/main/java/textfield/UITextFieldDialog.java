package main.java.textfield;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.java.utils.CommonUtil;

public class UITextFieldDialog extends JDialog {
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

    public UITextFieldDialog() {
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

    }

    public interface OnClickListener {
        void onGenerate(String name, String font, String color, String text, String radius, String bgcolor, String border, String border_color);

        void onCancel();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
