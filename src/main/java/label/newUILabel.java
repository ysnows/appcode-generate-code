package main.java.label;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;

import org.apache.http.util.TextUtils;

import main.java.utils.CommonUtil;
import main.java.utils.MyNotifier;

public class newUILabel extends AnAction {

    private AnActionEvent anActionEvent;

    private final UILabelDialog.OnClickListener mClickListener = new UILabelDialog.OnClickListener() {


        @Override
        public void onGenerate(String name, String font, String color, String text, String radius, String bgcolor, String border, String border_color) {
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

                document.insertString(firstEndIndex - 1, "\n@property(nonatomic, strong) BLabel *label" + name + ";");

                strContent = document.getText();
                int lastEndIndex = strContent.lastIndexOf("@end");
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("\n- (BLabel *)label").append(name).append(" {\n");

                strBuilder.append("\tif (!_label").append(name).append("){\n");

                strBuilder.append("\t\t_label").append(name).append(" = [[BLabel alloc] initWithFrame:CGRectZero];\n");
                strBuilder.append("\t\t_label").append(name).append(".textAlignment = NSTextAlignmentCenter;\n");
                strBuilder.append("\t\t_label").append(name).append(".textColor = ").append(CommonUtil.processColor(color)).append(";\n");
                strBuilder.append("\t\t_label").append(name).append(".font = ").append(CommonUtil.processFont(font)).append(";\n");
                strBuilder.append("\t\t_label").append(name).append(".text = ").append(CommonUtil.processText(text)).append(";\n");
                strBuilder.append("\t\t_label").append(name).append(".numberOfLines = 1;\n");

                if (!TextUtils.isBlank(bgcolor)) {
                    strBuilder.append("\t\t_label").append(name).append(".backgroundColor = ").append(CommonUtil.processColor(bgcolor)).append(";\n");
                } else {
                    strBuilder.append("\t\t_label").append(name).append(".backgroundColor = ").append("UIColor.clearColor").append(";\n");
                }

                if (!TextUtils.isBlank(radius)) {
                    strBuilder.append("\t\t[_label").append(name).append(" corner_radius:kNum(").append(radius).append(")];\n");
                }

                if (!TextUtils.isBlank(border) && !TextUtils.isBlank(border_color)) {
                    strBuilder.append("\t\t[_label").append(name).append(" border:kNum(").append(border).append(") color:").append(CommonUtil.processColor(border_color)).append("];\n");
                }

                strBuilder.append("\t}\n");
                strBuilder.append("\treturn _label").append(name).append(";\n");
                strBuilder.append("}\n");

                document.insertString(lastEndIndex - 1, strBuilder.toString());


                strContent = document.getText();
                int index = CommonUtil.getIndexOfMethod(strContent, "\\(void\\)updateConstraints");

                strBuilder = new StringBuilder();
                strBuilder.append("\n\t[self.label").append(name).append(" mas_makeConstraints:^(MASConstraintMaker *make) {\n");
                strBuilder.append("}];\n");
                document.insertString(index - 1, strBuilder.toString());

                strContent = document.getText();
                index = CommonUtil.getIndexOfMethod(strContent, "\\(void\\)loadView");

                strBuilder = new StringBuilder();
                strBuilder.append("\n\t[self.contentView addSubview:self.label").append(name).append("];");
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

        UILabelDialog generateDialog = new UILabelDialog();
        generateDialog.setOnClickListener(mClickListener);
        generateDialog.setTitle("Generate Field By String");
        //自动调整对话框大小
        generateDialog.pack();
        //设置对话框跟随当前windows窗口
        generateDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(e.getProject()));
        generateDialog.setVisible(true);

    }


}
