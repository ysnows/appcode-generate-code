package main.java.api;

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

import main.java.utils.MyNotifier;

public class newApi extends AnAction {

    private AnActionEvent anActionEvent;

    private final NewApiDialog.OnClickListener mClickListener = new NewApiDialog.OnClickListener() {


        @Override
        public void onGenerate(String str, String apiType, String model) {
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

                //1. 接口定义 2. 接口返回 3. modelClass 4. 请求参数
                var list = ApiUtil.apiParse(str, model, apiType);

                if (list == null) {
                    MyNotifier.notifyError(anActionEvent.getProject(), "解析失败");
                    return;
                }

                Document document = editor.getDocument();
                String strContent = document.getText();


                //当前文件添加方法实现
                int lastEndIndex = strContent.lastIndexOf("@end");
                document.insertString(lastEndIndex - 1, list.get(1));

                //头文件添加请求方法
                var path = psiFile.getVirtualFile().getPath();
                var hPath = path.replaceFirst("\\.m", "\\.h");
                var hFile = new File(hPath);

                if (hFile.exists()) {

                    var hVFile = LocalFileSystem.getInstance().findFileByIoFile(hFile);
                    var hDocumentFile = FileDocumentManager.getInstance().getDocument(hVFile);
                    var apiContent = hDocumentFile.getText();
                    if (!apiContent.contains(list.get(2))) {
                        if (apiType.equals("normal")) {
                            var lastIndex = apiContent.lastIndexOf("@end");
                            hDocumentFile.insertString(lastIndex - 1, "\n- (void)" + list.get(2) + ";");
                        } else if (apiType.equals("list")) {
                            apiContent = hDocumentFile.getText();
                            var index = apiContent.indexOf("@interface");
                            var lineNumber = document.getLineNumber(index);
                            var endOffset = document.getLineEndOffset(lineNumber + 1);
                            hDocumentFile.insertString(endOffset + 1, list.get(3));
                        }
                    }
                }


                //文件添加接口
                var project = anActionEvent.getProject();
                var projectFilePath = project.getBasePath();

                var file = new File(projectFilePath + "/aiyunji/Classes/Tools/Macros/ApiConfig.h");
                if (file.exists()) {

                    var vFile = LocalFileSystem.getInstance().findFileByIoFile(file);
                    var documentFile = FileDocumentManager.getInstance().getDocument(vFile);
                    var apiContent = documentFile.getText();
                    if (!apiContent.contains(list.get(2))) {
                        var lastIndex = apiContent.lastIndexOf("#endif");
                        documentFile.insertString(lastIndex - 1, list.get(0));
                    }
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

        NewApiDialog generateDialog = new NewApiDialog();
        generateDialog.setOnClickListener(mClickListener);
        generateDialog.setTitle("Generate Field By String");
        //自动调整对话框大小
        generateDialog.pack();
        //设置对话框跟随当前windows窗口
        generateDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(e.getProject()));
        generateDialog.setVisible(true);

    }

}
