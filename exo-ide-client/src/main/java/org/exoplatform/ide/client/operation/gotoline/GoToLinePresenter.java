/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package org.exoplatform.ide.client.operation.gotoline;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;

import org.exoplatform.gwtframework.commons.util.BrowserResolver;
import org.exoplatform.gwtframework.commons.util.BrowserResolver.Browser;
import org.exoplatform.gwtframework.ui.client.api.TextFieldItem;
import org.exoplatform.gwtframework.ui.client.dialog.Dialogs;
import org.exoplatform.ide.client.IDE;
import org.exoplatform.ide.client.framework.control.Docking;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler;
import org.exoplatform.ide.client.framework.editor.event.EditorGoToLineEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorSetFocusEvent;
import org.exoplatform.ide.client.framework.ui.api.IsView;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedEvent;
import org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler;
import org.exoplatform.ide.editor.client.api.Editor;
import org.exoplatform.ide.vfs.client.model.FileModel;

/**
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
 */
public class GoToLinePresenter implements EditorActiveFileChangedHandler, GoToLineHandler, ViewClosedHandler {

    public interface Display extends IsView {

        TextFieldItem getLineNumber();

        HasClickHandlers getGoButton();

        HasClickHandlers getCancelButton();

        void setCaptionLabel(String caption);

        void removeFocusFromLineNumber();

        void setFocusInLineNumberField();

    }

    /* Error messages */
    private static final String LINE_OUT_OF_RANGE = IDE.ERRORS_CONSTANT.goToLineLineNumberOutOfRange();

    private static final String CANT_PARSE_LINE_NUMBER = IDE.ERRORS_CONSTANT.goToLineCantParseLineNumber();

    private Display display;

    private int maxLineNumber;

    private HandlerRegistration keyUpHandler;

    private final Browser currentBrowser = BrowserResolver.CURRENT_BROWSER;

    private Editor activeEditor;

    public GoToLinePresenter() {
        IDE.getInstance().addControl(new GoToLineControl());
        IDE.getInstance().addControl(new CursorPositionControl(), Docking.STATUSBAR_RIGHT);

        IDE.addHandler(GoToLineEvent.TYPE, this);
        IDE.addHandler(EditorActiveFileChangedEvent.TYPE, this);
        IDE.addHandler(ViewClosedEvent.TYPE, this);
    }

    /** @see org.exoplatform.ide.client.edit.event.GoToLineHandler#onGoToLine(org.exoplatform.ide.client.edit.event.GoToLineEvent) */
    @Override
    public void onGoToLine(GoToLineEvent event) {
        if (display != null) {
            return;
        }

        display = GWT.create(Display.class);
        IDE.getInstance().openView(display.asView());
        bindDisplay();
        display.setFocusInLineNumberField();
    }

    public void bindDisplay() {
        display.getCancelButton().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent arg0) {
                closeView();
            }
        });

        display.getGoButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (display.getLineNumber().getValue() != null && !"".equals(display.getLineNumber().getValue())) {
                    goToLine();
                }
            }
        });

        display.getLineNumber().addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13)
                    goToLine();
            }
        });

        maxLineNumber = activeEditor.getDocument().getNumberOfLines();
        String labelCaption = IDE.EDITOR_CONSTANT.goToLineLabelEnterLineNumber(maxLineNumber);
        display.setCaptionLabel(labelCaption);
    }

    /**
     *
     */
    private void goToLine() {
        String lineString = display.getLineNumber().getValue();
        try {
            int line = Integer.parseInt(lineString);
            if (line > 0 && line <= maxLineNumber) {
                closeView();
                IDE.fireEvent(new EditorGoToLineEvent(line));
            } else {
                display.removeFocusFromLineNumber();
                Dialogs.getInstance().showError(LINE_OUT_OF_RANGE);
            }
        } catch (NumberFormatException e) {
            display.removeFocusFromLineNumber();
            Dialogs.getInstance().showError(CANT_PARSE_LINE_NUMBER);
        }
    }

    private void closeView() {
        IDE.getInstance().closeView(display.asView().getId());
        IDE.fireEvent(new EditorSetFocusEvent());
    }

    /** @see org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler#onEditorActiveFileChanged(org.exoplatform
     * .ide.client.framework.editor.event.EditorActiveFileChangedEvent) */
    @Override
    public void onEditorActiveFileChanged(EditorActiveFileChangedEvent event) {
        this.activeEditor = event.getEditor();
    }

    /** @see org.exoplatform.ide.client.framework.ui.api.event.ViewClosedHandler#onViewClosed(org.exoplatform.ide.client.framework.ui.api
     * .event.ViewClosedEvent) */
    @Override
    public void onViewClosed(ViewClosedEvent event) {
        if (event.getView() instanceof Display) {
            display = null;
        }
    }

    private native int getLineNumber(String content) /*-{
        if (!content) return 1;

        // test if content is not ended with line break
        if (content.charAt(content.length - 1) !== "\n") {
            return content.split("\n").length;
        }

        // in the Internet Explorer editor.setCode("\n") is displayed as 2 lines
        if (this.@org.exoplatform.ide.client.operation.gotoline.GoToLinePresenter::currentBrowser == @org.exoplatform.gwtframework.commons.util.BrowserResolver.Browser::IE) {
            return content.split("\n").length;
        }

        return content.split("\n").length - 1;
    }-*/;

}
