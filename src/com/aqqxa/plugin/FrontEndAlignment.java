package com.aqqxa.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FrontEndAlignment implements ApplicationComponent {
    public FrontEndAlignment() {
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "FrontEndAlignment";
    }

    public void alignText(Editor editor) {
        // String regex = Messages.showInputDialog(editor.getContentComponent(), "Enter regex to align with:", "Front End Alignment", null);

        SelectionModel selectionModel = editor.getSelectionModel();
        if (selectionModel.getSelectedText() == null) {
            return;
        }

        Document document = editor.getDocument();
        final int startOffset = document.getLineStartOffset(document.getLineNumber(selectionModel.getSelectionStart()));
        final int endOffset = document.getLineEndOffset(document.getLineNumber(selectionModel.getSelectionEnd()));
        final String text = document.getText(new TextRange(startOffset, endOffset));
        String regex = determineRegex(text);

        final String newText = (new RegexTextAligner(text, regex)).alignText();

        final Editor editorArg = editor;
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                editorArg.getDocument().replaceString(startOffset, endOffset, newText);
            }
        });
    }

    private String determineRegex(String text) {
        Map<String, Integer> counter = new HashMap<>();
        counter.put(":", 0);
        counter.put("=", 0);
        counter.put(",", 0);
        String[] lines = text.split("\n");
        for (String line : lines) {
            for (String key : counter.keySet()) {
                if(line.contains(key)){
                    counter.put(key, counter.get(key) + 1);
                }
            }
        }

        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : counter.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        return maxEntry == null ? ":" : maxEntry.getKey();
    }
}
