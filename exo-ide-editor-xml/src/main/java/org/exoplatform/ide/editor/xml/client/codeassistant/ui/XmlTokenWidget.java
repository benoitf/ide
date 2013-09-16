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
package org.exoplatform.ide.editor.xml.client.codeassistant.ui;

import com.google.gwt.user.client.ui.*;

import org.exoplatform.ide.editor.api.codeassitant.Token;
import org.exoplatform.ide.editor.api.codeassitant.TokenProperties;
import org.exoplatform.ide.editor.api.codeassitant.ui.TokenWidget;
import org.exoplatform.ide.editor.xml.client.XmlEditorExtension;

/**
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: HtmlTokenWidget Feb 23, 2011 1:12:49 PM evgen $
 */
public class XmlTokenWidget extends TokenWidget {

    private Grid grid;

    /** @param token */
    public XmlTokenWidget(Token token) {
        super(token);
        grid = new Grid(1, 3);
        grid.setStyleName(XmlEditorExtension.RESOURCES.css().item());
        grid.setWidth("100%");

        Image i = getImage();
        i.setHeight("16px");
        grid.setWidget(0, 0, i);

        String name = token.getName();
        if (token.hasProperty(TokenProperties.SHORT_HINT)) {
            String hint = token.getProperty(TokenProperties.SHORT_HINT).isStringProperty().stringValue();
            Label hintLabel = new Label(hint, false);
            grid.setWidget(0, 2, hintLabel);

        }

        Label nameLabel = new Label(name, false);

        grid.setWidget(0, 1, nameLabel);

        grid.getCellFormatter().setWidth(0, 0, "16px");
        grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
        grid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT);
        grid.getCellFormatter().setWidth(0, 2, "100%");

        initWidget(grid);
    }

    /** @return  */
    private Image getImage() {
        switch (token.getType()) {
            case ATTRIBUTE:
                return new Image(XmlEditorExtension.RESOURCES.attribute());

            case TAG:
            default:
                return new Image(XmlEditorExtension.RESOURCES.tag());
        }

    }

    /** @see org.exoplatform.ide.editor.api.codeassitant.ui.TokenWidget#getTokenValue() */
    @Override
    public String getTokenValue() {
        if (token.hasProperty(TokenProperties.CODE))
            return token.getProperty(TokenProperties.CODE).isStringProperty().stringValue();
        else
            return token.getName();
    }

    /** @see org.exoplatform.ide.editor.api.codeassitant.ui.TokenWidget#getTokenDecription() */
    @Override
    public Widget getTokenDecription() {
        if (token.hasProperty(TokenProperties.FULL_TEXT)) {
            Widget w = new SimplePanel();
            w.getElement().setInnerHTML(token.getProperty(TokenProperties.FULL_TEXT).isStringProperty().stringValue());
            return w;
        }
        return null;
    }

    /** @see org.exoplatform.ide.client.framework.codeassistant.TokenWidget#getTokenName() */
    @Override
    public String getTokenName() {
        return getToken().getName();
    }

    /** @see org.exoplatform.ide.client.framework.codeassistant.TokenWidget#setSelectedStyle() */
    @Override
    public void setSelectedStyle() {
        setStyleName(XmlEditorExtension.RESOURCES.css().selectedItem());
    }

    /** @see org.exoplatform.ide.client.framework.codeassistant.TokenWidget#setDefaultStyle() */
    @Override
    public void setDefaultStyle() {
        setStyleName(XmlEditorExtension.RESOURCES.css().item());
    }

}
