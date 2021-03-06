package main.java.api;

import org.apache.http.util.TextUtils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class NewApiDialog extends JDialog implements KeyListener {
    private JPanel content;
    private JButton btnCancel, btnGenerate;
    private OnClickListener onClickListener;
    private JRadioButton rbPublic, rbList;
    private JTextField tfModel;
    private JTextField tfCurl;
    /**
     * 成员变量类型：private or public
     */
    private String apiType = "normal";

    public NewApiDialog() {
        setContentPane(content);
        setModal(true);
        btnGenerate.addActionListener(e -> {
            if (onClickListener != null) {
                onClickListener.onGenerate(tfCurl.getText(), apiType, tfModel.getText());
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
                apiType = "public";
                rbList.setSelected(!rbPublic.isSelected());
            }
        });

        rbList.addChangeListener(e -> {
            if (rbList.isSelected()) {
                apiType = "list";
            } else {
                apiType = "normal";
            }
        });

        tfCurl.addKeyListener(this);
        tfModel.addKeyListener(this);
        rbList.addKeyListener(this);

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        char keyChar = keyEvent.getKeyChar();
        if (keyChar == KeyEvent.VK_ENTER) {
            if (!TextUtils.isBlank(tfCurl.getText())) {
                if (onClickListener != null) {
                    onClickListener.onGenerate(tfCurl.getText(), apiType, tfModel.getText());
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
        void onGenerate(String str, String member, String model);

        void onCancel();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
