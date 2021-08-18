package main.java.model;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.wm.WindowManager;

public class newModel extends AnAction {

    private AnActionEvent anActionEvent;

    private final NewModelDialog.OnClickListener mClickListener = new NewModelDialog.OnClickListener() {


        @Override
        public void onGenerate(String str, String member) {
            WriteCommandAction.runWriteCommandAction(anActionEvent.getProject(), () -> {
                Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
                if (editor == null) {
                    return;
                }

                Document document = editor.getDocument();
                String strContent = document.getText();

                var parsedStr = ModelUtil.json(str,strContent);

                strContent = document.getText();
                int lastEndIndex = strContent.lastIndexOf("@end");

                document.insertString(lastEndIndex - 1, parsedStr);
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

        NewModelDialog generateDialog = new NewModelDialog();
        generateDialog.setOnClickListener(mClickListener);
        generateDialog.setTitle("Generate Field By String");
        //自动调整对话框大小
        generateDialog.pack();
        //设置对话框跟随当前windows窗口
        generateDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(e.getProject()));
        generateDialog.setVisible(true);

    }


}
