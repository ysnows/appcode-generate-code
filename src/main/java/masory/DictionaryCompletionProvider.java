package main.java.masory;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.ProcessingContext;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author jansorg
 */
class DictionaryCompletionProvider extends CompletionProvider<CompletionParameters> {
    private final boolean onlyManual;

    /**
     * @param onlyManual if true, then completions are only returned when the user manually requested it
     */
    DictionaryCompletionProvider(boolean onlyManual) {
        this.onlyManual = onlyManual;
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        if (parameters.isAutoPopup() && onlyManual) {
            return;
        }


        // make sure that our prefix is the last word
        // for plain text file, all the content up to the caret is the prefix
        // we don't want that, because we're only completing a single word
        CompletionResultSet dictResult;
//        int lastSpace = prefix.lastIndexOf(' ');
//        if (lastSpace >= 0 && lastSpace < prefix.length() - 1) {
//            prefix = prefix.substring(lastSpace + 1);
//            dictResult = result.withPrefixMatcher(prefix);
//        } else {
        dictResult = result;
//        }

//        int length = prefix.length();
//        char firstChar = prefix.charAt(0);
//        boolean isUppercase = Character.isUpperCase(firstChar);

        process(parameters, dictResult);

    }

    private void process(CompletionParameters parameters, CompletionResultSet dictResult) {
        LookupElementBuilder element;

        var editor = parameters.getEditor();
        var document = editor.getDocument();

        var curOffset = editor.getCaretModel().getOffset();
        var lineNumber = document.getLineNumber(curOffset);
        var lineStartOffset = document.getLineStartOffset(lineNumber);
        var lineEndOffset = document.getLineEndOffset(lineNumber);
        var curLineText = document.getText(TextRange.create(lineStartOffset, lineEndOffset)).trim();

        System.out.println(curLineText);
        var needCompletion = MasoryUtil.isNeedCompletion(curLineText);
        System.out.println(needCompletion);
        if (needCompletion) {
            var propertyList = MasoryUtil.getPropertyList(document.getText());

//        if (isUppercase) {
//            element = LookupElementBuilder.create(word.substring(0, 1).toUpperCase() + word.substring(1));
//        } else {

            for (String property : propertyList) {
                ProgressManager.checkCanceled();
                element = LookupElementBuilder.create(property);
                dictResult.addElement(element);
            }
        }
    }
}
