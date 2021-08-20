package main.java.editItem;

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

public class EditItemViewDialog extends JDialog implements KeyListener, DocumentListener, FocusListener {
    private JPanel content;
    private JButton btnCancel, btnGenerate;
    private BTextField tfname;
    private BTextField tfFont;
    private BTextField tfColor;
    private BTextField tfText;
    private BTextField tfSubText;
    private BTextField tftfEditable;
    private BTextField tfArrow;
    private BTextField tfLine;
    private BTextField tfSubFont;
    private BTextField tfSubColor;
    private BTextField tfHeight;
    private JTextField tfMasory;
    private OnClickListener onClickListener;
    /**
     * 成员变量类型：private or public
     */
    private String memberType = "private";

    public EditItemViewDialog() {
        setContentPane(content);
        setModal(true);
        btnGenerate.addActionListener(e -> {
            if (onClickListener != null) {
                String name = CommonUtil.toUpperCase4Index(tfname.getText());
                onClickListener.onGenerate(name, tfFont.getText(), tfColor.getText(), tfText.getText(), tfSubFont.getText(), tfSubColor.getText(), tfSubText.getText(), tftfEditable.getText(), tfArrow.getText(), tfLine.getText(), tfHeight.getText(), tfMasory.getText());
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
        tftfEditable.addKeyListener(this);
        tfSubFont.addKeyListener(this);
        tfSubText.addKeyListener(this);
        tfArrow.addKeyListener(this);
        tfLine.addKeyListener(this);
        tfSubColor.addKeyListener(this);
        btnGenerate.addKeyListener(this);
        tfHeight.addKeyListener(this);
        tfMasory.addKeyListener(this);

        tfMasory.addFocusListener(this);
        tfHeight.addFocusListener(this);
        tfname.addFocusListener(this);
        tfFont.addFocusListener(this);
        tfColor.addFocusListener(this);
        tfText.addFocusListener(this);
        tftfEditable.addFocusListener(this);
        tfSubFont.addFocusListener(this);
        tfSubText.addFocusListener(this);
        tfArrow.addFocusListener(this);
        tfLine.addFocusListener(this);
        tfSubColor.addFocusListener(this);
        btnGenerate.addFocusListener(this);


        //关键是下面这两行代码
        Document document = tfname.getDocument();
        document.addDocumentListener(this);

        tfMasory.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                var text = tfMasory.getText();
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


                    if (!font.equals("0")) tfSubFont.setText(font);
                    if (!color.equals("0")) tfSubColor.setText(color);
                    if (!content.equals("0")) tfSubText.setText(content);

                    Timer timer = new Timer(100, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            tfMasory.setText("");
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {

            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }
        });
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
            if (!height.equals("0")) tfHeight.setText(height);

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
                    onClickListener.onGenerate(name, tfFont.getText(), tfColor.getText(), tfText.getText(), tfSubFont.getText(), tfSubColor.getText(), tfSubText.getText(), tftfEditable.getText(), tfArrow.getText(), tfLine.getText(), tfHeight.getText(), tfMasory.getText());
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
        void onGenerate(String nameStr, String font, String color, String text, String subFont, String subColor, String subText, String editable, String arrow, String line, String height, String masory);

        void onCancel();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
