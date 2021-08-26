package main.java.vmfield;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;

import java.io.File;

import main.java.utils.CommonUtil;
import main.java.utils.MyNotifier;

public class newVmField extends AnAction {

    private AnActionEvent anActionEvent;

    private final NewVmFieldDialog.OnClickListener mClickListener = new NewVmFieldDialog.OnClickListener() {


        @Override
        public void onGenerate(String str, String observe, String bind) {
            WriteCommandAction.runWriteCommandAction(anActionEvent.getProject(), () -> {
                //获取当前编辑的文件
                PsiFile psiFile = anActionEvent.getData(LangDataKeys.PSI_FILE);
                if (psiFile == null) {
                    MyNotifier.notifyError(anActionEvent.getProject(), "当前编辑文件不能为空！");
                    return;
                }

                Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
                if (editor == null) {
                    return;
                }
                var parsedTextList = FieldParseUtil.parse(str);
                if (parsedTextList == null || parsedTextList.size() < 3) {
                    return;
                }

                //头文件添加属性
                Document headerContent = null;
                var path = psiFile.getVirtualFile().getPath();
                if (path.endsWith("VM.m")) {
                    var hPath = path.replaceFirst("\\.m", "\\.h");
                    var hFile = new File(hPath);
                    if (hFile.exists()) {
                        var hVFile = LocalFileSystem.getInstance().findFileByIoFile(hFile);
                        var hDocumentFile = FileDocumentManager.getInstance().getDocument(hVFile);
                        headerContent = hDocumentFile;
                    }
                } else if (path.endsWith("VM.h")) {
                    headerContent = editor.getDocument();
                } else if (path.endsWith("View.m")) {
                    var hPath = path.replaceFirst("View\\.m", "VM\\.h");
                    var hFile = new File(hPath);
                    if (hFile.exists()) {
                        var hVFile = LocalFileSystem.getInstance().findFileByIoFile(hFile);
                        var hDocumentFile = FileDocumentManager.getInstance().getDocument(hVFile);
                        headerContent = hDocumentFile;
                    }
                }

                if (headerContent != null) {
                    String strContent = headerContent.getText();

                    int lineEndIndex = CommonUtil.getNextLineIndexOfString(strContent, "@interface", headerContent);
                    headerContent.insertString(lineEndIndex + 1, parsedTextList.get(0));
                }

                //View文件添加监听方法

                Document viewDocument = null;
                if (path.endsWith("View.m")) {
                    viewDocument = editor.getDocument();
                } else {
                    var viewPath = path.replaceFirst(path.endsWith(".m") ? "VM\\.m" : "VM\\.h", "View\\.m");

                    var viewFile = new File(viewPath);
                    if (viewFile.exists()) {
                        var viewVFile = LocalFileSystem.getInstance().findFileByIoFile(viewFile);
                        viewDocument = FileDocumentManager.getInstance().getDocument(viewVFile);
                    }
                }

                if (observe.equals("yes")) {

                    var endIndexOfMethod = CommonUtil.getEndIndexOfMethod(viewDocument.getText(), "\\(void\\)setupVm");
                    viewDocument.insertString(endIndexOfMethod - 1, parsedTextList.get(1));

                }
                if (bind.equals("yes")) {

                    var endIndexOfMethod = CommonUtil.getEndIndexOfMethod(viewDocument.getText(), "\\(void\\)setupVm");
                    viewDocument.insertString(endIndexOfMethod - 1, parsedTextList.get(2));

                }


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

        NewVmFieldDialog generateDialog = new NewVmFieldDialog();
        generateDialog.setOnClickListener(mClickListener);
        generateDialog.setTitle("Generate Field By String");
        //自动调整对话框大小
        generateDialog.pack();
        //设置对话框跟随当前windows窗口
        generateDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(e.getProject()));
        generateDialog.setVisible(true);

    }


}
