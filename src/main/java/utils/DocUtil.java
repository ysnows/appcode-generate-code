package main.java.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.DocumentUtil;

import java.io.File;

public class DocUtil {


    public static Document getHeaderDocument(Document curDocument) {

        Document headerDocument;
        VirtualFile curVirtualFile = FileDocumentManager.getInstance().getFile(curDocument);

        var curFileExtension = curVirtualFile.getExtension();
        if ("m".equals(curFileExtension)) {
            headerDocument = curDocument;
        } else {
            var headerPath = curVirtualFile.getPath().replaceFirst("\\.m", "\\.h");
            var headerFile = new File(headerPath);
            var headerVirtualFile = LocalFileSystem.getInstance().findFileByIoFile(headerFile);
            headerDocument = FileDocumentManager.getInstance().getDocument(headerVirtualFile);
        }

        return headerDocument;
    }

    public static String getSuperClass(Document curDocument) {
        var headerDocument = DocUtil.getHeaderDocument(curDocument);
        var content = headerDocument.getText();

        var interfaceIndex = content.indexOf("@interface");
        var lineNumber = headerDocument.getLineNumber(interfaceIndex);
        var lineContent = headerDocument.getText(DocumentUtil.getLineTextRange(headerDocument, lineNumber));

        var arrOne = lineContent.split(":");
        var arrTwo = arrOne[1].split("<");
        var superClass = arrTwo[0];

        return superClass;

    }
}
