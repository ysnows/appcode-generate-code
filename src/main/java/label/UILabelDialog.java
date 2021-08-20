package main.java.label;

import org.apache.http.util.TextUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import main.java.base.BTextField;
import main.java.utils.CommonUtil;

public class UILabelDialog extends JDialog implements KeyListener, DocumentListener, FocusListener {
    private JPanel content;
    private JButton btnCancel, btnGenerate;
    private BTextField tfname;
    private BTextField tfFont;
    private BTextField tfColor;
    private BTextField tfText;
    private BTextField tfRadius;
    private BTextField tfBgColor;
    private BTextField tfBorder;
    private BTextField tfBorderColor;
    private BTextField tfNumberOfLines;
    private BTextField tfAlign;
    private JTextField tfMasory;
    private OnClickListener onClickListener;
    /**
     * 成员变量类型：private or public
     */
    private String memberType = "private";

    public UILabelDialog() {
        setContentPane(content);
        setModal(true);
        btnGenerate.addActionListener(e -> {
            if (onClickListener != null) {
                String name = CommonUtil.toUpperCase4Index(tfname.getText());
                onClickListener.onGenerate(name, tfFont.getText(), tfColor.getText(), tfText.getText(), tfNumberOfLines.getText(), tfAlign.getText(), tfRadius.getText(), tfBgColor.getText(), tfBorder.getText(), tfBorderColor.getText(),tfMasory.getText());
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
        tfNumberOfLines.addKeyListener(this);
        tfRadius.addKeyListener(this);
        tfBorder.addKeyListener(this);
        tfBorderColor.addKeyListener(this);
        tfAlign.addKeyListener(this);
        btnGenerate.addKeyListener(this);
        tfMasory.addKeyListener(this);

        tfMasory.addFocusListener(this);
        tfname.addFocusListener(this);
        tfFont.addFocusListener(this);
        tfColor.addFocusListener(this);
        tfText.addFocusListener(this);
        tfBgColor.addFocusListener(this);
        tfNumberOfLines.addFocusListener(this);
        tfRadius.addFocusListener(this);
        tfBorder.addFocusListener(this);
        tfBorderColor.addFocusListener(this);
        tfAlign.addFocusListener(this);
        btnGenerate.addFocusListener(this);


        //关键是下面这两行代码
        Document document = tfname.getDocument();
        document.addDocumentListener(this);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        System.out.println("insert text");

//        String text = "12|#2A2A2A,100%|136,12|联系电话：15098119021|0";
        var text = tfname.getText();
        if (text.contains("|")) {

            var strArr = text.split("\\|");

            var font = strArr[0];
            var colorStr = strArr[1];
            var colorArr = colorStr.split(",");
            var color = colorArr[0];

            var whStr = strArr[2];
            var whArr = whStr.split(",");
            var width = whArr[0];
            var height = whArr[1];

            var content = strArr[3];
            var radius = strArr[4];
            var bgColorStr = strArr[5];
            var bgColorArr = bgColorStr.split(",");
            var bgColor = bgColorArr[0];

            var borderWidth = strArr[6];

            var borderColorStr = strArr[7];
            var borderColorArr = borderColorStr.split(",");
            var borderColor = borderColorArr[0];


            if (!font.equals("0")) tfFont.setText(font);
            if (!color.equals("0")) tfColor.setText(color);

            if (!content.equals("0")) tfText.setText(content);
            if (!radius.equals("0")) tfRadius.setText(radius);
            if (!bgColor.equals("0")) tfBgColor.setText(bgColor);
            if (!borderWidth.equals("0")) tfBorder.setText(borderWidth);
            if (!borderColor.equals("0")) tfBorderColor.setText(borderColor);

            Timer timer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    tfname.setText("");
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        System.out.println("remove text");
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        System.out.println("change text");
    }


    @Override
    public void focusGained(FocusEvent focusEvent) {
        JTextField component = (JTextField) focusEvent.getComponent();
        component.selectAll();
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {

    }



    @Override
    public void keyTyped(KeyEvent keyEvent) {
        char keyChar = keyEvent.getKeyChar();
        if (keyChar == KeyEvent.VK_ENTER) {
            if (!TextUtils.isBlank(tfname.getText())) {
                if (onClickListener != null) {
                    String name = CommonUtil.toUpperCase4Index(tfname.getText());
                    onClickListener.onGenerate(name, tfFont.getText(), tfColor.getText(), tfText.getText(), tfNumberOfLines.getText(), tfAlign.getText(), tfRadius.getText(), tfBgColor.getText(), tfBorder.getText(), tfBorderColor.getText(), tfMasory.getText());
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
        void onGenerate(String nameStr, String font, String color, String text, String numberOfLines, String align, String radius, String bgcolor, String border, String border_color, String masory);

        void onCancel();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
