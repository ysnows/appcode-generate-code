package main.java.model;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class NewModelDialog extends JDialog {
    private JPanel content;
    private JTextArea txtPasteStr;
    private JButton btnCancel, btnGenerate;
    private OnClickListener onClickListener;
    private JRadioButton rbPublic, rbPrivate;
    /**
     * 成员变量类型：private or public
     */
    private String memberType = "private";

    public NewModelDialog() {
        setContentPane(content);
        setModal(true);
        btnGenerate.addActionListener(e -> {
            if (onClickListener != null) {
                onClickListener.onGenerate(txtPasteStr.getText(), memberType);
            }
            dispose();
        });
        btnCancel.addActionListener(e -> {
            if (onClickListener != null) {
                onClickListener.onCancel();
            }
            dispose();
        });

        rbPublic.addChangeListener(e -> {
            if (rbPublic.isSelected()) {
                memberType = "public";
                rbPrivate.setSelected(!rbPublic.isSelected());
            }
        });

        rbPrivate.addChangeListener(e -> {
            if (rbPrivate.isSelected()) {
                rbPublic.setSelected(!rbPrivate.isSelected());
                memberType = "private";
            }
        });
    }

    public interface OnClickListener {
        void onGenerate(String str, String member);

        void onCancel();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
