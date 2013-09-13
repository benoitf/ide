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
package org.exoplatform.ide.editor.codeassistant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: CodeAssistantClientBundle Feb 22, 2011 5:06:30 PM evgen $
 */
public interface CodeAssistantClientBundle extends ClientBundle {
    CodeAssistantClientBundle INSTANCE = GWT.create(CodeAssistantClientBundle.class);

    @Source("org/exoplatform/ide/editor/client/bundle/codeassistant.css")
    CodeAssistantCss css();

}
