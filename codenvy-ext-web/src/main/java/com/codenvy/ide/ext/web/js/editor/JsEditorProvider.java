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
package com.codenvy.ide.ext.web.js.editor;

import com.codenvy.ide.api.editor.CodenvyTextEditor;
import com.codenvy.ide.api.editor.DocumentProvider;
import com.codenvy.ide.api.editor.EditorPartPresenter;
import com.codenvy.ide.api.editor.EditorProvider;
import com.codenvy.ide.api.notification.NotificationManager;
import com.google.inject.Inject;
import com.google.inject.Provider;


/**
 * EditorProvider for JavaScript css type
 *
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id:
 */
public class JsEditorProvider implements EditorProvider {
    private final DocumentProvider            documentProvider;
    private       Provider<CodenvyTextEditor> editorProvider;
    private final NotificationManager         notificationManager;

    /**
     * JS editor configuration.
     */
    private Provider<JsEditorConfiguration> jsEditorConfigurationProvider;

    /** @param documentProvider */
    @Inject
    public JsEditorProvider(DocumentProvider documentProvider,
                            Provider<CodenvyTextEditor> editorProvider,
                            NotificationManager notificationManager,
                            Provider<JsEditorConfiguration> jsEditorConfigurationProvider) {
        super();
        this.documentProvider = documentProvider;
        this.editorProvider = editorProvider;
        this.notificationManager = notificationManager;
        this.jsEditorConfigurationProvider = jsEditorConfigurationProvider;
    }

    /** {@inheritDoc} */
    @Override
    public EditorPartPresenter getEditor() {
        CodenvyTextEditor textEditor = editorProvider.get();
        textEditor.initialize(jsEditorConfigurationProvider.get(), documentProvider, notificationManager);
        return textEditor;
    }
}
