package main.java.line;

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
import main.java.utils.MasoryUtil;
import main.java.utils.MyNotifier;

public class newLineView extends AnAction {

    private AnActionEvent anActionEvent;

    private final LineViewDialog.OnClickListener mClickListener = new LineViewDialog.OnClickListener() {


        @Override
        public void onGenerate(String nameStr, String radius, String bgcolor, String border, String border_color, String height) {
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


                String name = "";
                String superView = "contentView";
                if (nameStr.contains(".")) {
                    var nameArr = nameStr.split("\\.");
                    name = nameArr[0];
                    superView = MasoryUtil.getSuperViewTest(nameArr[1]);
                }

                int firstEndIndex = strContent.indexOf("@end");

                document.insertString(firstEndIndex - 1, "\n@property(nonatomic, strong) LineView *line" + name + ";");

                strContent = document.getText();
                int lastEndIndex = strContent.lastIndexOf("@end");
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("\n- (LineView *)line").append(name).append(" {\n");

                strBuilder.append("\tif (!_line").append(name).append("){\n");

                strBuilder.append("\t\t_line").append(name).append(" = [[LineView alloc] initWithFrame:CGRectZero];\n");

                if (!TextUtils.isBlank(bgcolor)) {
                    strBuilder.append("\t\t_line").append(name).append(".backgroundColor = ").append(CommonUtil.processColor(bgcolor)).append(";\n");
                }

                if (!TextUtils.isBlank(radius)) {
                    strBuilder.append("\t\t[_line").append(name).append(" corner_radius:kNum(").append(radius).append(")];\n");
                }

                if (!TextUtils.isBlank(border) && !TextUtils.isBlank(border_color)) {
                    strBuilder.append("\t\t[_line").append(name).append(" border:kNum(").append(border).append(") color:").append(CommonUtil.processColor(border_color)).append("];\n");
                }

                strBuilder.append("\t}\n");
                strBuilder.append("\treturn _line").append(name).append(";\n");
                strBuilder.append("}\n");

                document.insertString(lastEndIndex - 1, strBuilder.toString());


                strContent = document.getText();
                int index = CommonUtil.getIndexOfMethod(strContent, "\\(void\\)updateConstraints");

                strBuilder = new StringBuilder();
                strBuilder.append("\n\t[self.line").append(name).append(" mas_makeConstraints:^(MASConstraintMaker *make) {\n");
                strBuilder.append("\n\t\tmake.height.mas_equalTo(kNum(").append(height).append("));");
                strBuilder.append("\n\t}];\n");
                document.insertString(index - 1, strBuilder.toString());

                strContent = document.getText();
                index = CommonUtil.getIndexOfMethod(strContent, "\\(void\\)loadView");

                strBuilder = new StringBuilder();
                strBuilder.append("\n\t[self."+superView+" addSubview:self.line").append(name).append("];");
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

        LineViewDialog generateDialog = new LineViewDialog();
        generateDialog.setOnClickListener(mClickListener);
        generateDialog.setTitle("Generate Field By String");
        //自动调整对话框大小
        generateDialog.pack();
        //设置对话框跟随当前windows窗口
        generateDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(e.getProject()));
        generateDialog.setVisible(true);

    }


}
