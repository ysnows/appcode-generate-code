package main.java.editItem;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;

import main.java.utils.CommonUtil;
import main.java.utils.MasoryUtil;
import main.java.utils.MyNotifier;

public class newEditItemView extends AnAction {

    private AnActionEvent anActionEvent;

    private final EditItemViewDialog.OnClickListener mClickListener = new EditItemViewDialog.OnClickListener() {


        @Override
        public void onGenerate(String nameStr, String font, String color, String text, String subFont, String subColor, String subText, String editable, String arrow, String line, String height, String masory) {
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

                String name = nameStr;
                String superView = "contentView";
                if (nameStr.contains(".")) {
                    var nameArr = nameStr.split("\\.");
                    name = nameArr[0];
                    superView = MasoryUtil.getSuperViewTest(nameArr[1]);
                }

                int firstEndIndex = strContent.indexOf("@end");

                document.insertString(firstEndIndex - 1, "\n@property(nonatomic, strong) EditItemView *edit" + name + ";");

                strContent = document.getText();
                int lastEndIndex = strContent.lastIndexOf("@end");
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("\n- (EditItemView *)edit").append(name).append(" {\n");

                strBuilder.append("\tif (!_edit").append(name).append("){\n");

                strBuilder.append("\t\t_edit").append(name).append(" = [[EditItemView alloc] initWithViewModel:self.vm title:@\"").append(text).append("\" titleFont:").append(CommonUtil.processFont(font)).append(" titleColor:").append(CommonUtil.processColor(color)).append(" placeholder:@\"").append(subText).append("\" subTitleFont:").append(CommonUtil.processFont(subFont)).append(" subTitleColor:").append(CommonUtil.processColor(subColor)).append(" desc:@\"\" arrow:").append(CommonUtil.processBoolean(arrow)).append(" editable:").append(CommonUtil.processBoolean(editable)).append(" line:").append(CommonUtil.processBoolean(line)).append("];\n");

                strBuilder.append("\t}\n");
                strBuilder.append("\treturn _edit").append(name).append(";\n");
                strBuilder.append("}\n");

                document.insertString(lastEndIndex - 1, strBuilder.toString());

                strContent = document.getText();
                int index = CommonUtil.getEndIndexOfMethod(strContent, "\\(void\\)updateConstraints");

                strBuilder = new StringBuilder();
                strBuilder.append("\n\t[self.edit").append(name).append(" mas_makeConstraints:^(MASConstraintMaker *make) {\n");
                strBuilder.append("\n\t\tmake.height.mas_equalTo(kNum(");
                strBuilder.append(height).append("));");

                var parsedMasory = MasoryUtil.parseMasory(masory);
                strBuilder.append(parsedMasory);

                strBuilder.append("\n\t}];\n");

                document.insertString(index - 1, strBuilder.toString());

                strContent = document.getText();
                index = CommonUtil.getEndIndexOfMethod(strContent, "\\(void\\)addView");

                strBuilder = new StringBuilder();
                strBuilder.append("\n\t[self." + superView + " addSubview:self.edit").append(name).append("];");
                document.insertString(index - 1, strBuilder.toString());

                MasoryUtil.moveCaretToMasoryLine(editor, document, name);

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

        EditItemViewDialog generateDialog = new EditItemViewDialog();
        generateDialog.setOnClickListener(mClickListener);
        generateDialog.setTitle("Generate Field By String");
        //自动调整对话框大小
        generateDialog.pack();
        //设置对话框跟随当前windows窗口
        generateDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(e.getProject()));
        generateDialog.setVisible(true);

    }


}
