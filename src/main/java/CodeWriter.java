package main.java;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author gaok
 * @description
 * @date 2018/02/07 11:18
 */
public class CodeWriter {

    private static CodeWriter INSTANCE;

    public static CodeWriter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CodeWriter();
        }
        return INSTANCE;
    }

    String write(String pastedStr, AnActionEvent event, String type, ICodeGenerator codeGenerator) {
        //获取当前编辑的文件
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
        if (psiFile == null) {
            return "当前编辑文件不能为空！";
        }
        final String[] resultMessage = {"success"};
        WriteCommandAction.runWriteCommandAction(event.getProject(), () -> {
            Editor editor = event.getData(PlatformDataKeys.EDITOR);
            if (editor == null) {
                resultMessage[0] = "Editor can not be null!";
                return;
            }
            Document document = editor.getDocument();
            String text = document.getText();
            int firstEndIndex = text.indexOf("@end");

            document.insertString(firstEndIndex - 1, "\n@property(nonatomic, strong) BButton *btnNext;");

            text = document.getText();
            int lastEndIndex = text.lastIndexOf("@end");
            document.insertString(lastEndIndex - 1, "\n- (BButton *)btnNext {\n" +
                    "    if (!_btnNext) {\n" +
                    "        _btnNext = [[BButton alloc] initWithFrame:CGRectZero];\n" +
                    "        [_btnNext.titleLabel setFont:FONT(15)];\n" +
                    "        [_btnNext setTitle:@\"下一步\" forState:UIControlStateNormal];\n" +
                    "        [_btnNext setTitleColor:UIColor.whiteColor forState:UIControlStateNormal];\n" +
                    "        [_btnNext setBackgroundColor:COLOR_PRIMARY_BUTTON];\n" +
                    "        [_btnNext corner_radius:kNum(19.5)];\n" +
                    "\n" +
                    "    }\n" +
                    "    return _btnNext;\n" +
                    "}");


            Project project = editor.getProject();
            if (project == null) {
                resultMessage[0] = "当前工程不能为空！";
                return;
            }
            //获取当前编辑的class对象
            PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
//            PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();


//            PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
//            if (psiClass == null) {
//                resultMessage[0] = "必须先新建class文件！";
//                return;
//            }
//            if (psiClass.getNameIdentifier() == null) {
//                return;
//            }
//            try {
//                codeGenerator.onSplice(codeGenerator.onParse(pastedStr), project, psiClass, type);
//            } catch (Exception e) {
//                resultMessage[0] = e.getMessage() + "\n" + "请检查复制的文本格式是否正确！";
//            }
        });
        return resultMessage[0];
    }
}
