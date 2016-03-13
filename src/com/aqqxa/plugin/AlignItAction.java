package com.aqqxa.plugin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import org.jetbrains.annotations.Nullable;

public class AlignItAction extends EditorAction {
    public AlignItAction() {
        super(new EditorActionHandler() {
            @Override
            protected void doExecute(Editor editor, @Nullable Caret caret, DataContext dataContext) {
                Application application = ApplicationManager.getApplication();
                FrontEndAlignment alignment = application.getComponent(FrontEndAlignment.class);
                if (alignment != null)
                    alignment.alignText(editor);
            }
        });
    }
}