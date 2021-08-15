package main.java.masory;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;

import org.apache.http.util.TextUtils;

import main.java.utils.CommonUtil;
import main.java.utils.MyNotifier;

public class masory extends AnAction {

    private AnActionEvent anActionEvent;


    public void onGenerate() {
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

            SelectionModel selectionModel = editor.getSelectionModel();
            CaretModel caretModel = editor.getCaretModel();

            int offset = caretModel.getOffset();
            int lineNumber = document.getLineNumber(offset);
            int lineEndOffset = document.getLineEndOffset(lineNumber);
            int lineStartOffset = document.getLineStartOffset(lineNumber);
            String curLineText = document.getText(TextRange.create(lineStartOffset, lineEndOffset));

            String pureText = curLineText.trim();

            var masory = MasoryUtil.parseMasory(pureText);

            document.deleteString(lineStartOffset, lineEndOffset);
            document.insertString(lineStartOffset, masory);


        });
    }


    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        this.anActionEvent = e;
//        Messages.showMessageDialog("hello", "Error", Messages.getInformationIcon());
//        MyNotifier.notifyError(e.getProject(),"Hello");

        onGenerate();

    }


}
