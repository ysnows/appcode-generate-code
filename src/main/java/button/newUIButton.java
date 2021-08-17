package main.java.button;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;

import org.apache.http.util.TextUtils;

import main.java.utils.CommonUtil;
import main.java.utils.MyNotifier;

public class newUIButton extends AnAction {

    private AnActionEvent anActionEvent;

    private final UIButtonDialog.OnClickListener mClickListener = new UIButtonDialog.OnClickListener() {


        @Override
        public void onGenerate(String name, String font, String color, String text, String radius, String bgcolor, String border, String border_color, String image, String imageHeight, String imageWidth, String imageSpacing, String imagePosition, String width, String height) {
            //获取当前编辑的文件
            PsiFile psiFile = anActionEvent.getData(LangDataKeys.PSI_FILE);
            if (psiFile == null) {
                MyNotifier.notifyError(anActionEvent.getProject(), "当前编辑文件不能为空！");
            }
            final String[] resultMessage = {"success"};
            WriteCommandAction.runWriteCommandAction(anActionEvent.getProject(), () -> {
                Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
                if (editor == null) {
                    resultMessage[0] = "Editor can not be null!";
                    return;
                }
                Document document = editor.getDocument();
                String strContent = document.getText();
                int firstEndIndex = strContent.indexOf("@end");

                document.insertString(firstEndIndex - 1, "\n@property(nonatomic, strong) BButton *btn" + name + ";");

                strContent = document.getText();
                int lastEndIndex = strContent.lastIndexOf("@end");
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("\n- (BButton *)btn").append(name).append(" {\n");

                strBuilder.append("\tif (!_btn").append(name).append("){\n");

                strBuilder.append("\t\t_btn").append(name).append(" = [[BButton alloc] initWithFrame:CGRectZero];\n");
                strBuilder.append("\t\t[_btn").append(name).append(" setTitleColor:").append(CommonUtil.processColor(color)).append(" forState:UIControlStateNormal];\n");
                strBuilder.append("\t\t[_btn").append(name).append(".titleLabel setFont:").append(CommonUtil.processFont(font)).append("];\n");
                strBuilder.append("\t\t[_btn").append(name).append(" setTitle:").append(CommonUtil.processText(text)).append(" forState:UIControlStateNormal];\n");

                if (!TextUtils.isBlank(image) || (!TextUtils.isBlank(imageWidth) && !TextUtils.isBlank(imageHeight))) {
                    strBuilder.append("\t\tUIImage *resizedImage = [[UIImage imageNamed:@\"").append(image).append("\"] qmui_imageResizedInLimitedSize:CGSizeMake(kNum(").append(imageWidth).append("), kNum(").append(imageHeight).append(")) resizingMode:QMUIImageResizingModeScaleAspectFill];\n");
                    strBuilder.append("\t\t[_btn").append(name).append(" setImage:resizedImage").append(" forState:UIControlStateNormal];\n");
                    if (!TextUtils.isBlank(imagePosition)) {
                        strBuilder.append("\t\t_btn").append(name).append(".imagePosition=QMUIButtonImagePosition").append(CommonUtil.toUpperCase4Index(imagePosition)).append(";\n");
                    }
                    if (!TextUtils.isBlank(imageSpacing)) {
                        strBuilder.append("\t\t_btn").append(name).append(".spacingBetweenImageAndTitle=kNum(").append(imageSpacing).append(");\n");
                    }
                }


                if (!TextUtils.isBlank(bgcolor)) {
                    strBuilder.append("\t\t[_btn").append(name).append(" setBackgroundColor:").append(CommonUtil.processColor(bgcolor)).append("];\n");
                } else {
                    strBuilder.append("\t\t[_btn").append(name).append(" setBackgroundColor:").append("UIColor.clearColor").append("];\n");
                }

                if (!TextUtils.isBlank(radius) && !radius.equals("0")) {
                    strBuilder.append("\t\t_btn").append(name).append(".cornerRadius=kNum(").append(radius.equals("00") ? "QMUIButtonCornerRadiusAdjustsBounds" : radius).append(");\n");
//                    strBuilder.append("\t\t[_btn").append(name).append(" corner_radius:kNum(").append(radius).append(")];\n");
                }

                if (!TextUtils.isBlank(border) && !TextUtils.isBlank(border_color)) {
                    strBuilder.append("\t\t[_btn").append(name).append(" border:kNum(").append(border).append(") color:").append(CommonUtil.processColor(border_color)).append("];\n");
                }

                strBuilder.append("\t}\n");
                strBuilder.append("\treturn _btn").append(name).append(";\n");
                strBuilder.append("}\n");

                document.insertString(lastEndIndex - 1, strBuilder.toString());


                strContent = document.getText();
                int index = CommonUtil.getIndexOfMethod(strContent, "\\(void\\)updateConstraints");

                strBuilder = new StringBuilder();
                strBuilder.append("\n\t[self.btn").append(name).append(" mas_makeConstraints:^(MASConstraintMaker *make) {\n");

                if (!TextUtils.isBlank(width) && !TextUtils.isBlank(height)) {
                    strBuilder.append("\n\t\tmake.height.mas_equalTo(kNum(").append(height).append("));");
                    strBuilder.append("\n\t\tmake.width.mas_equalTo(kNum(").append(width).append("));");
                }

                strBuilder.append("\n\t}];\n");
                document.insertString(index - 1, strBuilder.toString());

                strContent = document.getText();
                index = CommonUtil.getIndexOfMethod(strContent, "\\(void\\)loadView");

                strBuilder = new StringBuilder();
                strBuilder.append("\n\t[self.contentView addSubview:self.btn").append(name).append("];");
                document.insertString(index - 1, strBuilder.toString());


            });
        }

        @Override
        public void onCancel() {
            //nothing...
        }
    };


    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        this.anActionEvent = e;
//        Messages.showMessageDialog("hello", "Error", Messages.getInformationIcon());
//        MyNotifier.notifyError(e.getProject(),"Hello");

        UIButtonDialog generateDialog = new UIButtonDialog();
        generateDialog.setOnClickListener(mClickListener);
        generateDialog.setTitle("Generate Field By String");
        //自动调整对话框大小
        generateDialog.pack();
        //设置对话框跟随当前windows窗口
        generateDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(e.getProject()));
        generateDialog.setVisible(true);

    }


}
